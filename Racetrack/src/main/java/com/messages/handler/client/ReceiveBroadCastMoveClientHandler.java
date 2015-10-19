package src.main.java.com.messages.handler.client;

import javafx.geometry.Point2D;
import src.main.java.com.messages.BroadCastMoveMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.Game;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.Player;

/**
 * The Receive BroadCastMove Handler
 */
public class ReceiveBroadCastMoveClientHandler extends
		RaceTrackMessageClientHandler {

	public ReceiveBroadCastMoveClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}

	@Override
	/**
	 * Updates the Model + informs the UI that the game got updated with 
	 * a move of any kind
	 */
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker.printInDebugMode("ReceiveBroadCastMoveClientHandler: "
				+ "BroadCastMoveMessage now in Progress");
		try {
			BroadCastMoveMessage message = (BroadCastMoveMessage) messageToHandle;
			ILobbyInformation lobby = ModelExchange.LobbyList.getCurrentLobby();

			ModelExchange.CurrentGame.setCurrentPlayerTimeForMove(-Integer.MAX_VALUE);
			
			// Either initialize a new game
			if (message.getPlayerInGroup() == -1) {
				if (lobby != null) {
					
					// Create the game
					Game game = new Game(lobby);

					// Get Last player (he will start)
					int n = 0;
					for (int i = 0; i < lobby.getPlayerIDs().length; i++)
						if (lobby.getPlayerIDs()[i] != null)
							n = i;

					// Set Player and Round
					Player[] players = new Player[ModelExchange.LobbyList
							.getCurrentLobby().getPlayerIDs().length];
					game.setCurrentPlayerIndex(n);
					for (int i = 0; i < players.length; i++) {
						players[i] = new Player(lobby.getPlayerIDs()[i],
								lobby.getPlayerNames()[i]);
						players[i].setCurrentVelocity(null);
						players[i].setCurrentPosition(null);
					}

					// Set Players
					ModelExchange.CurrentGame.getGame().setPlayerList(players);

					// Initialize the game
					ModelExchange.CurrentGame.setGame(game);
					ModelExchange.CurrentGame.gameIsSet.set(true);
				}
			} 
			// or accept the move
			else {
				// Get the Game
				Game clientGame = null;
				if (ModelExchange.CurrentGame.getGame() != null)
					clientGame = ModelExchange.CurrentGame.getGame();
				else
					Racetracker
							.printInDebugMode("ReceiveBroadCastMoveClientHandler: "
									+ "BroadCastMoveMessage caused a fail because no game does exist");

				// get the CurrentPlayer on the Client
				Player currentPlayer = null;
				if (clientGame.getPlayerList()[message.getPlayerInGroup()] != null)
					currentPlayer = clientGame.getPlayerList()[message
							.getPlayerInGroup()];
				else
					Racetracker
							.printInDebugMode("ReceiveBroadCastMoveClientHandler: "
									+ "BroadCastMoveMessage caused a fail because the player does not exist");

				// Set Velocity
				if(message.getVelocity() != null)
					currentPlayer.setCurrentVelocity(message.getVelocity());

				// Set connection status
				currentPlayer.setDisconnected(message.isDisconnected());
				
				// Check for crash and add memorized grid points
				boolean crashed = message.getIsColliding();
				if (crashed) {
					// If crashed the colossion point is set as memorized grid point
					currentPlayer.setHasCrashed(true);
					currentPlayer.addMemorizeGridPoint(message.getCollisionPoint());
				} else {
					// Add the moveVector if the player hasn't crashed
					Point2D gridPoint = null;
					if(message.getVector() != null)
						gridPoint = message.getVector();
					
					if(gridPoint != null) {
						currentPlayer.addMemorizeGridPoint(gridPoint);
						currentPlayer.setCurrentPosition(gridPoint);
					}
				}
				

				// Change Round
				clientGame.setRoundCounter(message.getRound());

				// Set the updates for the current Player
				Player[] players = null;
				if(clientGame.getPlayerList() != null)
					players = clientGame.getPlayerList();
				if(currentPlayer != null)
					players[message.getPlayerInGroup()] = currentPlayer;
				if(players != null)
					clientGame.setPlayerList(players);

				// Update the Player to new Current Player
				clientGame.setCurrentPlayerIndex(message.getNextPlayer());
			}

			RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		} catch (Exception e) {
			e.printStackTrace();
			Racetracker.printInDebugMode("ReceiveBroadCastMoveClientHandler: "
					+ "BroadCastMoveMessage caused a fail on evaluation");
		}
	}
}