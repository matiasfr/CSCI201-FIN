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

import javax.swing.*;

public class ClientApplication extends JFrame{

	
	
		//Panels
	//	ClientLoginPanel loginPanel;
		//ClientLobbyPanel lobbyPanel;
		//ClientGamePanel gamePanel;
	//	ClientChatPanel chatPanel;
		ClientDrawingPanel drawingPanel;
		//Threads
	//	ClientLoginThread loginThread;
	//	ClientLobbyThread lobbyThread;
	//	ClientGameReceiverThread gameReceiverThread;
	//	ClientGameSenderThread gameSenderThread;
	//	ClientChatThread chatThread;
		//Model
		private ObjectOutputStream outToServer;
		private ObjectInputStream inFromServer;
		private PrintWriter pw;
		private BufferedReader br;	
		private static Socket mySocket;
		
		private static GridMapModel myGridMap;
		private  static ClientGamePanel myGame;
		public ClientApplication() {
			 myGame = new ClientGamePanel(myGridMap);
			 myGame.setMinimumSize(new Dimension(1000,600));
			 //myGame.setMaximumSize(new Dimension(1000,600));
			 
			 myGame.setLocationRelativeTo(null);
			// myGame.pack();
			 myGame.setVisible(true);
			 
			 try{
					String hostname = "127.0.0.1";
					int port = 1392;
					mySocket = new Socket(hostname, port);
					br =new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
					pw =new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
					NetworkThread myNT = new NetworkThread(this, br);
					myNT.start();
				}
				catch(IOException ioe){
					
				}
			//Create panels, pass in this to constructors
			//Create threads
		} //end public Client constructor
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
		public GridMapModel getFromServer(){
			GridMapModel gridMapModel = null;
			try{
				inFromServer = new ObjectInputStream(mySocket.getInputStream());
				gridMapModel = (GridMapModel)inFromServer.readObject();
				
			}catch (Exception e) {
		        
				System.err.println("Client Error: " + e.getMessage());
		        System.err.println("Localized: " + e.getLocalizedMessage());
		        System.err.println("Stack Trace: " + e.getStackTrace());
		    }
			
			return gridMapModel;
		
		}

		public void addChatMessage(String s) {
			//chatPanel.addText(s);
		}	
	
		public static void main(String [] args){
			
			ClientApplication myClient = new ClientApplication();
			myClient.setVisible(true);
			
			 
		
			 
		}
}

