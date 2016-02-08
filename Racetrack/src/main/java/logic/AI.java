package src.main.java.logic;

import java.util.Random;

import com.sun.glass.ui.Application;

public abstract class AI extends Player implements IAI {
	
	public static Random random = new Random(System.currentTimeMillis());

	protected Game mGame;
	protected long mSumOfTime;

	public AI( Integer playerID, String name )
	{
		this( playerID, name, -1 );
	}
	
	public AI(Integer playerID, String name, int playerColorId )
	{
		super( playerID, name, playerColorId );
		super.setParticipating(true);
		mSumOfTime=0;
		System.out.println( ""+ name + " - AI generated!" );
	}
	
	@Override
	public abstract javafx.geometry.Point2D move();
	
	public boolean isAI()
	{
		return true;
	}
	
	public void addTime( long timeNeeded )
	{
		System.out.println( this.getName() +" needed " + ( mSumOfTime+timeNeeded )+ "ms = " + mSumOfTime + "ms + " + timeNeeded + "ms so far." );
		mSumOfTime+=timeNeeded;
	}
	
	public void setGameInformation( Game g )
	{
		mGame=g;
	}

}
