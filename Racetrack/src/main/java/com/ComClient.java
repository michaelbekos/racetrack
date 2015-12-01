package src.main.java.com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import src.main.java.com.messages.RaceTrackMessage;
import src.main.java.core.ControllerClient;
import src.main.java.gui.Racetracker;

/**
 * ComClient handles the connection to the Racetrack server, receives messages 
 * propagates them to the controller and sends answers from the controller to
 * the server.
 * 
 * @author Denis
 *
 */
public class ComClient implements ICommunication {

	/**
	 * Controller on client side, administrating logic
	 */
	private ControllerClient controller;

	/**
	 * is currently writing?
	 */
	private boolean isWriting;

	/**
	 * Incoming messages from the server are written down onto this stream
	 */
	private ObjectInputStream objectInputStream;

	/**
	 * Outputstream on the socket, to be able to send messages to the server
	 */
	private ObjectOutputStream objectOutputStream;

	/**
	 * socket on client side, which "connects" to the socket on server side
	 */
	protected Socket serverSocket;

	private Boolean hasSuccesfullyConnected;

	private boolean isConnected;

	/**
	 * Set comClient controller
	 * 
	 * @param controller
	 *            The controller
	 */
	public ComClient(ControllerClient controller) {
		this.controller = controller;
	}

	/**
	 * This will try to kill the connection, if it wasn't already
	 */
	public void killComClient() {

		isConnected = false;

		hasSuccesfullyConnected = null;
		
		try {

			objectInputStream.close();
			objectOutputStream.close();
			serverSocket.close();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e){
			
			e.printStackTrace();
			
		}

	}

	/**
	 * Try to establish a connection to the server by given credentials.
	 * 
	 * @param host
	 *            The host ip address.
	 * @param port
	 *            The host port.
	 */
	public void connectToServer(String host, int port) {
		hasSuccesfullyConnected = false;
		ServerConnectionHandler serverHandler = new ServerConnectionHandler(host, port);
		if (hasSuccesfullyConnected) {
			serverHandler.start();
		}
	}

	/**
	 * sends a message over the outputstream to the server.
	 */
	@Override
	public void sendMessage(RaceTrackMessage m) {

		try {
			isWriting = true;
			objectOutputStream.writeObject(m);
			objectOutputStream.flush();
			objectOutputStream.reset();
			isWriting = false;
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * propagates a message from the server to the controller
	 * 
	 * @param message
	 */
	protected synchronized void processMessage(RaceTrackMessage message) {
		controller.receiveMessage(message);
	}

	/**
	 * Thread, which listens to answers from the server
	 * 
	 * @author denis_000
	 *
	 */
	protected class ServerConnectionHandler extends Thread {

		/**
		 * the host IP to whom the client connects
		 */
		private String host;

		/**
		 * host port on which the service is running
		 */
		private int port;

		public ServerConnectionHandler(String host, int port) {
			this.host = host;
			this.port = port;

			connectToServer();
		}

		/**
		 * tries to connect to the server and opens up the input and
		 * outputstreams
		 */
		private void connectToServer() {

			try {
				serverSocket = new Socket();
				serverSocket.connect(new InetSocketAddress(host, port), 10000);

				objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
				objectOutputStream.flush();
				objectOutputStream.reset();
				objectInputStream = new ObjectInputStream(serverSocket.getInputStream());

				hasSuccesfullyConnected = true;
				controller.hasConnectedSuccesfully(hasSuccesfullyConnected);
			} catch (Exception e) {

				hasSuccesfullyConnected = false;
				// no connection could be established
				controller.hasConnectedSuccesfully(hasSuccesfullyConnected);

				// e.printStackTrace();

			}

		}

		public void run() {
			isConnected = true;
			try {
				while (isConnected)
					readInput();
			} catch (Exception e) {
				// try {
				// serverSocket.close();
				// } catch (IOException e1) {
				// e1.printStackTrace();
				// }
			}
			// finally{
			// try {
			//
			// serverSocket.close();
			//
			// } catch (Exception e) {
			//
			// e.printStackTrace();
			// }
			// }

		}

		private void readInput() {
			try {

				/**
				 * the message received by the server
				 */
				RaceTrackMessage messageFromServer;
				Object object = null;

				while ((object = (RaceTrackMessage) objectInputStream.readObject()) != null) {

					if (object instanceof RaceTrackMessage) {
						messageFromServer = (RaceTrackMessage) object;
						controller.receiveMessage(messageFromServer);
					} else {
						break;
					}
				}

			} catch (SocketTimeoutException e) {

				try {
					Racetracker.printInDebugMode("ComClient: Lost connection because of SocketTimeoutException");
					controller.lostConnectionToServer();
					objectInputStream.close();
					objectOutputStream.close();
					serverSocket.close();
				} catch (IOException e1) {

					e1.printStackTrace();

				}

			} catch (SocketException e) {

				if (isConnected)
					try {
						Racetracker.printInDebugMode("ComClient: Lost connection because of SocketException");
						controller.lostConnectionToServer();
						isConnected = false;
					} catch (Exception e1) {

						e1.printStackTrace();

					}

			} catch (ClassNotFoundException e) {

				e.printStackTrace();

			} catch (IOException e) {

				if (isConnected)
					try {
						Racetracker.printInDebugMode("ComClient: Lost connection because of IOException");
						controller.lostConnectionToServer();
						isConnected = false;
					} catch (Exception e1) {

						e1.printStackTrace();
					}

			}
		}
	}
}