package tomasulo;

// NOTE: Still in the works, missing some important functionality
// Committed for visibility / integration purposes
public class ReOrderBuffer {

	public int sizeOfROB;
	
	private ROBEntry[] buffer;
	private int headPosition;
	private int tailPosition;
	
	public ReOrderBuffer(int sizeOfROB) {
		this.sizeOfROB = sizeOfROB;
		this.buffer = new ROBEntry[this.sizeOfROB];
		this.headPosition = 0;
		this.tailPosition = this.headPosition;
	}

	public void incrementHead() {
		
		if (this.headPosition == this.sizeOfROB - 1) {
			this.headPosition = 0;
		} else {
			this.headPosition += 1;
		}
		
	}
	
	public void incrementTail() {
		
		if (this.tailPosition == this.sizeOfROB - 1) {
			this.tailPosition = 0;
		} else {
			this.tailPosition += 1;
		}
		
	}
	
	public void enQueue(ROBEntry entry) {
		incrementTail();
	}
	
	
	public void displayBufferDetails() {
		System.out.println("Reorder Buffer");
		System.out.println("Head at position " + this.headPosition);
		System.out.println("Tail at position " + this.tailPosition);
		
		for (ROBEntry someEntry : this.buffer) {
			if (someEntry == null) {
				continue;
			} else {
				System.out.printf("The instruction is of type %s, has destination %d, has value %d, and the status of ready is %b", 
							someEntry.getInstructionType(), someEntry.getInstructionDestination(),
							someEntry.getInstructionValue(), someEntry.isReady());
			}
		}
	}
	
}


