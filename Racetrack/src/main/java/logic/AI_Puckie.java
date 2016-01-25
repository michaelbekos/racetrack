package src.main.java.logic;

import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import src.main.java.logic.AIstar.AIstar;
import src.main.java.logic.AIstar.LineSegment;
//import src.main.java.logic.AIstar.Point;
import src.main.java.logic.AIstar.State;
import src.main.java.logic.AIstar.StateComparator;
import java.util.ArrayList;
import java.util.List;

public class AI_Puckie extends AI
{
	private boolean mVerbose;
	
	private boolean mGridCreated;
	private boolean [][] mGrid;
	private int [][] mDijkstaDistances;
	private int [][] mLandingRegions;
	private int mWidth;
	private int mHeight;
	private int currentIndexPosition;
	
	// This is the pure path
	private List<Point2D> shortestPath;
	
	// Even Puckie can't take some slopes.
	// The AI(Puckie) will have to stop on some fields.
	private List<Point2D> movePath;
	private List<Point2D> goalPoints;
	
	public AI_Puckie(Integer playerID, String name)
	{
		super( playerID, name );
		mGridCreated=false;

		// SET THIS TO FALSE FOR LESS OUTPUT.
		mVerbose=true;
		
		currentIndexPosition=0;
		shortestPath=new ArrayList<Point2D>();
		movePath=new ArrayList<Point2D>();
		goalPoints=new ArrayList<Point2D>();
	}
	
	@Override
	public  javafx.geometry.Point2D move()
	{	
		int x=((int)getCurrentPosition().getX());
		int y=((int)getCurrentPosition().getY());
		
		if( !mGridCreated )
		{
			//System.out.println( this.getName()+ " is creating a grid." ); 
			mGridCreated=createGrid( x, y );
			//System.out.println( this.getName()+ " is calculating dijkstra." );
			doDijkstra( x, y );
			//System.out.println( this.getName()+ " is creating the path list." );
			createPathList();
			//System.out.println( this.getName()+ " is enhancing the path list." );
			createMovePath();
			//System.out.println( this.getName()+ " is extracting all landing regions." );
			
			ArrayList<ArrayList<Point2D> > lrl=searchLandingRegions();

			if( mVerbose )
			{
				for( int i=0 ; i<lrl.size() ; i++ )
				{
					System.out.println( "Region " + (i+1) + ". has " + lrl.get(i).size() + "elements :"  );
					for( int j=0 ; j<lrl.get(i).size() ; j++ )
					{
						System.out.println( "( x=" + lrl.get(i).get(j).getX() + "; y=" + lrl.get(i).get(j).getY()+ " )" );
					}
				}
				
				for( int j=mHeight-1 ; j>=0 ; j-- )
				{
					for( int i=0 ; i<mWidth ; i++ )
					{
						System.out.format( "%3d", mLandingRegions[i][j] );
					}
					System.out.println( " " );
				}
			}
			
		}
		currentIndexPosition++;
		
		System.out.println( ""+this.getName()+" will try to move to: ( "+ movePath.get( currentIndexPosition ).getX() + ", "+ movePath.get( currentIndexPosition ).getY() +" )" ); 
		return new javafx.geometry.Point2D( 
				movePath.get( currentIndexPosition ).getX(),
				movePath.get( currentIndexPosition ).getY()
				);
	}
	
	public boolean isAI()
	{
		return true;
	}
	
	private void createPathList()
	{
		Point2D tmpCurrentLastPoint=goalPoints.get(0);
		int tmpGoalMin=mDijkstaDistances[(int)goalPoints.get(0).getX()][(int)goalPoints.get(0).getY()];
		for( int i=1 ; i<goalPoints.size() ; i++ )
		{
			if( tmpGoalMin<=mDijkstaDistances[(int)goalPoints.get(i).getX()][(int)goalPoints.get(i).getY()] )
			{
				tmpCurrentLastPoint=goalPoints.get(i);
				tmpGoalMin=mDijkstaDistances[(int)goalPoints.get(i).getX()][(int)goalPoints.get(i).getY()];
			}
		}
		
		List<Point2D> tmpInverseShortestPath=new ArrayList<Point2D>();
		
		tmpInverseShortestPath.add( tmpCurrentLastPoint );
		while( -2==mDijkstaDistances[(int)tmpCurrentLastPoint.getX()][(int)tmpCurrentLastPoint.getY()] ||
				0<mDijkstaDistances[(int)tmpCurrentLastPoint.getX()][(int)tmpCurrentLastPoint.getY()]  )
		{
			tmpCurrentLastPoint=smallestNeighbor( tmpCurrentLastPoint );
			tmpInverseShortestPath.add( tmpCurrentLastPoint );			
		}
		
		for( int i=tmpInverseShortestPath.size() ; i>0 ; i-- )
		{
			shortestPath.add( tmpInverseShortestPath.get( i-1 ) );
		}
	}
	private void createMovePath()
	{
		// Generally there are two possible situations.

		// Situation 1:
		// We move straight (0°,90°,180°,270°). The '#' denote the possible positions after that move.
		// So we can drive every slope that is smaller or equal to 90°. ('^' is the head of the arrow describing a first move)
		// #------#------#
		// |      |      |
		// |      |      |
		// #------#------#
		// |      |      |
		// |      |      |
		// #------#------#
		// |      ^      |
		// |      |      |
		// o------o------o
		
		// Situation 2:
		// We move diagonal (45°,135°,225°,315°).
		// So we can drive every slope that is smaller than 90°. ('4' is the head of the arrow describing a first move)
		// o--#--#--#
		// |  |  |  |
		// |  |  |  |
		// o--#--#--#
		// |  |  |  |
		// |  |  |  |
		// o--#--#--#
		// | 4|  |  |
		// |/ |  |  |
		// o--o--o--o
		
		movePath.add( shortestPath.get(0) );

		// Vector: v1, v2
		// Angle between v1 and v2: alpha
		for( int i=0 ; i<shortestPath.size()-2 ; i++ )
		{
			Point2D v1=new Point2D( 
					shortestPath.get( i+1 ).getX()-shortestPath.get( i ).getX(), 
					shortestPath.get( i+1 ).getY()-shortestPath.get( i ).getY() );
			Point2D v2=new Point2D( 
					shortestPath.get( i+2 ).getX()-shortestPath.get( i+1 ).getX(), 
					shortestPath.get( i+2 ).getY()-shortestPath.get( i+1 ).getY() );
			double alpha=v1.angle(v2);
			if( alpha<0 )
				alpha=-alpha;

			movePath.add( shortestPath.get(i+1) );
			
			if( 0!=v1.getX() && 0!=v1.getY() )
			{// Situation 2
				if( alpha>=90 )
				{
					// add twice (we need to wait because the turn is to strong)
					movePath.add( shortestPath.get(i+1) );					
				}
				else
				{
					// add once (this turn is easy)					
				}
			}
			else
			{// Situation 1
				if( alpha>90 )
				{
					// add twice (we need to wait because the turn is to strong)
					movePath.add( shortestPath.get(i+1) );					
				}
				else
				{
					// add once (this turn is easy)					
				}
			}
		}	
		movePath.add( shortestPath.get( shortestPath.size()-1 ) );
		
		if( mVerbose )
		{
			for( int i=0 ; i<movePath.size() ; i++ )
			{
				System.out.println( "i="+i+": "+movePath.get(i) );
			}
		}
	}
	
	private Point2D smallestNeighbor( Point2D p )
	{
		Point2D ret = new Point2D( -1, -1 );
		Point2D tmpP;
		int minDist=mWidth*mHeight;
		int tmpNeighborDist=0;

		tmpP=new Point2D( p.getX(), p.getY()+1 );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}

		tmpP=new Point2D( p.getX()+1, p.getY()+1 );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}

		tmpP=new Point2D( p.getX()+1, p.getY() );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}

		tmpP=new Point2D( p.getX()+1, p.getY()-1 );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}

		tmpP=new Point2D( p.getX(), p.getY()-1 );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}

		tmpP=new Point2D( p.getX()-1, p.getY()-1 );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}

		tmpP=new Point2D( p.getX()-1, p.getY() );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}

		tmpP=new Point2D( p.getX()-1, p.getY()+1 );
		tmpNeighborDist=distanceOf( tmpP );
		if( tmpNeighborDist!=-1 && minDist>tmpNeighborDist )
		{
			ret=tmpP;
			minDist=tmpNeighborDist;
		}
		
		return ret;
	}
	private int distanceOf( Point2D p )
	{
		int retDistance=-1;
		int x=(int)p.getX();
		int y=(int)p.getY();
		if( x>=0 && x<=mWidth-1 && y>=0 && y<=mHeight-1 )
		{
			retDistance=mDijkstaDistances[x][y];
		}
		
		if( -2==retDistance )
			retDistance=-1;
		
		return retDistance;
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
		{
			goalPoints.add( new Point2D( x, y ) );
			return;
		}
		
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
		
		if( mVerbose )
		{
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
		//System.out.println( "Start position: ( " + sX + ", " + sY + " )" );
		
//		 1  Funktion Dijkstra(Graph, Startknoten):
//		 2      initialisiere(Graph,Startknoten,abstand[],vorgÃ¤nger[],Q)
//		 3      solange Q nicht leer:                       // Der eigentliche Algorithmus
//		 4          u:= Knoten in Q mit kleinstem Wert in abstand[]
//		 5          entferne u aus Q                                // fÃ¼r u ist der kÃ¼rzeste Weg nun bestimmt
//		 6          fÃ¼r jeden Nachbarn v von u:
//		 7              falls v in Q:
//		 8                 distanz_update(u,v,abstand[],vorgÃ¤nger[])   // prÃ¼fe Abstand vom Startknoten zu v'
//		 9      return vorgÃ¤nger[]
					 
		while( !list.isEmpty() )
		{
			Dijkstra2DEntry n=list.poll();
			//System.out.println( "" );
			//System.out.println( "PQ removed: " + n );

			dijkstraWriteEntry( n.x()  , n.y()+1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()+1, n.y()+1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()+1, n.y()  , n.getValue()+1, list );
			dijkstraWriteEntry( n.x()+1, n.y()-1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()  , n.y()-1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()-1, n.y()-1, n.getValue()+1, list );
			dijkstraWriteEntry( n.x()-1, n.y()  , n.getValue()+1, list );
			dijkstraWriteEntry( n.x()-1, n.y()+1, n.getValue()+1, list );
		}
		if( mVerbose )
		{
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
	}

	private void dijkstraWriteEntry( int x, int y, int newValue, PriorityQueue<Dijkstra2DEntry> list )
	{
		//System.out.println( "" );
		//System.out.println( "   Considering position: ( " + x + ", " + y + " )" );
		if( x<0 || y<0 || x>=mWidth || y>=mHeight )
		{
			//System.out.println( "   Rejected (bounderies)" );
			return;
		}
		if( !mGrid[x][y] )
		{
			//System.out.println( "   Rejected (track)" );
			return;
		}

		int v=0;
		for (Dijkstra2DEntry e : list)
		{
			if( x==e.x() && y==e.y() )
			{
				v=e.getValue();
				//System.out.println( "   old value: " + v + "; new value:" + newValue );
				if( v==-1||newValue<v )
				{
					if( list.remove( e ) )
					{
						//System.out.println( "   removed:" +e );
						list.add( new Dijkstra2DEntry( x, y, newValue ) );
						mDijkstaDistances[x][y]=newValue;
					}
					else
					{
						//System.out.println( "   remove failed" );
					}
				}
				return;
			}
		}
		//System.out.println( "   Not in PQ" );
	}
	private ArrayList<ArrayList<Point2D> > searchLandingRegions()
	{
		mLandingRegions=new int[mWidth][mHeight];
		for( int j=mHeight-1 ; j>=0 ; j-- )
		{
			for( int i=0 ; i<mWidth ; i++ )
			{
				if( -2==mDijkstaDistances[i][j] )
				{
					mLandingRegions[i][j]=-1;
				}
				else
				{
					mLandingRegions[i][j]=0;
				}
			}
		}
		
		int regionFoundFirstIdOrder=0;
		
		ArrayList<ArrayList<Point2D> > ret= new ArrayList<ArrayList<Point2D> >();
		ArrayList<Integer> idOfDijkstraShortestPathForSorting=new ArrayList<Integer>();
		ArrayList<Point2D> tmpLandingRegion;
		AtomicReference<Integer> w=new AtomicReference<Integer>( 0 );
		AtomicReference<Integer> landingRegionWidth=new AtomicReference<Integer>( 0 );
		AtomicReference<Integer> landingRegionAdditionalHeight=new AtomicReference<Integer>( 0 );
		AtomicReference<Integer> idForSorting=new AtomicReference<Integer>( 0 );
		for( int i=0 ; i<mWidth-1 ; i++ )
		{
			for( int j=0 ; j<mHeight-1 ; j++ )
			{
				boolean foundACorner=false;
				boolean horizontalDirectionRight=false;
				Point2D start=new Point2D( 0, 0 );
				Point2D end=new Point2D( 0, 0 );
				Point2D origin=new Point2D( 0, 0 );
				if(    false==mGrid[i][j+1]   &&   true==mGrid[i+1][j+1]   &&
					    true==mGrid[i][j]     &&   true==mGrid[i+1][j]     )
				{
					//#.
					//..
					foundACorner=true;
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i, j ), true, idForSorting );
					if( 0==w.get() ) calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i, j, true );

					origin=new Point2D( i+1, j );// i.e. O
					if( horizontalDirectionRight )
					{
						//####.....#
						//####oo...#
						//####oo...#
						//####oo...#
						//....Oo...#
						//....oo...#
						//-->.oo...#
						//....oo...#
						//....oo...#
						//##########
						start=origin.add( new Point2D( 0, landingRegionAdditionalHeight.get() ) );
						end=origin.add(   new Point2D( landingRegionWidth.get()-1, -w.get()+1 ) );
						if( mVerbose )
						{
							System.out.println( "TypeI : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
					else
					{
						//####.....#
						//####.....#
						//####.....#
						//####.....#
						//.oooOoooo#
						//.oooooooo#
						//<--......#
						//.........#
						//.........#
						//##########
						start=origin.add( new Point2D( -landingRegionAdditionalHeight.get(), 0 ) );
						end=origin.add(   new Point2D( w.get()-1, -landingRegionWidth.get()+1 ) );
						if( mVerbose )
						{
							System.out.println( "TypeII : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
				}
				else if(     true==mGrid[i][j+1]   &&  false==mGrid[i+1][j+1]   &&
					         true==mGrid[i][j]     &&   true==mGrid[i+1][j]     )
				{
					//.#
					//..
					foundACorner=true;
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i+1, j ), true, idForSorting );
					if( 0==w.get() ) calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i+1, j, true );

					origin=new Point2D( i, j );// i.e. O
					
					if( horizontalDirectionRight )
					{
						//#.....####
						//#.....####
						//#.....####
						//#.....####
						//#ooooOooo.
						//#oooooooo.
						//#-->......
						//#.........
						//#.........
						//##########
						start=origin.add( new Point2D( -w.get()+1, 0 ) );
						end=origin.add(   new Point2D( landingRegionAdditionalHeight.get(), -landingRegionWidth.get()+1 ) );
						if( mVerbose )
						{
							System.out.println( "TypeIII : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
					else
					{
						//#.....####
						//#...oo####
						//#...oo####
						//#...oo####
						//#...oO....
						//#...oo....
						//#...oo.<--
						//#...oo....
						//#...oo....
						//##########
						start=origin.add( new Point2D( -landingRegionWidth.get()+1, landingRegionAdditionalHeight.get() ) );
						end=origin.add(   new Point2D( 0, -w.get()+1 ) );
						if( mVerbose )
						{
							System.out.println( "Type IV : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
				}
				else if(     true==mGrid[i][j+1]   &&   true==mGrid[i+1][j+1]   &&
				         	false==mGrid[i][j]     &&   true==mGrid[i+1][j]     )
				{ 
					//..
					//#.
					foundACorner=true;
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i, j+1 ), false, idForSorting );
					if( 0==w.get() ) calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i, j+1, false );

					origin=new Point2D( i+1, j+1 );// i.e. O
					
					if( horizontalDirectionRight )
					{						
						//##########
						//....oo...#
						//....oo...#
						//-->.oo...#
						//....oo...#
						//....Oo...#
						//####oo...#
						//####oo...#
						//####oo...#
						//####.....#
						start=origin.add( new Point2D( 0, w.get()-1 ) );
						end=origin.add(   new Point2D( landingRegionWidth.get()-1, -landingRegionAdditionalHeight.get() ) );
						if( mVerbose )
						{
							System.out.println( "Type V : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
					else
					{
						//##########
						//.........#
						//.........#
						//<--......#
						//.oooooooo#
						//.oooOoooo#
						//####.....#
						//####.....#
						//####.....#
						//####.....#
						start=origin.add( new Point2D( -landingRegionAdditionalHeight.get(), landingRegionWidth.get()-1 ) );
						end=origin.add(   new Point2D( w.get()-1, 0 ) );
						if( mVerbose )
						{
							System.out.println( "TypeVI : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
				}
				else if(     true==mGrid[i][j+1]   &&   true==mGrid[i+1][j+1]   &&
						     true==mGrid[i][j]     &&  false==mGrid[i+1][j]     )
				{ 
					//..
					//.#
					foundACorner=true;
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i+1, j+1 ), false, idForSorting );
					if( 0==w.get() ) calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i+1, j+1, false );

					origin=new Point2D( i, j+1 );// i.e. O
					
					if( horizontalDirectionRight )
					{
						//##########
						//#.........
						//#.........
						//#......-->
						//#oooooooo.
						//#ooooOooo.
						//#.....####
						//#.....####
						//#.....####
						//#.....####
						start=origin.add( new Point2D( -w.get()+1, landingRegionWidth.get()-1 ) );
						end=origin.add(   new Point2D( landingRegionAdditionalHeight.get(), 0 ) );
						if( mVerbose )
						{
							System.out.println( "TypeVII : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
					else
					{
						//##########
						//#...oo....
						//#...oo....
						//#...oo.<--
						//#...oo....
						//#...oO....
						//#...oo####
						//#...oo####
						//#...oo####
						//#.....####
						start=origin.add( new Point2D( -landingRegionWidth.get()+1, w.get()-1 ) );
						end=origin.add(   new Point2D( 0, -landingRegionAdditionalHeight.get() ) );
						if( mVerbose )
						{
							System.out.println( "TypeVIII : " );
							System.out.println( "Origin: " + origin.getX() + "; " + origin.getY()  );
							System.out.println( "Start: " + start.getX() + "; " + start.getY()  );
							System.out.println( "End " + end.getX() + "; " + end.getY()  );
							System.out.println( "IdForSorting " + idForSorting.get()  );
						}
					}
				}

				if( foundACorner )
				{
					regionFoundFirstIdOrder++;
					tmpLandingRegion=new ArrayList<Point2D>(  );
					for( int k=(int)start.getX() ; k<=(int)end.getX() ; k++ )
					{
						for( int l=(int)start.getY() ; l>=(int)end.getY() ; l-- )
						{
							//mLandingRegions[k][l]=regionFoundFirstIdOrder;
							tmpLandingRegion.add( new Point2D( k, l ) );
						}
					}
					
					if( idOfDijkstraShortestPathForSorting.size() == 0 )
					{
						idOfDijkstraShortestPathForSorting.add( idForSorting.get() );
						ret.add( tmpLandingRegion );					
					}
					else
					{
						boolean foundSlot=false;
						int k=0;
						for( k=0 ; k<idOfDijkstraShortestPathForSorting.size()-1 ; k++ )
						{
							if( idOfDijkstraShortestPathForSorting.get( k )<idForSorting.get() &&
								idOfDijkstraShortestPathForSorting.get( k+1 )>idForSorting.get() )
							{
								foundSlot=true;
								idOfDijkstraShortestPathForSorting.add( k+1, idForSorting.get() );
								ret.add( k+1, tmpLandingRegion );
								break;
							}
						}
						if( !foundSlot )
						{
							if( idForSorting.get() < idOfDijkstraShortestPathForSorting.get( 0 ) )
							{
								idOfDijkstraShortestPathForSorting.add( 0, idForSorting.get() );
								ret.add( 0, tmpLandingRegion );
							}
							else
							{
								idOfDijkstraShortestPathForSorting.add( idForSorting.get() );
								ret.add( tmpLandingRegion );
							}
						}
					}
				}
			}
		}

		for( int i=0 ; i<ret.size() ; i++ )
		{
			for( int k=0 ; k<ret.get(i).size() ; k++ )
			{
				mLandingRegions[(int)ret.get(i).get(k).getX()][(int)ret.get(i).get(k).getY()]=i+1;
			}
		}
		
		return ret;
	}
	
	private void calcWidthAndLandingRegion( AtomicReference<Integer> w, AtomicReference<Integer> landingRegionWidth, AtomicReference<Integer> landingRegionAdditionalHeight, int i, int j, boolean searchDownwards )
	{
		w.set( calcWidth( new Point2D( i, j ), searchDownwards ) );
		landingRegionWidth.set( (int) Math.floor(Math.sqrt(w.get()*2+0.25)-0.5) );
		// s_max=landingRegionWidth+1
		landingRegionAdditionalHeight.set( (int) Math.floor(Math.sqrt(w.get()*2-1.75)-0.5)+1 );
	}

	private boolean isHorizontalDirectionRight( Point2D p, boolean searchDown, AtomicReference<Integer> idForSorting )
	{
		while( this.mGrid[(int)p.getX()][(int)p.getY()] )
		{
			for( int i=0; i<shortestPath.size(); i++ )
			{
				if( shortestPath.get(i).getX()==p.getX() &&
					shortestPath.get(i).getY()==p.getY() )
				{
					double dx=shortestPath.get(i+1).getX()-shortestPath.get(i-1).getX();
					double dy=shortestPath.get(i+1).getY()-shortestPath.get(i-1).getY();
					idForSorting.set( i );
					double a=Math.atan2( dy, dx );
					if( Math.abs( a )<1.57079632679 )
						return true;
					else
						return false;
				}
			}
			if( searchDown )
			{
				p.add( 0, 1 );
			}
			else
			{
				p.add( 0, -1 );
			}
		}
		System.exit( -666 );// == Suicide!
		return false;
	}
	
	private int calcWidth( Point2D p, boolean searchDown )
	{
		int i=0;
		while( this.mGrid[(int)p.getX()][(int)p.getY()] )
		{
			i++;
			if( searchDown )
			{
				p=p.add( 0, -1 );
			}
			else
			{
				p=p.add( 0, 1 );
			}
		}
		return i;
	}
}
