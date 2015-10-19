package src.main.java.com.messages;


/**
 * This Message can be sent by the client and by the server.
 * 
 * By the Server: The server informs the players about a disconnected player while in game.
 * Every player in that session will recieve this message
 * 
 * By the client: Inform the server that this client wants to disconnect
 * 
 * @author Denis
 *
 */
public class DisconnectMessage extends RaceTrackMessage {



	/**
	 * 
	 */
	private static final long serialVersionUID = -2015747322670239692L;

	private int playerToDisconnect;
	
	private int nextPlayerToMove;
	
	public DisconnectMessage(){
		super();
	}
	
	public DisconnectMessage(int palyerToDisconnect, int nextPlayerToMove){
		super();
		this.playerToDisconnect = palyerToDisconnect;
		this.nextPlayerToMove = nextPlayerToMove;
	}
	
	public boolean isDisconnectMessage(){
		return true;
	}
	
	public int getPlayerToDisconnect(){
		return playerToDisconnect;
	}
	
	public int getNextPlayerToMove(){
		return nextPlayerToMove;
	}

}
