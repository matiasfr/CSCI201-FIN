import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.Socket;

public class ClientLobbyPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = -4190798695389804885L;
	// Countdown
	private JLabel countdownLabel;
	private static int countdownNum = 30;
	private static final String TIME_UNTIL_START_STRING = "Seconds until start: ";
	private Timer countdownTimer;
	private static final int ONE_SECOND = 1000;
	// Users in lobby
	private JTextArea usersTextArea;
	private static final String PLAYERS_IN_LOBBY_STRING = "Players in Lobby:\n";
	private ArrayList<String> usersList;
	// Game stuff
	//private ClientApplication myClient;
	// Networking stuff
	private Socket s;
	private BufferedReader br;

	//// Constructor ////
	public ClientLobbyPanel(Socket s, BufferedReader br) {
		//// Initialization ////
		// Game stuff
		//this.myClient = myClient;
		
		// Countdown
		countdownLabel = new JLabel(TIME_UNTIL_START_STRING + countdownNum);
		countdownTimer = new Timer(ONE_SECOND, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				countdownNum--;
				updateCountdown();
			} // end public void actionPerformed(ActionEvent)
		});
		countdownTimer.start();

		// Users in lobby
		usersTextArea = new JTextArea(PLAYERS_IN_LOBBY_STRING);
		usersList = new ArrayList<String>();

		// Create ClientLobbyThread
		// Networking stuff
		this.s = s;
		this.br = br;
		// Stop thread
	} // end public ClientLobbyPanel constructor

	//// Player List Methods ////
	public void addPlayer(String newPlayer) {
		usersList.add(newPlayer);
		updatePlayers();
	} // end public void addPlayer(String)

	public void updatePlayers() {
		usersTextArea.setText(PLAYERS_IN_LOBBY_STRING);
		for(String username : usersList) {
			usersTextArea.setText(usersTextArea.getText() + username + "\n");
		}
	} // end public void updatePlayers

	public void removePlayer(String player) {
		usersList.remove(player);
		updatePlayers();
	} // end public void removePlayer(String)

	//// Countdown Methods ////
	public void setCountdown(int newCountdown) {
		countdownNum = newCountdown;
		updateCountdown();
	} // end public void setCountdown(int)
	
	public void updateCountdown() {
		countdownLabel.setText(TIME_UNTIL_START_STRING + countdownNum);
	} // end public void updateCountdown

	//// Thread Methods ////
	public void run() {
		while(true) {
			// Listen for new player name from server, add when received
			// Listen for start signal, tell myClient to show gamePanel
		} // end listening for signals from server			
	} // end public void run
} // end class ClientLobbyPanel
