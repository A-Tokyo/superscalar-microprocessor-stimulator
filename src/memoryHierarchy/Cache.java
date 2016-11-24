package memoryHierarchy;

public class Cache {
	int size; // size of cache
	int line_size; // line size of cache 
	int m; // associativity
	
	int total_hits;
	int total_misses;
	
	public int getSize() {
		return size;
	}
	public int getLine_size() {
		return line_size;
	}
	public int getM() {
		return m;
	}
	public int getTotal_hits() {
		return total_hits;
	}
	public int getTotal_misses() {
		return total_misses;
	}
}