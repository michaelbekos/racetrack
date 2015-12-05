package src.main.java.logic;

import java.util.Random;

import com.sun.glass.ui.Application;

public abstract class AI extends Player implements IAI {
	
	public static Random random = new Random(System.currentTimeMillis());

	public AI(Integer playerID, String name) {
		super(playerID, name);
		super.setParticipating(true);
	}
	
	@Override
	public abstract javafx.geometry.Point2D move(Game g);
	
	public boolean isAI() {
		return true;
	}
	

}
