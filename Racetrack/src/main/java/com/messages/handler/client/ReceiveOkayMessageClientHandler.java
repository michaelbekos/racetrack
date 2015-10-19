package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.OkayMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ModelExchange;

/**
 *	The Receive Okay handler
 */
public class ReceiveOkayMessageClientHandler extends RaceTrackMessageClientHandler {

	public ReceiveOkayMessageClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}
	
	/**
	 * Informs the UI that the server received the message, also sets ID and Name of the client
	 * on the first Connection.
	 */
	@Override
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveOkayMessageClientHandler: "
				+ "OkayMessage now in Progress");
		try{
			OkayMessage message = (OkayMessage) messageToHandle;
			
			// sets the playerID only on first Connection
			if(message.getPlayerID() != null)
				ModelExchange.GameOptions.setPlayerID(message.getPlayerID());
			
			// sets the playerName only on first Connection
			if(message.getPlayerName() != null)
				ModelExchange.GameOptions.setUserName(message.getPlayerName());
			
			RaceTrackMessageClientHandler.sceneBase.receivedOkayMessage();
		}catch(Exception e){
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveOkayMessageClientHandler: "
					+ "OkayMessage caused a fail on evaluation");
		}
	}	
}
