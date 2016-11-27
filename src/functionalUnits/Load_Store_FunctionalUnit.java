package functionalUnits;

import memoryHierarchy.Memory;

public class Load_Store_FunctionalUnit  extends FunctionalUnit {
	int cycles;
	//Memory m = new Memory();
	
public Load_Store_FunctionalUnit(int cycles){
	 this.cycles=cycles;
	 
 }
 public int execute(String opp,int R1, int R2){
	 if(opp.toLowerCase().equals("load")){
		return load (R1,R2);
	 }
	 else{
		return store(R1,R2);
	 }
	
 }
 public int  load (int R1, int R2){
	 return R1+R2;
	 
 }
 public int store(int R1,int R2){
	 return R1+R2;
 }
 
}
