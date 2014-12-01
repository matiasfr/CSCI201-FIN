import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.util.*;

import Models.PlayerModel;

public class ClientChatPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 8301904130678673097L;
	
	private Map<String, PlayerModel> players;
	private ObjectOutputStream oos;
	
	private JCheckBox global, team1, team2;
	private ArrayList<JCheckBox> team1_list, team2_list;
	
	private JPanel team1_panel, team2_panel;
	private JPanel inputOutputPanel;
	private JTextArea output;
	private JScrollPane outputPane;
	private JTextField input;
	
	//////////NEW CONSTRUCTOR////////////
	public ClientChatPanel(ObjectOutputStream oos) {
		this.oos = oos;
		
		team1_list = new ArrayList<JCheckBox>();
		team2_list = new ArrayList<JCheckBox>();
		
		global = new JCheckBox("global"); global.addActionListener(this);
		team1 = new JCheckBox("team 1"); team1.addActionListener(this);
		team2 = new JCheckBox("team 2"); team2.addActionListener(this);
		
		//for (Map.Entry<Integer, PlayerModel> playerEntry : players.entrySet()) {
			//PlayerModel player = playerEntry.getValue();
			//JCheckBox box = new JCheckBox(player.playerName);
			//box.addActionListener(this);
			//if (player.playerTeam == 1) team1_list.add(box);
			//else /*if player.playerTeam == 2*/ team2_list.add(box);
		//}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		team1_panel = new JPanel(); team1_panel.setLayout(new BoxLayout(team1_panel, BoxLayout.Y_AXIS));
		team2_panel = new JPanel(); team2_panel.setLayout(new BoxLayout(team2_panel, BoxLayout.Y_AXIS));
		JScrollPane team1_panel_pane = new JScrollPane(team1_panel);
		JScrollPane team2_panel_pane = new JScrollPane(team2_panel);
		inputOutputPanel = new JPanel();
		
		output = new JTextArea(5, 0); output.setEditable(false);
		input = new JTextField(); input.addActionListener(this);
		outputPane = new JScrollPane(output); outputPane.setPreferredSize(new Dimension(200, 200));
		inputOutputPanel.setMaximumSize(new Dimension(200, 250));
		inputOutputPanel.setMinimumSize(new Dimension(200, 250));
		inputOutputPanel.setLayout(new BoxLayout(inputOutputPanel, BoxLayout.Y_AXIS));
		
		inputOutputPanel.add(outputPane);
		inputOutputPanel.add(input);
		add(inputOutputPanel);
		add(global);
		add(team1);
		add(team1_panel_pane);
		add(team2);
		add(team2_panel_pane);
		
		setSize(200, 600);
	} // end public ClientChatPanel(ClientApplication, ObjectOutputStream) constructor
	/////////////////////////////////////
	
	//////////////NEW////////////////////
	public void initPlayers() {
		this.players = ClientApplication.myGridMap.getPlayers();
		
		for(Map.Entry<String, PlayerModel> playerEntry : players.entrySet()) {
			PlayerModel player = playerEntry.getValue();
			JCheckBox box = new JCheckBox(player.playerName);
			//System.out.println("new player added to chat: " + player.playerName);
			box.addActionListener(this);
			if (player.playerTeam == 1) team1_list.add(box);
			else /*if player.playerTeam == 2*/ team2_list.add(box);
		}
		
		for (JCheckBox box : team1_list) team1_panel.add(box);
		for (JCheckBox box : team2_list) team2_panel.add(box);
		//revalidate(); repaint();
	} // end public void initPlayers
	/////////////////////////////////////

	public void writeChatMessage (String s) {
		output.setText(output.getText() + "\n" + s);
	} // end public void writeChatMessage
	
	private String getSelectedPlayerIDs () {
		ArrayList<String> playerNames = new ArrayList<String>();
		for(JCheckBox box : team1_list)
			if(box.isSelected()) playerNames.add(box.getText());
		for(JCheckBox box : team2_list)
			if(box.isSelected()) playerNames.add(box.getText());
		
		String s = "CHAT:";
		for(Map.Entry<String, PlayerModel> player : players.entrySet())
			for(String playerName : playerNames)
				if(player.getValue().playerName.equals(playerName))
					s += (player.getKey()) + ",";
		
		return s + ":";
	} // end private String getSelectedPlayerIDs
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == input && !getSelectedPlayerIDs().equals("CHAT::") && !input.getText().equals("")) {
		//send "CHAT:playerID1,playerID2,etc:message" only if at least 1 recipient is selected, and message is not empty
			Object outputObject = getSelectedPlayerIDs() + input.getText();
			try {
				oos.writeObject(outputObject);
			} catch(Exception ex) {
				System.out.println(ex.getStackTrace());
			}
			//System.out.println(getSelectedPlayerIDs() + input.getText());
			input.setText("");
		}
		else if(e.getSource() == global) {
			boolean state = global.isSelected();
			team1.setSelected(state);
			team2.setSelected(state);
			for(JCheckBox box : team1_list) box.setSelected(state);
			for(JCheckBox box : team2_list) box.setSelected(state);
		}
		else if(e.getSource() == team1) {
			boolean state = team1.isSelected();
			if(team1.isSelected() && team2.isSelected()) global.setSelected(true);
			else global.setSelected(false);
			for(JCheckBox box : team1_list) box.setSelected(state);
		}
		else if(e.getSource() == team2) {
			boolean state = team2.isSelected();
			if(team1.isSelected() && team2.isSelected()) global.setSelected(true);
			else global.setSelected(false);
			for(JCheckBox box : team2_list) box.setSelected(state);
		}
		
		boolean team1_checked = true;
		for(JCheckBox box : team1_list)
			if(!box.isSelected()) {
				team1_checked = false;
				team1.setSelected(false);
			}
		if (team1_list.isEmpty()) team1_checked = false;
		if(team1_checked) {
			team1.setSelected(true);
			if(team1.isSelected() && team2.isSelected()) global.setSelected(true);
		}
		
		boolean team2_checked = true;
		for(JCheckBox box : team2_list)
			if(!box.isSelected()) {
				team2_checked = false;
				team2.setSelected(false);
			}
		if (team2_list.isEmpty()) team2_checked = false;
		if(team2_checked) {
			team2.setSelected(true);
			if(team1.isSelected() && team2.isSelected()) global.setSelected(true);
		}
	}
}