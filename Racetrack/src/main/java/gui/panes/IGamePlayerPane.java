package src.main.java.gui.panes;

import javafx.scene.paint.Color;

public interface IGamePlayerPane {

	/**
	 * Sets the stroke width property for the circle representing the player
	 * 
	 * @param percentage
	 *            Relative to the circle size
	 */
	public void setBorderStrokeWidthFillPercent(double percentage);

	/**
	 * Set stroke width to equal the player circle radius
	 */
	public void setFillBorderStrokeWidth();

	/**
	 * Set player color as circle color
	 * 
	 * @param color
	 *            Player color
	 */
	public void setColor(Color color);

	/**
	 * Set player name as label text
	 * 
	 * @param name
	 *            Player name
	 */
	public void setName(String name);

}
