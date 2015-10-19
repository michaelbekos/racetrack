package src.main.java.gui.panes;

import src.main.java.gui.scenes.LobbyController;
import src.main.java.gui.utils.StrokePercentageFill;
import src.main.java.logic.ModelExchange;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A single player pane for the lobby. The pane has a small fill if there is no
 * player. A medium fill for a pending player and a full circle fill if the
 * player is ready.
 * 
 * @author Tobias
 *
 */
public class LobbyPlayerPane extends Pane implements ILobbyPlayerPane {

	// MARK: Private variables
	/**
	 * Represents the delegate controller for this pane
	 */
	private LobbyController lobbyController;

	/**
	 * Index within the list of participating and empty players
	 */
	private int index;
	private Circle playerCircle;
	private Text playerName;
	private Button readyToPlayButton;

	// MARK: Initialization
	public LobbyPlayerPane() {
		this.setStyle("-fx-background-color: #FEFEFE; -fx-border-color: #CCC;");
		this.setPrefSize(600, 50);

		playerCircle = new Circle(15);
		playerCircle.setStrokeType(StrokeType.INSIDE);
		playerCircle.setFill(null);
		playerCircle.setStroke(Color.LIGHTGRAY);
		playerCircle.setStrokeWidth(playerCircle.getRadius() * StrokePercentageFill.small);
		playerCircle.setLayoutX(30);
		playerCircle.setLayoutY(25);

		playerName = new Text("---");
		playerName.setFont(new Font("Comic Sans MS", 21));
		playerName.setLayoutX(65);
		playerName.setLayoutY(32);

		readyToPlayButton = new Button();
		readyToPlayButton.setFont(new Font("Comic Sans MS", 13));
		readyToPlayButton.setLayoutX(510);
		readyToPlayButton.setLayoutY(12);
		setPlayerParticipating(false);

		this.getChildren().add(playerCircle);
		this.getChildren().add(playerName);

	}

	// MARK: Getter and setter
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setStrokeWidth(int width) {
		playerCircle.setStrokeWidth(width);
	}

	public void setBorderStrokeWidthFillPercent(double percentage) {
		playerCircle.setStrokeWidth(playerCircle.getRadius() * percentage);
	}

	public void setStrokeColor(Color color) {
		playerCircle.setStroke(color);
	}

	public void setPlayerName(String name) {
		playerName.setText(name);
		if (ModelExchange.GameOptions.getUserName().equals(playerName.getText())) {
			this.getChildren().add(readyToPlayButton);
		}
	}

	public void setPlayerParticipating(boolean ready) {
		if (ready) {
			readyToPlayButton.setText("Not ready");
			readyToPlayButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					lobbyController.updateParticipationEntry(index);
					setPlayerParticipating(false);
				}
			});
		} else {
			readyToPlayButton.setText("Ready");
			readyToPlayButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					lobbyController.updateParticipationEntry(index);
					setPlayerParticipating(true);
				}
			});
		}
	}

	public LobbyController getLobbyController() {
		return lobbyController;
	}

	public void setLobbyController(LobbyController lobbyController) {
		this.lobbyController = lobbyController;
	}

}
