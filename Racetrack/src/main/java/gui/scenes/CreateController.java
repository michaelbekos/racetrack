/*
 * Create Game
 */
package src.main.java.gui.scenes;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import src.main.java.gui.Racetracker;
import src.main.java.gui.canvas.CanvasGame;
import src.main.java.gui.navigation.NavigationSceneBase;
import src.main.java.gui.utils.Colors;
import src.main.java.logic.LobbyInformation;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.TrackFactory;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class CreateController extends NavigationSceneBase {
	@FXML
	private HBox trackHbox;
	@FXML
	private TextField lobbyNameText;
	@FXML
	private ChoiceBox<String> playersChoiceBox;
	@FXML
	private ChoiceBox<String> aisChoiceBox;
	@FXML
	private ChoiceBox<String> modeChoiceBox;
	@FXML
	private Button createButton;

	private CanvasGame[] trackCanvases;

	private int playersCount;
	@SuppressWarnings("unused")
	private int botsCount;
	private int playMode;
	private int selectedTrack;
	
	private boolean hasSendCreateMessageToServer;
	
	Random r = new Random();

	// MARK: Initialization
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		Racetracker.printInDebugMode("----- |GUI| ----- Init CreateController -----");
		loadHelpFileAtResource("/help/newGame/index.html");
		
		// Add listener to player choice box
		playersChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value) {
				playersCount = new_value.intValue() + 2;
			}
		});

		// Set player choice box items
		playersChoiceBox.setItems(
				FXCollections.observableArrayList("2 Players", "3 Players", "4 Players", "5 Players"));
		
		modeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				playMode = newValue.intValue();
			}
		});
		modeChoiceBox.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
		

		aisChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value) { 
				botsCount = new_value.intValue();
			} 
		});
		

		aisChoiceBox.setItems(FXCollections.observableArrayList("No Bot", "1 Bot", "2 Bots", "3 Bots", "4 Bots"));
		aisChoiceBox.getSelectionModel().select(0);

		//aisChoiceBox.setDisable(true);

		// Set array of RTCanvasGame with one item per sample track
		trackCanvases = new CanvasGame[TrackFactory.getSampleTrackCount()];

		// Setup sample tracks
		for (int i = 0; i < trackCanvases.length; i++) {
			StackPane trackPreviewWrapper = new StackPane();
			trackPreviewWrapper.setPrefSize(200, 200);
			trackPreviewWrapper.setMinSize(100, 200);

			// Generate and set up track preview
			CanvasGame trackPreview = new CanvasGame(TrackFactory.getSampleTrack(i), false);
			trackPreview.setDrawBackground(false);
			trackPreview.widthProperty().bind(trackPreviewWrapper.widthProperty());
			trackPreview.heightProperty().bind(trackPreviewWrapper.heightProperty());
			trackPreview.updateView();
			trackPreview.setTrackBackgroundColor(Colors.Track.grasBackground);

			trackPreviewWrapper.getChildren().add(trackPreview);

			trackHbox.setPadding(new Insets(10));
			trackHbox.getChildren().add(trackPreviewWrapper);
			HBox.setHgrow(trackPreviewWrapper, Priority.ALWAYS);

			trackCanvases[i] = trackPreview;

			trackPreviewWrapper.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					int newTrackId = (int) (event.getSceneX() / (gridBaseScenePane.getWidth() / trackCanvases.length));
					setSelectedTrack(newTrackId);
				}
			});
		}
		
		// Setup only non empty track names
		lobbyNameText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boolean invalid = (newValue.isEmpty()   // If text is empty
						|| newValue.matches("(\\s)+") // If text only contains spaces
						|| !newValue.matches(".{1,15}") // If text has size between 1-15
						);
				createButton.setDisable(invalid);
			}
		});
		
		
	}
	
	@Override
	protected void sceneWillShow() {
		super.sceneWillShow();
		
		// Select 2 player option
		playersChoiceBox.getSelectionModel().select(0);
		// Set medium play mode
		modeChoiceBox.getSelectionModel().select(1);
		
		// Show random track
		int randomTrackId = r.nextInt(TrackFactory.getSampleTrackCount());
		setSelectedTrack(randomTrackId);
		
		// Reset empty game name
		lobbyNameText.setText("New game");
		
		hasSendCreateMessageToServer = false;
	}

	// MARK: Button
	@FXML
	private void createNewGame(ActionEvent event) {
		if (!hasSendCreateMessageToServer) {
			hasSendCreateMessageToServer = true;
			sendNewGameToServer();
		}
	}

	@FXML
	public void goBack() {
		resetScene();
		stackPaneController.setScene(Racetracker.sceneLobbyList_name);
	}

	private void sendNewGameToServer() {
		String[] names = new String[playersCount];
		names[0] = ModelExchange.GameOptions.getUserName();
		boolean[] participating = new boolean[playersCount];
		Integer[] playerIDs = new Integer[playersCount];
		playerIDs[0] = ModelExchange.GameOptions.getPlayerID();

		String lobbyName = lobbyNameText.getText() == null || lobbyNameText.getText().isEmpty()
				|| lobbyNameText.getText().equals(" ") ? "Unnamed game" : lobbyNameText.getText();

		LobbyInformation lobbyInformation = new LobbyInformation(names, playerIDs, participating, selectedTrack,
				lobbyName, false, 0, playMode);
		lobbyInformation.setAmountOfAIs(botsCount);

		Racetracker.printInDebugMode("----- |CCM| ----- Send Create Lobby Message -----");
		ModelExchange.getController().sendCreateLobbyMessage(lobbyInformation);
	}

	// MARK: Helper function
	private void resetScene() {
		hasSendCreateMessageToServer = false;
	}
	
	private void setSelectedTrack(int trackId) {
		for (int i = 0; i < trackCanvases.length; i++) {
			trackCanvases[i].setDrawBackground(false);
			trackCanvases[i].updateView();
		}
		trackCanvases[trackId].setDrawBackground(true);
		trackCanvases[trackId].updateView();
		selectedTrack = trackId;
	}

	// MARK: ClientController
	@Override
	public void receivedOkayMessage() {
		super.receivedOkayMessage();

		/*
		 * // Redraws the lobby in lobbyController int currentLobbyId = ModelExchange.LobbyList.getCurrentLobbyId();
		 * ModelExchange.LobbyList.setCurrentLobbyId(currentLobbyId);
		 * Racetracker.printInDebugMode("Joined lobby, redirect to lobby scene: " + currentLobbyId);
		 */
	}

	/**
	 * Will be called if the user wants to create and join a new lobby
	 */
	@Override
	public void receivedUpdateFromServer() {
		super.receivedUpdateFromServer();
		Racetracker.printInDebugMode("----- |GUI| ----- Got updated LobbyList with new created Lobby -----");
		
		resetScene();
		stackPaneController.setScene(Racetracker.sceneLobby_name);
	}
}
