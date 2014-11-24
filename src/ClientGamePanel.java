import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class ClientGamePanel extends JFrame implements ActionListener {

	private GridMapModel gridMap;
	JPanel mainPanel;
	JPanel drawingPanel;
	JPanel chatPanel;
	JPanel statsPanel;
	

	public ClientGamePanel(GridMapModel gridMap){
		super("Our Game");
		setResizable(false);
		this.gridMap= gridMap;
		//Sets up the entire view for the game. Main panel will include:
		// 1.) ClientDrawingpanel (CENTER) 
		// 2.) ChatPanel        (EAST)
		// 3.) stats panel      (WEST)
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setSize(1000,600);
		
		//set up chat panel
		chatPanel = new ClientChatPanel();
		chatPanel.setSize(200,600);
		//chatPanel.getPreferredSize();
		statsPanel = new ClientStatsPanel();
		statsPanel.setSize(200,600);
		
		//set up drawing panel
		drawingPanel = new ClientDrawingPanel();
		drawingPanel.setSize(600,600);
		drawingPanel.setLayout(null);
		
		
		mainPanel.add(drawingPanel);
		mainPanel.add(statsPanel);
		mainPanel.add(chatPanel);
		chatPanel.setLocation(800, 0);
		add(mainPanel);
		//add(chatPanel);
		//mainPanel.setPreferredSize(new Dimension(600, 600));
		mainPanel.setVisible(true);
		
		
	}
	public JPanel gui(){
		return mainPanel;
	}
	
	
	public void actionPerformed(ActionEvent ae){
			
	}
}
