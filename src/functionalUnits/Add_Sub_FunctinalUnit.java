package functionalUnits;

public class Add_Sub_FunctinalUnit extends FunctionalUnit{
	int cycles;
	public Add_Sub_FunctinalUnit(int cycles){
		this.cycles=cycles;
	}
	public  int execute(String opp, int R1, int R2){
		if(opp.toLowerCase().equals("add")||opp.toLowerCase().equals("addi")){
			return add(R1,R2);
		}
		else{
			if(opp.toLowerCase().equals("sub")|| opp.toLowerCase().equals("subi")){
				return sub (R1,R2);
			}
			else{
				if(opp.toLowerCase().equals("branch")){
					return branch(R1,R2);
				}
				else{
					return nand(R1,R2);
				}
			}
		}
		
	}
public  int add(int X,int Y){
	return X+Y;
}
public  int sub(int R1, int R2){
	return R1-R2;
}
public  int branch(int R1, int R2){
	if(R1==R2){
		return 1;
	}
	else{
		return 0;
	}
}
public  int nand(int R1, int R2){
	int x =R1&R2;
	String y="";
	String bin=Integer.toBinaryString(x);
	for(int i=0;i<bin.length();i++){
		if(bin.charAt(i)=='1'){
			y+="0";
		}
		else{
			y+="1";
		}
		 
	}
	 x= Integer.parseInt(y, 2);

return x;
	

}
//public static void main(String [] args){
//	int x=execute("nand", 5, 5) ;
////	String y="";
////	String bin=Integer.toBinaryString(x);
////	for(int i=0;i<bin.length();i++){
////		if(bin.charAt(i)=='1'){
////			y+="0";
////		}
////		else{
////			y+="1";
////		}
////		 
////	}
////	 x= Integer.parseInt(y, 2);
//	 System.out.println(x);
////	 System.out.println(y);
//	 
//	
//}
	

}
