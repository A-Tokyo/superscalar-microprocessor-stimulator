package memoryHierarchy;

public class Cache {
	int size; // size of cache
	int lineSize; // line size of cache 
	int m; // associativity
	String writePolicyHit; // writeThrough or writeBack
	String writePolicyMiss; // writeThrough or writeBack
	int accessCycles; // access time (in cycles)
	
	Set [] sets; // array of sets containing blocks, 1 set if fully Associative, same as number of blocks if direct mapped
	
	int totalHits; // cache hits
	int totalMisses; // cache misses
	
	private static final int wordSizeInBits = 16;
	
	public Cache(int size, int lineSize, int m, String writePolicyHit, String writePolicyMiss, int accessCycles) {
		this.size = size;
		this.lineSize = lineSize;
		this.m = m;
		this.writePolicyHit = writePolicyHit;
		this.writePolicyMiss = writePolicyMiss;
		this.accessCycles = accessCycles;
		
		// size over line size to get number of blocks in cache, /m since there are m blocks per set.
		this.sets = new Set[size/lineSize/m];
		for (int i=0; i < this.sets.length; i++) {
			// each set is a new set of size m and lineSize lineSize
			this.sets[i] = new Set(m, lineSize);
		}
	}
	
	// cache Logic
	
	/*This one takes a block and a decimal index and writes the block in the set belonging to that index
	 * It first I search for an invalid block, if found, I replace that block
	 * If no invalid blocks exist, a random block is replaced
	 * of course in case of direct mapped the block of the index is replaced since the set would have 1 block
	 */
	public void write(Block block, int index) {
		Set toWriteTo = this.sets[index];
		for(int i = 0; i < toWriteTo.blocks.length; i++) {
			if (toWriteTo.blocks[i].getValidBit() == 0) {
				toWriteTo.blocks[i] = block;
				// An invalid block was found and replaced, terminate
				return;
			}
		}
		toWriteTo.blocks[(int) (m * Math.random())] = block;
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
	private String getTagBits(String addr){
		return addr.substring(0, getTagBitCount());
	}
	
	// takes an address and returns the tag bits of that address in string form
	private String getIndexBits(String addr){
		return addr.substring(getTagBitCount(), wordSizeInBits - getOffsetBitCount());
	}
	
	// Calculated Attrs Getters

	private int getNumberOfSets() {
		// size over line size to get number of blocks in cache, /m since there are m blocks per set.
		return size/lineSize/m;
	}
	
	private int getTagBitCount() {
		// 16 bits, 4 bytes, - the number of (index + offset)
		return wordSizeInBits - (getOffsetBitCount() + getIndexBitCount());
	}
	
	private int getIndexBitCount() {
		// in case of direct mapped cache log2NumberOfSets would the number of blocks
		return log2(getNumberOfSets());
	}
	
	private int getOffsetBitCount() {
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

	public int getTotalMisses() {
		return totalMisses;
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