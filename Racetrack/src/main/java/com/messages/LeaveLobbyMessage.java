package src.main.java.com.messages;


/**
 * This Message will be sent from the Client to the Server,
 * indicating that the client wants to leave the lobby,
 * or the game while it is running.
 * 
 * @author Denis
 *
 */
public class LeaveLobbyMessage extends RaceTrackMessage{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3078847770265881752L;

	public LeaveLobbyMessage(){
		super();
	}
}
