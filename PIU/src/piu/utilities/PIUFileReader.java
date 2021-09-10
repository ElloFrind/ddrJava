package piu.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import piu.gameplay.Period;

/**
 * This class holds all the methods related to reading song file data
 * This includes the note data, mp3, background picture etc.
 * All methods and fields are static; this will never be instantiated
 * @author AAA
 */
public class PIUFileReader {
	/**
	 * The constructor is private and does nothing. It will never be called,
	 * to ensure that this class is never instantiated
	 */
	private PIUFileReader() {}
	
	/**
	 * digitsToNumber takes a list of digits and returns the number made up of those digits in that order
	 * For instance, [1, 2, 3] would return 123.
	 * @param digits a List of the digits of the number
	 * @return the number made up of those digits
	 */
	private static int digitsToNumber(List<Integer> digits) {
		int output = 0;
		for (int i = 0; i < digits.size(); i++) {
			output += digits.get(i) * Math.pow(10, digits.size() - i - 1);
		}
		return output;
	}
	
	/**
	 * getNoteData takes a song name as a string and reads its NoteData file, returning the information for use by the Player class
	 * The files are of format:
	 * period1Start period1End
	 * period2Start period2End
	 * *
	 * where the '*' represents the end of a column's data
	 * This method may take a while to complete, especially on longer songs, so it is recommended not to use this on the event dispatch thread 
	 * @param song the name of the song to get the data for
	 * @return output a List<ArrayList<Period>> which contains the note data for each column
	 */
	public static List<ArrayList<Period>> getNoteData(String song) {
		List<ArrayList<Period>> noteData = null;
		// create a reference to the file which contains this song's note data 
		File file = new File("/home/aaa/Programs/computer science/compsci230/A2/PIU/songs/" + song, song + "NoteData.txt");
		try {
			// set up the output and the FileReader
			noteData = new ArrayList<ArrayList<Period>>();
			FileReader inputFile = new FileReader(file);
			// inputFile.read() returns the character's ASCII value
			// the first digit is read in
			int c = inputFile.read();
			
			while (c != -1) {
				ArrayList<Period> columnNotes = new ArrayList<Period>();
				// this part gets the Periods for every column
				while ((char) c != '*') {
					int start;
					int end;
					// calculate the first number of this period
					ArrayList<Integer> digits = new ArrayList<Integer>();

					// get all the digits of the first number. This ends with a ' '
					// the first digit has already been read into c
					while ((char) c != ' ') {
						digits.add(c-48);
						c = inputFile.read();
					}
					// convert the list of digits to a number and store it in "start"
					start = digitsToNumber(digits);
					// reset the digits ArrayList
					digits.clear();
					
					c = inputFile.read();
					// get all the digits of the second number. This ends with a '\n'
					while ((char) c != '\n') {
						digits.add(c-48);
						c = inputFile.read();
					}
					// convert the list of digits to a number and store it in "end"
					end = digitsToNumber(digits);
					
					// add this period to this column's notes
					columnNotes.add(new Period(start, end));
					// read in the first digit of the next line
					// if it's a '*' it'll break out of this loop
					c = inputFile.read();
				}
				
				noteData.add(columnNotes);
				// throw away the '\n' after the '*'
				inputFile.read();
				// get the first digit of the next number.
				// if this fails, the end of file is reached, c = -1, this loop is broken and the function returns
				c = inputFile.read();

			}
			inputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return noteData;
	}
}
