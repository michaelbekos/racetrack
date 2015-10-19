package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;

/**
 *	The Receive Chat Handler
 */
public class ReceiveChatMessageClientHandler extends RaceTrackMessageClientHandler {

	public ReceiveChatMessageClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}
	
	/**
	 * Informs the UI about a chat message that got received
	 */
	@Override
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveChatMessageClientHandler: "
				+ "ChatMessage now in Progress");
		try{
			// Add the message to the model
			
			//ModelExchange.
			//RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		}catch(Exception e){
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveChatMessageClientHandler: "
					+ "ChatMessage caused a fail on evaluation");
		}
	}	
}
