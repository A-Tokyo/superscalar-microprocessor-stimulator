package memoryHierarchy;
import utils.Utils;

public class MemoryHierarchy {
	public Cache[] caches;
	public Memory memory;
	/* cache description is an array of cache descriptions
	 * each descriptions string is as follows: cacheSize,lineSize,m,writePolicyHit,writePolicyMiss,cacheAccessCycles
	 */
	public MemoryHierarchy(int memoryAccessTime, int cacheLevels, String[] cacheDescription) {
		this.caches = new Cache[cacheLevels + 1];
		this.memory = new Memory(memoryAccessTime);
		int currCache = 0;
		for (int i = 0; i < cacheDescription.length; i++) {
			String [] cacheInfo = cacheDescription[i].split(",");			
			int cacheSize = Integer.parseInt(cacheInfo[0]);
			int lineSize = Integer.parseInt(cacheInfo[1]);
			int m = Integer.parseInt(cacheInfo[2]);
			String writePolicyHit = cacheInfo[3];
			String writePolicyMiss = cacheInfo[4];
			int cacheAccessCycles = Integer.parseInt(cacheInfo[5]);
			if(i == 0) {
				// Basic state, if no additional caches were added these will be the only 2 caches as per description
				// Instruction Cache
				this.caches[currCache] = new Cache(cacheSize / 2, lineSize, m, writePolicyHit, writePolicyMiss, cacheAccessCycles);
				// Data Cache
				this.caches[currCache+1] = new Cache(cacheSize / 2, lineSize, m, writePolicyHit, writePolicyMiss, cacheAccessCycles);
				currCache += 2;
			}
			else {
				// Creating a new cache for each extra cache level
				this.caches[currCache] = new Cache(cacheSize, lineSize, m, writePolicyHit, writePolicyMiss, cacheAccessCycles);
				currCache++;
			}
		}
	}
	
	/* This one takes an address in string form
	 * Returns A string represents the block where the byte resides in within cache level 1
	 * The Data is returned only when the cycles required to access it are finished
	 * other wise the method returns null since the data was not yet accessed
	 */
	public String loadData(String address) {
		this.caches[1].decrementAccessCyclesRemaining();
		if(this.caches[1].getAccessCyclesRemaining() == 0) {
			this.caches[1].setBeingAccessed(false);
			//reset cycle counter
			this.caches[1].resetAccessCyclesRemaining();
			return caches[1].read(address);
		}
		else {
			// the cache has to be marked as being accessed
			this.caches[1].setBeingAccessed(true);
			// returning null till the cycles remaining reach zero
			return null;
		}
	}
	
	/* This one takes an address in string form
	 * It returns the Data associated with that address
	 * The method works as follows, It loops through the caches in a non decreasing order till it gets a hit, or it accesses the memory
	 * Once The above condition is satisfied, the data is cached to the lower cache in the caches list 
	 * Then the loop goes back to that cache, and cashes the data in the cache preceiding it. This goes on till the level one cache has the data cached in it
	 * The data is then returned from the level one cache as planned 
	 */
	private String readAndCacheData(String address) {
		Block toCache;
		boolean reversing = false;

		for (int i = 1; i <= this.caches.length; i++) {
			if (i == this.caches.length) {
				// the data is not any any cache, read it from memory
				toCache = readFromMemory(address);
				String indexBits = this.caches[i-1].getIndexBits(address);
				writeBlock(toCache, indexBits, i - 1); // write block to the level where it missed
				i-=2;
				reversing = true;
			} else {
				if (caches[i].hit(address)) {
					// if the block exists in the current cache
					if(!reversing) {
						caches[i].incrementTotalHits();
						reversing = true;
					}
					if (i != 1) {
						// Read the data from the cache below
						toCache = readFromCacheBelow(i, address, false);	// Read block from lower level
						String indexBits = this.caches[i-1].getIndexBits(address);
						writeBlock(toCache, indexBits, i - 1); // write block to the level where it missed
						i-=2;
					} else {
						// If the current cache is the 1st Main cache
						// all the caches have been successfully updated with the data
						return caches[i].read(address);
					}
				}  else {
					if(!reversing){
						caches[i].incrementTotalMisses();	
					}
				}
			}
		}
		//		System.out.println("el donya kharbana khales w ana 3ayz anam b2a, 3ayz anam ba2a 3ashan khatry msh 3ayz ashofk");
		return null;
	}
	
	/* This one takes as input a block to write and a string representation of the index bits and an integer specifying the cache level
	 * It writes the given block in the cache, The replacement policy used is random replacement
	 * If a block is dirty, it acts according to the write police write through or write back
	 */
	public void writeBlock(Block blockToWrite, String indexBits, int cacheLevel) {
		int index;
		if(indexBits == null || indexBits.equals("")){
			// since this is fully associative there is only one set
			index = 0;
		}
		else{
			index = Integer.parseInt(indexBits, 2);	
		}
		Set setToWriteTo = this.caches[cacheLevel].sets[index];
		for(int i = 0; i < setToWriteTo.blocks.length; i++) {
			if (setToWriteTo.blocks[i].getValidBit() == 0) {
				// An invalid block was found and replaced, terminate
				setToWriteTo.blocks[i] = blockToWrite;
				return;
			}
		}
		int toReplaceBlockIndex = (int) (this.caches[cacheLevel].getM() * Math.random());
		Block blockToReplace = setToWriteTo.blocks[toReplaceBlockIndex];
		if (setToWriteTo.blocks[toReplaceBlockIndex].getDirtyBit() == 1) {
			// block address: First byte of the block to be replaced
			String blockToReplaceAddress = blockToReplace.getTag() + indexBits + (Utils.generateMask(Utils.getWordSizeInBits()-blockToReplace.getTag().length()-indexBits.length()));
			if(cacheLevel == 0)
				replaceBlock(setToWriteTo.blocks[toReplaceBlockIndex], blockToReplaceAddress, cacheLevel + 2);
			else
				replaceBlock(setToWriteTo.blocks[toReplaceBlockIndex], blockToReplaceAddress, cacheLevel + 1);

		}
		setToWriteTo.blocks[toReplaceBlockIndex] = blockToWrite;
		return;
	}
	
	/* This one takes as input a block toReplace and a string representing its address, also an integer representing the cache level to interactr with
	 * The Replacement policy used is random replacement, If the block toReplace is dirty, It is responsible for writing the data using 
	 * the specified writing policy for each cache
	 */
	private void replaceBlock(Block blockToReplace, String blockToReplaceAddress, int cacheLevel) {
		// Base Case
		// Write the replaced dirty block in cacheLevel or memory
		if(cacheLevel == this.caches.length) { // If in memory
			int address = Integer.parseInt(blockToReplaceAddress,2);
			for(int i = 0; i < this.caches[cacheLevel-1].getLineSize(); i++) { //Write block byte by byte to memory
				this.memory.writeToMemory(address, blockToReplace.data[i]);
				address++;
			}
		} else {
			Cache cacheToWriteTo = this.caches[cacheLevel];
			String blockAddress = blockToReplaceAddress;
			for(int i = 0; i < this.caches[cacheLevel -1].getLineSize(); i++) {
				// Write the line byte by byte
				cacheToWriteTo.writeByte(blockToReplaceAddress, blockToReplace.data[i]);
				// Increment the address by one decimal in preperation for the next byte
				blockToReplaceAddress = Utils.decimalToBinary(1+Integer.parseInt(blockToReplaceAddress, 2));
			}
			if (this.caches[cacheLevel].writePolicyHit.equals("writeThrough")){
				// recursively write through	
				replaceBlock(blockToReplace, blockAddress, cacheLevel + 1);
			}
		}

	}
	
	/*
	 * This one takes inputs an integer representing the cache index in the array of THIS instance, A string representing the address to read 
	 * and a boolean representing weather the block returned is an instruction
	 * It reads the data corresponding to this address from the current cache and returns it in a block compatible with the cache below it
	 * It returns a block compatible with the cache below this cache
	 */
	public Block readFromCacheBelow(int cacheIndex, String address, boolean isInstruction) {
		//boolean instructionOrNot ==> Reading an instruction
		// Reads a block from cache, corresponding to an address
		Cache cacheToReadFrom = this.caches[cacheIndex]; // The cache data is read from
		int lineSize;
		// This condition checks if the below cache is the main I cache, if so the line size is mutated to be compatible with it.
		if (isInstruction && cacheIndex==2){
			lineSize = this.caches[0].getLineSize(); //Size of block in the cache data will be written to	
		}
		else{
			lineSize = this.caches[cacheIndex-1].getLineSize();	
		}
		Block toCache = new Block(lineSize); // Block to be returned
		int byteIndex = Integer.parseInt(address, 2) % lineSize; // Index of the byte in the block to be returned
		int startAddress = Integer.parseInt(address, 2) - byteIndex; // The address of the first byte in the block to be returned
		for(int i = 0; i < toCache.data.length; i++) {
			// Reading byte by byte
			String binaryAddress = Utils.decimalToBinary(startAddress + i);
			toCache.data[i] = cacheToReadFrom.read(binaryAddress);
		}
		toCache.setTag(this.caches[cacheIndex-1].getTagBits(address));
		toCache.setValidBit(1);
		return toCache;
	}
	
	/* This one takes an address in string form and returns the block corresponding to that address from the Main memory
	 * The block returned has to be the same size as the last cache level's line size to be able to insert it there
	 * So the memory is to be divided into memorySize/lineSize Data Blocks to match the last level.
	 */
	public Block readFromMemory(String address) {
		int currBlockSize = this.caches[this.caches.length - 1].getLineSize(); //Size of block in last level
		Block toCache = new Block(currBlockSize); // Block to be cached
		int byteIndex = Integer.parseInt(address, 2) % currBlockSize;
		// to get the byte address I subtract the byte index in decimal from the address in decimal
		int startAddress = Integer.parseInt(address, 2) - byteIndex; // The address of the first byte in the block to be returned
		for(int i = 0; i < toCache.data.length; i++) {
			// Memory is byte addressable, load the bites into the block toCache
			toCache.data[i] = this.memory.ReadFromMemory(startAddress + i);
		}
		// Configure the new cached block attributes
		toCache.setValidBit(1);
		toCache.setTag(this.caches[this.caches.length-1].getTagBits(address));
		return toCache;
	}
	
	/* This one takes 2 String address and data as inputs
	 * It first checks if the data is cached in the top level cache if so, it writes and keeps on writing to the cache levels below until a cache with WB policy is encountered
	 * If the data is not cached in the first level, the data is first read and cached in the lower level caches
	 */
	public void write(String address, String data) {
		if (caches[1].hit(address)) {
			caches[1].incrementTotalHits();
			writeToCacheLevel(1, address, data);
		}
		else {
			// Now there is a cache miss in level one which means I need to read the block before I write the byte
			readAndCacheData(address);
			writeToCacheLevel(1, address, data);
		}
	}

	/* This one takes the cache level and 2 strings one representing the address and the other representing the data
	 * It writes the data in the block corresponding to the address in the current cache
	 * If the hit write policy is write through a recursive call takes place to continue writing to the lower level cache,
	 * until it either writes in the memory or encounters a cache with write back policy
	 */
	public void writeToCacheLevel(int cacheLevel, String address, String data) {
		if(cacheLevel == this.caches.length) {
			int addressValue =  Integer.parseInt(address,2);
			memory.writeToMemory(addressValue, data);
			return;
		}
		this.caches[cacheLevel].writeByte(address, data);
		if (this.caches[cacheLevel].getWritePolicyHit().equals("writeThrough")) {
			// if the write policy is write through, write through to the next cache
			writeToCacheLevel(cacheLevel + 1, address, data);
		}
	}
	
	/* This one takes an address in string form
	 * It returns the Data associated with that address
	 * This one works the same ways as readAndCacheData, i
	 */
	public  String readAndCacheInstruction(String address) {
		Block toCache;
		int higherLevelCacheIndex;
		for (int i = 0; i <= this.caches.length; i++) {
			// skipping main D cache
			if(i == 2)
				higherLevelCacheIndex = 0;
			else
				higherLevelCacheIndex = i-1;
			if (i != 1) { // Skip Data cache
				if (i == this.caches.length) {
					toCache = readFromMemory(address);
					String indexBits = this.caches[higherLevelCacheIndex].getIndexBits(address);
					writeBlock(toCache, indexBits, higherLevelCacheIndex);
					i-=2;
					if(i == 0)
						i = -1;
				} else {
					if (caches[i].hit(address)) {
						if (i!=0) {
							toCache = readFromCacheBelow(i, address, true);	// Read block from lower level
							String indexBits = this.caches[higherLevelCacheIndex].getIndexBits(address);
							writeBlock(toCache, indexBits, higherLevelCacheIndex); // write block to the level where it missed
							i-=2;
							if(i == 0)
								i = -1;
						} else {
							// If the current cache is the 1st Main cache
							// all the caches have been successfully updated with the data
							return caches[i].read(address);
						}
					}
				}
			}
		}
		return null;
	}

	
}
