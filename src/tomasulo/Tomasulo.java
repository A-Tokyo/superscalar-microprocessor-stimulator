package tomasulo;

//import tomasulo.ReOrderBuffer;

import java.util.ArrayList;

import memoryHierarchy.*;
// import f

public class Tomasulo {
	
	// NOTE: according to the project specification, 
	// the tomasulo object needs to have the following parameters:
	// A memory hierarchy
	// a reorder buffer [which will contain the ROBEntries] (size specified by the user)
	// an instruction buffer (size specified by user)
	// a list of reservation stations (size specified by the user, via details of )
	// a register file, a register status table
	// a PC (the start point of which will be specified by the user)
	// Pipeline Width (specified by the user - number of maximum allowed simaltenuous instructions issued to reservation stations
	// a list of data items that specify the value and memory address
	// a list of the amount of reservation stations for each class of instructions (assume 1:1 station:functional unit map; specified by user)
	// the register status table
	// the register file
	
	MemoryHierarchy memoryHierarchy;
	ReOrderBuffer ROBuffer;
	InstructionBuffer instructionBuffer;
	ReservationStation[] reservationStations;
	RegisterFile regFile;
	RegisterStatusTable regStatusTable;
	int PC, endOfPC;
	int pipelineWidth;
	int howMany_instructionsFinishExecuting; // Number of instructions executed
	int howMany_MispredictionsHappen;
	ArrayList<Integer> IndicesOfRS_ToIssued; // Indices of RS(s) in the order which they were issued 
	
	public static final short fetchDelay  = 1;
	public static final short issueDelay  = 1;
	public static final short writeDelay  = 1;
	public static final short commitDelay = 1;
	
	
	// constructor for an instance of the tomasulo object; should be called from main
	// using parameters entered by the user
	public Tomasulo(int sizeOfROBuffer, int sizeOfInstructionBuffer, String[] infoOfFunctionalUnits, int PC, int endOfPC, int width) {
		
// 		this.ROBuffer = new ReOrderBuffer(
	}
	
	
	// need to implement the phases of Tomasulo's algorithm: fetch -> issue -> execute -> write result -> commit
	// and any other methods necessary to make them work
	
	public void fetch() {
		
	}
	
	public void issue() {
		
	}
	
	public void execute() { 

		for(int i = 0; i < this.reservationStations.length; i++) {
			ReservationStation temp_reservationStations = this.reservationStations[i];
			if(temp_reservationStations.busy) {
				if(temp_reservationStations.op.toLowerCase().equals("load")) {
					if(temp_reservationStations.Qj == -1) {
						String address = Integer.toBinaryString(temp_reservationStations.A);
						while(address.length() < 16)
							address = "0" + address;
						String address1 = Integer.toBinaryString(temp_reservationStations.A + 1);
						while (address1.length() < 16)
							address1 = "0" + address1;
						
						if(temp_reservationStations.islowSet)
							address = address1;
						
						if(temp_reservationStations.isAddress_calculated && Check_ifStore(temp_reservationStations.A)) {
							if(temp_reservationStations.cacheLevel == this.memoryHierarchy.caches.length) { // memory
								if(!this.memoryHierarchy.memory.isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
									temp_reservationStations.isCacheAccessed = true;
									if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
										temp_reservationStations.cacheLevel--;
										temp_reservationStations.isCacheAccessed = false;
									}
								}
							}
							else if(!this.memoryHierarchy.caches[temp_reservationStations.cacheLevel].isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
								temp_reservationStations.isCacheAccessed = true;
								if(temp_reservationStations.cacheLevel == 1) {
									String Byte = this.memoryHierarchy.loadData(address);
									if(Byte != null) {
										temp_reservationStations.isCacheAccessed = false;
										if(!temp_reservationStations.islowSet) {
											temp_reservationStations.lowByte = Byte;
											temp_reservationStations.islowSet = true;
											temp_reservationStations.cacheLevel = this.memoryHierarchy.getCorrespondingCacheLevel(address1);
										}else {
											String result = Byte + temp_reservationStations.lowByte;
											temp_reservationStations.result = Integer.parseInt(convert_to_Decimal(result));
											temp_reservationStations.isAddress_calculated = false;
											howMany_instructionsFinishExecuting++;
											temp_reservationStations.lowByte = null;
											temp_reservationStations.islowSet = false;
											temp_reservationStations.executionCycles_left = 0;
										}
									}
								}
								else if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
									temp_reservationStations.cacheLevel--;
									temp_reservationStations.isCacheAccessed = false;
								}
							}
						}
						else {
							temp_reservationStations.A = temp_reservationStations.FunctionalUnit.execute("calculate_addr", temp_reservationStations.Vj, temp_reservationStations.A);
							temp_reservationStations.isAddress_calculated = true;
							temp_reservationStations.cacheLevel = this.memoryHierarchy.getCorrespondingCacheLevel(address);
						}
					}
				}
				else if(temp_reservationStations.op.toLowerCase().equals("store")) {
					if(temp_reservationStations.Qj == -1 && !temp_reservationStations.startStoring) {
						this.ROBuffer.getBuffer()[temp_reservationStations.dest].setInstructionDestination ( temp_reservationStations.Vj + temp_reservationStations.A); // has2lfeha omar
						String address = Integer.toBinaryString(temp_reservationStations.Vj + temp_reservationStations.A);
						while (address.length() < 16)
							address = "0" + address;
						temp_reservationStations.cacheLevel = memoryHierarchy.getCorrespondingCacheLevel(address);
//						temp_reservationStations.store_cycles_left = mem_heirarchy.store_cycles_left(address);
						temp_reservationStations.startStoring = true;
						howMany_instructionsFinishExecuting++;
					}
				}
				else if(temp_reservationStations.op.toLowerCase().equals("jalr")) {
					temp_reservationStations.result = temp_reservationStations.Vk;
					temp_reservationStations.executionCycles_left = 0;
					howMany_instructionsFinishExecuting++;
				}
				else { //Arithmetic Operations and Branch
				if(temp_reservationStations.is_executionStart) {
					if(temp_reservationStations.executionCycles_left > 0)
						temp_reservationStations.executionCycles_left--;
					if(temp_reservationStations.executionCycles_left == 0) {
						temp_reservationStations.result = temp_reservationStations.FunctionalUnit.execute(temp_reservationStations.op, temp_reservationStations.Vj, temp_reservationStations.Vk);
						howMany_instructionsFinishExecuting++;
						if(temp_reservationStations.op.toLowerCase().equals("branch")) {
							if(temp_reservationStations.result == 1 && (temp_reservationStations.A < 0)) // 2 operands are equal, and Branch was taken
								temp_reservationStations.result = 1; // Correct Prediction
							else if(temp_reservationStations.result == 1 && (temp_reservationStations.A >= 0)) { // 2 operands are equal, and Branch was not taken
								temp_reservationStations.result = 0; // howMany_MispredictionsHappen
								this.howMany_MispredictionsHappen++;
							}
							else if(temp_reservationStations.result == 0 && (temp_reservationStations.A < 0)) { // 2 operands are not equal and Branch was taken
								temp_reservationStations.result = 0;
								this.howMany_MispredictionsHappen++;
							}
							else if(temp_reservationStations.result == 0 && (temp_reservationStations.A >= 0))  // 2 operands are not equal and branch was not taken
								temp_reservationStations.result = 1; // Correct Prediction
						}
					}
				}
				else if (temp_reservationStations.Qj == -1 && temp_reservationStations.Qk == -1) {
					temp_reservationStations.is_executionStart = true;
				}
			}
		}
	}
	}

	
	public void write() {
		int writes = 0; // Number of instruction that can be written
		for(int i = 0; i < this.IndicesOfRS_ToIssued.size(); i++ ) {
			ReservationStation temp_reservationStations = this.reservationStations[IndicesOfRS_ToIssued.get(i)];
			if(temp_reservationStations.op.toLowerCase().equals("store")) {
				if(temp_reservationStations.Qk == -1) {
					if(temp_reservationStations.startStoring) {
						String address = Integer.toBinaryString(temp_reservationStations.A);
						while(address.length() < 16)
							address = "0" + address;

						if(temp_reservationStations.islowBytewriten) {
							LowByteStoring(temp_reservationStations.Vk, temp_reservationStations.A);
							temp_reservationStations.islowBytewriten = false;
							temp_reservationStations.isHighByteWrite = true;
						}else if(temp_reservationStations.isHighByteWrite) {
							HighByteStoring(temp_reservationStations.Vk, temp_reservationStations.A + 1);
							int b = temp_reservationStations.dest;
							this.ROBuffer.getBuffer()[b].setInstructionValue(temp_reservationStations.Vk);
							this.ROBuffer.getBuffer()[b].setReady( true);
							
							temp_reservationStations.startStoring = false;
							temp_reservationStations.flushRS();
							temp_reservationStations.isHighByteWrite = false;	
											
							IndicesOfRS_ToIssued.remove(i);
							writes++;
							if(writes == this.instruction_issued)
								return;
							else
								i--;
						}
						else if(temp_reservationStations.cacheLevel == this.memoryHierarchy.caches.length) { // memory
							if(!this.memoryHierarchy.memory.isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
								temp_reservationStations.isCacheAccessed = true;
								if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
									temp_reservationStations.cacheLevel--;
									temp_reservationStations.isCacheAccessed = false;
								}
							}
						}
						else if(!this.memoryHierarchy.caches[temp_reservationStations.cacheLevel].isBeingAccessed() || temp_reservationStations.isCacheAccessed) {
							temp_reservationStations.isCacheAccessed = true;
							if(temp_reservationStations.cacheLevel == 1 && this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
								temp_reservationStations.islowBytewriten = true;
							}
							else if(this.memoryHierarchy.getCacheCyclesRemaining(temp_reservationStations.cacheLevel, address) == 0) {
								temp_reservationStations.cacheLevel--;
								temp_reservationStations.isCacheAccessed = false;
							}
						}
					}
				}
			}
			// All instructions but Store
			else if(temp_reservationStations.executionCycles_left == 0) {
				int b = temp_reservationStations.dest;
				
				this.ROBuffer.getBuffer()[b].setReady(true);
				this.ROBuffer.getBuffer()[b].setInstructionValue( temp_reservationStations.result);
				
				for (int j = 0; j < this.reservationStations.length; j++) {
					ReservationStation RS2 = this.reservationStations[j];
					if(RS2.Qj == b) {
						RS2.Vj = temp_reservationStations.result;
						RS2.Qj = -1;
					}
					if(RS2.Qk == b) {
						RS2.Vk = temp_reservationStations.result;
						RS2.Qk = -1;
					}
				}
				temp_reservationStations.flushRS();
				IndicesOfRS_ToIssued.remove(i);
				writes++;
				if(writes == this.instruction_issued)
					return;
				else
					i--;
			}
		}
	}
	
	public void commit() {
		
	}
	
	public boolean Check_ifStore(int A) {
		//Checks that all stores have a different memory address, then the Load mem address
		for(int i = 0; i < this.ROBuffer.getBuffer().length; i++) {
			ROBEntry b = this.ROBuffer.getBuffer()[i];
			if(b!= null && b.getInstructionType().toLowerCase().equals("store") && b.getInstructionDestination() == A)
				return false;
		}
		return true;
	}
	
	private void LowByteStoring(int vk, int A) {
		String address = Integer.toBinaryString(A);
		while (address.length() < 16)
			address = "0" + address;
		
		String value = convert_To_Binary(vk);
		this.memoryHierarchy.write(address, value.substring(8));
	}
	
	private void HighByteStoring(int vk, int A) {
		String address = Integer.toBinaryString(A);
		while (address.length() < 16)
			address = "0" + address;
		
		String value = convert_To_Binary(vk);
		this.memoryHierarchy.write(address, value.substring(0,8));
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
	
	
}
