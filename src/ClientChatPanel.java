import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import Models.*;

//import Models.PlayerModel;

public class ClientChatPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 8301904130678673097L;
	
	private Map<Integer, PlayerModel> players;
	private PrintWriter pw;
	private BufferedReader br;
	
	private JCheckBox global, team1, team2;
	private ArrayList<JCheckBox> team1_list, team2_list;
	
	private JPanel team1_panel, team2_panel;
	private JPanel inputOutputPanel;
	private JTextArea output;
	private JScrollPane outputPane;
	private JTextField input;
	
	public ClientChatPanel (Map<Integer, PlayerModel> players, PrintWriter pw, BufferedReader br) {
		this.players = players;
		this.pw = pw;
		this.br = br;
		
		team1_list = new ArrayList<JCheckBox>();
		team2_list = new ArrayList<JCheckBox>();
		
		global = new JCheckBox("global"); global.addActionListener(this);
		team1 = new JCheckBox("team 1"); team1.addActionListener(this);
		team2 = new JCheckBox("team 2"); team2.addActionListener(this);
		
		for (Map.Entry<Integer, PlayerModel> playerEntry : players.entrySet()) {
			PlayerModel player = playerEntry.getValue();
			JCheckBox box = new JCheckBox(player.playerName);
			box.addActionListener(this);
			if (player.playerTeam == 1) team1_list.add(box);
			else /*if player.playerTeam == 2*/ team2_list.add(box);
		}
		
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
		for (JCheckBox box : team1_list) team1_panel.add(box);
		for (JCheckBox box : team2_list) team2_panel.add(box);
		
		setSize(200, 600);
	}
	
	public void actionPerformed (ActionEvent e) {
		if (e.getSource() == input) {
			//writeChatMessage(input.getText());
			pw.println(getSelectedPlayerIDs() + input.getText());
			pw.flush();
			input.setText("");
		}
		else if (e.getSource() == global) {
			boolean state = global.isSelected();
			team1.setSelected(state);
			team2.setSelected(state);
			for (JCheckBox box : team1_list) box.setSelected(state);
			for (JCheckBox box : team2_list) box.setSelected(state);
		}
		else if (e.getSource() == team1) {
			boolean state = team1.isSelected();
			if (team1.isSelected() && team2.isSelected()) global.setSelected(true);
			else global.setSelected(false);
			for (JCheckBox box : team1_list) box.setSelected(state);
		}
		else if (e.getSource() == team2) {
			boolean state = team2.isSelected();
			if (team1.isSelected() && team2.isSelected()) global.setSelected(true);
			else global.setSelected(false);
			for (JCheckBox box : team2_list) box.setSelected(state);
		}
		
		boolean team1_checked = true;
		for (JCheckBox box : team1_list)
			if (!box.isSelected()) {
				team1_checked = false;
				team1.setSelected(false);
			}
		if (team1_checked) {
			team1.setSelected(true);
			if (team1.isSelected() && team2.isSelected()) global.setSelected(true);
		}
		
		boolean team2_checked = true;
		for (JCheckBox box : team2_list)
			if (!box.isSelected()) {
				team2_checked = false;
				team2.setSelected(false);
			}
		if (team2_checked) {
			team2.setSelected(true);
			if (team1.isSelected() && team2.isSelected()) global.setSelected(true);
		}
	}
	
	public void writeChatMessage (String s) {
		output.setText(output.getText() + "\n" + s);
	}
	
	private String getSelectedPlayerIDs () {
		ArrayList<String> playerNames = new ArrayList<String>();
		for (JCheckBox box : team1_list)
			if (box.isSelected()) playerNames.add(box.getText());
		
		String s = "CHAT:";
		for (Map.Entry<Integer, PlayerModel> player : players.entrySet())
			for (String playerName : playerNames)
				if (player.getValue().playerName.equals(playerName))
					s += (player.getKey()) + ",";
		
		return s.substring(0, s.length() - 1) + ":";
	}
}