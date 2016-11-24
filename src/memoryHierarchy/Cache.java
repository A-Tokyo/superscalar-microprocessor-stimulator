package memoryHierarchy;

public class Cache {
	int size; // size of cache
	int lineSize; // line size of cache 
	int m; // associativity
	int writePolicyHit; // 0 for writeThrough 1 for writeBack
	int writePolicyMiss; // 0 for writeThrough 1 for writeBack
	int accessCycles; // access time (in cycles)
	
	Set [] sets;
	
	int totalHits;
	int totalMisses;
	
	public Cache(int size, int lineSize, int m, int writePolicyHit, int writePolicyMiss, int accessCycles) {
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

	public int getSize() {
		return size;
	}
	public int getLine_size() {
		return lineSize;
	}
	public int getM() {
		return m;
	}
	public Set[] getSets() {
		return sets;
	}
	public int getTotal_hits() {
		return totalHits;
	}
	public int getTotal_misses() {
		return totalMisses;
	}
}