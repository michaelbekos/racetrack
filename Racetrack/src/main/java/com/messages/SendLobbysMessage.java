package src.main.java.com.messages;

import java.util.ArrayList;
import java.util.List;

import src.main.java.logic.ILobbyInformation;

/**
 * Contains infromations about lobbies for player in the lobby selection screen
 * 
 * @author Denis
 *
 */
public class SendLobbysMessage extends RaceTrackMessage implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4639821754374612813L;
	private List<ILobbyInformation> listOfLobbyInformation = new ArrayList<ILobbyInformation>();
	
	public SendLobbysMessage(List<ILobbyInformation> listOfLobbyInformation){
		super();
		this.listOfLobbyInformation = listOfLobbyInformation;
	}
	
	public List<ILobbyInformation> getListOfLobby(){
		return listOfLobbyInformation;
	}
	
	
}
