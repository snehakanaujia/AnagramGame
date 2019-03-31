import java.io.*;

/**
 * WordList takes a text file in the readDict method and stores and counts all the words
 * from that file in an array. It can return a word from the dictionary array give an index number, and the total number of
 * words in the array.
 */
public class WordList {
	//Dictionary array that holds words
	private Word[] dictionary = new Word[UsefulConstants.MAXWORDS];
	
	//Total words in the dictionary
	private int totalWords=0;

	/**
	 * WordList constructor that calls a method to read in a text file that has words to store in the dictionary array.
	 * @param f is the name of file to be read in
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
		
		//Checks that the total word counter is correct (matches length of dictionary array)
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
