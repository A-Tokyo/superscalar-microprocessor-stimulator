package functionalUnits;



public class Add_Sub_FunctinalUnit extends FunctionalUnit {
	
	int cycles;
	
	public Add_Sub_FunctinalUnit(int cycles) {
		this.cycles = cycles;
	}
	
	public int execute(String type, int op1, int op2) {
		if(type.toLowerCase().equals("add") || type.toLowerCase().equals("addi")) {
			return add(op1, op2);
		}
		else if(type.toLowerCase().equals("subtract")) {
			return subtract(op1, op2);
		}
		else if(type.toLowerCase().equals("branch")) {
			return branch(op1, op2);
		}else {
			
			return nand(op1, op2);
		}
	}

	private int branch(int op1, int op2) {
		if (op1 == op2)
			return 1;
		else
			return 0;
	}

	public int add(int op1, int op2) {
		return op1 + op2;
	}
	
	public int subtract(int op1, int op2) {
		return op1 - op2;
	}
	
	public int nand(int op1, int op2) {
		

		}
	
	

}
