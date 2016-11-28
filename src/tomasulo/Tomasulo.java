package tomasulo;

//import tomasulo.ReOrderBuffer;

import memoryHierarchy.*;

import java.util.ArrayList;
import java.util.Arrays;

import functionalUnits.*;

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
	String lowByte;
	boolean lowByteIsSet;
	
	int howMany_instructionsFinishExecuting; // Number of instructions executed
	int howMany_MispredictionsHappen;
	ArrayList<Integer> IndicesOfRS_ToIssued;
	Instruction jumpInstruction;
	boolean stallOfJump;
	
	
	
	public static final short fetchDelay  = 1;
	public static final short issueDelay  = 1;
	public static final short writeDelay  = 1;
	public static final short commitDelay = 1;
	
	
	// constructor for an instance of the tomasulo object; should be called from main
	// using parameters entered by the user
	public Tomasulo(int sizeOfROBuffer, int sizeOfInstructionBuffer, String[] infoOfFunctionalUnits, int PC, int endOfPC, int width) {
		this.regFile = new RegisterFile(8);
		this.regStatusTable = new RegisterStatusTable(8);
		this.ROBuffer = new ReOrderBuffer(sizeOfROBuffer);
		this.instructionBuffer = new InstructionBuffer(sizeOfInstructionBuffer);
		IndicesOfRS_ToIssued = new ArrayList<Integer>();
		this.PC = PC;
		lowByteIsSet = false;
		this.endOfPC = endOfPC + 2; // to account for last instruction
		this.pipelineWidth = width;
		
		
		int numberOfReservationStations = 0;
		
		// format INSTRUCTION,#OfFunctionalUnits,#OfCyclesTakenByInstruction
		for (String eachFunctionalUnit : infoOfFunctionalUnits) {
			String[] DetailsPerUnit = eachFunctionalUnit.split(",");
			numberOfReservationStations += Integer.parseInt(DetailsPerUnit[1]);
		}
		
		this.reservationStations = new ReservationStation[numberOfReservationStations];
		int count = 0;
		for (String eachFunctionalUnit : infoOfFunctionalUnits) {
			String[] DetailsPerUnit = eachFunctionalUnit.split(",");
			String instructionType = DetailsPerUnit[0].toUpperCase();
			int numberOfFunctionalUnits = Integer.parseInt(DetailsPerUnit[1]);
			int numberOfCyclesRequired = Integer.parseInt(DetailsPerUnit[2]);
			
			for (int i = 0; i < numberOfFunctionalUnits; i++) {
				this.reservationStations[count] = new ReservationStation(instructionType + i + 1, numberOfCyclesRequired);
				
				switch (instructionType) {
				case "MUL":
					this.reservationStations[count++].FunctionalUnit = new MultiplyFunctionalUnit(numberOfCyclesRequired);
					break;
				case "ADD":
					this.reservationStations[count++].FunctionalUnit = new Add_Sub_FunctinalUnit(numberOfCyclesRequired);
					break;
				case "JALR":
					this.reservationStations[count++].FunctionalUnit = new JALRFunctionalUnit(numberOfCyclesRequired);
					break;
				case "LW":
					this.reservationStations[count++].FunctionalUnit = new Load_Store_FunctionalUnit(numberOfCyclesRequired);
					break;
				}
			}
		}
		
	}
	
	
	public void resetLowByte() {
		this.lowByteIsSet = false;
		this.lowByte = null;
	}
	
	// need to implement the phases of Tomasulo's algorithm: fetch -> issue -> execute -> write result -> commit
	// and any other methods necessary to make them work
	
	public String getInstructionFromMemory(int instructionAddress) {
		String highByte;
		String instructionAddressInBinary = Integer.toBinaryString(instructionAddress);
		if (instructionAddressInBinary.length() < 16) {
			char[] zeroes = new char[16 -instructionAddressInBinary.length()];
			Arrays.fill(zeroes, '0');
			instructionAddressInBinary = new String(zeroes) + instructionAddressInBinary;
		}
		if (lowByteIsSet) {
			instructionAddressInBinary = Integer.toBinaryString(instructionAddress + 1);
			if (instructionAddressInBinary.length() < 16) {
				char[] zeroes = new char[16 -instructionAddressInBinary.length()];
				Arrays.fill(zeroes, '0');
				instructionAddressInBinary = new String(zeroes) + instructionAddressInBinary;
			}
			highByte = this.memoryHierarchy.fetchInstruction(instructionAddressInBinary);
			if (highByte == null) {
				return highByte;
			}
		} else {
			this.lowByte = this.memoryHierarchy.fetchInstruction(instructionAddressInBinary);
			if (this.lowByte == null) {
				return this.lowByte;
			} else {
				this.lowByteIsSet = true;
				return null;
			}
		}
		String fullInstruction = highByte + this.lowByte;
		resetLowByte();
		return fullInstruction;
		
	}
	
	public boolean fetchBranch(Instruction instruction) {
		if (instruction.function_Type.toUpperCase().equals("BRANCH") && instruction.arithmeticOpCode < 0) {
			instruction.PC_pointer = this.PC + 2;
			this.PC += instruction.arithmeticOpCode + 2;
			return true;
		} else {
			instruction.PC_pointer = this.PC + instruction.arithmeticOpCode + 2;
			return false;
		}
	}
	
	// needs checking; used to implement the fetchReturn and fetchJump methods (as a skeleton with some modifications)
	public boolean fetchJALR(Instruction instruction) {
		if (instruction.function_Type.toUpperCase().equals("JALR")) {
			instruction.PC_pointer = this.PC + 2;
			this.jumpInstruction = instruction;
			int indexInROB = this.regStatusTable.reorderBufferindex[instruction.rs];
			if (indexInROB == -1) {
				this.PC = Integer.parseInt(this.regFile.registers[instruction.rs], 2);
				this.stallOfJump = false;
			} else {
				if (this.ROBuffer.buffer[indexInROB].isReady()) {
					this.PC = this.ROBuffer.buffer[indexInROB].getInstructionValue();
					this.stallOfJump = false;
				} else {
					this.stallOfJump = true;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean fetchJump(Instruction instruction) {
		if (instruction.function_Type.toUpperCase().equals("JUMP")) {
			this.jumpInstruction = instruction;
			int indexInROB = this.regStatusTable.reorderBufferindex[instruction.rs];
			if (indexInROB == -1) {
				this.PC += Integer.parseInt(this.regFile.registers[instruction.rs], 2) + instruction.arithmeticOpCode + 2;
				this.stallOfJump = false;
			} else {
				if (this.ROBuffer.buffer[indexInROB].isReady()) {
					this.PC += this.ROBuffer.buffer[indexInROB].getInstructionValue() + instruction.arithmeticOpCode + 2;
					this.stallOfJump = false;
				} else {
					this.stallOfJump = true;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean fetchReturn(Instruction instruction) {
		if (instruction.function_Type.toUpperCase().equals("RETURN")) {
			int indexInROB = this.regStatusTable.reorderBufferindex[instruction.rs];
			if (indexInROB == -1) {
				this.PC = Integer.parseInt(this.regFile.registers[instruction.rs], 2);
				this.stallOfJump = false;
			} else {
				if (this.ROBuffer.buffer[indexInROB].isReady()) {
					this.PC = this.ROBuffer.buffer[indexInROB].getInstructionValue();
					this.stallOfJump = false;
				} else {
					this.stallOfJump = true;
				}
			}
			return true;
		}
		return false;
	}
	
	public void fetch() {
		if (stallOfJump) {
			if (fetchBranch(this.jumpInstruction)) {
				return;
			} else if (fetchJALR(this.jumpInstruction)) {
				return;
			} else if (fetchJump(this.jumpInstruction)) {
				return;
			} else {
				fetchReturn(this.jumpInstruction);
				return;
			}
		} else if (!(this.instructionBuffer.Full_Instruction_Buffer()) && PC < endOfPC) {
			String instructionInMemory = getInstructionFromMemory(this.PC);
			if (instructionInMemory != null) {
				Instruction instruction = new Instruction(instructionInMemory);
				
				this.instructionBuffer.Add_To_Instruction_Buffer(instruction);
				if (fetchBranch(instruction)) {
					return;
				} else if (fetchJALR(instruction)) {
					return;
				} else if (fetchJump(instruction)) {
					return;
				} else {
					fetchReturn(instruction);
					return;
				}
			}
		}
	}

	public int getAvailableStation(Instruction instruction) {
		String instructionType = "";
		if (instruction.function_Type.toUpperCase().equals("NAND")
		|| instruction.function_Type.toUpperCase().equals("ADD")
		|| instruction.function_Type.toUpperCase().equals("SUBTRACT")
		|| instruction.function_Type.toUpperCase().equals("BRANCH")
		|| instruction.function_Type.toUpperCase().equals("ADDI")) {
			instructionType = "add";
		} else {
			if (instruction.function_Type.toUpperCase().equals("MULTIPLY")) {
				instructionType = "mul";
			} else if (instruction.function_Type.toUpperCase().equals("JALR")) {
				instructionType = "jalr";
			} else if (instruction.function_Type.toUpperCase().equals("LOAD")
					|| instruction.function_Type.toUpperCase().equals("STORE")) {
				instructionType = "lw";
			} 
		}
		int position = 0;
		for (ReservationStation station : this.reservationStations) {
			if (!station.busy && station.name.toUpperCase().contains(instructionType)) {
				return position;
			} else {
				position++;
			}
				
		}
		// no available station for the instruction
		return -1;
	}
	
	public void readyRs(Instruction instruction, ReservationStation station) {
		if (this.regStatusTable.reorderBufferindex[instruction.rs] == -1) {
			station.Vj = Integer.parseInt(this.regFile.registers[instruction.rs], 2);
			station.Qj = -1;
		} else {
			int indexInROB = this.regStatusTable.reorderBufferindex[instruction.rs];
			if (this.ROBuffer.buffer[indexInROB].isReady()) {
				station.Vj = this.ROBuffer.buffer[indexInROB].getInstructionValue();
				station.Qj = -1;
			} else {
				station.Qj = indexInROB;
			}
		}
	}
	
	public void readyRt(Instruction instruction, ReservationStation station) {
		if (!(instruction.function_Type.toUpperCase().equals("ADDI"))) {
			if (this.regStatusTable.reorderBufferindex[instruction.rt] == -1) {
				station.Vk = Integer.parseInt(this.regFile.registers[instruction.rt], 2);
				station.Qk = -1;
			} else {
				int indexInROB = this.regStatusTable.reorderBufferindex[instruction.rt];
				if (this.ROBuffer.buffer[indexInROB].isReady()) {
					station.Vk = this.ROBuffer.buffer[indexInROB].getInstructionValue();
					station.Qk = -1;
				} else {
					station.Qk = indexInROB;
				}
			}
		} else {
			station.Vk = instruction.arithmeticOpCode;
			station.Qk = -1;
		}
	}
	
	
	public void issue() {
		for (int i = 0; i < this.pipelineWidth; i++) {
			if (this.instructionBuffer.Empty_Instruction_Buffer()) {
				return;
			} else {
				Instruction instruction = this.instructionBuffer.peakHead();
				if (instruction.function_Type.toUpperCase().equals("RETURN") || instruction.function_Type.toUpperCase().equals("JUMP")) {
					if (this.ROBuffer.buffer[this.ROBuffer.getTailPosition()] != null) {
						return;
					} else {
						ROBEntry someEntry = new ROBEntry(instruction.function_Type, -1, true);
						this.ROBuffer.enQueue(someEntry);
						this.instructionBuffer.remove_element_from_Instruction_Buffer();
					}
				} else {
					if (this.getAvailableStation(instruction) != -1 && this.ROBuffer.buffer[ROBuffer.getTailPosition()] == null) {
						this.instructionBuffer.remove_element_from_Instruction_Buffer();
						ReservationStation station = this.reservationStations[this.getAvailableStation(instruction)];
						IndicesOfRS_ToIssued.add(this.getAvailableStation(instruction));
						int indexInROB = this.ROBuffer.getTailPosition();
						station.op = instruction.function_Type;
						station.busy = true;
						if (instruction.function_Type.toUpperCase().equals("JALR")) {
							station.dest = indexInROB;
							station.Vk = instruction.PC_pointer;
							ROBEntry someEntry = new ROBEntry(instruction.function_Type, instruction.rd);
							this.ROBuffer.enQueue(someEntry);
							this.regStatusTable.reorderBufferindex[instruction.rd] = indexInROB;
						} else {
							readyRs(instruction, station);
							if (station.Qj == -1 && station.Qk == -1) {
								station.is_executionStart = true;
							}
							if (instruction.function_Type.toUpperCase().equals("STORE")) {
								readyRt(instruction, station);
								station.A = instruction.arithmeticOpCode;
								station.dest = indexInROB;
								ROBEntry someEntry = new ROBEntry(instruction.function_Type, -1);
								this.ROBuffer.enQueue(someEntry);
							} else if (instruction.function_Type.toUpperCase().equals("LOAD")) {
								station.A = instruction.arithmeticOpCode;
								this.regStatusTable.reorderBufferindex[instruction.rd] = indexInROB;
								station.dest = indexInROB;
								ROBEntry someEntry = new ROBEntry(instruction.function_Type, instruction.rd);
								this.ROBuffer.enQueue(someEntry);
							} else if (instruction.function_Type.toUpperCase().equals("BRANCH")) {
								readyRt(instruction, station);
								station.dest = indexInROB;
								station.A = instruction.arithmeticOpCode;
								ROBEntry someEntry = new ROBEntry(instruction.function_Type, -1);
								someEntry.setPC(instruction.PC_pointer);
								this.ROBuffer.enQueue(someEntry);
							} else {
								readyRt(instruction, station);
								station.dest = indexInROB;
								ROBEntry someEntry = new ROBEntry(instruction.function_Type, instruction.rd);
								this.ROBuffer.enQueue(someEntry);
								this.regStatusTable.reorderBufferindex[instruction.rd] = indexInROB;
							}
							
						}
					}
				}
			}
		}
	}
	
	public void execute() {
		
	}
	
	public void writeResult() {
		
	}
	
	public void commit() {
		
	}
	
	
}
