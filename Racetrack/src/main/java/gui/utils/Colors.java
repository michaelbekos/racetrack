package src.main.java.gui.utils;

import javafx.scene.paint.Color;

/**
 * All colors used within the game are split up into: 
 * - Player 
 * - Track
 * - Canvas
 * - Other
 * 
 * @author Tobias
 *
 */
public class Colors {
	
	/**
	 * All available colors related to the player object or
	 * the player representation.
	 * 
	 * @author Tobias
	 *
	 */
	public static class Player {
		final public static Color red = Color.DARKRED;
		final public static Color green = Color.GREEN;
		final public static Color blue = Color.DARKCYAN;
		final public static Color orange = Color.ORANGE;
		final public static Color purple = Color.PURPLE;
	
		final public static Color inactive = Color.DARKGRAY;
		
		private static Color[] allColors = { red, green, blue, orange, purple };
	
		/**
		 * Get a players color from an index defined as: 
		 * 0 - red 
		 * 1 - green 
		 * 2 - blue 
		 * 3 - orange 
		 * 4 - purple
		 * 
		 * @param index
		 *            The Identifier
		 * @return An appropriate color
		 */
		public static Color getColorWithId(int index) {
			return Player.allColors[index % Player.getColorsCount()];
		}
	
		/**
		 * @return The number of all colors which a player could use.
		 */
		public static int getColorsCount() {
			return Player.allColors.length;
		}
	}

	/**
	 * All available colors for drawing the track for the game and in 
	 * various other conditions - like the track preview.
	 * 
	 * @author Tobias
	 *
	 */
	public static class Track {
		final public static Color boundary = Color.DARKGRAY;
		final public static Color startingPoints = Color.DARKGOLDENROD;
		final public static Color finishLine = Color.DARKCYAN;
		
		final public static Color clearBackground = new Color(1, 1, 1, 0.8);
		final public static Color grasBackground = new Color(0, 0.5, 0, 0.2);
		
		final public static Color pointPreview = Color.DARKGRAY;
	}
	
	/**
	 * All available colors for the canvas element used for drawing the game grid.
	 * 
	 * @author Tobias
	 *
	 */
	public static class Canvas {
		public static Color gridLines = Color.LIGHTGRAY;
	}
	
	/**
	 * Any colors needed which hasen't been covered yet.
	 * 
	 * @author Tobias
	 *
	 */
	public static class Other {
		public static Color mousePosition = Color.GRAY;
	}
}