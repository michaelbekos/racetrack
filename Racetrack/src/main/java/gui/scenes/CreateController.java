/*
 * Create Game
 */
package src.main.java.gui.scenes;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import src.main.java.gui.Racetracker;
import src.main.java.gui.canvas.CanvasGame;
import src.main.java.gui.navigation.NavigationSceneBase;
import src.main.java.gui.utils.Colors;
import src.main.java.logic.LobbyInformation;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.TrackFactory;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class CreateController extends NavigationSceneBase {
	@FXML
	private HBox trackHbox;
	@FXML
	private TextField lobbyNameText;
	@FXML
	private ChoiceBox<String> modeChoiceBox;
	@FXML
	private ChoiceBox<String> slot2ChoiceBox;
	@FXML
	private ChoiceBox<String> slot3ChoiceBox;
	@FXML
	private ChoiceBox<String> slot4ChoiceBox;
	@FXML
	private ChoiceBox<String> slot5ChoiceBox;
	@FXML
	private Button createButton;
	
	private HashMap<StackPane,Integer> trackPreviewWrappers = new HashMap<StackPane,Integer>();

	private CanvasGame[] trackCanvases;
	private ObservableList<String> playerOptions;
	
	//TODO: Write playersCount properly!
	//private int playersCount;
	@SuppressWarnings("unused")
	//TODO: Write botsCount properly!
	//private int botsCount;
	private List<Integer> playerSlotsSettings;
	
	private int playMode;
	private int selectedTrack;
	
	private boolean hasSendCreateMessageToServer;
	
	Random r = new Random();

	// MARK: Initialization
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		Racetracker.printInDebugMode("----- |GUI| ----- Init CreateController -----");
		loadHelpFileAtResource("/help/newGame/index.html");
		playerOptions=FXCollections.observableArrayList( "Closed", "Open" );
		playerSlotsSettings=new ArrayList<Integer>();
		playerSlotsSettings.add( new Integer(1) );
		playerSlotsSettings.add( new Integer(0) );
		playerSlotsSettings.add( new Integer(0) );
		playerSlotsSettings.add( new Integer(0) );
		playerSlotsSettings.add( new Integer(0) );
		initSlot2Coice();
		initSlot3Coice();
		initSlot4Coice();
		initSlot5Coice();
		modeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				playMode = newValue.intValue();
			}
		});
		modeChoiceBox.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard"));
		
		// Set array of RTCanvasGame with one item per sample track
		trackCanvases = new CanvasGame[TrackFactory.getSampleTrackCount()];
		ScrollPane trackPreviewWrapperWrapperWrapper = new ScrollPane();
		HBox trackPreviewWrapperWrapper = new HBox();
		trackPreviewWrapperWrapperWrapper.setContent(trackPreviewWrapperWrapper);
		trackPreviewWrapperWrapper.setPrefSize(200*trackCanvases.length, 200);
		trackPreviewWrapperWrapper.setMinSize(200*trackCanvases.length, 200);
		trackPreviewWrapperWrapperWrapper.setVbarPolicy(ScrollBarPolicy.NEVER);
		trackPreviewWrapperWrapperWrapper.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		trackHbox.getChildren().add(trackPreviewWrapperWrapperWrapper);

		// Setup sample tracks
		for (int i = 0; i < trackCanvases.length; i++) {
			StackPane trackPreviewWrapper = new StackPane();
			trackPreviewWrappers.put(trackPreviewWrapper, i);
			trackPreviewWrapper.setPrefSize(200, 200);
			trackPreviewWrapper.setMinSize(100, 200);

			// Generate and set up track preview
			CanvasGame trackPreview = new CanvasGame(TrackFactory.getSampleTrack(i), false);
			trackPreview.setDrawBackground(false);
			trackPreview.widthProperty().bind(trackPreviewWrapper.widthProperty());
			trackPreview.heightProperty().bind(trackPreviewWrapper.heightProperty());
			trackPreview.updateView();
			trackPreview.setTrackBackgroundColor(Colors.Track.grasBackground);
			trackPreviewWrapper.setTranslateX(i * trackPreview.widthProperty().doubleValue());

			trackPreviewWrapper.getChildren().add(trackPreview);
			trackPreviewWrapperWrapper.getChildren().add(trackPreviewWrapper);
			HBox.setHgrow(trackPreviewWrapperWrapper, Priority.ALWAYS);
			trackPreviewWrapperWrapper.setPadding(new Insets(10));
			

			trackHbox.setPadding(new Insets(10));
			//trackHbox.getChildren().add(trackPreviewWrapper);
			HBox.setHgrow(trackPreviewWrapper, Priority.ALWAYS);

			trackCanvases[i] = trackPreview;

			trackPreviewWrapper.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					/*System.out.println(event.getSource().toString());
					int newTrackId = (int) (event.getSceneX() / (gridBaseScenePane.getWidth() / trackCanvases.length));
					setSelectedTrack(newTrackId);*/
					setSelectedTrack(trackPreviewWrappers.get(event.getSource()));
				}
			});
		}
		
		// Setup only non empty track names
		lobbyNameText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boolean invalid = (newValue.isEmpty()   // If text is empty
						|| newValue.matches("(\\s)+") // If text only contains spaces
						|| !newValue.matches(".{1,15}") // If text has size between 1-15
						);
				createButton.setDisable(invalid);
			}
		});
		
		
	}
	
	@Override
	protected void sceneWillShow() {
		super.sceneWillShow();
		
		// Set medium play mode
		modeChoiceBox.getSelectionModel().select(1);
		
		// Show random track
		int randomTrackId = r.nextInt(TrackFactory.getSampleTrackCount());
		setSelectedTrack(randomTrackId);
		
		// Reset empty game name
		lobbyNameText.setText("New game");
		
		hasSendCreateMessageToServer = false;
	}

	// MARK: Button
	@FXML
	private void createNewGame(ActionEvent event)
	{
		if (!hasSendCreateMessageToServer)
		{
			hasSendCreateMessageToServer = true;
			sendNewGameToServer();
		}
	}

	@FXML
	public void goBack() {
		resetScene();
		stackPaneController.setScene(Racetracker.sceneLobbyList_name);
	}

	private int countPlayers()
	{
		int playersCount=0;
		for( int i=0 ; i<playerSlotsSettings.size() ; i++ )
		{
			// Free is not a player
			// Human is a player
			// Any AI is a player
			if( 0!=playerSlotsSettings.get( i ) )
				playersCount++;
		}
		return playersCount;
	}
	
	private String makeLobbyName()
	{
		return lobbyNameText.getText() == null || lobbyNameText.getText().isEmpty()
				|| lobbyNameText.getText().equals(" ") ? "Unnamed game" : lobbyNameText.getText();
	}
	
	private void sendNewGameToServer()
	{
		int playersCount=countPlayers();
		
		String[] names = new String[playersCount];
		
		// This is currently not used.
		boolean[] participating = new boolean[playersCount];
		Integer[] playerIDs = new Integer[playersCount];
		Integer[] typeIDs = new Integer[playersCount];
		
		names[0] = ModelExchange.GameOptions.getUserName();
		playerIDs[0] = ModelExchange.GameOptions.getPlayerID();
		typeIDs[0] = ModelExchange.GameOptions.getAiID(); // 1 => Human
		
		// First thought is `i<playersCount´, but we need to check every slot.
		for( int i=1, j=1; i<playerSlotsSettings.size(); i++ )
		{
			if( 0==playerSlotsSettings.get( i ) )
			{	// This is an empty slot, so we skip it.
				continue;
			}
			typeIDs[j]=playerSlotsSettings.get( i );
			names[j] = playerOptions.get(playerSlotsSettings.get( i ));
			
			// This stay's the way it got constructed.
			// playerIDs[j];
			// participating[j];
			
			// This should be next to i++; but then we would update it even if we continue
			j++;
		}			
		
		LobbyInformation lobbyInformation = new LobbyInformation(names, playerIDs, participating, typeIDs, selectedTrack,
				makeLobbyName(), false, 0, playMode);
		//lobbyInformation.setAIs(playerSlotsSettings);
		
		Racetracker.printInDebugMode("----- |CCM| ----- Send Create Lobby Message -----");
		ModelExchange.getController().sendCreateLobbyMessage(lobbyInformation);
	}

	// MARK: Helper function
	private void resetScene() {
		hasSendCreateMessageToServer = false;
	}
	
	private void setSelectedTrack(int trackId) {
		for (int i = 0; i < trackCanvases.length; i++) {
			trackCanvases[i].setDrawBackground(false);
			trackCanvases[i].updateView();
		}
		trackCanvases[trackId].setDrawBackground(true);
		trackCanvases[trackId].updateView();
		selectedTrack = trackId;
	}

	// MARK: ClientController
	@Override
	public void receivedOkayMessage() {
		super.receivedOkayMessage();

		/*
		 * // Redraws the lobby in lobbyController int currentLobbyId = ModelExchange.LobbyList.getCurrentLobbyId();
		 * ModelExchange.LobbyList.setCurrentLobbyId(currentLobbyId);
		 * Racetracker.printInDebugMode("Joined lobby, redirect to lobby scene: " + currentLobbyId);
		 */
	}

	/**
	 * Will be called if the user wants to create and join a new lobby
	 */
	@Override
	public void receivedUpdateFromServer() {
		super.receivedUpdateFromServer();
		Racetracker.printInDebugMode("----- |GUI| ----- Got updated LobbyList with new created Lobby -----");
		
		resetScene();
		stackPaneController.setScene(Racetracker.sceneLobby_name);
	}
	private void initSlot2Coice()
	{
		slot2ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>()
			{
				@Override
				public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value)
				{
					playerSlotsSettings.set( 1, new_value.intValue() );
				}
			});

		// Set player choice box items
		slot2ChoiceBox.setItems(
				FXCollections.observableArrayList( playerOptions ) );

		slot2ChoiceBox.getSelectionModel().select(0);
	}
	
	private void initSlot3Coice()
	{
		slot3ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>()
				{
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value)
					{
						playerSlotsSettings.set( 2, new_value.intValue() );
					}
				});

		// Set player choice box items
		slot3ChoiceBox.setItems(
					FXCollections.observableArrayList( playerOptions ) );

		slot3ChoiceBox.getSelectionModel().select(0);		
	}
	
	private void initSlot4Coice()
	{
		slot4ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>()
				{
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value)
					{
						playerSlotsSettings.set( 3, new_value.intValue() );
					}
				});

		// Set player choice box items
		slot4ChoiceBox.setItems(
					FXCollections.observableArrayList( playerOptions ) );

		slot4ChoiceBox.getSelectionModel().select(0);	
	}
	private void initSlot5Coice()
	{
		slot5ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>()
				{
					@Override
					public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value)
					{
						playerSlotsSettings.set( 4, new_value.intValue() );
					}
				});

		// Set player choice box items
		slot5ChoiceBox.setItems(
					FXCollections.observableArrayList( playerOptions ) );

		slot5ChoiceBox.getSelectionModel().select(0);
	}
}
