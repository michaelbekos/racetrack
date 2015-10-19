package src.main.java.gui.canvas;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import src.main.java.gui.scenes.GameController;
import src.main.java.logic.Line2D;
import src.main.java.logic.Track;

public interface ICanvasGame extends ICanvasResizable {

	// MARK: Computed Variables
	/**
	 * Get grid squares size based on current canvas dimension and
	 * track-size-ratio. Because the track can be initialized in the background
	 * the minimum size of a square is set to 1.
	 * 
	 * @return the width and hight of a grid square in the canvas.
	 */
	public int getGridSquaresSize();

	/**
	 * Calculate the track Offset for positioning any kind of point on the
	 * canvas
	 * 
	 * @return Point2D which represents the track offset in x and y direction
	 */
	public Point2D getTrackOffset();

	// MARK: View updates
	/**
	 * Based on user settings, this methode - if drawing is enabled - will first
	 * clear the canvas and than draws the grid and track on top of it.
	 */
	abstract void draw();

	// MARK: Getter and Setter
	/**
	 * 
	 * @return The GameController it is displayed upon.
	 */
	public GameController getDelegateController();

	/**
	 * Sets the delegateController for the canvas.
	 * 
	 * @param delegateController
	 *            The GameController where the canvas element is displayed on.
	 */
	public void setDelegateController(GameController delegateController);

	/**
	 * Set a custom grass or off track color.
	 * 
	 * @param trackBackgroundColor
	 *            Color will be displayed as the grass surounding the track
	 */
	public void setTrackBackgroundColor(Color trackBackgroundColor);

	/**
	 * Set if the grid should be drawn or not.
	 * 
	 * @param drawGrid
	 *            Indicates whether the grid should be drawn or not.
	 */
	public void setDrawGrid(boolean drawGrid);

	/**
	 * Set if a choosen track background color should be drawn or if the grid is
	 * drawn a green background and it the grid isn't drawn a white background
	 * should be visible or not.
	 * 
	 * @param drawBackground
	 *            Indicates if the background color should be drawn.
	 */
	public void setDrawBackground(boolean drawBackground);

	/**
	 * Set a track to display it on the canvas. This function will also
	 * translate all track segments/lines into x and y arrays for easier drawing
	 * the track and the off-track canvas.
	 * 
	 * @param track
	 *            Track object which should be drawn inside the canvas.
	 */
	public void setTrack(Track track);

	// MARK: Draw game objects
	/**
	 * Draws a players trail on the canvas. The trail is drawn based on the
	 * gridPoints and uses the playerColor. If a player has crashed, the last
	 * gridPoint is drawn as a cross, if not, the last gridPoint will simply be
	 * drawn as a bigger circle.
	 * 
	 * @param playerColor
	 *            The player color in which the trail should be drawn.
	 * @param gridPoints
	 *            GridPoints where the player had been before and where the
	 *            trail will be drawn.
	 * @param hasCrashed
	 *            Defines if a player has crashed and the last gridPoint will be
	 *            used as the crashPoint or not.
	 */
	public void drawPlayerTrail(Color playerColor, Point2D[] gridPoints, boolean hasCrashed, boolean isUserPlayer);

	// MARK: Draw lines and points on canvas
	/**
	 * Draws a simple line on the canvas.
	 * 
	 * @param line
	 *            The line to draw.
	 * @param color
	 *            The color in which the line should be drawn.
	 * @param lineWidth
	 *            The width of the line.
	 */
	public void drawLine(Line2D line, Color color, double lineWidth);

	/**
	 * Draws a simple point at a given point.
	 * 
	 * @param point
	 *            The center point.
	 * @param color
	 *            The user defined color.
	 * @param size
	 *            A custom size.
	 */
	public void drawPoint(Point2D point, Color color, int size);

	public void drawPoint(Point2D point, Color color);

	public void drawPoint(Point2D point);

	// MARK: Draw special markers
	public void drawCrossAtPoint(Point2D point, Color color, int size);

}
