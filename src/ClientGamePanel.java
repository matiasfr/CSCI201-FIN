import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import javax.swing.*;


public class ClientGamePanel extends JPanel {
	
	
	private PrintWriter pw;
	private BufferedReader br;	
	private static Socket mySocket;
	private GridMapModel gridMap;
	JPanel mainPanel;
	ClientDrawingPanel drawingPanel;
	JPanel chatPanel;
	JPanel statsPanel;
	

	public ClientGamePanel( GridMapModel gridMap, PrintWriter pw){
		
		this.gridMap= gridMap;
		
		this.pw=pw;
		//Sets up the entire view for the game. Main panel will include:
		// 1.) ClientDrawingpanel (CENTER) 
		// 2.) ChatPanel        (EAST)
		// 3.) stats panel      (WEST)
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setSize(1000,600);
		
		//set up chat panel
		//Map<Integer, PlayerModel> players, PrintWriter pw, BufferedReader br
		//chatPanel = new ClientChatPanel( gridMap.getPlayers(),pw, br );

		statsPanel = new ClientStatsPanel();
		statsPanel.setSize(200,600);
		
		//set up drawing panel
		drawingPanel = new ClientDrawingPanel(gridMap);
		drawingPanel.setSize(600,600);
		drawingPanel.setLayout(null);
		
		
		mainPanel.add(drawingPanel);
		drawingPanel.setLocation(200, 0);
		mainPanel.add(statsPanel);
		//mainPanel.add(chatPanel);
		//chatPanel.setLocation(800, 0);
		add(mainPanel);
		//add(chatPanel);
		//mainPanel.setPreferredSize(new Dimension(600, 600));
		mainPanel.setVisible(true);
		
		
		
		
	}
	
	
	
	//we got a new gridmapmodel so let everyone know. 
	public void refreshAll(){
		drawingPanel.repaint();
	}
	

	
	
}
