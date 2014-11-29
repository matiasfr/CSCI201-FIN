import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Models.*;

import javax.swing.*;

public class ClientApplication extends JFrame implements Runnable{

		ClientDrawingPanel drawingPanel;
		private static ClientLoginPanel logIn;
		private static ClientLobbyPanel lobby;
		
		private JPanel cardsPanel;
		private CardLayout cl;

		//Model
		private ObjectOutputStream outToServer;
		private ObjectInputStream inFromServer;
		private PrintWriter pw;
		private BufferedReader br;	
		private static Socket mySocket;
		
		private static GridMapModel myGridMap;
		private static ClientGamePanel myGame;
		private Boolean needToLogin = true;
		private Boolean inLobby = true;
		private int timerTest=1;
	
		
		
		
		public ClientApplication() {
			super("Our Game");
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setMinimumSize(new Dimension(1000,600));
			setLocationRelativeTo(null);
			
			
			
			try{
					String hostname = "127.0.0.1";
					int port = 5001;
					mySocket = new Socket(hostname, port);
					br =new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
					pw =new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
					//NetworkThread myNT = new NetworkThread(this, br);
					//myNT.start();
				}
				catch(IOException ioe){
					
				}
			 
			cl = new CardLayout();
			cardsPanel = new JPanel(cl);
						
			//myGame = new ClientGamePanel( myGridMap,pw, this);
			logIn = new ClientLoginPanel( pw);
			lobby = new ClientLobbyPanel(this);
			
			//cardsPanel.add(myGame, "GamePanel");
			cardsPanel.add(logIn, "LoginScreen");
			cardsPanel.add(lobby, "Lobby");
			add(cardsPanel);
			setVisible(true);
			cl.show(cardsPanel, "LoginScreen");
			
			
			new Thread(this).start();

			
		} //end public Client constructor
		
		public void run(){
			while(true){
				try {
					Thread.sleep(100);
					
					//BEGIN TESTING CODE 
					//hide loginPanel and show lobbyPanel (loggedIn())
					
					cl.show(cardsPanel, "Lobby");
					 if (timerTest ==1){
						 myGame.drawingPanel.setName("apple");
						 lobby.setCountdown(5);
						lobby.startCountdown();
						cl.show(cardsPanel,"GamePanel");
						timerTest--;
					 }
					//END TESTING CODE
					 
					//reading from the buffered reader.
					String readString="";
					if( (readString = br.readLine()) != null){
						
						String msg = readString;
						//MESSAGE PREFIX: TIMER
						if(msg.equals("TIMER")){
							lobby.setCountdown(20);
							lobby.startCountdown();
						}else{
							String serverMessage[] = msg.split(":");
							String prefix ="";
							String content="";
							 
							
							//MESSAGE PREFIX: CHAT 
							if(prefix.equals("CHAT")){
								System.out.println("CHAT:"+content);
								myGame.chatPanel.writeChatMessage(content);
							}

							//MESSAGE PREFIX: USERNAME
							if(prefix.equals("USERNAME")){
								//hide loginPanel and show lobbyPanel (loggedIn())
								//we get the username from the server and set it here so we know who we are. 
								System.out.println("in the username execute");
								myGame.drawingPanel.setName(content);
								cl.show(cardsPanel, "Lobby");
							}
							
							//MESSAGE PREFIX: STATUS
							if(prefix.equals("STATUS")){
								//when status is go, hide lobbyPanel and show gamePanel
								if(content.equals("START")){
									cl.show(cardsPanel,"GamePanel");
								}
							}
						}
					} 
					//String msg= br.readLine();
					getFromServer();
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
				 catch (IOException e) {
					   System.err.println("Error: " + e);
					 }
				//refreshAll();
			}
			//We need to wait for the server to start sending something to us.
			//then we have to take the object that the server sends to us and deserialize it. 	
			
		}
		
		public void loggedIn(){
			//hide login panel and show lobby panel 
			needToLogin=false;
		}
		
		public void launchGame(){
			//hide lobby panel and show game panel.
			inLobby=false; 
		}
		
		public void sendToServer(GridMapModel gridMapObject){
			try{
				outToServer = new ObjectOutputStream(mySocket.getOutputStream());		
				outToServer.writeObject(gridMapObject);   
			}
	     catch (Exception e) {
	        System.err.println("Client Error: " + e.getMessage());
	        System.err.println("Localized: " + e.getLocalizedMessage());
	        System.err.println("Stack Trace: " + e.getStackTrace());
	    }
		}
		public void getFromServer(){
			//GridMapModel gridMapModel = null;
			try{
				inFromServer = new ObjectInputStream(mySocket.getInputStream());
				myGridMap = (GridMapModel)inFromServer.readObject();
				myGame.drawingPanel.refreshMap(myGridMap);
				//update gridmapModel on our side. 
				
			}catch (Exception e) {
		        
				System.err.println("Client Error: " + e.getMessage());
		        System.err.println("Localized: " + e.getLocalizedMessage());
		        System.err.println("Stack Trace: " + e.getStackTrace());
		    }
			
			
		
		}
		
		public void sendServerMessage(String s){
			pw.println(s);
		}

		public void addChatMessage(String s) {
			//chatPanel.addText(s);
		}	
	
		public static void main(String [] args){
			
			ClientApplication myClient = new ClientApplication();
			 
		}
		
		
			
		
}

