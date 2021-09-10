/**
 * 
 */
package piu.gameplay;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * A Player acts as the interface between the GameView and the Columns.
 * This does a little processing, and will later handle any settings/individualisation
 * @author AAA
 */
class Player {
	// a list of all the columns this player has.
	private List<Column> _columns = new ArrayList<Column>();
	
	// the horizontal distance between arrows in columns
	private int _columnPadding = 20;
	
	/**
	 * Create a new Player. This involves making all the necessary columns with their note data,
	 * and then putting them on the _columns list. This will need to be changed to change the number of columns
	 * @param noteData a list of note data lists, one for each column
	 */
	public Player(List<ArrayList<Period>> noteData, String inputChars) {
		_columns.add(new Downleft(noteData.get(0), this, inputChars.charAt(0)));
		_columns.add(new Upleft(noteData.get(1), this, inputChars.charAt(1)));
		_columns.add(new Center(noteData.get(2), this, inputChars.charAt(2)));
		_columns.add(new Upright(noteData.get(3), this, inputChars.charAt(3)));
		_columns.add(new Downright(noteData.get(4), this, inputChars.charAt(4)));
	}
	
	int numCols() {
		return _columns.size();
	}
	/**
	 * Calls on all the columns to move their arrows forward
	 * 
	 * @param painter painter which will be used to paint the arrows
	 * @param timeElapsed the time elapsed since the start of the song
	 */
	void advance(GraphicsPainter painter, int timeElapsed) {
		for (Column c : _columns) {
			c.advance(painter, timeElapsed);
		}
	}
	/**
	 * Updates the positions of all the columns
	 * 
	 * @param width the width of this player
	 * @param height the height of the screen
	 * @param x the x position of the left of this player
	 */
	void updateSize(int width, int height, int x) {
		//x -= _columnPadding*(_columns.size()-1)/2 + Column.width()*_columns.size()/2;
		for (int i = 0; i < _columns.size(); i++) {
			_columns.get(i).updateSize(height, x);
			x += Column.width() + _columnPadding;
		}
	}
	/**
	 * When a key is pressed, the appropriate player is notified using the keyPressed method.
	 * The player then notifies the appropriate column that its column has been pressed
	 * @param e the KeyEvent which was generated by the original KeyPressed notification to GameView
	 */
	void keyPressed(KeyEvent e) {
		for (Column col : _columns) {
			if (col.inputChar() == e.getKeyChar()) {
				col.keyPressed();
				break;
			}
		}
	}
	/**
	 * When a key is released, the appropriate player is notified using the keyPressed method.
	 * The player then notifies the appropriate column that its column has been released
	 * @param e the KeyEvent which was generated by the original KeyPressed notification to GameView
	 */
	void keyReleased(KeyEvent e) {
		for (Column col : _columns) {
			if (col.inputChar() == e.getKeyChar()) {
				col.keyReleased();
				break;
			}
		}
	}
	
	/**
	 * When a column records a successful hit, the player is notified
	 */
	void noteHit() {
		//TODO update the combo etc.
		System.out.println("hit!");
	}
	
	/**
	 * When a column records a successful hit, the player is notified
	 */
	void noteMissed() {
		//TODO update the combo etc.
		System.out.println("miss!");
	}


}
