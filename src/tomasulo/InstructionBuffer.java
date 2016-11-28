package tomasulo;

public class InstructionBuffer {
 
	int head;
	int tail;
	Instruction [] Buffer ;
		
	public InstructionBuffer(int size) {
		this.Buffer=new Instruction[size];
	}
	
	public  boolean Empty_Instruction_Buffer(){
		for(int i = 0; i < Buffer.length; i++) {
			if(Buffer[i] != null)
				return false;
		}
		return true;
	}
		
	
	 public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public int getTail() {
		return tail;
	}

	public void setTail(int tail) {
		this.tail = tail;
	}

	public Instruction[] getBuffer() {
		return Buffer;
	}

	public void setBuffer(Instruction[] buffer) {
		Buffer = buffer;
	}

	public boolean  Full_Instruction_Buffer(){
		 Instruction Instruction_Buffer_tail=this.Buffer[tail];
//		 int length = this.Buffer.length ;
		
		 if(Instruction_Buffer_tail==null ){ 
			 return false;
		 
		} else return true;
	 }
	
	public boolean Add_To_Instruction_Buffer (Instruction addIt){
		
		if (Buffer[tail] == null) {
			Buffer[tail] = addIt;
			tail++;
			
			if(tail == Buffer.length)
				tail = 0; // TO BE like circle  
			return true;
		}
		else {
			return false;
		}
		
	}// end method
	
	public Instruction remove_element_from_Instruction_Buffer(){
		int length = this.Buffer.length  ;
		 Instruction Instruction_Buffer_head=this.Buffer[head];
		 
		// for(int head =0 ; head <length ;head++){
//			 Instruction Instruction_Buffer_head=this.Buffer[head];
			 
			 if(Instruction_Buffer_head ==null){ // head points to null 
				 return null;
			 }
			
			 Instruction_Buffer_head =null;
			 head++;
		// }
		 
		 if (length==head)
			 head =0;
			 return  Instruction_Buffer_head ;
		
	}
	
	public void Flush(){ // clean all 
		int length = this.Buffer.length  ;
		for(int i=0;i<length ; i++){			
			Buffer[i] = null;
		}
		this.head=0;
		this.tail=0 ;
	
	}
	
	

}
