package src.main.java.logic;

import java.util.List;

import javafx.geometry.Point2D;

/**
 * This class replies to ServerHandler queries and fetches the right information from the opened Lobbies and running Game instances.
 * It too handles creation of new Lobbies, closing Lobbies, starting games, letting people join lobbies etc.
 * 
 * @author Denis
 *
 */
interface IAdministration {

	/**
	 * This method creates a new player with a given ID
	 * and adds him into the list
	 * @param playerID
	 */
	public void createAndAddNewPlayer(int playerID);
	
	/**
	 * This method creates a new ai with a given ID
	 * and adds him into the list of players
	 * @return aiID
	 */
	public int createAndAddNewAI();
	
	/**
	 * This method removes a player from the playerList in ListOfLobby
	 * when he wants to disconnect
	 * @param playerDisconnected
	 */
	public void playerDisconnects(int playerToDisconnect);
	

	/**
	 * This method opens a new lobby which is stored in the
	 * listOfLobby 
	 */
	
//	public void createAndAddLobby();
	
	/**
	 * This method returns Lobbyinformation of a specific Lobby by playerID
	 * @param playerID
	 */
	public ILobbyInformation getLobbyInformation(int playerID);
	
	/**
	 * returns the lobbyinformation of all existing lobbies.
	 * @return
	 */
	public List<ILobbyInformation> getLobbyInformationList();
	
	
	/**
	 * This method returns a collision point if it exists, if not it just returns null
	 * @param playerID
	 * @param vector
	 * @return collision point if there was one
	 */
	public Point2D updatePlayerPositionByID(int playerID, Point2D vector);
	
	/**
	 * This method checks if this is the players move, if the move is valid
	 * and if there is a collision with another player by asking the specific lobby
	 * which has the game object
	 * @param playerID
	 * @param vector
	 * @return
	 */
	public boolean checkValidityOfClientMove(int playerID, Point2D vector);
	
	
	/**
	 * This method returns a boolean if the player who just played in the specific game of the lobby
	 * has won or not
	 * @param playerId
	 * @param sessionId
	 * @return
	 */
	public boolean hasPlayerWon(int playerID);
	
	
	/** This method assigns a name in the form of a String to a specific playerID
	 * This method 
	 * @param playerID
	 * @param clientName
	 * 
	 * @return the name of the player which will be safed
	 */
	public String setPlayerName(int playerID, String clientName);
	
	
	/**
	 * This method returns a boolean that tells if there's still some space left 
	 * in a specific lobby (sessionID)
	 * @param sessionID
	 * @return
	 */
	public boolean hasLobbyFreeSpace(int sessionID);
	
	/**
	 * This method returns the clientPosition of the player who plays next
	 * in the same session as the clientID who asked
	 * @param sessionID
	 * @return
	 */
	public int getNextPlayerToMove(int playerID);
	
	/**
	 * joins a player to a lobby
	 * if a player leaves a lobby, he will join lobby -1
	 * If no player is left in a lobby it ill automatically get deleted
	 * @param playerID
	 * @param lobbyNumber
	 */
	public void joinLobby(int playerID, int lobbyNumber);
	
	/**
	 * returns a list of integer with playerID which are in the same session as playerID
	 * @param playerID
	 * @return
	 */
	public List<Integer> getPlayerIdsInSameSession(int playerID);
	
	/**
	 * checks, if every player has crashed.
	 * It is checked in the same session as playerID is
	 * @param playerID
	 * @return
	 */
	public boolean hasEveryPlayerCrashed(int playerID);
	
	/**
	 * 
	 * @return player position in group of game between 1 and 5
	 */
	public int getGroupPositionByPlayerID(int playerID);
	
	/**
	 * creates a new lobby by a clientID and the LobbyInfromation he sends
	 * Then the client gets added to that lobby as the host.
	 * @param playerID
	 * @param lobbyInformation
	 */
	public void createLobbyByPlayerID(int playerID, ILobbyInformation lobbyInformation);
	
	/**
	 * updates the lobby depending on the LobbyInformation coming in
	 * @param lobbyID
	 * @param lobbyInformation
	 */
	public void setLobbyParametersByPlayerID( int playerID, ILobbyInformation lobbyInformation);
	
	
	/**
	 * determines whether a game started or not
	 * @param playerID determines the lobby which should be asked
	 * @return did the game started
	 */
	public boolean didGameStartById(int playerID);
	
	/**
	 * determines whether a game started or not by the sessionID
	 * @param sessionID
	 * @return
	 */
	public boolean didGameStartSession(int sessionID);
	
	/**
	 * removes a client out of his lobby and puts him to session -1, the lobbylist
	 */
	public void leaveLobby(int playerID);
	
	/**
	 * returns the session id of a client
	 * @param playerID
	 * @return
	 */
	public int getSessionFromPlayer(int playerID);
	
	/**
	 * sets a players "is participating" in his lobby to false, and
	 * removes him from the game
	 * @param playerID
	 */
	public void playerLeftGameByPlayerID(int playerID);
	
	/**
	 * returns the player who won in a game
	 * @param playerID
	 * @return
	 */
	public int getPlayerWhoWonByPlayerID(int playerID);
	
	/**
	 * tells the lobby to stop the running game and reset its player objects
	 */
	public void closeGameByPlayerID(int playerID);
	
	/**
	 * returns the current velocity of a player @param playerID
	 * @param playerID
	 * @return hist velocity
	 */
	public Point2D getCurrentPlayerVelocityByPlayerID(int playerID);
	
	/**
	 * returns the round of the game, in which @param playerID is at the moment
	 * @param playerID
	 * @return
	 */
	public int getPlayerRoundByPlayerID(int playerID);
	
	/**
	 * starts the next round in the game, after issueing a correct move
	 * @param playerID
	 */
	public void startNextRoundByPlayerID(int playerID);
	
	/**
	 * If a client does not send a move in a defines timeframe
	 * he will be moved by the game automatically, by calling this method.
	 * it will also send a BroadcastMovemessage to all clients in the same lobby
	 * as if he did actually move
	 * @param playerID
	 */
	public void moveClientDefaultVelocity(int playerID);
	
	public void moveAI(int aiID, javafx.geometry.Point2D point);
	
	/**
	 * Sends a message to the client about how much time he has left for his move
	 * @param clientID the client whos turn it is
	 * @param timeLeft how much time he has left before
	 * 					the server automatically moves him
	 */
	public void notifyClientOfTimeLeft(int clientID,int timeLeft);
	
	/**
	 * gets the LobbyInformationof a specific session
	 * 
	 * @param sessionID the session in question we want to get the lobbyinformation from
	 * @return LobbyInformation from the lobby with sessionID
	 */
	public ILobbyInformation getLobbyInformationBySession(int sessionID);
	
	/**
	 * Gets the playerID of the player who won.
	 * It searches the the session of the player playerID and then gets the winner of that session
	 * @param playerID
	 * @return the player who won in sessionID of player playerID
	 */
	public int getPlayerIDWhoWonByPlayerID(int playerID);
	}
