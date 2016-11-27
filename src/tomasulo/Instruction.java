package tomasulo;

public class Instruction {
	int rs; // source register
	int rt;  // source register
	int rd;  // destination register
	int arithmeticOpCode;  //  opcode which decide the which arithmetic operation i should do 
	
	String function_Type;   
	
	int PC_pointer;  //the place which the interaction exist 


	public Instruction(String instruction_WrittenInBinary) {
		String local_arithmeticOpCode = "" ;
		switch (instruction_WrittenInBinary) {
        case "000":
        	//load
        	function_Type= "load";
    		this.rd = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
    		this.rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(6, 9), 2);
    		this.rt = -1;
    		 local_arithmeticOpCode = instruction_WrittenInBinary.substring(9);

    		arithmeticOpCode = Integer.parseInt(convert_to_Decimal(""+local_arithmeticOpCode));
            break;
        case "001":
        	//store
        	function_Type = "store";
    		this.rt = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
    		this.rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(6, 9), 2);
    		 local_arithmeticOpCode = instruction_WrittenInBinary.substring(9);
    		arithmeticOpCode = Integer.parseInt(convert_to_Decimal(""+local_arithmeticOpCode));
    		this.rd = -1;
            break;
        case "010":
        	//beq
        	function_Type = "branch";
    		this.rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
    		this.rt = (int) Long.parseLong(instruction_WrittenInBinary.substring(6, 9), 2);
    		this.rd = -1;
    		 local_arithmeticOpCode = instruction_WrittenInBinary.substring(9);
    		arithmeticOpCode = Integer.parseInt(convert_to_Decimal(""+local_arithmeticOpCode));
            break;
        case "011":
        	//jump
        	function_Type = "jump";
    		this.rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
    		this.rd = -1;
    		this.rt = -1;
    		 local_arithmeticOpCode = instruction_WrittenInBinary.substring(9);
    		arithmeticOpCode = Integer.parseInt(convert_to_Decimal(""+local_arithmeticOpCode));
            break;
        case "100":
        	//jalr
        	function_Type = "jalr";
    		this.rd = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
    		this.rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(6, 9), 2);
    		this.rt = -1;
            break;
        case "101":
        	//ret
        	function_Type = "return";
    		rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
    		rt = -1;
    		rd = -1;

            break;
        case "110":
        	//addi
        	function_Type = "addi";
    		this.rd = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
    		this.rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(6, 9), 2);
    		this.rt = -1;
    		local_arithmeticOpCode = instruction_WrittenInBinary.substring(9);
    		arithmeticOpCode = Integer.parseInt(convert_to_Decimal(""+local_arithmeticOpCode));
            break;
        case "111":
        	decode_arithmetic(instruction_WrittenInBinary);
            break;
        
    }
		
		
	}
	
	private void decode_arithmetic(String instruction_WrittenInBinary) {
		// Add, Sub, nand mult rd, rs, rt
		String opCode_function = instruction_WrittenInBinary.substring(12,16);
		switch (opCode_function) {
        case "0000":
        	function_Type = "add";
            break;
        case "0001":
        	function_Type = "subtract";
            break;
        case "0010":
        	function_Type = "nand";
            break;
        case "0011":
        	function_Type = "multiply";
            break;
       
        
    }

		rd = (int) Long.parseLong(instruction_WrittenInBinary.substring(3, 6), 2);
		rs = (int) Long.parseLong(instruction_WrittenInBinary.substring(6, 9), 2);
		rt = (int) Long.parseLong(instruction_WrittenInBinary.substring(9, 12), 2);

			
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
		for(int i=0 ; i<bitString.length();i++){
			if(bitString.charAt(i)=='0'){
				//bitString.charAt(i) ='1';
				temp = temp+1 ; 
			}
			else temp = temp+0 ; 
				//bitString.charAt(i)='0';
		  } //end for 
		
		return temp;
	   }

		return number;
	}
	
	public static String convert_twos_complement(String regValue){
		//int out=0;
		String result ="";
		String stringvalue=Flip_Bits(regValue)  ;
		if (isNeg(regValue)==true ){
			for(int i=stringvalue.length()-1;i>0;i--){
			if(stringvalue.charAt(i)=='0'){
				result+="1";
				break ;
			  } else result+="0";
			}
			return result ;
		}
		//else result= regValue;
		return regValue ;
	}
	
	public static String convert_to_Decimal(String number){
		
		/* 
		 100111
		 (2^0)*1
		  */
		int factor=1;
		int result =0;
		int out=0;
		String convert_out="";
		String convert = number ;
		for(int i=convert.length()-1 ;i >-1 ;i--){
			result += factor * convert.charAt(i); //0101
			factor =factor*2;//factor=2  // 1*2
		}
		convert_out=""+result ;
		return convert_out;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
