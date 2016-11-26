package memoryHierarchy;

public class Cache {
	private int size; // size of cache
	private int lineSize; // line size of cache 
	private int m; // associativity
	private String writePolicyHit; // writeThrough or writeBack
	private String writePolicyMiss; // writeThrough or writeBack
	private int accessCycles; // access time (in cycles)
	
	Set [] sets; // array of sets containing blocks, 1 set if fully Associative, same as number of blocks if direct mapped
	
	private int totalHits; // cache hits
	private int totalMisses; // cache misses
	
	// tracking cache state
	private boolean isBeingAccessed; // Data is currently being accessed from a D-Cache
	private int accessCyclesRemaining; // tracks the number of cycles remaining complete instruction fetch.


	private boolean isBeingFetched; // An instruction is currently being fetched from an I-Cache
	private int fetchCyclesRemaining; // tracks the number of cycles remaining complete instruction fetch.
	
	private static final int wordSizeInBits = 16;
	
	public Cache(int size, int lineSize, int m, String writePolicyHit, String writePolicyMiss, int accessCycles) {
		this.size = size;
		this.lineSize = lineSize;
		this.m = m;
		this.writePolicyHit = writePolicyHit;
		this.writePolicyMiss = writePolicyMiss;
		this.accessCycles = accessCycles;
		
		this.isBeingAccessed = false;
		
		// size over line size to get number of blocks in cache, /m since there are m blocks per set.
		this.sets = new Set[size/lineSize/m];
		for (int i=0; i < this.sets.length; i++) {
			// each set is a new set of size m and lineSize lineSize
			this.sets[i] = new Set(m, lineSize);
		}
	}
	
	// cache Logic
	// This one takes a string address and data, it writes the data to that address (byte addressable memory)
	public void writeByte(String address, String data) {
		Set setToWriteTo;
		Block blockToWriteTo;
		String tagBits = getTagBits(address);
		String offsetBits = getOffsetBits(address);
		int offset = Integer.parseInt(offsetBits, 2);
		if(getIndexBitCount() == 0) {
			// since this is fully associative there is only one set
			setToWriteTo = this.sets[0];
		}
		else {
			int index = Integer.parseInt(getIndexBits(address), 2);
			setToWriteTo = this.sets[index];
		}
		for (int i = 0; i < setToWriteTo.blocks.length; i++) {
			if (tagBits.equals(setToWriteTo.blocks[i].getTag()) && setToWriteTo.blocks[i].getValidBit() == 1) {
				blockToWriteTo = setToWriteTo.blocks[i];
				// write the byte
				blockToWriteTo.data[offset] = data;
				// in case of write back mark the block as dirty to write it back
				if (writePolicyHit.equals("writeBack"))
					blockToWriteTo.setDirtyBit(1);
			}
		}
	}
	
	
	/*This one takes a block and a decimal index and writes the block in the set belonging to that index
	 * It first I search for an invalid block, if found, I replace that block
	 * If no invalid blocks exist, a random block is replaced
	 * of course in case of direct mapped the block of the index is replaced since the set would have 1 block
	 */
//	public void writeBlock(Block block, int index) {
//		Set toWriteTo = this.sets[index];
//		for(int i = 0; i < toWriteTo.blocks.length; i++) {
//			if (toWriteTo.blocks[i].getValidBit() == 0) {
//				toWriteTo.blocks[i] = block;
//				// An invalid block was found and replaced, terminate
//				return;
//			}
//		}
//		int blockIndex = (int) (m * Math.random());
//		if(toWriteTo.blocks[blockIndex].getDirtyBit() == 1){
//		}
//		toWriteTo.blocks[blockIndex] = block;
//	}
	
	// This one takes a string address , it reads the data in that address location and returns it
	public String read(String address) {
		Set setToRead;
		Block blockToRead;
		String tagBits = getTagBits(address);
		String offsetBits = getOffsetBits(address);
		int offset = (int) Long.parseLong(offsetBits, 2);
		if(getIndexBitCount() == 0) {
			// since this is fully associative there is only one set
			setToRead = this.sets[0];
		}
		else {
			// getting the set to read from
			int index = Integer.parseInt(getIndexBits(address), 2);
			setToRead = this.sets[index];
		}
		for (int i = 0; i < setToRead.blocks.length; i++) {
			if (tagBits.equals(setToRead.blocks[i].getTag()) && setToRead.blocks[i].getValidBit() == 1) {
				blockToRead = setToRead.blocks[i];
				// read the byte
				return blockToRead.data[offset];
			}
		}
		return null;
	}
	
	
	// takes a string address and returns true if it is a cache hit and false otherwise
	public boolean hit(String address) {
		Set setToSearchIn;
		String tag = getTagBits(address);
		if(getIndexBitCount() == 0) {
			// since this is fully associative there is only one set
			setToSearchIn = this.sets[0];
		}
		else {
			// getting index in decimal
			int index = Integer.parseInt(getIndexBits(address), 2);
			setToSearchIn = this.sets[index];
		}
		// searching through the previously selected set for the address
		for (int i = 0; i < setToSearchIn.blocks.length; i++) {
			// The tag bits of the block = the tag bits of the address, and the block is valid
			if (tag.equals(setToSearchIn.blocks[i].getTag()) && setToSearchIn.blocks[i].getValidBit() == 1)
				return true;
		}
		// cache miss
		return false;
	}
	
	// Parsing address logic
	
	// takes an address and returns the tag bits of that address in string form
	public String getTagBits(String addr){
		return addr.substring(0, getTagBitCount());
	}
	
	// takes an address and returns the tag bits of that address in string form
	public String getIndexBits(String addr){
		return addr.substring(getTagBitCount(), wordSizeInBits - getOffsetBitCount());
	}
	
	// takes an address and returns the offset bits of that address in string form
	public String getOffsetBits(String addr){
		return addr.substring(wordSizeInBits - getOffsetBitCount(), wordSizeInBits);
	}
	
	// Calculated Attrs Getters

	public int getNumberOfSets() {
		// size over line size to get number of blocks in cache, /m since there are m blocks per set.
		return size/lineSize/m;
	}
	
	public int getTagBitCount() {
		// 16 bits, 4 bytes, - the number of (index + offset)
		return wordSizeInBits - (getOffsetBitCount() + getIndexBitCount());
	}
	
	public int getIndexBitCount() {
		// in case of direct mapped cache log2NumberOfSets would the number of blocks
		return log2(getNumberOfSets());
	}
	
	public int getOffsetBitCount() {
		// offset is log base 2 of L where L is the lineSize
		return log2(lineSize);
	}
	
	public double getHitRate() {
		// The hit rate is the number of totalHits / TotalCacheAccesses
		return (double) totalHits /getTotalCacheAccesses();
	}
	
	public double getMissRate() {
		// The miss rate is the number of totalMisses / TotalCacheAccesses
		return (double) totalMisses /getTotalCacheAccesses();
	}
	
	public double getTotalCacheAccesses() {
		// The total number of cache accesses is the number of hits + the number of misses
		return (totalHits + totalMisses);
	}
	
	private int log2(int num){
		// Log base x of n is log n / log x
		return (int)(Math.log(num)/Math.log(2));
	}
	
	// Attrs Getters
	
	public int getSize() {
		return size;
	}

	public int getLineSize() {
		return lineSize;
	}

	public int getM() {
		return m;
	}

	public String getWritePolicyHit() {
		return writePolicyHit;
	}

	public String getWritePolicyMiss() {
		return writePolicyMiss;
	}

	public int getAccessCycles() {
		return accessCycles;
	}

	public Set[] getSets() {
		return sets;
	}

	public int getTotalHits() {
		return totalHits;
	}
	
	public void incrementTotalHits() {
		totalHits++;
	}

	public int getTotalMisses() {
		return totalMisses;
	}
	
	public void incrementTotalMisses() {
		totalMisses++;
	}
	
	public int getTotalAccesses() {
		return this.totalHits + this.totalMisses;
	}
	
	public boolean isBeingAccessed() {
		return isBeingAccessed;
	}

	public void setBeingAccessed(boolean isBeingAccessed) {
		this.isBeingAccessed = isBeingAccessed;
	}
	
	public int getAccessCyclesRemaining() {
		return accessCyclesRemaining;
	}

//	public void setAccessCyclesRemaining(int accessCyclesRemaining) {
//		this.accessCyclesRemaining = accessCyclesRemaining;
//	}
	
	public void decrementAccessCyclesRemaining() {
		accessCyclesRemaining -=1;
	}
	
	public void resetAccessCyclesRemaining() {
		accessCyclesRemaining = accessCycles;
	}

	public boolean isBeingFetched() {
		return isBeingFetched;
	}

	public void setBeingFetched(boolean isBeingFetched) {
		this.isBeingFetched = isBeingFetched;
	}

	public int getFetchCyclesRemaining() {
		return fetchCyclesRemaining;
	}

//	public void setFetchCyclesRemaining(int fetchCyclesRemaining) {
//		this.fetchCyclesRemaining = fetchCyclesRemaining;
//	}
	
	public void decrementFetchCyclesRemaining() {
		fetchCyclesRemaining--;
	}
	
	public void resetFetchCyclesRemaining() {
		fetchCyclesRemaining = accessCycles;
	}

//	public void setAccessCycles(int accessCycles) {
//		this.accessCycles = accessCycles;
//	}

	public String cacheToString(){
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("++++++++++++++++++++++++++++++++++++++++++++++++++" + "\n");
		for (int i = 0; i < this.sets.length; i++) {
			toReturn.append("***** Set "+i+" *****");
			toReturn.append(this.sets[i].toString());
			toReturn.append("\n");
		}
		toReturn.append("++++++++++++++++++++++++++++++++++++++++++++++++++" + "\n");
		return toReturn.toString();
	}
	
	public String toString(){
		String toReturn = "";
		toReturn += ("{ size: " + size);
		toReturn += (", line size: " + lineSize);
		toReturn += (", associativity m: " + m);
		toReturn += (", hit write policy: " + writePolicyHit);
		toReturn += (", miss write policy: " + this.writePolicyMiss);
		toReturn += (", access cycles: " + accessCycles);
		toReturn += (" }");
		return toReturn;
	}
}