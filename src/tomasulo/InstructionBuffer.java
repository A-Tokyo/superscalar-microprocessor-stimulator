package tomasulo;

public class InstructionBuffer {
 
	int head;
	int tail;
	Instruction [] Buffer ;
		
	public InstructionBuffer(int sizeOfBuffer) {
		this.Buffer=new Instruction[sizeOfBuffer];
	}
	
	public  boolean Empty_Instruction_Buffer(){
		 Instruction Instruction_Buffer_tail=this.Buffer[tail];
		 
		 if(Instruction_Buffer_tail==null ){ 
			 return true;
		 } else return false;
		
	}
	 public boolean  Full_Instruction_Buffer(){
		 Instruction Instruction_Buffer_tail=this.Buffer[tail];
//		 int length = this.Buffer.length ;
		
		 if(Instruction_Buffer_tail==null ){ 
			 return false;
		 
		} else return true;
	 }
	
	public boolean Add_To_Instruction_Buffer (Instruction addit){
		
		int length = this.Buffer.length ;
		for(tail=0 ; tail<length ; tail++){
		  Instruction Instruction_Buffer_tail=this.Buffer[tail];
		
		  if(Instruction_Buffer_tail==null ){
			Instruction_Buffer_tail=addit; // add the element in buffer
			return true;
		      } 
			
		} // end for 
		if(length==tail)
	        tail=0;	
		 return false ;
		
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
			 Instruction Instruction_Buffer_item=this.Buffer[i];
			 Instruction_Buffer_item = null;
		}
		this.head=0;
		this.tail=0 ;
	
	}
	
	

}
