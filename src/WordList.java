import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * WordList takes a text file in the readDict method and stores and counts all the words
 * from that file in an array. It can return a word from the dictionary array give an index number, and the total number of
 * words in the array.
 * 
 * Refactored by Cordelia Jones and Sneha Kanaujia
 */
public class WordList {
	// Dictionary array that holds words
	private Word[] dictionary = new Word[UsefulConstants.MAXWORDS];
	
	// Total words in the dictionary
	private int totalWords=0;

	/**
	 * WordList constructor that calls a method to read in a text file that has words to store in the dictionary array.
	 * @param fileName is the name of file to be read in
	 */
	public WordList(String fileName) {
		readDict(fileName);
		assert wellFormed();
	}

	/**
	 * Reads in a text file that has words to store in the dictionary array. The file is not
	 * meant to have more than 10000 words in it or an IndexOutOfBounds error will likely be thrown.
	 * @param f is the name of file to be read in
	 */
	private void readDict (String fileName) {
		FileInputStream fis;
		
		// This try block ensures the file can be found and correctly read in.
		try {
			fis = new FileInputStream (fileName);
			System.err.println ("reading dictionary...");

			char buffer[] = new char[UsefulConstants.MAXWORDLEN];
			String s;
			int readChar = 0;
			while (readChar!= UsefulConstants.EOF) {
				int i = 0;
				// read a word in from the word file
				while ( (readChar=fis.read()) != UsefulConstants.EOF) {
					if ( readChar == '\n' )
						break;
					buffer[i++] = (char) readChar;
				}
				s = new String(buffer,0,i);
				// Stores word into dictionary array
				dictionary[totalWords] = new Word(s);
				totalWords++;
			}
			// Outputs how many words were read into the dictionary
			System.err.println("main dictionary has " + totalWords + " entries.");
			fis.close();
		}
		catch (FileNotFoundException fnfe) {
			System.err.println("Cannot open the file of words '" + fileName + "'");
			throw new RuntimeException();
		} catch (IOException ioe) {
			System.err.println("Cannot read the file of words ");
			throw new RuntimeException();
		}
	}

	/**
	 * Gets the words in the dictionary.
	 * @param index in the dictionary arrray where a word is located 
	 * @return the word at the given index in the dictionary array
	 */
	public Word getWord (int index) {
		return dictionary[index];
	}

	/**
	 * Gets the total words in the dictionary.
	 * @return totalWords count
	 */
	public int getTotalWords() {
		return totalWords;
	}
	
	/**
	 * Searches through the dictionary array of words and finds words that could be anagrams of the target/original word
	 * @param target word that anagrams are being looked for
	 * @param minimumLength that the candidate anagrams can be
	 * @return array of anagram candidates
	 */
	public Word[] getPartialMatches(Word target, int minimumLength) {
		List<Word> candidates = new LinkedList<>();
		for (int i = 0; i < totalWords; i++)
			if (   (    dictionary[i].getTotalLetters() >= minimumLength   )
				&& (    dictionary[i].getTotalLetters() + minimumLength <= target.getTotalLetters()
					||  dictionary[i].getTotalLetters() == target.getTotalLetters())
				&& ( fewerOfEachLetter(target, dictionary[i]) )  )
				candidates.add(dictionary[i]);
		assert wellFormed();
		return candidates.toArray(new Word[candidates.size()]);
	}

	/**
	 * Ensures a potential candidate for the target word has the same or fewer number of letters as the original word
	 * @param target word that anagrams are being looked for
	 * @param entry is the potential candidate word
	 * @return true if the entry word has the same or fewer number of letters as the original/target word
	 */
	private boolean fewerOfEachLetter(Word target, Word entry)
	{
		for (int i = 25; i >=0; i--)
			if (entry.getLetterCount(i) > target.getLetterCount(i)) return false;
		return true;
	}

	/**
	 * The dictionary must have:
	 * 	1) An instantiated dictionary
	 * 	2) totalWords counter should be the length of the dictionary for it to be correct
	 * @return true if the dictionary is wellFormed, if not, return false
	 */
	public boolean wellFormed() {
		//Checks that dictionary is not null
		if(dictionary == null) {
			return false;
		}
		
		//Checks that the total word counter is correct as long as it matches the length of the dictionary array
		int counter = 0;
		for(int i = 0; i < dictionary.length; i++) {
			if(dictionary[i] != null) {
				counter++;
			}
		}
		if(totalWords != counter) {
			return false;
		}
		return true;
	}
}
