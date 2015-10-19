package src.main.java.com.messages;

import src.main.java.logic.ILobbyInformation;


/**
 * When inside of a lobby, this message contains 
 * the lobby object of the clients session,
 * telling the clients, what has changed.
 * @author Denis
 *
 */
public class SendOptionsMessage extends RaceTrackMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8577444178804843279L;
	/**
	 * updated Lobby, so the clients can draw the new Lobby
	 */
	private ILobbyInformation newLobbyInfromation;
	
	public SendOptionsMessage(ILobbyInformation newLobbyInformations){
		super();
		this.newLobbyInfromation = newLobbyInformations;
	}
	
	public ILobbyInformation getNewLobbyOptions(){
		return newLobbyInfromation;
	}
}
