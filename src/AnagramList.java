import java.util.LinkedList;
import java.util.List;

/**
 * A list of anagrams of a word.
 */
public class AnagramList{

	// Minimum length of a word
	private final int minimumLength;
	
	// Variables to hold the word anagrams are being generated from as well as an anagram candidate variable
	private String word, savedCandidates;
	
	// Word list with read-in file of words
	private WordList dictionary;
	// Finalized list of anagrams that can be made from all the target word's letters
	private List<String> anagrams;
	// Array of candidate anagrams, or all the words that can be made from the letters of the given targetWord
	private Word[] candidates;

	/**
	 * Generates a List of anagrams for the target word from the words
	 * available in the dictionary.
	 * @param dictionary list of words to find anagrams with
	 * @param targetWord word to find anagrams for
	 * @param minimumLength the min length for part of an anagram
	 */
	public AnagramList(WordList dictionary, String targetWord, int minimumLength) {
		word = targetWord;
		this.minimumLength = minimumLength;
		Word myAnagram = new Word(word);
		anagrams = new LinkedList<>();

		/* Generate candidates and save a string record before sorting*/
		candidates = dictionary.getPartialMatches(myAnagram, this.minimumLength);
		savedCandidates = generateCandidateString();
		/* Find anagrams */
		int RootIndexEnd = sortCandidates(myAnagram);
		findAnagrams(myAnagram, new String[UsefulConstants.MAXWORDLEN],  0, 0, RootIndexEnd);
	}

	/**
	 * Creates a String from the candidates array
	 */
	private String generateCandidateString()
	{
		StringBuilder sb = new StringBuilder();

		for (int i=0; i < candidates.length; i++)
			sb.append( candidates[i].getWord() + ", " + ((i%4 ==3) ?"\n":" " ) );

		return sb.toString();
	}
	
	/**
	 * Returns a String representing the candidates array just after it was
	 * created.
	 * @return String candidates
	 */
	public String getCandidateString() {
		return savedCandidates;
	}

	/**
	 * Recursively finds anagrams for the word with the candidates array.
	 * @param word Word to find Anagrams for
	 * @param anagramArray current combination of words that fit into Word
	 * @param level -- the current layer of recursion
	 * @param startAt the place to start in the candidates array
	 * @param endAt the place top stop in the candidates array
	 */
	private void findAnagrams( Word word, String anagramArray[], int level, int startAt, int endAt) {
		boolean enoughCommonLetters;

		for (int i = startAt; i < endAt; i++) {
			enoughCommonLetters = true;
			// confirms *all* the letters in candidates are also in word
			for (int x = 25; x >= 0 && enoughCommonLetters; x--) {
				if (word.getLetterCount(x) < candidates[i].getLetterCount(x))
					enoughCommonLetters = false;
			}
			if (enoughCommonLetters) {
				anagramArray[level] = candidates[i].getWord();
				Word remainingLetters = getRemainingLetters(word, candidates[i]);
				if (remainingLetters.getTotalLetters() == 0) {
					/* Found a series of words! */
					addAnagrams(anagramArray, level);
				} else if (remainingLetters.getTotalLetters() >= minimumLength) {
					findAnagrams(remainingLetters, anagramArray, level+1,i, candidates.length);
				}
			}
		}
	}

	/**
	 * Combine array of strings into a single string and save it in the final 
	 * list of anagrams
	 * @param anagramArray
	 * @param endIndex
	 */
	private void addAnagrams(String anagramArray[], int endIndex) {
		String anagram = "";
		for (int z = 0; z <= endIndex; z++)
			anagram += anagramArray[z] + " ";
		anagrams.add(anagram);
	}

	/**
	 * Gets the remaining letters after the letters in candidate are removed
	 * from word. Expects all letters in candidate to exist in word -- For each
	 * letter in candidate, there exists at least as many instance of that
	 * letter in word.
	 * @param word the Word to have letters removed from it
	 * @param candidate the Word to be removed
	 * @return A new Word containing the result of word - candidate
	 */
	private Word getRemainingLetters(Word word, Word candidate) {
		int[] wordLetterCount = word.getLetterCount();
		int[] candidateLetterCount = candidate.getLetterCount();
		String s = "";
		for (int y = 25; y >= 0; y--) {
			int r = wordLetterCount[y] - candidateLetterCount[y];
			for(int i = 0; i < r; i++) {
				s += (char) (y + 'a');
			}
		}

		Word remaining = new Word(s);
		return remaining;
	}

	/**
	 * Sorts the candidate Words by the least common letter shared with the
	 * target Word. 
	 * @param word
	 * @return
	 */
	private int sortCandidates(Word word)
	{
		// Keeps track of the frequency of each letter is in each word
		int[] masterCount=new int[26];
		// Keeps track of which index/letter in the array has the fewest letters
		int leastcommonIndex=0, leastCommonCount = 30;
		
		// Goes through each candidate word
		for (int i = candidates.length-1; i >=0; i--)
			// Goes through each letter of the cadidate word
			for (int j = 25; j >=0; j--) {
				// Adds the candidate list letter counts to the masterCount
				masterCount[j] += candidates[i].getLetterCount(j);
				/* Checks if the letter of the word is in masterCount and is less than leastCommonCount and the
				 * candidate word also has this letter */
				if ( masterCount[j] != 0 && masterCount[j] < leastCommonCount && word.containsLetter(j)  ) {
					// updates the leastCommonCount with the lower masterCount value
					leastCommonCount = masterCount[j];
					/* Updates the leastCommonIndex to the index in leastCommonCount that has the least 
					commonly used letter */
					leastcommonIndex = j;
				}
			}
		// Sorts the candidates
		quickSort(0, candidates.length-1, leastcommonIndex );

		return leastcommonIndex;
	}

	/**
	 * Standard quicksort from any algorithm book
	 * @param left
	 * @param right
	 * @param leastCommonLetter
	 */
	private void quickSort(int left, int right, int leastCommonLetter)
	{
		int i, last;
		if (left >= right) return;
		swap(left, (left+right)/2);
		last = left;
		for (i=left+1; i <=right; i++)  /* partition */
			if (candidates[i].multiFieldCompare ( candidates[left], leastCommonLetter ) ==  -1 )
				swap( ++last, i);

		swap(last, left);
		quickSort(left, last-1, leastCommonLetter);
		quickSort(last+1,right, leastCommonLetter);
	}

	/**
	 * Switches the contents of two indexes of the candidates array.
	 * @param d1 index 1 to swap
	 * @param d2 index 2 to swap
	 */
	private void swap(int d1, int d2) {
		Word tmp = candidates[d1];
		candidates[d1] = candidates[d2];
		candidates[d2] = tmp;
	}

	/**
	 * @return A List of anagrams of the word
	 */
	public List<String> getAnagrams() {
		return anagrams;
	}
}
