package src.main.java.logic;

import java.util.Random;

import com.sun.glass.ui.Application;

public class AI extends Player implements IAI {
	
	public static Random random = new Random(System.currentTimeMillis());

	public AI(Integer playerID, String name) {
		super(playerID, name);
		super.setParticipating(true);
	}
	
	@Override
	public  javafx.geometry.Point2D move(Game g) {
		// TODO Auto-generated method stub
		//g.
		return new  javafx.geometry.Point2D(random.nextInt(100), random.nextInt(100));
	}
	
	public boolean isAI() {
		return true;
	}
	

}
