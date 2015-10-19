package src.main.java.gui.navigation;

import javafx.scene.Node;

public interface IStackPaneController {
	/**
	 * Adding a scene to the RTStackPaneController for later access
	 * 
	 * @param name
	 *            Name is a unique identifier for each scene
	 * @param scene
	 *            A new scene with it's own scene graph
	 */
	public void addScene(String name, Node scene);

	/**
	 * Get the scene based on your unique name you entered.
	 * 
	 * @param name
	 *            The unique identifier for a scene
	 * @return the fxml scene for a given name
	 */
	public Node getScene(String name);

	/**
	 * Loading a scene from an fxml file and add it to the RTStackPaneController
	 * 
	 * @param name
	 *            a unique identifier for each scene
	 * @param resource
	 *            a resource string to the fxml file you want to load
	 * @return if loading the scene from the resource has been successfully done
	 */
	public boolean loadScene(String name, String resource);

	/**
	 * Set new scene. A smooth fade-out fade-in transition will be performed
	 * while switching scenes. And binds the new scenes dimensions to the main
	 * window dimensions.
	 * 
	 * @param name
	 *            The unique identifier for a scene
	 * @return Weather a scene could be replace the current scene graph
	 */
	public boolean setScene(final String name);

	/**
	 * Unload scene in your loaded scenes and indicates wheter it was
	 * successfull or not
	 * 
	 * @param name
	 *            The unique identifier for a scene
	 * @return Weather a scene could be removed. If not it hasn't been existed
	 *         in the hashmap.
	 */
	public boolean unloadScene(String name);
}
