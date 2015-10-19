package src.main.java.com.messages;

import javafx.geometry.Point2D;


/**
 * If a client has sent a valid move, 
 * this message gets send to all connected clients in a specific game session
 * 
 * It contains the vector, where the client is going to be, whether there was collision, or not
 * if there was collision, the collisionpoint
 * 
 * the next player to move and whether it was a startingpoint vector or not
 * @author Denis
 *
 */
public class BroadCastMoveMessage extends RaceTrackMessage{



	/**
	 * 
	 */
	private static final long serialVersionUID = 2999656430202554540L;
	/**
	 * The Point, where the client moves to
	 */
	private double vectorX;
	private double vectorY;

	/**
	 * which client moved in the group
	 */
	private int playerInGroup;

	/**
	 * crashed into a wall or not
	 */
	private boolean isColliding;

	/**
	 * disconnected and removed from the game
	 */
	private boolean isDisconnected;	
	
	/**
	 * where the client crashed
	 */
	private double collisionX ;
	private double collisionY;

	/**
	 * did that player win with that move?
	 */
	private boolean hasPlayerWon;

	/**
	 * the velocity of playerInGroup
	 */
	private double velocityX;
	private double velocityY;

	/**
	 * Which round is it?
	 */
	private int round;

	/**
	 * which client is allowed to move next 
	 */
	private int nextPlayerInGroup;

	/**
	 * determines if everyplayer has crashed in a session, then the game is over
	 */
	private boolean hasEveryPlayerCrashed;

	/**
	 * Use this constructor, if it is a normal move within the game.
	 * 
	 * @param vector where the client moves to
	 * @param collision where the client crashed, null, if there is no crash
	 * @param isDisconnected is the Client disconnected and thus removed from the game?
	 * @param nextPlayer which client is next to move
	 * @param playerInGroup which client moved
	 */
	public BroadCastMoveMessage(Point2D vector, Point2D collision, boolean isDisconnected, int nextPlayer, int playerInGroup, Point2D playerVelocity, int round){
		super();
		
		if(vector!=null){
			vectorX = vector.getX();
			vectorY = vector.getY();
		}
		
		//check if there was a collision
		if(collision == null){
			isColliding = false;

			collisionX = Double.MAX_VALUE;
			collisionY = Double.MAX_VALUE;

		}else{
			isColliding = true;
			collisionX = collision.getX();
			collisionY= collision.getY();

		}
		
		nextPlayerInGroup = nextPlayer;
		this.playerInGroup = playerInGroup;
		
		this.isDisconnected = isDisconnected;

		this.round = round;
		
		if(playerVelocity != null){
			this.velocityX = playerVelocity.getX();
			this.velocityY = playerVelocity.getY();
		}else{
			this.velocityX = Double.MAX_VALUE;
			this.velocityY = Double.MAX_VALUE;
		}
		
	}
	
	public int getPlayerInGroup(){
		return playerInGroup;
	}

	public Point2D getVector(){
		return new Point2D(vectorX,vectorY);
	}

	public boolean getIsColliding(){
		return isColliding;
	}

	public Point2D getCollisionPoint(){
		if(!isColliding)
			return null;
		else
			return new Point2D(collisionX, collisionY);
	}

	public Point2D getVelocity(){
		return new Point2D(velocityX, velocityY);
	}

	public void setCollisionPoint(Point2D col){
		if(col != null){
			this.isColliding = true;
			collisionX = col.getX();
			collisionY = col.getY();
		}else{
			this.isColliding = false;
		}	

	}

	public int getNextPlayer(){
		return nextPlayerInGroup;
	}

	public void setNextPlayer(int nextP){
		this.nextPlayerInGroup = nextP;
	}

	public boolean getHasEveryPlayerCrashed(){
		return hasEveryPlayerCrashed;
	}

	public void setHasEveryPlayerCrashed(boolean b){
		this.hasEveryPlayerCrashed = b;
	}

	public void setHasPlayerWon(boolean b){
		this.hasPlayerWon = b;
	}

	public boolean hasPlayerWon(){
		return hasPlayerWon;
	}

	public int getRound(){
		return round;
	}

	public boolean isDisconnected() {
		return isDisconnected;
	}
	
}
