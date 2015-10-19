package src.main.java.com.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * The message getting send between Client and Server, 
 * This class' subclasses contain all the relevant infromation, the client needs to display,
 * or the infromation, the server has to process
 * @author denis
 *
 */
public class RaceTrackMessage implements java.io.Serializable,IRaceTrackMessage{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -6210561742618881087L;
	/**
	 * When coming to the server:
	 * 		contains one id, of the sending client
	 * When going out of the server:
	 * 		contains all the receiver client ids
	 */
	protected List<Integer> receiverIDs;
	
	protected  RaceTrackMessage(){
		receiverIDs = new ArrayList<Integer>();
	}
	
	public List<Integer> getReceiverIds(){
		return receiverIDs;
	}
	
	/**
	 * adds a receiver client to the list
	 * @param clientId
	 */
	public void addClientID(Integer clientId){
		receiverIDs.add(clientId);
	}
}
