package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.TimeLeftMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ModelExchange;

/**
 *	The Time left Handler
 */
public class ReceiveTimeLeftMessageClientHandler extends RaceTrackMessageClientHandler{
	
	public ReceiveTimeLeftMessageClientHandler(MultiSceneBase sceneBase){
		super(sceneBase);
	}
	
	@Override
	/**
	 * Updates Model + informs UI about the left time for a move
	 */
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveTimeLeftMessageClientHandler: "
				+ "TimeLeftMessage now in Progress");
		try{
			TimeLeftMessage message = (TimeLeftMessage) messageToHandle;

			ModelExchange.CurrentGame.setCurrentPlayerTimeForMove(message.getTimeLeftToMove());
			
			//RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		}catch(Exception e){
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveTimeLeftMessageClientHandler: "
					+ "TimeLeftMessage caused a fail on evaluation");
		}
	}
}