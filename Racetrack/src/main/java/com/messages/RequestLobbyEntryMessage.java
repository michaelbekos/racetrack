package src.main.java.com.messages;


/**
 * A client sends this message to the server, after clicking the 
 * 'enter lobby' button.
 * @author Denis
 *
 */
public class RequestLobbyEntryMessage extends RaceTrackMessage{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4281760814828337214L;
	/**
	 * the lobby, the client tries to enter
	 */
	private int lobbyIdToEnter;


	public RequestLobbyEntryMessage(int id){
		super();
		this.lobbyIdToEnter = id;
	}
	
	public int getLobbyIdToEnter(){
		System.out.println("in message: "+lobbyIdToEnter);
		return lobbyIdToEnter;
	}

	
}
