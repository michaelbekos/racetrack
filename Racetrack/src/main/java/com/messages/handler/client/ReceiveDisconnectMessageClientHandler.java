package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.DisconnectMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ModelExchange;

/**
 *	The Receive Disconnect handler
 */
public class ReceiveDisconnectMessageClientHandler extends RaceTrackMessageClientHandler {

	public ReceiveDisconnectMessageClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}
	
	/**
	 * Informs the UI that a player disconnected from a running game
	 */
	@Override
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveDisconnectMessageClientHandler: "
				+ "DisconnectMessage now in Progress");
		try{
			DisconnectMessage message = (DisconnectMessage) messageToHandle;

			ModelExchange.CurrentGame.getGame().setCurrentPlayerIndex(message.getNextPlayerToMove());
			ModelExchange.CurrentGame.getGame().getPlayerList()[message.getPlayerToDisconnect()].setDisconnected(true);
			
			RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		}catch(Exception e){
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveDisconnectMessageClientHandler: "
					+ "DisconnectMessage caused a fail on evaluation");
		}
	}	
}