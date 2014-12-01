import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Models.ItemModel;

public class ClientStatsPanel extends JPanel {
	private static final long serialVersionUID = -8464325570681311400L;
	JLabel header;
	JLabel healthLabel;
	JLabel experienceLabel;
	JLabel weaponLabel;
	JLabel teamLabel;
	JLabel healthNum;
	JLabel armorLabel;
	JLabel weaponImage;
	
	int currHealth;
	static final int totalHealth = 100;
	
	ArrayList<ItemModel> inventory;

	public ClientStatsPanel() {
		setLayout(null);
		setPreferredSize(new Dimension(150, 475));
		setBackground(Color.white);
		//// Game client ////
		header = new JLabel("Name: ");
		
		healthLabel = new JLabel("Health: 100");
		
		weaponLabel = new JLabel("Weapon: none");
		
		armorLabel = new JLabel("Armor Points: 1");
		
		experienceLabel = new JLabel("Experience: 0");
		
		teamLabel = new JLabel("Team:");
		
		refreshStats();
		setVisible(true);
	} //end public ClientStatsPanel constructor
	//theGame.statsPanel.setCurrentWeapon( );
	//theGame.statsPanel.setExperience(gammaPlayer.playerExperiencePoints, gammaPlayer.playerExperienceLevel);
	public void setName(String playerName) {
		header.setText("Name: " + playerName);
	}

	public void setTeam(int num) {
		String teamName = (num == 1) ? "Red" : "Green";
		teamLabel.setText("Team: " + teamName);
	} // end public void setTeam(int)
	
	public void setCurrentWeapon(String weapon) {
		weaponLabel.setText("Weapon: " + weapon);
	} // end public void setCurrentWeapon(String)
	
	public void setArmorPoints(int armorPoints) {
		armorLabel.setText("Armor Points: " + armorPoints);
	}
	
	public void setExperience(int xpPoints) {
		String points = Integer.toString(xpPoints);
		experienceLabel.setText(" XP Points:" + points);
	} // end public void setExperience(int, int)
	
	public void setCurrentHealth(int currentHealth) {
		String curr = Integer.toString(currentHealth);
		String total = Integer.toString(totalHealth);
		healthLabel.setText("Health: " + curr + "/"  + total);
	} // end public void setCurrentHealth(int, int)
	
	public void refreshStats() {
		Dimension headerDim = header.getPreferredSize();
		header.setBounds(0, 0, headerDim.width, headerDim.height);
		add(header);

		Dimension healthLabelDim = healthLabel.getPreferredSize();
		healthLabel.setBounds(0, 50, healthLabelDim.width, healthLabelDim.height);
		add(healthLabel);
		
		Dimension weaponLabelDim = healthLabel.getPreferredSize();
		weaponLabel.setBounds(0, 120, weaponLabelDim.width, weaponLabelDim.height);
		add(weaponLabel);
		
		Dimension armorDim = armorLabel.getPreferredSize();
		armorLabel.setBounds(0, 150, armorDim.width, armorDim.height);
		add(armorLabel);
		
		Dimension experienceLabelDim = experienceLabel.getPreferredSize();
		experienceLabel.setBounds(0, 200, experienceLabelDim.width, experienceLabelDim.height);
		add(experienceLabel);
		
		Dimension teamLabelDim = teamLabel.getPreferredSize();
		teamLabel.setBounds(0, 270, teamLabelDim.width, teamLabelDim.height);
		add(teamLabel);
		
		repaint();
	}
} //end class ClientStatsPanel