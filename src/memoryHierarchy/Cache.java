package memoryHierarchy;

public class Cache {
	int size; // size of cache
	int lineSize; // line size of cache 
	int m; // associativity
	String writePolicyHit; // writeThrough or writeBack
	String writePolicyMiss; // writeThrough or writeBack
	int accessCycles; // access time (in cycles)
	
	Set [] sets; // array of sets containing blocks
	
	int totalHits; // cache hits
	int totalMisses; // cache misses
	
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
	
	// Calculated Attrs Getters

	public int getNumberOfSets() {
		// size over line size to get number of blocks in cache, /m since there are m blocks per set.
		return size/lineSize/m;
	}
	
	public int getTag() {
		// 16 bits, 4 bytes, - the number of (index + offset)
		return 16 - (getOffset() + getIndex());
	}
	
	public int getIndex() {
		// in case of direct mapped cache log2NumberOfSets would the number of blocks
		return log2(getNumberOfSets());
	}
	
	public int getOffset() {
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