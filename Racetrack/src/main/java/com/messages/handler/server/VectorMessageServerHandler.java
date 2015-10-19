package src.main.java.com.messages.handler.server;

import java.util.List;

import javafx.geometry.Point2D;
import src.main.java.com.messages.BroadCastMoveMessage;
import src.main.java.com.messages.InvalidMoveMessage;
import src.main.java.com.messages.PlayerWonMessage;
import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.VectorMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.core.DebugOutputHandler;
import src.main.java.logic.Administration;

/**
 * this class handles the incoming move messages from the client and
 * computes an answer of broadcastMoveMessage for the clients
 * 
 * it checks for collision, next player, winner, game loses 
 * @author Denis
 *
 */
public class VectorMessageServerHandler extends RaceTrackMessageServerHandler{


	public VectorMessageServerHandler(Administration administration){
		super( administration );
	}

	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(RaceTrackMessage messageToHandle){

		DebugOutputHandler.printDebug("Received a VectorMessage");


		//generate answer
		RaceTrackMessage answer = null;

		try{

			/*
			 * Get some fields before asking the administration for validation etc.
			 */
			VectorMessage incomingMessage = (VectorMessage) messageToHandle;

			Point2D playerVector = incomingMessage.getVector();

			int playerID = -1;
			if(incomingMessage.getReceiverIds() != null)
				playerID = incomingMessage.getReceiverIds().get(0);

			DebugOutputHandler.printDebug("Player with ID: "+ playerID+" wants to move to X: "+playerVector.getX()+" Y: "+playerVector.getY());

			List<Integer> playersInSameSession = null;

			if(administration.getPlayerIdsInSameSession(playerID) != null){
				playersInSameSession = administration.getPlayerIdsInSameSession(playerID);
			}

			//check if the move is valid
			if(administration.checkValidityOfClientMove(playerID, playerVector)){

				DebugOutputHandler.printDebug("Playermove is valid!");

				//generate fields for broadcastmove message and inform the game, that the player moved

				int playerWhoMoved = administration.getGroupPositionByPlayerID(playerID);

				//Move player and get collisionpoint if there was one
				Point2D collisionPointFromPlayer = administration.updatePlayerPositionByID(playerID, playerVector);

				administration.startNextRoundByPlayerID(playerID);
				
				int nextPlayerToMove = administration.getNextPlayerToMove(playerID);
				
				int playerWhoWon = administration.getPlayerWhoWonByPlayerID(playerID); 
				
				boolean didAPlayerWin = playerWhoWon != -1;

				int round = administration.getPlayerRoundByPlayerID(playerID);
						
				Point2D playerVelocity = administration.getCurrentPlayerVelocityByPlayerID(playerID);
				
				DebugOutputHandler.printDebug("Next Player to move is: "+ nextPlayerToMove);
				
				
				if(didAPlayerWin){
					DebugOutputHandler.printDebug("Player "+playerWhoWon+" did win the game.");
					
					answer = generatePlayerWonMessage(playerWhoMoved,playerWhoWon, playerVector);
					administration.closeGameByPlayerID(playerID);
					
				}else{
					
					answer = generateBroadcastMoveMessage(playerWhoMoved,playerVector,
							collisionPointFromPlayer,nextPlayerToMove, playerVelocity, round);
				}

				for(int p : playersInSameSession)
					answer.addClientID(p);	

			}//No Valid move :
			else{

				DebugOutputHandler.printDebug("Playermove is not valid!");

				answer = new InvalidMoveMessage();

				answer.addClientID(playerID);

			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			DebugOutputHandler.printDebug("Something went wrong. Check VectorMessageServerHandler.");
		}

		return answer;
	}

	public static PlayerWonMessage generatePlayerWonMessage(int playerWhoMoved,int playerWhoWon,Point2D playerVector){
		return new PlayerWonMessage(playerVector, playerWhoWon, playerWhoMoved);
	}

	public static BroadCastMoveMessage generateBroadcastMoveMessage(int playerWhoMoved,Point2D playerVector,Point2D collisionPointFromPlayer,int nextPlayerToMove,Point2D playerVelocity, int round){
		return new BroadCastMoveMessage(playerVector, collisionPointFromPlayer, false, nextPlayerToMove, playerWhoMoved, playerVelocity , round);
	}

}
