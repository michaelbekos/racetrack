package src.main.java.com.messages.handler.server;

import java.util.List;

import src.main.java.com.messages.OkayMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.SendOptionsMessage;
import src.main.java.com.messages.StartGameMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.Administration;
import src.main.java.logic.ILobbyInformation;

/**
 * This class handles the SendOptionsMessage by a client, when he changes something in a lobby
 * 
 * @author Denis
 *
 */
public class SendOptionsServerHandler extends RaceTrackMessageServerHandler{


	public SendOptionsServerHandler(Administration administration){
		super( administration );
	}

	/**
	 * Returns an answer for all clients in the same session about the change from one client in the lobby
	 */
	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(RaceTrackMessage messageToHandle){

		DebugOutputHandler.printDebug("Received a SendOptionsMessage");
		

		RaceTrackMessage answer = null;

		try{
			answer = new OkayMessage(null, null);

			SendOptionsMessage incomingMessage = (SendOptionsMessage) messageToHandle;

			int playerID = -1;
			List<Integer> clientsInSameSession  = null;

			if(messageToHandle.getReceiverIds() != null)
				playerID =  messageToHandle.getReceiverIds().get(0);
			answer.addClientID(playerID);

			if(administration.getPlayerIdsInSameSession(playerID) != null)
				clientsInSameSession = administration.getPlayerIdsInSameSession(playerID);

			//get the change from one of the clients in the lobby
			ILobbyInformation  lobbyChange = null;
			if(incomingMessage.getNewLobbyOptions() != null){
				lobbyChange = incomingMessage.getNewLobbyOptions();

				//try to set the new parameters
				administration.setLobbyParametersByPlayerID(playerID,lobbyChange);
				DebugOutputHandler.printDebug("Set the new Lobbyparameters by player "+playerID);
				for(int i = 0; i < lobbyChange.getParticipating().length ; i ++){
					DebugOutputHandler.printDebug("Player "+i+" is participating: "+lobbyChange.getParticipating()[i]+" ");
					
				}
				//if all players are ready in a lobby, the game starts by itself
				//answer depends on the game whether its running or not after changing lobbyinformation

				if(administration.didGameStartById(playerID)){

					int nextPlayer = administration.getNextPlayerToMove(playerID);
					
					ILobbyInformation lobbyToStart = administration.getLobbyInformation(playerID);
					answer = new StartGameMessage(lobbyToStart, nextPlayer);
					DebugOutputHandler.printDebug("The Game started!");
						

				}else{

					ILobbyInformation newLobbyInformation = administration.getLobbyInformation(playerID);

					answer = new SendOptionsMessage(newLobbyInformation);		

					DebugOutputHandler.printDebug("Generated the Lobbyinformation. Sending answer to all clients in the same session!");
							
				}

				for(Integer c : clientsInSameSession)
					answer.addClientID(c);
			}
		}catch(ClassCastException e){
			e.printStackTrace();
		}

		return answer;
	}
}
