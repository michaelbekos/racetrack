/*
 * Options
 */
package src.main.java.gui.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.main.java.gui.Racetracker;
import src.main.java.gui.navigation.NavigationSceneBase;
import src.main.java.logic.ILobbyInformation;
import src.main.java.logic.ModelExchange;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class SetupController extends NavigationSceneBase {
	@FXML
	private TextField hostIpTextfield;
	@FXML
	private TextField portTextfield;
	@FXML
	private TextField usernameTextfield;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	@FXML
	private ChoiceBox<String> aiChoiceBox;
	
	private int mAiId;
	
	private boolean alreadySetupGame = false;
	
	private boolean hostIpValid = true;
	private boolean hostPortValid = true;
	private boolean usernameValid = false;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		Racetracker.printInDebugMode("----- |GUI| ----- Init SetupController -----");
		loadHelpFileAtResource("/help/setup/index.html");
		
		chechIfEverythingIsValid();
		
		hostIpTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				hostIpValid = (newValue.isEmpty()  // If text is empty
						|| newValue.matches("^(\\s)+") // If text only contains spaces
						|| !newValue.matches("(\\d){1,3}\\.(\\d){1,3}\\.(\\d){1,3}\\.(\\d){1,3}")); // If text is not an ip adress
				chechIfEverythingIsValid();
				
				colorHightlightOutline(hostIpTextfield, hostIpValid);
			}
		});
		
		portTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				hostPortValid = (newValue.isEmpty()  // If text is empty
						|| newValue.matches("^(\\s)+") // If text only contains spaces
						|| !newValue.matches("\\d+")); // If text is not a (port) number
				chechIfEverythingIsValid();
				
				colorHightlightOutline(portTextfield, hostPortValid);
			}
		});
		
		usernameTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				usernameValid = (newValue.isEmpty()  // If text is empty
						|| newValue.matches("^(\\s)+") // If text only contains spaces
						|| !newValue.matches("[a-zA-Z0-9_]{1,15}") // If text has size between 1-15
						);
				chechIfEverythingIsValid();
				
				colorHightlightOutline(usernameTextfield, usernameValid);
			}
		});
		/*
		 * HOWTO ADD AN AI.
		 * 
		 * 1. Write the AI:
		 *    Take an existing AI, like "AI_NoMover.java" and copy it.
		 *    Then add your changes and write it as desired.
		 * 2. Add your new AI in the list below (where is says  "YOUR NEW AI"  )
		 * 3. Count through the list, Human == 1, determine the number of your AI
		 * 4. Write the number in the constructor of your AI to the member mTypeID.
		 * 5. Add your AI in Administation.java in the methode setPlayerNameAndAiType into the
		 *    switch in the same fashion as the others are added with its determined number,
		 *    where it says  ADD YOUR NEW AI ALSO HERE. ( Don't forget the break ... )
		 * 6. Add your AI to Game.java in the constructor: public Game(ILobbyInformation lobby)
		 *    into the switch in the same way the others got added, where it says
		 *    ADD YOUR NEW AI ALSO HERE. ( Don't forget the break ... )
		 * 7. Add to the last switch in ReceiveStartGameMessageClientHandler.java, in 
		 *    updateModel too
		 */

		aiChoiceBox.setItems( FXCollections.observableArrayList( /*"Free",*/ "Human", "No Mover", "Random", "Puckie", "AIStar", "Crasher", "Zigzag" /*, "YOUR NEW AI" */ ) );
		aiChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>()
				{
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value)
					{
						mAiId=new_value.intValue();
					}
				});
		mAiId=0;
		aiChoiceBox.getSelectionModel().select(0);
		
	}
	
	@Override
	protected void sceneWillShow() {
		super.sceneWillShow();
		
		if (ModelExchange.GameOptions.isShouldReturnToSetup()) {
			alreadySetupGame = false;
			cancelButton.setText("Cancel/Exit");
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				hostIpTextfield.setText(ModelExchange.GameOptions.getHostIp());
				portTextfield.setText(String.valueOf(ModelExchange.GameOptions.getHostPort()));
				usernameTextfield.setText(ModelExchange.GameOptions.getUserName());
			}
		});

	}

	// MARK: Buttons
	@FXML
	private void cancelButtonPressed(ActionEvent event) {
		if (alreadySetupGame) {
			goBackToStartScreen();
		} else {
			Racetracker.closeGame();
//			Stage theStage = (Stage) gridBaseScenePane.getScene().getWindow();
//			theStage.close();
		}
	}

	@FXML
	private void saveButtonPressed(ActionEvent event) {
		/*
		String currentPort = String.valueOf(ModelExchange.GameOptions.getHostPort());
		String currentIp = ModelExchange.GameOptions.getHostIp();
		if ( !hostIpTextfield.getText().equals(currentIp) || !portTextfield.getText().equals(currentPort)) {
			// If ip or port changed
			
		}
		
		String currentUserName = ModelExchange.GameOptions.getUserName();
		if (usernameTextfield.getText().equals(currentUserName)) {
			// If only username changes
		}
		*/
		ModelExchange.GameOptions.setShouldReturnToSetup(false);
		saveOptions();
		connectToControllerAndSendUserName();
	}

	// MARK: Actions
	/**
	 * Save options to static fields in the RaceTracker class
	 */
	private void saveOptions() {
		ModelExchange.GameOptions.setHostIp(hostIpTextfield.getText());
		try {
			Integer port = Integer.parseInt(portTextfield.getText());
			ModelExchange.GameOptions.setHostPort(port);
		} catch (Exception e) {
			ModelExchange.GameOptions.setHostPort(0);
		}

		ModelExchange.GameOptions.setUserName(usernameTextfield.getText());
		ModelExchange.GameOptions.setAiID( mAiId+1 ); // plus one here because we don't use free.
	}

	/**
	 * Method to go back to the start screen
	 */
	private void goBackToStartScreen() {
		stackPaneController.setScene(Racetracker.sceneStart_name);
	}

	// MARK: UI - Component updates
	/**
	 * Disables the UI
	 */
	private void disableUI() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				hostIpTextfield.setDisable(true);
				portTextfield.setDisable(true);
				usernameTextfield.setDisable(true);
				saveButton.setDisable(true);
			}
		});

	}

	/**
	 * Enables the UI
	 */
	private void enableUI() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				hostIpTextfield.setDisable(false);
				portTextfield.setDisable(false);
				usernameTextfield.setDisable(false);
				saveButton.setDisable(false);
			}
		});

	}

	private void showIpAndPortError() {
		hostIpTextfield.setStyle("-fx-border-color: rgba(255,0,0,0.5)");
		portTextfield.setStyle("-fx-border-color: rgba(255,0,0,0.5)");
	}

	private void showUsernameWaiting() {
		usernameTextfield.setStyle("-fx-border-color: rgba(255,255,0,0.5)");
	}

	private void showIpAndPortSuccess() {
		hostIpTextfield.setStyle("-fx-border-color: rgba(0,255,0,0.5)");
		portTextfield.setStyle("-fx-border-color: rgba(0,255,0,0.5)");
	}

	private void showUsernameSuccess() {
		usernameTextfield.setStyle("-fx-border-color: rgba(0,255,0,0.5)");
	}
	
	// MARK: Helper functions
	private void chechIfEverythingIsValid() {
		saveButton.setDisable(hostIpValid || hostPortValid || usernameValid);
	}
	
	private void colorHightlightOutline(final TextField textField, final boolean hightlight) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (hightlight) {
					textField.setStyle("-fx-border-color: rgba(255,0,0,0.5)");
				} else {
					textField.setStyle("-fx-border-color: #CCC");
				}
			}
		});
	}

	// MARK: ClientController
	/**
	 * Send a Message to the client controller According to which action is
	 * performed as well as which actions Succeeded or failed a visual feedback
	 * is presented to the user.
	 */
	private void connectToControllerAndSendUserName() {
		disableUI();
		
		// Disconnect from server if connected
		if (ModelExchange.getController().hasConnectedToServer() != null) {
//			Racetracker.printInDebugMode("----- |CCM| ----- Send disconnect message -----");
//			ModelExchange.getController().sendDisconnectMessage();
			ModelExchange.getController().killConnection();
			ModelExchange.resetController();
		}

		// Start new connection
		Racetracker.printInDebugMode("----- |CCM| ----- Send start connection message -----");
		ModelExchange.getController().startConnection( ModelExchange.GameOptions.getHostIp(),
				                                       ModelExchange.GameOptions.getHostPort() );
		ModelExchange.setControllerRefference(this);

		while (true) {
			if (ModelExchange.getController().hasConnectedToServer() == null) {
				// Waiting for connection
			} else if (ModelExchange.getController().hasConnectedToServer()) {
				// Connected successfully
				Racetracker.printInDebugMode("----- |GUI| ----- Successfully connected to server -----");
				showIpAndPortSuccess();
				
				showUsernameWaiting();
				Racetracker.printInDebugMode( "----- |CCM| ----- Send user name and AI type ID message -----" );
				ModelExchange.getController().sendUserNameAndAiTypeIdMessage( this.usernameTextfield.getText(), mAiId+1 );

				enableUI();
				break;
			} else {
				// Connection failed
				// show error
				Racetracker.printInDebugMode("----- |GUI| ----- Server connection failed -----");
				showIpAndPortError();
				enableUI();
				break;
			}
		}
	}

	/**
	 * Receive an okayMessage from the client if the user name has been
	 * successfully transfered.
	 */
	@Override
	public void receivedOkayMessage() {
		super.receivedOkayMessage();
		Racetracker.printInDebugMode("----- |GUI| ----- Username had been accepted -----");
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				showUsernameSuccess();
				alreadySetupGame = true;

				cancelButton.setText("Cancel");
				
				Stage stage = (Stage) gridBaseScenePane.getScene().getWindow();
				String userName = ModelExchange.GameOptions.getUserName();
				stage.setTitle("Racetrack: " + userName);
			}
		});
		
		goBackToStartScreen();
	}

	/**
	 * Receives an update from server message if the client has successfully
	 * connected to the server.
	 */
	@Override
	public void receivedUpdateFromServer() {
		super.receivedUpdateFromServer();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Racetracker.printInDebugMode("XXX UPDATEFROMSERVER");
				showUsernameSuccess();
				goBackToStartScreen();
			}
		});
	}

}
