package tomasulo;

import functionalUnits.FunctionalUnit;


public class ReservationStation {
	
	String name;
	boolean busy;
	String op;
	int Vj;
	int Vk;
	int Qj;
	int Qk;
	int dest;
	int A;
	int result;
	boolean isReady;

	boolean is_executionStart;
	int toalCyclesTaken; //Number of cycles taken To execute by Functional unit
	int executionCycles_left; //cycles left in execution 
	
	boolean isAddress_calculated; //For Load Reservation Stations
	int cacheLevel; // For load instructions
	boolean isCacheAccessed;
	String lowByte;
	boolean islowSet;
	
	
	FunctionalUnit FunctionalUnit;
	

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
	
	public boolean isReady(){
		if(Qj ==0 && Qk==0){
			return true;
			
		}else return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public int getVj() {
		return Vj;
	}

	public void setVj(int vj) {
		Vj = vj;
	}

	public int getVk() {
		return Vk;
	}

	public void setVk(int vk) {
		Vk = vk;
	}

	public int getQj() {
		return Qj;
	}

	public void setQj(int qj) {
		Qj = qj;
	}

	public int getQk() {
		return Qk;
	}

	public void setQk(int qk) {
		Qk = qk;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public boolean isIs_executionStart() {
		return is_executionStart;
	}

	public void setIs_executionStart(boolean is_executionStart) {
		this.is_executionStart = is_executionStart;
	}

	public int getToalCyclesTaken() {
		return toalCyclesTaken;
	}

	public void setToalCyclesTaken(int toalCyclesTaken) {
		this.toalCyclesTaken = toalCyclesTaken;
	}

	public int getExecutionCycles_left() {
		return executionCycles_left;
	}

	public void setExecutionCycles_left(int executionCycles_left) {
		this.executionCycles_left = executionCycles_left;
	}

	public boolean isAddress_calculated() {
		return isAddress_calculated;
	}

	public void setAddress_calculated(boolean isAddress_calculated) {
		this.isAddress_calculated = isAddress_calculated;
	}

	public int getCacheLevel() {
		return cacheLevel;
	}

	public void setCacheLevel(int cacheLevel) {
		this.cacheLevel = cacheLevel;
	}

	public boolean isCacheAccessed() {
		return isCacheAccessed;
	}

	public void setCacheAccessed(boolean isCacheAccessed) {
		this.isCacheAccessed = isCacheAccessed;
	}

	public String getLowByte() {
		return lowByte;
	}

	public void setLowByte(String lowByte) {
		this.lowByte = lowByte;
	}

	public boolean isIslowSet() {
		return islowSet;
	}

	public void setIslowSet(boolean islowSet) {
		this.islowSet = islowSet;
	}

	public FunctionalUnit getFunctionalUnit() {
		return FunctionalUnit;
	}

	public void setFunctionalUnit(FunctionalUnit functionalUnit) {
		FunctionalUnit = functionalUnit;
	}

	public boolean isStartStoring() {
		return startStoring;
	}

	public void setStartStoring(boolean startStoring) {
		this.startStoring = startStoring;
	}

	public boolean isIslowBytewriten() {
		return islowBytewriten;
	}

	public void setIslowBytewriten(boolean islowBytewriten) {
		this.islowBytewriten = islowBytewriten;
	}

	public boolean isHighByteWrite() {
		return isHighByteWrite;
	}

	public void setHighByteWrite(boolean isHighByteWrite) {
		this.isHighByteWrite = isHighByteWrite;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}


}
