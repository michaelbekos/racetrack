package src.main.java.logic;

import java.util.List;
import src.main.java.gui.Racetracker;

public class LobbyInformation implements ILobbyInformation, java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3709720973731614404L;
	// Array of Map<Player, Position>
	private PlayerKey[] arrayOfPlayers;
	private int trackId;
	private String lobbyName;
	private boolean isGameRunning;
	private int lobbyID;
	private int maxPlayers;
	// This is only a necessary information on Lobby Creation
	private int amountOfAIs;
	
	private int playMode;

	/**
	 * Lobby Information - this is used to communicate between server and client
	 * @param playerNames      Array of the Player Names - Position in the array = Color
	 * @param playerIDs        Array of the playerIDs - Position in the array = Color
	 * @param isParticipating  Array of the Player Participations - Position in the array = Color
	 * @param trackId          The Track ID
	 * @param lobbyName        The Lobby Name
	 * @param isGameRunning    Is the game running?
	 * @param lobbyID          The LobbyID
	 */
	public LobbyInformation(String[] playerNames, Integer[] playerIDs, boolean[] isParticipating, Integer[] typeIDs,
			int trackId, String lobbyName, boolean isGameRunning, int lobbyID) {
		super();
		
		// check if all arrays have the same size
		if(!(playerNames.length == isParticipating.length && isParticipating.length == playerIDs.length))
			Racetracker.printInDebugMode("LobbyInformation: Invalid arguments for Konstruktor");
		arrayOfPlayers = new PlayerKey[playerNames.length];
		
		// Setup playerMap
		for(int i = 0; i < playerNames.length; i++){
			PlayerKey player = new PlayerKey(playerNames[i], playerIDs[i], isParticipating[i], typeIDs[i]);
			arrayOfPlayers[i] = player;
		}
		
		this.trackId = trackId;
		this.lobbyName = lobbyName;
		this.isGameRunning = isGameRunning;
		this.lobbyID = lobbyID;
		maxPlayers = playerNames.length;
	}
	
	public LobbyInformation(String[] playerNames, Integer[] playerIDs, boolean[] isParticipating, Integer[] typeIDs,
			int trackId, String lobbyName, boolean isGameRunning, int lobbyID,int playMode) {
		super();
		
		// check if all arrays have the same size
		if(!(playerNames.length == isParticipating.length && isParticipating.length == playerIDs.length))
			Racetracker.printInDebugMode("LobbyInformation: Invalid arguments for Konstruktor");
		arrayOfPlayers = new PlayerKey[playerNames.length];
		
		// Setup playerMap
		for(int i = 0; i < playerNames.length; i++)
		{
			PlayerKey player = new PlayerKey(playerNames[i], playerIDs[i], isParticipating[i], typeIDs[i] );
			arrayOfPlayers[i] = player;
		}
		
		this.trackId = trackId;
		this.lobbyName = lobbyName;
		this.isGameRunning = isGameRunning;
		this.lobbyID = lobbyID;
		maxPlayers = playerNames.length;
		this.playMode = playMode;
	}


	@Override
	public String[] getPlayerNames() {
		if(arrayOfPlayers == null)
			return null;
		
		String[] playerNames = new String[arrayOfPlayers.length];
		for(int i = 0; i < playerNames.length; i++)
			if(arrayOfPlayers[i] != null)
				if(arrayOfPlayers[i].name != null)
					playerNames[i] = arrayOfPlayers[i].name;
		
		return playerNames;
	}

	public void setPlayerNames(String[] playerNames) {
		if(playerNames.length == arrayOfPlayers.length){
			for(int i = 0; i < playerNames.length; i++)
				arrayOfPlayers[i].name = playerNames[i];
		}else{
			Racetracker.printInDebugMode("LobbyInformation: Invalid arguments for setPlayerNames");
		}
	}

	@Override
	public boolean[] getParticipating() {
		if(arrayOfPlayers == null)
			return null;
		
		boolean[] isParticipating = new boolean[arrayOfPlayers.length];
		for(int i = 0; i < isParticipating.length; i++)
			if(arrayOfPlayers[i] != null)
				isParticipating[i] = arrayOfPlayers[i].isParticipating;
		
		return isParticipating;
	}

	public void setParticipating(boolean[] ready) {
		if(ready.length == arrayOfPlayers.length){
			for(int i = 0; i < ready.length; i++)
				arrayOfPlayers[i].isParticipating = ready[i];
		}else{
			Racetracker.printInDebugMode("LobbyInformation: Invalid arguments for setPlayerNames");
		}
	}

	@Override
	public int getTrackId() {
		return trackId;
	}

	@Override
	public void setTrackId(int trackID) {
		this.trackId = trackID;
	}
	
	@Override
	public int getPlayMode() {
		return this.playMode;
	}

	@Override
	public String getLobbyName() {
		return lobbyName;
	}

	public void setLobbyName(String lobbyName) {
		this.lobbyName = lobbyName;
	}

	@Override
	public boolean isGameRunning() {
		return isGameRunning;
	}

	public void setGameRunning(boolean isGameRunning) {
		this.isGameRunning = isGameRunning;
	}

	@Override
	public int getLobbyID() {
		return lobbyID;
	}	
	
	@Override
	public void changePlayerToIndex(int index) {
		boolean isDeactivated = true;
		
		if(!isDeactivated)
			if (index >= 0 && index < arrayOfPlayers.length) {
			
				// GET PLAYER ID
			
				Integer playerID = ModelExchange.GameOptions.getPlayerID();
				int currentIndex = -1;
			
				for (int i = 0; i < arrayOfPlayers.length; i++) {
					if (arrayOfPlayers[i].playerID != null && playerID != null) {
						if (arrayOfPlayers[i].playerID.equals(playerID)) {
							currentIndex = i;
							break;
						}
					}
				}
			
				if(currentIndex != -1){
					if (arrayOfPlayers[index].playerID == null) {
						PlayerKey player = arrayOfPlayers[index];
						arrayOfPlayers[index] = arrayOfPlayers[currentIndex];
						arrayOfPlayers[currentIndex] = player;
						this.printLobby();
					}else
						Racetracker.printInDebugMode("LobbyInformation: Position Change was invalid because there was already a Player on this Position");
				}else
					Racetracker.printInDebugMode("LobbyInformation: Position Change was invalid because you are not in the lobby");
			}else
				Racetracker.printInDebugMode("LobbyInformation: Position Change was invalid because the index you wanted to change to was corrupted");
		else
			Racetracker.printInDebugMode("LobbyInformation: Swap is deactivated until the Server got this feature implemented");
	}

	@Override
	public int getMaxPlayers() {
		return maxPlayers;
	}

	@Override
	public int getAmountOfAIs() {
		return amountOfAIs;
	}

	@Override
	public void setAmountOfAIs(int amountOfAIs) {
		this.amountOfAIs = amountOfAIs;
	}

	@Override
	public void setAIs(List<Integer> settings) {
		this.amountOfAIs = 0;
		for( int i=0 ; i<settings.size() ; i++ )
		{
			switch( settings.get( i ) )
			{
			case 0://Free
				break;
			case 1://Human
				break;
			default://AIs (or errors)
				this.amountOfAIs++;
				break;
			}
		}
	}
	
	@Override
	public void toggleParticipating(int index) {
		this.arrayOfPlayers[index].isParticipating = !this.arrayOfPlayers[index].isParticipating;
	}
	
	@Override
	public void setParticipating(int index, boolean particiapting) {
		this.arrayOfPlayers[index].isParticipating = particiapting;
	}
	
	public class PlayerKey implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6961812487093696317L;
		public String name;
		public Integer playerID;
		public boolean isParticipating;

		// Human: typeID==1
		// AI:    typeID==2-X
		public Integer typeID;
		
		public PlayerKey(String name, Integer playerID, boolean isParticipating, Integer typeID) {
		    this.name = name;
		    this.isParticipating = isParticipating;
		    this.playerID = playerID;
		    this.typeID=typeID;
		}
	}

	@Override
	public Integer[] getPlayerIDs() {
		if(arrayOfPlayers == null)
			return null;
		
		Integer[] playerIDs = new Integer[arrayOfPlayers.length];
		for(int i = 0; i < playerIDs.length; i++)
			if(arrayOfPlayers[i] != null)
				if(arrayOfPlayers[i].name != null)
					playerIDs[i] = arrayOfPlayers[i].playerID;
		
		return playerIDs;
	}
	
	public Integer[] getTypeIDs() {
		if(arrayOfPlayers == null)
			return null;

		Integer[] typeIDs = new Integer[arrayOfPlayers.length];
		for(int i = 0; i < typeIDs.length; i++)
			if(arrayOfPlayers[i] != null)
				if(arrayOfPlayers[i].name != null)
					typeIDs[i] = arrayOfPlayers[i].typeID;
		
		return typeIDs;
	}
	
	
	/**
	 * This prints all the Lobby Information to the console
	 */
	public void printLobby(){
		Racetracker.printInDebugMode("\nLobby Name: " + lobbyName + " , Lobby ID: " + lobbyID + " , Max Players: " + maxPlayers);
		Racetracker.printInDebugMode("Track ID: " + trackId + " , gameRunning: " + isGameRunning + " , Amount Of AIs" + amountOfAIs);
		for(int i = 0; i < arrayOfPlayers.length; i++){
			if(arrayOfPlayers[i] != null && arrayOfPlayers[i].name != null && arrayOfPlayers[i].playerID != null){
				Racetracker.printInDebugMode("Player[" + i + "] Name, ID, participating: " + arrayOfPlayers[i].name + " , " + 
						arrayOfPlayers[i].playerID + " , " + arrayOfPlayers[i].isParticipating + "\n");
			}
		}
	}
}
