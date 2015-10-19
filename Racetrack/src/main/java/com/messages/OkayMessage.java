package src.main.java.com.messages;

/**
 * Gets sent the first time the player connects to the server
 * it contains the players ID and his playerName
 */
public class OkayMessage extends RaceTrackMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3515726278904825154L;
	
	
	private Integer playerID;
	private String playerName;

	public OkayMessage(Integer playerID, String playerName){
		super();
		this.playerID = playerID;
		this.playerName = playerName;
	}
	
	/**
	 * This is only != null the first time the player connects to the server
	 * so he knows his ID
	 * @return The player id
	 */
	public Integer getPlayerID(){
		return playerID;
	}
	
	/**
	 * This is only != null the first time the player connects to the server
	 * so he knows his Name 
	 * @return The player name
	 */
	public String getPlayerName(){
		return playerName;
	}
}
