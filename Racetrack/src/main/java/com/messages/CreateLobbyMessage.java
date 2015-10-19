package src.main.java.com.messages;

import src.main.java.logic.ILobbyInformation;

/**
 * This class gets send by a client, who created a lobby.
 * The message contains the Information about what kind of Lobby the Client wants to create
 * 
 * This lobby contains lobbyInformations and the clients id
 * @author denis_000
 *
 */
public class CreateLobbyMessage extends RaceTrackMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6300370812896078893L;
	private ILobbyInformation lobbyInformation;
	
	public CreateLobbyMessage(ILobbyInformation lobbyInformation){
		this.lobbyInformation = lobbyInformation;
	}
	
	public ILobbyInformation getLobbyInformaion(){
		return lobbyInformation;
	}
}
