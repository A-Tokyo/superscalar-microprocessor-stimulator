package memoryHierarchy;

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

	
}
