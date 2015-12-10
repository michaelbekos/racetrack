package src.main.java.logic;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import src.main.java.logic.AIstar.AIstar;
import src.main.java.logic.AIstar.LineSegment;

public class AI_Puckie extends AI
{
	private boolean mGridCreated;
	private boolean [][] mGrid;
	private int mWidth;
	private int mHeight;
	public AI_Puckie(Integer playerID, String name)
	{
		super( playerID, name );
		mGridCreated=false;
	}
	
	@Override
	public  javafx.geometry.Point2D move()
	{	
		if( !mGridCreated )
		{
			mGridCreated=createGrid();
		}
		
		int x=((int)getCurrentPosition().getX());
		int y=((int)getCurrentPosition().getY());
		return new javafx.geometry.Point2D( x, y );
		//return new javafx.geometry.Point2D(random.nextInt(100), random.nextInt(100));
	}
	
	public boolean isAI()
	{
		return true;
	}
	
	private void tryFill( int x, int y, int xOld, int yOld, boolean [][] considered )
	{
		considered[x][y]=true;
		LineSegment l=LineSegment.GetLineSegment( new Line2D( new Point2D( x, y ), new Point2D( xOld, yOld ) ) );

		Track t=mGame.getTrack();
		for( int k=0 ; k<t.getInnerBoundary().length ; k++ )
		{
			LineSegment b=LineSegment.GetLineSegment( t.getInnerBoundary()[k] );
			if( b.IntersectWith(l) )
				return;
		}
		for( int k=0 ; k<t.getOuterBoundary().length ; k++ )
		{
			LineSegment b=LineSegment.GetLineSegment( t.getOuterBoundary()[k] );
			if( b.IntersectWith(l) )
				return;
		}
		
		LineSegment b=LineSegment.GetLineSegment( t.getFinishLine() );
		if( b.IntersectWith(l) )
			return;
		
		mGrid[x][y]=true;
		if( x>0 )
		{
			if( !considered[x-1][y] )
				tryFill( x-1, y, x, y, considered );
		}
		if( x<mWidth-1  )
		{
			if( !considered[x+1][y] )
				tryFill( x+1, y, x, y, considered );
		}
		if( y>0  )
		{
			if( !considered[x][y-1] )
				tryFill( x, y-1, x, y, considered );
		}
		if( y<mHeight-1  )
		{
			if( !considered[x][y+1] )
				tryFill( x, y+1, x, y, considered );
		}
	}
	
	private boolean createGrid()
	{
		mWidth=((int)mGame.getTrack().getDimension().getX());
		mHeight=((int)mGame.getTrack().getDimension().getY());
		mGrid=new boolean[mWidth][mHeight];
		boolean [][] considered=new boolean[mWidth][mHeight];

		int sX=(int)mGame.getTrack().getStartingPoints()[0].getX();
		int sY=(int)mGame.getTrack().getStartingPoints()[0].getY();
		tryFill( sX, sY, sX, sY, considered );
		
		System.out.println( "Grid created!" );
		
		//mGrid=new boolean[x][y];

		for( int j=mHeight-1 ; j>=0 ; j-- )
		{
			for( int i=0 ; i<mWidth ; i++ )
			{
				System.out.print( (mGrid[i][j])?" ":"X" );
			}
			System.out.println( " " );
		}
		return true;
	}

	void setGrid( Point2D pos, Point2D intesection )
	{
		if( intesection!=pos )
		{
			int i=((int)pos.getX() );
			int j=((int)pos.getY() );
			mGrid[i][j]=!mGrid[i][j];
			if( !mGrid[i][j]&&intesection==new Point2D( i-1, j ) )
			{
				mGrid[i-1][j]=false;
			}
		}
	}
}
