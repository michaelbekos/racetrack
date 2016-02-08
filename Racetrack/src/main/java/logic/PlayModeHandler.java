package src.main.java.logic;

import src.main.java.logic.utils.PlayModes;

public class PlayModeHandler {
	
	/**
	 * play modes
	 * 0 - easy, no time restriction
	 * 1 - medium 10 seconds time for a move
	 * 2 - hard 5 seconds time for a move
	 */
	private static final long[] modeTimer = {1000L , 11L, 6L };
	
	private PlayModes currentMode;
	
	public PlayModeHandler(int mode){
		mode = setMarginMode(mode);
		this.currentMode = getPlayModeByIndex(mode);
	}
	
	public PlayModes getMode(){
		return currentMode;
	}
	
	public void setMode(int newMode){
		newMode = setMarginMode(newMode);
		currentMode = getPlayModeByIndex(newMode);
	}
	
	public PlayModes getCurrentMode(){
		return currentMode;
	}
	
	public long getMaxTime(){
		return modeTimer[PlayModes.valueOf(currentMode.toString()).ordinal()];
	}
	
	private static int setMarginMode(int index){
		return index < 0 ? 0 : ( index > PlayModes.values().length-1 ? PlayModes.values().length - 1 : index);
	}
	
	private static PlayModes getPlayModeByIndex(int index){
		return PlayModes.values()[index];
	}
}
