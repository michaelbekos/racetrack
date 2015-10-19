package src.main.java.gui.scenes;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.NavigationSceneBase;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class StartController extends NavigationSceneBase {

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		Racetracker.printInDebugMode("----- |GUI| ----- Init StartController -----");
		
		loadHelpFileAtResource("/help/start/index.html");
	}

	@FXML
	private void startGame(MouseEvent event) {
		stackPaneController.setScene(Racetracker.sceneLobbyList_name);
	}

	@FXML
	private void options(MouseEvent event) {
		stackPaneController.setScene(Racetracker.sceneSetup_name);
	}

	@FXML
	private void exit(MouseEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Exit game");
		alert.setHeaderText("Exit game?");
		alert.setContentText(
				"Are you sure you want to end this amazing game? By pressing the 'OK' button you will leave this game.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Racetracker.closeGame();
//			Stage theStage = (Stage) gridBaseScenePane.getScene().getWindow();
//			theStage.close();
		}
		
		loadHelpFileAtResource("/help/start/index.html");
	}

	// MARK: ClientController
	@Override
	public void receivedOkayMessage() {
		super.receivedOkayMessage();
	}

	@Override
	public void receivedUpdateFromServer() {
		super.receivedUpdateFromServer();
	}

}
