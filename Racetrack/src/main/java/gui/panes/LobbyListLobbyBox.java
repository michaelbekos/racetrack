package src.main.java.gui.panes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import src.main.java.gui.canvas.CanvasGame;
import src.main.java.gui.scenes.LobbyListController;
import src.main.java.gui.utils.Colors;
import src.main.java.gui.utils.StrokePercentageFill;
import src.main.java.logic.Track;

/**
 * One lobby box for the list of lobbies. This box contains - track preview -
 * lobby name - player count and how many players are left
 * 
 * @author Tobias
 *
 */
public class LobbyListLobbyBox extends VBox implements ILobbyListLobbyBox {

	// MARK: Private variables
	/**
	 * Represents the delegate controller for this box
	 */
	private LobbyListController lobbyListController;

	/**
	 * Variable to store the lobbyId which the container represents.
	 */
	private int lobbyId;

	private AnchorPane lobbyTrackWrapperPane;
	private CanvasGame lobbyTrackCanvas;

	private StackPane lobbyNamePane;
	private Text lobbyNameText;

	private HBox lobbyPlayersWrapper;
	private StackPane[] lobbyPlayersPanes;

	private StackPane joinGameWrapper;
	
	private StackPane lobbyHostNameWrapper;
	private Text lobbyHostName;
	
	private StackPane lobbyPlayModeWrapper;
	private Text lobbyPlayModeText;

	/**
	 * This button gives the user the option to set and unset its 'game ready'
	 * status
	 */
	private Button gameReadyButton;

	// MARK: Initialization
	public LobbyListLobbyBox() {
		this.setStyle("-fx-background-color: #EEE;");
		this.setPrefSize(200, 350);
		this.setMinWidth(200);
		this.setMaxSize(200, 350);
		this.setScaleShape(false);

		lobbyTrackWrapperPane = new AnchorPane();
		lobbyTrackWrapperPane.setPrefSize(200, 150);
		lobbyTrackWrapperPane.setMinWidth(200);
		lobbyTrackWrapperPane.setMaxWidth(200);
		lobbyTrackWrapperPane.setStyle("-fx-border-color: #CCC;");
		lobbyTrackCanvas = new CanvasGame(false);
		lobbyTrackCanvas.setWidth(200);
		lobbyTrackCanvas.setHeight(150);
		lobbyTrackWrapperPane.getChildren().add(lobbyTrackCanvas);
		// Set anchor top/bottom/left/right to 0

		lobbyNamePane = new StackPane();
		lobbyNamePane.setScaleShape(false);
		lobbyNamePane.setPrefSize(200, 50);
		lobbyNamePane.setMinSize(200, 50);
		lobbyNamePane.setMaxWidth(200);
		lobbyNamePane.setStyle("-fx-border-color: #CCC;");
		lobbyNameText = new Text("-Track-");
		lobbyNameText.setFont(new Font("Comic Sans MS", 16));
		lobbyNamePane.getChildren().add(lobbyNameText);

		lobbyPlayersWrapper = new HBox();
		lobbyPlayersWrapper.setPrefSize(200, 50);
		lobbyPlayersWrapper.setMinSize(200, 50);
		lobbyPlayersWrapper.setStyle("-fx-border-color: #CCC;");

		joinGameWrapper = new StackPane();
		joinGameWrapper.setPrefSize(200, 50);
		joinGameWrapper.setStyle("-fx-border-color: #CCC;");
		gameReadyButton = new Button("Join game");
		gameReadyButton.setFont(new Font("Comic Sans MS", 14));
		joinGameWrapper.getChildren().add(gameReadyButton);
		gameReadyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				lobbyListController.joinLobbyAtIndex(lobbyId);
			}
		});
		
		lobbyHostNameWrapper = new StackPane();
		lobbyHostNameWrapper.setPrefSize(200, 25);
		lobbyHostNameWrapper.setMinWidth(200);
		lobbyHostNameWrapper.setMaxWidth(200);
		lobbyHostNameWrapper.setStyle("-fx-border-color: #CCC;");
		lobbyHostName = new Text("Host: ");
		lobbyHostName.setFont(new Font("Comic Sans MS", 12));
		lobbyHostNameWrapper.getChildren().add(lobbyHostName);
		
		
		lobbyPlayModeWrapper = new StackPane();
		lobbyPlayModeWrapper.setPrefSize(200, 25);
		lobbyPlayModeWrapper.setMinWidth(200);
		lobbyPlayModeWrapper.setMaxWidth(200);
		lobbyPlayModeWrapper.setStyle("-fx-border-color: #CCC;");
		lobbyPlayModeText = new Text("Mode:");
		lobbyPlayModeText.setFont(new Font("Comic Sans MS", 12));
		lobbyPlayModeWrapper.getChildren().add(lobbyPlayModeText);
		
		this.getChildren().add(lobbyTrackWrapperPane);
		this.getChildren().add(lobbyNamePane);
		this.getChildren().add(lobbyPlayersWrapper);
		this.getChildren().add(lobbyHostNameWrapper);
		this.getChildren().add(lobbyPlayModeWrapper);
		this.getChildren().add(joinGameWrapper);
		
	}

	// MARK: Getter and setter
	public void setTrack(Track track) {
		lobbyTrackCanvas.setTrack(track);
		lobbyTrackCanvas.updateView();
	}
	
	public void setLobbyName(String name) {
		lobbyNameText.setText(name);
	}
	
	public void setHostName(String name) {
		lobbyHostName.setText("Host: " + name);
	}
	
	public void setPlayMode(int playMode) {
		String playModeString = "";
		switch (playMode) {
		case 0:
			playModeString = "easy";
			break;
		case 1:
			playModeString = "medium";
			break;
		case 2:
			playModeString = "hard";
			break;
		default:
			playModeString = "";
		}
		
		lobbyPlayModeText.setText("Game mode: " + playModeString);
	}

	public void setPlayers(String[] playerNames) {
		int playerCount = playerNames.length;
		lobbyPlayersPanes = new StackPane[playerCount];

		for (int i = 0; i < playerCount; i++) {
			StackPane pane = new StackPane();
			pane.minWidth(40);
			pane.minHeight(40);
			pane.prefWidth(100);
			pane.prefHeight(50);

			Circle playerCircle = new Circle();
			playerCircle.setRadius(12);
			playerCircle.setStrokeType(StrokeType.INSIDE);
			playerCircle.setFill(null);

			playerCircle.setStroke(Colors.Player.getColorWithId(i));
			if (playerNames[i] != null) {
				// playerCircle.setStrokeWidth(100);
				playerCircle.setStrokeWidth(playerCircle.getRadius() * StrokePercentageFill.full);
			} else {
				// playerCircle.setStrokeWidth(4);
				playerCircle.setStrokeWidth(playerCircle.getRadius() * StrokePercentageFill.small);
			}

			pane.getChildren().add(playerCircle);

			lobbyPlayersPanes[i] = pane;

			lobbyPlayersWrapper.getChildren().add(pane);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}

	}

	public int getLobbyId() {
		return lobbyId;
	}
	
	public void setLobbyIndex(int lobbyId) {
		this.lobbyId = lobbyId;
	}

	public LobbyListController getLobbyListController() {
		return lobbyListController;
	}

	public void setLobbyListController(LobbyListController lobbyListController) {
		this.lobbyListController = lobbyListController;
	}
}
