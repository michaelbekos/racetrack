package src.main.java.gui.canvas;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import src.main.java.logic.Line2D;

public interface ICanvasPreview extends ICanvasResizable {

	// MARK: Getter and setter
	public Point2D getTrackOffset();

	public void setPlayerColor(Color playerColor);

	public void setTrackOffset(Point2D trackOffset);

	public double getGridSquaresSize();

	public void setGridSquaresSize(double gridSquaresSize);

	public void setPlayerGridLocation(Point2D playerGridLocation);

	public void setPlayerVelocity(Point2D playerVelocity);

	public void setPlayerVelocityChange(Point2D velocityChange);

	// MARK: Function and methods
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

	public void strokePoint(Point2D point, Color color, int diagonal, int width);

	public void hightlightCurrentMousePosition(Point2D gridPoint);

	public void highlightStartingPoint(Point2D[] trackStartingPoints, Point2D hightlightPoint);

}
