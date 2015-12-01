package src.main.java.gui.navigation;

import java.util.HashMap;

import src.main.java.gui.Racetracker;
import src.main.java.logic.ModelExchange;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *  Ref: https://blogs.oracle.com/acaicedo/entry/managing_multiple_screens_in_javafx1
 * 
 * For easier adapting changing views, this RTStackPaneController extends an AnchorPane
 * where loaded scenes width and height can be bined together.
 * 
 * @author Tobias Kaulich
 */
public class StackPaneController extends AnchorPane implements IStackPaneController {
    
	// MARK: Private variables
    /**
     * Hashmap for storing a scene name next to a whole scene.
     * Those scenes can be loaded with setScene and the given name
     * (Names should be statically saved within the RTRacetracker class)
     */
    private HashMap<String, Node> scenes = new HashMap<>();
    
    // MARK: Initialization
    public StackPaneController() {
        super();
    }
    
    // MARK: Functions
    public void addScene(String name, Node scene) {
    	scenes.put(name, scene);
    }
    
    public Node getScene(String name) {
        return scenes.get(name);
    }
    
    public boolean loadScene(String name, String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Racetracker.printInDebugMode("----- |GUI| ----- Resource Scene: "+getClass().getResource(resource).toString());
            Parent root = (Parent) loader.load();
            IMultiSceneBase sceneController = ((IMultiSceneBase) loader.getController());
            sceneController.setScreenParent(this);
            sceneController.setName(name);
            addScene(name, root);
            return true;
        } catch (Exception e) {
        	Racetracker.printInDebugMode(e.toString());
        	Racetracker.printInDebugMode(name + " : " + resource + " : " + e.getMessage());
            return false;
        }
    }
    
    public boolean setScene(final String name) {
    	// If scene has been previously loaded
    	Racetracker.printInDebugMode("----- |GUI| ----- Set Scene: "+(name)+" -----");
    	if (scenes.get(name) != null) {
            final DoubleProperty opacity = opacityProperty();
            
            if (!getChildren().isEmpty()) {
                // If there is more than one scene
            	// Inform controller that he will be presented!
            	ModelExchange.currentSceneName.set(name);
            	// Start transitioning
                Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                    new KeyFrame(new Duration(200), new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            // Remove current scene
                            getChildren().remove(0);
                            // Add new scene to scene graph
                            //getChildren().add(0, scenes.get(name));
                            
                            Stage theStage = ((Stage) getScene().getWindow());
                            
                            Region sceneAsRegion = (Region) scenes.get(name);
                            getChildren().setAll(sceneAsRegion);
                            
                            sceneAsRegion.prefWidthProperty().bind(theStage.widthProperty());
                            sceneAsRegion.prefHeightProperty().bind(theStage.heightProperty().subtract(20));
                            
                            /*
                            Region sceneAsRegion = (Region) scenes.get(name);
                            getChildren().add(0, sceneAsRegion);
                            sceneAsRegion.prefWidthProperty().bind(widthProperty());
                            sceneAsRegion.prefHeightProperty().bind(heightProperty());
                            */
                            

                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(100), new KeyValue(opacity, 1.0))
                            );
                            fadeIn.play();
                        }
                    }, new KeyValue(opacity, 0.0))
                );
                fade.play();
            } else {
            	ModelExchange.currentSceneName.set(name);
            	
                Stage theStage = ((Stage) getScene().getWindow());
                            
                Region sceneAsRegion = (Region) scenes.get(name);
                getChildren().setAll(sceneAsRegion);

                sceneAsRegion.prefWidthProperty().bind(theStage.widthProperty());
                sceneAsRegion.prefHeightProperty().bind(theStage.heightProperty().subtract(20));
                            
                /*
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(200), new KeyValue(opacity, 1.0))
                );
                fadeIn.play();
                */
            }
        
            return true;
        } else {
        	Racetracker.printInDebugMode("The scene (" + name + ") hasn't been loaded yet.");
            return false;
        }
    }
    
    public boolean unloadScene(String name) {
        if (scenes.remove(name) != null) {
            return true;
        } else {
        	Racetracker.printInDebugMode("Scene didn't exist in the hashmap.");
            return false;
        }
    }
    
    
    
}
