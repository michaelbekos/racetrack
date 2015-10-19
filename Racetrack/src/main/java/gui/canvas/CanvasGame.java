package src.main.java.gui.canvas;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import src.main.java.gui.scenes.GameController;
import src.main.java.gui.utils.Colors;
import src.main.java.gui.utils.Sizes;
import src.main.java.logic.Line2D;
import src.main.java.logic.Track;

/**
 * This class will be used to draw the game area.
 * It will store the game track and the keeps a reference
 * to its controller.
 * 
 * @author Tobias
 *
 */
public class CanvasGame extends CanvasResizable implements ICanvasGame {
	
	// MARK: Private variables
	private GameController delegateController;

	private Point2D trackDimension;
	private Point2D[] innerBoundaryPoints;
	private Point2D[] outerBoundaryPoints;
	private Point2D[] startPoints;
	private Line2D finishLine;

	/**
	 * Whether the base grid should be drawing or not.
	 */
	private boolean drawGrid = true;
	private boolean drawBackground = false;
	private boolean drawingEnabled = false;
	private Color trackBackgroundColor = null;
	
	// MARK: Computed Variables
	public int getGridSquaresSize() {
		if (trackDimension != null) {
			double width = this.getWidth();
			double height = this.getHeight();

			if (width / height < trackDimension.getX() / trackDimension.getY()) {
				// 'Portrait' Mode
				return (int) Math.max(width / trackDimension.getX(), 1);
			} else {
				// 'Landscape' Mode
				return (int) Math.max(height / trackDimension.getY(), 1);
			}
		} else {
			return 0;
		}
	}
	
	public Point2D getTrackOffset() {
		if (trackDimension != null) {
			double gridSquareSize = getGridSquaresSize();
			Point2D canvasSquares = new Point2D(((int) (this.getWidth() / gridSquareSize)),
					((int) (this.getHeight() / gridSquareSize)));

			Point2D canvasAndTrackSquaresDifference = canvasSquares.subtract(this.trackDimension);

			Point2D halfDifference = canvasAndTrackSquaresDifference.multiply(0.5);
			halfDifference = new Point2D((int) halfDifference.getX(), (int) halfDifference.getY());

			return halfDifference;
		} else {
			return new Point2D(0, 0);
		}
	}

	// MARK: Initialization
	/**
	 * Base initialization. Setting up width and height property listener for
	 * redrawing on change.
	 */
	public CanvasGame() {
		super();
	}

	/**
	 * Custom initialization.
	 * 
	 * @param track
	 *            A Track object for drawing inside the canvas.
	 * @param drawGrid
	 *            This values indicate, whether the base grid should be drawn or
	 *            not.
	 */
	public CanvasGame(Track track, boolean drawGrid) {
		this();
		this.drawGrid = drawGrid;
		setTrack(track);
	}

	public CanvasGame(boolean drawGrid) {
		this();
		this.drawGrid = drawGrid;
	}

	public CanvasGame(Track track) {
		this(track, true);
	}

	// MARK: View updates
	public void draw() {
		if (drawingEnabled) {
			this.clear();

			// Draw Grid
			if (drawGrid) {
				this.drawGrid();
			}

			// Draw Track
			this.drawTrack();
		}
	}

	// MARK: Getter and Setter
	public GameController getDelegateController() {
		return delegateController;
	}

	public void setDelegateController(GameController delegateController) {
		this.delegateController = delegateController;
	}

	public void setTrackBackgroundColor(Color trackBackgroundColor) {
		this.trackBackgroundColor = trackBackgroundColor;
		this.updateView();
	}

	public void setDrawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
		this.updateView();
	}

	public void setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
		this.updateView();
	}

	public void setTrack(Track track) {
		// Dimension for centering the game Track
		this.trackDimension = track.getDimension();

		// Line2D Arrays for storing the inner and outer boundaries
		Line2D[] innerBoundary = track.getInnerBoundary();
		Line2D[] outerBoundary = track.getOuterBoundary();

		// Convert the given Track to Points for easier drawing
		this.innerBoundaryPoints = new Point2D[innerBoundary.length + 1];
		this.outerBoundaryPoints = new Point2D[outerBoundary.length + 1];

		this.innerBoundaryPoints[0] = innerBoundary[0].getStartPoint();
		this.outerBoundaryPoints[0] = outerBoundary[0].getStartPoint();

		for (int i = 0; i < innerBoundary.length; i++) {
			this.innerBoundaryPoints[i + 1] = innerBoundary[i].getEndPoint();
		}

		for (int i = 0; i < outerBoundary.length; i++) {
			this.outerBoundaryPoints[i + 1] = outerBoundary[i].getEndPoint();
		}

		this.getBoundaryXYValues();

		this.startPoints = track.getStartingPoints();
		this.finishLine = track.getFinishLine();

		this.drawingEnabled = true;
		this.updateView();
	}
	
	
	// MARK: Draw game objects
	public void drawPlayerTrail(Color playerColor, Point2D[] gridPoints, boolean hasCrashed, boolean isUserPlayer) {
		Color theColor = playerColor;
		if (!isUserPlayer) {
			theColor = theColor.deriveColor(1.0, 0.9, 0.9, 0.25);
		}
		int gridSquareSize = getGridSquaresSize();
		int gridPointDiameter = (int) (gridSquareSize * Sizes.Player.memorizedGridPointDiameter);
		int trailLineWidth = (int) (gridSquareSize * Sizes.Player.trailWidth);
		
		
		
		
		for (int i = 1; i < gridPoints.length; i++) {
			Line2D line = new Line2D(gridPoints[i - 1], gridPoints[i]);
			drawLine(line, theColor, trailLineWidth);
		}
		
		for (int i = 0; i < gridPoints.length; i++) {
			drawPoint(gridPoints[i], theColor.darker(), gridPointDiameter);
		}
		
		int startPointDiameter = (int) (gridSquareSize * Sizes.Player.startingPointDiameter);
		int endPointDiameter = (int) (gridSquareSize * Sizes.Player.currentEndPointDiameter);
		
		// Draw dots
		if (gridPoints.length > 0) {
			// Starting point
			drawPoint(gridPoints[0], playerColor.darker(), startPointDiameter);
			
			if (!hasCrashed) {
				drawPoint(gridPoints[gridPoints.length - 1], playerColor, endPointDiameter);
			}
		}
		
		// Draw crash points
		if (gridPoints.length > 0) {
			if (hasCrashed) {
				Point2D lastPoint = gridPoints[gridPoints.length - 1];
				drawCrossAtPoint(lastPoint, Colors.Player.inactive, endPointDiameter);
				drawCrossAtPoint(lastPoint, playerColor.brighter(), (int) (endPointDiameter*0.9));
				drawCrossAtPoint(lastPoint, playerColor.brighter().brighter(), (int) (endPointDiameter*0.6));
			}
		}
	}

	

	/**
	 * Draws the user defined track on top of the grid. If user had enabled
	 * background drawing the background would be drawn underneath the track
	 * boundary.
	 */
	private void drawTrack() {
		if (this.drawBackground) {
			this.drawTrackBackground();

			if (this.getWidth() > 300 && this.getHeight() > 300) {
				this.drawStartingPoints();
				this.drawFinishLine();
			}
		}
		this.drawTrackBoundary();
	}

	/**
	 * Generates a temporary string for excluding the dracks inside and only
	 * coloring the terrain next to it.
	 */
	private void drawTrackBackground() {
		double width = this.getWidth();
		double height = this.getHeight();

		// Get graphics context
		GraphicsContext gc = this.getGraphicsContext2D();

		double[][] boundaryValues = getBoundaryXYValues();

		double[] xValuesInnerBoundary = boundaryValues[0];
		double[] yValuesInnerBoundary = boundaryValues[1];

		double[] xValuesOuterBoundary = boundaryValues[2];
		double[] yValuesOuterBoundary = boundaryValues[3];

		Color fillColor = Colors.Track.clearBackground;
		if (this.drawGrid) {
			fillColor = Colors.Track.grasBackground;
		} else if (this.trackBackgroundColor != null) {
			fillColor = this.trackBackgroundColor;
		}

		gc.setFill(fillColor);

		for (int i = 0; i < xValuesOuterBoundary.length / 2; i++) {
			double tmp = xValuesOuterBoundary[i];
			xValuesOuterBoundary[i] = xValuesOuterBoundary[xValuesOuterBoundary.length - i - 1];
			xValuesOuterBoundary[xValuesOuterBoundary.length - i - 1] = tmp;
		}

		double[] xInnerValues = new double[xValuesInnerBoundary.length + xValuesOuterBoundary.length + 6];
		System.arraycopy(xValuesInnerBoundary, 0, xInnerValues, 0, xValuesInnerBoundary.length);
		System.arraycopy(xValuesOuterBoundary, 0, xInnerValues, xValuesInnerBoundary.length,
				xValuesOuterBoundary.length);

		for (int i = 0; i < yValuesOuterBoundary.length / 2; i++) {
			double tmp = yValuesOuterBoundary[i];
			yValuesOuterBoundary[i] = yValuesOuterBoundary[yValuesOuterBoundary.length - i - 1];
			yValuesOuterBoundary[yValuesOuterBoundary.length - i - 1] = tmp;
		}

		double[] yInnerValues = new double[yValuesInnerBoundary.length + yValuesOuterBoundary.length + 6];
		System.arraycopy(yValuesInnerBoundary, 0, yInnerValues, 0, yValuesInnerBoundary.length);
		System.arraycopy(yValuesOuterBoundary, 0, yInnerValues, yValuesInnerBoundary.length,
				yValuesOuterBoundary.length);

		xInnerValues[xInnerValues.length - 1 - 5] = xValuesOuterBoundary[0];
		xInnerValues[xInnerValues.length - 1 - 4] = 0;
		xInnerValues[xInnerValues.length - 1 - 3] = 0;
		xInnerValues[xInnerValues.length - 1 - 2] = width;
		xInnerValues[xInnerValues.length - 1 - 1] = width;
		xInnerValues[xInnerValues.length - 1 - 0] = 0;

		yInnerValues[yInnerValues.length - 1 - 5] = yValuesInnerBoundary[0];
		yInnerValues[yInnerValues.length - 1 - 4] = height;
		yInnerValues[yInnerValues.length - 1 - 3] = 0;
		yInnerValues[yInnerValues.length - 1 - 2] = 0;
		yInnerValues[yInnerValues.length - 1 - 1] = height;
		yInnerValues[yInnerValues.length - 1 - 0] = height;

		gc.fillPolygon(xInnerValues, yInnerValues, xInnerValues.length);
	}

	/**
	 * Draws the track itself on the canvas in a dark gray.
	 */
	private void drawTrackBoundary() {
		int boundaryWidth = (int) (getGridSquaresSize() * Sizes.Track.boundaryWidth);
		
		GraphicsContext gc = getGraphicsContext2D();
		// Set color
		gc.setStroke(Colors.Track.boundary);
		// Prevent sharp edges in small views
		gc.setMiterLimit(5.0);
		// Set line width
		gc.setLineWidth(boundaryWidth);

		double[][] boundaryValues = getBoundaryXYValues();

		gc.strokePolyline(boundaryValues[0], boundaryValues[1], boundaryValues[0].length);
		gc.strokePolyline(boundaryValues[2], boundaryValues[3], boundaryValues[2].length);
	}

	/**
	 * Draws the starting points for the given track.
	 */
	private void drawStartingPoints() {
		int pointSize = (int) (getGridSquaresSize() * Sizes.Track.startingPointsDiameter);
		
		for (int i = 0; i < this.startPoints.length; i++) {
			drawPoint(this.startPoints[i], Colors.Track.startingPoints, pointSize);
		}
	}

	/**
	 * Draws the finish line for the given track.
	 */
	private void drawFinishLine() {
		int finishLineWidth = (int) (getGridSquaresSize() * Sizes.Track.finishLineWidth);
		
		this.drawLine(this.finishLine, Colors.Track.finishLine, finishLineWidth);
	}
	
	/**
	 * Draws the base grid.
	 */
	private void drawGrid() {
		double width = this.getWidth();
		double height = this.getHeight();

		GraphicsContext gc = this.getGraphicsContext2D();

		// Set color
		gc.setStroke(Colors.Canvas.gridLines);
		// Set line width
		double lineWidth = 1.0;
		gc.setLineWidth(lineWidth);

		// Get grid square size for current window size
		double gridSquaresSize = getGridSquaresSize();
		int verticalLinesCount = (int) Math.ceil(width / gridSquaresSize);
		int horizontalLinesCount = (int) Math.ceil(height / gridSquaresSize);

		// Generate vertical lines
		for (int i = 0; i < verticalLinesCount; i++) {
			double x = i * gridSquaresSize;
			x = (lineWidth % 2 == 1) ? x + 0.5 : x;

			gc.strokeLine(x, 0, x, height);
		}

		// Generate horizontal lines
		for (int i = 0; i < horizontalLinesCount; i++) {
			double y = i * gridSquaresSize;
			y = (lineWidth % 2 == 1) ? y + 0.5 : y;

			gc.strokeLine(0, y, width, y);
		}
	}
	
	// MARK: Draw lines and points on canvas
	public void drawLine(Line2D line, Color color, double lineWidth) {
		GraphicsContext gc = getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineWidth(lineWidth);

		Point2D trackOffset = getTrackOffset();

		Point2D startPoint = getPixelCoordsForPoint(line.getStartPoint().add(trackOffset));
		Point2D endPoint = getPixelCoordsForPoint(line.getEndPoint().add(trackOffset));

		gc.strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}

	public void drawPoint(Point2D point, Color color, int size) {
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(color);

		Point2D trackOffset = getTrackOffset();
		Point2D drawCoord = getPixelCoordsForPoint(point.add(trackOffset));

		gc.fillOval(drawCoord.getX() - size / 2, drawCoord.getY() - size / 2, size, size);
	}

	public void drawPoint(Point2D point, Color color) {
		this.drawPoint(point, color, 20);
	}

	public void drawPoint(Point2D point) {
		this.drawPoint(point, Color.GREEN, 20);
	}
	
	// MARK: Draw special markers
	public void drawCrossAtPoint(Point2D point, Color color, int size) {
		GraphicsContext gc = getGraphicsContext2D();
		gc.setStroke(color);

		Point2D trackOffset = getTrackOffset();
		Point2D drawCoord = getPixelCoordsForPoint(point.add(trackOffset));
		
		// Line '/'
		gc.strokeLine(drawCoord.getX() - size, drawCoord.getY() - size, drawCoord.getX() + size, drawCoord.getY() + size);
		// Line '\'
		gc.strokeLine(drawCoord.getX() - size, drawCoord.getY() + size, drawCoord.getX() + size, drawCoord.getY() - size);
	}
	
	// MARK: Helper functions
	/**
	 * Calculate the X and Y coordinates for drawing all track points (inner and
	 * outer) at the appropriate coordinates in the canvas.
	 * double[1] : X inner boundaries
	 * double[2] : Y inner boundaries
	 * double[3] : X outer boundaries
	 * double[4] : Y outer boundaries
	 * 
	 * @return The X and Y coordiantes for the inner and outer boundaries.
	 */
	private double[][] getBoundaryXYValues() {
		double[] xValuesInnerBoundary = new double[this.innerBoundaryPoints.length];
		double[] yValuesInnerBoundary = new double[this.innerBoundaryPoints.length];

		double[] xValuesOuterBoundary = new double[this.outerBoundaryPoints.length];
		double[] yValuesOuterBoundary = new double[this.outerBoundaryPoints.length];

		Point2D trackOffset = getTrackOffset();

		for (int i = 0; i < this.innerBoundaryPoints.length; i++) {
			Point2D innerBoundaryPoint = getPixelCoordsForPoint(this.innerBoundaryPoints[i].add(trackOffset));
			// Point2D innerBoundaryPoint =
			// getPixelCoordsForPointCoords(innerBoundaryPoints[i].getX(),
			// innerBoundaryPoints[i].getY());
			xValuesInnerBoundary[i] = innerBoundaryPoint.getX();
			yValuesInnerBoundary[i] = innerBoundaryPoint.getY();
		}

		for (int i = 0; i < this.outerBoundaryPoints.length; i++) {
			Point2D outerBoundaryPoint = getPixelCoordsForPoint(this.outerBoundaryPoints[i].add(trackOffset));
			// Point2D outerBoundaryPoint =
			// getPixelCoordsForPointCoords(outerBoundaryPoints[i].getX(),
			// outerBoundaryPoints[i].getY());
			xValuesOuterBoundary[i] = outerBoundaryPoint.getX();
			yValuesOuterBoundary[i] = outerBoundaryPoint.getY();
		}

		return new double[][] { xValuesInnerBoundary, yValuesInnerBoundary, xValuesOuterBoundary,
				yValuesOuterBoundary };
	}

	/**
	 * Converts a Point2D to a grid point on the canvas
	 * 
	 * @param point
	 *            which should be converted
	 * @return Point2D object representing the pixel value for drawing in the
	 *         canvas
	 */
	private Point2D getPixelCoordsForPoint(Point2D point) {
		double gridSquaresSize = getGridSquaresSize();
		Point2D newPoint = new Point2D(point.getX() * gridSquaresSize,
				(((int) (this.getHeight() / gridSquaresSize)) * gridSquaresSize) - (point.getY() * gridSquaresSize));

		return newPoint;
	}
}
