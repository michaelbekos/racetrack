package src.main.java.logic;

import javafx.geometry.Point2D;

public interface ILobby {
	
	/**
	 * adds a player who wants to enter the lobby If the player is the first
	 * one, then he is set as host
	 * 
	 * @param playerEnteringLobby
	 *            The player
	 */
	public void addPlayer(Player playerEnteringLobby);
	
	/**
	 * deletes a player who wants to leave the lobby the players sessionID is
	 * set to -1; he is not in a lobby if, by accident, he was the host, the
	 * host property is given to the next one in the lobby
	 * 
	 * @param playerLeavingLobby
	 *            The player
	 */
	public void deletePlayer(Player playerLeavingLobby);
	
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
	public Point2D getCollisionPointFromGame(Player player, Point2D velocityChange);
	
	/**
	 * @return if the lobby still has some space left
	 */
	public boolean hasLobbyFreeSlot();
	
	/**
	 * asks the game if there's already a player on the grid, the player is the current player,
	 * the player is still participating in the game and if the velocity change is valid
	 * 
	 * @param player
	 * 			the player performing a move
	 * @param selectedPosition
	 * 			the position he has chosen
	 * @return true if everything is valid
	 */
	public boolean isValidMoveInGame(Player player, Point2D selectedPosition);
	
	
	/**
	 * asks the game if the player has won the game after his last move
	 * 
	 * @param player
	 * 			player that performed a move
	 * @return true if the player won the game
	 */
	public boolean hasPlayerWonInGame(Player player);
	
	/**
	 * 
	 * @return returns the next player
	 */
	public int getNextPlayer();
	
	/**
	 * 
	 * @return true if the game is running
	 */
	public boolean isGameRunning();
	
	/**
	 * 
	 * @return number of players in lobby
	 */
	public int getNumberOfPlayers();
	
	/**
	 * 
	 * @return returns the lobby name
	 */
	public String getLobbyName();
	
	/**
	 * 
	 * @return true if every player has crashed within the game
	 */
	public boolean hasEveryPlayerCrashedInGame();
	
	/**
	 * sets game object to null and sets isGameRunning to false
	 */
	public void closeGame();
	
	/**
	 * gets a LobbyInformation object that updates the lobby 
	 * 
	 * @param info
	 * 			information updated by the host
	 */
	public boolean updateInformation(ILobbyInformation info);
	
	/**
	 * gets a LobbyInformation object and this object is compared to the actual
	 * lobby's information
	 * Will return true, if the changes that got made are legit
	 * Will return false, if at least one of the changes are not legit.
	 * 
	 * @param info
	 *            information being compared to the lobby's information
	 * @param clientID
	 * 			  the ID of the Player which pushed the Option change
	 * @return true if info and actual lobby settings are the same
	 */
	public boolean validateInformation(ILobbyInformation info, int clientID);
	
	/**
	 * 
	 * @return returns the current lobby settings that are sent to all the other players on the server
	 */
	public LobbyInformation generateLobbyInformation();
	
	/**
	 * the method returns a position from 1 to 5 
	 * that is telling the location of the clientID in the playerList array
	 * 
	 * @param clientID
	 * 			clientID of a player
	 * 
	 * @return current position of the player in the playerList array
	 */
	public int getGroupPositionByClientID(int clientID);
	
	/**
	 * Resets the Player objects in the lobby, as if they would have just joined the lobby
	 * Only the position is not reset.
	 */
	public void resetPlayers();
	
	public int getCurrentPlayMode();
}
