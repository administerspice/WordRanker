// file: WordRanker.java
// author: Amy Jiang
// date: June 14, 2015
// - Calculates the alphabetical rank of a string
// - Enter words as command line arguments
//

import java.util.*;

public class WordRanker {
	
	public static void main(String[] args){
		// Testing
		String[] testWords = new String[]{"ABAB","AAAB","BAAA","QUESTION","BOOKKEEPER","NONINTUITIVENESS","ZYXWVUTSRQPONMLKJIHG"};
		System.out.println("Test Cases:");
		long sTime = System.nanoTime();
		for(String word : testWords) System.out.printf( "%20s : %19d\n", word, rank(word));
		long eTime = System.nanoTime();
		System.out.println( "Test Execution Time: " + (eTime - sTime)/1000000 + " ms\n" );
		
		// From input
		System.out.println("Input Results:");
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
	
	// Calculates patterns before ones starting with first letter
	public static long calculate(char[] chars){
		long previous = 0;		
		Hashtable<Character, Integer> counts = getCounts(chars);
		ArrayList<Character> priors = getPriors(chars);
		
		long dividend = factorial(chars.length-1);
		for(char c : priors){
			previous += dividend/divisor(c,counts);
		}
		return previous;
	}
	
	public static long divisor(char c, Hashtable<Character,Integer> counts){
		counts.put(c, counts.get(c)-1);
		long divisor = 1;
		Enumeration<Character> keys = counts.keys();
		while(keys.hasMoreElements()){
			char letter = keys.nextElement();
			divisor *= factorial(counts.get(letter));
		}
		counts.put(c, counts.get(c)+1);
		return divisor;
	}
	
	// Return factorial of n
	public static long factorial(int n){
		long product = 1;
		if(n == 1) return product;
		for(int i=n; i>1; i--){
			product *= i;
		}
		return product;
	}	
	
	public static ArrayList<Character> getPriors(char[] chars){
		ArrayList<Character> priors = new ArrayList<Character>();
		char firstLetter = chars[0];
		for( int i=1; i<chars.length; i++ )
			if( chars[i] < firstLetter && !priors.contains(chars[i])) 
				priors.add(chars[i]);
		return priors;
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