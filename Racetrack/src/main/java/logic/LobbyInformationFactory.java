package src.main.java.logic;

public class LobbyInformationFactory {
	
	private static String[] lobbyNames = new String[] {
		"Hallo Welt",
		"Racer",
		"Nürburgring"
	};
	
	private static String[][] playerNames = new String[][] {
		new String[] {
				"Tobias",
				null,
				"Denis",
				null
		},
		new String[] {
				"Tobias",
				"Denis"
		},
		new String[] {
				"Tobias",
				"Denis",
				null,
				null
		}
	};
	
	private static Integer[][] playerIDs = new Integer[][]{
		new Integer[] {
				null,
				null,
				null,
				null
		},
		new Integer[] {
				null,
				null,
				null,
				null
		},
		new Integer[] {
				null,
				null,
				null,
				null
		}
	};
	
	private static Integer[][] typeIDs = new Integer[][]{
		new Integer[] {
				1,
				0,
				1,
				0
		},
		new Integer[] {
				1,
				1,
				0,
				0
		},
		new Integer[] {
				1,
				1,
				0,
				0
		}
	};
	
	private static boolean[][] playerParticipating = new boolean[][] {
		new boolean[] {
				true,
				false,
				false,
				false
		},
		new boolean[] {
				false,
				false
		},
		new boolean[] {
				false,
				false,
				false,
				false
		}
	};
	
	@SuppressWarnings("unused")
	private static Track[] tracks = new Track[] {
		TrackFactory.getSampleTrack(1),
		TrackFactory.getSampleTrack(2),
		TrackFactory.getSampleTrack(3)
	};
	
	public static LobbyInformation getSampleLobby(int i) {
		int index = i % lobbyNames.length;
		
		LobbyInformation lobby = new LobbyInformation(playerNames[index], playerIDs[index], playerParticipating[index], typeIDs[index], index, lobbyNames[index], false, index);
		
		return lobby;
	}

}
