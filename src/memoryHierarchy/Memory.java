package memoryHierarchy;

public class Memory {
	String [] main_memory; // Main memory array
	int access_time; // Input: Number of cycles to access
	int total_cycles; // Output: Total number of cycles in the current stimulation.
	
	public Memory(int access_time) {
		this.main_memory = new String[65536];
		this.access_time = access_time;
		this.total_cycles = 0;
	}

	public int getAccess_time() {
		return access_time;
	}

	public int getTotal_cycles() {
		return total_cycles;
	}
	
}
