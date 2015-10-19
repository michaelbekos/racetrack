package src.main.java.logic;


import javafx.geometry.Point2D;


/**
*
* @author Tobias Kaulich
*/
public class Track {

	
	private final int TRACK_ID;
	
	private Point2D dimension;
	private Line2D[] outerBoundary;
	private Line2D[] innerBoundary;
	private Point2D[] startingPoints;
	private Line2D finishLine;
	
	/**
	 * 
	 * @param trackID A Unique identifiert for each track
	 */
	public Track(int trackID) {
		TRACK_ID = trackID;
	}
	
	/**
	 * Constructor for the track with inner-outer-boundaries and the min game view dimension measured in squares.
	 * Imagine a sheet of paper where you start drawing from the bottom left to the top right a random track.
	 * In this case, the bottom left is Point(0,0) and the upper right is Point(n,m) if you use a dimension of Point(n,m).
	 * @param outerBoundary The outer boundary as a Line2D array
	 * @param innerBoundary The inner boundary as a Line2D array
	 * @param dimension The game area Dimension
	 * @param trackID The track id
	 * @param startingPoints Points where players can begin palying
	 * @param finishLine The line which has to been crossed to win
	 */
	public Track(Line2D[] outerBoundary, Line2D[] innerBoundary, Point2D dimension, int trackID, Point2D[] startingPoints, Line2D finishLine) {
		TRACK_ID = trackID;
		this.finishLine = finishLine;
		this.outerBoundary = outerBoundary;
		this.innerBoundary = innerBoundary;
		this.startingPoints = startingPoints;
		this.finishLine = finishLine;
		this.dimension = dimension;
		
	}
	
	public Track(Line2D[] outerBoundary, Line2D[] innerBoundary, Point2D dimension, int trackID, Point2D[] startingPoints) {
		this(outerBoundary, 
				innerBoundary, 
				dimension, 
				trackID, 
				startingPoints, 
				new Line2D(
						innerBoundary[innerBoundary.length-1].getEndPoint(),
						outerBoundary[outerBoundary.length-1].getEndPoint())
			);
	}
	
	public Track(Line2D[] outerBoundary, Line2D[] innerBoundary, Point2D dimension, int trackID) {
		this(outerBoundary, 
				innerBoundary, 
				dimension, 
				trackID, 
				getStartingPoints(
						outerBoundary[0].getStartPoint(), 
						innerBoundary[0].getStartPoint()), 
				new Line2D(
						innerBoundary[innerBoundary.length-1].getEndPoint(),
						outerBoundary[outerBoundary.length-1].getEndPoint())
			);
	}
	
	public Track(Point2D[] outerBoundaryPoints, Point2D[] innerBoundaryPoints, Point2D dimension, int trackID, Point2D[] startingPoints) {
		this(	convertPointArrayToLineArray(outerBoundaryPoints), 
				convertPointArrayToLineArray(innerBoundaryPoints), 
				dimension, 
				trackID, 
				startingPoints);
	}
	
	public Track(Point2D[] outerBoundaryPoints, Point2D[] innerBoundaryPoints, Point2D dimension, int trackID, Point2D[] startingPoints, Line2D finishLine) {
		this(	convertPointArrayToLineArray(outerBoundaryPoints), 
				convertPointArrayToLineArray(innerBoundaryPoints), 
				dimension, 
				trackID, 
				startingPoints,
				finishLine);
	}
	
	/**
	 * Convert an array of Point2D to an array of Line2D based on the order of the points.
	 * @param points Points starting from the first to the last which should represent the start end end of the Line2D array. 
	 * @return Array of Line2D objects.
	 */
	private static Line2D[] convertPointArrayToLineArray(Point2D[] points) {
		Line2D[] lineArray = new Line2D[points.length-1];
		for (int i = 0; i < lineArray.length; i++) {
			lineArray[i] = new Line2D(points[i], points[i+1]);
		}
		return lineArray;
	}
	
	
	/**
	 * Generate a Point2D array for all possible starting points. If both points have the same
	 * x or y coordinate, the starting Points will be generated to to fit all possible points in between.
	 * @param p1 Left starting point
	 * @param p2 Right starting point
	 * @return Point2D array for all possible starting points a player can choose from OR (0,0) if there coudn't been created.
	 */
	private static Point2D[] getStartingPoints(Point2D p1, Point2D p2) {
		if (p1.getX() == p2.getX()) {
			int length = (int) Math.abs(p1.getX() - p2.getX());
			boolean p1SmallerP2 = p1.getX() < p2.getX();
			Point2D[] startPoints = new Point2D[length];
			for (int i = 0; i < length; i++) {
				startPoints[i] = new Point2D( p1SmallerP2 ? p1.getX() + i : p2.getX() + i, p1.getX());
			}
			
		} else if (p1.getY() == p2.getY()) {
			int length = (int) Math.abs(p1.getY() - p2.getY());
			boolean p1SmallerP2 = p1.getY() < p2.getY();
			Point2D[] startPoints = new Point2D[length];
			for (int i = 0; i < length; i++) {
				startPoints[i] = new Point2D(p1SmallerP2 ? p1.getY() + i : p2.getY() + i, p1.getY());
			}
			
		} else {
			System.out.println("No vertical or horizontal points were choosen.");
		}
		return new Point2D[]{ new Point2D(0,0) };
	}
	
	/**
	 * 
	 * @return Minimum display track size
	 */
	public Point2D getDimension() {
		return dimension;
	}
	
	/**
	 * 
	 * @return Line2D array for the outer Boundary
	 */
	public Line2D[] getOuterBoundary() {
		return this.outerBoundary;
	}
	
	/**
	 * 
	 * @return Line2D array for the inner Boundary
	 */
	public Line2D[] getInnerBoundary() {
		return this.innerBoundary;
	}

	/**
	 * 
	 * @return Point2D array for all possible starting points a player can choose from
	 */
	public Point2D[] getStartingPoints() {
		return startingPoints;
	}

	/**
	 * 
	 * @return a Line2D object for the finish line,
	 * which has to be crossed to win the game!
	 */
	public Line2D getFinishLine() {
		return finishLine;
	}
	
	/**
	 * 
	 * @return Unique idetifier for each track
	 */
	public int getTrackID() {
		return TRACK_ID;
	}
	

}
