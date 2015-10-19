package src.main.java.gui.canvas;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import src.main.java.gui.utils.Colors;
import src.main.java.gui.utils.Sizes;
import src.main.java.logic.Line2D;

/**
 * This class will be used for showing the current cursor position on top of the
 * drawn game, as well as showing the 3x3 Matrix where a player can go next.
 * 
 * @author Tobias
 *
 */
public class CanvasPreview extends CanvasResizable implements ICanvasPreview {

	// MARK: Private variables
	private Point2D trackOffset;
	private double gridSquaresSize;

	private Point2D playerGridLocation;
	private Point2D playerVelocity;
	private Point2D playerVelocityChange;
	private Point2D mousePosition;
	private Color playerColor;

	// MARK: Initialization
	public CanvasPreview() {
	}
	
	// MARK: View updates
	@Override
	public void draw() {
		this.clear();
		
		if (playerGridLocation != null && playerVelocity != null) {
			drawPreviewGrid();
		}
		
		if (mousePosition != null) {
			int mousePositionDiameter = (int) (this.gridSquaresSize * Sizes.MovePreview.mousePointDiameter);
			drawPoint(mousePosition, Colors.Other.mousePosition, mousePositionDiameter);
		}
	}
	
	public void resetView() {
		// Clear canvas
		super.resetView();
		
		trackOffset = new Point2D(0,0);
		gridSquaresSize = 0;
		
		playerGridLocation = null;
		playerVelocity = null;
		playerVelocityChange = null;
		mousePosition = null;
	}
	

	// MARK: Getter and setter
	public Point2D getTrackOffset() {
		return trackOffset;
	}
	
	public void setPlayerColor(Color playerColor) {
		this.playerColor = playerColor;
	}

	public void setTrackOffset(Point2D trackOffset) {
		this.trackOffset = trackOffset;
	}

	public double getGridSquaresSize() {
		return gridSquaresSize;
	}

	public void setGridSquaresSize(double gridSquaresSize) {
		this.gridSquaresSize = gridSquaresSize;
	}

	public void setPlayerGridLocation(Point2D playerGridLocation) {
		this.playerGridLocation = playerGridLocation;
	}

	public void setPlayerVelocity(Point2D playerVelocity) {
		this.playerVelocity = playerVelocity;
	}

	public void setPlayerVelocityChange(Point2D velocityChange) {
		this.playerVelocityChange = velocityChange;
		if (velocityChange != null) {
			draw();
		}
	}

	// MARK: Function and methods
	public void drawLine(Line2D line, Color color, double lineWidth) {
		if (trackOffset != null) {
			GraphicsContext gc = getGraphicsContext2D();
			gc.setStroke(color);
			gc.setLineWidth(lineWidth);

			Point2D startPoint = getPixelCoordsForPoint(line.getStartPoint().add(trackOffset));
			Point2D endPoint = getPixelCoordsForPoint(line.getEndPoint().add(trackOffset));

			gc.strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
		}
	}

	public void drawPoint(Point2D point, Color color, int size) {
		if (trackOffset != null) {
			GraphicsContext gc = getGraphicsContext2D();
			gc.setFill(color);

			Point2D drawCoord = getPixelCoordsForPoint(point.add(trackOffset));

			gc.fillOval(drawCoord.getX() - size / 2, drawCoord.getY() - size / 2, size, size);
		}
	}
		

	public void drawPoint(Point2D point, Color color) {
		this.drawPoint(point, color, 10);
	}

	public void drawPoint(Point2D point) {
		this.drawPoint(point, Color.GRAY, 10);
	}
	
	public void strokePoint(Point2D point, Color color, int diagonal, int width) {
		if (trackOffset != null) {
			GraphicsContext gc = getGraphicsContext2D();
			gc.setStroke(color);
			gc.setLineWidth(width);
			
			Point2D drawCoord = getPixelCoordsForPoint(point.add(trackOffset));
			gc.strokeOval(drawCoord.getX() - diagonal / 2, drawCoord.getY() - diagonal / 2, diagonal, diagonal);
		}
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
		Point2D newPoint = new Point2D(point.getX() * gridSquaresSize,
				(((int) (this.getHeight() / gridSquaresSize)) * gridSquaresSize) - (point.getY() * gridSquaresSize));

		return newPoint;
	}

	// MARK: Player function and methods
	private void drawPreviewGrid() {
		int simplePointDiameter = (int) (this.gridSquaresSize * Sizes.MovePreview.simplePreviewPointDiameter);
		int simpleHighlightDiameter = (int) (this.gridSquaresSize * Sizes.MovePreview.simpleHightlightPointDiameter);
		
		Color previewColor = playerColor == null ? Colors.Track.pointPreview : playerColor;
		
		final int matrixSize = 3;
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				Point2D velocityChangeMatrix = new Point2D(j - 1, -1 * (i - 1));
				Point2D newPlayerVelocity = playerVelocity.add(velocityChangeMatrix);
				Point2D previewNewUserLocation = playerGridLocation.add(newPlayerVelocity);

				if (velocityChangeMatrix.equals(playerVelocityChange)) {
					int trailLineWidth = (int) (this.getGridSquaresSize() * Sizes.Player.trailWidth);
					this.drawLine(new Line2D(playerGridLocation, previewNewUserLocation), previewColor, trailLineWidth);
					
					drawPoint(previewNewUserLocation, previewColor, simpleHighlightDiameter);
				} else {
					drawPoint(previewNewUserLocation, previewColor, simplePointDiameter);
				}
			}
		}
		
		int currentEndPointDiameter = (int) (this.gridSquaresSize * Sizes.Player.currentEndPointDiameter);
		drawPoint(playerGridLocation, playerColor, currentEndPointDiameter);
	}

	public void hightlightCurrentMousePosition(Point2D gridPoint) {
		mousePosition = gridPoint;
		draw();
	}

	public void highlightStartingPoint(Point2D[] trackStartingPoints, Point2D hightlightPoint) {
		int simpleHighlightDiameter = (int) (this.gridSquaresSize * Sizes.MovePreview.simpleHightlightPointDiameter);
		
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		mousePosition = null;
		
		for (int i = 0; i < trackStartingPoints.length; i++) {
			Point2D point = trackStartingPoints[i];
			if (point.equals(hightlightPoint)) {
				strokePoint(point, Colors.Track.startingPoints.brighter(), simpleHighlightDiameter, (int) (simpleHighlightDiameter/3.0));
			} else {
//				drawPoint(point, Colors.Track.trackStartingPoints);
			}
		}
	}

}
