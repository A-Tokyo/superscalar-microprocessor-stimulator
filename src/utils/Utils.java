package utils;

public class Utils {
	
	private static final int wordSizeInBits = 16;

	public static int log2(int num){
		// Log base x of n is log n / log x
		return (int)(Math.log(num)/Math.log(2));
	}
	
	// Takes a number in decimal and returns the 16 bit binary representation in String form
	public static String decimalToBinary(int number) {
		String toReturn = "";
		int remainder = 0;
		while (number != 0) {
			remainder = number % 2;
			toReturn = remainder + toReturn;
			number /= 2;
		}
		while(toReturn.length() < wordSizeInBits){
			toReturn = 0 + toReturn;
		}
		return toReturn;
	}
	
}
