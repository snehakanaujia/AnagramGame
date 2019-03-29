import java.util.LinkedList;
import java.util.List;

public class AnagramList extends WordList {
	
	List<String> anagrams = new LinkedList<>();
	int minimumLength = 3;
	String word;

	public AnagramList(String fileName, String targetWord) {
		super(fileName);
		word = targetWord;
		initAnagrams();
	}
	
	/**
	 * 
	 * @param anag
	 */
	private void initAnagrams()
	{
		Word myAnagram = new Word(word);
		
		getCandidates(myAnagram);
		
		int RootIndexEnd = sortdictionarys(myAnagram);
		
		findAnagram(myAnagram, new String[UsefulConstants.MAXWORDLEN],  0, 0, RootIndexEnd);
	}

	/**
	 * 
	 * @param target
	 */
	private void getCandidates(Word target) {
		int totalCandidates;
		Word[] candidates = new Word[UsefulConstants.MAXWORDS];
		for (int i = totalCandidates = 0; i < totalWords; i++)
			if (   (    dictionary[i].getTotalLetters() >= minimumLength   )
				&& (    dictionary[i].getTotalLetters() + minimumLength <= target.getTotalLetters()
					||  dictionary[i].getTotalLetters() == target.getTotalLetters())
				&& ( fewerOfEachLetter(target, dictionary[i]) )  )
				candidates[totalCandidates++]=dictionary[i];	
		dictionary = candidates;
		totalWords = totalCandidates;
		assert wellFormed();
	}
	
	/**
	 * 
	 * @param target
	 * @param entry
	 * @return
	 */
	private boolean fewerOfEachLetter(Word target, Word entry)
	{
		for (int i = 25; i >=0; i--)
			if (entry.getLetterCount(i) > target.getLetterCount(i)) return false;
		return true;
	}
	
	/**
	 * 
	 */
	public String printCandidates()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Candiate words:");
		for (int i=0; i < totalWords; i++)
			sb.append( dictionary[i].getWord() + ", " + ((i%4 ==3) ?"\n":" " ) );
		
		return sb.toString();
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
			// confirms *all* the letters in dictionary are also in word
			for (int x = 25; x >= 0 && enoughCommonLetters; x--) {
				if (word.getLetterCount(x) < dictionary[i].getLetterCount(x))
					enoughCommonLetters = false;
			}
			if (enoughCommonLetters) {
				anagramArray[level] = dictionary[i].getWord();
				Word remainingLetters = getRemainingLetters(word, dictionary[i]);
				if (remainingLetters.getTotalLetters() == 0) {
					/* Found a series of words! */
					addAnagrams(anagramArray, level);
				} else if (remainingLetters.getTotalLetters() >= minimumLength) {
					findAnagram(remainingLetters, anagramArray, level+1,i, totalWords);
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
	 * 
	 * @param word
	 * @param dictionary
	 * @return
	 */
	private Word getRemainingLetters(Word word, Word dictionary) {
		int[] wordLetterCount = word.getLetterCount();
		int[] dictionaryLetterCount = dictionary.getLetterCount();
		String s = "";
		for (int y = 25; y >= 0; y--) {
			int r = wordLetterCount[y] - dictionaryLetterCount[y];
			for(int i = 0; i < r; i++) {
				s += (char) (y + 'a');
			}
		}
		
		Word remaining = new Word(s);
		return remaining;
	}

	/**
	 * 
	 * @param word
	 * @return
	 */
	private int sortdictionarys(Word word)
	{
		int[] MasterCount=new int[26];
		int LeastCommonIndex=0, LeastCommonCount;
		int i, j;
		
		for (j = 25; j >= 0; j--) MasterCount[j] = 0;
		for (i = totalWords-1; i >=0; i--)
			for (j = 25; j >=0; j--)
				MasterCount[j] += dictionary[i].getLetterCount(j);
		
		LeastCommonCount = Integer.MAX_VALUE;
		for (j = 25; j >= 0; j--)
			if (    MasterCount[j] != 0
				 && MasterCount[j] < LeastCommonCount
				 && word.containsLetter(j)  ) {
				LeastCommonCount = MasterCount[j];
				LeastCommonIndex = j;
			}
		
		quickSort(0, totalWords-1, LeastCommonIndex );
		
		for (i = 0; i < totalWords; i++)
			if (dictionary[i].containsLetter(LeastCommonIndex))
				break;
		
		return i;
	}

	/**
	 * 
	 * @param left
	 * @param right
	 * @param leastCommonLetter
	 */
	private void quickSort(int left, int right, int leastCommonLetter)
	{
		// standard quicksort from any algorithm book
		int i, last;
		if (left >= right) return;
		swap(left, (left+right)/2);
		last = left;
		for (i=left+1; i <=right; i++)  /* partition */
			if (dictionary[i].MultiFieldCompare ( dictionary[left], leastCommonLetter ) ==  -1 )
				swap( ++last, i);
		
		swap(last, left);
		quickSort(left, last-1, leastCommonLetter);
		quickSort(last+1,right, leastCommonLetter);
	}
	
	/**
	 * 
	 * @param d1
	 * @param d2
	 */
	private void swap(int d1, int d2) {
		Word tmp = dictionary[d1];
		dictionary[d1] = dictionary[d2];
		dictionary[d2] = tmp;
	}

	/**
	 * 
	 * @return
	 */
	public List getAnagrams() {
		return anagrams;
	}
}
