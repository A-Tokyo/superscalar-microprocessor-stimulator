package memoryHierarchy;

public class Memory {
	String [] memory; // Main memory array, length should be 2^16
	int accessTime; // Input: Number of cycles to access
	int totalCycles; // Output: Total number of cycles in the current stimulation.
	
	// tracking memory state
	private boolean isBeingAccessed;
	
	//Had to separate them into two counters since there must be one for data and one for instruction, these counters need to be reset once they reach zero;
	int fetchCyclesRemaining;
	int dataAccessCyclesRemaining;
	
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

	public int getAccessTime() {
		return accessTime;
	}

	public int getTotalCycles() {
		return totalCycles;
	}
	
	public void setTotalCycles(int totalCycles) {
		this.totalCycles = totalCycles;
	}
	
	public int incrementTotalCycles() {
		return ++this.totalCycles;
	}
	
	public boolean isBeingAccessed() {
		return isBeingAccessed;
	}

	public void setBeingAccessed(boolean isBeingAccessed) {
		this.isBeingAccessed = isBeingAccessed;
	}
	
	public int getFetchCyclesRemaining() {
		return fetchCyclesRemaining;
	}

	public void setFetchCyclesRemaining(int fetchCyclesRemaining) {
		this.fetchCyclesRemaining = fetchCyclesRemaining;
	}
	
	public void decrementFetchCyclesRemaining() {
		fetchCyclesRemaining--;
	}
	
	public void resetFetchCyclesRemaining() {
		this.fetchCyclesRemaining = accessTime;
	}

	public int getDataAccessCyclesRemaining() {
		return dataAccessCyclesRemaining;
	}

	public void setDataAccessCyclesRemaining(int dataAccessCyclesRemaining) {
		this.dataAccessCyclesRemaining = dataAccessCyclesRemaining;
	}
	
	public void decrementDataAccessCyclesRemaining() {
		dataAccessCyclesRemaining--;
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