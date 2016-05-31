package src.main.java.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import src.main.java.com.messages.DisconnectMessage;
import src.main.java.com.messages.PlayerWonMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.SendOptionsMessage;
import src.main.java.com.messages.TimeLeftMessage;
import src.main.java.com.messages.handler.server.VectorMessageServerHandler;
import src.main.java.core.ControllerServer;
import src.main.java.core.DebugOutputHandler;
import src.main.java.gui.Racetracker;
import src.main.java.logic.AIstar.AIstar;


public class Administration implements IAdministration{

	/**
	 * Determines the value if a player is not in a lobby. 
	 * He is in the lobbyList if his sessionID = NO_LOBBY_ID
	 */
	private static final int NO_LOBBY_ID = -1;

	/**
	 * Count the number of lobbys on the server, which had been opened while the server is online
	 * Each new lobby increments the counter by one.
	 * Each new lobby gets this new counter as a sessionID
	 */
	private static int lobbyIdCounter; 

	private Map<Integer, Lobby> lobbyMap;

	private Map<Integer, Player> playerMap;

	private Map<Integer, MoveTimer> moveTimerMap;

	private ControllerServer controller;

	/**
	 * determines if the move Timer should be used or not
	 */
	private boolean useMoveTimer;


	public Administration(ControllerServer controller){

		this.controller = controller;

		lobbyIdCounter = 0;

		lobbyMap = new HashMap<Integer, Lobby>();

		playerMap = new HashMap<Integer, Player>();

		moveTimerMap = new HashMap<Integer, MoveTimer>();

		/*
		 * set this to true for limited time on moves
		 */
		useMoveTimer = true;
	}

	/*
	 * Public methods, defines in IAdministration
	 */

	@Override
 	public void createAndAddNewPlayer( int playerID )
 	{
 		//add player with default name
 		Player newPlayer = new Player( playerID, "Player_"+playerID );
 		getPlayerMap().put( playerID, newPlayer );
 	}
	
	//@Override
	public int createAndAddNewAI( int ai_type )
	{
		//add player with default name
		Player newPlayer;
		int AIid = controller.getNewAIid();
		switch( ai_type )
		{
		case 2:
			newPlayer= new AI_NoMover( AIid, "No Mover" );
			break;
		case 3:
			newPlayer= new AI_Random( AIid, "Random" );
			break;
		case 4:
			newPlayer= new AI_Puckie( AIid, "Puckie" );
			break;
		case 5:
			newPlayer= new AIstar( AIid, "AIstar" );
			break;
		case 6:
			newPlayer= new AI_Crasher( AIid, "Crasher" );
			break;
		case 7:
			newPlayer= new AI_Zigzag( AIid, "ZigZag" );
			break;
		default:
			return 0;
		}
		//newPlayer.setSessionID( ... );
		playerMap.put( AIid, newPlayer );
		return AIid;
	}

	@Override
	public void joinLobby(int playerID, int sessionID){
		//cant join session -1, to get to session -1 a player needs to leave his lobby and get back
		//to lobby list
		if(sessionID == NO_LOBBY_ID)
			return;
		//check if the player is already in an other lobby
		int playerSession = getSessionFromClient(playerID);

		if(playerSession == NO_LOBBY_ID){
			lobbyMap.get(sessionID).addPlayer(getPlayerMap().get(playerID));
			DebugOutputHandler.printDebug("Player "+getPlayerMap().get(playerID).getName()+" now is in lobby " + getPlayerMap().get(playerID).getSessionID());
		}
	}
	
	@Override
	public void playerDisconnects(int playerToDisconnect){

		int playerToDisconnectPosition = -1;

		if( getPlayerMap().get(playerToDisconnect) != null){

			int session = getPlayerMap().get(playerToDisconnect).getSessionID();

			if(lobbyMap.get(session) != null){
				playerToDisconnectPosition = lobbyMap.get(session).getGroupPositionByClientID(playerToDisconnect);

				List<Integer> playersInSameSession = getPlayerIdsInSameSession(playerToDisconnect);

				if(session != NO_LOBBY_ID){

					lobbyMap.get(session).deletePlayer(getPlayerMap().get(playerToDisconnect));

					if(moveTimerMap.get(session)!=null && lobbyMap.get(session) != null){
						int currentPlayer = getPlayerIDByGroupPosition(lobbyMap.get(session).getNextPlayer(), session);
						if(moveTimerMap.get(session).getPlayer() == currentPlayer){

							moveTimerMap.get(session).setFinished(true);

							if(lobbyMap.get(session).isGameRunning()){
								moveTimerMap.put(session , new MoveTimer(currentPlayer, this, lobbyMap.get(session).getCurrentPlayMode()));
							}


						}
					}

					if(!lobbyMap.get(session).isGameRunning()){
						ILobbyInformation info = lobbyMap.get(session).generateLobbyInformation();

						RaceTrackMessage answer = new SendOptionsMessage(info);
						for(Integer i : playersInSameSession)
							answer.addClientID(i);
						sendMessage(answer);

					}

				}
				getPlayerMap().remove(playerToDisconnect);

				controller.disconnectPlayerFromServer(playerToDisconnect);

				RaceTrackMessage playerDisconnectsMessage = generateDisconnectMessage(playerToDisconnectPosition, session, playersInSameSession);

				sendMessage(playerDisconnectsMessage);

				sendWinnerMessage(playerDisconnectsMessage);
			}
		}
		return;
	}

	@Override
	public void leaveLobby(int playerID){

		int sessionID = getPlayerMap().get(playerID).getSessionID();

		DebugOutputHandler.printDebug(getPlayerMap().get(playerID).getName()+" was in lobby: "+getPlayerMap().get(playerID).getSessionID()+ " and left");

		//cant "leave" list of lobby as a lobby

		if(sessionID != NO_LOBBY_ID && lobbyMap.get(sessionID) != null && getPlayerMap().get(playerID) != null){

			int playerToDisconnectPosition = lobbyMap.get(sessionID).getGroupPositionByClientID(playerID);

			List<Integer> playersInSameSession = getPlayerIdsInSameSession(playerID);

			if(lobbyMap.get(sessionID).isGameRunning()){

				lobbyMap.get(sessionID).deletePlayer(getPlayerMap().get(playerID));

				resetPlayer(playerID);

				if(moveTimerMap.get(sessionID)!=null && lobbyMap.get(sessionID) != null){
					int currentPlayer = getPlayerIDByGroupPosition(lobbyMap.get(sessionID).getNextPlayer(), sessionID);
					if(moveTimerMap.get(sessionID).getPlayer() == currentPlayer){

						moveTimerMap.get(sessionID).setFinished(true);

						if(lobbyMap.get(sessionID).isGameRunning()){
							moveTimerMap.put(sessionID , new MoveTimer(currentPlayer, this, lobbyMap.get(sessionID).getCurrentPlayMode()));
						}
					}
				}
				RaceTrackMessage playerDisconnectsMessage = generateDisconnectMessage(playerToDisconnectPosition, sessionID, playersInSameSession);

				sendMessage(playerDisconnectsMessage);
				DebugOutputHandler.printDebug("Up to the first occurence I am happy");
				deleteEmptyLobbies(sessionID);
				DebugOutputHandler.printDebug("After to the first occurence I am still happy");
				
				sendWinnerMessage(playerDisconnectsMessage);

				return;
			}
			
			DebugOutputHandler.printDebug("Are we here rights");
			lobbyMap.get(sessionID).deletePlayer(getPlayerMap().get(playerID));
			resetPlayer(playerID);

		}
		//check if a lobby is empty now. if it is, delete it
		DebugOutputHandler.printDebug("Up to the second occurence I am happy");
		deleteEmptyLobbies(sessionID);
		DebugOutputHandler.printDebug("After to the second occurence I am still happy");

	}


	@Override
	public void createLobbyByPlayerID(int playerID, ILobbyInformation lobbyInformation){

		DebugOutputHandler.printDebug("Player "+playerID+" created a new Lobby");

		int currentPlayerSession = getSessionFromClient(playerID);

		if( currentPlayerSession != NO_LOBBY_ID)
			return;

		int sessionID = createAndAddLobby(lobbyInformation.getMaxPlayers(),lobbyInformation.getLobbyName(),lobbyInformation.getTrackId());

		if(sessionID == NO_LOBBY_ID)
		{
			return;
		}

		//set new lobby without players but with the right parameters
		setLobbyParameters(sessionID, lobbyInformation);

		//let the player join set new lobby
		joinLobby(playerID, sessionID);
	}

	@Override
	public void setLobbyParametersByPlayerID(int playerID, ILobbyInformation lobbyInformation){
		//sessionID = lobbyInformation.getSessionID() check for same IDs
		int sessionID = getSessionFromClient(playerID);

		//if lobby changes are valid, only then change them
		if(lobbyMap.get(sessionID).validateInformation(lobbyInformation, playerID)){

			if(lobbyMap.get(sessionID).updateInformation(lobbyInformation)){
				int nextPlayer = getNextPlayerToMove(playerID);
				int nextPlayerID = getPlayerIDByGroupPosition(nextPlayer,sessionID);
				moveTimerMap.put(sessionID, new MoveTimer(nextPlayerID, this, lobbyMap.get(sessionID).getCurrentPlayMode()));
				moveTimerMap.get(sessionID).start();
			}

		}else{

			DebugOutputHandler.printDebug("Administration: Incoming LobbyInformation was not valid");

		}
	}

	@Override
	public ILobbyInformation getLobbyInformation(int playerID){
		int sessionID = getSessionFromClient(playerID);
		if(sessionID == NO_LOBBY_ID)
			return null;

		ILobbyInformation lobbyInformation = null;

		if(lobbyMap.get(sessionID) != null && sessionID != NO_LOBBY_ID){
			lobbyInformation = lobbyMap.get(sessionID).generateLobbyInformation();
		}
		return lobbyInformation;
	}

	@Override
	public ILobbyInformation getLobbyInformationBySession(int sessionID){
		if(sessionID == NO_LOBBY_ID || lobbyMap.get(sessionID) == null)
			return null;
		ILobbyInformation lobbyInformation = lobbyMap.get(sessionID).generateLobbyInformation();
		return lobbyInformation;
	}

	@Override
	public List<ILobbyInformation> getLobbyInformationList(){
		List<ILobbyInformation> lobbyInfromationList = new ArrayList<ILobbyInformation>();

		for (Integer key : lobbyMap.keySet()) {
			ILobbyInformation lobInf = getLobbyInformationBySession(key);
			lobbyInfromationList.add(lobInf);
		}
		return lobbyInfromationList;
	}

	@Override
	public List<Integer> getPlayerIdsInSameSession(int playerID){

		List<Integer> playersInSameSession = new ArrayList<Integer>();
		int sessionIDSearched = getSessionFromClient(playerID);

		for(Player p : getPlayerMap().values()){
			if(p != null ){
				if(sessionIDSearched == p.getSessionID()){
					playersInSameSession.add(p.getPlayerID());
				}
			}
		}

		return playersInSameSession;
	}

	@Override
	public Point2D updatePlayerPositionByID(int playerID, Point2D vector){
		Player playerMoved = getPlayerMap().get(playerID);
		int playerSessionID = getSessionFromClient(playerID);	
		return lobbyMap.get(playerSessionID).getCollisionPointFromGame(playerMoved, vector);
	}

	//HOWTO
	@Override
	public String setPlayerNameAndAiType( int playerID, String clientName, Integer clientAiTypeId )
	{		
		String playerName=getNextFreePlayerName( clientName, playerID );

		Player entity;
		switch( clientAiTypeId )
		{
		default:
		case 1:
			entity= new Player( playerID, clientName );
			break;
		case 2:
			entity= new AI_NoMover( playerID, clientName );
			break;
		case 3:
			entity= new AI_Random( playerID, clientName );
			break;
		case 4:
			entity= new AI_Puckie( playerID,  clientName );
			break;
		case 5:
			entity= new AIstar( playerID,  clientName );
			break;
		case 6:
			entity= new AI_Crasher( playerID, clientName );
			break;
		case 7:
			entity= new AI_Zigzag( playerID, clientName );
			break;
		case 8:
			entity= new AI_LimitedView_DriveSafe( playerID, clientName );
			break;
		case 9:
			entity= new AI_LimitedView_FastCorner( playerID, clientName );
			break;
		case 10:
			entity= new AI_LimitedView_FastTopmost( playerID, clientName );
			break;
		case 11:
			entity= new AI_LimitedView_SpeedUp( playerID, clientName );
			break;
		case 12:
			entity= new AI_Bipartite( playerID, clientName );
			break;
		case 13:
			entity= new AI_LimitedView_DriveCarefully( playerID, clientName );
			break;
			/* ADD YOUR NEW AI ALSO HERE */
		}
		//Player player=getPlayerMap().get(playerID);
		getPlayerMap().put( playerID, entity );
		entity.setName(playerName);

		return playerName;
	}	

	@Override
	public boolean hasLobbyFreeSpace(int sessionID){
		if(lobbyMap.get(sessionID) == null)
			return false;
		else
			return lobbyMap.get(sessionID).hasLobbyFreeSlot();

	}


	@Override
	public boolean checkValidityOfClientMove(int playerID, Point2D vector) {


		Player playerMoved = getPlayerMap().get(playerID);

		int sessionID = playerMoved.getSessionID();

		if(playerMoved == null || sessionID == NO_LOBBY_ID)
			return false;

		return lobbyMap.get(sessionID).isValidMoveInGame(playerMoved, vector);
	}


	@Override
	public boolean hasPlayerWon(int playerID) {
		Player playerSearched = getPlayerMap().get(playerID);
		int sessionID = playerSearched.getSessionID();
		return lobbyMap.get(sessionID).hasPlayerWonInGame(playerSearched);
	}

	@Override
	public int getNextPlayerToMove(int playerID){
		int sessionID = getSessionFromClient(playerID);
		return lobbyMap.get(sessionID).getNextPlayer();
	}	

	@Override
	public boolean hasEveryPlayerCrashed(int playerID){

		int sessionID = getSessionFromClient(playerID);

		return lobbyMap.get(sessionID).hasEveryPlayerCrashedInGame();	

	}

	@Override
	public int getGroupPositionByPlayerID(int playerID){
		int sessionID = getSessionFromClient(playerID);
		return lobbyMap.get(sessionID).getGroupPositionByClientID(playerID);
	}

	@Override
	public boolean didGameStartById(int playerID) {
		int sessionID = getSessionFromClient(playerID);
		boolean isRunning = lobbyMap.get(sessionID).isGameRunning();

		DebugOutputHandler.printDebug("is game running in session " + sessionID+ ": "+isRunning);

		return 	isRunning;
	}

	@Override
	public boolean didGameStartSession(int sessionID) {
		return 	lobbyMap.get(sessionID).isGameRunning();
	}

	@Override
	public int getSessionFromPlayer(int playerID){
		return getPlayerMap().get(playerID).getSessionID();
	}

	@Override
	public void playerLeftGameByPlayerID(int playerID) {
		//TODO: finish
		int sessionID = getSessionFromClient(playerID);

		//		lobbyMap.get(sessionID).playerLeaves(playerID);

		lobbyMap.get(sessionID).deletePlayer(getPlayerMap().get(playerID));


	}

	@Override
	public int getPlayerWhoWonByPlayerID(int playerID) {

		Integer playerWhoWon = null;

		int sessionID = getSessionFromClient(playerID);

		if(lobbyMap.get(sessionID) != null && sessionID != -1)
			playerWhoWon = lobbyMap.get(sessionID).returnWinnerIndex();

		return playerWhoWon == null? -1: playerWhoWon;
	}

	@Override
	public int getPlayerIDWhoWonByPlayerID(int playerID) {

		Integer playerWhoWon = null;

		int sessionID = getSessionFromClient(playerID);

		if(lobbyMap.get(sessionID) != null && sessionID != -1)
			playerWhoWon = lobbyMap.get(sessionID).returnWinnerID();

		return playerWhoWon == null? -1: playerWhoWon;
	}

	@Override
	public void closeGameByPlayerID(int playerID){
		int sessionID = getSessionFromClient(playerID);
		List<Integer> playersInSameSession = getPlayerIdsInSameSession(playerID);

		if(sessionID != NO_LOBBY_ID && lobbyMap.get(sessionID) != null){

			if(moveTimerMap.get(sessionID) != null)
				moveTimerMap.get(sessionID).setFinished(true);

			lobbyMap.get(sessionID).closeGame();
			lobbyMap.get(sessionID).resetPlayers();
			for(Integer i : playersInSameSession){
				
				leaveLobby(i);
				
			}
		}

	}

	@Override
	public Point2D getCurrentPlayerVelocityByPlayerID(int playerID) {

		Point2D currentPlayerVelocity = null;

		int sessionID = getSessionFromClient(playerID);
		if( sessionID != NO_LOBBY_ID && lobbyMap.get(sessionID) != null){
			currentPlayerVelocity = lobbyMap.get(sessionID).getVelocityOfPlayerByID(playerID);
		}

		return currentPlayerVelocity;
	}

	@Override
	public int getPlayerRoundByPlayerID(int playerID){
		int sessionID = getSessionFromClient(playerID);
		if(sessionID != NO_LOBBY_ID && lobbyMap.get(sessionID)!= null){
			return lobbyMap.get(sessionID).getRound();
		}
		return -1;
	}

	@Override
	public void startNextRoundByPlayerID(int playerID){
		int sessionID = getSessionFromClient(playerID);
		if(sessionID != NO_LOBBY_ID && lobbyMap.get(sessionID)!= null){
			lobbyMap.get(sessionID).calculateNextRound();

			//			if(useMoveTimer && !lobbyMap.get(sessionID).isFirstRound()){
			if(useMoveTimer){
				if(moveTimerMap.get(sessionID) != null){
					moveTimerMap.get(sessionID).setFinished(true);
				}
				int nextPlayer = getNextPlayerToMove(playerID);
				int nextPlayerID = getPlayerIDByGroupPosition(nextPlayer,sessionID);
				moveTimerMap.put(sessionID, new MoveTimer(nextPlayerID, this, lobbyMap.get(sessionID).getCurrentPlayMode()));
				moveTimerMap.get(sessionID).start();
			}
		}
	}
	
	public Map<Integer,Player> getPlayerMap()
	{
		return this.playerMap;
	}

	@Override
	public void notifyClientOfTimeLeft(int playerID, int timeLeft) {
		RaceTrackMessage answer = new TimeLeftMessage(timeLeft);

		answer.addClientID(playerID);
		sendMessage(answer);
	}
	


	@Override
	public void moveAI(int aiID, javafx.geometry.Point2D point) {
		RaceTrackMessage answer = null;

		int sessionID = getSessionFromClient(aiID);

		//Move player and get collisionpoint if there was one
		if(lobbyMap.get(sessionID) != null && sessionID != NO_LOBBY_ID && getPlayerMap().get(aiID)!=null){

			if(lobbyMap.get(sessionID).isFirstRound()){
				setRandomStartingPoint(aiID, sessionID);
				return;
			}

			List<Integer> playersInSameSession = null;

			if(getPlayerIdsInSameSession(aiID) != null){
				playersInSameSession = getPlayerIdsInSameSession(aiID);
			}

			int playerWhoMoved = getGroupPositionByPlayerID(aiID);

			AI ai = (AI) getPlayerMap().get(aiID);

			if(ai.hasCrashed() || !ai.isParticipating()){
				return;
			}

			Point2D collisionPointFromPlayer = lobbyMap.get(sessionID).makeAIMoveAt(ai,point);

			Point2D playerPosition = ai.getCurrentPosition();

			startNextRoundByPlayerID(aiID);

			int nextPlayerToMove = getNextPlayerToMove(aiID);

			int playerWhoWon = getPlayerWhoWonByPlayerID(aiID); 

			boolean didAPlayerWin = playerWhoWon != -1;

			int round = getPlayerRoundByPlayerID(aiID);

			Point2D playerVelocity = getCurrentPlayerVelocityByPlayerID(aiID);

			if(didAPlayerWin){
				DebugOutputHandler.printDebug((getPlayerMap().get(playerWhoWon).isAI() ? "AI " : "Player ") +playerWhoWon+" won the game.");

				answer = VectorMessageServerHandler.generatePlayerWonMessage(playerWhoMoved,playerWhoWon, playerPosition);
				closeGameByPlayerID(aiID);

			}else{
				answer = VectorMessageServerHandler.generateBroadcastMoveMessage(playerWhoMoved,playerPosition,
						null,nextPlayerToMove, playerVelocity, round);
			}

			for(int p : playersInSameSession)
			{
				answer.addClientID(p);
			}
			//method on lobby and game which just moves the player by his velocity again! not just by a specific point
		}		

		sendMessage(answer);
	}

	@Override
	public void moveClientDefaultVelocity(int playerID) {
		RaceTrackMessage answer = null;

		int sessionID = getSessionFromClient(playerID);

		//Move player and get collisionpoint if there was one
		if(lobbyMap.get(sessionID) != null && sessionID != NO_LOBBY_ID && getPlayerMap().get(playerID)!=null){

			if(lobbyMap.get(sessionID).isFirstRound()){
				setRandomStartingPoint(playerID, sessionID);
				return;
			}

			List<Integer> playersInSameSession = null;

			if(getPlayerIdsInSameSession(playerID) != null){
				playersInSameSession = getPlayerIdsInSameSession(playerID);
			}

			int playerWhoMoved = getGroupPositionByPlayerID(playerID);

			Player player = getPlayerMap().get(playerID);

			if(player.hasCrashed() || !player.isParticipating()){
				return;
			}

			Point2D collisionPointFromPlayer = lobbyMap.get(sessionID).makeDefaultMoveForPlayer(player);

			Point2D playerPosition = player.getCurrentPosition();

			startNextRoundByPlayerID(playerID);

			int nextPlayerToMove = getNextPlayerToMove(playerID);

			int playerWhoWon = getPlayerWhoWonByPlayerID(playerID); 

			boolean didAPlayerWin = playerWhoWon != -1;

			int round = getPlayerRoundByPlayerID(playerID);

			Point2D playerVelocity = getCurrentPlayerVelocityByPlayerID(playerID);

			if(didAPlayerWin){
				DebugOutputHandler.printDebug("Player "+playerWhoWon+" did win the game.");

				answer = VectorMessageServerHandler.generatePlayerWonMessage(playerWhoMoved,playerWhoWon, playerPosition);
				closeGameByPlayerID(playerID);

			}else{
				answer = VectorMessageServerHandler.generateBroadcastMoveMessage(playerWhoMoved,playerPosition,
						collisionPointFromPlayer,nextPlayerToMove, playerVelocity, round);
			}

			for(int p : playersInSameSession)
				answer.addClientID(p);	
			//method on lobby and game which just moves the player by his velocity again! not just by a specific point
		}		

		sendMessage(answer);
	}

	/*
	 * Private Methods
	 */

	/**
	 * Checks if the lobby is empty and deltes it from the list if necessary
	 * @param lobbyID
	 */
	private void deleteEmptyLobbies(int lobbyID){
		DebugOutputHandler.printDebug("Checking for an empty Lobby...");
		if(lobbyID == NO_LOBBY_ID)
			return;
		Lobby l = lobbyMap.get(lobbyID);
		Player[] playersInLobby = l.getPlayerList();
		boolean deleteLobby = true;
		for(int i = 0; i < playersInLobby.length; i++){
			if(playersInLobby[i] != null ){
				if(!playersInLobby[i].isDummyPlayer())
					deleteLobby = false;
			}
		}
		if(deleteLobby){
			DebugOutputHandler.printDebug("Found empty Lobby, and deleting it");
			lobbyMap.remove(lobbyID);
		}

	}

	/**
	 * Creates a new LobbyObject, adds it to the list and sets the lobbyname
	 * @param maxPlayers
	 * @param lobbyName
	 * @param trackID
	 * @return
	 */
	private int createAndAddLobby(int maxPlayers, String lobbyName, int trackID){

		DebugOutputHandler.printDebug("A new Lobby has been created");
		Lobby newLobby = new Lobby(++lobbyIdCounter, maxPlayers, lobbyName, trackID, this);
		if(lobbyMap.get(lobbyIdCounter) == null){
			lobbyMap.put(lobbyIdCounter,  newLobby);
			return lobbyIdCounter;
		}else{
			return -1;
		}
	}

	/**
	 * sets the parameters of the lobby, according to the player which has created the lobby
	 * @param lobbyInformation the parameters for the new lobby
	 */
	private void setLobbyParameters(int sessionID, ILobbyInformation lobbyInformation){

		if(lobbyMap.get(sessionID) != null && lobbyInformation != null)
			lobbyMap.get(sessionID).updateInformation(lobbyInformation);
	}


	/**
	 * returns the session id from a player
	 * we need this method, since in some other methods, only the clientID is given,
	 * but sessioID is asked, without the need to create a Player instance
	 * @param playerID
	 * @return players sessionID
	 */
	private int getSessionFromClient(int playerID){

		int sessionIDSearched = -1;

		for (Player p : getPlayerMap().values()) {
			if(p.getPlayerID() == playerID){
				sessionIDSearched = p.getSessionID();
				break;
			}
		}

		return sessionIDSearched;

	}

	/**
	 * Checks if there is a player with name @param clientName on the server.
	 * if there is, the clientname gets his id added at the end
	 * @return the playerName he will have
	 */
	private String  getNextFreePlayerName(String clientName, int playerID){
		String realPlayerName = clientName;

		if( !isPlayerNameFreeToUse(clientName)){ 
			realPlayerName += playerID;
		}
		return realPlayerName;
	}

	/**
	 * checks if the name is alreay in Use
	 * @param clientName
	 * @return
	 */
	private boolean isPlayerNameFreeToUse(String clientName){
		for (Player p : getPlayerMap().values()) {
			if(p.getName().equals(clientName)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Resets the fields of a player after a game finished, or a player leaves the game.
	 * @param playerID
	 */
	private void resetPlayer(int playerID) {

		if(getPlayerMap().get(playerID) != null){
			getPlayerMap().get(playerID).setParticipating(false);
			getPlayerMap().get(playerID).setCrashed(false);
			getPlayerMap().get(playerID).setCurrentPosition(null);
			getPlayerMap().get(playerID).setCurrentVelocity(null);
		}
	}

	/**
	 * generates a message object beeing sent to every client in a specific session informing them, that one player disconnects
	 * @param playerToDisconnect the player which disconnected
	 * @param session the session in which the player dropped
	 * @return RaceTrackMessage containing information about the disconnected player, and the next player to move
	 */
	private RaceTrackMessage generateDisconnectMessage(int playerToDisconnect,int session, List<Integer> playersInSameSession){
		RaceTrackMessage answer = null;
		DebugOutputHandler.printDebug("Player: "+ playerToDisconnect+" disconnected, sending now a message to each client");

		if(session != NO_LOBBY_ID && lobbyMap.get(session)!=null){
			//			lobbyMap.get(session).playerDisconnected(playerToDisconnect);
			int nextPlayer = lobbyMap.get(session).getNextPlayer();
			answer = new DisconnectMessage(playerToDisconnect, nextPlayer);

			for(int i : playersInSameSession){
				answer.addClientID(i);
			}	

		}

		return answer;
	}

	/**
	 * Generates a WinnerMessage and sends that message to the controler.
	 * @param playerDisconnectsMessage
	 */
	private void sendWinnerMessage(RaceTrackMessage playerDisconnectsMessage) {
		if(playerDisconnectsMessage != null)
			try{

				int playerWhoWon = getPlayerWhoWonByPlayerID(((DisconnectMessage) playerDisconnectsMessage).getNextPlayerToMove()); 
				int playerWhoWonID = getPlayerIDWhoWonByPlayerID(((DisconnectMessage) playerDisconnectsMessage).getNextPlayerToMove());
				if(playerWhoWon != -1){
					Point2D lastVec = null;
					if(getPlayerMap().get(playerWhoWonID) != null)
						lastVec = getPlayerMap().get(playerWhoWonID).getCurrentPosition();


					PlayerWonMessage answer;

					answer = VectorMessageServerHandler.generatePlayerWonMessage(playerWhoWon, playerWhoWon, lastVec);

					closeGameByPlayerID(playerWhoWon);

					answer.addClientID(playerWhoWonID);

					sendMessage(answer);
					
					//inform the AIs that game is over!!

				}
			}catch(ClassCastException e){

			}
	}

	/**
	 * Get a playerID from a player by his position in the game and his sessionID
	 * @param clientPosition
	 * @param sessionID
	 * @return
	 */
	private int getPlayerIDByGroupPosition(int clientPosition, int sessionID){
		if(sessionID != NO_LOBBY_ID && lobbyMap.get(sessionID) != null){
			return lobbyMap.get(sessionID).getPlayerIDByPosition(clientPosition);
		}
		return -1;
	}

	/**
	 * Sets a client to a random starting position from the current map and informs the other players about that move
	 * @param playerID
	 * @param sessionID
	 */
	private void setRandomStartingPoint(int playerID,int sessionID){


		List<Integer> playersInSameSession = null;

		if(getPlayerIdsInSameSession(playerID) != null){
			playersInSameSession = getPlayerIdsInSameSession(playerID);
		}

		Player player = getPlayerMap().get(playerID);

		if(player.hasCrashed() || !player.isParticipating()){
			return;
		}

		int trackID = lobbyMap.get(sessionID).getTrackId();

		Point2D randomStartingPoint = null;

		do{

			randomStartingPoint = TrackFactory.getRandomStartingPoint( trackID );

		}while(!checkValidityOfClientMove(playerID, randomStartingPoint));

		lobbyMap.get(sessionID).getCollisionPointFromGame(player, randomStartingPoint);

		startNextRoundByPlayerID(playerID);

		int round = getPlayerRoundByPlayerID(playerID);


		int playerWhoMoved = getGroupPositionByPlayerID(playerID);


		int nextPlayerToMove = getNextPlayerToMove(playerID);


		RaceTrackMessage answer = VectorMessageServerHandler.generateBroadcastMoveMessage(playerWhoMoved,randomStartingPoint,
				null,nextPlayerToMove, new Point2D(0,0), round);


		for(int p : playersInSameSession)
			answer.addClientID(p);	

		sendMessage(answer);
	}
	
	private void sendMessage(RaceTrackMessage message)
	{
//		List<Integer> idsToRemove = new ArrayList<Integer>();
//		List<Integer> receivers = message.getReceiverIds();
//		for (Integer i : receivers)
//		{
//			if (playerMap.get(i).isAI())
//			{
//				idsToRemove.add(i);
//			}				
//		}
//		for (Integer id : idsToRemove)
//		{
//			receivers.remove(id);
//		}
//		message.setReceiverIDs(receivers);
		controller.sendExtraMessage(message);		
	}
}
