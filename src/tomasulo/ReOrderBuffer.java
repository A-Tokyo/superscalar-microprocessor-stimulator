package tomasulo;

public class ReOrderBuffer {

	public int sizeOfROB;
	
	public ROBEntry[] buffer;
	private int headPosition;
	public int getHeadPosition() {
		return headPosition;
	}

	public void setHeadPosition(int headPosition) {
		this.headPosition = headPosition;
	}

	public int getTailPosition() {
		return tailPosition;
	}

	public void setTailPosition(int tailPosition) {
		this.tailPosition = tailPosition;
	}



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
		System.out.printf("Reorder Buffer. Extending From the head at %d to the tail at %d", this.headPosition, this.tailPosition);
		
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