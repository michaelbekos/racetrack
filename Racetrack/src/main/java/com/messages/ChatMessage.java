package src.main.java.com.messages;

/**
 * This is a chat message
 * If sent from client -> server, it only contains the chatMessage string
 * If sent from server -> client, it contains both, the message and  the name of the player
 *                        who sent the message.
 *
 */
public class ChatMessage extends RaceTrackMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8222212080039495352L;

	/**
	 * The message the player did sent
	 */
	private String chatMessage;
	
	/**
	 * The name of the Player who sent the message.
	 * 
	 * This is null, if the message comes from the client
	 * This contains the player name, if it comes from the server
	 */
	private String playerName;
	
	public ChatMessage(String chatMessage, String playerName){
		super();
		this.chatMessage = chatMessage;
		this.playerName = playerName;
	}
	
	public String getChatMessage(){
		return chatMessage;
	}
	
	public String getPlayerName(){
		return playerName;
	}
}
