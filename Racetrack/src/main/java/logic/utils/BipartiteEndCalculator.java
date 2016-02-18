package src.main.java.logic.utils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Point2D;
import src.main.java.logic.LandingPoint;
import src.main.java.logic.LandingRegion;
import src.main.java.logic.AIstar.LineSegment;
import src.main.java.logic.AIstar.Point;
import src.main.java.logic.utils.AIUtils.Direction;

public class BipartiteEndCalculator
{
	private Direction mOut;
	private boolean mHorizontal;
	private boolean mPositiv;
	private int [] mWeights;
	//private ArrayList<LandingPoint> mLandingPoints;
	AtomicReference<LandingRegion> mEndLR;
	private int mFastestId;
	
    public BipartiteEndCalculator( 	AtomicReference<LandingRegion> endLR, 
    		 						LineSegment finishLine,
                                    boolean [][] entireGrid )
    {
    	mOut=endLR.get().getNewDirection();
    	mEndLR=endLR;
    	calcAreaFlippingRotation();
    	calcWeights( finishLine );
    	calcFastestPoint();
    }

    private void calcFastestPoint()
    {
		mFastestId=0;
		int fastestValue=mEndLR.get().getLandingPoints().get().get( 0 ).getDistance()+mWeights[0];
		for( int i=0 ; i<mEndLR.get().getLandingPoints().get().size()-1 ; i++ )
		{
	    	if( fastestValue>mEndLR.get().getLandingPoints().get().get( i ).getDistance()+mWeights[i] )
	    	{
	    		mFastestId=i;
	    		fastestValue=mEndLR.get().getLandingPoints().get().get( i ).getDistance()+mWeights[i];
	    	}
		}
    }
    
    public LandingPoint getFastestPoint()
    {
		return mEndLR.get().getLandingPoints().get().get( mFastestId );
    }
    
    public int getFastestPointId()
    {
		return mFastestId;
    }
    
    private void calcWeights( LineSegment finishLine )
	{
		mWeights=new int[mEndLR.get().getLandingPoints().get().size()];
		for( int i=0 ; i<mEndLR.get().getLandingPoints().get().size() ; i++ )
		{
			mWeights[i]=calcWeight( mEndLR.get().getLandingPoints().get().get( i ), finishLine );
		}
	}
	
    private int calcWeight( LandingPoint lp, LineSegment finishLine )
    {
    	int t=0;
    	int x=0, y=0, newX=0, newY=0;
    	int sx=0, sy=0;
    	x=(int)lp.getPosition().getX();
    	y=(int)lp.getPosition().getY();
    	sx=(int)lp.getSpeed().getX();
    	sy=(int)lp.getSpeed().getY();
    	LineSegment nextMove;
    	int increment=mPositiv?1:-1;
    	if( mHorizontal )
    	{
        	do
        	{
        		t++;
        		sx+=increment;
        		newX=x+sx;
        		
        		if( 0!=sy )
        		{
        			sy=(sy>0)?sy-1:sy+1;
        		}
        		newY=y+sy;
        		
        		nextMove=new LineSegment( new Point( x, y ),new Point( newX, newY ) );
        		x=newX;
        		y=newY;
        	}
        	while( !nextMove.IntersectWith( finishLine ) );
    	}
    	else
    	{
        	do
        	{
        		t++;
        		sy+=increment;
        		newY=y+sy;
        		
        		if( 0!=sx )
        		{
        			sx=(sx>0)?sx-1:sx+1;
        		}
        		newX=x+sx;
        		
        		nextMove=new LineSegment( new Point( x, y ),new Point( newX, newY ) );
        		x=newX;
        		y=newY;
        	}
        	while( !nextMove.IntersectWith( finishLine ) );
    	}
    	return t;
    }
    
    private void calcAreaFlippingRotation()
	{
		if( mOut==Direction.UP )
		{
			mHorizontal=false;
			mPositiv=true;
		}
		else if( mOut==Direction.DOWN )
		{
			mHorizontal=false;
			mPositiv=false;
		}
		else if( mOut==Direction.RIGHT )
		{
			mHorizontal=true;
			mPositiv=true;
		}
		else if( mOut==Direction.LEFT )
		{
			mHorizontal=true;
			mPositiv=false;
		}
	}

	public ArrayList<Point2D> getPath( LandingPoint from, LineSegment finishLine )
	{
		ArrayList<Point2D> ret=new ArrayList<Point2D>();
    	int t=0;
    	int x=0, y=0, newX=0, newY=0;
    	int sx=0, sy=0;
    	x=(int)from.getPosition().getX();
    	y=(int)from.getPosition().getY();
    	sx=(int)from.getSpeed().getX();
    	sy=(int)from.getSpeed().getY();
    	LineSegment nextMove;
    	int increment=mPositiv?1:-1;
    	if( mHorizontal )
    	{
        	do
        	{
        		t++;
        		sx+=increment;
        		newX=x+sx;
        		
        		if( 0!=sy )
        		{
        			sy=(sy>0)?sy-1:sy+1;
        		}
        		newY=y+sy;
        		nextMove=new LineSegment( new Point( x, y ),new Point( newX, newY ) );
        		ret.add( new Point2D( x, y ) );
        		x=newX;
        		y=newY;
        	}
        	while( !nextMove.IntersectWith( finishLine ) );
    	}
    	else
    	{
        	do
        	{
        		t++;
        		sy+=increment;
        		newY=y+sy;
        		
        		if( 0!=sx )
        		{
        			sx=(sx>0)?sx-1:sx+1;
        		}
        		newX=x+sx;
        		
        		nextMove=new LineSegment( new Point( x, y ),new Point( newX, newY ) );
        		ret.add( new Point2D( x, y ) );
        		x=newX;
        		y=newY;
        	}
        	while( !nextMove.IntersectWith( finishLine ) );
    	}
		return ret;
	}

	private ArrayList<Point2D> rotateAccelerations90CounterClockwise( ArrayList<Point2D> accelerations )
	{
		ArrayList<Point2D> ret=new ArrayList<Point2D>();
		for( int i=0 ; i<accelerations.size() ; i++ )
		{
			ret.add( new Point2D( -accelerations.get( i ).getY(),
					              accelerations.get( i ).getX() ) );
		}
		return ret;
	}
	
	private ArrayList<Point2D> flipAccelerationsOnXAxis( ArrayList<Point2D> accelerations )
	{
		ArrayList<Point2D> ret=new ArrayList<Point2D>();
		for( int i=0 ; i<accelerations.size() ; i++ )
		{
			ret.add( new Point2D(  accelerations.get( i ).getX(),
		                          -accelerations.get( i ).getY() ) );
		}
		return ret;
	}
	
	private ArrayList<Point2D> flipAccelerationsOnYAxis( ArrayList<Point2D> accelerations )
	{
		ArrayList<Point2D> ret=new ArrayList<Point2D>();
		for( int i=0 ; i<accelerations.size() ; i++ )
		{
			ret.add( new Point2D( -accelerations.get( i ).getX(),
                                   accelerations.get( i ).getY() ) );
		}
		return ret;
	}
}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				