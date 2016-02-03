package src.main.java.gui.scenes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import src.main.java.gui.Racetracker;
import src.main.java.gui.canvas.CanvasGame;
import src.main.java.gui.canvas.CanvasPreview;
import src.main.java.gui.navigation.MultiSceneBase;
import src.main.java.gui.panes.GamePlayerPane;
import src.main.java.gui.utils.Colors;
import src.main.java.logic.ModelExchange;
import src.main.java.logic.Game;
import src.main.java.logic.Player;
import src.main.java.logic.IAI;
import src.main.java.logic.AI;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class GameController extends MultiSceneBase implements Initializable {
	@FXML
	private Pane gameAreaBasePane;
	@FXML
	private Pane gameAreaPreviewPane;
	@FXML
	private HBox gameMenue;
	@FXML
	private Pane movePreviewPane;
	@FXML
	private HBox playerBox;
	@FXML
	private StackPane movePreviewPaneWrapper;
	@FXML
	private Label currentActionLabel;
	@FXML
	private Label roundText;
	@FXML
	private Label speedText;
	@FXML
	private Label timeText;
	@FXML
	private StackPane infoActionLabelWrapper;

	@FXML
	private ImageView showHideInfoActionImage;
	@FXML
	private Label helpLabel;
	@FXML
	private Label closeLabel;
	
	private VBox matrixTool;

	public CanvasGame gameAreaCanvas;
	public CanvasPreview previewCanvas;

	private Game currentGame;
	private boolean userWantsToExitGame;
	private boolean waitingForServerAnswer;
	
	private Timer timer;
	
	private boolean hasPlayerCrashed;
	
	Player mSelf;

	// MARK: Initialization
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		Racetracker.printInDebugMode("----- |GUI| ----- Init GameController -----");
		loadHelpFileAtResource("/help/game/index.html");
		
		final Tooltip helpTooltip = new Tooltip();
		helpTooltip.setText("Update list of games");
		helpLabel.setTooltip(helpTooltip);
		
		final Tooltip exitTootlip = new Tooltip();
		exitTootlip.setText("Exit game");
		closeLabel.setTooltip(exitTootlip);
		
		previewCanvas = new CanvasPreview();
		previewCanvas.setOpacity(0.75);
		gameAreaPreviewPane.getChildren().add(previewCanvas);
		previewCanvas.widthProperty().bind(gameAreaBasePane.widthProperty());
		previewCanvas.heightProperty().bind(gameAreaBasePane.heightProperty());

		gameAreaCanvas = new CanvasGame();
		gameAreaCanvas.setDelegateController(this);

		gameAreaCanvas.cursorProperty().set(Cursor.CROSSHAIR);
		previewCanvas.cursorProperty().set(Cursor.CROSSHAIR);
		
		gameAreaPreviewPane.cursorProperty().set(Cursor.CROSSHAIR);
		gameAreaBasePane.cursorProperty().set(Cursor.CROSSHAIR);

		gameAreaBasePane.getChildren().add(gameAreaCanvas);
		gameAreaCanvas.widthProperty().bind(gameAreaBasePane.widthProperty());
		gameAreaCanvas.heightProperty().bind(gameAreaBasePane.heightProperty());
		gameAreaCanvas.setDrawBackground(true);
		
		gameAreaBasePane.widthProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable o) {
				previewCanvas.setTrackOffset(gameAreaCanvas.getTrackOffset());
				previewCanvas.setGridSquaresSize(gameAreaCanvas.getGridSquaresSize());
				previewCanvas.updateView();

				
				movePreviewPaneWrapper.setLayoutX((gameAreaBasePane.getWidth() / 2) - 75);
				
				drawPlayerTrails();
			}
		});
		
		gameAreaBasePane.heightProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable o) {
				previewCanvas.setTrackOffset(gameAreaCanvas.getTrackOffset());
				previewCanvas.setGridSquaresSize(gameAreaCanvas.getGridSquaresSize());
				previewCanvas.updateView();
				
				drawPlayerTrails();
			}
		});
		
		previewCanvas.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				previewCanvas.hightlightCurrentMousePosition(null);
			}
		});
		
		previewCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Point2D mousePosition = new Point2D(event.getX(), event.getY());
				Point2D gridPoint = convertWindowCoordsToGridPoints(mousePosition);

				performClickOnGameArea(gridPoint);
			}
		});
		
		previewCanvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				previewCanvas.setTrackOffset(gameAreaCanvas.getTrackOffset());
				previewCanvas.setGridSquaresSize(gameAreaCanvas.getGridSquaresSize());
				
				previewCanvas.setPlayerGridLocation(null);
				previewCanvas.setPlayerVelocityChange(null);

				Point2D mousePosition = new Point2D(event.getX(), event.getY());
				Point2D gridPoint = convertWindowCoordsToGridPoints(mousePosition);

				movedOverGameArea(gridPoint);
			}
		});
		
		previewCanvas.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				previewCanvas.hightlightCurrentMousePosition(null);
			}
		});
		
		ModelExchange.CurrentGame.gameIsSet.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if (newValue != null) {
					final boolean gameIsSet = newValue;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (gameIsSet) {
								setGame(ModelExchange.CurrentGame.getGame());
							} else if (userWantsToExitGame || currentGame == null) {
								leaveGameScene();
							} else {
								if (timer != null) {
									timer.cancel();
									timer = null;
									setTimeLabel(-Integer.MAX_VALUE);
								}
								
								Player winnerPlayer = ModelExchange.CurrentGame.getGame().getPlayerList()[ModelExchange.CurrentGame.getGame().getCurrentPlayerIndex()];
								String winnerName =  winnerPlayer.getName();
								
								Alert alert = new Alert(Alert.AlertType.INFORMATION);
								alert.setTitle("Game finished");
								
								if (winnerPlayer.getPlayerID().equals(ModelExchange.GameOptions.getPlayerID())) {
									// If I am the winner.
									alert.setHeaderText("You won! :)");
									alert.setContentText("Congratulation, you won the game!");
								} else {
									// If I am not the winner.
									alert.setHeaderText("You lose.");
									alert.setContentText("Player " + winnerName + " has won the game!");
								}
								
								try {
									Optional<ButtonType> result = alert.showAndWait();
									if (result.get() == ButtonType.OK) {
										leaveGameScene();
									} else {
										Racetracker.printInDebugMode("----- |INF| ----- Information box has unexpected exit -----");
									}
								} catch (Exception e) {
									Racetracker.printInDebugMode("----- |INF| ----- Information box has unexpected exit -----");
									leaveGameScene();
								}
							}
						}
					});
				}
				Racetracker.printInDebugMode("----- |GUI| ----- Game 'isSet' value has been set to " + newValue + " -----");
			}
		});
		
		ModelExchange.CurrentGame.currentPlayerTimeForMove.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (newValue != null) {
							if (timer == null) {
								if (newValue.intValue() > 0 && isCurrentPlayer()) {
									int time = newValue.intValue() + 1;
									timer = new Timer();
									ModelExchange.CurrentGame.currentPlayerTimeForMove.set(time);
									timer.scheduleAtFixedRate(new TimerTask() {
										public void run() {
											int nextTime = ModelExchange.CurrentGame.currentPlayerTimeForMove.get() - 1;
											ModelExchange.CurrentGame.currentPlayerTimeForMove.set(nextTime);
											setTimeLabel(nextTime);
										}
									}, 0, 1000);

								}
							} else if (newValue.intValue() == -Integer.MAX_VALUE) {
								if (timer != null) {
									timer.cancel();
								}
								
								setTimeLabel(-1);
								
								if (timer != null) {
									timer = null;
								}
							}
						}
					}
				});
			}
		});

		/*
		 * // Ues for early previews
		 * ModelExchange.LobbyList.lobbyId.addListener(new
		 * ChangeListener<Number>() {
		 * 
		 * @Override public void changed(ObservableValue<? extends Number> arg0,
		 * Number oldValue, Number newValue) {
		 * 
		 * final ILobbyInformation[] lobbies =
		 * ModelExchange.LobbyList.getLobbyInformationList(); int newLobby =
		 * newValue.intValue(); // ModelExchange.LobbyList.getCurrentLobbyId();
		 * 
		 * if (lobbies != null) { for (int i = 0; i < lobbies.length; i++) {
		 * final int index = i; if (lobbies[index].getLobbyID() == newLobby) {
		 * Platform.runLater(new Runnable() {
		 * 
		 * @Override public void run() {
		 * setILobbyInformationAsGame(lobbies[index]); } }); } } } } });
		 */

		setupMoveToolMatrix();
		userWantsToExitGame = false;
		waitingForServerAnswer = false;
		hasPlayerCrashed = false;

		timer = null;
	}
	

	private void setupMoveToolMatrix() {
		int matrixSize = 3;
		final int radius = 15;
		final int baseStrokeWidth = 4;
		final int hoverStrokeWidth = (int) (radius / 2);

		VBox matrixRowsVBox = new VBox();
		matrixRowsVBox.setAlignment(Pos.CENTER);
		matrixRowsVBox.setCursor(Cursor.HAND);

		for (int i = 0; i < matrixSize; i++) {
			HBox matrixColumnHBox = new HBox();
			matrixColumnHBox.setAlignment(Pos.CENTER);
			matrixColumnHBox.setCursor(Cursor.HAND);

			for (int j = 0; j < matrixSize; j++) {
				final int vy = -1 * (i - 1);
				final int vx = j - 1;

				StackPane circleWrapper = new StackPane();
				circleWrapper.setCursor(Cursor.HAND);
				circleWrapper.setPadding(new Insets(5));

				final Circle matrixCircle = new Circle();
				matrixCircle.setCursor(Cursor.HAND);
				matrixCircle.setFill(null);
				matrixCircle.setRadius(radius);
				matrixCircle.setStrokeWidth(baseStrokeWidth);
				matrixCircle.setStrokeType(StrokeType.INSIDE);
				matrixCircle.setStroke(Color.GRAY);

				circleWrapper.getChildren().add(matrixCircle);

				circleWrapper.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final Timeline timeline = new Timeline();
						timeline.setAutoReverse(false);
						final KeyValue keyValue = new KeyValue(matrixCircle.strokeWidthProperty(), hoverStrokeWidth);
						final KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
						timeline.getKeyFrames().add(keyFrame);
						timeline.play();

						if (shouldShowMovePreview()) {
							Point2D velocityChangeVector = new Point2D(vx, vy);
							movedOverMatrixCircle(velocityChangeVector);
						}
						// matrixCircle.setStrokeWidth(hoverStrokeWidth);
					}
				});

				circleWrapper.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final Timeline timeline = new Timeline();
						timeline.setAutoReverse(false);
						final KeyValue keyValue = new KeyValue(matrixCircle.strokeWidthProperty(), baseStrokeWidth);
						final KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
						timeline.getKeyFrames().add(keyFrame);
						timeline.play();

						// matrixCircle.setStrokeWidth(baseStrokeWidth);
					}
				});

				circleWrapper.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						final Timeline strokeTimeline = new Timeline();
						strokeTimeline.setAutoReverse(false);
						final KeyValue keyValue = new KeyValue(matrixCircle.strokeWidthProperty(), radius);
						final KeyFrame keyFrame = new KeyFrame(Duration.millis(50), keyValue);
						strokeTimeline.getKeyFrames().add(keyFrame);

						// final Timeline fillTimeline = new Timeline();
						// fillTimeline.setAutoReverse(false);
						// final KeyValue keyValueFill = new
						// KeyValue(matrixCircle.strokeProperty(),
						// Color.BLUEVIOLET);
						// final KeyFrame keyFrameFill = new
						// KeyFrame(Duration.millis(75), keyValueFill);
						// fillTimeline.getKeyFrames().add(keyFrameFill);

						strokeTimeline.play();
						// fillTimeline.play();

						if (shouldShowMovePreview()) {
							Point2D velocityChangeVector = new Point2D(vx, vy);
							performClickOnMatrixCircle(velocityChangeVector);
						}

						// matrixCircle.setStrokeWidth(radius);
					}
				});

				matrixColumnHBox.getChildren().add(circleWrapper);
			}

			matrixRowsVBox.getChildren().add(matrixColumnHBox);
		}

		matrixTool = matrixRowsVBox;
		matrixTool.setCursor(Cursor.HAND);
		movePreviewPane.getChildren().add(matrixRowsVBox);
		movePreviewPane.setCursor(Cursor.HAND);
	}

	// MARK: Buttons
	@FXML
	private void exitCurrentGame(MouseEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Exit game");
		alert.setHeaderText("Exit game?");
		alert.setContentText(
				"Are you sure you want to end this amazing game? By pressing the 'OK' button you will get to the main game menu.");

		try {
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				userWantsToExitGame = true;
				Racetracker.printInDebugMode("----- |CCM| ----- Send leave lobby message -----");
				ModelExchange.getController().sendLeaveLobbyMessage();
			} else {
				Racetracker.printInDebugMode("----- |INF| ----- Exit current game had been canceled -----");
			}
		} catch (Exception e) {
			userWantsToExitGame = true;
			leaveGameScene();
		}
	}
	
	@FXML private void showHideInfoActionWrapper() {
		infoActionLabelWrapper.setVisible(!infoActionLabelWrapper.isVisible());
	}

	// MARK: Setter
	private void setGame(final Game game)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{				
				currentGame = game;
				Game modelGame = ModelExchange.CurrentGame.getGame();
				
				if (game != null && modelGame != null)
				{
					// Set track
					gameAreaCanvas.setTrack(game.getTrack());
					previewCanvas.resetView();
					previewCanvas.setTrackOffset(gameAreaCanvas.getTrackOffset());
					previewCanvas.setGridSquaresSize(gameAreaCanvas.getGridSquaresSize());
					previewCanvas.updateView();
					
					previewCanvas.setPlayerVelocityChange(null);
					// Delete current player
					playerBox.getChildren().removeAll(playerBox.getChildren());
	
					Integer currentGamePlayerId = game.getCurrentPlayerIndex();
					Integer userId = ModelExchange.GameOptions.getPlayerID();
					
					for( int i=0 ; i<game.getPlayers().length ; i++ )
					{
						Player gamePlayer = game.getPlayers()[i];
						if( gamePlayer.getPlayerID()!=null )
						{
							if( gamePlayer.getPlayerID().equals( userId ) )
							{
								hasPlayerCrashed=gamePlayer.hasCrashed();
								
								if( gamePlayer.getCurrentVelocity()!=null )
								{
									// Set current velocity
									setCurrentVelocityLabel( gamePlayer.getCurrentVelocity() );
								}
							}
						}
					}
					
					
					
					// Set player
					for( int i=0 ; i<game.getPlayers().length ; i++ )
					{
						Player gamePlayer = game.getPlayers()[i];
						// If player is not null -> is set
						if( gamePlayer.getPlayerID()!=null )
						{
							Integer gamePlayerId = gamePlayer.getPlayerID();
							String gamePlayerName = gamePlayer.getName();
	
							GamePlayerPane newPlayerPane = new GamePlayerPane();
							
							if( gamePlayerId!=null )
							{
								newPlayerPane.setName(gamePlayerName);
								
								drawPlayerTrails();
								if (gamePlayer.hasCrashed() || gamePlayer.isDisconnected())
								{
									newPlayerPane.setColor(Colors.Player.inactive);
								}
								else
								{
									newPlayerPane.setColor(Colors.Player.getColorWithId(i));
								}
							} 
							else
							{
								newPlayerPane.setName(".....");
							}
							
							// Color Move tool
							Color desaturatedColor = Colors.Player.getColorWithId(i).desaturate().desaturate().desaturate();
							if (gamePlayerId.equals(userId)) {
								for (int h = 0; h < matrixTool.getChildren().size(); h++) {
									HBox box = ((HBox) matrixTool.getChildren().get(h));
									for (int v = 0; v < box.getChildren().size() ; v++) {
										StackPane stackPane = (StackPane) (box.getChildren().get(v));
										Circle circle = (Circle) (stackPane.getChildren().get(0));
										circle.setStroke(desaturatedColor);
									}
								}
							}
							
							
							
							if( i==currentGamePlayerId )
							{
								// Highlight current set Player
								Racetracker.printInDebugMode("----- |INF| ----- Current player: " + i + " -----");
								newPlayerPane.setFillBorderStrokeWidth();

								
								if ( gamePlayerId.equals( userId ) )
								{
									String action;
									boolean firstGameMove = isFirstGameMove();
									if( currentGame.getPlayers()[i].isAI() )
									{
										((AI)currentGame.getPlayers()[i]).setGameInformation( currentGame );
										if( firstGameMove )
										{
											if( matrixTool!=null )
												matrixTool.setDisable( false );
											action = "Select one of the gold highlighed starting points!";
											setGameActionLabel( action );
										}
										else
										{
											if( matrixTool!=null )
												matrixTool.setDisable( true );
											action = gamePlayerName + " will now calculate the next move.";
											setGameActionLabel( action );

											mSelf=currentGame.getPlayers()[i];
											moveAI();
										}
									}
									else
									{
										// If 'i am' the current player
										if( matrixTool!=null )
										{
											matrixTool.setDisable( false );
										}
		
										if( firstGameMove )
										{
											action = "Select one of the gold highlighed starting points!";
										}
										else
										{
											action = gamePlayerName + ": Choose your next move.";
										}
										setGameActionLabel( action );
										
										
										if( shouldShowMovePreview() ) 
										{
											previewCanvas.setPlayerColor(desaturatedColor);
											if( !firstGameMove )
											{
												previewCanvas.setPlayerGridLocation(getCurrentUserLocation());
												previewCanvas.setPlayerVelocity(getCurrentUserVelocity());
											}
											previewCanvas.updateView();
										}										
									}
								} 
								else
								{
									// If someone else is the player
									if (matrixTool != null)
									{
										matrixTool.setDisable(true);
									}
	
									String action = "Please wait until " + gamePlayerName + " selects his/her position.";
									setGameActionLabel(action);
								}
							}
							
							if (gamePlayerId != null) {
								playerBox.getChildren().add(newPlayerPane);
								HBox.setHgrow(newPlayerPane, Priority.SOMETIMES);
							}
						}
	
						roundText.setText(Integer.toString(game.getRoundCounter()));
						timeText.setText("--");
					}
				}
			}
		});
	}
	
	private void drawPlayerTrails() {
		if (currentGame != null) {
			Integer userId = ModelExchange.GameOptions.getPlayerID();
			
			for (int i = 0; i < currentGame.getPlayers().length; i++) {
				if (currentGame.getPlayers()[i].getPlayerID() != null) {
					Player player = currentGame.getPlayers()[i];
					
					Color playerColor = Colors.Player.getColorWithId(i);
					if (player.isDisconnected()) {
						playerColor = Colors.Player.inactive;
					}

					ArrayList<Point2D> memorizedGridPointsList = player.getMemorizeGridPoints();

					Point2D[] memorizedGridPoints = (Point2D[]) memorizedGridPointsList.toArray(new Point2D[memorizedGridPointsList.size()]);
					if (memorizedGridPoints != null) {
						gameAreaCanvas.drawPlayerTrail(playerColor, memorizedGridPoints, player.hasCrashed(), player.getPlayerID().equals(userId));
					}
				}
			}
		}
	}

	// MARK: Logic
	private Point2D convertWindowCoordsToGridPoints(Point2D point) {
		double squareSize = gameAreaCanvas.getGridSquaresSize();

		Point2D canvasSize = new Point2D((int) (gameAreaCanvas.getWidth() / squareSize),
				(int) (gameAreaCanvas.getHeight() / squareSize));
		Point2D mousePosition = new Point2D((int) Math.round(point.getX() / squareSize),
				canvasSize.getY() - (int) Math.round(point.getY() / squareSize));
		Point2D offset = gameAreaCanvas.getTrackOffset();

		return mousePosition.subtract(offset);
	}

	// MARK: Event functions
	private void performClickOnGameArea(Point2D gridPointLocation) {
		if (shouldShowMovePreview()) {
			previewCanvas.setTrackOffset(gameAreaCanvas.getTrackOffset());
			previewCanvas.setGridSquaresSize(gameAreaCanvas.getGridSquaresSize());
	
			sendMoveToServer(gridPointLocation);
			previewCanvas.drawPoint(gridPointLocation);
		}
	}
	
	private void movedOverGameArea(Point2D gridPointLocation) {
		previewCanvas.setTrackOffset(gameAreaCanvas.getTrackOffset());
		previewCanvas.setGridSquaresSize(gameAreaCanvas.getGridSquaresSize());
		
		if (shouldShowMovePreview()) {
			if (!isFirstGameMove()) {
				Point2D userLocation = getCurrentUserLocation();
				Point2D userVelocity = getCurrentUserVelocity();
				previewCanvas.setPlayerGridLocation(userLocation);
				previewCanvas.setPlayerVelocity(userVelocity);
				
				previewCanvas.setPlayerVelocityChange(gridPointLocation.subtract(userLocation).subtract(userVelocity));
			}
		}
		
		previewCanvas.hightlightCurrentMousePosition(gridPointLocation);
	}

	private void performClickOnMatrixCircle(Point2D velocityChangeVector) {
		if (shouldShowMovePreview()) {
			Point2D currentUserLocation = getCurrentUserLocation();
			Point2D currentUserVelocity = getCurrentUserVelocity();
	
			if (currentUserLocation != null && currentUserVelocity != null) {
				if (isFirstGameMove()) {
					Point2D[] startingPoints = this.currentGame.getTrack().getStartingPoints();
					int velocityChangeVectorIndex = getVelocityChangeVectorIndex(velocityChangeVector);
					if (velocityChangeVectorIndex == -1) {
						velocityChangeVectorIndex = 0;
					} else {
						velocityChangeVectorIndex = velocityChangeVectorIndex % startingPoints.length;
					}
					
					sendMoveToServer(startingPoints[velocityChangeVectorIndex]);
					Racetracker.printInDebugMode("Set start location to: " + startingPoints[velocityChangeVectorIndex]);
					
				} else {
					Point2D newUserLocation = currentUserLocation.add(velocityChangeVector).add(currentUserVelocity);
		
					previewCanvas.setPlayerGridLocation(newUserLocation);
					previewCanvas.setPlayerVelocity(currentUserVelocity);
		
					sendMoveToServer(newUserLocation);
					Racetracker.printInDebugMode("Change location to: " + newUserLocation);
				}
			}
		}
	}

	private void movedOverMatrixCircle(Point2D velocityChangeVector) {
		Point2D currentUserLocation = getCurrentUserLocation();
		previewCanvas.setTrackOffset(gameAreaCanvas.getTrackOffset());
		previewCanvas.setGridSquaresSize(gameAreaCanvas.getGridSquaresSize());
		
		if (currentUserLocation != null) {
			if (isFirstGameMove()) {
				Point2D[] startingPoints = this.currentGame.getTrack().getStartingPoints();
				int velocityChangeVectorIndex = getVelocityChangeVectorIndex(velocityChangeVector);
				if (velocityChangeVectorIndex == -1) {
					velocityChangeVectorIndex = 0;
				} else {
					velocityChangeVectorIndex = velocityChangeVectorIndex % startingPoints.length;
				}
				
				Point2D newUserLocation = startingPoints[velocityChangeVectorIndex];
				
				previewCanvas.highlightStartingPoint(startingPoints, newUserLocation);
				Racetracker.printInDebugMode("Preview start location: " + newUserLocation);
				
			} else {
				Point2D newUserLocation = currentUserLocation.add(velocityChangeVector);
				previewCanvas.setPlayerGridLocation(getCurrentUserLocation());
				previewCanvas.setPlayerVelocity(getCurrentUserVelocity());
				previewCanvas.setPlayerVelocityChange(velocityChangeVector);
				Racetracker.printInDebugMode("Preview location: " + newUserLocation);
			}
		}
	}
	
	// MARK: Helper functions
	private boolean shouldShowMovePreview() {
		if (currentGame != null) {
			if (!waitingForServerAnswer) {
				Integer currentGamePlayerId = currentGame.getPlayerList()[currentGame.getCurrentPlayerIndex()].getPlayerID();
				Integer userId = ModelExchange.GameOptions.getPlayerID();
				if (currentGamePlayerId.equals(userId)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private int getVelocityChangeVectorIndex(Point2D velocityChangeVector) {
		if (velocityChangeVector.equals(new Point2D(-1,1))) {
			return 0;
		} else if (velocityChangeVector.equals(new Point2D(0,1))) {
			return 1;
		} else if (velocityChangeVector.equals(new Point2D(1,1))) {
			return 2;
		} else if (velocityChangeVector.equals(new Point2D(-1,0))) {
			return 3;
		} else if (velocityChangeVector.equals(new Point2D(0,0))) {
			return 4;
		} else if (velocityChangeVector.equals(new Point2D(1,0))) {
			return 5;
		} else if (velocityChangeVector.equals(new Point2D(-1,-1))) {
			return 6;
		} else if (velocityChangeVector.equals(new Point2D(0,-1))) {
			return 7;
		} else if (velocityChangeVector.equals(new Point2D(1,-1))) {
			return 8;
		}
		
		return -1;
	}
	
	// MARK: Logic	
	private boolean isFirstGameMove() {
		if (currentGame != null) {
			if (currentGame.getRoundCounter() > 0  || 
					currentGame.getPlayers()[currentGame.getCurrentPlayerIndex()].getMemorizeGridPoints().size() > 0) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isCurrentPlayer() {
		if (currentGame != null) {
			Integer currentGamePlayerId = currentGame.getPlayerList()[currentGame.getCurrentPlayerIndex()].getPlayerID();
			Integer userId = ModelExchange.GameOptions.getPlayerID();
			if (currentGamePlayerId.equals(userId)) {
				return true;
			}
		}
		return false;
	}
	
	private Point2D getCurrentUserLocation() {
		Game game = ModelExchange.CurrentGame.getGame();
		if (game != null) {
			int currentPlayerId = game.getCurrentPlayerIndex(); // ModelExchange.GameOptions.getPlayerID();// 
			Player player = game.getPlayers()[currentPlayerId];
			Point2D playerLocation = player.getCurrentPosition();
			ArrayList<Point2D> memGridPoints = player.getMemorizeGridPoints();

			Point2D currentPosition = null;

			if (playerLocation != null) {
				currentPosition = playerLocation;
			} else if (memGridPoints.size() == 0) {
				// If this is first round
				Point2D[] startingPoints = game.getTrack().getStartingPoints();
				currentPosition = startingPoints[startingPoints.length / 2];
			} else if (memGridPoints.size() > 0) {
				// Fall back...
				currentPosition = memGridPoints.get(memGridPoints.size() - 1);
			}
			
			return currentPosition;
		} else {
			return null;
		}
	}

	private Point2D getCurrentUserVelocity() {
		Game game = ModelExchange.CurrentGame.getGame();
		if (game != null) {
			int currentPlayerId = game.getCurrentPlayerIndex();
			Player player = game.getPlayers()[currentPlayerId];
			Point2D playerVelocity = player.getCurrentVelocity();
			ArrayList<Point2D> memGridPoints = player.getMemorizeGridPoints();

			Point2D currentVelocity = null;
			
			if (playerVelocity != null) {
				currentVelocity = playerVelocity;
			} else if (memGridPoints.size() == 1 || memGridPoints.size() == 0) {
				currentVelocity = new Point2D(0, 0);
			} else if (memGridPoints.size() > 1) {
				Point2D lastPosition = player.getMemorizeGridPoints().get(player.getMemorizeGridPoints().size() - 2);
				Point2D currentPosition = player.getMemorizeGridPoints().get(player.getMemorizeGridPoints().size() - 1);
				currentVelocity = currentPosition.subtract(lastPosition);
			}
			return currentVelocity;
		} else {
			return new Point2D(0, 0);
		}
	}

	// MARK: ClientController
	private void sendMoveToServer(Point2D newGridLocation) {
		waitingForServerAnswer = true;
		
		matrixTool.setDisable(true);
		previewCanvas.setPlayerGridLocation(null);
		previewCanvas.updateView();
		
		setGameActionLabel("Waiting for server response...");
		
		Racetracker.printInDebugMode("----- |CCM| ----- Send vector message -----");
		ModelExchange.getController().sendVectorMessage(newGridLocation);
	}

	@Override
	public void receivedOkayMessage() {
		super.receivedOkayMessage();
		setModel();
	}

	@Override
	public void receivedUpdateFromServer() {
		super.receivedUpdateFromServer();
		
		setModel();
		
		if (!isCurrentPlayer())
		{
			if (timer != null)
			{
				timer.cancel();
				timer = null;
				setTimeLabel(-Integer.MAX_VALUE);
			}
		}
		
		if (userWantsToExitGame)
		{
			leaveGameScene();
		}
	}
	
	private void moveAI() 
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run() 
			{
				if( null==mSelf )
					return;
				long start_time=System.currentTimeMillis();

				javafx.geometry.Point2D newGridLocation = ((IAI)mSelf).move();
				
				long time_needed=System.currentTimeMillis()-start_time;
				if( time_needed < 1500 )
				{
					try
					{
						synchronized(this)
						{
							this.wait( 1500-time_needed );
						}
					} 
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
				sendMoveToServer( newGridLocation );
			}
		});
	}
	
	
	
	private void leaveGameScene() {
		if (ModelExchange.GameOptions.isShouldReturnToSetup()) {
			Racetracker.printInDebugMode("----- |GUI| ----- Leave game scene -> SetUp -----");
			stackPaneController.setScene(Racetracker.sceneSetup_name);
		} else {
			Racetracker.printInDebugMode("----- |GUI| ----- Leave game scene -> List of game -----");
			stackPaneController.setScene(Racetracker.sceneLobbyList_name);
		}
		resetGameScene();
	}
	
	private void resetGameScene() {
		// Reset game
		currentGame = null;
		ModelExchange.CurrentGame.gameIsSet.set(false);
		ModelExchange.CurrentGame.setGame(null);
		// Reset Lobby
		ModelExchange.LobbyList.setCurrentLobbyId(-1);
		
		previewCanvas.resetView();
		
		waitingForServerAnswer = false;
		setGameActionLabel("---");
		setCurrentVelocityLabel(new Point2D(0,0));

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		
		userWantsToExitGame = false;
		hasPlayerCrashed = false;
	}
	
	@Override
	public void receivedInvalidMoveMessage() {
		super.receivedInvalidMoveMessage();
		Racetracker.printInDebugMode("----- |GUI| ----- Received invalid move message -----");
		if (isFirstGameMove()) {
			setGameActionLabel("The position you chose is not valid. Please try again.");
		} else {
			setGameActionLabel("The move you made is not valid. Please try again.");
		}
		waitingForServerAnswer = false;
		matrixTool.setDisable(false);
	}

	private void setModel() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				waitingForServerAnswer = false;
				if (ModelExchange.CurrentGame.getGame() != null) {
					Racetracker.printInDebugMode("----- |GUI| ----- Set Model -----");
					setGame(ModelExchange.CurrentGame.getGame());
				}
			}
		});
	}
	
	
	// MARK: Set label	
	private void setGameActionLabel(final String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (hasPlayerCrashed) {
					currentActionLabel.setText("You crashed...");
				} else {
					currentActionLabel.setText(text);
				}
			}
		});
	}
	
	private void setCurrentVelocityLabel(final Point2D point) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				int x = (int) point.getX();
				int y = (int) point.getY();
				speedText.setText(Integer.toString(x) + "|" + Integer.toString(y));
			}
		});
	}
	
	private void setTimeLabel(final int time) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (time < 0) {
					timeText.setText("--");
				} else {
					timeText.setText(String.valueOf(time));
				}
				
			}
		});
	}
}
