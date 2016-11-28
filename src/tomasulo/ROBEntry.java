package tomasulo;


public class ROBEntry {

	
	private String instructionType;
	private int instructionDestination;
	private int instructionValue;
	private boolean Ready;
	
	public ROBEntry(String instructionType, int instructionDestination, int instructionValue, boolean Ready) {
		this.instructionType = instructionType;
		this.instructionDestination = instructionDestination;
		this.instructionValue = instructionValue;
		this.Ready = Ready;
	}
	
	public String getInstructionType() {
		return instructionType;
	}
	public void setInstructionType(String instructionType) {
		this.instructionType = instructionType;
	}

	
	public long getInstructionDestination() {
		return instructionDestination;
	}
	public void setInstructionDestination(int instructionDestination) {
		this.instructionDestination = instructionDestination;
	}
	public int getInstructionValue() {
		return instructionValue;
	}
	public void setInstructionValue(int instructionValue) {
		this.instructionValue = instructionValue;
	}
	public boolean isReady() {
		return Ready;
	}
	public void setReady(boolean ready) {
		Ready = ready;
	}
	
	public boolean isEqual(ROBEntry otherEntry) {
		if (this.instructionType != otherEntry.instructionType) {
			return false;
		} else if (this.instructionDestination != otherEntry.instructionDestination) {
			return false;
		} else if (this.instructionValue != otherEntry.instructionValue) {
			return false;
		} else if (this.Ready != otherEntry.Ready) {
			return false;
		} else {
			return true;
		}
	}
	
	public ROBEntry(ROBEntry otherEntry) {
		this.instructionType = otherEntry.instructionType;
		this.instructionDestination = otherEntry.instructionDestination;
		this.instructionValue = otherEntry.instructionValue;
		this.Ready = otherEntry.Ready;
	}
	
}
