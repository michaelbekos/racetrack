package src.main.java.com;

import src.main.java.com.messages.RaceTrackMessage;

public interface ICommunication {

	/**
	 * Sends a RaceTrackMessage to the connected Socket on the other side.
	 * 
	 * If a server sends the message, all Clients within the ReceiverId's list will receive that message
	 * 
	 * If a client sends the message, only the server receives the message
	 * @param message
	 */
	public void sendMessage(RaceTrackMessage message);
}
