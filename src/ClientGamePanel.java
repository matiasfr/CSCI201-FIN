import java.awt.BorderLayout;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

public class ClientGamePanel extends JPanel {
	private static final long serialVersionUID = 3074976706165093453L;
	@SuppressWarnings("unused")
	private ObjectOutputStream oos;
	ClientDrawingPanel drawingPanel;
	ClientChatPanel chatPanel;
	//ClientStatsPanel statsPanel;
	//JPanel statsPanel;
	@SuppressWarnings("unused")
	private ClientApplication myApp;

	public ClientGamePanel(ObjectOutputStream oos, ClientApplication myApp) {
		// Initial GUI Setup
		setLayout(new BorderLayout());
		setSize(1000, 600);
		
		this.myApp = myApp;
		this.oos = oos;
		//Sets up the entire view for the game. Main panel will include:
		// 1.) ClientDrawingpanel (CENTER) 
		// 2.) ChatPanel        (EAST)
		// 3.) stats panel      (WEST)
		
		//set up chat panel
		//Map<Integer, PlayerModel> players, PrintWriter pw, BufferedReader br
		try {
			chatPanel = new ClientChatPanel(myApp, oos);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// Set up stats panel
		//statsPanel = new ClientStatsPanel();
		//statsPanel.setSize(200,600);
		
		//set up drawing panel
		drawingPanel = new ClientDrawingPanel(myApp, this);
		
		// Add panels
		add(drawingPanel, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.EAST);
		
		// Final GUI Setup
		setVisible(true);	
	} // end public ClientGamePanel(ObjectOutputStream, ClientApplication) constructor

	//we got a new gridmapmodel so let everyone know. 
	public void refreshAll() {
		drawingPanel.repaint();
	} // end public void refreshAll
} // end public class ClientGamePanel extends JPanel