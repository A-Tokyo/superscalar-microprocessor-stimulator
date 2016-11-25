package memoryHierarchy;

public class Set {

	public Block[] blocks; // Blocks in a Set
	
	public Set(int noBlocks, int lineSize) { // noBlocks is m in the cache, the associativity
		this.blocks = new Block[noBlocks];
		for(int i = 0; i < this.blocks.length; i++){
			this.blocks[i] = new Block(lineSize);	
		}
	}
	
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		for (int i = 0; i < blocks.length; i++) {
			toReturn.append("Block " + i + ": ");
			toReturn.append(blocks[i].toString());
			toReturn.append("\n");
		}
		return toReturn.toString();
	}

	public Block[] getBlocks() {
		return blocks;
	}
	
}