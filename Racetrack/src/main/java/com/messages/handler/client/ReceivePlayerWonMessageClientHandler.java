package src.main.java.com.messages.handler.client;

import javafx.geometry.Point2D;
import src.main.java.com.messages.PlayerWonMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.Game;
import src.main.java.logic.ModelExchange;

/**
 * The Receive PlayerWon Handler
 */
public class ReceivePlayerWonMessageClientHandler extends RaceTrackMessageClientHandler{

	public ReceivePlayerWonMessageClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}

	/**
	 * Updates Model + informs UI that a player did win or all players lost
	 */
	@Override
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceivePlayerWonMessageClientHandler: "
				+ "PlayerWonMessage now in Progress");
		try{
			PlayerWonMessage message = (PlayerWonMessage) messageToHandle;
			
			// Get the Game
			Game clientGame = null;
			if(ModelExchange.CurrentGame.getGame() != null)
				clientGame = ModelExchange.CurrentGame.getGame();
			else
				Racetracker.printInDebugMode("ReceivePlayerWonMessageClientHandler: "
						+ "PlayerWonMessage caused a fail because no game does exist");
			
			// Add the moveVector
			Point2D gridPoint = message.getLastPlayerMove();
			int lastGamePlayer = message.getPlayerWhoMovedLastByPosition();
			clientGame.getPlayers()[lastGamePlayer].addMemorizeGridPoint(gridPoint);
			
			// Player who won (will also work, if no player won, because then PlayerWhoWon will stay null in ModelExchange
			if(message.getPlayerWhoWonByPosition() != null)
				if(clientGame.getPlayerList()[message.getPlayerWhoWonByPosition()] != null)
					clientGame.setCurrentPlayerIndex(message.getPlayerWhoWonByPosition());
			
			// End Game
			ModelExchange.CurrentGame.setCurrentPlayerTimeForMove(-Integer.MAX_VALUE);
			ModelExchange.CurrentGame.gameIsSet.set(false);			
			
			RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		} catch(Exception e) {
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceivePlayerWonMessageClientHandler: "
					+ "PlayerWonMessage caused a fail on evaluation");
		}
	}
}