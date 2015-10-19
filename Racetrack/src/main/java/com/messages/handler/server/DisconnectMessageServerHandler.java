package src.main.java.com.messages.handler.server;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.com.messages.handler.RaceTrackMessageServerHandler;
import src.main.java.logic.Administration;

/**
 * Handles the DisconnectMessage by a client
 * This Handler informs the Administration about a client who disconnected, so he can be removed.
 * @author Denis
 *
 */
public class DisconnectMessageServerHandler  extends RaceTrackMessageServerHandler{

	public DisconnectMessageServerHandler(Administration administration) {
		super(administration);
	}

	@Override
	public RaceTrackMessage generateAnswerAndUpdateModel(
			RaceTrackMessage messageToHandle) {
		
		RaceTrackMessage answer = null;
		
		int playerID = messageToHandle.getReceiverIds().get(0);
		
		administration.playerDisconnects(playerID);

		return answer;
	}

}
