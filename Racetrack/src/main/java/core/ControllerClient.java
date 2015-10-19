package src.main.java.core;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Point2D;
import src.main.java.com.ComClient;
import src.main.java.com.messages.BroadCastMoveMessage;
import src.main.java.com.messages.ChatMessage;
import src.main.java.com.messages.ClientNameMessage;
import src.main.java.com.messages.CreateLobbyMessage;
import src.main.java.com.messages.DenyLobbyEntryMessage;
import src.main.java.com.messages.DisconnectMessage;
import src.main.java.com.messages.InvalidMoveMessage;
import src.main.java.com.messages.LeaveLobbyMessage;
import src.main.java.com.messages.OkayMessage;
import src.main.java.com.messages.PlayerWonMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.RequestLobbyEntryMessage;
import src.main.java.com.messages.RequestLobbysMessage;
import src.main.java.com.messages.SendLobbysMessage;
import src.main.java.com.messages.SendOptionsMessage;
import src.main.java.com.messages.StartGameMessage;
import src.main.java.com.messages.TimeLeftMessage;
import src.main.java.com.messages.VectorMessage;
import src.main.java.com.messages.handler.IRaceTrackMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceiveBroadCastMoveClientHandler;
import src.main.java.com.messages.handler.client.ReceiveInvalidMoveMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceiveOkayMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceiveLobbysClientHandler;
import src.main.java.com.messages.handler.client.ReceiveOptionsClientHandler;
import src.main.java.com.messages.handler.client.ReceiveChatMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceiveDenyLobbyEntryMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceiveDisconnectMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceivePlayerWonMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceiveStartGameMessageClientHandler;
import src.main.java.com.messages.handler.client.ReceiveTimeLeftMessageClientHandler;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;

/**
 * This class is the interface between the Client and the Server.
 * It passes messages from the Client to the Server and receives Messages from the Server.
 * It creates the messages it has to send and evaluates incoming messages.
 * It informs the UI about each incoming message through Message Handlers.
 */
public class ControllerClient {

	private static ComClient com;
	private static MultiSceneBase communicationInterface;
	private static Boolean isConnected;
	private static String ipAdress;
	private static int port;
	
	/**
	 * Maps a raceTrack message type to its handler
	 */
	private static Map<Class <? extends RaceTrackMessage>,  IRaceTrackMessageClientHandler> map;
	
	/**
	 * Creates the Controller Client which is responsible for being the Communication Interface
	 * between (Client UI + UserInput) and the (Server + Logic)
	 */
	public  ControllerClient(){
		com = new ComClient(this);
		isConnected = null;
		map = new HashMap<Class <? extends RaceTrackMessage>,  IRaceTrackMessageClientHandler>();
		initHandlers();
	}
	
	
	/**
	 * Get's the incoming message from the server and evaluates it.
	 * The information is written in the Model Class and the UI get's informed
	 * that something changed
	 * @param message	The incoming message from the server
	 */
	public void receiveMessage(RaceTrackMessage message){
		//receive the handler from the HashMap
		Racetracker.printInDebugMode("ControllerClient: Got a Message from the Server");
		IRaceTrackMessageClientHandler messageHandler = map.get(message.getClass());
		Racetracker.printInDebugMode(" of type " + message.getClass().toString());
		//do corresponding Action with the handler + inform UI about update
		messageHandler.updateModel(message);
		Racetracker.printInDebugMode("ControllerClient: Message progression ended.");
	}
	
	/**
	 * Creates all the handlers for the different types of messages
	 */
	private void initHandlers(){
		IRaceTrackMessageClientHandler okayMessageHandler = new ReceiveOkayMessageClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveLobbysHandler = new ReceiveLobbysClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler broadCastMoveHandler = new ReceiveBroadCastMoveClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveOptionsHandler = new ReceiveOptionsClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveChatMessageHandler = new ReceiveChatMessageClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receivePlayerWonMessageHandler = new ReceivePlayerWonMessageClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveStartGameMessageHandler = new ReceiveStartGameMessageClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveInvalidMoveMessageHandler = new ReceiveInvalidMoveMessageClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveDisconnectMessageHandler = new ReceiveDisconnectMessageClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveDenyLobbyEntryMessageHandler = new ReceiveDenyLobbyEntryMessageClientHandler(communicationInterface);
		IRaceTrackMessageClientHandler receiveTimeLeftMessageHandler = new ReceiveTimeLeftMessageClientHandler(communicationInterface);

		
		
		map.put(OkayMessage.class,  okayMessageHandler);
		map.put(SendLobbysMessage.class,  receiveLobbysHandler);
		map.put(BroadCastMoveMessage.class,  broadCastMoveHandler);
		map.put(SendOptionsMessage.class,  receiveOptionsHandler);
		map.put(ChatMessage.class,  receiveChatMessageHandler);
		map.put(PlayerWonMessage.class,  receivePlayerWonMessageHandler);
		map.put(StartGameMessage.class,  receiveStartGameMessageHandler);
		map.put(InvalidMoveMessage.class,  receiveInvalidMoveMessageHandler);
		map.put(DisconnectMessage.class, receiveDisconnectMessageHandler);
		map.put(DenyLobbyEntryMessage.class, receiveDenyLobbyEntryMessageHandler);
		map.put(TimeLeftMessage.class, receiveTimeLeftMessageHandler);
		
	}
	
	/**
	 * Initializes the Connection to the given ip:port
	 * @param ip	The IP of the server
	 * @param port	The port the server listens at
	 */
	public void startConnection(String ip, int port) {
		ControllerClient.ipAdress = ip;
		ControllerClient.port = port;
		this.connectToServer();
	}
	
	/**
	 * @param isConnected determine if it has connected successfully or not
	 */
	public void hasConnectedSuccesfully(boolean isConnected){
		ControllerClient.isConnected = isConnected;
	}
	
	/**
	 * Checks if the connection was successful
	 * @return	true for a successful connection, false for a failed connection
	 * 			and null if the connection try is still ongoing
	 */
	public Boolean hasConnectedToServer() {
		return isConnected;
	}
	
	/**
	 * Updates the scene for the controller client, so it can continue to interact
	 * with the UI in case of incoming changes from the server
	 * @param currentScene	The Scene which is displayed and active
	 */
	public void setInterface(MultiSceneBase currentScene) {
		communicationInterface = currentScene;
		
		map.get(OkayMessage.class).setRTMultiSceneBase(currentScene);
		map.get(SendLobbysMessage.class).setRTMultiSceneBase(currentScene);
		map.get(BroadCastMoveMessage.class).setRTMultiSceneBase(currentScene);
		map.get(SendOptionsMessage.class).setRTMultiSceneBase(currentScene);
		map.get(ChatMessage.class).setRTMultiSceneBase(currentScene);
		map.get(PlayerWonMessage.class).setRTMultiSceneBase(currentScene);
		map.get(StartGameMessage.class).setRTMultiSceneBase(currentScene);
		map.get(DisconnectMessage.class).setRTMultiSceneBase(currentScene);
		map.get(DenyLobbyEntryMessage.class).setRTMultiSceneBase(currentScene);
		map.get(TimeLeftMessage.class).setRTMultiSceneBase(currentScene);
		
	}
	
	/**
	 * Initializes the Connection to the current IP:Port through creating an ComClient instance
	 */
	private void connectToServer(){
		com.connectToServer(ControllerClient.ipAdress, ControllerClient.port);
	}
	
	/**
	 * Sends a message to server
	 * @param message The RaceTrackMessage
	 */
	private void sendMessage(RaceTrackMessage message, String messageType){
		//if there is an answer, which needs to be sent to the client, send it
		if(message != null && isConnected)
			com.sendMessage(message);
		Racetracker.printInDebugMode("ControllerClient: Sent message of type " + messageType + " to the server");
	}
	
	/**
	 * Sends a ClientNameMessage to the server which has to hold the following parameters
	 * @param userName	The name of the Player
	 */
	public void sendUserNameMessage(String userName){
		RaceTrackMessage rtMessage = new ClientNameMessage(userName);
		sendMessage(rtMessage , "UserNameMessage");
	}
	
	/**
	 * Sends a SendOptionsMessage to the server which has to hold the following parameters
	 * @param lobbyInformation LobbyInformation Class which contains the Lobby with all the changes the user did.
	 */
	public void sendOptionsMessage(ILobbyInformation lobbyInformation){
		RaceTrackMessage rtMessage = new SendOptionsMessage(lobbyInformation);
		sendMessage(rtMessage, "OptionsMessage");
	}
	
	/**
	 * Sends a CreateLobbyMessage to the server which creates a new lobby and which has to hold
	 * the following parameters
	 * @param lobbyInformation Object which contains the Lobby that the user wants to create
	 */
	public void sendCreateLobbyMessage(ILobbyInformation lobbyInformation){
		RaceTrackMessage rtMessage = new CreateLobbyMessage(lobbyInformation);
		if(ModelExchange.LobbyList.getRequestedLobbyToJoinID() == null)
			ModelExchange.LobbyList.setRequestedLobbyToJoinID(-1);
		sendMessage(rtMessage, "CreateLobbyMessage");
	}
	
	/**
	 * Sends a RequestLobbysMessage
	 */
	public void sendRequestAllLobbiesMessage(){
		RaceTrackMessage rtMessage = new RequestLobbysMessage();
		sendMessage(rtMessage, "RequestAllLobbiesMessage");
	}
	
	/**
	 * Sends a RequestLobbyEntryMessage to  the server which will ask for joining a specific lobby
	 */
	public void sendRequestLobbyEntranceMessage(){
		RaceTrackMessage rtMessage = new RequestLobbyEntryMessage(ModelExchange.LobbyList.getRequestedLobbyToJoinID());
		sendMessage(rtMessage, "RequestLobbyEntryMessage");
	}
	
	/**
	 * Sends the move to the server
	 * @param point Contains the information of the move
	 */
	public void sendVectorMessage(Point2D point){
		RaceTrackMessage rtMessage = new VectorMessage(point);
		sendMessage(rtMessage, "VectorMessage");
	}
	
	/**
	 * Sends a leaveLobbyMessage
	 */
	public void sendLeaveLobbyMessage() {
		RaceTrackMessage rtMessage = new LeaveLobbyMessage();
		sendMessage(rtMessage, "LeaveLobbyMessage");
	}
	
	/**
	 * Sends a chat message
	 * @param chat The chat message
	 */
	public void sendChatMessage(String chat){
		RaceTrackMessage rtMessage = new ChatMessage(chat, null);
		sendMessage(rtMessage, "ChatMessage");
	}
	
	/**
	 * Sends a disconnect message - this is used, when leaving the lobby browser and returning
	 * to the options screen. Also kills the connection.
	 */
	public void sendDisconnectMessage(){
		RaceTrackMessage rtMessage = new DisconnectMessage();
		sendMessage(rtMessage, "DisconnectMessage");
		Racetracker.printInDebugMode("ControllerClient: Disconnected succesfully!");
	}
	
	/**
	 * kills the connection thread
	 */
	public void killConnection(){
		com.killComClient();
	}
	
	/**
	 * Used to inform UI that connection
	 * to server is lost.
	 */
	public void lostConnectionToServer(){
		communicationInterface.disconnectedFromHost();
	}
	
}