package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.DenyLobbyEntryMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ModelExchange;

/**
 *	The Receive Disconnect handler
 */
public class ReceiveDenyLobbyEntryMessageClientHandler extends RaceTrackMessageClientHandler {

	public ReceiveDenyLobbyEntryMessageClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}
	
	/**
	 * Informs the UI that the requested lobby could not be joined
	 */
	@Override
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveDenyLobbyEntryMessageClientHandler: "
				+ "DenyLobbyEntryMessage now in Progress");
		try{			
			@SuppressWarnings("unused")
			DenyLobbyEntryMessage message = (DenyLobbyEntryMessage) messageToHandle;

			ModelExchange.LobbyList.setRequestedLobbyToJoinID(null);
			
			RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		}catch(Exception e){
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveDenyLobbyEntryMessageClientHandler: "
					+ "DenyLobbyEntryMessage caused a fail on evaluation");
		}
	}	
}