package tomasulo;

public class RegisterFile {

	public String [] registers;
	final String Reg_zero ="0000000000000000"; 
	
	public RegisterFile() {
		int sizeOfRegister=8 ;
		this.registers=new String [sizeOfRegister];
		int Regsize=registers.length ;
		for(int i=1 ; i< Regsize ;i++){
			registers[i]="000000000000000";
		}
		registers[0]="0000000000000000";
		
	}

	public static boolean isNeg(String number){
		if(number.charAt(0)=='1'){
			return true;
		}
		else return false;
	}
	
	public static int Flip_Bits(int number){
		 String bitString = Integer.toBinaryString(number);
		 int k=0;
		if(bitString.charAt(k)!='1'){
		      k++;
		  }
		bitString=bitString.substring(1, bitString.length());
		for(int i=1 ; i<bitString.length();i++){
			if(bitString.charAt(i)=='0'){
				bitString.charAt(i) ='1';
			}
			else bitString.charAt(i)='0';
			
		}
		 int result = 0 ;
		 int factor = 1;

		    for (int j = bitString.length()-1; j > -1; j--){
		        result += factor * bitString.charAt(j);
		        factor =factor*2;
		    }

		    return result;
	}
	
	public static String convert_twos_complement(String regValue){
		String result ="";
		if (isNeg(regValue)==true ){
			result = "1"+ Flip_Bits(regValue);
		}
		else result='0'+ regValue;
		return result ;
		
	}
	
	
	public static void main(String[] args) {
	

	}

}
