import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import javax.swing.*;

import Models.*;

public class ClientGamePanel extends JPanel {
	private static final long serialVersionUID = 3074976706165093453L;
	private ObjectOutputStream oos;
	private BufferedReader br;	
	private static Socket mySocket;
	private GridMapModel gridMap;
	JPanel mainPanel;
	ClientDrawingPanel drawingPanel;
	ClientChatPanel chatPanel;
	//ClientStatsPanel statsPanel;
	//JPanel statsPanel;
	private ClientApplication myApp;

	public ClientGamePanel(ObjectOutputStream oos, ClientApplication myApp){
		this.gridMap= myApp.myGridMap;
		this.myApp = myApp;
		this.oos = oos;
		//Sets up the entire view for the game. Main panel will include:
		// 1.) ClientDrawingpanel (CENTER) 
		// 2.) ChatPanel        (EAST)
		// 3.) stats panel      (WEST)
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setSize(1000,600);
		
		//set up chat panel
		//Map<Integer, PlayerModel> players, PrintWriter pw, BufferedReader br
		try {
			chatPanel = new ClientChatPanel(myApp, oos);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//statsPanel = new ClientStatsPanel();
		//statsPanel.setSize(200,600);
		
		//set up drawing panel
		drawingPanel = new ClientDrawingPanel(myApp, this);
		drawingPanel.setSize(600,600);
		drawingPanel.setLayout(null);
		
		mainPanel.add(drawingPanel, BorderLayout.CENTER);
		//drawingPanel.setLocation(200, 0);
		//mainPanel.add(statsPanel);
		mainPanel.add(chatPanel, BorderLayout.EAST);
		chatPanel.setLocation(800, 0);
		add(mainPanel);
		add(chatPanel);
		mainPanel.setPreferredSize(new Dimension(600, 600));
		mainPanel.setVisible(true);	
	}	

	//we got a new gridmapmodel so let everyone know. 
	public void refreshAll() {
		drawingPanel.repaint();
	}
}