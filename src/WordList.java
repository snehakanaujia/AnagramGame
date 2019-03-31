import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * A list of words pulled from a textfile.
 *
 */
public class WordList {
	protected Word[] dictionary = new Word[UsefulConstants.MAXWORDS];
	protected int totalWords=0;

	public WordList(String fileName) {
		readDict(fileName);
		assert wellFormed();
	}

	/**
	 * Reads list of words from the specified file, if possible.
	 * @throws RuntimeException
	 * @param fileName
	 */
	private void readDict (String fileName) {
		FileInputStream fis;
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
				dictionary[totalWords] = new Word(s);
				totalWords++;
			}
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
	 * @param index
	 * @return Word at specified index in dictionary array
	 */
	public Word getWord (int index) {
		return dictionary[index];
	}

	/**
	 * 
	 * @return Number of words in the WordList
	 */
	public int getTotalWords() {
		return totalWords;
	}
	
	/**
	 * 
	 * @param target
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

	public boolean wellFormed() {
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
