package tomasulo;

import  functionalUnit;


public class ReservationStation {
	
	String name;
	boolean busy;
	String op;
	int Vj;
	int Vk;
	int Qj;
	int Qk;

	//int Address;
	int cycle_time;
	//int execution_left ;
	//int destination_ROB;
//	boolean busy;
//	String op;
	String name_Instruction;
	int offset;
	int Reg_Value;


	int dest;
	int A;
	int result;

	boolean is_executionStart;
	int toalCyclesTaken; //Number of cycles taken To execute by Functional unit
	int executionCycles_left; //cycles left in execution 
	
	boolean isAddress_calculated; //For Load Reservation Stations
	int cacheLevel; // For load instructions
	boolean isCacheAccessed;
	String lowByte;
	boolean islowSet;
	
	
	 functionalUnit FunctionalUnit;
	

//	int store_cycles_left;
	boolean startStoring;
	boolean islowBytewriten;
	boolean isHighByteWrite;

	
	public ReservationStation(String oneEntryName, int cycles) {
		name = oneEntryName;
		busy = false;
		op = "";
		toalCyclesTaken = cycles;
		Qj = -1;
		Qk = -1;
		isAddress_calculated = false;
		executionCycles_left = cycles;

	}
	
	public void flushRS() {
		this.busy = false;
		this.op = "";
		this.Qj = -1;
		this.Qk = -1;
		isAddress_calculated = false;
		executionCycles_left = toalCyclesTaken;
		result = 0;

	}



}
