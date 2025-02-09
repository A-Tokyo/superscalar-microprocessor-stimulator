package assembler;
import java.util.Arrays;
import java.util.Hashtable;

import utils.Utils;

public class Assembler {
	
	private static final int lineSizeInBits = 16;
	private Hashtable<String, Integer> labelAddress;
	private int currMemoryAddress;
	
	public Assembler(int assemblyOrigin) {
		this.currMemoryAddress = assemblyOrigin;
		labelAddress = new Hashtable<String, Integer>();
	}
	/*
	 * This one takes an assembly instruction in string form, case insensitive and space insensitive
	 * Returns A string containing the corresponding binary machine code in 16 bits
	 * Exceptions thrown: illegal argument exception if the register entered is not between reg0 and reg7 inclusive
	 * or if the instruction is not the microprocessor's instruction set
	 * 
	 * Instructions Go as follows
	 * lw: 000 , sw 001, jmp 010, beq 011, jalr 100,  ret 101, addi 110, Arithmetic 111
	 * For arithmetic instrction the op codes are:  add: 0000 , sub: 0001, mul: 0010, nand 0011
	 */
	public String assemble(String instruction){
		StringBuilder machineCodeBuilder = new StringBuilder();
		instruction = instruction.trim().toLowerCase();
		// check for labels and return null if this is not an instruction but a label
		if(instruction.charAt(0)=='@'){
			int splitIndex = instruction.indexOf(" ");
			labelAddress.put(instruction.substring(1, splitIndex==-1?instruction.length():splitIndex), currMemoryAddress);
			return null;
		}
		String keyword = instruction.substring(0, instruction.indexOf(" "));
		String [] instructionOperands = instruction.substring(instruction.indexOf(" ")+1, instruction.length()).split(",");
		// just like javascript maping, trimming white spaces from all elements off array
		Arrays.stream(instructionOperands).map(String::trim).toArray(unused -> instructionOperands);
		convertLabels(instructionOperands);
		// for labels		
		switch (keyword) {
		case "lw":
			machineCodeBuilder.append("000");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(Utils.decimalToBinary(Integer.parseInt(instructionOperands[2]), 7));
			break;
		case "sw":
			machineCodeBuilder.append("001");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(Utils.decimalToBinary(Integer.parseInt(instructionOperands[2]), 7));
			break;
		case "jmp":
			machineCodeBuilder.append("010");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(Utils.decimalToBinary(Integer.parseInt(instructionOperands[1]), 7));
			machineCodeBuilder.append(Utils.generateMask(lineSizeInBits-machineCodeBuilder.length()));
			break;
		case "beq":
			machineCodeBuilder.append("011");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(Utils.decimalToBinary(Integer.parseInt(instructionOperands[2]), 7));
			break;
		case "jalr":
			machineCodeBuilder.append("100");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(Utils.generateMask(lineSizeInBits-machineCodeBuilder.length()));
			break;
		case "ret":
			machineCodeBuilder.append("101");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(Utils.generateMask(lineSizeInBits-machineCodeBuilder.length()));
			break;
		case "addi":
			machineCodeBuilder.append("110");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(Utils.decimalToBinary(Integer.parseInt(instructionOperands[2]), 7));
			break;
		case "add":
			machineCodeBuilder.append("111");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[2]));
			machineCodeBuilder.append("0000");
			break;
		case "sub":
			machineCodeBuilder.append("111");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[2]));
			machineCodeBuilder.append("0001");
			break;
		case "mul":
			machineCodeBuilder.append("111");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[2]));
			machineCodeBuilder.append("0010");			
			break;
		case "nand":
			machineCodeBuilder.append("111");
			machineCodeBuilder.append(registerToBinary(instructionOperands[0]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[1]));
			machineCodeBuilder.append(registerToBinary(instructionOperands[2]));
			machineCodeBuilder.append("0011");
			break;
		default: throw new IllegalArgumentException("Syntax Error, Invalid Instruction");
		}
//		System.out.println(keyword);
//		System.out.println(Utils.arrayStringToString(instructionOperands));
//		System.out.println(currMemoryAddress);
		incrementCurrMemoryAddress();
		return machineCodeBuilder.toString();
	}

	private void convertLabels(String[] instructionOperands) {
		for (int i = 0; i < instructionOperands.length; i++) {
			if (instructionOperands[i].indexOf('@')==0) {
				String label = instructionOperands[i].substring(1,instructionOperands[i].length());
				int labelAddressDecimal = labelAddress.get(label);
				instructionOperands[i] = labelAddressDecimal+"";
			}
		}
	}
	
	public static String registerToBinary(String register) {
		register = register.toLowerCase();
		if (!register.startsWith("reg")){
			throw new IllegalArgumentException("Syntax Error in Register field");
		}
		switch (Integer.parseInt(register.substring(3))) {
		case 0:
			return "000";
		case 1:
			return "001";
		case 2:
			return "010";
		case 3:
			return "011";
		case 4:
			return "100";
		case 5:
			return "101";
		case 6:
			return "110";
		case 7:
			return "111";
		}
		throw new IllegalArgumentException("Syntax Error, Invalid Register Number");
	}
	
	private void incrementCurrMemoryAddress(){
		currMemoryAddress++;
	}
	
//	public static void main(String[] args) {
//				0101110000011000
//		System.out.println(assemble(" mul reg5, reg5, reg7"));
//	}
}
