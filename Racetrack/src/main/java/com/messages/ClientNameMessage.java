package src.main.java.com.messages;

/**
 * When a client connects succesfully to the server, the client sends this message to the server immediately,
 * to tell his name. The server will check if that Name is already in use.
 * If it is already in Use, the Username will get is ID added
 * @author Denis
 *
 */
public class ClientNameMessage extends RaceTrackMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2865592168538581759L;
	/**
	 * the client name
	 */
	private String clientName;
	
	public ClientNameMessage(String clientName){
		super();
		this.clientName = clientName;
	}
	
	public String getClientName(){
		return clientName;
	}
	
}
