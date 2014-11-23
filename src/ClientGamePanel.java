import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;


public class ClientGamePanel extends JFrame implements ActionListener {

	private GridMapModel gridMap;
	JPanel mainPanel;
	JPanel drawingPanel;
	//JPanel chatPanel;
	//JPanel statsPanel;

	public ClientGamePanel(GridMapModel gridMap){
		this.gridMap= gridMap;
		//Sets up the entire view for the game. Main panel will include:
		// 1.) ClientDrawingpanel (CENTER) 
		// 2.) ChatPanel        (EAST)
		// 3.) stats panel      (WEST)
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//set up chat panel
		//chatPanel = new ChatPanel();
		//statsPanel = new StatsPanel();
		
		//set up drawing panel
		drawingPanel = new ClientDrawingPanel();
		
		
	}
	
	public void actionPerformed(ActionEvent ae){
			
	}
}
