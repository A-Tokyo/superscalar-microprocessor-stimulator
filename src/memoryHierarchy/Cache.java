package memoryHierarchy;

public class Cache {
	int size; // size of cache
	int lineSize; // line size of cache 
	int m; // associativity
	String writePolicyHit; // writeThrough or writeBack
	String writePolicyMiss; // writeThrough or writeBack
	int accessCycles; // access time (in cycles)
	
	Set [] sets;
	
	int totalHits;
	int totalMisses;
	
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

	// Getters
	
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
}