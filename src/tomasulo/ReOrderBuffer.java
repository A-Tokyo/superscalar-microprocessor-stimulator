package tomasulo;

public class ReOrderBuffer {

	public int sizeOfROB;
	public int currentSize;
	
	private ROBEntry[] buffer;
	private int headPosition;
	private int tailPosition;
	
	public ReOrderBuffer(int sizeOfROB) {
		this.sizeOfROB = sizeOfROB;
		this.buffer = new ROBEntry[this.sizeOfROB];
		currentSize = 0;
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
	
	public int getSizeOfROB() {
		return sizeOfROB;
	}

	public void setSizeOfROB(int sizeOfROB) {
		this.sizeOfROB = sizeOfROB;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	public ROBEntry[] getBuffer() {
		return buffer;
	}

	public void setBuffer(ROBEntry[] buffer) {
		this.buffer = buffer;
	}

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

	public ROBEntry peakFront() {
		return new ROBEntry(this.buffer[this.headPosition]);
	}
	
	public ROBEntry peakBack() {
		return new ROBEntry(this.buffer[this.tailPosition]);
	}
	
	public boolean enQueue(ROBEntry entry) {
		if (this.isFull()) {
			return false;
		}
		buffer[this.tailPosition] = entry;
		this.incrementTail();
		this.currentSize += 1;
		return true;
	}
		
	public void reset() {
		for (int i = 0; i < this.sizeOfROB; i++) {
			this.buffer[i] = null;
		}
		this.currentSize = 0;
		this.headPosition = 0;
		this.tailPosition = 0;
	}
	
	public ROBEntry deQueue() {
		if (this.isEmpty()) {
			return null;
		}
		ROBEntry returnEntry = new ROBEntry(this.buffer[this.headPosition]);
		this.buffer[this.headPosition] = null;
		this.currentSize -= 1;
		incrementHead();
		return returnEntry;
	}
	
	public ROBEntry[] getCopy() {
		ROBEntry[] returnBuffer = new ROBEntry[this.sizeOfROB];
		for (int i = 0; i < this.sizeOfROB; i++) {
			returnBuffer[i] = new ROBEntry(this.buffer[i]);
		}
		return returnBuffer;
	}
	
	public boolean isEmpty() {
		return (this.currentSize == 0);
	}
	
	public boolean isFull() {
		return (this.currentSize == this.sizeOfROB);
	}
	public ROBEntry getEntry(){
		return this.buffer[this.headPosition];
	}
	public void cleanRS() {
		
		int length=this.buffer.length;
		for(int i = 0; i < length; i++) {
			this.buffer[i] = null;
		}
		this.headPosition = 0;
		this.tailPosition = 0;
	}
	
}


