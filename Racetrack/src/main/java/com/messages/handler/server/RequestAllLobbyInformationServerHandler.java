package src.main.java.com.messages.handler.server;

import java.util.List;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.SendLobbysMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.Administration;
import src.main.java.logic.ILobbyInformation;

/**
 * Handler for when a client asks for an update for all opened lobbies
 * @author Denis
 *
 */
public class RequestAllLobbyInformationServerHandler extends RaceTrackMessageServerHandler{

	public RequestAllLobbyInformationServerHandler(Administration administration) {
		super(administration);
	}

	/**
	 * Returns a RequestLobbysMessage which has a List of all opened Lobbies.
	 * 
	 * @param messageToHandle incoming message, asking for an update
	 * @return RaceTrackMessage holding the list of the lobbyinformation
	 */
	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(
			RaceTrackMessage messageToHandle) {

		DebugOutputHandler.printDebug("Received a RequestAllLobbyInformationMessage.");
		

		//no answer, if anything goes wrong generating the answer
		RaceTrackMessage answer = null;

		//get the list of all opened Lobbie0s
		List<ILobbyInformation> lobbyInformationList = null;
		if(administration.getLobbyInformationList() != null){
			lobbyInformationList = administration.getLobbyInformationList();
		}
		//generate SendLobbysMessage, only containing the list of lobby information
		answer = new SendLobbysMessage(lobbyInformationList);
		
		//address this answer to the client, who sent it
		if(messageToHandle.getReceiverIds() != null)
			answer.addClientID(messageToHandle.getReceiverIds().get(0));
		else
			answer = null;
		
		DebugOutputHandler.printDebug("Player with ID "+messageToHandle.getReceiverIds().get(0)+" requested the List of Lobbies");

		DebugOutputHandler.printDebug("Generated succesfully a List of all LobbyInformation");
		

		return answer;
	}

}
