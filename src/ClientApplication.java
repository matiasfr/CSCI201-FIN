import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Models.GridMapModel;

public class ClientApplication extends JFrame implements Runnable {
	private static final long serialVersionUID = 2466254954333927929L;
	ClientDrawingPanel drawingPanel;
	private static ClientLoginPanel logIn;
	private static ClientLobbyPanel lobby;
	
	private JPanel cardsPanel;
	private CardLayout cl;

	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromServer;
	private static Socket mySocket;
	
	public static GridMapModel myGridMap = null;
	private static ClientGamePanel myGame;
	@SuppressWarnings("unused")
	private Boolean needToLogin = true;
	@SuppressWarnings("unused")
	private Boolean inLobby = true;
	@SuppressWarnings("unused")
	private int timerTest = 1;
	
	public ClientApplication() {
		super("Our Game");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 475));
		setLocationRelativeTo(null);

		try {
			String hostname = "127.0.0.1";
			int port = 5001;
			mySocket = new Socket(hostname, port);
			inFromServer = new ObjectInputStream(mySocket.getInputStream());
			outToServer = new ObjectOutputStream(mySocket.getOutputStream());		
		}
		catch(IOException ioe) {
			System.out.println("not connected");
		}
		 
		cl = new CardLayout();
		cardsPanel = new JPanel(cl);
		
		myGame = new ClientGamePanel(outToServer, this);
		logIn = new ClientLoginPanel(outToServer);
		lobby = new ClientLobbyPanel(this);
		
		cardsPanel.add(myGame, "GamePanel");
		cardsPanel.add(logIn, "LoginScreen");
		cardsPanel.add(lobby, "Lobby");
		add(cardsPanel);
		setVisible(true);
		cl.show(cardsPanel, "LoginScreen");
		new Thread(this).start();
	} //end public Client constructor
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
				
				//BEGIN TESTING CODE 
				//hide loginPanel and show lobbyPanel (loggedIn())
				
				/*cl.show(cardsPanel, "Lobby");
				 if (timerTest ==1){
					 myGame.drawingPanel.setName("apple");
					 lobby.setCountdown(5);
					lobby.startCountdown();
					cl.show(cardsPanel,"GamePanel");
					timerTest--;
				 }*/
				//END TESTING CODE
				 
				getFromServer();
				repaint();
				revalidate();
			} 
			catch(Exception e) {
				System.out.println(e.getStackTrace());
			}
		}
		//We need to wait for the server to start sending something to us.
		//then we have to take the object that the server sends to us and deserialize it. 	
	} // end public void run
	
	public void sendReadySignal() {
		try {
			outToServer.writeObject("STATUS:READY");
		} catch(IOException e) {
			e.printStackTrace();
		}
	} // end public void sendReadySignal
	
 	public void loggedIn() {
		//hide login panel and show lobby panel 
		needToLogin = false;
	} // end public void loggedIn
	
	public void launchGame() {
		//hide lobby panel and show game panel.
		inLobby = false; 
	} // end public void launchGame
	
	public void sendToServer(GridMapModel gridMapObject) {
		try{
			outToServer.writeObject(gridMapObject);   
		}
		catch(Exception e) {
			System.err.println("Client Error: " + e.getMessage());
			System.err.println("Localized: " + e.getLocalizedMessage());
			System.err.println("Stack Trace: " + e.getStackTrace());
		}
	} // end public void sendToServer(GridMapModel)

	public void getFromServer() {
		//GridMapModel gridMapModel = new GridMapModel();
		try {
			Object serverObject = inFromServer.readObject();
			if(serverObject instanceof GridMapModel) {
				Object serverObject2 = inFromServer.readObject();
				if(serverObject2 instanceof GridMapModel) {
					myGridMap = (GridMapModel)serverObject2;
					//System.out.println(myGridMap.playerLookup.size());
					//System.out.println(myGridMap.allModels.length);				
					myGame.drawingPanel.refreshMap();
					//update gridmapModel on our side.
				} else {
					String serverString = (String)serverObject2;
					readServerString(serverString);
				}
			} else {
				String serverString = (String)serverObject;
				readServerString(serverString);
				
			}
		} catch(Exception e) {
			System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
	    }	
	} // end public void getFromServer
	
	public void readServerString(String msg) {
		System.out.println("\n" + msg);

		//MESSAGE PREFIX: TIMER
		if(msg.equals("TIMER")) {
			lobby.setCountdown(4);
			lobby.startCountdown();
		} else {
			String serverMessage[] = msg.split(":");
			String prefix = serverMessage[0];
			String content = serverMessage[1];
			
			//MESSAGE PREFIX: CHAT 
			if(prefix.equals("CHAT")) {
				if(content.equals("BROADCAST")) {
					String playerFinalName = serverMessage[2];
					lobby.addPlayer(playerFinalName);
				} else {
					System.out.println("CHAT:" + content);
					myGame.chatPanel.writeChatMessage(content + ": " + serverMessage[2]);
				}
			} // end if(prefix.equals("CHAT"))

			//MESSAGE PREFIX: USERNAME
			else if(prefix.equals("USERNAME")) {
				//hide loginPanel and show lobbyPanel (loggedIn())
				//we get the username from the server and set it here so we know who we are. 
				System.out.println("in the username execute");
				myGame.drawingPanel.setName(content);
				cl.show(cardsPanel, "Lobby");
			} // end if(prefix.equals("USERNAME"))
			
			//MESSAGE PREFIX: STATUS
			else if(prefix.equals("STATUS")) {
				//when status is go, hide lobbyPanel and show gamePanel
				if(content.equals("START")) {
					getFromServer();
					cl.show(cardsPanel, "GamePanel");
//					System.out.println("GamePanel has been set");
					myGame.chatPanel.initPlayers();
				} // end if(content.equals("START"))
			} // end if(prefix.equals("STATUS"))
			else if(prefix.contains("USERNAME")) {
				//hide loginPanel and show lobbyPanel (loggedIn())
				//we get the username from the server and set it here so we know who we are. 
				System.out.println("in the username execute");
				myGame.drawingPanel.setName(content);
				cl.show(cardsPanel, "Lobby");
			} // end if(prefix.contains("USERNAME"))
		} // end if (!msg.equals("TIMER"))
	} // end public void readServerMessage(String)
	
	public void sendServerMessage(String s) {
		try {
//			System.out.println("server message: " + s);
			outToServer.writeObject(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end public void sendServerMessage(String)

	public void addChatMessage(String s) {
		//chatPanel.addText(s);
	} // end public void addChatMessage(String)

	public static void main(String [] args){
		@SuppressWarnings("unused")
		ClientApplication myClient = new ClientApplication();
	} // end public static void main
} // end public class ClientApplication extends JFrame implements Runnable