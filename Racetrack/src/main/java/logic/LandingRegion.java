package src.main.java.logic;
import java.util.ArrayList;

import javafx.geometry.Point2D;
import src.main.java.logic.utils.AIUtils;
import src.main.java.logic.utils.AIUtils.Direction;

public class LandingRegion
{
    private Point2D mOrigin; // Point diagonal to cornor
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

    static private ArrayList<ZigZagVertex> landingRegionSpeedMatrix;
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
    }
    
    static void setMaxSpeeds( int recessiveMaxSpeed, int dominantMaxSpeed )
    {
    	recessiveDirectionMaxSpeed=recessiveMaxSpeed;
    	dominantDirectionMaxSpeed=dominantMaxSpeed;
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
    private ArrayList<ZigZagVertex> createEveryVertex()
    {
    	ArrayList<Point2D> allPos=getAllPositions();
		ArrayList<ZigZagVertex> ret = new ArrayList<ZigZagVertex>();
    	for( int i=0 ; i<allPos.size() ; i++ )
    	{
    		for( int j=1 ; j<recessiveDirectionMaxSpeed+1 ; j++ )
        	{
        		for( int k=1 ; k<dominantDirectionMaxSpeed+1 ; k++ )
            	{
        			if( Direction.RIGHT==mNewDirection || Direction.LEFT==mNewDirection )
        			{
        				ret.add( new ZigZagVertex( allPos.get( i ), new Point2D( k, j ) ) );
        			}
        			else
        			{
        				ret.add( new ZigZagVertex( allPos.get( i ), new Point2D( j, k ) ) );
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

		ArrayList<ZigZagVertex> tmpLandingRegionSpeedMatrix = new ArrayList<ZigZagVertex>();
    	for( int i=0 ; i<matrixPositions.size() ; i++ )
    	{
    		for( int j=1 ; j<=a ; j++ )//recessiveDirectionMaxSpeed=lw ; We won't drive in with speed 0 ... 
        	{
        		for( int k=0 ; k<=a ; k++ )//dominatingDirectionMaxSpeed==a
            	{
        			tmpLandingRegionSpeedMatrix.add( new ZigZagVertex( matrixPositions.get( i ), new Point2D( k, j ) ) );
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
            			if( tmpLandingRegionSpeedMatrix.get( k ).Speed().getX()<( i-w+1 ) )
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
            			if( tmpLandingRegionSpeedMatrix.get( k ).Speed().getY()>maxSpeedForRow )
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
            			double incomingSpeed=tmpLandingRegionSpeedMatrix.get( k ).Speed().getY()/tmpLandingRegionSpeedMatrix.get( k ).Speed().getX();
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

    	// For every point in the landing region, does this point with it's incoming speed
        // go into the landing region again if going backwards? If yes, remove it.
    	
    	for( int i=0 ; i<tmpLandingRegionSpeedMatrix.size() ; i++ )
    	{
    		Point2D successor=new Point2D( 
    								tmpLandingRegionSpeedMatrix.get( i ).Position().getX()-tmpLandingRegionSpeedMatrix.get( i ).Speed().getX(),
									tmpLandingRegionSpeedMatrix.get( i ).Position().getY()-tmpLandingRegionSpeedMatrix.get( i ).Speed().getY() );
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

    private ArrayList<ZigZagVertex> flipLandingRegionOnXAxis( ArrayList<ZigZagVertex> lrsm ) // Landing Region Speed Matrix
    {
    	ArrayList<ZigZagVertex> ret=new ArrayList<ZigZagVertex>();
    	for( int ix=0 ; ix<mW+mA ; ix++ )
        {
            for( int iy=0 ; iy<mLW ; iy++ )
            {
            	Point2D p=new Point2D( ix, iy ); 
        		for( int k=0 ; k<lrsm.size() ; k++ )
            	{
            		if( lrsm.get( k ).isAtPosition( p ) )
            		{
	            		double x=lrsm.get( k ).Position().getX();
	            		double y=lrsm.get( k ).Position().getY();
	            		double sx=lrsm.get( k ).Speed().getX();
	            		double sy=lrsm.get( k ).Speed().getY();
	        			ret.add( new ZigZagVertex( new Point2D( x, mLW-( y+1 ) ), new Point2D( sx, -sy ) ) );
            		}
            	}
            }
        }
    	return ret;
    }
    private ArrayList<ZigZagVertex> rotateLandingRegion90Clockwise( ArrayList<ZigZagVertex> lrsm ) // Landing Region Speed Matrix
    {
    	ArrayList<ZigZagVertex> ret=new ArrayList<ZigZagVertex>();
    	for( int ix=0 ; ix<mW+mA ; ix++ )
        {
            for( int iy=0 ; iy<mLW ; iy++ )
            {
            	Point2D p=new Point2D( ix, iy ); 
        		for( int k=0 ; k<lrsm.size() ; k++ )
            	{
            		if( lrsm.get( k ).isAtPosition( p ) )
            		{
	            		double x=lrsm.get( k ).Position().getX();
	            		double y=lrsm.get( k ).Position().getY();
	            		double sx=lrsm.get( k ).Speed().getX();
	            		double sy=lrsm.get( k ).Speed().getY();
	        			ret.add( new ZigZagVertex( new Point2D( y, mLW-( x+1 ) ), new Point2D( -sy, sx ) ) );
            		}
            	}
            }
        }
    	return ret;
    }
    
    public ArrayList<ZigZagVertex> getZigZagVertice()
    {
    	ArrayList<ZigZagVertex> ret;
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
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<landingRegionSpeedMatrix.size() ; i++ )
            	{
            		double x=landingRegionSpeedMatrix.get( i ).Position().getX();
            		double y=landingRegionSpeedMatrix.get( i ).Position().getY();
            		double sx=landingRegionSpeedMatrix.get( i ).Speed().getX();
            		double sy=landingRegionSpeedMatrix.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
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
            	ArrayList<ZigZagVertex> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=flipLandingRegionOnXAxis( rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) ) );
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).Position().getX();
            		double y=pLRSM.get( i ).Position().getY();
            		double sx=pLRSM.get( i ).Speed().getX();
            		double sy=pLRSM.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
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
            	ArrayList<ZigZagVertex> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=flipLandingRegionOnXAxis( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) );
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).Position().getX();
            		double y=pLRSM.get( i ).Position().getY();
            		double sx=pLRSM.get( i ).Speed().getX();
            		double sy=pLRSM.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
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
            	ArrayList<ZigZagVertex> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=rotateLandingRegion90Clockwise( landingRegionSpeedMatrix );
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).Position().getX();
            		double y=pLRSM.get( i ).Position().getY();
            		double sx=pLRSM.get( i ).Speed().getX();
            		double sy=pLRSM.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
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
            	ArrayList<ZigZagVertex> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=flipLandingRegionOnXAxis( landingRegionSpeedMatrix );
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).Position().getX();
            		double y=pLRSM.get( i ).Position().getY();
            		double sx=pLRSM.get( i ).Speed().getX();
            		double sy=pLRSM.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
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
            	ArrayList<ZigZagVertex> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) );
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).Position().getX();
            		double y=pLRSM.get( i ).Position().getY();
            		double sx=pLRSM.get( i ).Speed().getX();
            		double sy=pLRSM.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
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
            	ArrayList<ZigZagVertex> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( rotateLandingRegion90Clockwise( landingRegionSpeedMatrix ) ) );
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).Position().getX();
            		double y=pLRSM.get( i ).Position().getY();
            		double sx=pLRSM.get( i ).Speed().getX();
            		double sy=pLRSM.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
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
            	ArrayList<ZigZagVertex> pLRSM;// Prepared Landing Region Speed Matrix
            	pLRSM=rotateLandingRegion90Clockwise( flipLandingRegionOnXAxis( landingRegionSpeedMatrix ) );
            	ret=new ArrayList<ZigZagVertex>();
            	for( int i=0 ; i<pLRSM.size() ; i++ )
            	{
            		double x=pLRSM.get( i ).Position().getX();
            		double y=pLRSM.get( i ).Position().getY();
            		double sx=pLRSM.get( i ).Speed().getX();
            		double sy=pLRSM.get( i ).Speed().getY();
            		ret.add( new ZigZagVertex( new Point2D( x+mStart.getX(), y+mEnd.getY() ), new Point2D( sx, sy ) ) );
            	}
            }
            break;
            default:
            {
            	ret=new ArrayList<ZigZagVertex>();
            }
        	break;
        }
        return ret;
    }
}

