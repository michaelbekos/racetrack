package src.main.java.logic;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class AI_Crasher extends AI
{
	private int turn;
	public AI_Crasher(Integer playerID, String name)
	{
		super( playerID, name );
		turn = 1;
	}
	
	@Override
	public  javafx.geometry.Point2D move()
	{
		int x;
		int y;
		if (turn <= 5)
		{
			x =((int)getCurrentPosition().getX());
			y=((int)getCurrentPosition().getY()+turn);
			turn+=1;
		}
		else
		{
			x=((int)getCurrentPosition().getX()+1);
			y=((int)getCurrentPosition().getY());
		}
		
		return new javafx.geometry.Point2D( x, y );
	}
	
	public boolean isAI()
	{
		return true;
	}
}
