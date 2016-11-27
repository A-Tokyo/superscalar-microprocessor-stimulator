package functionalUnits;

public class MultiplyFunctionalUnit extends FunctionalUnit{
	int cycles;
	public MultiplyFunctionalUnit(int cycles){
		this.cycles=cycles;
	}
	public  int execute(String opp, int R1, int R2){
		if(opp.toLowerCase().equals("mul")){
			return mul(R1,R2);
		}else{
			return divd(R1,R2);
		}
		
	}
public  int mul(int R1, int R2){
	return R1*R2;
}
public  int divd( int R1, int R2){
	return R1/R2;
}
//public static void main(String [] args){
//	int x=execute("divd", 5, 0) ;
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
