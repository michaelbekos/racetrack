package src.main.java.com.messages;

import javafx.geometry.Point2D;

/**
 * Message object containing a vector and whether it is a starting point or not.
 * If it is not a starting vector,
 * this message is a move while the game is running.
 * The server will process that move and respond accordingly
 * 
 * If it  is a starting vector,
 * This represents the coice to start of a player at the start of the game
 * 
 * @author Denis
 *
 */
public class VectorMessage extends RaceTrackMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2613644493144479239L;
	/**
	 * The Vector, the client has chosen
	 */
//	private Point2D vector;
	
	private double vectorX;
	private double vectorY;
	
	
	public VectorMessage(Point2D vector){
		super();
		this.vectorX = vector.getX();
		this.vectorY = vector.getY();
	}
	
	public Point2D getVector(){
		return new Point2D(vectorX, vectorY);
	}

	
	
}
