/**
 * A model that holds a String as well as a record of which letters it has and
 * in what quantities.
 */
public final class Word  {
	private final int letterCount[] = new int[26];  // count of each letter in the word
	private final int totalLetters;  // number of letters in the word
	private final String word;  // the word

	/**
	 * Creates a new Word with the given String.
	 * @param s
	 */
	public Word(String s) { // construct an entry from a string
		word = s;
		int total = 0;
		s = word.toLowerCase();
		
		for (int i = 'a'; i <= 'z'; i++) letterCount[i-'a'] = 0;
		
		int ch;
		for (int i = s.length()-1; i >= 0; i--) {
			ch = s.charAt(i) - 'a';
			if (ch >= 0 && ch < 26) {
				total++;
				letterCount[ch]++;
			}
		}
		
		totalLetters = total;
		assert wellFormed();
	}

	/**
	 * @param letter to check for
	 * @return True if the word contains the specified letter.
	 */
	public boolean containsLetter(int j){
		return letterCount[j] != 0;
	}

	/**
	 * Compares this word to the passed in word, checking the leastCommonLetter
	 * first.
	 * @param word the word to compare to
	 * @param leastCommonLetter the letter to check for
	 * @return 0 if the words are equal, < 0 if word is greater than this one,
	 * > 0 if word is less than this one
	 */
	public int multiFieldCompare(Word word, int leastCommonLetter)
	{
		if ( (containsLetter(leastCommonLetter) ) &&  !(word.containsLetter(leastCommonLetter)) )
			return 1;

		if ( !(containsLetter(leastCommonLetter) ) &&  (word.containsLetter(leastCommonLetter)) )
			return -1;

		if ( word.totalLetters != totalLetters )
			return (word.totalLetters - totalLetters);
		
		assert wellFormed();

		return (getWord()).compareTo(word.getWord());
	}
	
	/**
	 * @return The total number of letters (not characters) in the original word
	 */
	public int getTotalLetters() {
		return totalLetters;
	}

	/**
	 * @return the original word
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Returns an array containing one index for each letter of the alphabet.
	 * The value of each index corresponds to the instances of that letter in
	 * the original word.
	 * @return Array representing the number of each letter in the word
	 */
	public int[] getLetterCount() {
		return letterCount.clone();
	}
	
	/**
	 * Returns the number of instances of a particular letter
	 * @param index
	 * @return
	 */
	public int getLetterCount(int index) {
		return letterCount[index];
	}
	
	/**
	 * Returns a string containing the characters of the word in alphabetical
	 * order.
	 * {@inheritDoc}
	 */
	public String toString() {
		String s = "";
		for (int i = 0; i < letterCount.length; i++) {
			for (int j = 0; j < letterCount[i]; j++) {
				s = s + (char)(i + 'a');
			}
		}
		return s;
	}

	/**
	 * Internal testing method that returns true if the word satisfies the 
	 * class invariants:
	 * 1) totalLetters is equal to the total number of letters represented
	 * 		in the letterCount array
	 * 2) the number of letters represented in the letterCount array should
	 * 		not exceed the number of characters in the starting word
	 * @return returns true if class satisfies class invariants
	 */
	public boolean wellFormed() {
		//Checks if the totalLetters variable is equal to the total number of letters added up from letterCount??
		int counter = 0;
		for(int i = 0; i < letterCount.length; i++) {
			counter += letterCount[i];
		}
		if(totalLetters != counter) {
			return false;
		}
		
		if(counter > word.length()) {
			return false;
		}

		return true;
	}

}

