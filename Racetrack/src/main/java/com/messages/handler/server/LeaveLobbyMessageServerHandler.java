package src.main.java.com.messages.handler.server;

import java.util.List;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.SendOptionsMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.Administration;
import src.main.java.logic.ILobbyInformation;

/**
 * This handler reacts to a leaveLobbymessage by the client
 * it removes the client from his old lobby and deletes the lobby if it is empty
 * @author denis
 *
 */
public class LeaveLobbyMessageServerHandler extends RaceTrackMessageServerHandler{

	public LeaveLobbyMessageServerHandler(Administration administration) {
		super(administration);
	}

	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(
			RaceTrackMessage messageToHandle) {

		RaceTrackMessage answer =null;

		if(messageToHandle.getReceiverIds().get(0) != null){

			int playerID = messageToHandle.getReceiverIds().get(0);
			
			List<Integer> playersInSameSession = administration.getPlayerIdsInSameSession(playerID);
			
			int oldSessionFromPlayer = administration.getSessionFromPlayer(playerID);

			DebugOutputHandler.printDebug("Player "+messageToHandle.getReceiverIds().get(0)+" wants to leave his lobby "+oldSessionFromPlayer);
			
			administration.leaveLobby(messageToHandle.getReceiverIds().get(0));

			ILobbyInformation newLobby = administration.getLobbyInformationBySession(oldSessionFromPlayer);
			
			answer = new SendOptionsMessage(newLobby);
			
			if(messageToHandle.getReceiverIds().get(0) != null){
				for(Integer i : playersInSameSession)
					answer.addClientID(i);
			}

		}
		return answer;
	}

}
