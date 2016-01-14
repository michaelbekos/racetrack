package src.main.java.logic;

import java.util.List;

public interface ILobbyInformation {

	/**
	 * 
	 * @return all playerNames in the lobby
	 */
	public String[] getPlayerNames();

	/**
	 * 
	 * @return all participating players in the lobby
	 */
	public boolean[] getParticipating();

	/**
	 * 
	 * @return all the playerIDs in the lobby
	 */
	public Integer[] getPlayerIDs();
	
	/**
	 * 
	 * @return all the typeIDs in the lobby ( i.e AI types / Human )
	 */
	public Integer[] getTypeIDs();
	
	/**
	 * 
	 * @return the track
	 */
	public int getTrackId();
	
	/**
	 * Sets the track id for the lobby
	 * @param trackID the track id
	 */
	public void setTrackId(int trackID);
	
	/**
	 * 
	 * @return the play mode
	 */
	public int getPlayMode();

	/**
	 * 
	 * @return lobbyName
	 */
	public String getLobbyName();

	/**
	 * 
	 * @return if the game is running
	 */
	public boolean isGameRunning();
	
	/**
	 * 
	 * @return the lobbyID
	 */
	public int getLobbyID();
	
	/**
	 * Changes the Players position
	 * @param index	   The Position the player should be after this method call
	 * 
	 * @author Tobias
	 */
	public void changePlayerToIndex(int index);
	
	/**
	 * Toggles the participation status
	 * @param index The player index
	 */
	public void toggleParticipating(int index);
	
	/**
	 * 
	 * @param index The player index in the array
	 * @param particiapting Whether the player wants to set its participating status to true or false
	 */
	public void setParticipating(int index, boolean particiapting);

	/**
	 * This is only a necessary information on Lobby Creation
	 * @return How many players in the game shall be AI?
	 */
	public int getAmountOfAIs();
	
	/**
	 * This is only a necessary information on Lobby Creation
	 * @param isAI how many AI's shall there be in the game?
	 */
	public void setAmountOfAIs(int isAI);
	
	/**
	 * 
	 * @return Max amount of players
	 */
	public int getMaxPlayers();
	
	/**
	 * Prints the lobby to the console (only if debug is on)
	 */
	public void printLobby();
	void setAIs(List<Integer> settings);
}
