package tomasulo;

public class InstructionBuffer {
 
	int head;
	int tail;
	InstructionBuffer [] Buffer ;
		
	public InstructionBuffer(int sizeOfBuffer) {
		this.Buffer=new InstructionBuffer[sizeOfBuffer];
	}
	
	

}
