package src.main.java.com.messages.handler.server;

import java.util.List;

import src.main.java.com.messages.CreateLobbyMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.SendLobbysMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.Administration;
import src.main.java.logic.ILobbyInformation;

/**
 * Tells the administration to create a new Lobby and make the client, who opened up the lobby join it immediately
 * 
 * @author Denis
 *
 */
public class CreateLobbyMessageServerHandler  extends RaceTrackMessageServerHandler{

	public CreateLobbyMessageServerHandler(Administration administration) {
		super(administration);
	}


	/**
	 *  Reads the incoming messages client ID and gets the new LobbyInformation.
	 *  This method tells the administration, which Lobby should be created and by whom.
	 *  @param meesageToHandle incoming message by the client
	 */
	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(
			RaceTrackMessage messageToHandle) {

		DebugOutputHandler.printDebug("Received a CreateLobbyMessage");
		
		RaceTrackMessage answer = null;

		try{

			//fetch the message and cast it to the type, this handler needs
			CreateLobbyMessage incomingMessage = (CreateLobbyMessage) messageToHandle;

			int clientID = -1;
			if(incomingMessage.getReceiverIds()!= null)
				clientID = incomingMessage.getReceiverIds().get(0);

			//			answer.addClientID(clientID);

			//how the client configured the lobby to create
			ILobbyInformation newLobby = null;
			if(incomingMessage.getLobbyInformaion() != null){
				newLobby = incomingMessage.getLobbyInformaion();
			}
			//newLobby.setAmountOfAIs(1);
			
			DebugOutputHandler.printDebug("Player with ID: "+clientID+" wants to create a Lobby");
			

			//tell the administration about the new lobby
			if(!(newLobby == null || clientID == -1)){

				administration.createLobbyByPlayerID(clientID, newLobby);

				List<ILobbyInformation> lobbyInformationList = null;
				if(administration.getLobbyInformationList() != null){
					lobbyInformationList = administration.getLobbyInformationList();
					
					DebugOutputHandler.printDebug("Lobby has been created succesfully.");
//					ILobbyInformation newLob = lobbyInformationList.get(lobbyInformationList.size() - 1);
//					DebugOutputHandler.printDebug(newLob.getLobbyName()+" with ID: "+ newLob.getLobbyID()+ " Track: "+newLob.getTrackId());
					

					//generate SendLobbysMessage, only containing the list of lobby information
					answer = new SendLobbysMessage(lobbyInformationList);

					//address this answer to the client, who sent it
					if(messageToHandle.getReceiverIds() != null)
						answer.addClientID(clientID);
					else
						answer = null;
				}

			}

		}catch(ClassCastException e){
			//will be deleted later on 
			e.printStackTrace();
		}

		//no player receives an update about the new lobby till they refresh the lobby list
		return answer;
	}

}
