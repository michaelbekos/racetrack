package src.main.java.gui.canvas;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Costume canvas, to make it resizable and redraw if the canvas has been
 * resized.
 * 
 * @author Tobias Kaulich
 */
public class CanvasResizable extends Canvas implements ICanvasResizable  {

	// MARK: Public variables
	public boolean isResizable() {
		return true;
	}

	// MARK: Computed variables
	public double prefWidth(double height) {
		return getWidth();
	}

	public double prefHeight(double width) {
		return getHeight();
	}
	
	// MARK: Initialization
	/**
	 * Initiliaze resizable canvas. Add listener for size changes and redraw on
	 * change.
	 */
	public CanvasResizable() {
		// Redraw canvas when size changes.
		this.widthProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable o) {
				draw();
			}
		});
		this.heightProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable o) {
				draw();
			}
		});
	}

	
	// MARK: Update view
	public void updateView() {
		draw();
	}
	
	public void draw() {
		double width = getWidth();
		double height = getHeight();

		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);

		gc.setStroke(Color.RED);
		gc.strokeLine(0, 0, width, height);
		gc.strokeLine(0, height, width, 0);
	}
	
	public void resetView() {
		this.clear();
	}
	
	public void clear() {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
	}
}
