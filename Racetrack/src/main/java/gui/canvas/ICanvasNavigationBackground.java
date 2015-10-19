package src.main.java.gui.canvas;

public interface ICanvasNavigationBackground extends ICanvasResizable {
	/**
	 * Calculating the minimum canvas side length.
	 * 
	 * @return Minimum canvas side length
	 */
	public double getMinCanvasDimension();
	
	/**
	 * Calculating the current grid squares side length
	 * 
	 * @return Grid square size
	 */
	public int calcGridSquareSize();
	
}
