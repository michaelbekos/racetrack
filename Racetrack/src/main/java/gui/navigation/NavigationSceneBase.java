package src.main.java.gui.navigation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import src.main.java.gui.canvas.CanvasNavigationBackground;

/**
 * Extends RTMultiSceneBase to represent only navigation scenes. Those scenes
 * have a background pane where a uniform background ( a grid with predefinded
 * tile size ) is drawn on top.
 * 
 * @author Tobias
 */
public class NavigationSceneBase extends MultiSceneBase {
	
	// MARK: Public variables
	/***
	 * The backgound tile size (= grid dimensions)
	 */
	public Point2D backgroundSceneDimension = new Point2D(60, 40);

	// MARK: Protected variables
	/***
	 * Base scene pane should have name 'gridBaseScenePane' which will be set
	 * within the FXML File
	 */
	@FXML
	protected Pane gridBaseScenePane;

	// MARK: Private variables
	/**
	 * The label showing the help questionmark.
	 */
	@FXML
	private Label helpLabel;

	/**
	 * Reprecents the background canvas for drawing a uniform background for
	 * each navigation scene.
	 */
	private CanvasNavigationBackground backgroundCanvas;

	
	// MARK: Initalization
	/**
	 * Initializes the controller class.
	 */
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		// Add new gameCanvas
		backgroundCanvas = new CanvasNavigationBackground(backgroundSceneDimension);
		gridBaseScenePane.getChildren().add(backgroundCanvas);

		// Bind width and height of the backgroundCanvas to the main pane in
		// this scene
		backgroundCanvas.widthProperty().bind(gridBaseScenePane.widthProperty());
		backgroundCanvas.heightProperty().bind(gridBaseScenePane.heightProperty());
		
		// Add a tooltip to the help label
		final Tooltip helpTooltip = new Tooltip();
		helpTooltip.setText("Open help");
		helpLabel.setTooltip(helpTooltip);
	}
	
	
}
