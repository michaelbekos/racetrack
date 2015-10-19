package src.main.java.core;

import src.main.java.com.messages.RaceTrackMessage;

public interface IControllerServer {

	/**
	 * Informs the Administration that a client connected, with his ID
	 * The Administration will add the client to his list.
	 * @param clientId the client who connected
	 */
	public void clientConnected(Integer clientId);
	
	/**
	 * This method gets called by the ComServer while working through the messageBuffer
	 * This method will call the right handler for the message and gives the message to the handler
	 * @param message
	 */
	public void receiveMessage(RaceTrackMessage message);
	
	/**
	 * This method will be called by the administration.
	 * It informs the ComServer about a client who wants to disconnect.
	 * @param playerIDToDisconnect
	 */
	public void disconnectPlayerFromServer(int playerIDToDisconnect);
	
	/**
	 * Sends an extra message to ComServer. This message is not an answer to the message coming from the client
	 * @param message the extra message needed to be sent to the client
	 */
	public void sendExtraMessage(RaceTrackMessage message);

}
