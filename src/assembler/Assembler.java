package assembler;

public class Assembler {
	public static void assemble(String x){
		
	}

	public static String decodeRegister(String register) {
		register = register.toLowerCase();
		if (!register.startsWith("reg")){
			throw new IllegalArgumentException("Syntax Error in Register field");
		}
		switch (Integer.parseInt(register.substring(3))) {
		case 0:
			return "000";
		case 1:
			return "001";
		case 2:
			return "010";
		case 3:
			return "011";
		case 4:
			return "100";
		case 5:
			return "101";
		case 6:
			return "110";
		case 7:
			return "111";
		}
		throw new IllegalArgumentException("Invalid Register Number");
	}
	
	public static void main(String[] args) {
//		System.out.println(decodeRegister("REG8"));
	}
}
