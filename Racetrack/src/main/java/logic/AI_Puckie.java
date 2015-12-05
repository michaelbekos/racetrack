package src.main.java.logic;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

public class AI_Puckie extends AI
{
	boolean mGridCreated;
	boolean [][] mGrid;
	public AI_Puckie(Integer playerID, String name)
	{
		super( playerID, name );
		mGridCreated=false;
	}
	
	@Override
	public  javafx.geometry.Point2D move(Game g)
	{
		if( !mGridCreated )
		{
			mGridCreated=createGrid(g);
		}
		g.getTrack().getInnerBoundary();
		return new javafx.geometry.Point2D(random.nextInt(100), random.nextInt(100));
	}
	
	public boolean isAI()
	{
		return true;
	}
	
	private boolean createGrid(Game g)
	{
		int x=((int)g.getTrack().getDimension().getX());
		int y=((int)g.getTrack().getDimension().getY());
		mGrid=new boolean[x][y];
		
		for( int i=0 ; i<x ; i++ )
		{
			for( int j=0 ; j<y ; j++ )
			{
				if( 0==j )
					mGrid[i][j]=false;
				else
					mGrid[i][j]=mGrid[i][j-1];
				
				Line l=new Line( i, j, i, j+1 );
					    
				for( int k=0 ; k<g.getTrack().getInnerBoundary().length ; k++ )
				{
					if(g.getTrack().getInnerBoundary()[k].intersects(l))
					{
						if(null==g.getTrack().getInnerBoundary()[k].pointOfIntersection(new Line2D(l)))
						{
							System.out.println( "MAYBE ON LINE: null" );
						}
						else
						{
							mGrid[i][j]=!mGrid[i][j];
						}
					}
				}
				for( int k=0 ; k<g.getTrack().getOuterBoundary().length ; k++ )
				{
					if( g.getTrack().getOuterBoundary()[k].intersects( l ) )
					{
						if(null==g.getTrack().getOuterBoundary()[k].pointOfIntersection(new Line2D(l)))
						{
							System.out.println( "MAYBE ON LINE: null" );
						}
						else
						{
							mGrid[i][j]=!mGrid[i][j];
						}
					}	
				}
				if(g.getTrack().getFinishLine().intersects(l))
				{
					if(null==g.getTrack().getFinishLine().pointOfIntersection(new Line2D(l)))
					{
						System.out.println( "MAYBE ON LINE: null" );
					}
					else
					{
						mGrid[i][j]=!mGrid[i][j];
					}
				}
			}					
		}
		System.out.println( "Grid created!" );
		
		mGrid=new boolean[x][y];
		
		for( int i=0 ; i<x ; i++ )
		{
			for( int j=0 ; j<y ; j++ )
			{
				System.out.print( (mGrid[i][j])?"X":" " );
			}
			System.out.println( " " );
		}
		return true;
	}
}
