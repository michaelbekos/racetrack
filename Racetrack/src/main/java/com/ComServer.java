package src.main.java.com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import src.main.java.com.messages.DisconnectMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.core.DebugOutputHandler;
import src.main.java.core.IControllerServer;

/**
 * ComServer handles new clients which are connecting to the server,
 * gives them a unique ID,and sends that id to the controller.
 * The class sends RaceTrackMessage objects over the network to specific clients depending
 * on their id.
 * ComServer and the Server controller know each other to be able to send and recieve 
 * RacetrackMessages.
 * ComServer uses the following public methods by the Server Controller:
 * recieveMessage
 * clientConnected
 * clientDisconnected
 * 
 * 
 * The Client listener and handler are nested classes since no other class needs to know of their existence.
 * Data encapsulation etc.
 * @author Denis
 */
public class ComServer implements ICommunication{

	//Private fields
	/**
	 * after receiving messages, we store them in a arrayList,
	 * only if the controller has processed the last message, comServer gives him a new message to process
	 */
	private static List<RaceTrackMessage> messageBuffer;

	/**
	 * a list that safes, how many clients are connected, and which client id should
	 * be given to the next client
	 */
	private static List<Integer> clientIds;

	/**
	 * determines which id a client will get, when he connects to the server.
	 */
	private static int nextClientId;

	/**
	 * an instance of the administrating controller on the server side.
	 * ComServer propagates RacetrackMessages to this controller
	 * and gets the apropriate answers
	 */
	private IControllerServer controller;

	/**
	 * determines over which port the communication will be held.
	 * 
	 */
	private int port;

	/**
	 * matches an object output stream to each id of a  connected client
	 * Integer - clientID
	 * ObjectOutputStream - outputstream on the socket.
	 */
	private HashMap<Integer, ObjectOutputStream> objectOutputStreams;

	/**
	 * matches the player ID to its corresponding socket
	 */
	private HashMap<Integer, Socket> playerSocketMap;

	/**
	 * determines, if the comServer is processing one of the client messages at the moment
	 */
	private boolean isProcessing;

	/**
	 * Constructor initializes fields and starts the connection handler which listens for 
	 * new connections
	 * @param port binds the socket to the specific port
	 * @param controller the administrating controller of the server
	 */
	public ComServer(int port, IControllerServer controller){

		//initialize fields
		this.controller = controller;

		playerSocketMap = new HashMap<Integer, Socket>();
		objectOutputStreams = new HashMap<Integer, ObjectOutputStream>();
		nextClientId = objectOutputStreams.size();
		clientIds = new ArrayList<Integer>();
		messageBuffer = new ArrayList<RaceTrackMessage>();
		this.port = port;
		//start connection handler
		startConnectionHandler();
		this.isProcessing = false;
	}

	//public methods

	/**
	 * sends a message object to each client written in the messages ArrayList of receiver IDs
	 */
	@Override
	public void sendMessage(RaceTrackMessage message) {

		List<Integer> receiverIDs = message.getReceiverIds();

		for(int clientId : receiverIDs){

			try{
				DebugOutputHandler.printDebug("Send an answer to Player "+clientId);
				if(objectOutputStreams.get(clientId) != null){
					objectOutputStreams.get(clientId).writeObject(message);
					//				objectOutputStreams.get(clientId).flush();
					objectOutputStreams.get(clientId).reset();
				}
			}catch(IOException e){
//				e.printStackTrace();
			}

		}
	}

	/**
	 * This Method removes the player from the hashMaps, he is not yet removed from after a disconnection of his socket
	 * @param playerID the Player ID of the disconnecting player
	 */
	public void disconnectPlayer(int playerID){

		if( playerSocketMap.get(playerID) != null){

			playerSocketMap.remove(playerID);

		}
		DebugOutputHandler.printDebug("Player with ID "+playerID+ " disconnected! ");
	}
	//private methods

	/**
	 * starts the connection handler to listen on a specific port
	 * @throws IOException 
	 */
	private void startConnectionHandler(){

		new ListenerClientConnections(port).start();

	}

	/**
	 * checks the next free Id and increments it by one
	 * This method has to be synchronized to prevent problems with same ID clients
	 * if two clients connect at the exact same time. Otherwise those clients would get the 
	 * same id
	 * @return next free id as Integer
	 */
	public synchronized int getNextId(){
		return nextClientId++;
	}

	/**
	 * This Method processes through the message buffer.
	 * Every incoming message will  be stored in the messageBuffer. 
	 * After a new message has been recieved, ComServer checks if he is already processing the messages or not.
	 * If it is not processing the messages, it will loop through all the messages and send them to the controller, 
	 * letting him process it, generate an answer and send it back to the client
	 * 
	 * If it is already processing some messages, the incoming message will be appended to the messageBuffer
	 */
	private void processMessages(){
		//while the ComServer is processing messages, dont process any more messages
		this.isProcessing = true;

		//while there are still messages, process the next one and delete the old one
		while(messageBuffer.size()>0){
			//give the controller a message of the buffer and remove it when the controler is done processing it
			controller.receiveMessage(messageBuffer.get(0));
			messageBuffer.remove(0);
		}
		//if there are no more messages in the buffer 
		//let the sub threads of the clienthandler be able to call this method again
		isProcessing = false;
	}

	/**
	 * ListenerClientConnections listens on a socket for connecting clients
	 * when a new clients connects, a new thread starts, which handles the in and output
	 * to and from the client.
	 * @author Denis
	 *
	 */
	private class ListenerClientConnections extends Thread{

		/**
		 * the socket the server is listening and and waiting
		 * for new clients to connect
		 */
		private ServerSocket serverListenerSocket;

		/**
		 * call the constructor to bind a socket to this specific port and listen for client connections
		 * @param port binds a socket to this port
		 * @throws IOException 
		 */
		public ListenerClientConnections(Integer port) {

			try{

				serverListenerSocket = new ServerSocket(port);

			}catch(IOException e){
//				e.printStackTrace();
			}

		}

		/**
		 * after starting the ListenerClientConnections thread, this method will be called,
		 * and the socket starts listening.
		 * After a new client connected, the server starts a new thread to handle that connection
		 */
		public void run(){

			try{
				DebugOutputHandler.printDebug("I am free! ");
				while(true){
					new ClientHandler(serverListenerSocket.accept()).start();
				}

			}catch(IOException e){

//				e.printStackTrace();

			}finally{

				try {
					serverListenerSocket.close();
				} catch (IOException e) {
//					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * Client Handler handles on single connection to one client.
	 * The client first gets a new unique ID.
	 * Each message received by this client will get his ID written inside.
	 * 
	 * @author Denis
	 *
	 */
	private class ClientHandler extends Thread{

		/**
		 * the socket over which the communication to one specific client will be handled
		 */
		private Socket clientSocket;

		/**
		 * unique id for the client
		 */
		private int clientId;

		/**
		 * client writes his objects onto this stream
		 */
		private ObjectInputStream objectInputStream;

		/**
		 * server writes his response onto this stream
		 */
		private ObjectOutputStream objectOutputStream;

		public ClientHandler(Socket socket){
			this.clientSocket = socket;

			askForClientId();
			addClientToLists();

			DebugOutputHandler.printDebug("Player #"+clientId+ " connected");

		}

		/**
		 * Reads the input of a client connected to this socket.
		 */
		public void run(){
			
			try{
				
				readInput();
				
			}catch(Exception e){
				
				removeClientFromList();
				
				try {
					
					clientSocket.close();
					
				} catch (IOException e1) {
					
//					e1.printStackTrace();
					
				}
			}
		}

		/**
		 * Try to read input from a client on the objectInputStream
		 * and add the message to the messageBuffer. 
		 * 
		 */
		private void readInput(){
			try{
				RaceTrackMessage receivedMessage = null;

				Object object = null;

				//handle incoming messages
				while((object = objectInputStream.readObject())!=null){
					
					/*
					 * instaceof might be bad programming habit, but i could not find an other way
					 * to make this work otherwise..
					 */
					if (object instanceof RaceTrackMessage) {
						
						receivedMessage = ( RaceTrackMessage ) object;
						
						receivedMessage.addClientID(clientId);
						
						addMessageToBuffer(receivedMessage);
					}
					
				}
				
			}catch(SocketTimeoutException e){

				removeClientFromList();
				try {
					objectInputStream.close();
					objectOutputStream.close();
					clientSocket.close();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
				
			}catch(SocketException e){
				try {
					
					removeClientFromList();
					clientSocket.close();
					
				} catch (IOException e1) {

					System.out.println("could not close socket.");
				}
			}catch(IOException | ClassNotFoundException e){

				removeClientFromList();
//				e.printStackTrace();
			}

		}

		/**
		 * This Method tries to get the input and ouputstream of the socket, the client connected to.
		 * Then the ouputstream will be flushed, so it is ready to be written on
		 * @throws IOException
		 */
		private void openStreams() throws IOException{
			
			objectInputStream = new ObjectInputStream( clientSocket.getInputStream() );
			objectOutputStream = new ObjectOutputStream( clientSocket.getOutputStream() );
			//flush stream to be able to write into the stream
			objectOutputStream.flush();
			
		}

		/**
		 * asks for a free ID for the new client and notifies the controller 
		 * of a newly connected client
		 */
		private void askForClientId(){
			
			this.clientId = getNextId();
			controller.clientConnected(clientId);
			
		}

		/**
		 * adds the outputstream of the client to the outputstream hashmap.
		 * Then it matches the clients IP address to his id
		 */
		private synchronized void addClientToLists(){

			try {
				
				openStreams();
				
			} catch (IOException e) {
				
			}

			clientIds.add(clientId);

			objectOutputStreams.put(clientId, objectOutputStream );
			playerSocketMap.put(clientId, clientSocket);

		}

		/**
		 * removes the client which disconnected from the objectOutputStreams hashmap and
		 * the clientIds array list
		 * @param id clients id
		 */
		private synchronized void removeClientFromList(){

			if(objectOutputStreams.get(clientId) != null)
				objectOutputStreams.remove(clientId);
			
			if(playerSocketMap.get(clientId)!= null)
				playerSocketMap.remove(clientId);
			
			//search the clientID in the clientIDs hash map and delete that client
			int counter = 0;
			for(Integer iteratorId : clientIds){
				if(iteratorId == clientId){
					clientIds.remove(counter);
					break;
				}else{
					counter += 1;
				}
			}
			DebugOutputHandler.printDebug("Player "+this.clientId+" disconnected.");
			RaceTrackMessage dc = new DisconnectMessage();
			dc.addClientID(clientId);
			controller.receiveMessage(dc);
		}

		/**
		 * Adds a message to the messageBuffer, it only will add the message , when ComServer is not working on the messageBuffer at that moment.
		 * @param message by the client, which the controller should process
		 */
		private void addMessageToBuffer(RaceTrackMessage message){

			//adds the new message by a client to a buffer. only client at a time will be able to add 
			//a message to the buffer
			synchronized(messageBuffer){
				messageBuffer.add(message);
			}
			
			//if the server is not processing any messages at the moment,
			//try to process all the messages in the buffer
			
			if(!isProcessing){
				processMessages();
			}
		}
	}

}
