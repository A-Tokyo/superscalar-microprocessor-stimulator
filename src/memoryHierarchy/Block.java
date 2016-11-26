package memoryHierarchy;

public class Block {
	String [] data; // data in bytes
	private String tag; // tag bits
	private int validBit; // determines if the content is valid or not
	private int dirtyBit; // used in writeBack only

	public Block(int lineSize) {
		this.data = new String[lineSize]; // lineSize is the number of bytes therefore we need an element for each byte
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}
	
	public int getValidBit() {
		return validBit;
	}

	public void setValidBit(int validBit) {
		this.validBit = validBit;
	}
	
	public int getDirtyBit() {
		return dirtyBit;
	}

	public void setDirtyBit(int dirtyBit) {
		this.dirtyBit = dirtyBit;
	}

	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("[ " + validBit);
		toReturn.append(", " + tag);
		for (int i = 0; i < data.length; i++) {
			toReturn.append(data[i]);
			toReturn.append(i<data.length?",":"");
		}
		toReturn.append(" ]");
		return toReturn.toString();
	}
}
