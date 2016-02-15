package src.main.java.logic;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;

public class LandingPoint
{
	private Point2D mPosition;
	private Point2D mSpeed;
	private int mDistance;
	private LandingPoint mPredecessor;
	
	public LandingPoint( Point2D position, Point2D speed )
	{
		mPosition = position;
		mSpeed = speed;
		mDistance = -1;
		mPredecessor = null;
	}
	
	public Point2D getPosition()
	{
		return mPosition;
	}
	
	public Point2D getSpeed()
	{
		return mSpeed;
	}
	
	public double getTotalSpeed()
	{
		return Math.sqrt( mSpeed.getX()*mSpeed.getX()+mSpeed.getY()*mSpeed.getY() );
	}
	
	public int getDistance()
	{
		return mDistance;
	}
	
	public void setDistance( int distance )
	{
		mDistance=distance;
	}
	
	public LandingPoint getPredecessor()
	{
		return mPredecessor;
	}
	
	public void setPredecessor( LandingPoint predecessor )
	{
		mPredecessor=predecessor;
	}
	
	@Override
	public String toString()
	{
		Point2D p=mPosition;
		Point2D s=mSpeed;
		return new String( "( x:" + p.getX() + " , y:" + p.getY() + " ); ( sx:" + s.getX() + " , sy:" + s.getY() + " ) " );
	}
	
	public boolean isAtPosition( Point2D p )
	{
		return (p.getX()==this.mPosition.getX() && p.getY()==this.mPosition.getY() );
	}
}
