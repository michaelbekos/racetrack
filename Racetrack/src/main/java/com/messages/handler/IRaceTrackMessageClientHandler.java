package src.main.java.com.messages.handler;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.gui.navigation.MultiSceneBase;

/**
 *	The Abstract class for the message handler on client side
 */
public interface IRaceTrackMessageClientHandler {
	
	/**
	 * Updates the Clients version of the lobbies and the game
	 * @param messageToHandle the message that got received
	 */
	public void updateModel(RaceTrackMessage messageToHandle);
	
	/**
	 * Updating the interface on change
	 * @param sceneBase the communication interface for the handler
	 */
	public void setRTMultiSceneBase(MultiSceneBase sceneBase);
}
