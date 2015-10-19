package src.main.java.gui.navigation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import src.main.java.gui.Racetracker;
import src.main.java.gui.scenes.HelpController;
import src.main.java.logic.ModelExchange;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class which implements the interface RTMultiScene It is used as a basic
 * layout for all scenes used within this project.
 * 
 * @author Tobias
 */
public class MultiSceneBase implements IMultiSceneBase, Initializable {

	// MARK: Public variables
	public StackPaneController stackPaneController;
	
	// MARK: Private variables
	/**
	 * Stores the helpUrl for this scene.
	 */
	private String helpUrl;
	private String sceneName;
	protected boolean shouldReturnToSetup;

	// // MARK: Initialization
	/**
	 * Initialize a change listener for changes in the current scene name in
	 * ModelExchange. This will set the reference scene to the scene which is on
	 * screen.
	 */
	public void initialize(URL url, ResourceBundle rb) {
		ModelExchange.currentSceneName.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
				if (newValue == getName()) {
					if (ModelExchange.getController() != null) {
						ModelExchange.setControllerRefference(MultiSceneBase.this);
						sceneWillShow();
					}
					Racetracker.printInDebugMode("----- |GUI| ----- Go to scene: " + newValue + " -----");
				}
			}
		});
		
		// Set default help
		helpUrl = "/help/index.html";
	}

	// MARK: View updates
	/**
	 * Will be called if scenes changes and will appear on screen.
	 */
	protected void sceneWillShow() {

	}
	
	
	// MARK: Getter and Setter
	public void setScreenParent(StackPaneController sceneController) {
		stackPaneController = sceneController;
	}

	public void setName(String name) {
		this.sceneName = name;
	}

	public String getName() {
		return this.sceneName;
	}
	
	// MARK: Helper functions
	/**
	 * Opens the help web view in a separate window.
	 */
	@FXML
	private void openHelp() {
		if (Racetracker.helpController != null) {
			Racetracker.helpController.setResourceUrl(helpUrl);
		} else {
			try {
				Racetracker.printInDebugMode("----- |GUI| ----- Load help scene -----");

				// Initialize fxml loader from file
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/java/gui/scenes/Help.fxml"));

				// Load content
				Parent root = (Parent) loader.load();
				// Set loaded content as FXML scene graph
				Scene scene = new Scene(root);

				// Create new window
				Stage stage = new Stage();
				// Set window title
				stage.setTitle("Racetrack Help");
				// Set initilized scene as current scene
				stage.setScene(scene);
				
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						Racetracker.helpController = null;
					}
				});

				// Load help html file into controller
				HelpController hp = loader.getController();
				hp.setResourceUrl(helpUrl);

				// Present window to user
				stage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Method for loading an html resource file as the help
	 * in der web view.
	 * 
	 * @param url The html resource file
	 */
	protected void loadHelpFileAtResource(String url) {
		helpUrl = url;
	}

	// MARK: Client controller methods
	public void receivedOkayMessage() {
		Racetracker.printInDebugMode("----- |CCM| ----- RECEIVED OK Message -----");
	}

	public void receivedUpdateFromServer() {
		Racetracker.printInDebugMode("----- |CCM| ----- RECEIVED Update From Server -----");
	}

	public void receivedInvalidMoveMessage() {
	}
	
	public void disconnectedFromHost() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ModelExchange.GameOptions.setShouldReturnToSetup(true);
				
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Server disconnection");
				alert.setHeaderText("Host server is disconnected");
				alert.setContentText("The host connection is lost.\nYou will be redirected to the setup scene.\n"
						+ "Let the game keep playing by establishing a new connection to another host server!");
		
					alert.showAndWait();
				
				stackPaneController.setScene(Racetracker.sceneSetup_name);
			}
		});
	}
	
}
