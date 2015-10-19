package src.main.java.com.messages.handler;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.gui.navigation.MultiSceneBase;

/**
 *	The Abstract class for the message handler on client side
 */
public abstract class RaceTrackMessageClientHandler implements IRaceTrackMessageClientHandler{

	protected static MultiSceneBase sceneBase;
	
	/**
	 * Creating the handler
	 * @param sceneBase the communication interface for the handler
	 */
	public RaceTrackMessageClientHandler(MultiSceneBase sceneBase){
		RaceTrackMessageClientHandler.sceneBase = sceneBase;
	}
	
	/**
	 * Updating the interface on change
	 * @param sceneBase the communication interface for the handler
	 */
	public void setRTMultiSceneBase(MultiSceneBase sceneBase){
		RaceTrackMessageClientHandler.sceneBase = sceneBase;
	}
	
	/**
	 * Updates the Clients version of the lobbies and the game
	 * @param messageToHandle the message that got received
	 */
	abstract public void updateModel(RaceTrackMessage messageToHandle);
}
