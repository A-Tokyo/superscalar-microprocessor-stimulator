package main;
import assembler.StimulationRunner;

public class Main {
	
	public static void main(String[] args) throws Exception {
		StimulationRunner stimulationRunner = new StimulationRunner("program.txt");
 		stimulationRunner.run();
	}
}