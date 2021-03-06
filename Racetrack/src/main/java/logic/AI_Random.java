package src.main.java.logic;

public class AI_Random extends AI
{
	public AI_Random( Integer playerID, String name )
	{
		this( playerID, name, -1 );
	}
	public AI_Random( Integer playerID, String name, int playerColorId )
	{
		super( playerID, name, playerColorId );
		mTypeID=3;
	}
	
	@Override
	public  javafx.geometry.Point2D move()
	{
		return new javafx.geometry.Point2D(random.nextInt(100), random.nextInt(100));
	}
	
	@Override
	public boolean isAI()
	{
		return true;
	}
}
