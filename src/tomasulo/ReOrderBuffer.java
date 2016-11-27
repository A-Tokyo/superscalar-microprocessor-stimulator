package tomasulo;

// NOTE: Still in the works, missing some important functionality
// Committed for visibility / integration purposes
public class ReOrderBuffer {

	// set as 6 for simplicity; should maybe be taken from user - if so, simply change this value
	public int sizeOfROB;
	
	private ROBEntry[] buffer;
	private ROBEntry head;
	private int headPosition;
	private ROBEntry tail;
	private int tailPosition;
	
	public ReOrderBuffer(int sizeOfROB) {
		this.sizeOfROB = sizeOfROB;
		this.buffer = new ROBEntry[this.sizeOfROB];
		this.headPosition = 0;
		this.tailPosition = this.headPosition;
		this.head = this.buffer[this.headPosition];
		this.tail = this.buffer[this.tailPosition];
	}

	public void incrementHead() {
		
		if (this.headPosition == this.sizeOfROB - 1) {
			this.headPosition = 0;
		} else {
			this.headPosition += 1;
		}
		
		this.head = this.buffer[this.headPosition];
		
	}
	
	public void incrementTail() {
		
		if (this.tailPosition == this.sizeOfROB - 1) {
			this.tailPosition = 0;
		} else {
			this.tailPosition += 1;
		}
		
		this.tail = this.buffer[this.tailPosition];
	}
	
	public void enQueue(ROBEntry entry) {
		this.tail = entry;
		incrementTail();
	}
	
	
}


