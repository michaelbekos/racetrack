package src.main.java.com.messages.handler.server;

import java.util.List;

import src.main.java.com.messages.ChatMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.logic.Administration;

/**
 * Handles incoming Chatmessages.
 * 
 * A Chatmessage will be sent directly to every other player in the same session.
 * So it is possible for clients to chat in the lobbyList, in each of the lobbys and ingame.
 * @author Denis
 *
 */
public class ChatMessageServerHandler extends RaceTrackMessageServerHandler{

	protected ChatMessageServerHandler(Administration administration) {
		super(administration);
	}

	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(
			RaceTrackMessage messageToHandle) {
		
		ChatMessage answer = null;

		try{
			
			ChatMessage incomingMessage = (ChatMessage) messageToHandle;

			//Get the players in the same session
			List<Integer> playersInSameSession = administration.getPlayerIdsInSameSession(incomingMessage.getReceiverIds().get(0));
			
			//sage the message
			String message = incomingMessage.getChatMessage();
			
			//get the playername who sent the message
			String player = incomingMessage.getPlayerName();

			//generate new message 
			answer = new ChatMessage(message,player);
			
			for(Integer i : playersInSameSession ) {
				answer.addClientID(i);
			}
			
		}catch(ClassCastException e){
			e.printStackTrace();
		}


		return answer;
	}

}
