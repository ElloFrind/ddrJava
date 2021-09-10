package piu.gameplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Image;

/**
 * Implementation of the Painter interface that delegates drawing to a
 * java.awt.Graphics object.
 * 
 * @author Ian Warren & Avery O'Callahan
 * 
 */
public class GraphicsPainter {
	private Color DEFAULT_COLOR = Color.BLACK;
	private Font DEFAULT_FONT =  new Font("TimessRoman", Font.PLAIN, Column.fontSize());
	// Delegate object.
	private Graphics2D _g;

	/**
	 * Creates a GraphicsPainter object and sets its Graphics delegate.
	 */
	public GraphicsPainter(Graphics g) {
		this._g = (Graphics2D) g;
		_g.setColor(DEFAULT_COLOR);
		_g.setFont(DEFAULT_FONT);
	}

	/**
	 * @see Painter.drawRect
	 */
	public void drawRect(int x, int y, int width, int height) {
		_g.drawRect(x, y, width, height);
	}

	/**
	 * @see Painter.drawOval
	 */
	public void drawOval(int x, int y, int width, int height) {
		_g.drawOval(x, y, width, height);
	}

	/**
	 * @see Painter.drawLine
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		_g.drawLine(x1, y1, x2, y2);
	}

	/**
	 * @see Painter.fillRect
	 */
	public void fillRect(int x, int y, int width, int height) {
		_g.fillRect(x, y, width, height);
	}
	
	/**
	 * @see Painter.getColor
	 */
	public Color getColor() {
		return _g.getColor();
	}

	/**
	 * @see Painter.setColor
	 */
	public void setColor(Color color) {
		_g.setColor(color);
	}

	/**
	 * Draws text centered around the x and y coordinates
	 * @param text the text to draw
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void drawCenteredText(String text, int x, int y) {
		FontMetrics fm = _g.getFontMetrics();
		int ascent = fm.getAscent();
		int descent = fm.getDescent();

		int xPos = x - fm.stringWidth(text) / 2;
		int yPos = y;

		if( ascent > descent ) 
			yPos += (ascent - descent) / 2;
		else if (ascent < descent)
			yPos -= (descent - ascent) / 2;

		_g.drawString(text, xPos, yPos);
	}
	
	/**
	 * @see Painter.drawText
	 * @param text
	 * @param x
	 * @param y
	 */
	public void drawText(String text, int x, int y) {
		_g.drawString(text, x, y);
	}
	/**
	 * @see Painter.translate
	 */
	public void translate(int x, int y) {
		_g.translate(x, y);
	}
	
	/**
	 * @see Painter.drawImage
	 */
	public void drawImage(Image img, int x, int y, int width, int height) {
		 _g.drawImage(img, x, y, width, height, null);
	}
	
	/**
	 * @see Painter.rotate
	 * angle in radians!
	 */
	public void rotate(double d) {
		 _g.rotate(d);
	}
	
	/**
	 * Sets the Font to a new font using the provided parameters
	 * @param name the name of the font
	 * @param style the font style
	 * @param size the font size
	 */
	public void setFont(String name, int style, int size) {
		_g.setFont(new Font(name, style, size));
	}
	
	/**
	 * Sets a new font size
	 * @param size the new font size
	 */
	public void setFontSize(float size) {
		_g.setFont(_g.getFont().deriveFont(size));
	}
}
