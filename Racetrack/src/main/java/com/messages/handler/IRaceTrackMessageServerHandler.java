package src.main.java.com.messages.handler;

import src.main.java.com.messages.RaceTrackMessage;

public interface IRaceTrackMessageServerHandler {

	public RaceTrackMessage generateAnswerAndUpdateModel(RaceTrackMessage messageToHandle);
	
}
