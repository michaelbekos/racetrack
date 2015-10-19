package src.main.java.com.messages;

import java.util.List;

/**
 * A RaceTrackMessage is a Message , sent from client to server or from server to client.
 * All the communication happens through the RaceTrackMessages
 * 
 * @author Denis
 *
 */
public interface IRaceTrackMessage {

	/**
	 * get the List of clients who receive the message, or the id of the client who sent the message
	 * @return
	 */
	public List<Integer> getReceiverIds();
	
	/**
	 * adds a receiver client to the list
	 * @param clientId
	 */
	public void addClientID(Integer clientId);
}
