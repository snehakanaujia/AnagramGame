
public final class Word  {
	private final int letterCount[] = new int[26];  // count of each letter in the word
	private final int totalLetters;  // number of letters in the word
	private final String word;  // the word

	public Word(String s) { // construct an entry from a string
		word = s;//s.replaceAll("\\r|\\n", "");
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

	public boolean containsLetter(int j){
		return letterCount[j] != 0;
	}

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

	public String toString() {
		String s = "";
		for (int i = 0; i < letterCount.length; i++) {
			for (int j = 0; j < letterCount[i]; j++) {
				s = s + (char)(i + 'a');
			}
		}
		return s;
	}
	
	public int getTotalLetters() {
		return totalLetters;
	}

	public String getWord() {
		return word;
	}
	
	public int[] getLetterCount() {
		return letterCount;
	}
	
	public int getLetterCount(int index) {
		return letterCount[index];
	}

	/**
	 * Internal testing method that returns true if the word satisfies the class invariants:
	 * 	1) 
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
		
//		if(word.indexOf('\n') != -1 || word.indexOf('\r') != -1)
//			return false;
		
		return true;
	}

}

