package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import assembler.StimulationRunner;
 
 public class Main {
	private static String fileName="programs/program3.txt";
 	
	private static void getInput() throws IOException {
 		System.out.println("Please enter the file name:");
		 BufferedReader br = new BufferedReader(new
				 InputStreamReader(System.in));
		 String thisLine;
		 while((thisLine=br.readLine())!=null){
			fileName = thisLine.trim();
			break;
		 }
		 br.close();
 		System.out.println("\n");
	}
	
	public static void main(String[] args) throws Exception {
// 		getInput();
  		StimulationRunner stimulationRunner = new StimulationRunner(fileName);
 		stimulationRunner.run();
 	}
 }