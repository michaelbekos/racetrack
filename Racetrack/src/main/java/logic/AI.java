package src.main.java.logic;

import java.util.Random;

public class AI extends Player implements IAI {
	
	public static Random random = new Random(System.currentTimeMillis());

	public AI(Integer playerID, String name) {
		super(playerID, name);
		super.setParticipating(true);
	}
	
	@Override
	public  javafx.geometry.Point2D move() {
		// TODO Auto-generated method stub
		return new  javafx.geometry.Point2D(random.nextInt(100), random.nextInt(100));
	}
	
	public boolean isAI() {
		return true;
	}
	

}
