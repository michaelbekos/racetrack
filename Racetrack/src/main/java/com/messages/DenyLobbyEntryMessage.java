package src.main.java.com.messages;

/**
 * This Message will be send to a client after he requested entry to a lobby.
 * If the lobby is full or has already started, A DenyLobbyEntryMessage will be sent back.
 * @author Denis
 *
 */
public class DenyLobbyEntryMessage extends RaceTrackMessage{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3706057984285494354L;

	public DenyLobbyEntryMessage(){
		super();
	}
}
