package assembler;

import java.util.ArrayList;

import memoryHierarchy.MemoryHierarchy;
import tomasulo.Tomasulo;


public class StimulationRunner {
	private static int id=-1;
	private int currLineIndex;
	private String currLine;
	private ArrayList<String> parsedFile;
	MemoryHierarchy memoryHierarchy;

	public StimulationRunner(String fileFullPath) {
		currLineIndex = -1;
		currLine = "";
		parsedFile  = new ArrayList<String>();
		parsedFile = FileParser.parseFile(fileFullPath);
		id++;
	}

	public void run() throws Exception {
		System.out.println("Initializing Stimulator...\n");
		incrementLineLowerCase();
		if(currLine.contains("memory") && currLine.contains("hierarchy")){
			incrementLine();
			initMemoryHierarchy();
			initHardware();
			assembleToMemory();
			initProgramData();
		}else{
			System.err.println("Please initialize the Memory Hierarchy first !!");
			return;
		}
		System.out.println("\nRunning Stimulation...\n");
		//TODO stimulate program
	}

	private void initHardware() throws Exception {
		currLine = currLine.toLowerCase();
		if(!(currLine.contains("hardware") && currLine.contains("organization"))){
			System.err.println("Please initialize the hardware organization.");
			throwException("Hardware organization not initialized");
		}
		incrementLineLowerCase();
		if (!(currLine.contains("pipeline") && currLine.contains("width"))) {
			throwException("pipeLine width not initialized");
		}

		int pipeLineWidth = extractJSONvalueInt(currLine);
		incrementLineLowerCase();
		if (!(currLine.contains("size") && currLine.contains("instruction") && currLine.contains("buffer"))) {
			throwException("Instruction buffer size not initialized");
		}
		int intsructionBufferSize = extractJSONvalueInt(currLine);
		incrementLineLowerCase();
		if (!(currLine.contains("size") && currLine.contains("rob"))) {
			throwException("ROB buffer size not initialized");
		}
		int robSize = extractJSONvalueInt(currLine);
		//		System.out.println(pipeLineWidth + ", " + intsructionBufferSize + "," + robSize);
		incrementLine();
		//		getJSONValue
		int addRS = Integer.parseInt(getJSONValue(currLine, "addRS"));
		int addCycles = Integer.parseInt(getJSONValue(currLine, "addCycles"));
		incrementLine();
		int mulRS = Integer.parseInt(getJSONValue(currLine, "mulRS"));
		int mulCycles = Integer.parseInt(getJSONValue(currLine, "mulCycles"));
		incrementLine();
		int lwRS = Integer.parseInt(getJSONValue(currLine, "lwRS"));
		int lwCycles = Integer.parseInt(getJSONValue(currLine, "lwCycles"));
		incrementLine();
		int jalrRS = Integer.parseInt(getJSONValue(currLine, "jalrRS"));
		int jalrCycles = Integer.parseInt(getJSONValue(currLine, "jalrCycles"));
		System.out.println("\nHardware organization parsed successfully...\n");
		// TODO initialize tomasulo
		System.out.println("\n****TODO INITIALIZE TOMASULO AND RUN****\n");
//		System.out.println("\nHardware organization initialised successfully...\n");
		incrementLine();
	}

	private void initMemoryHierarchy() throws Exception{
		//		System.out.println("memory stuff");
		currLine.toLowerCase();
		if(!(currLine.contains("num") && currLine.contains("cache") && currLine.contains("levels") && currLine.contains(":"))){
			System.err.println("Please initialize the number of cache levels");
			throwException("Number of cache levels not initialized");
		}
		// gut number of cache levels
		final int numOfCacheLevels = Integer.parseInt(currLine.substring(currLine.indexOf(":")+1).trim());
		incrementLine();
		// get cache descriptions
		String [] cachesDescription =  new String[numOfCacheLevels];
		for (int i = 0; i < cachesDescription.length; i++) {
			currLine = currLine.substring(currLine.indexOf("{")+1, currLine.indexOf("}")).trim();
			//		System.out.println(currLine);
			String [] cacheParametersArray = currLine.split(",");
			int cacheS = extractJSONvalueInt(cacheParametersArray[0]);
			//		System.out.println(Utils.arrayStringToString(cacheParametersArray));
			int cacheL = extractJSONvalueInt(cacheParametersArray[1]);
			int cacheM = extractJSONvalueInt(cacheParametersArray[2]);
			String cacheWritePolicyHit = extractJSONvalue(cacheParametersArray[3]);
			if(cacheWritePolicyHit.toLowerCase().contains("through")){
				cacheWritePolicyHit = "writeThrough";
			}else
				if(cacheWritePolicyHit.toLowerCase().contains("back")){
					cacheWritePolicyHit = "writeBack";
				}else{
					throwException ("Unsuported write policy");
				}
			String cacheWritePolicyMiss = extractJSONvalue(cacheParametersArray[4]);
			if(cacheWritePolicyMiss.toLowerCase().contains("through")){
				cacheWritePolicyMiss = "writeThrough";
			}else{
				if(cacheWritePolicyMiss.toLowerCase().contains("back")){
					cacheWritePolicyMiss = "writeBack";
				}else{
					throwException ("Unsuported write policy");
				}
			}
			int cacheCycles = extractJSONvalueInt(cacheParametersArray[5]);
			cachesDescription[i] = cacheS+","+cacheL+","+cacheM+","+cacheWritePolicyHit+","+cacheWritePolicyMiss+","+cacheCycles;
			//			System.out.println(cachesDescription[i]);
			incrementLine();
		}
		currLine = currLine.toLowerCase().trim();
		if (!(currLine.contains("memory")&&currLine.contains("cycles"))){
			throwException ("Memory Cycles syntax error");
		}
		//extract main memory cycles
		int mainMemoryCycles = extractJSONvalueInt(currLine);
		incrementLine();
		memoryHierarchy = new MemoryHierarchy(mainMemoryCycles, numOfCacheLevels, cachesDescription);
		// Memory Hierarchy Initialized
		System.out.println("\nMemory Hierarchy initialised successfully...\n");
	}

	private void assembleToMemory() throws Exception{
		currLine = currLine.toLowerCase();
		if (!currLine.contains("assembly")){
			throwException ("Can not parse program code");
		}
		incrementLine();
		if(!currLine.toLowerCase().contains(".org")){
			throwException("Assembly program origin not specified");
		}
		// parse .org
		int assemblyOrigin = Integer.parseInt(currLine.substring(currLine.indexOf("g")+1).trim());
		incrementLine();
		// assemble program
		int memIndex = assemblyOrigin;
		Assembler assembler = new Assembler(assemblyOrigin);
		while(!currLine.toLowerCase().trim().contains("endassembly") && memIndex<65536){
			//			System.out.println(currLine);
			//			System.out.println(memIndex+ ","+ Assembler.assemble(currLine));
			String instructionBinary = assembler.assemble(currLine);
			if(instructionBinary!=null){
				memoryHierarchy.memory.write(memIndex, instructionBinary);	
				memIndex++;
			}
			incrementLine();
		}
		System.out.println("\nProgram code was assembled and added to memory successfully...\n");
		incrementLine();
	}

	private void initProgramData() throws Exception{
		if(!(currLine.contains("prog") && currLine.contains("data"))){
			return;
		}
		incrementLine();
		while(!(currLine.toLowerCase().trim().contains("end") && currLine.toLowerCase().contains("data"))){
			String [] addressDataPair = currLine.split(":");
			int address = Integer.parseInt(addressDataPair[0].trim());
			String data = addressDataPair[1].trim();
			//			System.out.println(address + "," + data);
			if (!data.matches("^[01]+$")) {
				throwException("data is not binary");
			}
			if(data.length()!=16){
				throwException("invalid data bit length");
			}
			if (address<0 || address>=65536) {
				throwException("invalid memory address");
			}
			memoryHierarchy.memory.write(address, data);
			incrementLine();
		}
		System.out.println("\nProgram Data added to memory successfully...\n");
		incrementLine();
	}

	private String extractJSONvalue(String JSONstring) {
		return JSONstring.substring(JSONstring.indexOf(":")+1).trim();
	}

	private int extractJSONvalueInt(String JSONstring) {
		return Integer.parseInt(JSONstring.substring(JSONstring.indexOf(":")+1).trim());
	}

	private void incrementLine() {
		if(currLineIndex+1 >= parsedFile.size()){
			return;
		}
		currLineIndex++;
		currLine = parsedFile.get(currLineIndex);
		if(currLine.trim().startsWith("//")){
			incrementLine();
		}
	}

	private String getJSONValue(String JSON, String key) throws Exception{
		JSON = JSON.trim();
		if (JSON.charAt(0)!='{' || JSON.charAt(JSON.length()-1)!='}') {
			throwException(JSON+" is not a valid JSON");
		}
		String mutatedJSON = JSON.substring(1, JSON.length()-1);
		key = key.trim();
		try{
			String [] splitted = mutatedJSON.split(",");
			for (int i = 0; i < splitted.length; i++) {
				splitted[i] = splitted[i].trim();
				if(splitted[i].substring(0, splitted[i].indexOf(":")).equals(key)){
					return splitted[i].substring(splitted[i].indexOf(":")+1, splitted[i].length()).trim();
				}
			}	
		}catch(Exception e){
			throwException(JSON+" is not a valid JSON");
		}
		return null;
	}

	private void incrementLineLowerCase() {
		incrementLine();
		currLine = currLine.toLowerCase();
	}

	private void throwException(String text) throws Exception {
		System.err.println("Parsing error near line: " + currLineIndex+1);
		System.err.println(currLine);
		throw new Exception(text);
	}

	public static int getId() {
		return id;
	}
}