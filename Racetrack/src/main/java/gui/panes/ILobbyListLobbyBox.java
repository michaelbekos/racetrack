package src.main.java.gui.panes;

import src.main.java.gui.scenes.LobbyListController;
import src.main.java.logic.Track;

public interface ILobbyListLobbyBox {

	// MARK: Getter and setter
	public void setTrack(Track track);

	/**
	 * Sets the lobby name label text
	 * 
	 * @param name
	 *            Lobby name
	 */
	public void setLobbyName(String name);

	public void setHostName(String name);

	public void setPlayMode(int playMode);

	/**
	 * Adds all players to the preview. The length of the player names array
	 * will be used as an indicator for how many players could join the lobby
	 * and if a name is not null it is used by a player.
	 * 
	 * @param playerNames
	 *            An array of all player names
	 */
	public void setPlayers(String[] playerNames);

	/**
	 * @return The lobby id represented by this 'card'.
	 */
	public int getLobbyId();

	/**
	 * Set represented lobby id
	 * 
	 * @param lobbyId
	 *            The lobby id
	 */
	public void setLobbyIndex(int lobbyId);

	/**
	 * @return The controller where all the actions will be performed.
	 */
	public LobbyListController getLobbyListController();

	/**
	 * Set lobby controller.
	 * 
	 * @param lobbyListController
	 *            The controller to send actions to.
	 */
	public void setLobbyListController(LobbyListController lobbyListController);

}
