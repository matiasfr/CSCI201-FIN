import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClientStatsPanel extends JPanel {
	private static final long serialVersionUID = -8464325570681311400L;
	JLabel header;
	JLabel healthLabel;
	JLabel experienceLabel;
	JLabel weaponLabel;
	JLabel teamLabel;
	JLabel healthNum;
	JLabel weaponImage;

	public ClientStatsPanel() {
		setLayout(null);
		setSize(1000, 800);
		setBackground(Color.white);
		//// Game client ////
		//this.myClient = myClient;
		healthLabel = new JLabel("Health: 100");
		Dimension healthLabelDim = healthLabel.getPreferredSize();
		healthLabel.setBounds(50, 0, healthLabelDim.width, healthLabelDim.height);
		add(healthLabel);
		
		weaponLabel = new JLabel("Weapon: none");
		Dimension weaponLabelDim = healthLabel.getPreferredSize();
		weaponLabel.setBounds(120, 0, weaponLabelDim.width, weaponLabelDim.height);
		add(weaponLabel);
		
		experienceLabel = new JLabel("Experience: 100");
		Dimension experienceLabelDim = experienceLabel.getPreferredSize();
		experienceLabel.setBounds(200, 0, experienceLabelDim.width, experienceLabelDim.height);
		add(experienceLabel);
		
		teamLabel = new JLabel("Team:");
		Dimension teamLabelDim = teamLabel.getPreferredSize();
		teamLabel.setBounds(270, 0, teamLabelDim.width, teamLabelDim.height);
		add(teamLabel);
	} //end public ClientStatsPanel constructor
	//theGame.statsPanel.setCurrentWeapon( );
	//theGame.statsPanel.setCurrentHealth(gammaPlayer.playerCurrentHealth, gammaPlayer.playerTotalHealth );
	//theGame.statsPanel.setExperience(gammaPlayer.playerExperiencePoints, gammaPlayer.playerExperienceLevel);
	//theGame.statsPanel.setTeam(gammaPlayer.playerTeam);
	public void setTeam(int num) {
		String teamNum = Integer.toString(num);
		teamLabel.setText("Weapon: " + teamNum);
	} // end public void setTeam(int)
	
	public void setCurrentWeapon(String weapon) {
		weaponLabel.setText("Weapon: " + weapon);
	} // end public void setCurrentWeapon(String)
	
	public void setExperience(int xpLevel, int xpPoints) {
		String points = Integer.toString(xpPoints);
		String level = Integer.toString(xpLevel);
		experienceLabel.setText("XP Level:" + level + " XP Points:" + points);
	} // end public void setExperience(int, int)
	
	public void setCurrentHealth(int currentHealth, int totalHealth) {
		String curr = Integer.toString(currentHealth);
		String total = Integer.toString(totalHealth);
		healthLabel.setText("Health: " + curr + "/"  + total);
	} // end public void setCurrentHealth(int, int)
} //end class ClientStatsPanel