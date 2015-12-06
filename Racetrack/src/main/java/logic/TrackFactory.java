package src.main.java.logic;

import java.util.Random;

import javafx.geometry.Point2D;

/**
 * Class for all predefined maps
 * @author Tobias Kaulich
 *
 */
public class TrackFactory {

	// n-Track
	private static Track nTrack = new Track(
			new Point2D[] {
					new Point2D(5,2),
					new Point2D(2,2),
					new Point2D(2,16),
					new Point2D(28,16),
					new Point2D(28,2)
			}, 
			new Point2D[] {
					new Point2D(5,2),
					new Point2D(8,2),
					new Point2D(8,12),
					new Point2D(22,12),
					new Point2D(22,2)
			}, 
			new Point2D(30, 20), 
			0, 
			new Point2D[] {
				new Point2D(3,3),
				new Point2D(4,3),
				new Point2D(5,3),
				new Point2D(6,3),
				new Point2D(7,3)
			}
			);	

	// M-Track
	private static Track mTrack = new Track(
			new Point2D[]{
					new Point2D(6,2),
					new Point2D(2,2),
					new Point2D(2,8),
					new Point2D(9,13),
					new Point2D(9,18),
					new Point2D(2,22),
					new Point2D(2,29),
					new Point2D(22,29),
					new Point2D(31,25),
					new Point2D(39,29),
					new Point2D(57,29),
					new Point2D(57,23),
					new Point2D(48,16),
					new Point2D(48,11),
					new Point2D(57,5),
					new Point2D(57,2)
			}, 
			new Point2D[]{
					new Point2D(6,2),
					new Point2D(10,2),
					new Point2D(10,5),
					new Point2D(17,13),
					new Point2D(17,18),
					new Point2D(14,22),
					new Point2D(10,23),
					new Point2D(22,23),
					new Point2D(31,18),
					new Point2D(39,23),
					new Point2D(49,23),
					new Point2D(40,16),
					new Point2D(40,11),
					new Point2D(48,5),
					new Point2D(48,2)
			}, 
			new Point2D(59, 31), 
			1, 
			new Point2D[] {
				new Point2D(3,3),
				new Point2D(4,3),
				new Point2D(5,3),
				new Point2D(6,3),
				new Point2D(7,3),
				new Point2D(8,3),
				new Point2D(9,3)
			}
			);

	// Random (d) Track
	private static Track dTrack = new Track(
			new Point2D[]{
					new Point2D(5,2),
					new Point2D(2,2),
					new Point2D(2,10),
					new Point2D(14,10),
					new Point2D(14,12),
					new Point2D(6,18),
					new Point2D(6,27),
					new Point2D(29,27),
					new Point2D(29,18),
					new Point2D(35,16),
					new Point2D(35,6),
					new Point2D(29,4),
					new Point2D(29,2)
			}, 
			new Point2D[]{
					new Point2D(5,2),
					new Point2D(8,2),
					new Point2D(8,4),
					new Point2D(14,4),
					new Point2D(20,8),
					new Point2D(20,14),
					new Point2D(13,19),
					new Point2D(13,21),
					new Point2D(23,21),
					new Point2D(23,15),
					new Point2D(29,13),
					new Point2D(29,9),
					new Point2D(23,7),
					new Point2D(23,2)
			}, 
			new Point2D(37, 29), 
			2, 
			new Point2D[] {
				new Point2D(3,3),
				new Point2D(4,3),
				new Point2D(5,3),
				new Point2D(6,3),
				new Point2D(7,3)
			}
			);

	// Oval (O-) Track
	private static Track oTrack = new Track(
			new Point2D[]{
					new Point2D(20,2),
					new Point2D(10,2),
					new Point2D(2,10),
					new Point2D(2,22),
					new Point2D(10,30),
					new Point2D(30,30),
					new Point2D(38,22),
					new Point2D(38,10),
					new Point2D(30,2),
					new Point2D(20,2)
			}, 
			new Point2D[]{
					new Point2D(20,8),
					new Point2D(13,8),
					new Point2D(8,13),
					new Point2D(8,19),
					new Point2D(13,24),
					new Point2D(27,24),
					new Point2D(32,19),
					new Point2D(32,13),
					new Point2D(27,8),
					new Point2D(20,8)
			}, 
			new Point2D(40, 32), 
			3, 
			new Point2D[] {
				new Point2D(20,3),
				new Point2D(20,4),
				new Point2D(20,5),
				new Point2D(20,6),
				new Point2D(20,7)
			},
			new Line2D(new Point2D(20, 24), new Point2D(20, 30))
			);
	// P-Track
	private static Track pTrack = new Track(
			new Point2D[]{
					new Point2D(6,2),
					new Point2D(2,2),
					new Point2D(2,8),
					new Point2D(9,13),
					new Point2D(11,18),
					new Point2D(4,23),
					new Point2D(9,29),
					new Point2D(22,29),
					new Point2D(31,24),
					new Point2D(39,29),
					new Point2D(50,25),
					new Point2D(49,20),
					new Point2D(37,16),
					new Point2D(23,12),
					new Point2D(24,7),
					new Point2D(32,14),
					new Point2D(40,16),
					new Point2D(48,16),
					new Point2D(45,7),
					new Point2D(39,7),
					new Point2D(37,5),
					new Point2D(39,7),
					new Point2D(45,7)
			}, 
			new Point2D[]{
					new Point2D(6,2),
					new Point2D(10,2),
					new Point2D(10,5),
					new Point2D(14,13),
					new Point2D(15,18),
					new Point2D(14,22),
					new Point2D(10,23),
					new Point2D(22,23),
					new Point2D(31,18),
					new Point2D(39,23),
					new Point2D(46,23),
					new Point2D(39,23),
					new Point2D(31,18),
					new Point2D(18,14),
					new Point2D(17,9),
					new Point2D(20,3),
					new Point2D(29,2),
					new Point2D(31,6),
					new Point2D(40,11),
					new Point2D(42,11),
					new Point2D(40,11),
					new Point2D(31,6),
					new Point2D(34,2),
					new Point2D(45,2)
			}, 
			new Point2D(52, 30), 
			4, 
			new Point2D[] {
				new Point2D(3,3),
				new Point2D(4,3),
				new Point2D(5,3),
				new Point2D(6,3),
				new Point2D(7,3),
				new Point2D(8,3),
				new Point2D(9,3)
			}
			);


	// Indianapolis Indy(l,h,w) - see Trello for usage of l,h,w
		private static int l = 150;
		private static int h = 10;
		private static int w = 5;
		private static Point2D[] getIndianapolisStartingPositions()
		{
			Point2D[] startingPositions = new Point2D[w];
			for (int i = 0; i < w; i++)
			{
				startingPositions[i] = new Point2D(5+w+l/2+1,6+i);
			}
			return startingPositions;
		}
		private static Track indianapolis = new Track(
				
				new Point2D[] {	
						new Point2D(5+w+l/2+1,5),
						new Point2D(5,5),
						new Point2D(5,6+2*w+h),
						new Point2D(6+l+2*w,6+2*w+h),
						new Point2D(6+l+2*w,5),
						new Point2D(5+w+l/2+1,5)
				}, 
				new Point2D[] {
						new Point2D(5+w+l/2+1,6+w),
						new Point2D(6+w,6+w),
						new Point2D(6+w,5+w+h),
						new Point2D(5+w+l,5+w+h),
						new Point2D(5+w+l,6+w),
						new Point2D(5+w+l/2,6+w),
						new Point2D(5+w+l/2,5),
						new Point2D(5+w+l/2,6+w),
						new Point2D(5+w+l/2+1,6+w)
				}, 
				new Point2D(10 + 2*w + l, 10 + 2*w + h), 
				5, 
				getIndianapolisStartingPositions(),
				new Line2D(new Point2D(5+w+l/2-1,5),new Point2D(5+w+l/2-1,6+w))
		);	
		
	private static Track[] allTracks = new Track[] {
		nTrack,
		mTrack,
		dTrack,
		oTrack,
		pTrack,
		indianapolis
	};

	/*
	private static Track mTrack = new Track(
			outerPoints, 
			innerPoints, 
			dimensions, 
			-1, 
			startingPoints
			);
	 */

	public static Track getSampleTrack(int index) {
		int correctedIndex = index % allTracks.length;

		return allTracks[correctedIndex];
	}

	public static int getSampleTrackCount() {
		return allTracks.length;
	}

	public static Point2D getRandomStartingPoint(int trackID){
		
		Point2D[] startingPoints = allTracks[trackID].getStartingPoints();

		int length = startingPoints.length;

		Random generator = new Random(); 
		
		int randomStartingPoint = generator.nextInt(length);

		return startingPoints[randomStartingPoint];
	}

}
