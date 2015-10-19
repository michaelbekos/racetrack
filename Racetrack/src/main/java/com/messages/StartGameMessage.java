package src.main.java.com.messages;

import src.main.java.logic.ILobbyInformation;

/**
 * This message will be sent after every player is ready to play, 
 * or the host starts the game with the start game button.
 * It contains the lobbyInformation and the first player to move.
 * @author Denis
 *
 */
public class StartGameMessage extends RaceTrackMessage{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6124849844575901361L;

	private ILobbyInformation newLobbyInfo;
	
	private int firstPlayerToMove;
	

	public StartGameMessage(ILobbyInformation lI, int firstPlayerToMove){
		super();
		this.newLobbyInfo = lI;
		this.firstPlayerToMove = firstPlayerToMove;
	}
	
	public ILobbyInformation getLobbyInformation(){
		return newLobbyInfo;
	}
	
	public int getFirstPlayerToMove(){
		return firstPlayerToMove;
	}

}
