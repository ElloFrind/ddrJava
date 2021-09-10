package piu.gameplay;

import java.awt.Color;
import java.util.List;

/**
 * @author AAA
 * This column is for the up-right arrow
 */
class Upright extends Column {

	private static final String CHARACTER = "🡽";

	/**
	 * creates a new column with a list of arrows and a player
	 * @param times
	 */
	public Upright(List<Period> times, Player player, char inputChar) {
		super(times, player, inputChar);
	}

	/**
	 * Sets up the painter to be used by this column
	 * @param painter the painter to be set up
	 */
	@Override
	protected void setUpPainter(GraphicsPainter painter) {
		super.setUpPainter(painter);
		painter.setColor(Color.BLUE);
	}
	
	/**
	 * Restores the painter to its original state
	 * @param painter the painter to be restored
	 */
	@Override
	protected void restorePainter(GraphicsPainter painter) {
		super.restorePainter(painter);
		painter.setColor(Color.BLACK);
	}
	
	@Override
	protected void drawArrow(int y, GraphicsPainter painter) {
		painter.drawText(CHARACTER, _x, y);
	}
}
