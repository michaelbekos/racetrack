package src.main.java.logic;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class AI_NoMover extends AI
{
	public AI_NoMover(Integer playerID, String name)
	{
		super( playerID, name );
	}
	
	@Override
	public  javafx.geometry.Point2D move(Game g)
	{
		int x=((int)getCurrentPosition().getX());
		int y=((int)getCurrentPosition().getY());
		return new javafx.geometry.Point2D( x, y );
	}
	
	public boolean isAI()
	{
		return true;
	}
}
