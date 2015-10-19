package src.main.java.com.messages.handler.server;

import java.util.List;

import src.main.java.com.messages.DenyLobbyEntryMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.RequestLobbyEntryMessage;
import src.main.java.com.messages.SendOptionsMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.Administration;
import src.main.java.logic.ILobbyInformation;

/**
 * This Class asks the Administration, if a client could enter a Lobby or not,
 * if the lobby has free space, the client is put into the lobby 
 * @author Denis
 *
 */
public class RequestLobbyEntryServerHandler extends RaceTrackMessageServerHandler{


	public RequestLobbyEntryServerHandler(Administration administration){
		super( administration );
	}

	/**
	 * Checks in administration whether a specific lobby has free space for one more client
	 * if it has, let the client join, and send him and every other player in the session an update 
	 * of the LobbyInformation
	 */
	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(RaceTrackMessage messageToHandle){

		DebugOutputHandler.printDebug("Received a RequestLobbyEntryMessage.");
		

		RaceTrackMessage answer = null;

		try{

			RequestLobbyEntryMessage incomingMessage = (RequestLobbyEntryMessage) messageToHandle;
			int clientID = -1;
			List<Integer> clientsInSession = null;

			if(incomingMessage.getReceiverIds() != null)
				clientID = incomingMessage.getReceiverIds().get(0);

			int lobbyToEnter = incomingMessage.getLobbyIdToEnter();
			System.out.println("Lobby to Enter by ClientID: "+incomingMessage.getReceiverIds().get(0)+ " to lobby: "+lobbyToEnter);
			//ask administration for free space
			
			if(administration.hasLobbyFreeSpace(lobbyToEnter) && !administration.didGameStartSession(lobbyToEnter)){
				DebugOutputHandler.printDebug("Lobby "+ lobbyToEnter +" had a free Slot for the player.");
				

				administration.joinLobby(clientID, lobbyToEnter);

				if(administration.getPlayerIdsInSameSession(clientID) != null)
					clientsInSession = administration.getPlayerIdsInSameSession(clientID);
				DebugOutputHandler.printDebug("Sending new LobbyOptions to Players :");
				for(Integer i : clientsInSession){
					DebugOutputHandler.printDebug(i +"  ");
				}
						
				ILobbyInformation lobbyInformation= null;
				if(administration.getLobbyInformationBySession(lobbyToEnter) != null)
					lobbyInformation = administration.getLobbyInformationBySession(lobbyToEnter);
				else
					DebugOutputHandler.printDebug("LobbyInformation is null. Should not happen");
				
				//if he can enter, send lobbyinformation and tell every player in that lobby of the new player
				answer = new SendOptionsMessage(lobbyInformation);

				if(clientsInSession != null){
					for(Integer i : clientsInSession)
						answer.addClientID(i);
				}
				
				DebugOutputHandler.printDebug("Client "+clientID+"  has been added to the Lobby. All clients in the lobby"+ lobbyToEnter+"  get a notification.");
				

			}else{
				DebugOutputHandler.printDebug("Lobby "+ lobbyToEnter+" had no free Slot left for Player "+clientID);
				
				answer = new DenyLobbyEntryMessage();
				answer.addClientID(clientID);
				
			}


		}catch( ClassCastException e){
			e.printStackTrace();
		}

		return answer;
	}
}
