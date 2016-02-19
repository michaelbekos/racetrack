package src.main.java.logic;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import src.main.java.logic.AIstar.AIstar;
import src.main.java.logic.AIstar.LineSegment;
//import src.main.java.logic.AIstar.Point;
import src.main.java.logic.AIstar.State;
import src.main.java.logic.AIstar.StateComparator;
import src.main.java.logic.utils.AIUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class AI_LimitedView_DriveSafe extends AI
{
	private boolean mVerbose;
	
	private List<AIUtils.Direction> landingRegionOldDirections;
	private List<AIUtils.Direction> landingRegionNewDirections;
	private int maxSpeedDominantDirection;
	private int maxSpeedOtherDirection;
	private HashSet<Point2D> grid;
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
	
	public AI_LimitedView_DriveSafe( Integer playerID, String name )
	{	
		this( playerID, name, -1 );
	}
	
	public AI_LimitedView_DriveSafe( Integer playerID, String name, int playerColorId )
	{
		super( playerID, name, playerColorId );
		mTypeID=8;
		mGridCreated=false;
		// SET THIS TO FALSE FOR LESS OUTPUT.
		mVerbose=true;
		
		currentIndexPosition=0;
		shortestPath=new ArrayList<Point2D>();
		movePath=new ArrayList<Point2D>();
		goalPoints=new ArrayList<Point2D>();
		landingRegionOldDirections = new ArrayList<AIUtils.Direction>();
		landingRegionNewDirections = new ArrayList<AIUtils.Direction>();
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
			//createMovePath();
			//System.out.println( this.getName()+ " is extracting all landing regions." );
			
			ArrayList<LandingRegion> lrl=searchLandingRegions();
			
			LinkedList<LineSegment> borders = new LinkedList<LineSegment>();
			for (Line2D boundary : mGame.getTrack().getOuterBoundary())
			{
				borders.add(LineSegment.GetLineSegment(boundary));
			}
			for (Line2D boundary : mGame.getTrack().getInnerBoundary())
			{
				borders.add(LineSegment.GetLineSegment(boundary));
			}

			LinkedList<ArrayList<LandingPoint>> landingRegions = new LinkedList<ArrayList<LandingPoint>>();
			int sx = (int)this.getCurrentVelocity().getX();
			int sy = (int)this.getCurrentVelocity().getY();
			LandingPoint tmpFrom=new LandingPoint( this.getCurrentPosition(), this.getCurrentVelocity() );
			LandingPoint tmpTo = null;
			List<LandingPoint> landingPoints = new ArrayList<LandingPoint>();
			for (int i = 0; i < lrl.size(); i++)
			{
				ArrayList<LandingPoint> tmpTos=lrl.get( i ).getSafeLandingPoint();
				int bestSpeed = 0;
				int minimumNumberOfTurns = -1;
				AIUtils.Direction tmpNewDirection = landingRegionNewDirections.get(i);
				for (LandingPoint landingPoint : tmpTos)
				{
					List<Point2D> accelerations = AIUtils.CalculateAccelerations( 	tmpFrom.getPosition(), 
							landingPoint.getPosition(), 
							tmpFrom.getSpeed(), 
							landingPoint.getSpeed(), 
							borders );
					if (accelerations == null)
					{
						continue;
					}
					if (accelerations.size() < minimumNumberOfTurns || minimumNumberOfTurns == -1)
					{
						tmpTo = landingPoint;
						minimumNumberOfTurns = accelerations.size();
						switch (tmpNewDirection)
						{
							case UP:
							{
								bestSpeed = (int)landingPoint.getSpeed().getY();
								break;
							}
							case DOWN:
							{
								bestSpeed = -(int)landingPoint.getSpeed().getY();
								break;
							}
							case RIGHT:
							{
								bestSpeed = (int)landingPoint.getSpeed().getX();
								break;
							}
							case LEFT:
							{
								bestSpeed = -(int)landingPoint.getSpeed().getX();
								break;
							}
						}
					}
					else
					{
						if (accelerations.size() == minimumNumberOfTurns)
						{
							switch (tmpNewDirection)
							{
								case UP:
								{
									if ((int)landingPoint.getSpeed().getY() > bestSpeed)
									{
										bestSpeed = (int)landingPoint.getSpeed().getY();
										tmpTo = landingPoint;
									}
									break;
								}
								case DOWN:
								{
									if (-(int)landingPoint.getSpeed().getY() > bestSpeed)
									{
										bestSpeed = -(int)landingPoint.getSpeed().getY();
										tmpTo = landingPoint;
									}
									break;
								}
								case RIGHT:
								{
									if ((int)landingPoint.getSpeed().getX() > bestSpeed)
									{
										bestSpeed = (int)landingPoint.getSpeed().getX();
										tmpTo = landingPoint;
									}
									break;
								}
								case LEFT:
								{
									if (-(int)landingPoint.getSpeed().getX() > bestSpeed)
									{
										bestSpeed = -(int)landingPoint.getSpeed().getX();
										tmpTo = landingPoint;
									}
									break;
								}
							}
						}
					}
				}
				List<Point2D> accelerations = AIUtils.CalculateAccelerations( 	tmpFrom.getPosition(), 
						tmpTo.getPosition(), 
						tmpFrom.getSpeed(), 
						tmpTo.getSpeed(), 
						borders );
				for (int j = 0; j < accelerations.size(); j++)
				{
					sx += (int)accelerations.get(j).getX();
					sy += (int)accelerations.get(j).getY();
					x += sx;
					y += sy;
					movePath.add(new Point2D(x,y));
				}
				landingPoints.add(tmpTo);
				tmpFrom=tmpTo;
			}
			List<Point2D> finalAccelerations = AIUtils.CalculateFinalAccelerations( 	
					tmpFrom.getPosition(), 
					tmpFrom.getSpeed(),
					LineSegment.GetLineSegment(mGame.getTrack().getFinishLine()),
					landingRegionNewDirections.get(landingRegionNewDirections.size()-1),
					borders );
			for (int j = 0; j < finalAccelerations.size(); j++)
			{
				sx += (int)finalAccelerations.get(j).getX();
				sy += (int)finalAccelerations.get(j).getY();
				x += sx;
				y += sy;
				movePath.add(new Point2D(x,y));
			}
			for( int j=mHeight-1 ; j>=0 ; j-- )
			{
				for( int i=0 ; i<mWidth ; i++ )
				{
					if( grid.contains( new Point2D( i, j ) ) )
					{
						boolean isLandingPoint = false;
						int landingPointIndex;
						for (landingPointIndex = 0; landingPointIndex < landingPoints.size(); landingPointIndex++)
						{
							if ((int)landingPoints.get(landingPointIndex).getPosition().getX() == i)
							{
								if ((int)landingPoints.get(landingPointIndex).getPosition().getY() == j)
								{
									isLandingPoint = true;
									break;
								}
							}
						}
						if (!isLandingPoint)
						{
							System.out.print( " " );
						}
						else
						{
							System.out.print(landingPointIndex);
						}	
					}
					else
					{
						System.out.print( "X" );
					}
				}
				System.out.println( " " );
			}
			for (int landingPointIndex = 0; landingPointIndex < landingPoints.size(); landingPointIndex++)
			{
				System.out.println(landingPointIndex + ": " + landingPoints.get(landingPointIndex).toString());
			}
		}			
			
			
			
			
		
		System.out.println( ""+this.getName()+" will try to move to: ( "+ movePath.get( currentIndexPosition + 1 ).getX() + ", "+ movePath.get( currentIndexPosition + 1 ).getY() +" )" ); 
		return movePath.get( currentIndexPosition++ );
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
	
//	private void tryFill( int x, int y, int xOld, int yOld, boolean [][] considered )
//	{
//		considered[x][y]=true;
//		LineSegment l=LineSegment.GetLineSegment( new Line2D( new Point2D( x, y ), new Point2D( xOld, yOld ) ) );
//
//		Track t=mGame.getTrack();
//		for( int k=0 ; k<t.getInnerBoundary().length ; k++ )
//		{
//			LineSegment b=LineSegment.GetLineSegment( t.getInnerBoundary()[k] );
//			if( b.IntersectWith(l) )
//				return;
//		}
//		for( int k=0 ; k<t.getOuterBoundary().length ; k++ )
//		{
//			LineSegment b=LineSegment.GetLineSegment( t.getOuterBoundary()[k] );
//			if( b.IntersectWith(l) )
//				return;
//		}
//		
//		LineSegment b=LineSegment.GetLineSegment( t.getFinishLine() );
//		if( b.IntersectWith(l) )
//		{
//			goalPoints.add( new Point2D( x, y ) );
//			return;
//		}
//		
//		mGrid[x][y]=true;
//		if( x>0 )
//		{
//			if( !considered[x-1][y] )
//				tryFill( x-1, y, x, y, considered );
//		}
//		if( x<mWidth-1  )
//		{
//			if( !considered[x+1][y] )
//				tryFill( x+1, y, x, y, considered );
//		}
//		if( y>0  )
//		{
//			if( !considered[x][y-1] )
//				tryFill( x, y-1, x, y, considered );
//		}
//		if( y<mHeight-1  )
//		{
//			if( !considered[x][y+1] )
//				tryFill( x, y+1, x, y, considered );
//		}
//	}

	
	private HashSet<Point2D> iterativeTryFill()
	{
		
		Queue<Point2D> openList=new LinkedBlockingQueue<Point2D>();
		HashSet<Point2D> closedList=new HashSet<Point2D>();
		int x=(int)mGame.getTrack().getStartingPoints()[0].getX();
		int y=(int)mGame.getTrack().getStartingPoints()[0].getY();
		openList.add( new Point2D( x, y ) );
		
		Point2D cP=openList.poll();
		while( null!=cP )
		{
			closedList.add( cP );
			LineSegment l_right=null;
			LineSegment l_left=null;
			LineSegment l_down=null;
			LineSegment l_up=null;
			Point2D p_right=new Point2D( cP.getX()+1, cP.getY()   );
			Point2D p_left=new Point2D(  cP.getX()-1, cP.getY()   );
			Point2D p_up=new Point2D(    cP.getX(),   cP.getY()+1   );
			Point2D p_down=new Point2D(  cP.getX(),   cP.getY()-1   );
			
			if( x<mWidth-1 )
			{
				l_right=LineSegment.GetLineSegment( new Line2D( cP, p_right ) );
			}
			if( x>0 )
			{
				l_left=LineSegment.GetLineSegment(  new Line2D( cP, p_left  ) );
			}
			if( y<mHeight-1 )
			{
				l_up=LineSegment.GetLineSegment(    new Line2D( cP, p_up    ) );
			}
			if( y>0 )
			{
				l_down=LineSegment.GetLineSegment(  new Line2D( cP, p_down  ) );
			}
			
			Track t=mGame.getTrack();
			for( int k=0 ; k<t.getInnerBoundary().length ; k++ )
			{
				LineSegment b=LineSegment.GetLineSegment( t.getInnerBoundary()[k] );
				if( null!=l_right && b.IntersectWith(l_right) )
					l_right=null;
				if( null!=l_left && b.IntersectWith(l_left) )
					l_left=null;
				if( null!=l_up && b.IntersectWith(l_up) )
					l_up=null;
				if( null!=l_down && b.IntersectWith(l_down) )
					l_down=null;
			}
			for( int k=0 ; k<t.getOuterBoundary().length ; k++ )
			{
				LineSegment b=LineSegment.GetLineSegment( t.getOuterBoundary()[k] );
				if( null!=l_right && b.IntersectWith(l_right) )
					l_right=null;
				if( null!=l_left && b.IntersectWith(l_left) )
					l_left=null;
				if( null!=l_up && b.IntersectWith(l_up) )
					l_up=null;
				if( null!=l_down && b.IntersectWith(l_down) )
					l_down=null;
			}
			
			LineSegment b=LineSegment.GetLineSegment( t.getFinishLine() );
			if( null!=l_right && b.IntersectWith(l_right) )
			{
				l_right=null;
				goalPoints.add( p_right );
			}
			if( null!=l_left && b.IntersectWith(l_left) )
			{
				l_left=null;
				goalPoints.add( p_left );
			}
			if( null!=l_up && b.IntersectWith(l_up) )
			{
				l_up=null;
				goalPoints.add( p_up );
			}
			if( null!=l_down && b.IntersectWith(l_down) )
			{
				l_down=null;
				goalPoints.add( p_down );
			}
			if( null!=l_right && !closedList.contains( p_right ) )
			{
				openList.add( p_right );
				closedList.add( p_right );
			}
			if( null!=l_left && !closedList.contains( p_left ) )
			{
				openList.add( p_left );
				closedList.add( p_left );
			}
			if( null!=l_up && !closedList.contains( p_up ) )
			{
				openList.add( p_up );
				closedList.add( p_up );
			}
			if( null!=l_down && !closedList.contains( p_down ) )
			{
				openList.add( p_down );
				closedList.add( p_down );
			}
			cP=openList.poll();
		}
		return closedList;
	}
	
	private boolean createGrid( int sX, int sY )
	{
		mWidth=((int)mGame.getTrack().getDimension().getX());
		mHeight=((int)mGame.getTrack().getDimension().getY());
		mGrid=new boolean[mWidth][mHeight];
		boolean [][] considered=new boolean[mWidth][mHeight];

		sX=(int)mGame.getTrack().getStartingPoints()[0].getX();
		sY=(int)mGame.getTrack().getStartingPoints()[0].getY();
	
		grid = iterativeTryFill();
		//tryFill( sX, sY, sX, sY, considered );
		
		if( mVerbose )
		{
			System.out.println( "Grid created!" );
			
			//mGrid=new boolean[x][y];
	
			for( int j=mHeight-1 ; j>=0 ; j-- )
			{
				for( int i=0 ; i<mWidth ; i++ )
				{
					if( grid.contains( new Point2D( i, j ) ) )
					{
						System.out.print( " " );
						mGrid[i][j]=true;	
					}
					else
					{
						System.out.print( "X" );
						mGrid[i][j]=false;
					}
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
	private ArrayList< LandingRegion > searchLandingRegions()
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
		
		ArrayList<LandingRegion> ret= new ArrayList<LandingRegion>();
		ArrayList<Integer> idOfDijkstraShortestPathForSorting=new ArrayList<Integer>();
		//ArrayList<Point2D> tmpLandingRegion;
		AtomicReference<Integer> w=new AtomicReference<Integer>( 0 );
		AtomicReference<Integer> landingRegionWidth=new AtomicReference<Integer>( 0 );
		AtomicReference<Integer> landingRegionAdditionalHeight=new AtomicReference<Integer>( 0 );
		AtomicReference<Integer> idForSorting=new AtomicReference<Integer>( 0 );
		LandingRegion tmpLandingRegion=null;
		for( int i=0 ; i<mWidth-1 ; i++ )
		{
			for( int j=0 ; j<mHeight-1 ; j++ )
			{
				boolean horizontalDirectionRight=false;
				Point2D start=new Point2D( 0, 0 );
				Point2D end=new Point2D( 0, 0 );
				Point2D origin=new Point2D( 0, 0 );
				AIUtils.Direction oldDirection = AIUtils.Direction.UP;
				AIUtils.Direction newDirection = AIUtils.Direction.UP;
				tmpLandingRegion=null;
				if(    false==mGrid[i][j+1]   &&   true==mGrid[i+1][j+1]   &&
					    true==mGrid[i][j]     &&   true==mGrid[i+1][j]     )
				{
					//#.
					//..
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i, j ), true, idForSorting );
					if( 0==w.get() )
					{
						calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i, j, true );
						maxSpeedDominantDirection = landingRegionAdditionalHeight.get();
						maxSpeedOtherDirection = landingRegionWidth.get();
					    LandingRegion.setMaxSpeeds( maxSpeedOtherDirection, maxSpeedDominantDirection );
						LandingRegion.setLandingRegionSpeedMatrix( w.get() );
					}
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
						oldDirection = AIUtils.Direction.RIGHT;
						newDirection = AIUtils.Direction.UP;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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
						oldDirection = AIUtils.Direction.DOWN;
						newDirection = AIUtils.Direction.LEFT;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i+1, j ), true, idForSorting );
					if( 0==w.get() )
					{
						calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i, j, true );
						maxSpeedDominantDirection = landingRegionAdditionalHeight.get();
						maxSpeedOtherDirection = landingRegionWidth.get();
					    LandingRegion.setMaxSpeeds( maxSpeedOtherDirection, maxSpeedDominantDirection );
						LandingRegion.setLandingRegionSpeedMatrix( w.get() );
					}
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
						oldDirection = AIUtils.Direction.DOWN;
						newDirection = AIUtils.Direction.RIGHT;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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
						oldDirection = AIUtils.Direction.LEFT;
						newDirection = AIUtils.Direction.UP;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i, j+1 ), false, idForSorting );
					if( 0==w.get() )
					{
						calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i, j, true );
						maxSpeedDominantDirection = landingRegionAdditionalHeight.get();
						maxSpeedOtherDirection = landingRegionWidth.get();
					    LandingRegion.setMaxSpeeds( maxSpeedOtherDirection, maxSpeedDominantDirection );
						LandingRegion.setLandingRegionSpeedMatrix( w.get() );
					}
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
						oldDirection = AIUtils.Direction.RIGHT;
						newDirection = AIUtils.Direction.DOWN;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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
						oldDirection = AIUtils.Direction.UP;
						newDirection = AIUtils.Direction.LEFT;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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
					horizontalDirectionRight=isHorizontalDirectionRight( new Point2D( i+1, j+1 ), false, idForSorting );
					if( 0==w.get() )
					{
						calcWidthAndLandingRegion( w, landingRegionWidth, landingRegionAdditionalHeight, i, j, true );
						maxSpeedDominantDirection = landingRegionAdditionalHeight.get();
						maxSpeedOtherDirection = landingRegionWidth.get();
					    LandingRegion.setMaxSpeeds( maxSpeedOtherDirection, maxSpeedDominantDirection );
						LandingRegion.setLandingRegionSpeedMatrix( w.get() );
					}
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
						oldDirection = AIUtils.Direction.UP;
						newDirection = AIUtils.Direction.RIGHT;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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
						oldDirection = AIUtils.Direction.LEFT;
						newDirection = AIUtils.Direction.DOWN;
						tmpLandingRegion=new LandingRegion( origin, start, end, oldDirection, newDirection, w.get(), landingRegionAdditionalHeight.get(), landingRegionWidth.get() );
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

				if( null!=tmpLandingRegion )
				{
					regionFoundFirstIdOrder++;
					
					ArrayList<LandingPoint> zzv=tmpLandingRegion.getLandingPoints();
					//TODO: REMOVE THIS:
					if( mVerbose )
					{
						System.out.println( ""+regionFoundFirstIdOrder+": " );
						for( int x=0 ; x<zzv.size() ;  x++ )
						{
							System.out.println( zzv.get( x ) );
						}
					}
					
					
					if( idOfDijkstraShortestPathForSorting.size() == 0 )
					{
						idOfDijkstraShortestPathForSorting.add( idForSorting.get() );
						ret.add( tmpLandingRegion );
						landingRegionOldDirections.add(oldDirection);
						landingRegionNewDirections.add(newDirection);
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
								landingRegionOldDirections.add(k+1, oldDirection);
								landingRegionNewDirections.add(k+1, newDirection);
								break;
							}
						}
						if( !foundSlot )
						{
							if( idForSorting.get() < idOfDijkstraShortestPathForSorting.get( 0 ) )
							{
								idOfDijkstraShortestPathForSorting.add( 0, idForSorting.get() );
								ret.add( 0, tmpLandingRegion );
								landingRegionOldDirections.add(0, oldDirection);
								landingRegionNewDirections.add(0, newDirection);
							}
							else
							{
								idOfDijkstraShortestPathForSorting.add( idForSorting.get() );
								ret.add( tmpLandingRegion );
								landingRegionOldDirections.add(oldDirection);
								landingRegionNewDirections.add(newDirection);
							}
						}
					}
				}
			}
		}

		
		for( int i=0 ; i<ret.size() ; i++ )
		{
			ArrayList<Point2D> lr=ret.get(i).getAllPositions();
			for( int k=0 ; k<lr.size() ; k++ )
			{
				mLandingRegions[(int)lr.get(k).getX()][(int)lr.get(k).getY()]=i+1;
			}
		}
		
		return ret;
	}
	
	private void calcWidthAndLandingRegion( AtomicReference<Integer> w, AtomicReference<Integer> landingRegionWidth, AtomicReference<Integer> landingRegionAdditionalHeight, int i, int j, boolean searchDownwards )
	{
		w.set( calcWidth( new Point2D( i, j ), searchDownwards ) );
		landingRegionWidth.set( (int) Math.floor(Math.sqrt(w.get()*2+1.75)-0.5) );
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
	
	private void makeViewMap()
	{
		int [][] mViewMap;
		mViewMap=new int[mWidth][mHeight];
		for( int j=mHeight-1 ; j>=0 ; j-- )
		{
			for( int i=0 ; i<mWidth ; i++ )
			{
				mViewMap[i][j]=mLandingRegions[i][j];
			}
		}
		
		
		
	}
}
