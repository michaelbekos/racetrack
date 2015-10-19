package src.main.java.core;

import javafx.geometry.Point2D;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ILobbyInformation;

public interface IControllerClient {

	/**
	 * Get's the incoming message from the server and evaluates it.
	 * The information is written in the Model Class and the UI get's informed
	 * that something changed
	 * @param message	The incoming message from the server
	 */
	public void receiveMessage(RaceTrackMessage message);
	
	/**
	 * Initializes the Connection to the given ip:port
	 * @param ip	The IP of the server
	 * @param port	The port the server listens at
	 */
	public void startConnection(String ip, int port);
	
	/**
	 * @param isConnected determine if it has connected successfully or not
	 */
	public void hasConnectedSuccesfully(boolean isConnected);
	
	/**
	 * Updates the scene for the controller client, so it can continue to interact
	 * with the UI in case of incoming changes from the server
	 * @param currentScene	The Scene which is displayed and active
	 */
	public void setInterface(MultiSceneBase currentScene);
	
	/**
	 * Checks if the connection was successful
	 * @return	true for a successful connection, false for a failed connection
	 * 			and null if the connection try is still ongoing
	 */
	public Boolean hasConnectedToServer();
	
	/**
	 * Sends a ClientNameMessage to the server which has to hold the following parameters
	 * @param userName	The name of the Player
	 */
	public void sendUserNameMessage(String userName);
	
	/**
	 * Sends a SendOptionsMessage to the server which has to hold the following parameters
	 * @param LobbyInformation Class which contains the Lobby with all the changes the user did.
	 */
	public void sendOptionsMessage(ILobbyInformation lobbyInformation);
	
	/**
	 * Sends a CreateLobbyMessage to the server which creates a new lobby and which has to hold
	 * the following parameters
	 * @param lobbyInformation Object which contains the Lobby that the user wants to create
	 */
	public void sendCreateLobbyMessage(ILobbyInformation lobbyInformation);
	
	/**
	 * Sends a RequestLobbysMessage
	 */
	public void sendRequestAllLobbiesMessage();
	
	/**
	 * Sends a RequestLobbyEntryMessage to  the server which will ask for joining a specific lobby
	 * The LobbyID for the requested Lobby will be taken from ModelExchange
	 */
	public void sendRequestLobbyEntranceMessage();
	
	/**
	 * Sends the move to the server
	 * @param point Contains the information of the move
	 */
	public void sendVectorMessage(Point2D point);
	
}
