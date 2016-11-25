package memoryHierarchy;

public class Memory {
	String [] memory; // Main memory array, length should be 2^16
	int accessTime; // Input: Number of cycles to access
	int totalCycles; // Output: Total number of cycles in the current stimulation.
	
	// tracking memory state
	private boolean isBeingAccessed;
	
	public Memory(int access_time) {
		this.memory = new String[65536]; // 2^16
		this.accessTime = access_time;
		this.totalCycles = 0;
	}
	
	// Takes an int address as an input and returns the data in the memory location associated with it.
	public String ReadFromMemory(int address) {
		return memory[address];
	}
	
	// Takes an int address as an input and a string data, writes the data to the memory location of index address.
	public void writeToMemory(int address, String data){
		memory[address] = data;
	}

	public int getAccess_time() {
		return accessTime;
	}

	public int getTotal_cycles() {
		return totalCycles;
	}
	
	public boolean isBeingAccessed() {
		return isBeingAccessed;
	}

	public void setBeingAccessed(boolean isBeingAccessed) {
		this.isBeingAccessed = isBeingAccessed;
	}
	
	// Returns the main memory in string form
	public String memoryToString(){
		StringBuilder toReturn = new StringBuilder();
		for(int i = 0; i < this.memory.length; i++) {
			toReturn.append("[Address: " + i + ", Data: "+ memory[i]+"]");
			if (i % 16 == 0 && i!=0){ // for readability the memory is split to 4096 lines.
				toReturn.append("\n");
			}else{
				toReturn.append(", ");
			}
		}
		return toReturn.toString();
	}
	// Takes a start address and an end address and returns the data in these addresses in string form
	public String memoryToString(int start_address, int end_address){
		StringBuilder toReturn = new StringBuilder();
		if(start_address<0 || end_address>= this.memory.length || end_address-start_address<0){
			return "Memory indeces: " + start_address + ", " + end_address + " are invalid.";	
		}
		for(int i = start_address; i <= end_address && i < this.memory.length && i>=0; i++) {
			toReturn.append("[Address: " + i + ", Data: "+ memory[i]+"]");
			 // for readability the memory is split to 4096 lines.
			toReturn.append(i % 16 == 0 && i!=0?"\n":", ");
		}
		return toReturn.toString();
	}
	
//	public static void main(String[] args) {
//		Memory x = new Memory (10);
//		System.out.println(x.memoryToString(0,87));
//	}
	
}