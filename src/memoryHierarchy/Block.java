package memoryHierarchy;

public class Block {
	String tag; // tag bits
	String [] data; // data in bytes

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
	
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("[ " + tag);
		for (int i = 0; i < data.length; i++) {
			toReturn.append(data[i]);
			toReturn.append(i<data.length?",":"");
		}
		toReturn.append(" ]");
		return toReturn.toString();
	}
}
