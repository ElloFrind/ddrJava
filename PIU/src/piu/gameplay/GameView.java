package piu.gameplay;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import piu.utilities.PIUFileReader;

/**
 * Simple GUI program to show an animation of shapes. Class GameView is
 * a special kind of GUI component (JPanel), and as such an instance of
 * GameView can be added to a JFrame object. A JFrame object is a
 * window that can be closed, minimised, and maximised. The state of an
 * GameView object comprises a list of Shapes and a Timer object. An
 * GameView instance subscribes to events that are published by a Timer.
 * In response to receiving an event from the Timer, the GameView iterates
 * through a list of Shapes requesting that each Shape paints and moves itself.
 *
 * @author Ian Warren and AAA
 *
 */
@SuppressWarnings("serial")
public class GameView extends JPanel implements ActionListener, ComponentListener, KeyListener {
	// Frequency in milliseconds for the Timer to generate events. This has a large effect on performance.
	private static final int DELAY = 20;
	
	// the list of possible input characters which could be used by the players' columns, up to 71 total columns supported
	private static final String INPUT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890`-=[];',.";

	// list of players
	private List<Player> _players = new ArrayList<Player>();
	
	//the number of columns each player has
	private int _numCols; 
	
	private Timer _timer = new Timer(DELAY, this);
	
	// The amount of time elapsed since the timer was started
	private int _timeElapsed = 0;
	/**
	 * Creates an GameView instance with a list of Player objects and
	 * starts the animation.
	 * @param numPlayers the number of players
	 * @param song the name of the song
	 */
	public GameView(int numPlayers, String song) {
		List<ArrayList<Period>> noteData = PIUFileReader.getNoteData(song);
		// get the number of columns each player has
		_numCols = noteData.get(0).size();
		for (int i = 0; i < numPlayers; i++) {
			// give each player the same noteData and its set of input characters
			_players.add(new Player(noteData, INPUT_CHARACTERS.substring(i, i+_numCols)));
		}
		componentResized(null);
		// Start the animation.
		_timer.start();
	}

	/**
	 * Called by the Swing framework whenever this GameView object
	 * should be repainted. This can happen, for example, after an explicit
	 * repaint() call or after the window that contains this GameView
	 * object has been opened, exposed or moved.
	 *
	 */
	@Override
	public void paintComponent(Graphics g) {
		// Call inherited implementation to handle background painting.
		super.paintComponent(g);

		// Create a GraphicsPainter that Shape objects will use for drawing.
		// The GraphicsPainter delegates painting to a basic Graphics object.
		GraphicsPainter painter = new GraphicsPainter(g);

		// Progress the animation.
		for(Player s : _players) {
			s.advance(painter, _timeElapsed);
		}
		_timeElapsed += DELAY;
	}

	/**
	 * Notifies this GameView object of an ActionEvent. ActionEvents are
	 * received by the Timer.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Request that the GameView repaints itself. The call to
		// repaint() will cause the GameView's paintComponent() method
		// to be called.
		repaint();
	}

	/**
	 * Main program method to create an GameView object and display this
	 * within a JFrame window. Used for testing purposes; later this view will be created from View.java, which commands the view
	 * The song will be specified by the main view
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Animation viewer");
				String song = "test";
				GameView game = new GameView(1, song);
				frame.add(game);
				frame.addComponentListener(game);
				frame.addKeyListener(game);
				// Set window properties.
				frame.setSize(1500, 1000);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	/**
	 * ComponentListener methods
	 */
	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	/**
	 * Get all the players to update their width and height and reposition themselves
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		//the width of a player
		int width = getWidth()/_players.size();
		
		for (int i = 0; i < _players.size(); i++) {
			//passed in are: width of the player, height of the screen, and the x value of the middle of this player
			_players.get(i).updateSize(width, getHeight(),width*i);
		}
	}
	
	@Override
	public void componentShown(ComponentEvent e) {}

	/**
	 * KeyListener methods
	 */
	
	/**
	 * When a key is pressed, keyPressed will send out a signal to the correct player
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		//INPUT_CHARACTERS.indexOf(e.getKeyChar()) gets the index of the character in the INPUT_CHARACTERS String
		//_players.get(0).numCols() gets the number of columns of which the players are using (all the players use the same number of columns)
		//character index / number of columns per player = the index of the player
		//calling Player.keyPressed(e) means that that player receives the input
		int characterIndex = INPUT_CHARACTERS.indexOf(e.getKeyChar());
		if (characterIndex < _numCols*_players.size()) {
			//then this character is a valid input character
			_players.get(characterIndex/_numCols).keyPressed(e);
		}
	}
	/**
	 * When a key is released, keyReleased will send out a signal to the correct player
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int characterIndex = INPUT_CHARACTERS.indexOf(e.getKeyChar());
		if (characterIndex < _numCols*_players.size()) {
			//then this character is a valid input character
		_players.get(INPUT_CHARACTERS.indexOf(e.getKeyChar())/_numCols).keyReleased(e);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
