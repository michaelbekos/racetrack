package src.main.java.gui.panes;

import javafx.scene.paint.Color;
import src.main.java.gui.scenes.LobbyController;

public interface ILobbyPlayerPane {

	/**
	 * Sets the pane index within the lobby scene
	 * 
	 * @param index
	 *            Pane position
	 */
	public void setIndex(int index);

	/**
	 * Set absolute stroke width
	 * 
	 * @param width
	 *            Stroke width
	 */
	public void setStrokeWidth(int width);

	/**
	 * Set stroke width relative to the player circle size.
	 * 
	 * @param percentage
	 *            Relative to the player circle size where filled is 1.0
	 */
	public void setBorderStrokeWidthFillPercent(double percentage);

	/**
	 * Set color which represents the current player or line
	 * 
	 * @param color
	 *            Player color
	 */
	public void setStrokeColor(Color color);

	/**
	 * Set the player name visible in the pane next to the players color.
	 * 
	 * @param name
	 *            Player name
	 */
	public void setPlayerName(String name);

	/**
	 * Updates the player participating entry for when a player changes his
	 * ready status.
	 * 
	 * @param ready
	 *            Indicates if a player has time to play or not while he is in
	 *            the lobby.
	 */
	public void setPlayerParticipating(boolean ready);

	public LobbyController getLobbyController();

	public void setLobbyController(LobbyController lobbyController);

}
