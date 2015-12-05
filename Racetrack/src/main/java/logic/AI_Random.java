package src.main.java.logic;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class AI_Random extends AI
{
	public AI_Random(Integer playerID, String name)
	{
		super( playerID, name );
	}
	
	@Override
	public  javafx.geometry.Point2D move(Game g)
	{
		return new javafx.geometry.Point2D(random.nextInt(100), random.nextInt(100));
	}
	
	public boolean isAI()
	{
		return true;
	}
}
