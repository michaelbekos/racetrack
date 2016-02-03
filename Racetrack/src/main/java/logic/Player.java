package src.main.java.logic;

import java.util.ArrayList;

import src.main.java.gui.utils.Colors;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 * 
 * @author Sotirios Pavlidis
 */
public class Player
{
	private final Integer PLAYER_ID;
	protected Integer sessionID;
	private String name;
	private Point2D currentVelocity;
	private Point2D currentPosition;
	private boolean isHost;
	private boolean isParticipating;
	private boolean hasToWait = false;
	private ArrayList<Point2D> memorizeGridPoints = new ArrayList<Point2D>();
	private boolean crashed;
	private boolean isDisconnected = false;

	private int colorId;
	protected int mTypeID;
	
	private boolean isDummyPlayer;

	/*
	 * Constructor for players with a specific color or additionally a sessionID
	 */
	public Player(Integer playerID) {
		this(playerID, "Unknown");
	}

	public Player(Integer playerID, String name) {
		this(playerID, name, -1);
		hasToWait = false;
	}

	public Player(Integer playerID, String name, int playerColorId) {
		this(playerID, name, playerColorId, -1);
	}
	
	public Player(){
		name = "Disconnected";
		PLAYER_ID = -2;
		crashed = true;
		isDummyPlayer = true;
		isParticipating = false;
		hasToWait = false;
		mTypeID=1;
	}

	/**
	 * Initializing a new player.
	 * 
	 * @param playerID
	 *            Unique identifier given from the comServer
	 * @param name
	 *            A player name, choosen by a player itself on game launch.
	 * @param playerColorId
	 *            An ID representing 5 different colors from red to purple.
	 * @param sessionID
	 *            An ID for the server
	 * @param isAI
	 *            A property if it is an AI
	 */
	public Player(Integer playerID, String name, int playerColorId, int sessionID) {
		this.PLAYER_ID = playerID;
		this.name = name;
		this.colorId = playerColorId;
		this.sessionID = sessionID;
		this.crashed = false;
		hasToWait = false;
		mTypeID=1;
	}

	public Point2D getCurrentVelocity() {
		return currentVelocity;
	}

	public void setCurrentVelocity(Point2D currentVelocity) {
		this.currentVelocity = currentVelocity;
	}

	public Point2D getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Point2D currentPosition) {
		this.currentPosition = currentPosition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPlayerID() {
		return PLAYER_ID;
	}

	public Integer getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost() {
		this.isHost = true;
	}

	public int getTypeID()
	{// Assume this is a Human:
		return mTypeID;
	}

	public ArrayList<Point2D> getMemorizeGridPoints() {
		return memorizeGridPoints;
	}

	public void setMemorizeGridPoints(ArrayList<Point2D> memorizeGridPoints) {
		this.memorizeGridPoints = memorizeGridPoints;
	}
	
	public void addMemorizeGridPoint(Point2D gridPoint) {
		this.memorizeGridPoints.add(gridPoint);
	}

	public boolean isParticipating() {
		return isParticipating;
	}

	public void setParticipating(boolean isParticipating) {
		this.isParticipating = isParticipating;
	}

	/**
	 * stores all the positions of a player he has such that the gui can draw
	 * the lines
	 * 
	 * @param currentPosition
	 *            New point to add to a players trace.
	 */
	public void storeGridPoint(Point2D currentPosition) {
		memorizeGridPoints.add(currentPosition);
	}

	/**
	 * 
	 * @return A players color, used to draw a players circle or trace.
	 * 
	 * @author Tobias
	 */
	public Color getColor() {
		if (colorId < 0) {
			return Color.LIGHTGRAY;
		} else {
			return Colors.Player.getColorWithId(colorId);
		}
	}

	/**
	 * Set player color with ID
	 * 
	 * @param colorId
	 *            An ID representing 5 different colors from red to purple.
	 */
	public void setColor(int colorId) {
		this.colorId = colorId % Colors.Player.getColorsCount();
	}

	/**
	 * 
	 * @return Whether a player had crashed during a game.
	 */
	public boolean hasCrashed() {
		return crashed;
	}

	/**
	 * 
	 * @param crashed
	 *            Set to yes if player has crashed into a boundary or another
	 *            player.
	 */
	public void setHasCrashed(boolean crashed) {
		this.crashed = crashed;
	}

	public boolean isAI() {
		return false;
	}

	/**
	 * @return the pLAYER_ID
	 */
	public Integer getPLAYER_ID() {
		return PLAYER_ID;
	}

	/**
	 * @return the crashed
	 */
	public boolean isCrashed() {
		return crashed;
	}

	/**
	 * @return the colorId
	 */
	public int getColorId() {
		return colorId;
	}

	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID(Integer sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @param isHost the isHost to set
	 */
	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	/**
	 * @param crashed the crashed to set
	 */
	public void setCrashed(boolean crashed) {
		this.crashed = crashed;
	}

	/**
	 * @param colorId the colorId to set
	 */
	public void setColorId(int colorId) {
		this.colorId = colorId;
	}

	/**
	 * 
	 * @return is the player disconnected?
	 */
	public boolean isDisconnected() {
		return isDisconnected;
	}

	/**
	 * @param isDisconnected set the player connection status
	 */
	public void setDisconnected(boolean isDisconnected) {
		this.isDisconnected = isDisconnected;
	}
	
	public boolean isDummyPlayer(){
		return isDummyPlayer;
	}
	
	public boolean HasToWait()
	{
		return hasToWait;
	}
	
	public void AddPenaltyWait()
	{
		hasToWait = true;
	}
	
	public void WaitAsPenalty()
	{
		hasToWait = false;
	}
}
