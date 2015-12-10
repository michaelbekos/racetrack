package src.main.java.logic;

import java.util.Random;

import com.sun.glass.ui.Application;

public abstract class AI extends Player implements IAI {
	
	public static Random random = new Random(System.currentTimeMillis());

	protected Game mGame;
	public AI(Integer playerID, String name) {
		super(playerID, name);
		super.setParticipating(true);
		System.out.println( ""+ name + " - AI generates!" );
	}
	
	@Override
	public abstract javafx.geometry.Point2D move();
	
	public boolean isAI() {
		return true;
	}
	
	public void setGameInformation( Game g )
	{
		mGame=g;
	}

}
