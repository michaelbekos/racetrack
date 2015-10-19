package src.main.java.logic;

import src.main.java.core.DebugOutputHandler;

public class MoveTimer extends Thread{


	private static final long NO_TIME_LEFT = 0L;

	private static final long ONE_SECOND = 1000L;

	private long timeLeft;

	private int playerID;
	
	private Boolean isFinished;
	
	private boolean didNotifyPlayer;
	
	private Administration admin;
	
	private PlayModeHandler playModeHandler;
	

	public MoveTimer(int playerID, Administration admin, int playMode){

		playModeHandler = new PlayModeHandler(playMode);
		
		timeLeft = playModeHandler.getMaxTime();

		this.playerID = playerID;
		
		this.admin = admin;
		
		isFinished = false;
		
		didNotifyPlayer = false;
	}

	@Override
	public void run(){


		
		while( timeLeft > NO_TIME_LEFT){
			
			try {
//				DebugOutputHandler.printDebug("Player "+playerID+" has "+timeLeft+" seconds left");
				Thread.sleep(ONE_SECOND);
				
				timeLeft--;
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			if(!didNotifyPlayer){
				admin.notifyClientOfTimeLeft(playerID,(int) timeLeft);
				didNotifyPlayer = true;
			}
			
			if(isFinished){
				break;
			}

		}
		
		isFinished = !isFinished;
		
		if(isFinished){
			admin.moveClientDefaultVelocity(playerID);
		}
		
	}
	
	public void setFinished(boolean b){
		isFinished = b;
	}
	
	public int getPlayer(){
		return playerID;
	}
	
}
