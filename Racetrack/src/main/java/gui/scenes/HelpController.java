package src.main.java.gui.scenes;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import src.main.java.gui.Racetracker;
import sun.applet.Main;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class HelpController implements Initializable {

	@FXML
	private WebView webView;
	public Stage stage;
	
	/**
	 * Initializes the controller class.
	 */
	public void initialize(URL url, ResourceBundle rb) {
		Racetracker.printInDebugMode("----- |GUI| ----- Init HelpController -----");
		Racetracker.helpController = this;
	}
	
	public void setResourceUrl(String url) {
		String helpResourceUrl = Main.class.getResource(url).toExternalForm();  
		webView.getEngine().load(helpResourceUrl);
	}
}
