package src.main.java.com.messages.handler.server;

import src.main.java.com.messages.ClientNameMessage;
import src.main.java.com.messages.OkayMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.Administration;

/**
 * Handler for ClientNameMessage
 * 
 * This Message contains the ingame name of the client.
 * No Answer will be generated, since the player will be in the lobbylist, and no other player sees him in that list
 * That means, none of the clients needs to know his name and update
 * 
 * @author Denis
 *
 */
public class ClientNameMessageServerHandler extends RaceTrackMessageServerHandler
{
	public ClientNameMessageServerHandler( Administration administration )
	{
		super( administration );
	}

	/**
	 * informs the lobby that a specific player now has a name
	 * 
	 * no answer will be generated
	 */
	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel( RaceTrackMessage messageToHandle )
	{
		DebugOutputHandler.printDebug("Received a ClientNameMessage");

		//No answer needs to be sent to other clients
		RaceTrackMessage answer = null;

		//cast to the right type of message
		try
		{
			ClientNameMessage incomingMessage = (ClientNameMessage) messageToHandle;

			//get the id of the client, which connected
			int playerId = -1;
			if(incomingMessage.getReceiverIds() != null)
				playerId = incomingMessage.getReceiverIds().get(0);

			//Get the players name
			String playerName ="";
			Integer playerEntityId=-1;
			if( null!=incomingMessage.getClientName() )
			{
				playerName = incomingMessage.getClientName();
				playerEntityId = incomingMessage.getClientAiTypeId();
			}

			DebugOutputHandler.printDebug( "Player: "+playerName+" wants to connect with ID: " + playerId + " and entity ID: " + playerEntityId );
			
			

			String  fixedPlayerName = "Null";
			//inform the lobby, that a specific player has now a name
			if( -1!=playerId && ""!=playerName && -1!=playerEntityId )
			{
				fixedPlayerName = administration.setPlayerNameAndAiType( playerId, playerName, playerEntityId );
 
				DebugOutputHandler.printDebug(fixedPlayerName+" has been added to Playerlist");

			}
			answer = new OkayMessage(playerId, fixedPlayerName);
			answer.addClientID(playerId);

		}catch(ClassCastException e){

			DebugOutputHandler.printDebug("Error: go check your code that does not work");
			
			e.printStackTrace();

		}		

		return answer;
	}
}
