package src.main.java.logic;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

/**
 * Line2D Class extends Javafx gemoetry line for easier usage. Adding
 * constructors for two point objects. Intersection with another line. A start
 * and stop point.
 *
 * @author Tobias
 */
public class Line2D extends Line {
	/**
	 * Costum constructor using two points
	 * 
	 * @param p1
	 *            start point
	 * @param p2
	 *            end point
	 */
	public Line2D(Line l) {
		super(l.getStartX(), l.getStartX(), l.getEndX(), l.getEndX());
	}
	/**
	 * Costum constructor using two points
	 * 
	 * @param p1
	 *            start point
	 * @param p2
	 *            end point
	 */
	public Line2D(Point2D p1, Point2D p2) {
		super(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/**
	 * Costum intersects method between two lines
	 * 
	 * @param line
	 *            to test intersection with
	 * @return weather there has been an intersection or not
	 */
	public boolean intersects(Line line) {
		return super.intersects(this.getStartX(), this.getStartY(),
				this.getEndX(), this.getEndY());
	}

	/**
	 * @return Line start point
	 */
	public Point2D getStartPoint() {
		return new Point2D(this.getStartX(), this.getStartY());
	}

	/**
	 * @return Line end point
	 */
	public Point2D getEndPoint() {
		return new Point2D(this.getEndX(), this.getEndY());
	}

	/**
	 * Intersection helper methode. returns the exact intersection point of the
	 * playerLine with the outer/ innerBoundary credit to: Alexander Hristov,
	 * http://www.ahristov.com
	 * 
	 * @param x1
	 *            x1
	 * @param y1
	 *            y1
	 * 
	 * @param x2
	 *            x2
	 * @param y2
	 *            y2
	 * 
	 * @param x3
	 *            x3
	 * @param y3
	 *            y3
	 * 
	 * @param x4
	 *            x4
	 * @param y4
	 *            y4
	 * @return Intersection point
	 */
	private Point2D intersection(double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {

		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (d == 0)
			return null;

		double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2)
				* (x3 * y4 - y3 * x4))
				/ d;
		double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2)
				* (x3 * y4 - y3 * x4))
				/ d;

		return new Point2D(xi, yi);
	}

	/**
	 * Calculates the intersection point of two lines This function isn't
	 * implemented for a shape object, because intersection only checks whether
	 * an intersection happens or not!
	 * 
	 * @param secondLine
	 *            A Line2D object which should be checked agains intersection
	 * @return The intersection of both lines as a Point2D or null if there is
	 *         no intersection
	 * 
	 * @author Tobias
	 */
	public Point2D pointOfIntersection(Line2D secondLine) {
		return this.intersection(this.getStartX(), this.getStartY(),
				this.getEndX(), this.getEndY(), secondLine.getStartX(),
				secondLine.getStartY(), secondLine.getEndX(),
				secondLine.getEndY());
	}
}
