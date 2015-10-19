package src.main.java.core;

import java.util.HashMap;
import java.util.Map;

import src.main.java.com.ComServer;
import src.main.java.com.messages.ClientNameMessage;
import src.main.java.com.messages.CreateLobbyMessage;
import src.main.java.com.messages.DisconnectMessage;
import src.main.java.com.messages.LeaveLobbyMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.RequestLobbyEntryMessage;
import src.main.java.com.messages.RequestLobbysMessage;
import src.main.java.com.messages.SendOptionsMessage;
import src.main.java.com.messages.VectorMessage;
import src.main.java.com.messages.handler.IRaceTrackMessageServerHandler;
import src.main.java.com.messages.handler.server.ClientNameMessageServerHandler;
import src.main.java.com.messages.handler.server.CreateLobbyMessageServerHandler;
import src.main.java.com.messages.handler.server.DisconnectMessageServerHandler;
import src.main.java.com.messages.handler.server.LeaveLobbyMessageServerHandler;
import src.main.java.com.messages.handler.server.RequestAllLobbyInformationServerHandler;
import src.main.java.com.messages.handler.server.RequestLobbyEntryServerHandler;
import src.main.java.com.messages.handler.server.SendOptionsServerHandler;
import src.main.java.com.messages.handler.server.VectorMessageServerHandler;
import src.main.java.logic.Administration;



/**
 * 
 * This class gets Message objects from the ComServer class and propagates those messages to their respective 
 * message handlers. 
 * The answer messages from the handler are beeing sent to the ComServer class for beeing processed further
 * 
 * @author Denis
 *
 */
public class ControllerServer implements IControllerServer {

	/**
	 * The Communication Class, running parallel to handle Client connections
	 */
	private ComServer com;

	/**
	 * The LobbyList with all GameSessions.
	 * handlers will ask it for answer generation
	 */
	private Administration administration;

	/**
	 * Maps a raceTrack message type to its handler
	 */
	private Map<Class <? extends RaceTrackMessage>,  IRaceTrackMessageServerHandler> map;

	private static int messageCounter;


	public ControllerServer(Integer port){
		map = new HashMap<Class <? extends RaceTrackMessage>,IRaceTrackMessageServerHandler>();
		initCom(port);
		initHandlers();
		messageCounter = 0;
	}

	//public methods

	/**
	 * informs the lobbylist, that there is a new player.
	 * sends the lobbylist the new player, with hist id and a default name
	 */
	@Override
	public void clientConnected(Integer clientId){
		administration.createAndAddNewPlayer(clientId);
	}

	@Override
	public void receiveMessage(RaceTrackMessage message){
		processMessage(message);
	}

	@Override
	public void disconnectPlayerFromServer(int playerIDToDisconnect){
		com.disconnectPlayer(playerIDToDisconnect);
	}
	
	@Override
	public void sendExtraMessage(RaceTrackMessage message){
		if(message!=null){
			com.sendMessage(message);
		}
	}
	//private methods

	/**
	 * initializes the Communication
	 * @param port where to server should listen to
	 */
	private void initCom(int port){
		com = new ComServer(port, this);
		administration = new Administration(this);
	}

	/**
	 * Initializes the message Handlers
	 */
	private void initHandlers(){
		IRaceTrackMessageServerHandler vectorMessageHandler = new VectorMessageServerHandler(administration);
		IRaceTrackMessageServerHandler	helloMessagehandler = new ClientNameMessageServerHandler(administration);
		IRaceTrackMessageServerHandler	requestLobbyEntryHandler = new RequestLobbyEntryServerHandler(administration);
		IRaceTrackMessageServerHandler		requestLobbysHandler = new RequestAllLobbyInformationServerHandler(administration);
		IRaceTrackMessageServerHandler		sendOptionsHandler = new SendOptionsServerHandler(administration);
		IRaceTrackMessageServerHandler		createLobbyHandler = new CreateLobbyMessageServerHandler(administration);
		IRaceTrackMessageServerHandler      leaveLobbyHandler = new LeaveLobbyMessageServerHandler(administration);
		IRaceTrackMessageServerHandler      disconnectHandler = new DisconnectMessageServerHandler(administration);

		map.put(VectorMessage.class, vectorMessageHandler);
		map.put(ClientNameMessage.class, helloMessagehandler);
		map.put(RequestLobbysMessage.class, requestLobbysHandler);
		map.put(RequestLobbyEntryMessage.class, requestLobbyEntryHandler);
		map.put(SendOptionsMessage.class, sendOptionsHandler);
		map.put(CreateLobbyMessage.class,  createLobbyHandler);
		map.put(LeaveLobbyMessage.class, leaveLobbyHandler);
		map.put(DisconnectMessage.class, disconnectHandler);
	}

	/**
	 * gives the message to its corresponding message handler
	 * For example, a Vectormessage gets send to the VectorMessageHandler, etc.
	 * @param messageToHandle the message, the controller got from ComServer
	 */
	private void processMessage(RaceTrackMessage messageToHandle){

		messageCounter++;
		DebugOutputHandler.printDebug("--------------------Message "+messageCounter+"--------------------");
		//receive the handler from the hashmap
		IRaceTrackMessageServerHandler messageHandler = map.get(messageToHandle.getClass());

		//generate answer with the handler
		RaceTrackMessage answer = messageHandler.generateAnswerAndUpdateModel(messageToHandle);

		//if there is an answer, which needs to be sent to the client, send it
		if(answer != null){

			DebugOutputHandler.printDebug("An Answer has been generated of type "+answer.getClass());
			com.sendMessage(answer);
		}


		DebugOutputHandler.printDebug("--------------------End Message "+messageCounter+"---------------");
	}

	
}
