package src.main.java.gui.scenes;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.NavigationSceneBase;
import src.main.java.gui.panes.LobbyListLobbyBox;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.LobbyInformation;
import src.main.java.logic.LobbyInformationFactory;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.TrackFactory;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class LobbyListController extends NavigationSceneBase {
	@FXML
	private FlowPane lobbyListFlowPane;
	@FXML
	private StackPane scrollLeftPane;
	@FXML
	private StackPane scrollRightPane;
	@FXML
	private Button newGameButton;
	@FXML
	private Button refreshListOfGamesButton;
	
	
	private int flowPaneScrolledRight = 0;
	private int flowPaneItemsCount = 0;

	private boolean wantToJoinLobby = false;

	// MARK: Initialization
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		Racetracker.printInDebugMode("----- |GUI| ----- Init LobbyListController -----");
		
		final Tooltip newGameTooltip = new Tooltip();
		newGameTooltip.setText("Create new game");
		newGameButton.setTooltip(newGameTooltip);
		
		final Tooltip refreshTooltip = new Tooltip();
		refreshTooltip.setText("Update list of games");
		refreshListOfGamesButton.setTooltip(refreshTooltip);
		
		
		loadHelpFileAtResource("/help/listOfLobbies/index.html");
	}

	// MARK: FXML Buttons
	@FXML
	private void goBack(MouseEvent event) {
		Racetracker.printInDebugMode("----- |GUI| ----- Go Back -----");
		stackPaneController.setScene(Racetracker.sceneStart_name);
	}

	@FXML
	private void createNewGame(ActionEvent event) {
		Racetracker.printInDebugMode("----- |GUI| ----- Create new Game -----");
		Racetracker.printInDebugMode("----- |GUI| ----- Set Scene on: "+Racetracker.sceneCreateNewGame_name+" -----");
		stackPaneController.setScene(Racetracker.sceneCreateNewGame_name);
	}

	// MARK: Scroll lobbies left and right
	@FXML
	private void scrollRight(MouseEvent event) {
		if (flowPaneScrolledRight != flowPaneItemsCount - 2) {
			scrollLeftPane.setOpacity(1);
			flowPaneScrolledRight++;
			animateTranslationInFlowPane();

			if (flowPaneScrolledRight == flowPaneItemsCount - 2) {
				scrollRightPane.setOpacity(0.5);
			}
		}
	}

	@FXML
	private void scrollLeft(MouseEvent event) {
		if (flowPaneScrolledRight != 0) {
			scrollRightPane.setOpacity(1);
			flowPaneScrolledRight--;
			animateTranslationInFlowPane();

			if (flowPaneScrolledRight == 0) {
				lobbyListFlowPane.translateXProperty().set(0);
				scrollLeftPane.setOpacity(0.5);
			}
		}
	}

	private void animateTranslationInFlowPane() {
		Duration time = new Duration(100);
		KeyValue keyValue = new KeyValue(lobbyListFlowPane.translateXProperty(), -240 * flowPaneScrolledRight);
		KeyFrame keyFrame = new KeyFrame(time, keyValue);

		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(1);
		timeline.setAutoReverse(false);
		timeline.play();
	}

	// MARK: Button
	@FXML
	private void reloadLobbyList(ActionEvent event) {
		Racetracker.printInDebugMode("----- |GUI| ----- Reload -----");

		boolean shouldUseDemoSamples = false;

		if (shouldUseDemoSamples) {
			Random r = new Random();
			LobbyInformation[] informations;
			if (r.nextInt() % 3 == 2) {
				informations = new LobbyInformation[] { LobbyInformationFactory.getSampleLobby(0),
						LobbyInformationFactory.getSampleLobby(1), LobbyInformationFactory.getSampleLobby(2),
						LobbyInformationFactory.getSampleLobby(3), LobbyInformationFactory.getSampleLobby(4) };
			} else if (r.nextInt() % 3 == 1) {
				informations = new LobbyInformation[] { LobbyInformationFactory.getSampleLobby(0),
						LobbyInformationFactory.getSampleLobby(1), LobbyInformationFactory.getSampleLobby(2) };
			} else {
				informations = new LobbyInformation[] { LobbyInformationFactory.getSampleLobby(0),
						LobbyInformationFactory.getSampleLobby(1) };

				setLobbyList(informations);
			}

			setLobbyList(informations);
		} else {
			sceneWillShow();
		}
	}

	// MARK: Redraw
	private ILobbyInformation[] currentLobbyList;

	public void setLobbyList(ILobbyInformation[] newLobbyList) {
		currentLobbyList = newLobbyList;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				drawLobbies();
			}
		});
	}

	public void drawLobbies() {
		resetLobbyListsHBox();

		if (currentLobbyList == null) {
			return;
		}

		for (int i = 0; i < currentLobbyList.length; i++) {
			ILobbyInformation currentLobby = currentLobbyList[i];

			// if (currentLobby != null && !isLobbyFull(currentLobby)) {
			if (currentLobby != null) {
				if (!isLobbyFull(currentLobby)) {
					flowPaneItemsCount++;
					LobbyListLobbyBox plBox = new LobbyListLobbyBox();
					plBox.setLobbyListController(this);
					plBox.setLobbyIndex(i);
					plBox.setTrack(TrackFactory.getSampleTrack(currentLobby.getTrackId()));
					plBox.setLobbyName(currentLobby.getLobbyName());
					plBox.setPlayers(currentLobby.getPlayerNames());
					plBox.setHostName(getHostName(currentLobby.getPlayerNames()));
					plBox.setPlayMode(currentLobby.getPlayMode());

					lobbyListFlowPane.getChildren().add(plBox);
				}
			}
		}
	}
	
	
	private String getHostName(String[] names) {
		for (int i = 0; i < names.length; i++) {
			if (names[i] != null && names[i] != "") {
				return names[i];
			}
		}
		
		return "";
	}

	private boolean isLobbyFull(ILobbyInformation lobby) {
		String[] names = lobby.getPlayerNames();
		for (int i = 0; i < names.length; i++) {
			if (names[i] == null) {
				return false;
			}
		}
		return true;
	}

	private void resetLobbyListsHBox() {
		flowPaneItemsCount = 0;
		lobbyListFlowPane.getChildren().removeAll(lobbyListFlowPane.getChildren());
	}

	public void joinLobbyAtIndex(int index) {
		ILobbyInformation lobby = currentLobbyList[index];
		int id = lobby.getLobbyID();
		Racetracker.printInDebugMode("Join Lobby with id: " + id);
		wantToJoinLobby = true;

		ModelExchange.LobbyList.setRequestedLobbyToJoinID(id);
		Racetracker.printInDebugMode("----- |CCM| ----- Send request lobby entrance message to ID: " + id + " -----");
		ModelExchange.getController().sendRequestLobbyEntranceMessage();
	}

	@Override
	protected void sceneWillShow() {
		super.sceneWillShow();

		if (ModelExchange.getController().hasConnectedToServer() != null) {
			requestLobbyList();
		}
	}

	// MARK: Controller connection
	public void requestLobbyList() {
		Racetracker.printInDebugMode("----- |CCM| ----- Send request all lobbies message -----");
		ModelExchange.getController().sendRequestAllLobbiesMessage();
	}

	// MARK: ClientController
	@Override
	public void receivedOkayMessage() {
		super.receivedOkayMessage();
	}

	/**
	 * Called if the model updates or the player want to join a lobby
	 */
	@Override
	public void receivedUpdateFromServer() {
		super.receivedUpdateFromServer();

		Racetracker.printInDebugMode("----- |GUI| ----- Received new requesteted LobbyList -----");
		if (wantToJoinLobby) {
			wantToJoinLobby = false;
			Racetracker.printInDebugMode(ModelExchange.LobbyList.getCurrentLobbyId() + "");
			if (ModelExchange.LobbyList.getCurrentLobbyId() > 0) {
				setLobbyList(ModelExchange.LobbyList.getLobbyInformationList());
				stackPaneController.setScene(Racetracker.sceneLobby_name);
			}
		} else {
			setLobbyList(ModelExchange.LobbyList.getLobbyInformationList());
		}

	}

}
