package src.main.java.logic.utils;

import java.util.ArrayList;

import src.main.java.logic.LandingPoint;
import src.main.java.logic.LandingRegion;
import src.main.java.logic.AIstar.LineSegment;
import src.main.java.logic.AIstar.Point;
import src.main.java.logic.utils.AIUtils.Direction;

public class BipartiteEndCalculator
{

    public BipartiteEndCalculator( 	LandingRegion startLR, 
    		 						LineSegment finishLine,
            						Direction in, 
            						Direction main, 
            						Direction out, 
                                    int w,
                                    boolean [][] entireGrid,
                                    ArrayList<LandingPoint> predDistInfo )
    {
    	calcWeight( finishLine );
    }
    
    public int calcWeight( LineSegment finishLine )
    {
    	int t=0;
    	int x=0, y=0, newX=0, newY=0;
    	
    	LineSegment nextMove;
    	do
    	{
    		nextMove=new LineSegment( new Point( x, y ),new Point( newX, newY ) );
    	}
    	while( !nextMove.IntersectWith( finishLine ) );
    	return t;
    	
    	
    }
}