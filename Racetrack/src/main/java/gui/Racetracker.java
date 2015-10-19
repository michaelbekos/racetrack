package src.main.java.gui;

import java.io.IOException;

import javax.swing.ImageIcon;

import src.main.java.gui.navigation.StackPaneController;
import src.main.java.gui.scenes.HelpController;
import src.main.java.logic.ModelExchange;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Main game class. Initializing game gui.
 * 
 * @author Tobias Kaulich
 */
public class Racetracker extends Application {

	private static boolean debugMode = false;

	private static ModelExchange model;
	
	private static Stage stage;
	
	/**
	 * Representing the scene controller for a separate help window. 
	 */
	public static HelpController helpController;

	// Static names for the RTStackPaneController for loading scenes into the
	// scene graph
	// Start Screen
	public static String sceneStart_name = "Start";
	public static String sceneStart_fxml = "/src/main/java/gui/scenes/Start.fxml";
	// Lobby
	public static String sceneLobbyList_name = "LobbyList";
	public static String sceneLobbyList_fxml = "/src/main/java/gui/scenes/LobbyList.fxml";
	// GameView
	public static String sceneGameView_name = "Game";
	public static String sceneGameView_fxml = "/src/main/java/gui/scenes/Game.fxml";
	// Options
	public static String sceneSetup_name = "Setup";
	public static String sceneSetup_fxml = "/src/main/java/gui/scenes/Setup.fxml";
	// Join Game
	public static String sceneLobby_name = "Lobby";
	public static String sceneLobby_fxml = "/src/main/java/gui/scenes/Lobby.fxml";
	// Create Game
	public static String sceneCreateNewGame_name = "Create";
	public static String sceneCreateNewGame_fxml = "/src/main/java/gui/scenes/Create.fxml";

	@Override
	public void start(final Stage stage) throws IOException {
		Racetracker.printInDebugMode("--------------Racetrack SS2015--------------");
		// Set the stage icon on Mac and Windowas as well as the taskbar icon on windows
		stage.getIcons().add(new Image(getClass().getResourceAsStream("scenes/resources/racetrack512.png")));
		
		// Set the stage 'taskbar' icon on MacOS
//		try {
//			java.net.URL iconURL = getClass().getResource("scenes/resources/racetrack512.png");
//			java.awt.Image image = new ImageIcon(iconURL).getImage();
//			com.apple.eawt.Application.getApplication().setDockIconImage(image);
//	    } catch (Exception e) {
//	        // Catches by Windows or Linux.
//	    }
		
		
		stage.initStyle(StageStyle.DECORATED);
		
		Racetracker.stage = stage;
		
		/**
		 * This code will be called if the player tries to close the application
		 * by pressing the default system X-Icon.
		 */
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
//				if (ModelExchange.CurrentGame.getGame() != null) {
//					event.consume();
//					Alert alert = new Alert(Alert.AlertType.INFORMATION);
//					alert.setTitle("Exit denied");
//					alert.setHeaderText("Please leave the game first.");
//					alert.setContentText("Please use the X-Button on the game view first.\n"
//							+ "This will leave the game and returns you to the list of games\n."
//							+ "From there, you can close the game.");
//
//						alert.showAndWait();
//				} else {
					Racetracker.printInDebugMode("CLOSE EVENT: \r\n" + event);
					Racetracker.closeGame();
//				}
			}
		});
		
		Racetracker.printInDebugMode("------------ Initializing Scenes ------------");
		/*
		 * Create an new RTStackPaneController for managing multiple scenes.
		 * RTStackPaneController dynamically changes scenes inside a stackPane
		 * for a lightweight scene graph. After adding them to the
		 * RTStackPaneController, those scenes can be loaded and displayed with
		 * 'setScene' with their name as an argument.
		 */
		StackPaneController mainRTStackPaneController = new StackPaneController();
		mainRTStackPaneController.loadScene(Racetracker.sceneGameView_name, Racetracker.sceneGameView_fxml);
		mainRTStackPaneController.loadScene(Racetracker.sceneLobby_name, Racetracker.sceneLobby_fxml);
		mainRTStackPaneController.loadScene(Racetracker.sceneCreateNewGame_name, Racetracker.sceneCreateNewGame_fxml);
		mainRTStackPaneController.loadScene(Racetracker.sceneLobbyList_name, Racetracker.sceneLobbyList_fxml);
		mainRTStackPaneController.loadScene(Racetracker.sceneSetup_name, Racetracker.sceneSetup_fxml);
		mainRTStackPaneController.loadScene(Racetracker.sceneStart_name, Racetracker.sceneStart_fxml);
		Racetracker.printInDebugMode("---------- Finished Initialization ----------");
		
		// New group for adding all mainRTStackPaneController to them
		Group root = new Group();
		root.getChildren().addAll(mainRTStackPaneController);
		
		// Generate new Scene with all possible scenes and limit min Size to
		// 900x700
		Scene scene = new Scene(root, 800, 600);
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		// Add generated scene to the stage
		stage.setScene(scene);
		stage.setTitle("Racetrack");

		// Load init scene into the stage
		mainRTStackPaneController.setScene(Racetracker.sceneSetup_name);

		// Show stage -> display window
		stage.show();

		/*
		 * // OLD CODE; with manual FXML loading, this was outsoucred to the
		 * RTStackPaneController! // For reference only FXMLLoader loader = new
		 * FXMLLoader(getClass().getResource("Scene_Game_Start.fxml")); Parent
		 * root = (Parent)loader.load();
		 * 
		 * Scene scene = new Scene(root);
		 * 
		 * stage.setTitle("Racetracker"); 
		 * stage.setScene(scene);
		 * 
		 * stage.setMinWidth(900); stage.setMinHeight(700);
		 * 
		 * stage.show();
		 */
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * @return The current Model as ModelExchange
	 */
	public static ModelExchange getModel() {
		if (model == null) {
			model = new ModelExchange();
		}
		return model;
	}

	/**
	 * Public method used to print out objects, if the debug boolean is set.
	 * 
	 * @param string
	 *            The output which should be printed in one line.
	 */
	public static void printInDebugMode(String string) {
		if (debugMode) {
			System.out.println(string);
		}
	}

	/**
	 * Public method to close the game as well as sending a disconnect message.
	 */
	public static void closeGame() {
		Racetracker.printInDebugMode("----- |AKT| ----- Close Connection -----");
		if (ModelExchange.getController().hasConnectedToServer() != null) {
			Racetracker.printInDebugMode("----- |CCM| ----- Kill Connection -----");
			ModelExchange.getController().killConnection();
		}
		
		Racetracker.stage.close();
	}
}
