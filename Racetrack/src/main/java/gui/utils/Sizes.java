package src.main.java.gui.utils;

/**
 * Simple class for defining basic sizes for the track and player
 * 
 * @author Tobias
 *
 */
public class Sizes {
	public static class Track {
		final public static double boundaryWidth = 0.25;
		final public static double finishLineWidth = 0.2;
		final public static double startingPointsDiameter = 0.35;
	}
	
	public static class Player {
		final public static double trailWidth = 0.15;
		final public static double startingPointDiameter = 0.5;
		final public static double currentEndPointDiameter = 0.5;
		final public static double crashCrossSize = 0.35;
		final public static double memorizedGridPointDiameter = 0.25;
	}
	
	public static class MovePreview {
		final public static double mousePointDiameter = 0.4;
		final public static double simplePreviewPointDiameter = 0.4;
		final public static double simpleHightlightPointDiameter = 0.7;
	}
}
