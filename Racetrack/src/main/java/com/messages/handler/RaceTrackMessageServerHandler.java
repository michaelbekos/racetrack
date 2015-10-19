package src.main.java.com.messages.handler;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.logic.Administration;

public abstract class RaceTrackMessageServerHandler implements IRaceTrackMessageServerHandler{

	protected Administration administration;
	
//	RaceTrackMessage messageToHandle;
	//TODO:
	protected RaceTrackMessageServerHandler(Administration administration){
		this.administration = administration;
	}
	
	abstract public RaceTrackMessage generateAnswerAndUpdateModel(RaceTrackMessage messageToHandle);
	
	
}
