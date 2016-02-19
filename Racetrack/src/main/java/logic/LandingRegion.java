package src.main.java.logic;
import java.util.ArrayList;

import javafx.geometry.Point2D;
//import src.main.java.logic.utils.AIUtils;
import src.main.java.logic.utils.AIUtils.Direction;

public class LandingRegion
{
    private Point2D mOrigin; // Point diagonal to cornor
    private Point2D mCornor; // Point diagonal to cornor
    private Point2D mStart;
    private Point2D mEnd;

    private int mW;  // Track width
    private int mA;  // Landing region additional height
    private int mLW; // Landing region width

    private Direction mOldDirection;
    private Direction mNewDirection;

    static private boolean mVerbose=false;
    static private int recessiveDirectionMaxSpeed;
    static private int dominantDirectionMaxSpeed;

    static private ArrayList<LandingPoint> landingRegionSpeedMatrix;
    //static private boolean isLandingRegionSpeedMatrixSet=false;

    public LandingRegion( Point2D origin, 
                          Point2D start, 
                          Point2D end, 
                          Direction oldDirection, 
                          Direction newDirection, 
                          int w, 
                          int a, 
                          int lW ) 
    {
        mOrigin= origin;
        mStart= start;
        mEnd=    end;

        mW=  w;
        mA=  a;
        mLW= lW;

        mOldDirection= oldDirection;
        mNewDirection= newDirection;
        if(    mOldDirection==Direction.LEFT && mNewDirection==Direction.DOWN ||
    		   mOldDirection==Direction.DOWN && mNewDirection==Direction.LEFT )
        {
        	mCornor = new Point2D( mOrigin.getX()-1, mOrigin.getY()-1 );
        }
        else if( mOldDirection==Direction.LEFT && mNewDirection==Direction.UP ||
    		     mOldDirection==Direction.UP && mNewDirection==Direction.LEFT )
        {
        	mCornor = new Point2D( mOrigin.getX()-1, mOrigin.getY()+1 );
        }
        else if( mOldDirection==Direction.RIGHT && mNewDirection==Direction.DOWN ||
    		     mOldDirection==Direction.DOWN && mNewDirection==Direction.RIGHT )
        {
        	mCornor = new Point2D( mOrigin.getX()+1, mOrigin.getY()-1 );
        }
        else if( mOldDirection==Direction.RIGHT && mNewDirection==Direction.UP ||
    		     mOldDirection==Direction.UP && mNewDirection==Direction.RIGHT )
        {
        	mCornor = new Point2D( mOrigin.getX()+1, mOrigin.getY()+1 );
        }
    }

    public int getW()
    {
    	return mW;
    }
    public Direction getOldDirection()
    {
    	return mOldDirection;
    }
    public Direction getNewDirection()
    {
    	return mNewDirection;
    }
    static void setMaxSpeeds( int recessiveMaxSpeed, int dominantMaxSpeed )
    {
    	recessiveDirectionMaxSpeed=recessiveMaxSpeed;
    	dominantDirectionMaxSpeed=dominantMaxSpeed;
    }

    public Point2D getAreaStartPosition()
    {
    	return mStart;
    }
    public Point2D getAreaEndPosition()
    {
    	return mEnd;
    }
    
    public ArrayList<Point2D> getAllPositions()
    {
        ArrayList<Point2D> ret = new ArrayList<Point2D>();
        for( int k=(int)mStart.getX() ; k<=(int)mEnd.getX() ; k++ )
        {
            for( int l=(int)mStart.getY() ; l>=(int)mEnd.getY() ; l-- )
            {
                ret.add( new Point2D( k, l ) );
            }
        }
        return ret;
    }
    
    /*
    // Creates every Vertex, even those that are not relevant.
    private ArrayList<LandingPoint> createEveryVertex()
    {
    	ArrayList<Point2D> allPos=getAllPositions();
		ArrayList<LandingPoint> ret = new ArrayList<LandingPoint>();
    	for( int i=0 ; i<allPos.size() ; i++ )
    	{
    		for( int j=1 ; j<recessiveDirectionMaxSpeed+1 ; j++ )
        	{
        		for( int k=1 ; k<dominantDirectionMaxSpeed+1 ; k++ )
            	{
        			if( Direction.RIGHT==mNewDirection || Direction.LEFT==mNewDirection )
        			{
        				ret.add( new LandingPoint( allPos.get( i ), new Point2D( k, j ) ) );
        			}
        			else
        			{
        				ret.add( new LandingPoint( allPos.get( i ), new Point2D( j, k ) ) );
        			}
            	}
        	}
    	}
    	return ret;
    }
    */
    
    public static void setLandingRegionSpeedMatrix( int w )
    {
        //##########
        //#.........
        //#.........
        //#......-->
        //#oooooooo.
        //#ooooOooo. <= The matrix will be built like this and has to be turn around the origin.
        //#.....####
        //#.....####
        //#.....####
        //#.....####
		int lw=(int) Math.floor( Math.sqrt( w*2+1.75 )-0.5 );
		int a=(int) Math.floor( Math.sqrt( w*2-1.75 )-0.5 )+1;
    	
		// int dominatingDirectionMaxSpeed=a;
		// int recessiveDirectionMaxSpeed=lw;
		
        ArrayList<Point2D> matrixPositions = new ArrayList<Point2D>();
        for( int k=0 ; k<w+a ; k++ )
        {
            for( int l=0 ; l<lw ; l++ )
            {
            	matrixPositions.add( new Point2D( k, l ) );
            }
        }

		ArrayList<LandingPoint> tmpLandingRegionSpeedMatrix = new ArrayList<LandingPoint>();
    	for( int i=0 ; i<matrixPositions.size() ; i++ )
    	{
    		for( int j=1 ; j<=a ; j++ )//recessiveDirectionMaxSpeed=lw ; We won't drive in with speed 0 ... 
        	{
        		for( int k=0 ; k<=a ; k++ )//dominatingDirectionMaxSpeed==a
            	{
        			tmpLandingRegionSpeedMatrix.add( new LandingPoint( matrixPositions.get( i ), new Point2D( k, j ) ) );
            	}
        	}
    	}
    	// All speeds for every position in the landing region are set in tmpLandingRegionSpeedMatrix now.
    	
    	if( mVerbose )
    	{
			System.out.println( "All:" );
	    	for( int i=0 ; i<tmpLandingRegionSpeedMatrix.size() ; i++ )
	    	{
	    		System.out.println( tmpLandingRegionSpeedMatrix.get( i ) );
	    	}
    	}
    	
    	// Right Additional Landing Region Dominating Direction Region Reducing
        for( int i=w ; i<a+w ; i++ )
        {
            for( int j=0 ; j<lw ; j++ )
            {
            	Point2D p=new Point2D( i, j );

            	for( int k=0 ; k<tmpLandingRegionSpeedMatrix.size() ; k++ )
            	{//Search all 'wrong' vertex and remove them
            		if( tmpLandingRegionSpeedMatrix.get( k ).isAtPosition( p ) )
            		{
            			if( tmpLandingRegionSpeedMatrix.get( k ).getSpeed().getX()<( i-w+1 ) )
            			{
            				// This vertex implies we are driving into the additional landing region with a too small speed.
            				// It is not possible and we will delete the vertex.
            				tmpLandingRegionSpeedMatrix.remove( k );
            				k--;
            			}
            		}
            	}
            }
        }
    	if( mVerbose )
    	{
			System.out.println( "Right Additional Landing Region Dominating Direction Region Reducing:" );    	
	    	for( int i=0 ; i<tmpLandingRegionSpeedMatrix.size() ; i++ )
	    	{
	    		System.out.println( tmpLandingRegionSpeedMatrix.get( i ) );
	    	}
    	}
    	// Landing Region Rezessive Direction Reducing
        for( int l=0 ; l<lw ; l++ )
        {
        	int maxSpeedForRow=(int)Math.floor( Math.sqrt( 2*( w-l )-1.75 )-0.5 )+1;
	        for( int j=0 ; j<w+a ; j++ )
	        {
            	Point2D p=new Point2D( j, l ); 
        		
            	for( int k=0 ; k<tmpLandingRegionSpeedMatrix.size() ; k++ )
            	{//Search all 'wrong' vertex and remove them
            		if( tmpLandingRegionSpeedMatrix.get( k ).isAtPosition( p ) )
            		{
            			if( tmpLandingRegionSpeedMatrix.get( k ).getSpeed().getY()>maxSpeedForRow )
            			{
            				// This vertex implies we are driving into the additional landing region with a too small speed.
            				// It is not possible and we will delete the vertex.
            				tmpLandingRegionSpeedMatrix.remove( k );
            				k--;
            			}
            		}
            	}
            }
        }
    	if( mVerbose )
    	{
			System.out.println( " Landing Region Rezessive Direction Reducing: " );   	
	    	for( int i=0 ; i<tmpLandingRegionSpeedMatrix.size() ; i++ )
	    	{
	    		System.out.println( tmpLandingRegionSpeedMatrix.get( i ) );
	    	}
    	}
    	
    	// Additional Landing Region Edge cutting Reducing
        Point2D origin = new Point2D( w-1, 0 );
        Point2D corner = new Point2D( origin.getX()+1, -1 );
        for( int j=w+1 ; j<w+a ; j++ )
        {
            for( int l=0 ; l<lw ; l++ )
            {
            	Point2D p=new Point2D( j, l );
        		double m=(p.getY()-corner.getY())/(p.getX()-corner.getX());
        		//double n=p.getY()-m*p.getX();
            	for( int k=0 ; k<tmpLandingRegionSpeedMatrix.size() ; k++ )
            	{//Search all 'wrong' vertex and remove them
            		if( tmpLandingRegionSpeedMatrix.get( k ).isAtPosition( p ) )
            		{
            			double incomingSpeed=tmpLandingRegionSpeedMatrix.get( k ).getSpeed().getY()/tmpLandingRegionSpeedMatrix.get( k ).getSpeed().getX();
            			if( m<=incomingSpeed )
            			{
            				// This vertex implies we are driving into the additional landing region with a too small speed.
            				// It is not possible and we will delete the vertex.
            				tmpLandingRegionSpeedMatrix.remove( k );
            				k--;
            			}
            		}
            	}
            }
        }    
    	if( mVerbose )
    	{
			System.out.println( " Additional Landing Region Edge cutting Reducing: " );
	    	for( int i=0 ; i<tmpLandingRegionSpeedMatrix.size() ; i++ )
	    	{
	    		System.out.println( tmpLandingRegionSpeedMatrix.get( i ) );
	    	}
    	}

		
    	
    	// Normal Landing Region Reducing Not Achievable Speeds
        for( int j=0 ; j<w+a ; j++ )
        {
        	int rowMaxSpeed=(int)Math.floor( Math.sqrt( ( j+1 )*2-1.75 )-0.5 );
            for( int l=0 ; l<lw ; l++ )
            {
            	Point2D p=new Point2D( j, l );
            	for( int k=0 ; k<tmpLandingRegionSpeedMatrix.size() ; k++ )
            	{//Search all 'wrong' vertex and remove them
            		if( tmpLandingRegionSpeedMatrix.get( k ).isAtPosition( p ) )
            		{
            			if( tmpLandingRegionSpeedMatrix.get( k ).getSpeed().getX()>rowMaxSpeed )
            			{
            				// This vertex implies we are driving into the additional landing region with a too small speed.
            				// It is not possible and we will delete the vertex.
            				tmpLandingRegionSpeedMatrix.remove( k );
            				k--;
            			}
            		}
            	}
            }
        }    
    	if( mVerbose )
    	{
			System.out.println( " Additional Landing Region Edge cutting Reducing: " );
	    	for( int i=0 ; i<tmpLandingRegionSpeedMatrix.size() ; i++ )
	    	{
	    		System.out.println( tmpLandingRegionSpeedMatrix.get( i ) );
	    	}
    	}

    	// For every point in the landing region, does this point with it's incoming speed
        // go into the landing region again if going backwards? If yes, remove it.
    	
    	for( int i=0 ; i<tmpLandingRegionSpeedMatrix.size() ; i++ )
    	{
    		Point2D successor=new Point2D( 
    								tmpLandingRegionSpeedMatrix.get( i ).getPosition().getX()-tmpLandingRegionSpeedMatrix.get( i ).getSpeed().getX(),
									tmpLandingRegionSpeedMatrix.get( i ).getPosition().getY()-tmpLandingRegionSpeedMatrix.get( i ).getSpeed().getY() );
        	for( int j=0 ; j<tmpLandingRegionSpeedMatrix.size() ; j++ )
        	{
        		if( tmpLandingRegionSpeedMatrix.get( j ).isAtPosition( successor ) )
        		{
    				tmpLandingRegionSpeedMatrix.remove( i );
    				i--;
    				break;
        		}
        	}
    	}


    	landingRegionSpeedMatrix=tmpLandingRegionSpeedMatrix;
    	if( mVerbose )
    	{
			System.out.println( " incoming speed in landing region: " );
	    	for( int i=0 ; i<landingRegionSpeedMatrix.size() ; i++ )
	    	{
	    		System.out.println( landingRegionSpeedMatrix.get( i ) );
	    	}
    	}
    }

    private ArrayList<LandingPoint> flipLandingRegionOnXAxis( ArrayList<LandingPoint> lrsm ) // Landing Region Speed Matrix
    {
    	ArrayList<LandingPoint> ret=new ArrayList<LandingPoint>();
    	int maxHorizontal=0;
    	int maxVertical=0;
    	for( int i=0 ; i<lrsm.size() ; i++ )
    	{
    		if( maxHorizontal<lrsm.get( i ).getPosition().getX() )
    		{
    			maxHorizontal=(int)lrsm.get( i ).getPosition().getX();
    		}
    		if( maxVertical<lrsm.get( i ).getPosition().getY() )
    		{
    			maxVertical=(int)lrsm.get( i ).getPosition().getY();
    		}
    	}
    	
    	for( int ix=0 ; ix<maxHorizontal+1 ; ix++ )
        {
            for( int iy=0 ; iy<maxVertical+1 ; iy++ )
            {
            	Point2D p=new Point2D( ix, iy ); 
        		for( int k=0 ; k<lrsm.size() ; k++ )
            	{
            		if( lrsm.get( k ).isAtPosition( p ) )
            		{
	            		double x=lrsm.get( k ).getPosition().getX();
	            		double y=lrsm.get( k ).getPosition().getY();
	            		double sx=lrsm.get( k ).getSpeed().getX();
	            		double sy=lrsm.get( k ).getSpeed().getY();
	        			ret.add( new LandingPoint( new Point2D( x, ( maxVertical+1 )-( y+1 ) ), new Point2D( sx, -sy ) ) );
            		}
            	}
            }
        }
    	return ret;
    }
    
    private ArrayList<LandingPoint> rotateLandingRegion90Clockwise( ArrayList<LandingPoint> lrsm ) // Landing Region Speed Matrix
    {
    	ArrayList<LandingPoint> ret=new ArrayList<LandingPoint>();
    	int maxHorizontal=0;
    	int maxVertical=0;
    	for( int i=0 ; i<lrsm.size() ; i++ )
    	{
    		if( maxHorizontal<lrsm.get( i ).getPosition().getX() )
    		{
    			maxHorizontal=(int)lrsm.get( i ).getPosition().getX();
    		}
    		if( maxVertical<lrsm.get( i ).getPosition().getY() )
    		{
    			maxVertical=(int)lrsm.get( i ).getPosition().getY();
    		}
    	}
    	for( int ix=0 ; ix<maxHorizontal+1 ; ix++ )
        {
            for( int iy=0 ; iy<maxVertical+1 ; iy++ )
            {
            	Point2D p=new Point2D( ix, iy ); 
        		for( int k=0 ; k<lrsm.size() ; k++ )
            	{
            		if( lrsm.get( k ).isAtPosition( p ) )
            		{
	            		double x=lrsm.get( k ).getPosition().getX();
	            		double y=lrsm.get( k ).getPosition().getY();
	            		
	            		// t=transposed
	            		Point2D t=new Point2D( y, x );
	            		
	            		double sx=lrsm.get( k ).getSpeed().getX();
	            		double sy=lrsm.get( k ).getSpeed().getY();
	        			ret.add( new LandingPoint( new Point2D( t.getY(), maxVertical-t.getX() ), new Point2D( sy, -sx ) ) );
            		}
            	}
            }
        }
    	return ret;
    }
    
    public ArrayList<LandingPoint> getLandingPoints()
    {
    	ArrayList<LandingPoint> ret;
    	if( null==landingRegionSpeedMatrix )
    	{
    		LandingRegion.setLandingRegionSpeedMatrix( mW );
    	}
  
        switch( mOldDirection )
        {
        case UP:
            if( Direction.RIGHT==mNewDirection )
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
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<landingRegionSpeedMatrix.size() ; i++ )
            	{
            		double x=landingRegionSpeedMatrix.get( i ).getPosition().getX();
            		double y=landingRegionSpeedMatrix.get( i ).getPosition().getY();
            		double sx=landingRegionSpeedMatrix.get( i ).getSpeed().getX();
            		double sy=landingRegionSpeedMatrix.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            else
            { // LEFT==mNewDirection
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
            	ArrayList<LandingPoint> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=flipLandingRegionOnXAxis( rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) ) );
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).getPosition().getX();
            		double y=pLRSM.get( i ).getPosition().getY();
            		double sx=pLRSM.get( i ).getSpeed().getX();
            		double sy=pLRSM.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            break;
        case RIGHT:
            if( Direction.UP==mNewDirection )
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
            	ArrayList<LandingPoint> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=flipLandingRegionOnXAxis( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) );
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).getPosition().getX();
            		double y=pLRSM.get( i ).getPosition().getY();
            		double sx=pLRSM.get( i ).getSpeed().getX();
            		double sy=pLRSM.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            else
            { // DOWN==mNewDirection
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
            	ArrayList<LandingPoint> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=rotateLandingRegion90Clockwise( landingRegionSpeedMatrix );
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).getPosition().getX();
            		double y=pLRSM.get( i ).getPosition().getY();
            		double sx=pLRSM.get( i ).getSpeed().getX();
            		double sy=pLRSM.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            break;
        case DOWN:
            if( Direction.RIGHT==mNewDirection )
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
            	ArrayList<LandingPoint> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=flipLandingRegionOnXAxis( landingRegionSpeedMatrix );
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).getPosition().getX();
            		double y=pLRSM.get( i ).getPosition().getY();
            		double sx=pLRSM.get( i ).getSpeed().getX();
            		double sy=pLRSM.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            else
            { // LEFT==mNewDirection
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
            	ArrayList<LandingPoint> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) );
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).getPosition().getX();
            		double y=pLRSM.get( i ).getPosition().getY();
            		double sx=pLRSM.get( i ).getSpeed().getX();
            		double sy=pLRSM.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            break;
        case LEFT:
            if( Direction.UP==mNewDirection )
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
            	ArrayList<LandingPoint> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) ) );
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).getPosition().getX();
            		double y=pLRSM.get( i ).getPosition().getY();
            		double sx=pLRSM.get( i ).getSpeed().getX();
            		double sy=pLRSM.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            else
            { // DOWN==mNewDirection
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
            	ArrayList<LandingPoint> pLRSM;// Prepared Landing Region Speed Matrix

        		System.out.println( "Pure: " );
            	for( int i=0; i<landingRegionSpeedMatrix.size() ; i++ )
            	{
            		System.out.println( landingRegionSpeedMatrix.get( i ) );
            	}
            	ArrayList<LandingPoint> flipped=flipLandingRegionOnXAxis( landingRegionSpeedMatrix );
        		System.out.println( "Flipped On X: " );
            	for( int i=0; i<flipped.size() ; i++ )
            	{
            		System.out.println( flipped.get( i ) );
            	}
            	ArrayList<LandingPoint> flippedAndRot=rotateLandingRegion90Clockwise( flipLandingRegionOnXAxis( landingRegionSpeedMatrix ) );
        		System.out.println( "Flipped On X and rotated: " );
            	for( int i=0; i<flippedAndRot.size() ; i++ )
            	{
            		System.out.println( flippedAndRot.get( i ) );
            	}
            	
            	
            	pLRSM=rotateLandingRegion90Clockwise( flipLandingRegionOnXAxis( landingRegionSpeedMatrix ) );
            	ret=new ArrayList<LandingPoint>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).getPosition().getX();
            		double y=pLRSM.get( i ).getPosition().getY();
            		double sx=pLRSM.get( i ).getSpeed().getX();
            		double sy=pLRSM.get( i ).getSpeed().getY();
            		ret.add( new LandingPoint( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            break;
            default:
            {
            	ret=new ArrayList<LandingPoint>();
            }
        	break;
        }
        return ret;
    }

    public LandingPoint getFastCornerLandingPoint()
    {
    	LandingPoint ret=null;
    	ArrayList<LandingPoint> allVertex=getLandingPoints();
    	double s=0;
    	for( int i=0 ; i<allVertex.size() ; i++ )
    	{
    		if( mCornor.equals( allVertex.get( i ).getPosition() ) )
    		{
    			if( s<allVertex.get( i ).getTotalSpeed() )
				{
					s=allVertex.get( i ).getTotalSpeed();
					ret=allVertex.get( i );
				}
    		}
    	}
    	return ret;
    }

    public LandingPoint getFastTopLandingPoint()
    {
    	LandingPoint ret=null;
    	boolean horizontal=false;
    	boolean positiv=false;
    	if( mNewDirection==Direction.LEFT )
    	{
    		horizontal=true;
    		positiv=false;
    	}
    	else if( mNewDirection==Direction.RIGHT )
    	{
    		horizontal=true;
    		positiv=true;
    	}
    	else if( mNewDirection==Direction.DOWN )
    	{
    		horizontal=false;
    		positiv=false;
    	}
    	else if( mNewDirection==Direction.UP )
    	{
    		horizontal=false;
    		positiv=true;
    	}
    	
    	ArrayList<LandingPoint> allVertex=getLandingPoints();
    	int frontmost=0;
    	int tmpPossiblyFrontmost=0;
		frontmost=(int)( ( horizontal )?( allVertex.get( 0 ).getPosition().getX() ):( allVertex.get( 0 ).getPosition().getY() ) );
    	ret=allVertex.get( 0 );
		
    	for( int i=1 ; i<allVertex.size() ; i++ )
    	{
    		tmpPossiblyFrontmost=(int)( ( horizontal )?( allVertex.get( i ).getPosition().getX() ):( allVertex.get( i ).getPosition().getY() ) );
    		boolean bigger=false;
    		if( positiv )
    		{
    			if( tmpPossiblyFrontmost>frontmost ) 
    				bigger=true;
    		}
    		else
    		{
    			if( tmpPossiblyFrontmost<frontmost ) 
    				bigger=true;
    		}
    		if( bigger )
    		{
    			frontmost=tmpPossiblyFrontmost;
    			ret=allVertex.get( i );
    		}
    	}
    	
    	return ret;
    }
    public ArrayList<LandingPoint> getSafeLandingPoint()
    {
    	ArrayList<LandingPoint> ret=new ArrayList<LandingPoint>();
    	boolean horizontal=false;
    	boolean positiv=false;
    	if( mNewDirection==Direction.LEFT )
    	{
    		horizontal=true;
    		positiv=false;
    	}
    	else if( mNewDirection==Direction.RIGHT )
    	{
    		horizontal=true;
    		positiv=true;
    	}
    	else if( mNewDirection==Direction.DOWN )
    	{
    		horizontal=false;
    		positiv=false;
    	}
    	else if( mNewDirection==Direction.UP )
    	{
    		horizontal=false;
    		positiv=true;
    	}
    	
    	//int landingRegionHeight=mW+mA;
    	ArrayList<LandingPoint> allVertex=getLandingPoints();
    	int highestVertex=(int)( ( horizontal )?( allVertex.get( 0 ).getPosition().getX() ):( allVertex.get( 0 ).getPosition().getY() ) );
    	int lowestVertex=(int)( ( horizontal )?( allVertex.get( 0 ).getPosition().getX() ):( allVertex.get( 0 ).getPosition().getY() ) );
    	for( int i=1 ; i<allVertex.size() ; i++ )
    	{
    		if( highestVertex<(int)( ( horizontal )?( allVertex.get( i ).getPosition().getX() ):( allVertex.get( i ).getPosition().getY() ) ) )
    		{
    			highestVertex=(int)( ( horizontal )?( allVertex.get( i ).getPosition().getX() ):( allVertex.get( i ).getPosition().getY() ) );
    		}
    	}
    	
    	for( int i=1 ; i<allVertex.size() ; i++ )
    	{
    		if( lowestVertex>(int)( ( horizontal )?( allVertex.get( i ).getPosition().getX() ):( allVertex.get( i ).getPosition().getY() ) ) )
    		{
    			lowestVertex=(int)( ( horizontal )?( allVertex.get( i ).getPosition().getX() ):( allVertex.get( i ).getPosition().getY() ) );
    		}
    	}
    	int diff=highestVertex-lowestVertex;
    	int center=0;
    	int center2=-1;
    	boolean odd=( diff )%2==0;
    	if( odd )
    	{
    		center=( diff-1 )/2;
    		center2=( diff+1 )/2;
    	}
    	else
    	{
    		center=diff/2;
    	}
    	int tmp;	
    	for( int i=0 ; i<allVertex.size() ; i++ )
    	{
    		tmp=(int)( ( horizontal )?( allVertex.get( i ).getPosition().getX() ):( allVertex.get( i ).getPosition().getY() ) );
    		if( tmp==center || tmp==center2 )
    		{
        		ret.add( allVertex.get( i ) );
    		}
    	}
    	
    	return ret;
    }
}







