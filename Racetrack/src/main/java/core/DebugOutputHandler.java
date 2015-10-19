package src.main.java.core;

public class DebugOutputHandler {

	
	public static boolean isDebug = true;

	/**
	 * more to come
	 */
	
	public void switchIsDebug(){
		DebugOutputHandler.isDebug = !isDebug;
	}
	
	public static void printDebug(String s){
		if(isDebug)
			System.out.println(s);
	}
}
