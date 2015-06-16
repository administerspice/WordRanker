import java.util.*;

/** WordRanker.java
 * Description:
 * Returns alphabetical rank of a string ordered on all possible unique arrangements of its 
 * letters. Enter words as command line arguments.
 * 
 * This is a recursive algorithm that calculates the number of patterns that precede every 
 * string based on the number of letters that precede the first letter. Then it removes the 
 * first letter of that string, and adds to the total by applying the same method to the 
 * consequent substring. In the end, we add one to indicate the rank of the string in question.
 * 
 * Example usage with input "BADC":
 * 1) BADC: 'A'<'B' --> 3!=6 strings beginning with 'A' precede 'BADC'	 6
 * 2) -ADC: no letter precedes 'A'										+0
 * 3) --DC: 'C'<'D' --> 1 string beginning with 'C' precede 'DC'		+1
 * 4) ---C: Here we add 1 for the rank of 'BADC'.						+1
 * 8) Rank = 8
 * 
 * @author Amy Jiang
 * @date June 14, 2015
 */
public class WordRanker {
	
	public static void main(String[] args){
		// Self-testing
//		String[] testWords = new String[]{"ABAB","AAAB","BAAA","QUESTION","BOOKKEEPER","NONINTUITIVENESS","ZYXWVUTSRQPONMLKJIHG"};
//		run(testWords);

		run(args);
	}
	
	/**
	 * Process input and get ranks.
	 * 
	 * @param input
	 */
	private static void run(String[] input){
		if(input.length == 0){
			System.out.println("You didn't enter anything...");
			return;
		}
		long startTime = System.nanoTime();
		for(String word : input){
			if(word.length()>20){
				System.out.printf("%s : Too long to calculate!\n", word);
				continue;
			}
			System.out.printf( "%20s : %19d\n", word, rank(word));
		}
		long endTime = System.nanoTime();
		System.out.println( "Input Execution Time: " + (endTime - startTime)/1000000 + " ms" );
	}
	
	/**
	 * Recursively finds rank of word. 
	 * 
	 * @param word
	 * @return
	 */
	public static long rank(String word){
		char[] chars = word.toCharArray();
		if(word.length() == 0) return 0;
		if(word.length() == 1) return 1;
		return getPermutation(chars) + rank(word.substring(1,word.length()));
	}
	
	/**
	 * Calculates patterns that occur before first letter
	 * Formula: n!/(m_a!*m_b!..m_z!)
	 * 		n is the number of characters
	 * 		m_z is the number of times char z occurs
	 * 
	 * @param word
	 * @return
	 */
	public static long getPermutation(char[] word){
		long numPreviousCombos = 0;		
		long dividend = factorial(word.length-1);
		Hashtable<Character, Integer> counts = getCounts(word);

		for(char c : counts.keySet())
			if(c < word[0])
				numPreviousCombos += dividend/divisor(c, counts);
		
		return numPreviousCombos;
	}
	
	/**
	 * The divisor in the formula.
	 * (m_a!*m_b!..m_z)
	 * Represents duplicate patterns to be removed from total permutations.
	 * 
	 * @param c
	 * @param counts
	 * @return
	 */
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
	
	/**
	 * Return factorial of n
	 * 
	 * @param n
	 * @return
	 */
	public static long factorial(int n){
		long product = 1;
		if(n == 1) return product;
		for(int i=n; i>1; i--)
			product *= i;
		return product;
	}	
	
	/**
	 * Return Hashtable of letters and their counts
	 *  
	 * @param chars
	 * @return
	 */
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