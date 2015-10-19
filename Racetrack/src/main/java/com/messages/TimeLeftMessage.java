package src.main.java.com.messages;


/**
 * This Message informs the current player who has to play, about how much time he has to send his move
 * @author Denis
 *
 */
public class TimeLeftMessage extends RaceTrackMessage{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2222916206776272942L;
	private int timeLeftToMove;
	
	public TimeLeftMessage(int timeLeftToMove){
		super();
		
		this.timeLeftToMove = timeLeftToMove;
	}
	
	public int getTimeLeftToMove(){
		return timeLeftToMove;
	}
}
