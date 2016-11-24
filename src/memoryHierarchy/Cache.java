package memoryHierarchy;

public class Cache {
	int size; // size of cache
	int lineSize; // line size of cache 
	int m; // associativity
	
	Set [] sets;
	
	int totalHits;
	int totalMisses;
	
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