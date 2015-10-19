
package src.main.java.gui.canvas;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import src.main.java.gui.utils.Colors;

/**
 * Standard background for all navigation scenes. Drawing only canvas lines
 * without a track.
 * 
 * @author Tobias Kaulich
 */
public class CanvasNavigationBackground extends CanvasResizable implements ICanvasNavigationBackground {

	// MARK: Private variables
	private double factor;
	private double minGridSquaresCount = 1;

	// MARK: Computed variables
	public double getMinCanvasDimension() {
		double width = this.getWidth();
		double height = this.getHeight();

		return Math.min(width, Math.min(height, width / factor));
	}
	
	public int calcGridSquareSize() {
		return (int) Math.max(getMinCanvasDimension() / minGridSquaresCount, 1);
	}

	// MARK: Initialization

	/**
	 * Initilization for drawing a grid based on given dimensions.
	 * 
	 * @param dimension
	 *            Minimum grid size dimension
	 */
	public CanvasNavigationBackground(Point2D dimension) {
		super();

		this.minGridSquaresCount = Math.max(dimension.getX(), dimension.getY());
		this.factor = dimension.getX() / dimension.getY();

		this.widthProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable o) {
				draw();
			}
		});
		this.heightProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable o) {
				draw();
			}
		});

	}
	
	public CanvasNavigationBackground() {
		this(new Point2D(40, 30));
	}
	
	// MARK: View updates
	/**
	 * Methode which will be called if the canvas size change.
	 */
	@Override
	public void draw() {
		this.clear();
		drawCanvasGrid();
	}

	// MARK: Drawing
	/**
	 * Draws the basic canvas grid.
	 */
	private void drawCanvasGrid() {
		double width = this.getWidth();
		double height = this.getHeight();

		GraphicsContext gc = getGraphicsContext2D();

		// Set color
		gc.setStroke(Colors.Canvas.gridLines);
		// Set line width
		double lineWidth = 1.0;
		gc.setLineWidth(lineWidth);

		// Get grid square size for current window size
		double gridSquaresSize = calcGridSquareSize();

		// Generate vertical lines
		for (int x = 0; x < width; x += gridSquaresSize) {
			double x1 = (lineWidth % 2 == 1) ? x + 0.5 : x;
			gc.strokeLine(x1 - 1, 0, x1 - 1, height);
		}

		// Generate horizontal lines
		for (int y = 0; y < height; y += gridSquaresSize) {
			double y1 = (lineWidth % 2 == 1) ? y + 0.5 : y;
			gc.strokeLine(0, y1 - 1, width, y1 - 1);
		}
	}

}