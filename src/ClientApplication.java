import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClientApplication {

	
	
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
		GridMapModel myGridMap;
		private  static ClientGamePanel myGame;
		public ClientApplication() {
			//Create panels, pass in this to constructors
			//Create threads
		} //end public Client constructor
		public void sendToServer(String message){
		
		}
		//public Object getFromServer(){
		
	//	}

		public void addChatMessage(String s) {
			//chatPanel.addText(s);
		}	
	
		public static void main(String [] args){
			 myGame = new ClientGamePanel(myGridMap);
			 myGame.setMinimumSize(new Dimension(800,600));
			 myGame.setLocationRelativeTo(null);
			 myGame.setVisible(true);
		}
}

