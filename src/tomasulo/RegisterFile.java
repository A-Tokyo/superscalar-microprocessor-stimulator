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
	
	public static String Flip_Bits(String number){
		String temp = "";
		 String bitString = number;
		 int k=0;
		if(bitString.charAt(0)=='1'){
			temp = temp+0 ; 
		      
		bitString=bitString.substring(1, bitString.length());
		for(int i=1 ; i<bitString.length();i++){
			if(bitString.charAt(i)=='0'){
				//bitString.charAt(i) ='1';
				temp = temp+1 ; 
			}
			else temp = temp+0 ; 
				//bitString.charAt(i)='0';
		  }
	   }

		    return temp;
	}
	
	public static String convert_twos_complement(String regValue){
		String result ="";
		if (isNeg(regValue)==true ){
			result = "1"+ Flip_Bits(regValue);
		}
		else result='0'+ regValue;
		return result ;
	}
	
	public static String convert_to_Decimal(int number){
		int factor=1;
		int result =0;
		int out=0;
		String convert_out="";
		String convert = Integer.toBinaryString(number);
		for(int i=convert.length()-1 ;i >-1 ;i--){
			result += factor * convert.charAt(i); //0101
			factor =factor*2;//factor=2  // 1*2
		}
		convert_out=""+result ;
		return convert_out;
		
	}
	
	
	public static void main(String[] args) {
	

	}

}
