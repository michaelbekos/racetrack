package src.main.java.com.messages;

import javafx.geometry.Point2D;



/**
 * 
 * Indicates that a player did win with his last move, or every other player crashed, and only one player left participating
 * 
 * @author Denis
 *
 */
public class PlayerWonMessage extends RaceTrackMessage{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2423394238650901252L;

	private Integer playerWhoWonByPosition;

	private int playerWhoMovedLastByPosition;

	private double lastPlayerMoveX;
	private double lastPlayerMoveY;

	public  PlayerWonMessage(Point2D lastVector,Integer playerWhoWon,int playerWhoMovedLastByPosition){
		super();

		if(lastVector!=null){
			this.lastPlayerMoveX = lastVector.getX();
			this.lastPlayerMoveY = lastVector.getY();
		}else{
			lastPlayerMoveX = -1000;
			lastPlayerMoveY = -1000;
		}
		this.playerWhoWonByPosition = playerWhoWon;

		this.playerWhoMovedLastByPosition = playerWhoMovedLastByPosition;
	}

	public Point2D getLastPlayerMove(){
		return new Point2D(lastPlayerMoveX, lastPlayerMoveY);
	}

	public Integer getPlayerWhoWonByPosition(){
		return playerWhoWonByPosition;
	}

	public int getPlayerWhoMovedLastByPosition(){
		return playerWhoMovedLastByPosition;
	}
}
