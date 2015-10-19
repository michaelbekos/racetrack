package src.main.java.com.messages;

/**
 * This Message will be sent as a response to a VectorMessage by a client.
 * if a move, the client sent is invalid, he will get an InvalidMoveMessage as a response
 * @author Denis
 *
 */
public class InvalidMoveMessage extends RaceTrackMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1144961575306788286L;

	public InvalidMoveMessage(){
		super();
	}
}
