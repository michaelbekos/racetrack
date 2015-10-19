package src.main.java.gui.navigation;

/**
 * Interface for using multiple scenes.
 * 
 * @author Tobias Kaulich
 */
public interface IMultiSceneBase {

	/**
	 * Adding the stackPane as parent view controller
	 * 
	 * @param sceneController
	 *            A super view controller for all scene graphs within the
	 *            appliction
	 */
	public void setScreenParent(StackPaneController sceneController);

	/**
	 * Sets the scene name for identification.
	 * @param name Scene name
	 */
	public void setName(String name);
	
	/**
	 * Returns the scene name
	 * @return Scene name
	 */
	public String getName();

	// MARK: ComClient methods
	/**
	 * This method is called, if the server updates the model. The update is
	 * found in ModelExchange.
	 */
	public void receivedUpdateFromServer();

	/**
	 * This method is called, if the client ask for an action and needs a
	 * confirmation (= okay) message.
	 */
	public void receivedOkayMessage();
	
	/**
	 * This method is called, if the client perfomed an invalid move.
	 */
	public void receivedInvalidMoveMessage();
	
	/**
	 * This method will be called, if the host is closed or does not respond any more.
	 */
	public void disconnectedFromHost();
}
