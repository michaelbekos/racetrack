package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.SendOptionsMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;

/**
 *	The Receive Options Handler
 */
public class ReceiveOptionsClientHandler extends RaceTrackMessageClientHandler{

	public ReceiveOptionsClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}

	@Override
	/**
	 * Updates Model + informs UI Lobby Options got changed
	 */
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveOptionsClientHandler: "
				+ "SendOptionsMessage now in Progress");
		try{
			SendOptionsMessage message = (SendOptionsMessage) messageToHandle;
			ILobbyInformation lobby = message.getNewLobbyOptions();
			
			// update the lobby
			ModelExchange.LobbyList.setSpecificLobby(lobby);
			
			if(lobby != null)
				lobby.printLobby();
			
			// Join the send Lobby, if the client wanted to join one.
			if(ModelExchange.LobbyList.getRequestedLobbyToJoinID() != null){
				ModelExchange.LobbyList.setCurrentLobbyId(lobby.getLobbyID());
				ModelExchange.LobbyList.setRequestedLobbyToJoinID(null);
			}
			
			RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		} catch(Exception e) {
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveOptionsClientHandler: "
					+ "SendOptionsMessage caused a fail on evaluation");
		}
	}
}
