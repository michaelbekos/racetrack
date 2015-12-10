package src.main.java.logic;

public interface IAI {
	
	/*
	 * The result is a position where the AI wants to go, not the velocity or direction!!
	 */
	public javafx.geometry.Point2D move();
}
