import java.util.List;

/*
 * Usage: java Anagram string [[min-len] wordfile] Java Anagram program, Peter
 * van der Linden Jan 7, 1996. Feel free to pass this program around, as long
 * as this header stays intact.
 */

/**
 *	Main method for anagram program
 *
 *  Refactored by Cordelia Jones and Sneha Kanaujia
 */
public class anagram {
	public static Word[] Candidate = new Word[UsefulConstants.MAXWORDS];
	public static int totalCandidates=0;
	public static int minimumLength = 3;
	
	/**
	 * Generates anagrams for word provided as command line argument.
	 * Min word length and word source file may also be specified.
	 * @param argv [0] word to make anagrams for, [1] (optional) min word
	 * length for anagrams, [2] (optional) file to get word options from.
	 */
	public static void main(String[] argv) {
		if (argv.length < 1 || argv.length > 3) {
			System.err.println("Usage: java anagram  string-to-anagram " + "[min-len [word-file]]");
			return;
		}

		if (argv.length >= 2)
			minimumLength = Integer.parseInt(argv[1]);

		// word filename is optional 3rd argument
		WordList dictionary = new WordList( argv.length == 3 ? argv[2] : "words.txt");
		AnagramList anagrams = new AnagramList( dictionary , argv[0] , minimumLength);
		List<String> anagramList = anagrams.getAnagrams();

		System.out.println("Candiate words:");
		System.out.println(anagrams.getCandidateString());
		System.out.println("");
		System.out.println("Anagrams of " + argv[0] + ":");
		for(int i = 0; i < anagramList.size(); i++) {
			System.out.print(anagramList.get(i));
			System.out.println("");
		}
		System.out.println("----" + argv[0] + "----");
	}
}
