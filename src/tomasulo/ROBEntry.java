package tomasulo;


public class ROBEntry {

	
	private String instructionType;
	private int  instructionDestination;
	private int instructionValue;
	private boolean Ready;
	int PC__value;
	
	public ROBEntry(String instructionType, int instructionDestination, int instructionValue, boolean Ready) {
		this.instructionType = instructionType;
		this.instructionDestination = instructionDestination;
		this.instructionValue = instructionValue;
		this.Ready = Ready;
	}
	
	public ROBEntry(String instructionType, int instructionDestination, boolean Ready) {
		this.instructionType = instructionType;
		this.instructionDestination = instructionDestination;
		this.Ready = Ready;
	}

	public ROBEntry(String instructionType, int instructionDestination) {
		this(instructionType, instructionDestination, false);
	}

	
	public String getInstructionType() {
		return instructionType;
	}
	public void setInstructionType(String instructionType) {
		this.instructionType = instructionType;
	}

	
	public int getInstructionDestination() {
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
	
	public int getPC__value() {
		return PC__value;
	}

	public void setPC__value(int pC__value) {
		PC__value = pC__value;
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
