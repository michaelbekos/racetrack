package src.main.java.logic;

import java.util.PriorityQueue;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import src.main.java.logic.AIstar.AIstar;
import src.main.java.logic.AIstar.LineSegment;
import src.main.java.logic.AIstar.State;
import src.main.java.logic.AIstar.StateComparator;

public class AI_Puckie extends AI
{
	private boolean mGridCreated;
	private boolean [][] mGrid;
	private int [][] mDijkstaDistances;
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
		int x=((int)getCurrentPosition().getX());
		int y=((int)getCurrentPosition().getY());
		
		if( !mGridCreated )
		{
			mGridCreated=createGrid( x, y );
			doDijkstra( x, y );
		}
		
		
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
	
	private boolean createGrid( int sX, int sY )
	{
		mWidth=((int)mGame.getTrack().getDimension().getX());
		mHeight=((int)mGame.getTrack().getDimension().getY());
		mGrid=new boolean[mWidth][mHeight];
		boolean [][] considered=new boolean[mWidth][mHeight];

		sX=(int)mGame.getTrack().getStartingPoints()[0].getX();
		sY=(int)mGame.getTrack().getStartingPoints()[0].getY();
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
	private void doDijkstra( int sX, int sY )
	{
		mDijkstaDistances=new int[mWidth][mHeight];
		PriorityQueue<Dijkstra2DEntry> list=new PriorityQueue<Dijkstra2DEntry>( 10,new Dijkstra2DEntryComparator() );
		
		for( int j=mHeight-1 ; j>=0 ; j-- )
		{
			for( int i=0 ; i<mWidth ; i++ )
			{
				if( mGrid[i][j] )
				{
					if( i==sX && j==sY )
					{
						list.add( new Dijkstra2DEntry( i, j, 0 ) );
					}
					else
					{
						list.add( new Dijkstra2DEntry( i, j, -1 ) );
					}
					mDijkstaDistances[i][j]=-1;
				}
				else
				{
					mDijkstaDistances[i][j]=-2;
				}					
			}
		}
		
		mDijkstaDistances[sX][sY]=0;
		System.out.println( "Start position: ( " + sX + ", " + sY + " )" );
//		 1  Funktion Dijkstra(Graph, Startknoten):
//		 2      initialisiere(Graph,Startknoten,abstand[],vorgänger[],Q)
//		 3      solange Q nicht leer:                       // Der eigentliche Algorithmus
//		 4          u:= Knoten in Q mit kleinstem Wert in abstand[]
//		 5          entferne u aus Q                                // für u ist der kürzeste Weg nun bestimmt
//		 6          für jeden Nachbarn v von u:
//		 7              falls v in Q:
//		 8                 distanz_update(u,v,abstand[],vorgänger[])   // prüfe Abstand vom Startknoten zu v'
//		 9      return vorgänger[]
					 
		while( !list.isEmpty() )
		{
			Dijkstra2DEntry n=list.poll();
			System.out.println( "" );
			System.out.println( "PQ removed: " + n );

			dijkstraWriteEntry( n.x()  , n.y()+1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()+1, n.y()+1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()+1, n.y()  , n.getValue()+1, list );
			dijkstraWriteEntry( n.x()+1, n.y()-1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()  , n.y()-1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()-1, n.y()-1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()-1, n.y()  , n.getValue()+1, list );
			dijkstraWriteEntry( n.x()-1, n.y()+1, n.getValue()+1, list );
		}

		// Output
		for( int j=mHeight-1 ; j>=0 ; j-- )
		{
			for( int i=0 ; i<mWidth ; i++ )
			{
				System.out.format( "%3d", mDijkstaDistances[i][j] );
			}
			System.out.println( " " );
		}
	}

	private void dijkstraWriteEntry( int x, int y, int newValue, PriorityQueue<Dijkstra2DEntry> list )
	{
		System.out.println( "" );
		System.out.println( "   Considering position: ( " + x + ", " + y + " )" );
		if( x<0 || y<0 || x>=mWidth || y>=mHeight )
		{
			System.out.println( "   Rejected (bounderies)" );
			return;
		}
		if( !mGrid[x][y] )
		{
			System.out.println( "   Rejected (track)" );
			return;
		}

		int v=0;
		for (Dijkstra2DEntry e : list)
		{
			if( x==e.x() && y==e.y() )
			{
				v=e.getValue();
				System.out.println( "   old value: " + v + "; new value:" + newValue );
				if( v==-1||newValue<v )
				{
					if( list.remove( e ) )
					{
						System.out.println( "   removed:" +e );
						list.add( new Dijkstra2DEntry( x, y, newValue ) );
						mDijkstaDistances[x][y]=newValue;
					}
					else
					{
						System.out.println( "   remove failed" );
					}
				}
				return;
			}
		}
		System.out.println( "   Not in PQ" );
	}
}
