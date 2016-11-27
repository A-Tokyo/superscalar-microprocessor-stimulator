package tomasulo;

public class RegisterStatusTable {
<<<<<<< HEAD

	int []reorderBufferindex  ; // here i mean each element (RD) in this array point to it`s position in ReorderBufferTable

=======
	int []reorderBufferindex  ; // here i mean each element (RD) in this array point to it`s position in ReorderBufferTable
>>>>>>> tomasulo_main_file
	public RegisterStatusTable(int size) {
		reorderBufferindex = new int [size];
		for(int i = 0; i < reorderBufferindex.length; i++) // intialy load by nulls 
			reorderBufferindex[i] = -1; // choose any -ve mean null 

	}
	public void whatIsInMyRegisterStatusTable() {
		System.out.println("Register Status Table :");
		for (int i = 0; i < reorderBufferindex.length; i++) {
			System.out.print("R"+i+": " + reorderBufferindex[i] +", ");
		}
		System.out.print("\n");

	}

	
	public void emptyRegisterStatusTable() {   // as in  lecture flush is equal to clear everything 
		for(int i = 0; i < reorderBufferindex.length; i++) //  load by nulls 
			reorderBufferindex[i] = -1; // choose any -ve mean null 

	
	}

	

	public static void main(String[] args) {
		// i leave main because i test something don`t remove plz 

	}

}
