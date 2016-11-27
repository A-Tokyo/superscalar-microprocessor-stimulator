package assembler;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
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
		}catch(IOException e){
			e.printStackTrace();
		}
		return parsedFile;
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ArrayList<String> x;
//		x = parseFile("LICENSE.txt");
//		for (int i = 0; i < x.size(); i++) {
//			System.out.println(x.get(i));
//		}
//	}

}
