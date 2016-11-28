package tomasulo;

import tomasulo.ReOrderBuffer;
import tomasulo.ROBEntry;
import tomasulo.InstructionBuffer;
import tomasulo.RegisterStatusTable;
import tomasulo.RegisterFile;

import memoryHierarchy.*;

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
	 InstructionBuffer instruction_buffer;
	int PC, endOfPC;
	int pipelineWidth;
	int instruction_issued ;
	int instruction_finished ;
	int branche;
    int mispredictions_branch;
   
	
	
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
		
	}
	
	public void writeResult() {
		
	}
	
	public void commit() {
		for(int i = 0; i < this.instruction_issued; i++) {
			ROBEntry ROB_entry = this.ROBuffer.getEntry();
			if(ROB_entry == null)
				return;
			
			int dest = ROB_entry.getInstructionDestination(); //Reg or Mem Address if store
			if(ROB_entry.isReady()) { // if ready 
				if(ROB_entry.getInstructionType().toLowerCase().equals("store")) { 
					this.ROBuffer.deQueue();
					instruction_finished++;
				}
				else if(ROB_entry.getInstructionType().toLowerCase().equals("jmp")) {
					this.ROBuffer.deQueue();
					instruction_finished++;
				}
				else if(ROB_entry.getInstructionType().toLowerCase().equals("return")) {
					this.ROBuffer.deQueue();
					instruction_finished++;
				}
				else if(ROB_entry.getInstructionType().toLowerCase().equals("branch")) {
					this.branche++;
					instruction_finished++;
					if(ROB_entry.getInstructionValue()  == 1) { // if Branch was predicted correctly
						this.ROBuffer.deQueue();

					} else {  // if not
						this.PC = ROB_entry.PC_value;
						this.ROBuffer.cleanRS();
						this.regStatusTable.emptyRegisterStatusTable();
						flushreservation_stations();
						this.instruction_buffer.Flush();
						//this.low_byte = null;
						//this.low_byte_set = false;
						return;
					       }
				     }
				    else {
					this.regFile.registers[dest] = binary.convertToBinary(ROB_entry.getInstructionValue());
					this.regStatusTable.reorderBufferindex[dest] = -1; 
					this.ROBuffer.deQueue();
					instruction_finished++;
				}

			}
		}
	}
	
	
	private void flushreservation_stations() {
		for (int i = 0; i < this.reservationStations.length; i++) {
			reservationStations[i].flushRS();
		}	
	}
	
	public void PrintResults(){
		
		
	}
	
	
	
	
}
