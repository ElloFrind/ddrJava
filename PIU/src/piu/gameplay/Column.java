package piu.gameplay;
import java.util.ArrayList;
import java.util.List;
/**
 * A Column does all the keystroke analysis and draws the board for the column
 * This is the abstract superclass for all columns. The subclasses define the
 * drawArrow() method, which determines how the column looks
 * @author AAA
 */
abstract class Column {
	// need to add the Player class. Refers to the player whom this column is for. The results of keystrokes are sent to this player.
	private Player _player;
	
	//the arrow density drawn during a hold, 1 arrow per this many ms. This has a large effect on performance.
	static final int MAX_HOLD_ARROW_DENSITY = 20;
	
	//the maximum allowable distance from the actual arrow timing which will still give a positive result, in ms
	static final int DISCREPANCY = 150;
	
	//font size to use
	protected static final int FONT_SIZE = 100;
	
	//time to travel from bottom to top of the screen, in ms
	private static final int SCROLL_SPEED = 2000;
	
	//distance from top of the screen to the middle of the header arrows
	private static final int UPPER_BUFFER = 100;
	
	//the character which this column takes input for e.g. "g" means that this column is being inputted to when a "g" is pressed
	private char _inputChar;
	
	//keeps track of whether the input key for this column is currently pressed or not
	private boolean _inputOn = false;
	
	// holds all of the periods for the arrows, max frequency every 20ms. No overlapping periods
	private List<Period> _noteData = new ArrayList<Period>();
	
	// holds the index of the earliest arrow on the screen
	private int _startIndex = 0;
	
	// holds the index of the arrow AFTER the last arrow on the screen
	private int _endIndex = 0;
	
	// holds the x position at which to draw the arrows
	protected int _x;
	
	//holds the height of the screen
	private int _height;
	
	/**
	 * @param times a list of Periods showing the times when arrows should appear
	 * @param player the player whose column this is
	 */
	public Column(List<Period> noteData, Player player, char inputChar) {
		_noteData = noteData;
		_player = player;
		_inputChar = inputChar;
	}
	/**
	* return the width of this column
	*/
	static int width() {
		return FONT_SIZE;
	}
	/**
	* return the font size of this column
	*/
	static int fontSize() {
		return FONT_SIZE;
	}
	/**
	 * return the character for which this column takes input
	 */
	char inputChar() {
		return _inputChar;
	}
	/**
	 * Called to paint the column check the results of inputs. The arrows are updated for their new position
	 * @param painter painter used to paint the arrows
	 * @param timeElapsed the time elapsed since the song began
	 */
	void advance(GraphicsPainter painter, int timeElapsed) {
		setUpPainter(painter);
		// screenBottomTime is the number of ms from the start of the song at which arrows at the bottom of the screen will appear
		// e.g. if the time elapsed is 1000ms and the SCROLL_SPEED is 500ms then the arrows from 1500ms and earlier will have reached the screen
		int screenBottomTime = timeElapsed + SCROLL_SPEED;
		
		//update the start and end indices
		
		// if the starting time of the next arrow is < screenBottomTime, then the arrow should appear so we need to add it to the arrows being drawn
		// this means incrementing _endIndex to include the latest arrow
		if (_endIndex < _noteData.size() && _noteData.get(_endIndex).start() < screenBottomTime) {
			_endIndex++;
		}
		// if the ending time of the next arrow is less than the time elapsed + the time margin to the top of the screen,
		// then it should be off the screen by now and we don't need to draw it
		if (_startIndex < _noteData.size() &&_noteData.get(_startIndex).end() < timeElapsed + UPPER_BUFFER / _height * SCROLL_SPEED) {
			_startIndex++;
		}
		
		if (_inputOn) {
			// check if a note has been hit
			int index = _startIndex;
			while (index < _endIndex && !_noteData.get(index).hit(timeElapsed)) {
				index++;
			}
			// if a note has been hit, notify the player
			if (index < _endIndex) {
				_player.noteHit();
			}
		}
		// check if a note has been missed
		int index = _startIndex;
		while (index < _endIndex && _noteData.get(index).start() < timeElapsed - DISCREPANCY) {
			if (!_noteData.get(index).isHit(timeElapsed - DISCREPANCY)) {
				// if this note hasn't been hit yet, notify the player that it's been missed
				_player.noteMissed();
				// hit the note just in case it might get checked again (but it shouldn't)
				_noteData.get(index).hit(timeElapsed - DISCREPANCY);
				break;
			}
			index++;
		}
		// for each arrow which is currently on the screen, draw it for the entire duration which it exists for
		for (int i = _startIndex; i < _endIndex; i++) {
			Period currentPeriod = _noteData.get(i);
			// for the whole period, draw as if there was an arrow every MAX_HOLD_ARROW_DENSITYms
			for (int j = currentPeriod.start(); j < currentPeriod.end(); j += MAX_HOLD_ARROW_DENSITY) {
				drawArrow(_height * (j-timeElapsed)/SCROLL_SPEED, painter);
			}
		}
		restorePainter(painter);
	}
	
	/**
	 * Called to draw an arrow of this class. Its look must be specified by each subclass individually
	 * @param y the y position of the arrow
	 * @param painter the painter used to draw the arrow
	 */
	abstract protected void drawArrow(int y, GraphicsPainter painter);
	
	/**
	 * Set up the painter for this column.
	 * Anything defined here will be called by every column, but they also each have their own definitions as well
	 * @param painter the painter which is being setup
	 */
	protected void setUpPainter(GraphicsPainter painter) {
		painter.setFontSize(FONT_SIZE);
	}
	
	/**
	 * Restore up the painter for this column, and restore the painter to the original state.
	 * Anything defined here will be called by every column, but they also each have their own definitions as well
	 * @param painter the painter which is being restored
	 */
	protected void restorePainter(GraphicsPainter painter) {
		
	}
	
	/**
	 * Called when the size of the screen changes
	 * @param height the height of the screen
	 * @param x the x position at which the arrows for this Column should be drawn
	 */
	void updateSize(int height, int x) {
		_height = height;
		_x = x;
	}
	
	/**
	 * When this column's key is pressed, this method is called to notify the column
	 */
	public void keyPressed() {
		_inputOn = true;
	}
	
	/**
	 * When this column's key is released, this method is called to notify the column
	 */
	public void keyReleased() {
		_inputOn = false;
	}
}