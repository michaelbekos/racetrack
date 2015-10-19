package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.InvalidMoveMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;

/**
 *	The Receive InvalidMoveMessage handler
 */
public class ReceiveInvalidMoveMessageClientHandler extends RaceTrackMessageClientHandler {

	public ReceiveInvalidMoveMessageClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}
	
	/**
	 * Informs the UI that the last move was invalid
	 */
	@Override
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveInvalidMoveMessageClientHandler: "
				+ "InvalidMoveMessage now in Progress");
		try{
			@SuppressWarnings("unused")
			InvalidMoveMessage message = (InvalidMoveMessage) messageToHandle;
			
			RaceTrackMessageClientHandler.sceneBase.receivedInvalidMoveMessage();
		}catch(Exception e){
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveInvalidMoveMessageClientHandler: "
					+ "InvalidMoveMessage caused a fail on evaluation");
		}
	}	
}
