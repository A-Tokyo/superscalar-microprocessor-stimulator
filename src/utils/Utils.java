package utils;

public class Utils {
	
	private static final int wordSizeInBits = 16;

	public static int log2(int num){
		// Log base x of n is log n / log x
		return (int)(Math.log(num)/Math.log(2));
	}
	
	// Takes a number in decimal and returns the 16 bit binary representation in String form
	public static String decimalToBinary(int number) {
		String toReturn = Integer.toBinaryString(number);
		if (number >= 0) {
			while(toReturn.length() < wordSizeInBits)
				toReturn = '0' + toReturn;
		}
		else {
			toReturn = toReturn.substring(wordSizeInBits);
		}
		return toReturn;
	}
	
	public static String generateMask(int length) {
		String toReturn ="";
		for(int i = 0; i < length; i++)
			toReturn += '0';
		return toReturn;
	}

	public static String arrayStringToString(String [] array) {
		String toReturn = "[";
		for (int i = 0; i < array.length; i++) {
			toReturn+=array[i];
			toReturn+=(i==array.length-1?"]":", ");
		}
		return toReturn;
	}
	
	public static int getWordSizeInBits(){
		return wordSizeInBits;
	}
	
//	public static void main(String[] args) {
//		the bug was not taking a substring when i is neg so 32 bits are returned
//		int i = -56;
//		System.out.println(Integer.toBinaryString(i));
//	}
	
}

