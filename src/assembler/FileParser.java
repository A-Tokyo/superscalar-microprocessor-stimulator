package assembler;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
public class FileParser {

	public static ArrayList<String> parseFile(String fileName){
		ArrayList<String> parsedFile = new ArrayList<String>(); 
		try{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String currLine;
			while ((currLine = reader.readLine()) != null) {
				parsedFile.add(currLine);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return parsedFile;
	}
	
//	public static void main(String[] args){
//		// TODO Auto-generated method stub
//		ArrayList<String> x =  new ArrayList<>();
//		x = parseFile("programs/program.txt");
//		for (int i = 0; i < x.size(); i++) {
//			System.out.println(x.get(i));
//		}
//	}

}
