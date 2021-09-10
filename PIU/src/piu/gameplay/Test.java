package piu.gameplay;

import java.io.File;
import piu.utilities.PIUFileReader;

/**
 * @author AAA
 * Class for testing various components
 */
public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("/home/AAA/computer science/compsci230/A2/PIU/songs/test", "testNoteData.txt");

//		try {
//			FileReader inputfile = new FileReader(file);
//			int c = inputfile.read();
//			while (c != '\n') {
//				System.out.println((char) c);
//				c = inputfile.read();
//			}
//			inputfile.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		System.out.println(PIUFileReader.getNoteData("test"));
	}

}
