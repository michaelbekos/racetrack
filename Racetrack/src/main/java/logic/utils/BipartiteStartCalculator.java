package src.main.java.logic.utils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Point2D;
import src.main.java.logic.LandingPoint;
import src.main.java.logic.LandingRegion;
import src.main.java.logic.utils.AIUtils.Direction;

public class BipartiteStartCalculator
{
	private LandingRegion mEndLR;

	private LandingPoint mStartLandingPoint;
	private ArrayList<LandingPoint> mEndLRVertex;
	private boolean [][] mEntireGrid;
	
	private LandingPoint mAreaStartLandingPoint;
	private ArrayList<LandingPoint> mAreaEndLRVertex;	
	private boolean [][] mAreaGrid;
	
	private LandingPoint mTransformedStartLandingPoint;
	private ArrayList<LandingPoint> mTransformedEndLRVertex;	
	private boolean [][] mTransformedAreaGrid;
	
	private int [] mWeights;
	
	private Direction mMain; 
	private Direction mOut;
    private int mW;
    
    // Transformation information
	private boolean mFlipArroundXAxis;
	private boolean mFlipArroundYAxis;
	private boolean mRotate90DegreeClockwise;
	
	// 
	private Point2D mStartAreaOfPath;
	private Point2D mEndAreaOfPath;
	
    public BipartiteStartCalculator( 	Point2D startPoint, 
            							LandingRegion endLR, 
            							Direction main, 
            							Direction out, 
                                        int w,
                                        //ArrayList<LineSegment> borders,
                                        boolean [][] entireGrid )
    {
    	mEndLR=endLR;
    	mMain=main; 
    	mOut=out; 
        mW=w;
    	//mBorders=borders;
    	mEntireGrid=entireGrid;

    	mStartLandingPoint=new LandingPoint( startPoint, new Point2D( 0, 0 ) ); 
    	mEndLRVertex=   mEndLR.getLandingPoints();
    	
    	calcAreaFlippingRotationUturn();
    	createGridTransform();
    	calcWeights();
    }

    private void createGridTransform()
    {
    	mAreaEndLRVertex=new ArrayList<LandingPoint>();	

    	int grid_width=(int)( mEndAreaOfPath.getX()-mStartAreaOfPath.getX()+1 );
    	int grid_height=(int)( mEndAreaOfPath.getY()-mStartAreaOfPath.getY()+1 );
    	
    	mAreaGrid=new boolean[grid_width][grid_height];
    	for( int ii=0, i=(int)mStartAreaOfPath.getX() ; i<mEndAreaOfPath.getX()+1 ; i++, ii++ )
    	{
    		for( int jj=0, j=(int)mStartAreaOfPath.getY() ; j<mEndAreaOfPath.getY()+1 ; j++, jj++ )
        	{
    			mAreaGrid[ii][jj]=mEntireGrid[i][j];
            	Point2D p=new Point2D( i, j ); 
        		if( mStartLandingPoint.isAtPosition( p ) )
        		{
        			mAreaStartLandingPoint = new LandingPoint( p, mStartLandingPoint.getSpeed() );
        		}
    			
    			for( int k=0 ; k<mEndLRVertex.size() ; k++ )
            	{//Search all 'wrong' vertex and remove them
            		if( mEndLRVertex.get( k ).isAtPosition( p ) )
            		{
            			mAreaEndLRVertex.add( new LandingPoint( p, mEndLRVertex.get( k ).getSpeed() ) );            			
            		}
            	}
        	}	
    	}
    	boolean[][] tmpGrid=mAreaGrid;
    	ArrayList<LandingPoint> tmpStartList=new ArrayList<LandingPoint>();
    	tmpStartList.add( mAreaStartLandingPoint );
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
    	mTransformedAreaGrid=tmpGrid;
    	mTransformedStartLandingPoint=tmpStartList.get( 0 );
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
		// All ZigZag turns that are not a complete u turn take this if - branch
		//calcAccelerationsS
		if( mMain==Direction.UP )
		{
			if( mOut==Direction.RIGHT )
			{
				// 1.
				//     -->
				//    ^
				//    |
				//    |
				//
				mStartAreaOfPath=new Point2D( mEndLR.getAreaStartPosition().getX()-1, mStartLandingPoint.getPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mEndLR.getAreaEndPosition().getX()+1, mEndLR.getAreaEndPosition().getY()+1 );
				mFlipArroundXAxis=false;
				mFlipArroundYAxis=false;
				mRotate90DegreeClockwise=false;
			}
			else
			{
				// 2.
				//    <--
				//       ^
				//       |
				//       |
				//
				mStartAreaOfPath=new Point2D( mEndLR.getAreaStartPosition().getX()-1, mStartLandingPoint.getPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mEndLR.getAreaEndPosition().getX()+1, mEndLR.getAreaStartPosition().getY()+1 );
				mFlipArroundXAxis=true;
				mFlipArroundYAxis=false;
				mRotate90DegreeClockwise=false;
			}
		}
		else if( mMain==Direction.DOWN )
		{
			if( mOut==Direction.RIGHT )
			{
				// 3.
				//    |
				//    |
				//    v
				//     -->
				//	
				mStartAreaOfPath=new Point2D( mEndLR.getAreaStartPosition().getX()-1, mEndLR.getAreaEndPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mEndLR.getAreaEndPosition().getX()+1, mStartLandingPoint.getPosition().getY()+1 );
				mFlipArroundXAxis=false;
				mFlipArroundYAxis=true;
				mRotate90DegreeClockwise=false;
			}
			else
			{
				// 4.
				//    |
				//    |
				//    v
				// <--
				//
				mStartAreaOfPath=new Point2D( mEndLR.getAreaStartPosition().getX()-1, mEndLR.getAreaEndPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mEndLR.getAreaEndPosition().getX()+1, mStartLandingPoint.getPosition().getY()+1 );
				mFlipArroundXAxis=true;
				mFlipArroundYAxis=true;
				mRotate90DegreeClockwise=false;
			}				
		}
		else if( mMain==Direction.RIGHT )
		{
			if( mOut==Direction.UP )
			{
				// 5.
				//         ^
				//         |
				//     --->
				//	
				mStartAreaOfPath=new Point2D( mStartLandingPoint.getPosition().getX()-1, mEndLR.getAreaEndPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mEndLR.getAreaEndPosition().getX()+1, mEndLR.getAreaStartPosition().getY()+1 );
				mFlipArroundXAxis=false;
				mFlipArroundYAxis=true;
				mRotate90DegreeClockwise=true;					
			}
			else
			{
				// 6.
				//     --->
				//         |
				//         v
				//	
				mStartAreaOfPath=new Point2D( mStartLandingPoint.getPosition().getX()-1, mEndLR.getAreaEndPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mEndLR.getAreaEndPosition().getX()+1, mEndLR.getAreaStartPosition().getY()+1 );
				mFlipArroundXAxis=true;
				mFlipArroundYAxis=true;
				mRotate90DegreeClockwise=true;					
			}
		}
		else if( mMain==Direction.LEFT )
		{
			if( mOut==Direction.UP )
			{
				// 7.
				//    ^
				//    |
				//     <---
				//	
				mStartAreaOfPath=new Point2D( mEndLR.getAreaStartPosition().getX()-1, mEndLR.getAreaEndPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mStartLandingPoint.getPosition().getX()+1, mEndLR.getAreaStartPosition().getY()+1 );
				mFlipArroundXAxis=false;
				mFlipArroundYAxis=false;
				mRotate90DegreeClockwise=true;					
			}
			else
			{
				// 8.
				//     <---
				//    |
				//    v
				//	
				mStartAreaOfPath=new Point2D( mEndLR.getAreaStartPosition().getX()-1, mEndLR.getAreaEndPosition().getY()-1 );
				mEndAreaOfPath=new Point2D( mStartLandingPoint.getPosition().getX()+1, mEndLR.getAreaStartPosition().getY()+1 );
				mFlipArroundXAxis=true;
				mFlipArroundYAxis=false;
				mRotate90DegreeClockwise=true;					
			}
		}
	}

	

	private void calcWeights()
	{
		mWeights= new int [mTransformedEndLRVertex.size()];
		for( int j=0 ; j<mTransformedEndLRVertex.size() ; j++ )
		{
			mWeights[j]=calcWeight( this.mTransformedStartLandingPoint, mTransformedEndLRVertex.get( j ) );
			mTransformedEndLRVertex.get( j ).setDistance( mWeights[j] );
			mTransformedEndLRVertex.get( j ).setPredecessor( mTransformedStartLandingPoint );
		}
	}
	
	public ArrayList<LandingPoint> getDistancePredecessorInformation()
	{
		return mTransformedEndLRVertex;
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
		int x=to-t-from;
		// incase we have two minimas, these are the two points of "intersection"
		int x1=0,x2=0;
		boolean twoMinima=( x%2==1 );
		ArrayList<Integer> ret=new ArrayList<Integer>();
		int integral=0;
		int tmpVal=0;
		if( twoMinima )
		{
			x1=( x-1 )/2;
			x2=( x+1 )/2;

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
			x/=2;
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

	private ArrayList<Integer> fill( ArrayList<Integer> speeds )
	{
		boolean successfulFilledOne=false;
		ArrayList<Integer> differences=new ArrayList<Integer>();
		for( int i=0 ; i<speeds.size()-1; i++ )
		{
			differences.add( speeds.get( i+1 )-speeds.get( i ) );
		}
		// Search -1, 1
		for( int i=0 ; i<differences.size()-1 && !successfulFilledOne ; i++ )
		{
			if( -1==differences.get( i ) && 1==differences.get( i+1 ) )
			{
				differences.set( i, 0 );
				differences.set( i+1, 0 );
				successfulFilledOne=true;
			}
		}
		// Search -1, 0
		for( int i=0 ; i<differences.size()-1 && !successfulFilledOne; i++ )
		{
			if( -1==differences.get( i ) && 0==differences.get( i+1 ) )
			{
				differences.set( i, 0 );
				differences.set( i+1, -1 );
				successfulFilledOne=true;
			}
		}
		// Search 0, 1
		for( int i=0 ; i<differences.size()-1 && !successfulFilledOne; i++ )
		{
			if( 0==differences.get( i ) && 1==differences.get( i+1 ) )
			{
				differences.set( i, 1 );
				differences.set( i+1, 0 );
				successfulFilledOne=true;
			}
		}
		// Search 0, 0
		for( int i=0 ; i<differences.size()-1 && !successfulFilledOne; i++ )
		{
			if( 0==differences.get( i ) && 1==differences.get( i+1 ) )
			{
				differences.set( i, 1 );
				differences.set( i+1, -1 );
				successfulFilledOne=true;
			}
		}
		ArrayList<Integer> ret=new ArrayList<Integer>();
		ret.add( speeds.get( 0 ) );
		for( int i=0 ; i<differences.size(); i++ )
		{
			ret.add( ret.get( i )+differences.get( i ) );
		}
		return ret;
	}
}
