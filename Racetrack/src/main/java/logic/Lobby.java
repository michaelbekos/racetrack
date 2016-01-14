package src.main.java.logic;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import src.main.java.core.DebugOutputHandler;

public class Lobby implements ILobby {

	/**
	 * a lobby cant be of a bigger size then 5
	 */
	private static final int MAX_LOBBY_SIZE = 5;

	/**
	 * determines the current max players allowed in a lobby
	 */
	private int maxCurrentPlayer;

	/**
	 * the id, by whom it gets called by administration
	 */
	private int lobbyID;

	/**
	 * The id of the track, which will be running in the lobby
	 */
	private int trackId;

	/**
	 * whether the game is running or not
	 */
	private boolean isGameRunning;

	/**
	 * saves the players in the Lobby
	 */
	private Player[] playerList;

	/**
	 * RacetrackGame which will be started when everyone is ready to play
	 */
	private Game game;

	private String lobbyName;

	private int playMode;

	private int lastPlayerDisconnected;
	
	private IAdministration admin;

	public Lobby(int lobbyID, IAdministration admin) {
		this.lobbyID = lobbyID;
		playerList = new Player[5];
		maxCurrentPlayer = 5;
		this.admin = admin;
	}

	public Lobby(int lobbyID, int maxCurrentPlayer, String lobbyName,
			int currentTrackId, IAdministration admin) {

		// lobby cant be of a bigger size than 5
		maxCurrentPlayer = maxCurrentPlayer > MAX_LOBBY_SIZE ? MAX_LOBBY_SIZE
				: maxCurrentPlayer;

		this.lobbyID = lobbyID;
		this.maxCurrentPlayer = maxCurrentPlayer;
		this.lobbyName = lobbyName;
		this.trackId = currentTrackId;
		this.playerList = new Player[maxCurrentPlayer];
		isGameRunning = false;
		this.admin = admin;
	}

	public Lobby(int lobbyID, int maxCurrentPlayer, String lobbyName,
			int currentTrackId,int playMode, IAdministration admin) {

		// lobby cant be of a bigger size than 5
		maxCurrentPlayer = maxCurrentPlayer > MAX_LOBBY_SIZE ? MAX_LOBBY_SIZE
				: maxCurrentPlayer;

		this.lobbyID = lobbyID;
		this.maxCurrentPlayer = maxCurrentPlayer;
		this.lobbyName = lobbyName;
		this.trackId = currentTrackId;
		this.playerList = new Player[maxCurrentPlayer];
		isGameRunning = false;
		this.playMode = playMode;
		lastPlayerDisconnected = -1;
		this.admin = admin;
	}

	/*
	 * Public methods defined in ILobby
	 */

	/**
	 * adds a player who wants to enter the lobby If the player is the first
	 * one, then he is set as host
	 * 
	 * This will check if the player is already in the lobby (client sent wrong
	 * data)
	 * 
	 * @param playerEnteringLobby
	 *            The player
	 */
	@Override
	public void addPlayer(Player playerEnteringLobby) {
		System.out.println( "ADDED PLAYER" );
		for (int i = 0; i < maxCurrentPlayer; i++) {

			// Cancel, if the player is already in the lobby
			if (playerList != null)
				if (playerList[i] != null)
					if (playerList[i].getPlayerID().equals(
							playerEnteringLobby.getPlayerID()))
						break;

			if (playerList[i] == null) {
				playerList[i] = playerEnteringLobby;
				playerEnteringLobby.setSessionID(lobbyID);
				if (i == 0) {
					playerEnteringLobby.setHost();
				}
				break;
			}
		}
	}

	/**
	 * deletes a player who wants to leave the lobby the players sessionID is
	 * set to -1; he is not in a lobby if, by accident, he was the host, the
	 * host property is given to the next one in the lobby
	 * 
	 * @param playerLeavingLobby
	 *            The player
	 */
	public void deletePlayer(Player playerLeavingLobby) {
		lastPlayerDisconnected = playerLeavingLobby.getPLAYER_ID();
		for (int i = 0; i < maxCurrentPlayer; i++) {
			if (playerList[i] == playerLeavingLobby) {
				playerLeavingLobby.setSessionID(-1);
				playerList[i] = null;
				if(game!=null){
					//hier dummy player einfuegen
					playerList[i] = new Player();
					//naechsten spieler anpassen
					setNextPlayerToPlay(i);
				}
				if (playerLeavingLobby.isHost() == true) {
					for (int j = i; j < maxCurrentPlayer; j++) {
						if (playerList[j] != null && !playerList[j].isDummyPlayer()) {
							playerList[j].setHost();
						}
					}
				}
			}
		}
		if(game!=null)
			game.setPlayerList(playerList);
	}
	
	public IAdministration getAdministration(){
		return admin;
	}

	public Player[] getPlayerList() {
		if (playerList != null)
			return playerList;
		else
			return null;
	}

	public int getLobbyID() {
		return lobbyID;
	}

	/**
	 * The round counter
	 * 
	 * @return the counter if game is running, -1 if not.
	 */
	public int getRound() {
		if (game != null)
			return game.getRoundCounter();
		else
			return -1;
	}

	/**
	 * The Velocity of a player with Index
	 * 
	 * @param indexOfThePlayerInLobby The index of the player in the lobby list of players
	 * @return the Point2D Velocity, if it exists, else null.
	 */
	public Point2D getVelocityOfPlayerByIndex(int indexOfThePlayerInLobby) {
		if (game != null)
			if (game.getPlayers() != null)
				if (game.getPlayers()[indexOfThePlayerInLobby] != null)
					if (game.getPlayers()[indexOfThePlayerInLobby]
							.getCurrentVelocity() != null)
						return game.getPlayers()[indexOfThePlayerInLobby]
								.getCurrentVelocity();

		return null;
	}

	/**
	 * The Velocity of a player with Index
	 * 
	 * @param idOfThePlayerInLobby The player id
	 * @return the Point2D Velocity, if it exists, else null.
	 */
	public Point2D getVelocityOfPlayerByID(Integer idOfThePlayerInLobby) {
		if (game.getPlayers() != null) {
			Player[] players = game.getPlayers();
			for (int i = 0; i < players.length; i++)
				if (players[i] != null)
					if (players[i].getPlayerID().equals(idOfThePlayerInLobby))
						if (players[i].getCurrentVelocity() != null)
							return players[i].getCurrentVelocity();

		}

		return null;
	}

	/**
	 * asks the game if the move from the player leads to a collision or not
	 * 
	 * @param player
	 *            The player who wants to perform a move
	 * @param velocityChange
	 *            the position he has chosen
	 * @return Whether a players velocity change will result in a colission or
	 *         not.
	 */
	public Point2D getCollisionPointFromGame(Player player,
			Point2D velocityChange) {
		if (game != null)
			return game.getCollisionPoint(player, velocityChange);
		else
			return null;
	}

	/**
	 * @return if the lobby still has some space left
	 */
	public boolean hasLobbyFreeSlot() {
		boolean freeSpace = false;
		for (int i = 0; i < maxCurrentPlayer; i++) {
			if (playerList[i] == null) {
				freeSpace = true;
				break;
			}
		}
		return freeSpace;
	}

	public Point2D makeDefaultMoveForPlayer(Player player) {
		if (player.getCurrentPosition() == null) {
			return null;
		} else {
			Point2D newPlayerPosition = player.getCurrentPosition().add(
					player.getCurrentVelocity());
			if (game != null){
				if(isCrashingIntoOtherPlayer(newPlayerPosition, player.getPLAYER_ID())){
					player.setHasCrashed(true);
					player.setParticipating(false);
					return newPlayerPosition;
				}else{
					return game.getCollisionPoint(player, newPlayerPosition);
				}
			}else
				return null;
		}
	}
	
	public Point2D makeAIMoveAt(AI player, javafx.geometry.Point2D point) {
		if (player.getCurrentPosition() == null) {
			return null;
		} else {
			Point2D speed = point.add(-1*player.getCurrentPosition().getX(),-1*player.getCurrentPosition().getY());
			Point2D newPlayerPosition = player.getCurrentPosition().add(speed);
			if (game != null){
				if(isCrashingIntoOtherPlayer(newPlayerPosition, player.getPLAYER_ID())){
					player.setHasCrashed(true);
					player.setParticipating(false);
					return newPlayerPosition;
				}else{
					return game.getCollisionPoint(player, newPlayerPosition);
				}
			}else
				return null;
		}
	}

	private boolean isCrashingIntoOtherPlayer(Point2D newPlayerPosition,int thisPlayerID){
		for(int i = 0; i < playerList.length;i++){
			if(playerList[i] != null){
				if(!playerList[i].isDummyPlayer()){
					if( playerList[i].getCurrentPosition().getX() == newPlayerPosition.getX() &&
							playerList[i].getCurrentPosition().getY() == newPlayerPosition.getY() &&
							playerList[i].getPLAYER_ID() != thisPlayerID){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * asks the game if there's already a player on the grid, the player is the
	 * current player, the player is still participating in the game and if the
	 * velocity change is valid
	 * 
	 * @param player
	 *            the player performing a move
	 * @param selectedPosition
	 *            the position he has chosen
	 * @return true if everything is valid
	 */
	public boolean isValidMoveInGame(Player player, Point2D selectedPosition) {
		if (game != null)
			return game.isValid(player, selectedPosition);
		else
			return false;
	}

	/**
	 * asks the game if the player has won the game after his last move
	 * 
	 * @param player
	 *            player that performed a move
	 * @return true if the player won the game
	 */
	public boolean hasPlayerWonInGame(Player player) {
		if (game != null)
			return game.hasPlayerWon(player);
		else
			return false;
	}

	/**
	 * 
	 * @return returns the next player
	 */
	public int getNextPlayer() {
		if (game != null)
			return game.getNextPlayer();
		else
			return -1;
	}

	/**
	 * 
	 * @return true if the game is running
	 */
	public boolean isGameRunning() {
		if (game != null)
			return isGameRunning;
		else
			return false;
	}

	/**
	 * 
	 * @return number of players in lobby
	 */
	@Override
	public int getNumberOfPlayers() {
		int counter = 0;

		for (int i = 0; i < maxCurrentPlayer; i++) {
			if (playerList[i] != null) {
				counter += 1;
			}
		}

		return counter;
	}

	/**
	 * 
	 * @return returns the lobby name
	 */
	public String getLobbyName() {
		if (lobbyName != null)
			return lobbyName;
		else
			return null;
	}

	/**
	 * 
	 * @return returns the current track
	 */
	public int getTrackId() {
		return trackId;
	}

	/**
	 * 
	 * @return true if every player has crashed within the game
	 */
	public boolean hasEveryPlayerCrashedInGame() {
		if (game != null)
			return game.hasEveryPlayerCrashed();
		else
			return true;
	}

	/**
	 * sets game object to null and sets isGameRunning to false
	 */
	public void closeGame() {
		game = null;
		setGameRunning(false);
	}

	/**
	 * gets a LobbyInformation object that updates the lobby
	 * 
	 * @param info
	 *            information updated by the host
	 */
	@Override
	public boolean updateInformation(ILobbyInformation info) {

		// swap
		for (int i = 0; i < maxCurrentPlayer; i++)
			if (playerList[i] == null && info.getPlayerIDs()[i] != null)
				for (int n = 0; n < maxCurrentPlayer; n++)
					if (playerList[n] != null && info.getPlayerIDs()[i] == null) {
						playerList[i] = playerList[n];
						playerList[n] = null;
					}

		// update
		for (int i = 0; i < playerList.length; i++)
			if (playerList[i] != null && info.getPlayerIDs()[i] != null) {
				playerList[i].setName(info.getPlayerNames()[i]);
				playerList[i].setParticipating(info.getParticipating()[i]);
				DebugOutputHandler.printDebug("player " + i + "  "
						+ playerList[i].isParticipating());
			}

		this.trackId = info.getTrackId();

		lobbyName = info.getLobbyName();

		this.playMode = info.getPlayMode();

		if (isEveryPlayerReady()){
			startGame();
			return true;
		}
		
		return false;
	}

	/**
	 * gets a LobbyInformation object and this object is compared to the actual
	 * lobby's information Will return true, if the changes that got made are
	 * legit Will return false, if at least one of the changes are not legit.
	 * 
	 * @param info
	 *            information being compared to the lobby's information
	 * @param playerID
	 *            the ID of the Player which pushed the Option change
	 * @return true if info and actual lobby settings are the same
	 */
	@Override
	public boolean validateInformation(ILobbyInformation info, int playerID) {

		// Lengths of the arrays do not match
		if (!(playerList != null && info.getPlayerIDs() != null && playerList.length == info
				.getPlayerIDs().length)) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because the array lengths do not match");
			return false;
		}

		// which player is he on server
		int playerOnServer = -1;
		for (int i = 0; i < this.playerList.length; i++)
			if (playerList[i] != null)
				if (playerList[i].getPlayerID().equals(playerID))
					playerOnServer = i;

		// which player is he on client
		int playerOnClient = -1;
		for (int i = 0; i < info.getPlayerIDs().length; i++)
			if (info.getPlayerIDs()[i] != null)
				if (info.getPlayerIDs()[i].equals(playerID))
					playerOnClient = i;

		// player not existent
		if (playerOnServer == -1) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because the player is not existent int the lobby on the server");
			return false;
		}

		// player not existent
		if (playerOnClient == -1) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because the player is not existent int the lobby on the client");
			return false;
		}

		// changed max current player
		if (info.getMaxPlayers() != this.maxCurrentPlayer) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because there is a wrong maxCurrentPlayer number");
			return false;
		}

		// changed game running
		if (info.isGameRunning() != this.isGameRunning) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because isRunning was changed");
			return false;
		}

		// incorrect bot amount
		if (info.getAmountOfAIs() > 4 || info.getAmountOfAIs() < 0) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because there was entered a wrong bot number");
			return false;
		}

		// non host changed play mode
		if ((info.getPlayMode() != this.playMode)
				&& !playerList[playerOnServer].isHost()) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because a non host changed the Play Mode");
			return false;
		}

		// non host changed lobby name
		if (!info.getLobbyName().equals(this.lobbyName)
				&& !playerList[playerOnServer].isHost()) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because a non host changed the Lobby Name");
			return false;
		}

		// non host changed trackID or host changed to invalid number
		if (info.getTrackId() != this.trackId
				&& !playerList[playerOnServer].isHost()) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because a non host changed the Track ID");
			return false;
		} else if (info.getTrackId() > TrackFactory.getSampleTrackCount() - 1 || info.getTrackId() < 0) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because Host changed the Track ID to a non existing one");
			return false;
		}

		// how many player position changes where made?
		int counter = 0;
		for (int i = 0; i < this.playerList.length; i++) {
			// Position change was not done to an empty location
			if (playerList[i] != null && info.getPlayerIDs()[i] != null)
				if (!playerList[i].getPlayerID().equals(info.getPlayerIDs()[i])) {
					DebugOutputHandler
					.printDebug("Lobby: Incoming LobbyInformation is not valid because a player got swapped on top of another one");
					return false;
				}

			// a player got changed to an empty location
			if (playerList[i] != null && info.getPlayerIDs()[i] == null)
				if (i != playerOnServer) {
					DebugOutputHandler
					.printDebug("Lobby: Incoming LobbyInformation is not valid because a wrong player got swapped");
					return false;
				} else
					counter++;
			if (playerList[i] == null && info.getPlayerIDs()[i] != null)
				if (i != playerOnClient) {
					DebugOutputHandler
					.printDebug("Lobby: Incoming LobbyInformation is not valid because a wrong player got swapped");
					return false;
				} else
					counter++;
		}

		// more than one player got changed or other player got changed
		if (playerOnServer != playerOnClient) {
			if (counter > 2) {
				DebugOutputHandler
				.printDebug("Lobby: Incoming LobbyInformation is not valid because multiple players got swapped");
				return false;
			}
		} else if (counter > 0) {
			DebugOutputHandler
			.printDebug("Lobby: Incoming LobbyInformation is not valid because someone else than the player got swapped");
			return false;
		}

		for (int i = 0; i < this.playerList.length; i++) {
			// Check for invalid changes on not swapped players
			if (playerList[i] != null && info.getPlayerIDs()[i] != null)
				if (playerList[i].getPlayerID().equals(info.getPlayerIDs()[i]))
					// Changes to other players where made
					if (!playerList[i].getPlayerID().equals(playerID)) {
						// Name change
						if (!playerList[i].getName().equals(
								info.getPlayerNames()[i])) {
							DebugOutputHandler
							.printDebug("Lobby: Incoming LobbyInformation is not valid because of Name change of another player");
							return false;
						}
						// Participating change
						if (playerList[i].isParticipating() != info
								.getParticipating()[i]
										&& !playerList[playerOnServer].isHost()) {
							DebugOutputHandler
							.printDebug("Lobby: Incoming LobbyInformation is not valid because of Participation change of another player done by a non host");
							return false;
						}
					}
		}

		DebugOutputHandler
		.printDebug("Lobby: Incoming LobbyInformation is valid");

		// Every invalid change was checked for, if landed here, change was
		// valid
		return true;
	}

	/**
	 * 
	 * @return returns the current lobby settings that are sent to all the other
	 *         players on the server
	 */
	@Override
	public LobbyInformation generateLobbyInformation() {
		String[] playerNames = new String[maxCurrentPlayer];
		Integer[] playerIDs = new Integer[maxCurrentPlayer];
		Integer[] typeIDs = new Integer[maxCurrentPlayer];
		boolean[] isParticipating = new boolean[maxCurrentPlayer];
		for (int i = 0; i < maxCurrentPlayer; i++) {
			if (playerList[i] != null) {
				playerNames[i] = playerList[i].getName();
				playerIDs[i] = playerList[i].getPlayerID();
				isParticipating[i] = playerList[i].isParticipating();
			} else {
				playerNames[i] = null;
				playerIDs[i] = null;
				isParticipating[i] = false;
			}

		}
		return new LobbyInformation(playerNames, playerIDs, isParticipating, typeIDs,
				trackId, lobbyName, isGameRunning, lobbyID,playMode);
	}

	/**
	 * the method returns a position from 1 to 5 that is telling the location of
	 * the clientID in the playerList array
	 * 
	 * @param clientID
	 *            clientID of a player
	 * 
	 * @return current position of the player in the playerList array
	 */
	@Override
	public int getGroupPositionByClientID(int clientID) {
		for (int i = 0; i < maxCurrentPlayer; i++) {
			if(playerList[i]!= null)
			if (this.playerList[i].getPlayerID() == clientID) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This will Reset The player after a game was finished.
	 */
	@Override
	public void resetPlayers() {
		if (playerList != null)
			for (int i = 0; i < playerList.length; i++) {
				if (playerList[i] != null) {
					playerList[i].setParticipating(false);
					playerList[i].setHasCrashed(false);
					playerList[i].setCurrentPosition(null);
					playerList[i].setCurrentVelocity(new Point2D(0,0));
					playerList[i].setMemorizeGridPoints(new ArrayList<Point2D>());
				}
			}
	}

	/*
	 * Private methods
	 */

	/**
	 * starts the game, by creating a new game object
	 */
	private void startGame() {

		Player[] playersParticipating = getParticipatingPlayersAsList();
		this.playerList = playersParticipating;
		this.maxCurrentPlayer = playerList.length;
		// playerList = playersParticipating;
		// maxCurrentPlayer = playersParticipating.length;
		game = new Game(TrackFactory.getSampleTrack(trackId),
				playersParticipating, this);
		setGameRunning(true);
	}

	private Player[] getParticipatingPlayersAsList() {
		int numOfPlayersParticipating = 0;
		for (int i = 0; i < maxCurrentPlayer; i++) {
			if (playerList[i] != null) {
				numOfPlayersParticipating++;
			}
		}

		Player[] playersParticipating = new Player[numOfPlayersParticipating];
		int counter = 0;
		for (int i = 0; i < maxCurrentPlayer; i++) {
			if (playerList[i] != null) {
				playersParticipating[counter] = playerList[i];
				counter++;
			}
		}

		return playersParticipating;
	}

	/**
	 * sets the field isGameRunning to wrong or false
	 * 
	 * @param isGameRunning If the game ist running
	 */
	private void setGameRunning(boolean isGameRunning) {
		this.isGameRunning = isGameRunning;
		DebugOutputHandler.printDebug("Game is running now: "
				+ this.isGameRunning + " in lobby " + lobbyID);
	}

	/*
	 * private static methods
	 */
	/*
	 * /** checks if all values in an array are true
	 * 
	 * @param values
	 * 
	 * @return true if every value in the array is true
	 * 
	 * private static boolean allTrue(boolean[] values) { for (boolean value :
	 * values) { if (!value) return false; } return true; }
	 */
	private boolean isEveryPlayerReady() {
		boolean isEveryPlayerReady = false;
		int numberOfPlayers = 0;

		for (int i = 0; i < maxCurrentPlayer; i++) {
			if (playerList[i] != null) {
				if (playerList[i].isParticipating()) {
					isEveryPlayerReady = true;
					numberOfPlayers++;
				} else {
					isEveryPlayerReady = false;
					break;
				}
			}
		}
		return isEveryPlayerReady && numberOfPlayers > 1;
	}

	/**
	 * This will call game.calculateNextRound(); which will as before set
	 * everything for the next round.
	 */
	public void calculateNextRound() {
		if (game != null)
			game.calculateNextRound();
	}

	/**
	 * Looks for the winner of a running match
	 * 
	 * @return the winner Index or null, if there is no winner right now
	 */
	public Integer returnWinnerIndex() {
		if (game != null)
			return game.getWinner();

		return null;
	}
	
	public Integer returnWinnerID(){
		if(game != null)
			return game.getWinnerID();
		return null;
	}

	/**
	 * @return The current play mode
	 */
	public int getCurrentPlayMode(){
		return playMode;
	}

	public int playerDisconnectedLast(){
		return lastPlayerDisconnected;
	}

	/**
	 * calculates the next player which has to move after one client disconnected while playing
	 * 
	 * @param playerDisconnected 
	 */
	private void setNextPlayerToPlay(int playerDisconnected){
		if(game!=null)
			game.nextRoundByDisconnection(playerDisconnected);
	}

	public boolean isFirstRound(){
		return game.isFirstRound();
	}
	
	public int getPlayerIDByPosition(int clientPosition){
		return playerList[clientPosition].getPLAYER_ID();
	}
}
