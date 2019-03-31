import java.util.LinkedList;
import java.util.List;

/**
 * A list of anagrams of a word.
 */
public class AnagramList{

	private int minimumLength = 3;
	private String word, savedCandidates;
	private WordList dictionary;
	private List<String> anagrams;
	private Word[] candidates;

	public AnagramList(WordList dictionary, String targetWord) {
		word = targetWord;
		Word myAnagram = new Word(word);
		anagrams = new LinkedList<>();

		/* Generate candidates and save a string record before sorting*/
		candidates = dictionary.getPartialMatches(myAnagram, minimumLength);
		savedCandidates = generateCandidateString();
		/* Find anagrams */
		int RootIndexEnd = sortCandidates(myAnagram);
		findAnagram(myAnagram, new String[UsefulConstants.MAXWORDLEN],  0, 0, RootIndexEnd);
	}

	/**
	 * 
	 */
	private String generateCandidateString()
	{
		StringBuilder sb = new StringBuilder();

		for (int i=0; i < candidates.length; i++)
			sb.append( candidates[i].getWord() + ", " + ((i%4 ==3) ?"\n":" " ) );

		return sb.toString();
	}
	
	public String getCandidateString() {
		return savedCandidates;
	}

	/**
	 * 
	 * @param word
	 * @param anagramArray
	 * @param level
	 * @param startAt
	 * @param endAt
	 */
	private void findAnagram( Word word, String anagramArray[], int level, int startAt, int endAt) {
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
					findAnagram(remainingLetters, anagramArray, level+1,i, candidates.length);
				}
			}
		}
	}

	/**
	 * 
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
		int[] MasterCount=new int[26];
		int LeastCommonIndex=0, LeastCommonCount;
		int i, j;

		for (j = 25; j >= 0; j--) MasterCount[j] = 0;
		for (i = candidates.length-1; i >=0; i--)
			for (j = 25; j >=0; j--)
				MasterCount[j] += candidates[i].getLetterCount(j);

		LeastCommonCount = Integer.MAX_VALUE;
		for (j = 25; j >= 0; j--)
			if (    MasterCount[j] != 0
				 && MasterCount[j] < LeastCommonCount
				 && word.containsLetter(j)  ) {
				LeastCommonCount = MasterCount[j];
				LeastCommonIndex = j;
			}

		quickSort(0, candidates.length-1, LeastCommonIndex );

		for (i = 0; i < candidates.length; i++)
			if (candidates[i].containsLetter(LeastCommonIndex))
				break;

		return i;
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