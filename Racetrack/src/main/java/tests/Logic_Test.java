/*package src.main.java.tests;

import javafx.geometry.Point2D;
import src.main.java.logic.Game;
import src.main.java.logic.Lobby;
import src.main.java.logic.Player;
import src.main.java.logic.TrackFactory;

public class Logic_Test {

	public static void main(String[] args) {
		int whichScenario = 6;

		if (whichScenario == 0) {
			// initiate important stuff for the test
			Player player1 = new Player(1);
			Player player2 = new Player(2);
			Player[] playerList = { player1, player2 };
			Lobby testLobby = new Lobby(1, 2, "testLobby", 0);
			Game testGame = new Game(TrackFactory.getSampleTrack(testLobby
					.getTrackId()), playerList, testLobby);
			Point2D firstMovePlayer2 = new Point2D(3, 3);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			// check if every player is participating
			System.out.println("Player 1 participating: "
					+ player1.isParticipating() + ", Player 2 participating: "
					+ player2.isParticipating());
			// check the method isValid(firstRound, valid move)
			System.out.println("Is the first move of Player 2 valid?: "
					+ testGame.isValid(player2, firstMovePlayer2));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 2 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player2,
									firstMovePlayer2));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if player is set in grid
			System.out.println("Is Player 2 on the grid?: "
					+ testGame.getRTField()[3][3]);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			// check the method isValid(firstRound, not valid move)
			System.out
					.println("Player 1 wants to start at the same point as Player 2, correct?: "
							+ testGame.isValid(player1, firstMovePlayer2));
			Point2D firstMovePlayer1 = new Point2D(4, 3);
			// check the method isValid(firstRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, firstMovePlayer1));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									firstMovePlayer1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if player is set in grid
			System.out.println("Is Player 1 where he must be on the grid?: "
					+ testGame.getRTField()[4][2]);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove1Player1 = new Point2D(4, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove1Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove1Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D failMove1Player2 = new Point2D(2, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 2 chooses valid move: "
					+ testGame.isValid(player2, failMove1Player2));
			// check method getCollisionPoint(normalRound, crash with boundary)
			System.out
					.println("Move Player 2 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player2,
									failMove1Player2));
			// check method hasPlayerWon(normalRound, wrong move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			Point2D normalMove2Player1 = new Point2D(5, 5);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove2Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove2Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove3Player1 = new Point2D(5, 8);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove3Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove3Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove4Player1 = new Point2D(6, 11);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove4Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove4Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove5Player1 = new Point2D(8, 13);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove5Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove5Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove6Player1 = new Point2D(9, 14);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove6Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove6Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove7Player1 = new Point2D(11, 14);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove7Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove7Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove8Player1 = new Point2D(14, 14);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove8Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove8Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove9Player1 = new Point2D(18, 14);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove9Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove9Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove10Player1 = new Point2D(21, 13);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove10Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove10Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove11Player1 = new Point2D(23, 12);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove11Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove11Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove12Player1 = new Point2D(24, 10);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove12Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove12Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove13Player1 = new Point2D(24, 7);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove13Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove13Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove14Player1 = new Point2D(24, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove14Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove14Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove15Player1 = new Point2D(24, 0);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove15Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove15Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

		}
		if (whichScenario == 1) {
			// initiate important stuff for the test
			Player player1 = new Player(1);
			Player player2 = new Player(2);
			Player[] playerList = { player1, player2 };
			Lobby testLobby = new Lobby(1, 2, "testLobby", 0);
			Game testGame = new Game(TrackFactory.getSampleTrack(testLobby
					.getTrackId()), playerList, testLobby);
			Point2D firstMovePlayer2 = new Point2D(3, 2);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			// check if every player is participating
			System.out.println("Player 1 participating: "
					+ player1.isParticipating() + ", Player 2 participating: "
					+ player2.isParticipating());
			// check the method isValid(firstRound, valid move)
			System.out.println("Is the first move of Player 2 valid?: "
					+ testGame.isValid(player2, firstMovePlayer2));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 2 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player2,
									firstMovePlayer2));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if player is set in grid
			System.out.println("Is Player 2 on the grid?: "
					+ testGame.getRTField()[3][2]);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D firstMovePlayer1 = new Point2D(7, 2);
			// check the method isValid(firstRound, valid move)
			System.out.println("Is the first move of Player 1 valid?: "
					+ testGame.isValid(player1, firstMovePlayer1));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									firstMovePlayer1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check if player is set in grid
			System.out.println("Is Player 1 on the grid?: "
					+ testGame.getRTField()[7][2]);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D failMove1Player1 = new Point2D(8, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, failMove1Player1));
			// check method getCollisionPoint(normalRound, fail move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									failMove1Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
			// check method isNextPlayerLastPlayer
			System.out
					.println("Is the next player the last player on the grid?: "
							+ testGame.isNextPlayerLastPlayer());
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D failMove1Player2 = new Point2D(2, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 2 chooses valid move: "
					+ testGame.isValid(player2, failMove1Player2));
			// check method getCollisionPoint(normalRound, crash with boundary)
			System.out
					.println("Move Player 2 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player2,
									failMove1Player2));
			// check method hasPlayerWon(normalRound, wrong move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
		}

		if (whichScenario == 2) {
			// initiate important stuff for the test
			System.out
					.println("game with only 1 player to test if starting points are being compared correctly");
			Player player1 = new Player(1);
			Player[] playerList = { player1 };
			Lobby testLobby = new Lobby(1, 2, "testLobby", 0);
			Game testGame = new Game(TrackFactory.getSampleTrack(testLobby
					.getTrackId()), playerList, testLobby);

			System.out.println("Starting points: ");
			for (int i = 0; i < TrackFactory.getSampleTrack(0)
					.getStartingPoints().length; i++)
				System.out.println(TrackFactory.getSampleTrack(0)
						.getStartingPoints()[i]);

			// select random point on grid to test if it is on the starting
			// points or not
			Point2D firstMove1Player1 = new Point2D(8, 3);
			System.out.println("random player move with point: "
					+ firstMove1Player1
					+ " which is also the boundary of the track");
			System.out
					.println("Player 1 chooses wrong gridpoint as starting point, should be false: "
							+ testGame.isValid(player1, firstMove1Player1));

			// select random point on grid to test if it is on the starting
			// points or not
			Point2D firstMove2Player1 = new Point2D(11, 3);
			System.out.println("random player move with point: "
					+ firstMove2Player1
					+ " which is just a pointless point on the grid");
			System.out
					.println("Player 1 chooses wrong gridpoint as starting point, should be false: "
							+ testGame.isValid(player1, firstMove2Player1));
		}

		if (whichScenario == 3) {
			// initiate important stuff for the test
			System.out
					.println("game with 5 players to see if counter works also with more than 2 or 3 players");
			Player player1 = new Player(1);
			Player player2 = new Player(2);
			Player player3 = new Player(3);
			Player player4 = new Player(4);
			Player player5 = new Player(5);
			Player[] playerList = { player1, player2, player3, player4, player5 };
			Lobby testLobby = new Lobby(1, 5, "testLobby", 0);
			Game testGame = new Game(TrackFactory.getSampleTrack(testLobby
					.getTrackId()), playerList, testLobby);

			System.out.println("\n\n\n");
			// FIRST ROUND START
			System.out.println("FIRST ROUND START \n\n\n");

			Point2D firstMovePlayer5 = new Point2D(3, 3);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			// check if every player is participating
			System.out.println("Player 1 participating: "
					+ player1.isParticipating() + ", Player 2 participating: "
					+ player2.isParticipating() + ", Player 3 participating: "
					+ player3.isParticipating() + ", Player 4 participating: "
					+ player4.isParticipating() + ", Player 5 participating: "
					+ player5.isParticipating());
			// check the method isValid(firstRound, valid move)
			System.out.println("Is the first move of Player 5 valid?: "
					+ testGame.isValid(player5, firstMovePlayer5));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 5 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player5,
									firstMovePlayer5));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player5));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D firstMovePlayer4 = new Point2D(4, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 4 chooses correct move: "
					+ testGame.isValid(player4, firstMovePlayer4));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 4 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player4,
									firstMovePlayer4));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player4));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D firstMovePlayer3 = new Point2D(5, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 3 chooses correct move: "
					+ testGame.isValid(player3, firstMovePlayer3));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 3 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player3,
									firstMovePlayer3));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 3 won the game?: "
					+ testGame.hasPlayerWon(player3));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D firstMovePlayer2 = new Point2D(6, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 2 chooses correct move: "
					+ testGame.isValid(player2, firstMovePlayer2));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 2 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player2,
									firstMovePlayer2));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D firstMovePlayer1 = new Point2D(7, 3);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, firstMovePlayer1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									firstMovePlayer1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player5));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			System.out.println("\n\n\n");
			// NORMAL ROUND START
			System.out.println("NORMAL ROUND START \n\n\n");

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove1Player1 = new Point2D(7, 4);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove1Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove1Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove1Player2 = new Point2D(6, 4);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 2 chooses correct move: "
					+ testGame.isValid(player2, normalMove1Player2));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 2 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player2,
									normalMove1Player2));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove1Player3 = new Point2D(5, 4);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 3 chooses correct move: "
					+ testGame.isValid(player3, normalMove1Player3));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 3 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player3,
									normalMove1Player3));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 3 won the game?: "
					+ testGame.hasPlayerWon(player3));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove1Player4 = new Point2D(4, 4);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 4 chooses correct move: "
					+ testGame.isValid(player4, normalMove1Player4));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 4 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player4,
									normalMove1Player4));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player4));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove1Player5 = new Point2D(3, 4);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			// check the method isValid(normal round, valid move)
			System.out.println("Is the move of Player 5 valid?: "
					+ testGame.isValid(player5, normalMove1Player5));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Set Player 5 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player5,
									normalMove1Player5));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player5));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			System.out.println("\n\n\n");
			// 2nd ROUND
			System.out.println("2nd ROUND \n\n\n");

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove2Player1 = new Point2D(8, 5);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 1 chooses correct move: "
					+ testGame.isValid(player1, normalMove2Player1));
			// check method getCollisionPoint(normalRound, collision on
			// boundary)
			System.out
					.println("Move Player 1 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove2Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove2Player2 = new Point2D(6, 5);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 2 chooses correct move: "
					+ testGame.isValid(player2, normalMove2Player2));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 2 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player2,
									normalMove2Player2));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove2Player3 = new Point2D(5, 5);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 3 chooses correct move: "
					+ testGame.isValid(player3, normalMove2Player3));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 3 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player3,
									normalMove2Player3));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 3 won the game?: "
					+ testGame.hasPlayerWon(player3));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove2Player4 = new Point2D(4, 5);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 4 chooses correct move: "
					+ testGame.isValid(player4, normalMove2Player4));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 4 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player4,
									normalMove2Player4));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player4));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove2Player5 = new Point2D(3, 5);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			// check the method isValid(normal round, valid move)
			System.out.println("Is the move of Player 5 valid?: "
					+ testGame.isValid(player5, normalMove2Player5));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Set Player 5 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player5,
									normalMove2Player5));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player5));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			System.out.println("\n\n\n");
			// 3rd ROUND
			System.out.println("3rd ROUND \n\n\n");

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove3Player2 = new Point2D(6, 6);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 2 chooses correct move: "
					+ testGame.isValid(player2, normalMove3Player2));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 2 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player2,
									normalMove3Player2));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 2 won the game?: "
					+ testGame.hasPlayerWon(player2));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove3Player3 = new Point2D(5, 6);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 3 chooses correct move: "
					+ testGame.isValid(player3, normalMove3Player3));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 3 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player3,
									normalMove3Player3));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 3 won the game?: "
					+ testGame.hasPlayerWon(player3));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			Point2D normalMove3Player4 = new Point2D(4, 6);
			// check the method isValid(normalRound, valid move)
			System.out.println("Player 4 chooses correct move: "
					+ testGame.isValid(player4, normalMove3Player4));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Move Player 4 on the grid and is there collision?: "
							+ testGame.getCollisionPoint(player4,
									normalMove3Player4));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player4));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove3Player5 = new Point2D(3, 6);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());
			// check the method isValid(normal round, valid move)
			System.out.println("Is the move of Player 5 valid?: "
					+ testGame.isValid(player5, normalMove3Player5));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Set Player 5 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player5,
									normalMove3Player5));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 5 won the game?: "
					+ testGame.hasPlayerWon(player5));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
		}

		if (whichScenario == 4) {

			System.out
					.println("test if the check of velocity change is working right: \n");
			// initiate important stuff for the test
			Player player1 = new Player(1);
			Player[] playerList = { player1 };
			Lobby testLobby = new Lobby(1, 1, "testLobby", 0);
			Game testGame = new Game(TrackFactory.getSampleTrack(testLobby
					.getTrackId()), playerList, testLobby);

			Point2D firstMovePlayer1 = new Point2D(3, 3);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(firstRound, valid move)
			System.out.println("Is the first move" + firstMovePlayer1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, firstMovePlayer1));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									firstMovePlayer1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMovePlayer1 = new Point2D(3, 6);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(firstRound, valid move)
			System.out
					.println("Given point: "
							+ normalMovePlayer1
							+ " should be invalid (because velocity change too high) and should lead to false: "
							+ testGame.isValid(player1, normalMovePlayer1));
		}

		if (whichScenario == 5) {
			System.out.println("test of a normal crash scenario: \n");
			// initiate important stuff for the test
			Player player1 = new Player(1);
			Player[] playerList = { player1 };
			Lobby testLobby = new Lobby(1, 1, "testLobby", 0);
			Game testGame = new Game(TrackFactory.getSampleTrack(testLobby
					.getTrackId()), playerList, testLobby);

			Point2D firstMovePlayer1 = new Point2D(4, 3);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(firstRound, valid move)
			System.out.println("Is the first move " + firstMovePlayer1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, firstMovePlayer1));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									firstMovePlayer1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMovePlayer1 = new Point2D(3, 4);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(firstRound, valid move)
			System.out.println("Is normal move" + normalMovePlayer1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, normalMovePlayer1));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMovePlayer1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove2Player1 = new Point2D(1, 6);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(firstRound, valid move)
			System.out.println("Is normal move of Player 1 valid?: "
					+ testGame.isValid(player1, normalMove2Player1));
			// check method getCollisionPoint(firstRound, valid move)
			System.out.println("Set Player 1 to " + normalMove2Player1
					+ " should be a collision because boundary is: "
					+ TrackFactory.getSampleTrack(0).getOuterBoundary()[1]
					+ " : "
					+ testGame.getCollisionPoint(player1, normalMove2Player1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
		}

		if (whichScenario == 6) {
			System.out.println("test of driving through a corner: \n");
			// initiate important stuff for the test
			Player player1 = new Player(1);
			Player[] playerList = { player1 };
			Lobby testLobby = new Lobby(1, 1, "testLobby", 0);
			Game testGame = new Game(TrackFactory.getSampleTrack(testLobby
					.getTrackId()), playerList, testLobby);

			Point2D firstMovePlayer1 = new Point2D(3, 3);
			// check the method isValid(firstRound, valid move)
			System.out.println("Is the first move " + firstMovePlayer1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, firstMovePlayer1));
			// check method getCollisionPoint(firstRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									firstMovePlayer1));
			// check method hasPlayerWon(firstRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMovePlayer1 = new Point2D(3, 4);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(normalRound, valid move)
			System.out.println("Is normal move" + normalMovePlayer1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, normalMovePlayer1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMovePlayer1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove2Player1 = new Point2D(3, 6);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(normalRound, valid move)
			System.out.println("Is normal move" + normalMove2Player1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, normalMove2Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove2Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove3Player1 = new Point2D(4, 8);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(normalRound, valid move)
			System.out.println("Is normal move" + normalMove3Player1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, normalMove3Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove3Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove4Player1 = new Point2D(6, 10);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(normalRound, valid move)
			System.out.println("Is normal move" + normalMove4Player1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, normalMove4Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("Set Player 1 to the grid and is there a collision?: "
							+ testGame.getCollisionPoint(player1,
									normalMove4Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());

			Point2D normalMove5Player1 = new Point2D(9, 13);
			// check if currentPlayerIndex is set right
			System.out.println("currentPlayerIndex: "
					+ testGame.getCurrentPlayerIndex());

			// check the method isValid(normalRound, valid move)
			System.out.println("Is normal move" + normalMove5Player1
					+ " of Player 1 valid?: "
					+ testGame.isValid(player1, normalMove5Player1));
			// check method getCollisionPoint(normalRound, valid move)
			System.out
					.println("The line crosses the corner of the inner boundary (which is: "
							+ TrackFactory.getSampleTrack(0).getInnerBoundary()[1]
							+ " and: "
							+ TrackFactory.getSampleTrack(0).getInnerBoundary()[2]
							+ " ). So there should be a collision: "
							+ testGame.getCollisionPoint(player1,
									normalMove5Player1));
			// check method hasPlayerWon(normalRound, valid move)
			System.out.println("Has Player 1 won the game?: "
					+ testGame.hasPlayerWon(player1));
			// check method hasEveryPlayerCrashed
			System.out.println("Has every player crashed?: "
					+ testGame.hasEveryPlayerCrashed());
		}
	}
}
*/