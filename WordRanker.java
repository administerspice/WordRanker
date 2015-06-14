// file: WordRanker.java
// author: Amy Jiang
// date: June 14, 2015
// - Returns rank number of a string out of all possible unique arrangements of its letters.
// - Enter words as command line arguments.

import java.util.*;

public class WordRanker {
	
	public static void main(String[] args){
		// Testing
		String[] testWords = new String[]{"ABAB","AAAB","BAAA","QUESTION","BOOKKEEPER","NONINTUITIVENESS","ZYXWVUTSRQPONMLKJIHG"};
		System.out.println("Examples:");
		long sTime = System.nanoTime();
		for(String word : testWords) 
			System.out.printf( "%20s : %19d\n", word, rank(word));
		long eTime = System.nanoTime();
		System.out.println( "Test Execution Time: " + (eTime - sTime)/1000000 + " ms\n" );
		
		// From input
		System.out.println("Your Input Results:");
		if(args.length != 0){
			long startTime = System.nanoTime();
			for(String word : args){
				if(word.length()>20){
					System.out.printf("%s : Too long to calculate!\n", word);
					continue;
				}
				System.out.printf( "%20s : %19d\n", word, rank(word));
			}
			long endTime = System.nanoTime();
			System.out.println( "Input Execution Time: " + (endTime - startTime)/1000000 + " ms" );
		}
	}
	
	// Recursively finds rank
	public static long rank(String word){
		char[] chars = word.toCharArray();
		if(word.length() == 0) return 0;
		if(word.length() == 1) return 1;
		return calculate(chars) + rank(word.substring(1,word.length()));
	}
	
	// Calculates patterns that occur before first letter
	public static long calculate(char[] word){
		long previous = 0;		
		long dividend = factorial(word.length-1);
		Hashtable<Character, Integer> counts = getCounts(word);

		for(char c : counts.keySet())
			if(c < word[0])
				previous += dividend/divisor(c, counts);
		
		return previous;
	}
	
	public static long divisor(char c, Hashtable<Character,Integer> counts){
		long divisor = 1;
		Enumeration<Character> keys = counts.keys();
		
		counts.put(c, counts.get(c)-1); 	// Remove an instance of current letter for calculation
		while(keys.hasMoreElements()){
			char letter = keys.nextElement();
			divisor *= factorial(counts.get(letter));
		}
		counts.put(c, counts.get(c)+1);		// Replace letter
		
		return divisor;
	}
	
	// Return factorial of n
	public static long factorial(int n){
		long product = 1;
		if(n == 1) return product;
		for(int i=n; i>1; i--)
			product *= i;
		return product;
	}	
	
	// Return table of letters and their counts based on char array
	public static Hashtable<Character, Integer> getCounts(char[] chars){
		Hashtable<Character, Integer> counts = new Hashtable<Character, Integer>();
		for(char c : chars){
			if(counts.containsKey(c))
				counts.put(c, (int)counts.get(c)+1);
			else 
				counts.put(c, 1);
		}
		return counts;
	}
}