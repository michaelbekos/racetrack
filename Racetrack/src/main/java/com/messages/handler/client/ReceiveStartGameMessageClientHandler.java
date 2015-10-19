package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.StartGameMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.Game;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.Player;

/**
 * The Receive StartGame Handler
 */
public class ReceiveStartGameMessageClientHandler extends
		RaceTrackMessageClientHandler {

	public ReceiveStartGameMessageClientHandler(MultiSceneBase sceneBase) {
		super(sceneBase);
	}

	/**
	 * Informs the UI that the game started
	 */
	@Override
	public void updateModel(RaceTrackMessage messageToHandle) {
		Racetracker
				.printInDebugMode("ReceiveStartGameMessageClientHandler: "
						+ "StartGameMessag now in Progress");
		try {
			StartGameMessage message = (StartGameMessage) messageToHandle;

			ILobbyInformation newLobby = null;
			if (message.getLobbyInformation() != null)
				newLobby = message.getLobbyInformation();
			else
				Racetracker
						.printInDebugMode("ReceiveStartGameMessageClientHandler: "
								+ "StartGameMessag caused a fail because it contained an empty lobby");

			ModelExchange.LobbyList.setSpecificLobby(newLobby);
			ModelExchange.LobbyList.setRequestedLobbyToJoinID(null);
			
			// Initialize a new game
			ILobbyInformation lobby = ModelExchange.LobbyList.getCurrentLobby();
			if (lobby != null) {

				// Create the game
				Game game = new Game(lobby);

				// Get Last player (he will start)
				int n = 0;
				for (int i = 0; i < lobby.getPlayerIDs().length; i++)
					if (lobby.getPlayerIDs()[i] != null)
						n = i;

				// Set Player and Round
				Player[] players = new Player[ModelExchange.LobbyList.getCurrentLobby().getPlayerIDs().length];
				game.setCurrentPlayerIndex(n);
				for (int i = 0; i < players.length; i++) {
					players[i] = new Player(lobby.getPlayerIDs()[i],
							lobby.getPlayerNames()[i]);
					players[i].setCurrentVelocity(null);
					players[i].setCurrentPosition(null);
				}

				// Initialize the game
				ModelExchange.CurrentGame.setGame(game);
				// Set Players
				if(players != null && game != null)
					ModelExchange.CurrentGame.getGame().setPlayerList(players);
				ModelExchange.CurrentGame.gameIsSet.set(true);

			} else {
				Racetracker
						.printInDebugMode("ReceiveStartGameMessageClientHandler: "
								+ "StartGameMessag caused a fail on initializing the game");
			}

			RaceTrackMessageClientHandler.sceneBase.receivedUpdateFromServer();
		} catch (Exception e) {
			e.printStackTrace();
			Racetracker
					.printInDebugMode("ReceiveStartGameMessageClientHandler: "
							+ "StartGameMessag caused a fail on evaluation");
		}
	}
}
