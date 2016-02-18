package src.main.java.logic.utils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Point2D;
import src.main.java.logic.LandingPoint;
import src.main.java.logic.LandingRegion;
import src.main.java.logic.utils.AIUtils.Direction;

public class BipartiteNormalCalculator
{
	private AtomicReference<LandingRegion> mStartLR; 
	private AtomicReference<LandingRegion> mEndLR;

	private boolean [][] mEntireGrid;
	
	private ArrayList<LandingPoint> mAreaStartLRVertex;
	private ArrayList<LandingPoint> mAreaEndLRVertex;	
	private boolean [][] mAreaGrid;
	
	private ArrayList<LandingPoint> mTransformedStartLRVertex;
	private ArrayList<LandingPoint> mTransformedEndLRVertex;	
	//private boolean [][] mTransformedAreaGrid;
	
	private int [][] mWeights;
	
	private Direction mIn;
	private Direction mMain; 
	private Direction mOut;
	private boolean   mIsUTurn;
	//private ArrayList<LineSegment> mBorders; 
    private int mW;
    
    // Transformation information
	private boolean mFlipArroundXAxis;
	private boolean mFlipArroundYAxis;
	private boolean mRotate90DegreeClockwise;
	
	// 
	private Point2D mStartAreaOfPath;
	private Point2D mEndAreaOfPath;

       
    public BipartiteNormalCalculator( 	AtomicReference<LandingRegion> startLR, 
    									AtomicReference<LandingRegion> endLR, 
                                        boolean [][] entireGrid )
    {
    	mStartLR=startLR; 
    	mEndLR=endLR;
    	mIn=startLR.get().getOldDirection();
    	mMain=startLR.get().getNewDirection(); 
    	mOut=endLR.get().getNewDirection(); 
        mW=startLR.get().getW();
    	mEntireGrid=entireGrid;
    	
    	//mStartLRVertex= mStartLR.get().getLandingPoints(); 
    	//mEndLRVertex=   mEndLR.get().getLandingPoints();
    	
    	calcAreaFlippingRotationUturn();
    	createGridTransform();
    	calcWeights();
    }
    

    private void createGridTransform()
    {
    	mAreaStartLRVertex=new ArrayList<LandingPoint>();
    	mAreaEndLRVertex=new ArrayList<LandingPoint>();	

    	int grid_width=(int)( mEndAreaOfPath.getX()-mStartAreaOfPath.getX()+1 );
    	int grid_height=(int)( mEndAreaOfPath.getY()-mStartAreaOfPath.getY()+1 );
    	
    	mAreaGrid=new boolean[grid_width][grid_height];
    	for( int ii=0, i=(int)mStartAreaOfPath.getX() ; i<mEndAreaOfPath.getX()+1 ; i++, ii++ )
    	{
    		for( int jj=0, j=(int)mStartAreaOfPath.getY() ; j<mEndAreaOfPath.getY()+1 ; j++, jj++ )
        	{
    			// For every point on of the area ( sub section of entire grid)
    			mAreaGrid[ii][jj]=mEntireGrid[i][j];
            	Point2D p=new Point2D( i, j ); 
    			for( int k=0 ; k<mStartLR.get().getLandingPoints().get().size() ; k++ )
            	{
            		if( mStartLR.get().getLandingPoints().get().get( k ).isAtPosition( p ) )
            		{
            			// If we have a LandingPoint, then translate it to the new position
            			// (Speed stay the same because we don't flip/rotate yet.)
            			mAreaStartLRVertex.add( new LandingPoint( p, mStartLR.get().getLandingPoints().get().get( k ).getSpeed() ) );
            		}
            	}
    			
    			for( int k=0 ; k<mEndLR.get().getLandingPoints().get().size() ; k++ )
            	{//Search all 'wrong' vertex and remove them
            		if( mEndLR.get().getLandingPoints().get().get( k ).isAtPosition( p ) )
            		{
            			// If we have a LandingPoint, then translate it to the new position
            			// (Speed stay the same because we don't flip/rotate yet.)
            			mAreaEndLRVertex.add( new LandingPoint( p, mEndLR.get().getLandingPoints().get().get( k ).getSpeed() ) );            			
            		}
            	}
        	}
    	}
    	boolean[][] tmpGrid=mAreaGrid;
    	ArrayList<LandingPoint> tmpStartList=mAreaStartLRVertex;
    	ArrayList<LandingPoint> tmpEndList=mAreaEndLRVertex;
    	// FlipX
    	if( mFlipArroundXAxis )
    	{
    		tmpGrid=flipX( tmpGrid, grid_width, grid_height );
			tmpStartList=flipXVertex( tmpStartList, grid_width, grid_height );
			tmpEndList=flipXVertex( tmpEndList, grid_width, grid_height );
    	}
    	// FlipY
    	if( mFlipArroundYAxis )
    	{
    		tmpGrid=flipY( tmpGrid, grid_width, grid_height );
			tmpStartList=flipYVertex( tmpStartList, grid_width, grid_height );
			tmpEndList=flipYVertex( tmpEndList, grid_width, grid_height );
    	}
    	// Rot
    	if( mRotate90DegreeClockwise )
    	{
    		tmpGrid=rotate90DegreeClockwise( tmpGrid, grid_width, grid_height );
			tmpStartList=rotate90DegreeClockwiseVertex( tmpStartList, grid_width, grid_height );
			tmpEndList=rotate90DegreeClockwiseVertex( tmpEndList, grid_width, grid_height );
    	}
    	//mTransformedAreaGrid=tmpGrid;
    	mTransformedStartLRVertex=tmpStartList;
    	mTransformedEndLRVertex=tmpEndList;
    }

    private boolean[][] flipX( boolean[][] grid, int grid_width, int grid_height )
    {
    	boolean[][] flippedX = new boolean[grid_width][grid_height];
    	for( int i=0 ; i<grid_width ; i++ )
    	{
    		for( int j=0 ; j<grid_height ; j++ )
        	{
    			flippedX[i][grid_height-j-1]=grid[i][j];
        	}	
    	}
    	return flippedX;
    }
    
    private boolean[][] flipY( boolean[][] grid, int grid_width, int grid_height )
    {
    	boolean[][] flippedY = new boolean[grid_width][grid_height];
    	for( int i=0 ; i<grid_width ; i++ )
    	{
    		for( int j=0 ; j<grid_height ; j++ )
        	{
    			flippedY[grid_width-i-1][j]=grid[i][j];
        	}	
    	}
    	return flippedY;
    }
    
    private boolean[][] rotate90DegreeClockwise( boolean[][] grid, int grid_width, int grid_height )
    {
    	boolean[][] rot = new boolean[grid_height][grid_width];
    	for( int i=0 ; i<grid_height ; i++ )
    	{
    		for( int j=0 ; j<grid_width ; j++ )
        	{
    			rot[i][j]=grid[grid_width-j-1][i];
        	}	
    	}
    	return rot;
    }
    
    private ArrayList<LandingPoint> flipXVertex( ArrayList<LandingPoint> inList, int grid_width, int grid_height )
    {
    	ArrayList<LandingPoint> ret=new ArrayList<LandingPoint>();
    	for( int k=0 ; k<inList.size(); k++ )
    	{
    		ret.add( new LandingPoint(  
    					new Point2D( inList.get( k ).getPosition().getX(), 
    							     grid_height-inList.get( k ).getPosition().getY()-1 ),
						new Point2D(  inList.get( k ).getSpeed().getX(),
									 -inList.get( k ).getSpeed().getY() ) ) );
    		
    	}
    	return ret;
    }
    
    private ArrayList<LandingPoint> flipYVertex( ArrayList<LandingPoint> inList, int grid_width, int grid_height )
    {
    	ArrayList<LandingPoint> ret=new ArrayList<LandingPoint>();
    	for( int k=0 ; k<inList.size(); k++ )
    	{
    		ret.add( new LandingPoint(  
    					new Point2D( grid_width-inList.get( k ).getPosition().getX()-1, 
    							     inList.get( k ).getPosition().getY() ),
						new Point2D( -inList.get( k ).getSpeed().getX(),
									  inList.get( k ).getSpeed().getY() ) ) );
    		
    	}
    	return ret;
    }
    
    private ArrayList<LandingPoint> rotate90DegreeClockwiseVertex( ArrayList<LandingPoint> inList, int grid_width, int grid_height )
    {
    	ArrayList<LandingPoint> ret=new ArrayList<LandingPoint>();
    	for( int k=0 ; k<inList.size(); k++ )
    	{
    		ret.add( new LandingPoint(  
    					new Point2D( inList.get( k ).getPosition().getY(), 
    							     grid_width-inList.get( k ).getPosition().getX()-1 ),
						new Point2D(  inList.get( k ).getSpeed().getY(),
									 -inList.get( k ).getSpeed().getX() ) ) );
    		
    	}
    	return ret;
    }
    
	private void calcAreaFlippingRotationUturn()
	{
		mIsUTurn=( mIn!=mOut );
		if( !mIsUTurn )
		{
			// All ZigZag turns that are not a complete u turn take this if - branch
			//calcAccelerationsS
			if( mIn==Direction.RIGHT )
			{
				if( mMain==Direction.UP )
				{
					// 1.
					//     -->
					//    ^
					//    |
					//    |
					// -->
					//
					mStartAreaOfPath=new Point2D( mStartLR.get().getAreaStartPosition().getX()-1, mStartLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mEndLR.get().getAreaEndPosition().getY()+mW );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=false;
				}
				else
				{
					// 2.
					// -->
					//    |
					//    |
					//    v
					//     -->
					//
					mStartAreaOfPath=new Point2D( mStartLR.get().getAreaStartPosition().getX()-1, mEndLR.get().getAreaStartPosition().getY()-mW );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mStartLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=false;
				}
			}
			else if( mIn==Direction.LEFT )
			{
				if( mMain==Direction.UP )
				{
					// 3.
					// <--
					//    ^
					//    |
					//    |
					//     <--
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()-1, mStartLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mEndLR.get().getAreaEndPosition().getY()+1 );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=false;
				}
				else
				{
					// 4.
					//     <--
					//    |
					//    |
					//    v
					// <--
					//
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()-1, mEndLR.get().getAreaStartPosition().getY()-mW );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mStartLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=false;
				}				
			}
			else if( mIn==Direction.DOWN )
			{
				if( mMain==Direction.RIGHT )
				{
					// 5.
					//    |
					//    v
					//     --->
					//         |
					//         v
					//	
					mStartAreaOfPath=new Point2D( mStartLR.get().getAreaStartPosition().getX()-1, mEndLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()+mW, mStartLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=true;					
				}
				else
				{
					// 6.
					//         |
					//         v
					//     <---
					//    |
					//    v
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()-mW, mEndLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mStartLR.get().getAreaEndPosition().getX()+1, mStartLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=true;					
				}
			}
			else if( mIn==Direction.UP )
			{
				if( mMain==Direction.RIGHT )
				{
					// 7.
					//    ^
					//    |
					//     <---
					//         ^
					//         |
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()-mW, mEndLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mStartLR.get().getAreaEndPosition().getX()+1, mEndLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=true;					
				}
				else
				{
					// 8.
					//         ^
					//         |
					//     --->
					//    ^
					//    |
					//	
					mStartAreaOfPath=new Point2D( mStartLR.get().getAreaStartPosition().getX()-1, mStartLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()+mW, mEndLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=true;					
				}
			}			
		}
		else
		{ 
			// All turns of Indianapolis would take this else-branch.
			//calcAccelerationsC
			if( mIn==Direction.RIGHT )
			{
				if( mMain==Direction.UP )
				{
					// 1.
					//     <---
					//         ^
					//         |
					//     --->
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()-1, mStartLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mEndLR.get().getAreaEndPosition().getY()+mW );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=false;
				}
				else
				{
					// 2.
					//     --->
					//         |
					//         v
					//     <---
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()-1, mEndLR.get().getAreaStartPosition().getY()-mW );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mStartLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=false;
				}
			}
			else if( mIn==Direction.LEFT )
			{
				if( mMain==Direction.UP )
				{
					// 3.
					//     --->
					//    ^
					//    |
					//     <---
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()-1, mStartLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mEndLR.get().getAreaEndPosition().getY()+mW );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=false;
				}
				else
				{
					// 4.
					//     <---
					//    |
					//    v
					//     --->
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()-1, mEndLR.get().getAreaStartPosition().getY()-mW );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()+1, mStartLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=false;
				}
			}
			else if( mIn==Direction.UP )
			{
				if( mMain==Direction.LEFT )
				{
					// 5.
					//     <---
					//    |    ^
					//    v    |
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()-mW, mEndLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mStartLR.get().getAreaEndPosition().getX()+1, mEndLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=true;
				}
				else
				{
					// 6.
					//     --->
					//    ^    |
					//    |    v
					//	
					mStartAreaOfPath=new Point2D( mStartLR.get().getAreaStartPosition().getX()-1, mEndLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()+mW, mEndLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=false;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=true;
				}
			}
			else if( mIn==Direction.DOWN )
			{
				if( mMain==Direction.LEFT )
				{
					// 7.
					//    ^    |
					//    |    v
					//     <---
					//	
					mStartAreaOfPath=new Point2D( mEndLR.get().getAreaEndPosition().getX()-mW, mEndLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mStartLR.get().getAreaEndPosition().getX()+1, mEndLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=false;
					mRotate90DegreeClockwise=true;
				}
				else
				{
					// 8.
					//    |    ^
					//    v    |
					//     --->
					//	
					mStartAreaOfPath=new Point2D( mStartLR.get().getAreaStartPosition().getX()-1, mEndLR.get().getAreaEndPosition().getY()-1 );
					mEndAreaOfPath=new Point2D( mEndLR.get().getAreaStartPosition().getX()+mW, mEndLR.get().getAreaStartPosition().getY()+1 );
					mFlipArroundXAxis=true;
					mFlipArroundYAxis=true;
					mRotate90DegreeClockwise=true;
				}
			}
		}
	}

	private void calcWeights()
	{
		AtomicReference< ArrayList<LandingPoint> > allEndLandingPoints=mEndLR.get().getLandingPoints();
		AtomicReference< ArrayList<LandingPoint> > allStartLandingPoints=mStartLR.get().getLandingPoints();
		mWeights= new int [mTransformedStartLRVertex.size()][mTransformedEndLRVertex.size()];
		for( int i=0 ; i<mTransformedStartLRVertex.size() ; i++ )
		{
			for( int j=0 ; j<mTransformedEndLRVertex.size() ; j++ )
			{
				System.out.println( "  from:" + mEndLR.get().getLandingPoints().get().get( i ) + "; to:" + mEndLR.get().getLandingPoints().get().get( j ) + "." );
				mWeights[i][j]=calcWeight( mTransformedStartLRVertex.get( i ), mTransformedEndLRVertex.get( j ) );
				if( null==allEndLandingPoints.get().get( j ).getPredecessor() ||
					( mWeights[i][j]+allStartLandingPoints.get().get( i ).getDistance() )<allEndLandingPoints.get().get( j ).getDistance() )
				{
					allEndLandingPoints.get().get( j ).setDistance( mWeights[j][j]+allStartLandingPoints.get().get( i ).getDistance() );
					allEndLandingPoints.get().get( j ).setPredecessor( allStartLandingPoints.get().get( i ) );
				}
			}	
		}
	}
	
	public int calcWeight( LandingPoint from, LandingPoint to )
	{
		ArrayList<Integer> xSpeeds;
		ArrayList<Integer> ySpeeds;
		int t=(int)Math.max( Math.abs( to.getSpeed().getX()-from.getSpeed().getX() ), Math.abs( to.getSpeed().getY()-from.getSpeed().getY() ) );
		t=Math.max( t, 1 );
		int dx=(int)( to.getPosition().getX()-from.getPosition().getX() );
		int dy=(int)( to.getPosition().getY()-from.getPosition().getY() );
		int ix=0;
		int iy=0;
		boolean restart=false;
		while( true )
		{
			restart=false;
			AtomicReference<Integer> integralX=new AtomicReference<Integer>();
			xSpeeds=createGraph( (int)from.getSpeed().getX(), (int)to.getSpeed().getX(), t, integralX );
			ix=integralX.get();	
			while( dx!=ix )
			{
				if( !fillable( xSpeeds ) || ix>dx )
				{
					t++;
					restart=true;
					break;
				}
				else
				{
					xSpeeds=fill( xSpeeds );
					ix++;
				}
			}
			if( restart )
				continue;
			AtomicReference<Integer> integralY=new AtomicReference<Integer>();
			ySpeeds=createGraph( (int)from.getSpeed().getY(), (int)to.getSpeed().getY(), t, integralY );
			iy=integralY.get();
			while( dy!=iy )
			{
				if( !fillable( ySpeeds ) || iy>dy )
				{
					t++;
					restart=true;
					break;
				}
				else
				{
					ySpeeds=fill( ySpeeds );
					iy++;
				}
			}
			if( restart )
				continue;
			break;
		}
		int Csx=(int)( ( from.getSpeed().getX()+1 )*from.getSpeed().getX()+( to.getSpeed().getX()+1 )*to.getSpeed().getX() )/2;
		int Csy=(int)( ( from.getSpeed().getY()+1 )*from.getSpeed().getY()+( to.getSpeed().getY()+1 )*to.getSpeed().getY() )/2;
		int s_max_x=(int)( Math.sqrt( dx+Csx+0.25 )-0.5 );
		int s_max_y=(int)( Math.sqrt( dy+Csy+0.25 )-0.5 );
		int t_f_x=(int)( 2*s_max_x-from.getSpeed().getX()-to.getSpeed().getX() );
		int t_f_y=(int)( 2*s_max_y-from.getSpeed().getY()-to.getSpeed().getY() );
		int t_f=Math.max( t_f_x, t_f_y );
		System.out.println( "Real t:" + t + "; Formula t:" + t_f + "." );
		return t;
	}

	private ArrayList<Integer> createGraph( int from, int to, int t, AtomicReference<Integer> integralOut )
	{
		//Point2D pFrom=new Point2D( 0, from );
		//Point2D pTo=new Point2D( t, to );
		int n_f=from;
		int m_f=-1;
		int n_to=to-t;
		int m_to=1;
		// x is the point of intersection
		double xx=((double)(n_to-n_f))/(m_f-m_to);
		// incase we have two minimas, these are the two points of "intersection"
		int x1=0,x2=0;
		boolean twoMinima=( ( xx-Math.floor( xx ) )>=0.1 );
		ArrayList<Integer> ret=new ArrayList<Integer>();
		int integral=0;
		int tmpVal=0;
		if( twoMinima )
		{
			x1=(int)(xx-0.5);
			x2=(int)(xx+0.5);

			ret.add( from );
			for( int i=1 ; i<=x1 ; i++ )
			{
				tmpVal=m_f*i+n_f;
				integral+=tmpVal;
				ret.add( tmpVal );
			}
			for( int i=x2 ; i<=t ; i++ )
			{
				tmpVal=m_to*i+n_to;
				integral+=tmpVal;
				ret.add( tmpVal );
			}
			
		}
		else
		{
			int x=(int)xx;
			ret.add( from );
			for( int i=1 ; i<=x ; i++ )
			{
				tmpVal=m_f*i+n_f;
				integral+=tmpVal;
				ret.add( tmpVal );
			}
			for( int i=x+1 ; i<=t ; i++ )
			{
				tmpVal=m_to*i+n_to;
				integral+=tmpVal;
				ret.add( tmpVal );
			}
		}
		integralOut.set( integral );
		return ret;
	}

	private boolean fillable( ArrayList<Integer> speeds )
	{
		int t=speeds.size()-1;
		int from=speeds.get( 0 );
		int to=speeds.get( t );
		//Point2D pFrom=new Point2D( 0, from );
		//Point2D pTo=new Point2D( t, to );
		int n_f=from;
		int m_f=1;
		int n_to=to+t;
		int m_to=-1;
		int x=to+t-from;
		int x1=0,x2=0;
		boolean twoMaxima=( x%2==1 );
		
		if( twoMaxima )
		{
			x1=( x-1 )/2;
			x2=( x+1 )/2;

			for( int i=1 ; i<=x1 ; i++ )
			{
				if( speeds.get( i )!=( m_f*i+n_f ) )
					return true;
			}
			for( int i=x2 ; i<=t ; i++ )
			{
				if( speeds.get( i )!=( m_to*i+n_to ) )
					return true;
			}			
		}
		else
		{
			x/=2;
			for( int i=1 ; i<=x ; i++ )
			{
				if( speeds.get( i )!=( m_f*i+n_f ) )
					return true;
			}
			for( int i=x+1 ; i<=t ; i++ )
			{
				if( speeds.get( i )!=( m_to*i+n_to ) )
					return true;
			}
		}

		// allSpeedsSetToMax
		return false;		
	}

	private ArrayList<Integer> calculateAccelations( ArrayList<Integer> speeds )
	{
		ArrayList<Integer> ret=new ArrayList<Integer>();
		for( int i=0 ; i<speeds.size()-1; i++ )
		{
			ret.add( speeds.get( i+1 )-speeds.get( i ) );
		}
		return ret;
	}
	
	private ArrayList<Integer> fill( ArrayList<Integer> speeds )
	{
		boolean successfulFilledOne=false;
		ArrayList<Integer> accelerations=calculateAccelations( speeds );
		// Search -1, 1
		for( int i=0 ; i<accelerations.size()-1 && !successfulFilledOne ; i++ )
		{
			if( -1==accelerations.get( i ) && 1==accelerations.get( i+1 ) )
			{
				accelerations.set( i, 0 );
				accelerations.set( i+1, 0 );
				successfulFilledOne=true;
			}
		}
		// Search -1, 0
		for( int i=0 ; i<accelerations.size()-1 && !successfulFilledOne; i++ )
		{
			if( -1==accelerations.get( i ) && 0==accelerations.get( i+1 ) )
			{
				accelerations.set( i, 0 );
				accelerations.set( i+1, -1 );
				successfulFilledOne=true;
			}
		}
		// Search 0, 1
		for( int i=0 ; i<accelerations.size()-1 && !successfulFilledOne; i++ )
		{
			if( 0==accelerations.get( i ) && 1==accelerations.get( i+1 ) )
			{
				accelerations.set( i, 1 );
				accelerations.set( i+1, 0 );
				successfulFilledOne=true;
			}
		}
		// Search 0, 0
		for( int i=0 ; i<accelerations.size()-1 && !successfulFilledOne; i++ )
		{
			if( 0==accelerations.get( i ) && 1==accelerations.get( i+1 ) )
			{
				accelerations.set( i, 1 );
				accelerations.set( i+1, -1 );
				successfulFilledOne=true;
			}
		}
		ArrayList<Integer> ret=new ArrayList<Integer>();
		ret.add( speeds.get( 0 ) );
		for( int i=0 ; i<accelerations.size(); i++ )
		{
			ret.add( ret.get( i )+accelerations.get( i ) );
		}
		return ret;
	}

	public ArrayList<Point2D> getPath( LandingPoint from, LandingPoint to )
	{
		ArrayList<Point2D> ret=new ArrayList<Point2D>();
		ArrayList<Integer> xSpeeds;
		ArrayList<Integer> ySpeeds;
		int t=(int)Math.max( Math.abs( to.getSpeed().getX()-from.getSpeed().getX() ), Math.abs( to.getSpeed().getY()-from.getSpeed().getY() ) );
		t=Math.max( t, 1 );
		int dx=(int)( to.getPosition().getX()-from.getPosition().getX() );
		int dy=(int)( to.getPosition().getY()-from.getPosition().getY() );
		int ix=0;
		int iy=0;
		boolean restart=false;
		while( true )
		{
			restart=false;
			AtomicReference<Integer> integralX=new AtomicReference<Integer>();
			xSpeeds=createGraph( (int)from.getSpeed().getX(), (int)to.getSpeed().getX(), t, integralX );
			ix=integralX.get();	
			while( dx!=ix )
			{
				if( !fillable( xSpeeds ) || ix>dx )
				{
					t++;
					restart=true;
					break;
				}
				else
				{
					xSpeeds=fill( xSpeeds );
					ix++;
				}
			}
			if( restart )
				continue;
			AtomicReference<Integer> integralY=new AtomicReference<Integer>();
			ySpeeds=createGraph( (int)from.getSpeed().getY(), (int)to.getSpeed().getY(), t, integralY );
			iy=integralY.get();
			while( dy!=iy )
			{
				if( !fillable( ySpeeds ) || iy>dy )
				{
					t++;
					restart=true;
					break;
				}
				else
				{
					ySpeeds=fill( ySpeeds );
					iy++;
				}
			}
			if( restart )
				continue;
			break;
		}


		ArrayList<Integer> accX=calculateAccelations( xSpeeds );
		ArrayList<Integer> accY=calculateAccelations( ySpeeds );
		ArrayList<Point2D> realAccelerations=new ArrayList<Point2D>();
		for( int i=0 ; i<accX.size() ; i++ )
		{
			realAccelerations.add( new Point2D( accX.get( i ), accY.get( i ) ) );
		}

		if( mRotate90DegreeClockwise )
			realAccelerations=rotateAccelerations90CounterClockwise( realAccelerations );
		if(mFlipArroundYAxis  )
			realAccelerations=flipAccelerationsOnYAxis( realAccelerations );
		if( mFlipArroundXAxis )
			realAccelerations=flipAccelerationsOnXAxis( realAccelerations );

		Point2D currentPos=from.getPosition();
		Point2D currentSpeed=from.getSpeed();
		for( int i=0 ; i<realAccelerations.size() ; i++ )
		{
			currentSpeed=currentSpeed.add( realAccelerations.get( i ) );
			currentPos=currentPos.add( currentSpeed );
			ret.add( currentPos );
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
