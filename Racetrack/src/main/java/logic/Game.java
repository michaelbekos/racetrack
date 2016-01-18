package src.main.java.logic;

import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.utils.GameState;
import javafx.geometry.Point2D;

public class Game {

	private Lobby inGameLobby;
	private Track track;
	private Player[][] RTField;
	private Player[] playerList;
	private int currentPlayerIndex;
	private int lastPlayerIndex;
	private boolean gameIsRunning;
	private boolean firstRound;
	private GameState gameState;
	private int roundCounter;
	

	/**
	 * @return the inGameLobby
	 */
	public Lobby getInGameLobby() {
		return inGameLobby;
	}

	/**
	 * @return the playerList
	 */
	public Player[] getPlayerList() {
		return playerList;
	}

	/**
	 * @return the gameIsRunning
	 */
	public boolean isGameIsRunning() {
		return gameIsRunning;
	}

	/**
	 * @return the gameState
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * @param inGameLobby
	 *            the inGameLobby to set
	 */
	public void setInGameLobby(Lobby inGameLobby) {
		this.inGameLobby = inGameLobby;
	}

	/**
	 * @param track
	 *            the track to set
	 */
	public void setTrack(Track track) {
		this.track = track;
	}

	/**
	 * @param rTField
	 *            the rTField to set
	 */
	public void setRTField(Player[][] rTField) {
		RTField = rTField;
	}

	/**
	 * @param playerList
	 *            the playerList to set
	 */
	public void setPlayerList(Player[] playerList) {
		this.playerList = playerList;
	}

	/**
	 * @param lastPlayerIndex
	 *            the lastPlayerIndex to set
	 */
	public void setLastPlayerIndex(int lastPlayerIndex) {
		this.lastPlayerIndex = lastPlayerIndex;
	}

	/**
	 * @param gameIsRunning
	 *            the gameIsRunning to set
	 */
	public void setGameIsRunning(boolean gameIsRunning) {
		this.gameIsRunning = gameIsRunning;
	}

	/**
	 * @param firstRound
	 *            the firstRound to set
	 */
	public void setFirstRound(boolean firstRound) {
		this.firstRound = firstRound;
	}

	/**
	 * @param gameState
	 *            the gameState to set
	 */
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	/**
	 * @param roundCounter
	 *            the roundCounter to set
	 */
	public void setRoundCounter(int roundCounter) {
		this.roundCounter = roundCounter;
	}

	/**
	 * Game initilization
	 * 
	 * @param track
	 *            A user selected track
	 * @param playerList
	 *            An array of players
	 * @param inGameLobby
	 *            An in-game-lobby
	 */
	public Game(Track track, Player[] playerList, Lobby inGameLobby) {
		this.track = track;
		this.RTField = new Player[(int) track.getDimension().getX()][(int) track
		                                                             .getDimension().getY()];
		this.playerList = playerList;
		this.lastPlayerIndex = -1;
		this.currentPlayerIndex = getFirstPlayerToMove();
		this.inGameLobby = inGameLobby;
		this.gameIsRunning = true;
		this.firstRound = true;
		this.gameState = GameState.FIRST_PLAYER_START_VECTOR;
		for (int i = 0; i < playerList.length; i++) {
			if (playerList[i] != null) {
				playerList[i].setParticipating(true);
				playerList[i].setHasCrashed(false);
				playerList[i].setCurrentVelocity(new Point2D(0.0d, 0.0d));
				if(playerList[i].isAI())
					((AI)playerList[i]).setGameInformation(this);
			}
		}
		this.roundCounter = 0;
	}

	public Game(ILobbyInformation lobby) {
		Player[] players = new Player[lobby.getPlayerNames().length];
		for (int i = 0; i < players.length; i++) {
			if (lobby.getPlayerIDs()[i] != null)
				players[i] = new Player(lobby.getPlayerIDs()[i],
						lobby.getPlayerNames()[i], i);
			else
				players[i] = new Player(null, lobby.getPlayerNames()[i], i);
		}

		this.track = TrackFactory.getSampleTrack(lobby.getTrackId());
		this.RTField = new Player[(int) track.getDimension().getX()][(int) track
		                                                             .getDimension().getY()];
		this.playerList = players;
		this.lastPlayerIndex = -1;
		this.currentPlayerIndex = getFirstPlayerToMove();
		this.inGameLobby = null;
		this.gameIsRunning = true;
		this.firstRound = true;
		this.gameState = GameState.FIRST_PLAYER_START_VECTOR;
		this.roundCounter = 0;
	}

	public Track getTrack() {
		return this.track;
	}

	public Player[] getPlayers() {
		return this.playerList;
	}

	public boolean isFirstRound() {
		return firstRound;
	}

	public int getRoundCounter() {
		return roundCounter;
	}

	/**
	 * This method calculates the new velocity of a player
	 * 
	 * @param player
	 *            the current player
	 * @param newPosition
	 *            the new position by a player on the game grid
	 * @return New calculated velocity for a player
	 * 
	 * @author Tobias
	 */
	private Point2D calculateNewVelocity(Player player, Point2D newPosition) {
		Point2D oldPosition = player.getCurrentPosition();
		Point2D newVelocity = newPosition.subtract(oldPosition);

		return newVelocity;
	}

	/**
	 * This method just decreases the currentPlayerIndex because of the first
	 * round
	 */
	private void nextFirstRound() {

		boolean checkIfParticipating = true;
		this.lastPlayerIndex = this.currentPlayerIndex;

		while (checkIfParticipating) {
			currentPlayerIndex = (currentPlayerIndex - 1);
			// this.gameState = GameState.values()[7 + currentPlayerIndex];
			if(currentPlayerIndex == -1){
				currentPlayerIndex = lastPlayerIndex;
				firstRound = false;
				break;
			}
			if (playerList[currentPlayerIndex].isParticipating() == true) {
				checkIfParticipating = false;
			}
		}

		//		this.lastPlayerIndex = this.currentPlayerIndex;
		//		this.currentPlayerIndex -= 1;
		//		this.gameState = GameState.values()[gameState.ordinal() + 1];
	}

	private int getFirstPlayerToMove() {
		for (int i = playerList.length - 1; i >= 0; i--) {
			if (playerList[i] != null)
				return i;
		}
		return 0;
	}

	/**
	 * This method increases the currentPlayerIndex by 1 every time, after a
	 * normal round. If the next player's not participating, he is being
	 * skipped.
	 */
	private void nextRound() {
		boolean checkIfParticipating = true;
		while (checkIfParticipating) {
			this.lastPlayerIndex = this.currentPlayerIndex;
			currentPlayerIndex = (currentPlayerIndex + 1) % playerList.length;
			// this.gameState = GameState.values()[7 + currentPlayerIndex];
			if (playerList[currentPlayerIndex].isParticipating() == true) {
				checkIfParticipating = false;
			}
		}
		if (isLastPlayerOfArray())
			roundCounter++;
	}

	/**
	 * Checks, if the player that left is
	 * the current player and initializes the next
	 * round, if so.
	 * @param playerDisconnted
	 */
	public void nextRoundByDisconnection(int playerDisconnted) {
		boolean checkIfParticipating = true;

		if(playerDisconnted == currentPlayerIndex){
			if(isFirstRound()){
				
				int firstParticipatingPlayerIndex = getFirstParticipatingPlayerIndex();
				
				DebugOutputHandler.printDebug(firstParticipatingPlayerIndex+" ");
				
				if (currentPlayerIndex <= firstParticipatingPlayerIndex) {
					currentPlayerIndex = getFirstParticipatingPlayerIndex(currentPlayerIndex);
					DebugOutputHandler.printDebug("### "+currentPlayerIndex);
					firstRound = false;
				} else {
					nextFirstRound();
				}
			}else{
				int counter = 0;
				while (checkIfParticipating) {
					this.lastPlayerIndex = this.currentPlayerIndex;
					currentPlayerIndex = (currentPlayerIndex + 1) % playerList.length;
					counter++;
					// this.gameState = GameState.values()[7 + currentPlayerIndex];
					if (playerList[currentPlayerIndex].isParticipating() == true || counter >= playerList.length*2) {
						checkIfParticipating = false;
					}
				}
				if (isLastPlayerOfArray())
					roundCounter++;
			}
		}

	}

	private boolean isLastPlayerOfArray() {

		if (currentPlayerIndex == playerList.length - 1)
			return true;
		else {
			for (int i = currentPlayerIndex + 1; i < playerList.length; i++) {
				if (playerList[i] != null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * takes a vector and checks if it is in the starting points
	 * 
	 * @param point
	 *            A point selected to be the starting point
	 * @return Whether the given point is one of the tracks starting points or
	 *         not.
	 */
	public boolean checkIfFirstPositionIsInStartingPoints(Point2D point) {
		for (int i = 0; i < track.getStartingPoints().length; i++) {
			if (track.getStartingPoints()[i].equals(point)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method looks whether there is a collision with the boundaries or not
	 * If there's a collision, then the collision point is being returned If
	 * there's no collision, the return is just null and
	 * currentVelocity/Position and the RTField are being updated
	 * 
	 * @param player
	 *            currentPlayer
	 * @param selectedPosition
	 *            the selected position by the player on the grid
	 * @return If there was a collision it returns a collision point - otherwise
	 *         it returns null.
	 * 
	 * @author Tobias
	 */
	public Point2D getCollisionPoint(Player player, Point2D selectedPosition) {
		if (gameIsRunning) {
			if (firstRound) {
				RTField[(int) selectedPosition.getX()][(int) selectedPosition
				                                       .getY()] = player;
				player.setCurrentPosition(selectedPosition);
				player.storeGridPoint(selectedPosition);
				return null;

			} else {

				Point2D oldPosition = player.getCurrentPosition();
				Line2D lastPlayerMoveLine = new Line2D(oldPosition,
						selectedPosition);

				// Inner boundary
				for (Line2D boundary : track.getInnerBoundary()) {
					if (java.awt.geom.Line2D.linesIntersect(
							lastPlayerMoveLine.getStartX(),
							lastPlayerMoveLine.getStartY(),
							lastPlayerMoveLine.getEndX(),
							lastPlayerMoveLine.getEndY(), boundary.getStartX(),
							boundary.getStartY(), boundary.getEndX(),
							boundary.getEndY())) {
						Point2D intersectionPoint = lastPlayerMoveLine
								.pointOfIntersection(boundary);
						System.out.println("IntersectionPoint: "
								+ intersectionPoint);
						//player.setParticipating(false);
						float xDirection;
						float yDirection;
						if (player.getCurrentPosition().getX() < intersectionPoint.getX())
						{
							xDirection = 1.0f;
						}
						else
						{
							xDirection = -1.0f;
						}
						if (player.getCurrentPosition().getY() < intersectionPoint.getY())
						{
							yDirection = 1.0f;
						}
						else
						{
							yDirection = -1.0f;
						}
						Point2D newPosition = new Point2D((int) intersectionPoint.getX() - xDirection, (int) intersectionPoint.getY() - yDirection);
						RTField[(int) oldPosition.getX()][(int) oldPosition
						                                  .getY()] = null;
						//player.setHasCrashed(true);
						player.AddPenaltyWait();
						player.setCurrentPosition(newPosition);
						player.setCurrentVelocity(new Point2D (0,0));
						player.storeGridPoint(newPosition);
						RTField[(int) newPosition.getX()][(int) newPosition.getY()] = player;

						return newPosition;

					}
				}

				// Outer boundary
				for (Line2D boundary : track.getOuterBoundary()) {
					if (java.awt.geom.Line2D.linesIntersect(
							lastPlayerMoveLine.getStartX(),
							lastPlayerMoveLine.getStartY(),
							lastPlayerMoveLine.getEndX(),
							lastPlayerMoveLine.getEndY(), boundary.getStartX(),
							boundary.getStartY(), boundary.getEndX(),
							boundary.getEndY())) {

						Point2D intersectionPoint = lastPlayerMoveLine
								.pointOfIntersection(boundary);
						System.out.println("IntersectionPoint: "
								+ intersectionPoint);
						//player.setParticipating(false);
						float xDirection;
						float yDirection;
						if (player.getCurrentPosition().getX() < intersectionPoint.getX())
						{
							xDirection = 1.0f;
						}
						else
						{
							xDirection = -1.0f;
						}
						if (player.getCurrentPosition().getY() < intersectionPoint.getY())
						{
							yDirection = 1.0f;
						}
						else
						{
							yDirection = -1.0f;
						}
						Point2D newPosition = new Point2D((int) intersectionPoint.getX() - xDirection, (int) intersectionPoint.getY() - yDirection);
						RTField[(int) oldPosition.getX()][(int) oldPosition
						                                  .getY()] = null;
						//player.setHasCrashed(true);
						player.AddPenaltyWait();
						player.setCurrentPosition(newPosition);
						player.setCurrentVelocity(new Point2D (0,0));
						player.storeGridPoint(newPosition);
						RTField[(int) newPosition.getX()][(int) newPosition.getY()] = player;

						return newPosition;
					}
				}

				setVelocityPositionAndUpdateField(player, selectedPosition);

				return null;
			}
		} else
			return null;
	}

	/**
	 * This method sets velocity, position and updates the gaming field
	 * according to a valid move of a player
	 * 
	 * @param player
	 *            the current player
	 * @param selectedPosition
	 *            the selected position by a player on the grid
	 */
	private void setVelocityPositionAndUpdateField(Player player,
			Point2D selectedPosition) {
		RTField[(int) player.getCurrentPosition().getX()][(int) player
		                                                  .getCurrentPosition().getY()] = null;

		player.setCurrentVelocity(calculateNewVelocity(player, selectedPosition));
		player.setCurrentPosition(selectedPosition);
		player.storeGridPoint(selectedPosition);

		// Don't let the player break out of the map
		int x = (int) selectedPosition.getX();
		int y = (int) selectedPosition.getY();

		// Check if Player breaks in X direction lower
		if(x < 0)
			x = 0;
		else
			// Check if Player breaks in X direction upper
			if(x >= RTField.length)
				x = RTField.length - 1;

		// Check if Player breaks in Y direction lower
		if(y < 0)
			y = 0;
		else
			// Check if Player breaks in Y direction upper
			if(y >= RTField[0].length)
				y = RTField[0].length - 1;

		RTField[x][y] = player;

	}

	/**
	 * This method checks weather a given player can perform a move or not. For
	 * performing a move, the game has to be running and the player needs to
	 * participate and be the current player in the game, the given velocity
	 * change has to be valid and on the new calculated position shouldn't be
	 * another player.
	 * 
	 * @param player
	 *            A player, who should participate and the current player in the
	 *            game
	 * @param selectedPosition
	 *            A velocity change Point2D object; x,y in (-1,0,1)
	 * @return Whether the player is able to change it's position and the
	 *         velocity change is valid as well as the player do not collide
	 *         with another player.
	 * 
	 * @author Tobias
	 */
	public boolean isValid(Player player, Point2D selectedPosition) {
		if (gameIsRunning) {
			// If player is current player
			if ((player == playerList[currentPlayerIndex] &&
					// If player is still participating or already crashed
					player.isParticipating())
					&&
					// Check if velocity change is between -1 to 1
					(checkIfValidVelocityChange(selectedPosition)) &&
					// If player will crash into another player
					(crashIntoOtherPlayer(selectedPosition))) {
				return true;
			}
		}

		return false;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public int getLastPlayerIndex() {
		return this.lastPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		if (currentPlayerIndex != this.currentPlayerIndex) {
			this.lastPlayerIndex = this.currentPlayerIndex;
		}
		this.currentPlayerIndex = currentPlayerIndex;
	}

	/**
	 * This method checks if there's a car crash or not
	 * 
	 * @param gridPosition
	 *            selected position by the player on the grid
	 * @return true if there's no crash, false if there's crash.
	 */
	boolean crashIntoOtherPlayer(Point2D gridPosition) {
		if (gridPosition.getX() >= track.getDimension().getX()
				|| gridPosition.getY() >= track.getDimension().getY()
				|| gridPosition.getX() < 0 || gridPosition.getY() < 0) {
			return true;
		} else {
			return RTField[(int) gridPosition.getX()][(int) gridPosition.getY()] == null
					|| RTField[(int) gridPosition.getX()][(int) gridPosition
					                                      .getY()] == playerList[currentPlayerIndex];
		}
	}

	/**
	 * This method returns true if the vector given is one of the starting
	 * points or the selected position by the player is a valid velocity change
	 * in [-1, 0, 1]
	 * 
	 * @param gridPosition
	 *            a player's selected position on the grid
	 * @return If a vector is on valid velocity change vector or a starting
	 *         position
	 */
	private boolean checkIfValidVelocityChange(Point2D gridPosition) {
		if (firstRound) {
			return checkIfFirstPositionIsInStartingPoints(gridPosition);
		}

		else {

			Point2D currentPosition = playerList[currentPlayerIndex]
					.getCurrentPosition();
			Point2D currentVelocity = playerList[currentPlayerIndex]
					.getCurrentVelocity();
			Point2D velocityChangeValue = gridPosition
					.subtract(currentPosition);

			double normalChangeX = velocityChangeValue.getX()
					- currentVelocity.getX();
			double normalChangeY = velocityChangeValue.getY()
					- currentVelocity.getY();

			if (normalChangeX == -1.0d || normalChangeX == 0.0d
					|| normalChangeX == 1.0d) {
				if (normalChangeY == -1.0d || normalChangeY == 0.0d
						|| normalChangeY == 1.0d) {
					return true;
				} else
					return false;
			} else {
				return false;
			}

		}
	}

	/**
	 * This method is the last method called in a turn it is also responsible
	 * for handling the currentPlayerIndex
	 * 
	 * @param player
	 *            The current player
	 * @return True if the player just crossed the finish line and false if not.
	 */
	public boolean hasPlayerWon(Player player) {
		if (gameIsRunning && !firstRound && !player.hasCrashed()) {
			try {
				int lastPositionInMemorizeGridPoint = player
						.getMemorizeGridPoints().size() - 2;
				Point2D lastPosition = player.getMemorizeGridPoints().get(
						lastPositionInMemorizeGridPoint);
				Line2D lastMove = new Line2D(lastPosition,
						player.getCurrentPosition());

				if (java.awt.geom.Line2D.linesIntersect(track.getFinishLine()
						.getStartX(), track.getFinishLine().getStartY(), track
						.getFinishLine().getEndX(), track.getFinishLine()
						.getEndY(), lastMove.getStartX(), lastMove.getStartY(),
						lastMove.getEndX(), lastMove.getEndY()))
					return true;
				else
					return false;
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

		} else
			return false;
	}

	public Player[][] getRTField() {
		return RTField;
	}

	/**
	 * 
	 * @return The next playerID
	 */
	public int getNextPlayer() {
		if (playerList[currentPlayerIndex].HasToWait())
		{
			playerList[currentPlayerIndex].WaitAsPenalty();
			currentPlayerIndex = (currentPlayerIndex + 1) % playerList.length;
		}
		return currentPlayerIndex;
	}

	protected void calculateNextRound() {

		if (firstRound) {

			int firstParticipatingPlayerIndex = getFirstParticipatingPlayerIndex();

			if (currentPlayerIndex == firstParticipatingPlayerIndex) {
				firstRound = false;
			} else {
				nextFirstRound();
			}
		}

		else {
			nextRound();
		}
		
		if (playerList[currentPlayerIndex].isAI())
		{
			long start_time=System.currentTimeMillis();
			javafx.geometry.Point2D point = ((IAI)playerList[currentPlayerIndex]).move();
			while (!inGameLobby.getAdministration().checkValidityOfClientMove(playerList[currentPlayerIndex].getPlayerID(), point))
			{
				point = ((IAI)playerList[currentPlayerIndex]).move();
			}
			long time_needed=start_time-System.currentTimeMillis();
			if( time_needed > 1500 )
			{
				try {
					synchronized(this) {
				        this.wait( 1500-time_needed );
				      }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			inGameLobby.getAdministration().moveAI(playerList[currentPlayerIndex].getPlayerID(), point);
		}
	}

	/**
	 * this method checks if every player in the game has crashed, also
	 * responsible of setting the currentPlayerIndex
	 * 
	 * @return true if everyone has crashed
	 */
	public boolean hasEveryPlayerCrashed() {
		boolean check;
		for (int i = 0; i < playerList.length; i++) {
			if (!playerList[i].hasCrashed()) {
				check = false;
				return check;
			}
		}
		check = true;
		return check;
	}

	/**
	 * this method returns if all players have crashed and the last one is the
	 * only one left on the grid
	 * 
	 * @return true if next player is the last one
	 */
	public boolean isNextPlayerLastPlayer() {
		boolean[] checkIfAllOtherPlayersHaveCrashed = new boolean[playerList.length - 1];

		for (int i = 0; i < playerList.length; i++) {
			if (i < currentPlayerIndex) {
				checkIfAllOtherPlayersHaveCrashed[i] = playerList[i]
						.hasCrashed();
			} else if (i > currentPlayerIndex) {
				checkIfAllOtherPlayersHaveCrashed[i] = playerList[i]
						.hasCrashed();
			}
		}

		for (int i = 0; i < checkIfAllOtherPlayersHaveCrashed.length; i++) {
			if (checkIfAllOtherPlayersHaveCrashed[i] == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @return Returns the winner or null if there is none.
	 */
	protected Integer getWinner() {
		if(playerList != null)
			for(Integer i = 0; i < playerList.length; i++)
				if(playerList[i] != null)
					if (hasPlayerWon(playerList[i]))
						return i;

		int counter = 0;
		int winner = -1;
		if(playerList != null)
			for(Integer i = 0; i < playerList.length; i++)
				if(playerList[i] != null)
					if(!playerList[i].hasCrashed()){
						counter++;
						winner = i;
					}
		if(counter == 1)
			return winner;

		return null;
	}

	/**
	 * 
	 * @return Returns the winner or null if there is none.
	 */
	public Integer getWinnerID() {
		if(playerList != null)
			for(Integer i = 0; i < playerList.length; i++)
				if(playerList[i] != null)
					if (hasPlayerWon(playerList[i]))
						return playerList[i].getPLAYER_ID();

		int counter = 0;
		int winner = -1;
		if(playerList != null)
			for(Integer i = 0; i < playerList.length; i++)
				if(playerList[i] != null)
					if(!playerList[i].hasCrashed()){
						counter++;
						winner = i;
					}
		if(counter == 1)
			return playerList[winner].getPLAYER_ID();

		return null;
	}

	private int  getFirstParticipatingPlayerIndex(){
		if(playerList!=null)
			for(int i = 0; i < playerList.length; i++){
				if(playerList[i] != null && !playerList[i].isDummyPlayer() && !playerList[i].isCrashed())
					return i;
			}
		return -1;
	}
	
	private int  getFirstParticipatingPlayerIndex(int index){
		if(playerList!=null)
			for(int i = 0; i < playerList.length; i++){
				if(playerList[i] != null && !playerList[i].isDummyPlayer() && !playerList[i].isCrashed() && i != index)
					return i;
			}
		return -1;
	}
}
