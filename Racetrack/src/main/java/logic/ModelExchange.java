package src.main.java.logic;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import src.main.java.core.ControllerClient;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;

/**
 * This class will be used to exchange model states between the
 * server-client component and the gui.
 * The server-client will inform the gui over changes in the model
 * leading to changes in the gui.
 * @author Tobias
 *
 */
public class ModelExchange {

	public static SimpleStringProperty currentSceneName = new SimpleStringProperty("");

	private static ControllerClient controller;

	/**
	 * Initialization
	 */
	public ModelExchange() {
	}

	public static ControllerClient getController() {
		if (controller == null) {
			controller = new ControllerClient();
		} 
		return controller;
	}
	
	public static void resetController() {
		controller = new ControllerClient();
	}

	public static void setControllerRefference(MultiSceneBase currentScene) {
		controller.setInterface(currentScene);
	}

	/**
	 * Game options stores general and mandatory values;
	 * - host ip adress
	 * - host port
	 * - user name
	 * - user id
	 * @author Tobias
	 *
	 */
	public static class GameOptions {
		private static String hostIp;
		private static int hostPort;
		private static String userName;
		private static int playerID;
		private static boolean shouldReturnToSetup;
		
		/*
		 * TODO: Remove defaults
		 */
		public static String getHostIp() {
			if (hostIp == null) {
				hostIp = "127.0.0.1";
			}
			return hostIp;
		}
		public static void setHostIp(String hostIp) {
			GameOptions.hostIp = hostIp;
		}

		public static int getHostPort() {
			if (hostPort == 0) {
				hostPort = 55570;
			}
			return hostPort;
		}
		public static void setHostPort(int hostPort) {
			GameOptions.hostPort = hostPort;
		}

		public static String getUserName() {
			if (userName == null) {
				userName = "Player";
			}
			return userName;
		}
		
		public static void setUserName(String userName) {
			GameOptions.userName = userName;
		}
		
		public static int getPlayerID() {
			return playerID;
		}
		
		public static void setPlayerID(int playerID) {
			GameOptions.playerID = playerID;
		}
		
		public static boolean isShouldReturnToSetup() {
			return shouldReturnToSetup;
		}
		
		public static void setShouldReturnToSetup(boolean shouldReturnToSetup) {
			GameOptions.shouldReturnToSetup = shouldReturnToSetup;
		}
	}

	/**
	 * Lobby lists represents all lobbies currently available (to join).
	 * 
	 * @author Tobias
	 *
	 */
	public static class LobbyList {

		private static ILobbyInformation[] lobbiesInformation;

		/**
		 * Observable lobbyId property
		 */
		public static SimpleIntegerProperty lobbyId = new SimpleIntegerProperty(-1);

		/**
		 * Is an Integer if a lobbyRequest exists or got confirmed
		 * Is Null if there is no lobbyRequest or if the lobbyRequest got declined
		 */
		private static Integer requestedLobbyToJoinID;

		public static ILobbyInformation[] getLobbyInformationList() {
			return lobbiesInformation;
		}

		public static void setLobbyList(ILobbyInformation[] lobbyList) {
			LobbyList.lobbiesInformation = lobbyList;
		}

		public static void setRequestedLobbyToJoinID(Integer requestedLobbyToJoinID){
			LobbyList.requestedLobbyToJoinID = requestedLobbyToJoinID;
		}

		public static Integer getRequestedLobbyToJoinID() {
			return requestedLobbyToJoinID;
		}

		public static int getCurrentLobbyId() {
			if (lobbyId != null) {
				return lobbyId.get();
			} else {
				return -1;
			}
			
		}

		public static void setCurrentLobbyId(int currentLobbyId) {
			Racetracker.printInDebugMode("Current lobbyId : " + currentLobbyId);
			lobbyId.set(currentLobbyId);
		}

		/**
		 * Updates a lobby on the client with a new lobby which the server sent
		 * @param lobby The lobby which gets updated. Through the lobbyID.
		 */
		public static void setSpecificLobby(ILobbyInformation lobby) {
			int lobbyID;
			if(lobby == null)
				lobbyID = -1;
			else
				lobbyID = lobby.getLobbyID();
			for(int i  = 0; i < lobbiesInformation.length; i++){
				if(lobbiesInformation[i].getLobbyID() == lobbyID){
					lobbiesInformation[i] = lobby;
					break;
				}
			}
			lobbyId.set(lobbyId.get());
		}
		
		public static ILobbyInformation getCurrentLobby() {
			ILobbyInformation[] lobbies = LobbyList.getLobbyInformationList();
			int currentLobbyId = LobbyList.getCurrentLobbyId();
			for (int i = 0; i < lobbies.length; i++) {
				Racetracker.printInDebugMode(lobbies[i].getLobbyID() + " : " + currentLobbyId);
				if (lobbies[i].getLobbyID() == currentLobbyId) {
					Racetracker.printInDebugMode("Current lobby: " + currentLobbyId);
					return lobbies[i];
				}
			}
			return null;
		}
	}

	/**
	 * Current game will be used for any changes during a running game.
	 * 
	 * @author Tobias
	 *
	 */
	public static class CurrentGame {

		/**
		 * Observable gameIsSet property
		 */
		public static SimpleBooleanProperty gameIsSet = new SimpleBooleanProperty(false);
		
		/**
		 * Observable property
		 * If player is running out of time and only have X minutes
		 */
		public static SimpleIntegerProperty currentPlayerTimeForMove = new SimpleIntegerProperty(-1);

		private static Game game;

		public static Game getGame() {
			return game;
		}

		public static void setGame(Game game) {
			CurrentGame.game = game;

			/*
			if (game == null) {
				gameIsSet.set(false);
			} else {
				gameIsSet.set(true);
			}
			*/
		}
		
		public static int getCurrentPlayerTimeForMove() {
			return currentPlayerTimeForMove.get();
		}

		public static void setCurrentPlayerTimeForMove(int time) {
			CurrentGame.currentPlayerTimeForMove.set(time);
		}
		
	}
}
