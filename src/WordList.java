import java.io.*;

public class WordList {
	protected Word[] dictionary = new Word[UsefulConstants.MAXWORDS];
	protected int totalWords=0;

	public WordList(String fileName) {
		readDict(fileName);
		assert wellFormed();
	}

	/**
	 * 
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
				while ( (readChar=fis.read()) != UsefulConstants.EOF ) {
					if ( readChar == '\n' ) break;
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
	 * 
	 * @param index
	 * @return
	 */
	public Word getWord (int index) {
		return dictionary[index];
	}

	/**
	 * 
	 * @return
	 */
	public int getTotalWords() {
		return totalWords;
	}

	/**
	 * 
	 * @return
	 */
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
