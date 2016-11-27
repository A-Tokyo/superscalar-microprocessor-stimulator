package assembler;

import java.util.ArrayList;

import memoryHierarchy.MemoryHierarchy;
import tomasulo.Tomasulo;
import utils.Utils;


public class StimulationRunner {
	private static int id=-1;
	private int currLineIndex;
	private String currLine;
	private ArrayList<String> parsedFile;
	MemoryHierarchy memoryHierarchy;

	public StimulationRunner(String fileName) {
		currLineIndex = 0;
		currLine = "";
		parsedFile  = new ArrayList<String>();
		parsedFile = FileParser.parseFile("programs/"+fileName);
		id++;

	}

	public void run() throws Exception {
		System.out.println(parsedFile.get(currLineIndex));
		currLine = parsedFile.get(currLineIndex).toLowerCase();
		if(currLine.contains("memory") && currLine.contains("hierarchy")){
			incrementLine();
			initMemoryHierarchy();
			assembleToMemory();
		}else{
			System.err.println("Please initialize the Memory Hierarchy first !!");
			return;
		}
	}

	private void initMemoryHierarchy() throws Exception{
		System.out.println("memory stuff");
		currLine.toLowerCase();
		if(!(currLine.contains("num") && currLine.contains("cache") && currLine.contains("levels") && currLine.contains(":"))){
			System.err.println("Please initialize the number of cache levels");
			throw new Exception("Number of cache levels not initialized");
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
					throw new Exception ("Unsuported write policy");
				}
			String cacheWritePolicyMiss = extractJSONvalue(cacheParametersArray[4]);
			if(cacheWritePolicyMiss.toLowerCase().contains("through")){
				cacheWritePolicyMiss = "writeThrough";
			}else{
				if(cacheWritePolicyMiss.toLowerCase().contains("back")){
					cacheWritePolicyMiss = "writeBack";
				}else{
					throw new Exception ("Unsuported write policy");
				}
			}
			int cacheCycles = extractJSONvalueInt(cacheParametersArray[5]);
			cachesDescription[i] = cacheS+","+cacheL+","+cacheM+","+cacheWritePolicyHit+","+cacheWritePolicyMiss+","+cacheCycles;
//			System.out.println(cachesDescription[i]);
			incrementLine();
		}
		currLine = currLine.toLowerCase().trim();
		if (!(currLine.contains("memory")&&currLine.contains("cycles"))){
			throw new Exception ("Memory Cycles syntax error");
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
			throw new Exception ("Can not parse program code data");
		}
		incrementLine();
		if(!currLine.toLowerCase().contains(".org")){
			throw new Exception("Assembly program origin not specified");
		}
		// parse .org
		int assemblyOrigin = Integer.parseInt(currLine.substring(currLine.indexOf("g")+1).trim());
		incrementLine();
		// assemble program
		while(!currLine.toLowerCase().trim().contains("endassembly")){
//			System.out.println(currLine);
			System.out.println(Assembler.assemble(currLine));
//			Assembler.assemble(currLine);
			incrementLine();
		}
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
	//	public static void main(String[] args) {
	//		MemoryHierarchy memoryHierarchy = new MemoryHierarchy(memoryAccessTime, cacheLevels, cacheDescription)
	//		initialize("program.txt");
	//	}

}
