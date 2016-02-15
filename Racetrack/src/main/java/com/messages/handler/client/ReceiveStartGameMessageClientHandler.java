package src.main.java.com.messages.handler.client;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.StartGameMessage;
import src.main.java.com.messages.handler.RaceTrackMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.AI_Bipartite;
import src.main.java.logic.AI_Crasher;
import src.main.java.logic.AI_LimitedView_DriveSafe;
import src.main.java.logic.AI_LimitedView_FastCorner;
import src.main.java.logic.AI_LimitedView_FastTopmost;
import src.main.java.logic.AI_LimitedView_SpeedUp;
import src.main.java.logic.AI_NoMover;
import src.main.java.logic.AI_Puckie;
import src.main.java.logic.AI_Random;
import src.main.java.logic.AI_Zigzag;
import src.main.java.logic.Game;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.Player;
import src.main.java.logic.AIstar.AIstar;

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
				for (int i = 0; i < players.length; i++) 
				{
					switch( lobby.getTypeIDs()[i] )
					{
					case 1:
						players[i]= new Player( lobby.getPlayerIDs()[i], lobby.getPlayerNames()[i] );
						break;
					case 2:
						players[i]= new AI_NoMover( lobby.getPlayerIDs()[i], "AI No Mover " + lobby.getPlayerNames()[i] );
						break;
					case 3:
						players[i]= new AI_Random( lobby.getPlayerIDs()[i], "AI Random " + lobby.getPlayerNames()[i] );
						break;
					case 4:
						players[i]= new AI_Puckie( lobby.getPlayerIDs()[i], "AI Puckie " + lobby.getPlayerNames()[i] );
						break;
					case 5:
						players[i]= new AIstar( lobby.getPlayerIDs()[i], "AIstar " + lobby.getPlayerNames()[i] );
						break;
					case 6:
						players[i]= new AI_Crasher( lobby.getPlayerIDs()[i], "AI Crasher " + lobby.getPlayerNames()[i] );
						break;
					case 7:
						players[i]= new AI_Zigzag( lobby.getPlayerIDs()[i], "AI Zigzag " + lobby.getPlayerNames()[i] );
						break;
					case 8:
						players[i]= new AI_LimitedView_DriveSafe( lobby.getPlayerIDs()[i], "AI LimitedView DriveSafe " + lobby.getPlayerNames()[i] );
						break;
					case 9:
						players[i]= new AI_LimitedView_FastCorner( lobby.getPlayerIDs()[i], "AI LimitedView FastCorner " + lobby.getPlayerNames()[i] );
						break;
					case 10:
						players[i]= new AI_LimitedView_FastTopmost( lobby.getPlayerIDs()[i], "AI LimitedView FastTopmost " + lobby.getPlayerNames()[i] );
						break;
					case 11:
						players[i]= new AI_LimitedView_SpeedUp( lobby.getPlayerIDs()[i], "AI LimitedView SpeedUp " + lobby.getPlayerNames()[i] );
						break;
					case 12:
						players[i]= new AI_Bipartite( lobby.getPlayerIDs()[i], "AI Bipartite " + lobby.getPlayerNames()[i] );
						break;
						/* ADD YOUR NEW AI ALSO HERE */
					default:
						return;
					}
					players[i].setCurrentVelocity( null );
					players[i].setCurrentPosition( null );
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
