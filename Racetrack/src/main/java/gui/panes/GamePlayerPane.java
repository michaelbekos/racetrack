package src.main.java.gui.panes;

import src.main.java.gui.utils.StrokePercentageFill;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The players pane during a game (shown in the bottom left).
 * 
 * @author Tobias
 *
 */
public class GamePlayerPane extends StackPane implements IGamePlayerPane {

	// MARK: Private variables
	/**
	 * The player circle indicates its current status.
	 */
	private Circle playerCircle;
	/**
	 * The player name.
	 */
	private Text playerName;

	// MARK: Initialization
	/**
	 * Initialize a light gray stroked circle with '---' three dashes as a
	 * playceholfer for the player name.
	 */
	public GamePlayerPane() {
		this.setWidth(70);
		this.setHeight(150);

		this.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		this.setMaxSize(150, 75);

		playerCircle = new Circle();
		playerCircle.setRadius(22);
		setBorderStrokeWidthFillPercent(StrokePercentageFill.small);
		playerCircle.setStroke(Color.LIGHTGRAY);
		playerCircle.setFill(null);
		playerCircle.setStrokeType(StrokeType.INSIDE);
		GamePlayerPane.setMargin(playerCircle, new Insets(-12, 5, 5, 5));
		this.getChildren().add(playerCircle);

		playerName = new Text();
		playerName.setFont(new Font("Comic Sans MS", 13));
		playerName.setText("--");
		GamePlayerPane.setMargin(playerName, new Insets(55, 0, 0, 0));
		this.getChildren().add(playerName);
	}

	// MARK: Getter and setter
	public void setBorderStrokeWidthFillPercent(double percentage) {
		playerCircle.setStrokeWidth(playerCircle.getRadius() * percentage);
	}
	
	public void setFillBorderStrokeWidth() {
		playerCircle.setStrokeWidth(playerCircle.getRadius());
	}

	public void setColor(Color color) {
		playerCircle.setStroke(color);
	}
	
	public void setName(String name) {
		playerName.setText(name);
//		if (name.length() < 8) {
//			playerName.setFont(new Font("Comic Sans MS", 13));
//		} else if (name.length() < 12) {
//			playerName.setFont(new Font("Comic Sans MS", 10));
//		} else {
//			playerName.setFont(new Font("Comic Sans MS", 9));
//		}
	}
}
