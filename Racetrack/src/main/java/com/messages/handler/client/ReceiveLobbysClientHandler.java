package src.main.java.com.messages.handler.client;

import java.util.List;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.SendLobbysMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;

/**
 *	The Receive Lobbies Handler
 */
public class ReceiveLobbysClientHandler extends RaceTrackMessageClientHandler{
	
	public ReceiveLobbysClientHandler(MultiSceneBase sceneBase){
		super(sceneBase);
	}
	
	@Override
	/**
	 * Updates Model + informs UI about all the lobbies
	 */
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveLobbysClientHandler: "
				+ "SendLobbysMessage now in Progress");
		try{
			SendLobbysMessage message = (SendLobbysMessage) messageToHandle;
			List<ILobbyInformation> listOfLobbyInformation = message.getListOfLobby();
			ModelExchange.LobbyList.setLobbyList(listOfLobbyInformation.toArray(new ILobbyInformation[listOfLobbyInformation.size()]));
			
			// Join the last send Lobby - this will Only happen, if the client wanted to create a lobby and then joins it automatically
			if(ModelExchange.LobbyList.getRequestedLobbyToJoinID() != null){
				ModelExchange.LobbyList.setCurrentLobbyId(listOfLobbyInformation.get(listOfLobbyInformation.size()-1).getLobbyID());
				ModelExchange.LobbyList.setRequestedLobbyToJoinID(null);
			}
			
			RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		}catch(Exception e){
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveLobbysClientHandler: "
					+ "SendLobbysMessage caused a fail on evaluation");
		}
	}
}
