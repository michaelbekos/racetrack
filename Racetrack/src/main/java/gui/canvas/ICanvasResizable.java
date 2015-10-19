package src.main.java.gui.canvas;

public interface ICanvasResizable {

	// MARK: Public variables
	/**
	 * Overrides resiabale property
	 */
	public boolean isResizable();

	// MARK: Computed variables
	/**
	 * Change prefered width dynamically
	 */
	public double prefWidth(double height);

	/**
	 * Change prefered height dynamically
	 */
	public double prefHeight(double width);

	// MARK: View updates
	/**
	 * Methode for updating the view and redraw everything.
	 */
	public void updateView();

	/**
	 * Methode to be overritten in subclasses. Drawing two red crossed lines
	 * from the upper left to the bottom right and from the upper right to the
	 * bottom left corner.
	 */
	abstract void draw();
	
	/**
	 * Resets all views variables to default values
	 */
	public void resetView();
	
	/**
	 * Clears the grapicContext.
	 */
	public void clear();
}
