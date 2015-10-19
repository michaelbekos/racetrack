package src.main.java.gui.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import src.main.java.gui.Racetracker;
import src.main.java.gui.canvas.CanvasGame;
import src.main.java.gui.navigation.NavigationSceneBase;
import src.main.java.gui.panes.LobbyPlayerPane;
import src.main.java.gui.utils.Colors;
import src.main.java.gui.utils.StrokePercentageFill;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.TrackFactory;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class LobbyController extends NavigationSceneBase {
	@FXML
	private Pane gridBaseScenePane;
	@FXML
	private Pane gameTrackPreview;
	@FXML
	private VBox playerBox;
	@FXML
	private Label lobbyNameLabel;
	@FXML
	public Button startGameButton;
	@FXML
	public Label playModeLabel;

	private CanvasGame gameAreaCanvas;

	private ILobbyInformation lobbyInformation;

	private boolean changedColor = false;
	private boolean changedReadyState = false;
	private boolean wantToLeaveLobby = false;

	// MARK: Initialization
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		Racetracker.printInDebugMode("----- |GUI| ----- Init LobbyController -----");
		loadHelpFileAtResource("/help/lobby/index.html");
		
		gameAreaCanvas = new CanvasGame(false);
		gameAreaCanvas.setWidth(600);
		gameAreaCanvas.setHeight(300);
		gameAreaCanvas.setStyle("-fx-border-color: #CCC;");

		gameTrackPreview.getChildren().add(gameAreaCanvas);
		gameAreaCanvas.widthProperty().bind(gameTrackPreview.widthProperty());
		gameAreaCanvas.heightProperty().bind(gameTrackPreview.heightProperty());
		
		startGameButton.setDisable(true);
		startGameButton.setVisible(false);

		ModelExchange.LobbyList.lobbyId.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				if (newValue != null) {
					// int newLobby = newValue.intValue();
					ILobbyInformation lobby = ModelExchange.LobbyList.getCurrentLobby();
					if (lobby != null) {
						lobby.printLobby();
						updateLobbyInfo();
					}
				}
			}
		});

		ModelExchange.CurrentGame.gameIsSet.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if (newValue == true) {
					Racetracker.printInDebugMode("----- |INF| ----- Game 'isSet' value is set to TRUE! -----");
					stackPaneController.setScene(Racetracker.sceneGameView_name);
				} else {
					Racetracker.printInDebugMode("----- |INF| ----- Game 'isSet' value is set to false! -----");
				}

			}
		});
		
		
	}

	@Override
	protected void sceneWillShow() {
		super.sceneWillShow();
		/*
		 * if (ModelExchange.LobbyList.getCurrentLobbyId() == 0) { int
		 * newLobbyIndex =
		 * ModelExchange.LobbyList.getLobbyInformationList()[ModelExchange.
		 * LobbyList.getLobbyInformationList().length-1].getLobbyID();
		 * ModelExchange.LobbyList.setCurrentLobbyId(newLobbyIndex); }
		 */
		updateLobbyInfo();
	}

	// MARK: Button actions
	@FXML
	private void startGame(ActionEvent event) {
		for (int i = 0; i < lobbyInformation.getParticipating().length; i++) {
			lobbyInformation.setParticipating(i, true);
		}
		Racetracker.printInDebugMode("----- |CCM| ----- Send options message -----");
		ModelExchange.getController().sendOptionsMessage(lobbyInformation);
	}
	
	@FXML
	private void goBack(MouseEvent event) {
		wantToLeaveLobby = true;
		Racetracker.printInDebugMode("----- |CCM| ----- Send leave loobby message -----");
		ModelExchange.getController().sendLeaveLobbyMessage();
	}

	// MARK: Setter
	private void setLobby(ILobbyInformation lobby) {
		if (lobby != null) {
			final ILobbyInformation finalLobby = lobby;
			this.lobbyInformation = lobby;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					setTrack(finalLobby.getTrackId());
					setUpPlayer(finalLobby.getPlayerNames(), finalLobby.getParticipating());
					
					setPlayModeLabel(finalLobby.getPlayMode());
					setLobbyName(finalLobby.getLobbyName());
				}
			});
		} else {
			Racetracker.printInDebugMode("----- |INF| ----- Lobby is null -----");
		}
	}

	private void setLobbyName(String name) {
		lobbyNameLabel.setText(name);
	}
	
	private void setPlayModeLabel(int playMode) {
		String playModeString = "";
		switch (playMode) {
		case 0:
			playModeString = "Easy";
			break;
		case 1:
			playModeString = "Medium";
			break;
		case 2:
			playModeString = "Hard";
			break;
		default:
			playModeString = "";
		}
		playModeLabel.setText(playModeString);
	}

	/**
	 * Generate the player rows.
	 * 
	 * @param playerNames
	 *            An array of player names.
	 * @param playerParticipating
	 *            The participating status for each player.
	 */
	private void setUpPlayer(final String[] playerNames, final boolean[] playerParticipating) {
		final LobbyController lobbyController = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Clean up
				playerBox.getChildren().removeAll(playerBox.getChildren());

				@SuppressWarnings("unused")
				final int playerIndex = getCurrenIndexPosition();

				// Generate for each player one row
				for (int i = 0; i < playerNames.length; i++) {
					LobbyPlayerPane lobbyPlayerPane = new LobbyPlayerPane();
					lobbyPlayerPane.setStrokeColor(Colors.Player.getColorWithId(i));
					lobbyPlayerPane.setIndex(i);
					lobbyPlayerPane.setPlayerParticipating(lobbyInformation.getParticipating()[i]);
					lobbyPlayerPane.setTranslateY(-i);

					if (playerNames[i] != null) {
						lobbyPlayerPane.setLobbyController(lobbyController);
						lobbyPlayerPane.setPlayerName(playerNames[i]);
						if (playerParticipating[i]) {
							lobbyPlayerPane.setBorderStrokeWidthFillPercent(StrokePercentageFill.full);
						} else {
							lobbyPlayerPane.setBorderStrokeWidthFillPercent(StrokePercentageFill.medium);
						}
					} else {
						@SuppressWarnings("unused")
						final int indexNumber = i;
						/*
						 * lobbyPlayerPane.setOnMouseClicked(new
						 * EventHandler<MouseEvent>() {
						 * 
						 * @Override public void handle(MouseEvent arg0) {
						 * Racetracker.printInDebugMode("Change to player " +
						 * indexNumber + " // " + playerIndex);
						 * lobbyInformation.changePlayerToIndex(indexNumber);
						 * 
						 * Racetracker.printInDebugMode(Arrays.toString(lobbyInformation.
						 * getPlayerNames()));
						 * 
						 * ModelExchange.getController().sendOptionsMessage(
						 * lobbyInformation); } });
						 */
					}

					playerBox.getChildren().add(lobbyPlayerPane);
				}
				
				// Show or hide host 'Start game' button
				if (isPlayerHost()) {
					// player is host
					startGameButton.setDisable(false);
					startGameButton.setVisible(true);
				} else {
					// player is not host
					startGameButton.setDisable(true);
					startGameButton.setVisible(false);
				}
			}
		});
	}

	public void updateParticipationEntry(int index) {
		lobbyInformation.toggleParticipating(index);
		Racetracker.printInDebugMode("----- |CCM| ----- Send options message -----");
		ModelExchange.getController().sendOptionsMessage(lobbyInformation);
	}

	private int getCurrenIndexPosition() {
		if (lobbyInformation != null) {
			Integer[] IDs = lobbyInformation.getPlayerIDs();
			int playerId = ModelExchange.GameOptions.getPlayerID();

			if (IDs != null) {
				for (int i = 0; i < IDs.length; i++) {
					if (IDs[i] != null) {
						if (IDs[i].equals(playerId)) {
							return i;
						}
					}
				}
			} else {
				Racetracker.printInDebugMode("----- |INF| ----- LobbyController: player IDs are not set -----");
			}
		} else {
			Racetracker.printInDebugMode("----- |INF| ----- LobbyController: LobbyInformation is not set -----");
		}
		return -1;
	}

	public void changedColor() {
		changedColor = true;
	}

	public void changedReadyState() {
		changedReadyState = true;
	}

	protected void setTrack(int trackId) {
		gameAreaCanvas.setTrack(TrackFactory.getSampleTrack(trackId));
	}
	
	// MARK: Logic
	private boolean isPlayerHost() {
		Integer[] ids = lobbyInformation.getPlayerIDs();
		for (int i = 0; i < lobbyInformation.getMaxPlayers(); i++) {
			if (ids[i] != null) {
				return (ids[i].equals(ModelExchange.GameOptions.getPlayerID()));
			}
		}
		return false;
	}

	// MARK: ClientController
	/**
	 * Will be called, if the player wants to leave.
	 */
	@Override
	public void receivedOkayMessage() {
		super.receivedOkayMessage();
	}

	/**
	 * Will be called if the player changes ready status or color
	 */
	@Override
	public void receivedUpdateFromServer() {
		super.receivedUpdateFromServer();

		if (changedReadyState) {
			changedReadyState = false;
		} else if (changedColor) {
			changedColor = false;
		} else if (wantToLeaveLobby) {
			wantToLeaveLobby = false;
			Racetracker.printInDebugMode("----- |GUI| ----- Leave LobbyController -----");
			ModelExchange.LobbyList.setCurrentLobbyId(-1);
			stackPaneController.setScene(Racetracker.sceneLobbyList_name);
		}
		updateLobbyInfo();
	}

	private void updateLobbyInfo() {
		setLobby(ModelExchange.LobbyList.getCurrentLobby());
		/*
		ILobbyInformation[] lobbies = ModelExchange.LobbyList.getLobbyInformationList();
		int newLobby = ModelExchange.LobbyList.getCurrentLobbyId();

		if (lobbies != null) {
			Racetracker.printInDebugMode("Lobbies: " + lobbies);
			for (int i = 0; i < lobbies.length; i++) {
				if (lobbies[i].getLobbyID() == newLobby) {
					lobbyInformation = lobbies[i];
					setLobby(lobbyInformation);
					break;
				}
			}
		}
		*/
	}

}
