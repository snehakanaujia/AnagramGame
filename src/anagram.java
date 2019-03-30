import java.util.Arrays;
import java.util.List;

/*
 * Usage: java Anagram string [[min-len] wordfile] Java Anagram program, Peter
 * van der Linden Jan 7, 1996. Feel free to pass this program around, as long
 * as this header stays intact.
 */

/**
 *
 */
public class anagram {
	public static Word[] Candidate = new Word[UsefulConstants.MAXWORDS];
	public static int totalCandidates=0;
	public static int minimumLength = 3;
	static WordList Dictionary;
	
	public static void main(String[] argv) {
		if (argv.length < 1 || argv.length > 3) {
			System.err.println("Usage: java anagram  string-to-anagram " + "[min-len [word-file]]");
			return;
		}
		
		if (argv.length >= 2)
			minimumLength = Integer.parseInt(argv[1]);
		
		// word filename is optional 3rd argument
		Dictionary = new WordList( argv.length==3? argv[2] : "words.txt" );
		initAnagrams(argv[0]);
	}
	
	/**
	 * 
	 * @param anag
	 */
	private static void initAnagrams(String anag)
	{
		Word myAnagram = new Word(anag);
		
		getCandidates(myAnagram);
		
		printCandidate();
		
		int RootIndexEnd = sortCandidates(myAnagram);
		
		System.out.println("Anagrams of " + anag + ":");
		findAnagram(myAnagram, new String[UsefulConstants.MAXWORDLEN],  0, 0, RootIndexEnd);
		
		System.out.println("----" + anag + "----");
	}

	/**
	 * 
	 * @param target
	 */
	private static void getCandidates(Word target) {
		for (int i = totalCandidates = 0; i < Dictionary.getTotalWords(); i++)
			if (   (    Dictionary.getWord(i).getTotalLetters() >= minimumLength   )
				&& (    Dictionary.getWord(i).getTotalLetters() + minimumLength <= target.getTotalLetters()
					||  Dictionary.getWord(i).getTotalLetters() == target.getTotalLetters())
				&& ( fewerOfEachLetter(target, Dictionary.getWord(i)) )  )
				Candidate[totalCandidates++]=Dictionary.getWord(i);	
	}
	
	/**
	 * 
	 * @param target
	 * @param entry
	 * @return
	 */
	private static boolean fewerOfEachLetter(Word target, Word entry)
	{
		for (int i = 25; i >=0; i--)
			if (entry.getLetterCount(i) > target.getLetterCount(i)) return false;
		return true;
	}
	
	/**
	 * 
	 */
	static void printCandidate()
	{
		System.out.println("Candiate words:");
		for (int i=0; i < totalCandidates; i++)
			System.out.print( Candidate[i].getWord() + ", " + ((i%4 ==3) ? "\n" : " " ) );
		System.out.println("");
		System.out.println();
	}

	/**
	 * 
	 * @param word
	 * @param anagramArray
	 * @param level
	 * @param startAt
	 * @param endAt
	 */
	private static void findAnagram( Word word, String anagramArray[], int level, int startAt, int endAt) {
		boolean enoughCommonLetters;
		
		for (int i = startAt; i < endAt; i++) {
			enoughCommonLetters = true;
			// confirms *all* the letters in candidate are also in word
			for (int x = 25; x >= 0 && enoughCommonLetters; x--) {
				if (word.getLetterCount(x) < Candidate[i].getLetterCount(x))
					enoughCommonLetters = false;
			}
			if (enoughCommonLetters) {
				anagramArray[level] = Candidate[i].getWord();
				Word remainingLetters = getRemainingLetters(word, Candidate[i]);
				if (remainingLetters.getTotalLetters() == 0) {
					/* Found a series of words! */
					printAnagramGroup(anagramArray, level);
				} else if (remainingLetters.getTotalLetters() >= minimumLength) {
					findAnagram(remainingLetters, anagramArray, level+1,i, totalCandidates);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param anagramArray
	 * @param endIndex
	 */
	private static void printAnagramGroup(String anagramArray[], int endIndex) {
		for (int z = 0; z <= endIndex; z++)
			System.out.print(anagramArray[z] + " ");
		System.out.println();
	}
	
	/**
	 * 
	 * @param word
	 * @param candidate
	 * @return
	 */
	private static Word getRemainingLetters(Word word, Word candidate) {
		int[] wordLetterCount = word.getLetterCount();
		int[] candidateLetterCount = candidate.getLetterCount();
		String s = "";
		for (int y = 25; y >= 0; y--) {
			int r = (byte) (wordLetterCount[y] - candidateLetterCount[y]);
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
	private static int sortCandidates(Word word)
	{
		int[] MasterCount=new int[26];
		int LeastCommonIndex=0, LeastCommonCount;
		int i, j;
		
		for (j = 25; j >= 0; j--) MasterCount[j] = 0;
		for (i = totalCandidates-1; i >=0; i--)
			for (j = 25; j >=0; j--)
				MasterCount[j] += Candidate[i].getLetterCount(j);
		
		LeastCommonCount = Integer.MAX_VALUE;
		for (j = 25; j >= 0; j--)
			if (    MasterCount[j] != 0
				 && MasterCount[j] < LeastCommonCount
				 && word.containsLetter(j)  ) {
				LeastCommonCount = MasterCount[j];
				LeastCommonIndex = j;
			}
		
		quickSort(0, totalCandidates-1, LeastCommonIndex );
		
		for (i = 0; i < totalCandidates; i++)
			if (Candidate[i].containsLetter(LeastCommonIndex))
				break;
		
		return i;
	}

	/**
	 * 
	 * @param left
	 * @param right
	 * @param leastCommonLetter
	 */
	static void quickSort(int left, int right, int leastCommonIndex)
	{
		// standard quicksort from any algorithm book
		int i, last;
		if (left >= right) return;
		swap(left, (left+right)/2);
		last = left;
		for (i=left+1; i <=right; i++)  /* partition */
			if (Candidate[i].multiFieldCompare( Candidate[left], leastCommonIndex ) ==  -1 )
				swap( ++last, i);
		
		swap(last, left);
		quickSort(left, last-1, leastCommonIndex);
		quickSort(last+1,right, leastCommonIndex);
	}
	
	static void swap(int d1, int d2) {
		Word tmp = Candidate[d1];
		Candidate[d1] = Candidate[d2];
		Candidate[d2] = tmp;
	}
}