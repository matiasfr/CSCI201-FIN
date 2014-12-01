import java.awt.*;

import javax.swing.*;

import java.io.*;
import java.awt.image.*;

import javax.imageio.*;

import Models.*;

class ClientDrawingPanel extends JPanel {
	private static final long serialVersionUID = 6563046194798024970L;
	public static BufferedImage image;
	public int currentQuadrant = 0;
	public String playerName = "";
	private Boolean firstDrawDone = false;
	public Boolean drawQuadChange = false;
	private ClientPlayer thisPlayer;
	private ClientApplication myApp;
	private ClientGamePanel theGame;
	//IMAGES
	BufferedImage imgArmor = null;
	BufferedImage imgSword = null;
	BufferedImage imgHealth = null;
	BufferedImage backgroundImage[] = new BufferedImage[4];

	BufferedImage forwardGreen[] = new BufferedImage[4];
	BufferedImage leftGreen[] = new BufferedImage[4];
	BufferedImage rightGreen[] = new BufferedImage[4];
	BufferedImage downGreen[] = new BufferedImage[4];

	BufferedImage forwardRed[] = new BufferedImage[4];
	BufferedImage leftRed[] = new BufferedImage[4];
	BufferedImage rightRed[] = new BufferedImage[4];
	BufferedImage downRed[] = new BufferedImage[4];
	
	BufferedImage doneImage;

	public ClientDrawingPanel(ClientApplication myApp, ClientGamePanel theGame) {
		this.myApp = myApp;
		this.theGame = theGame;

		try {
			//When we start these are the defaults.
			imgArmor = ImageIO.read(new File("images/armor/bronze/facing_forward.png"));
			imgSword = ImageIO.read(new File("images/items/sword.png"));
			imgHealth = ImageIO.read(new File("images/items/health.png"));

			backgroundImage[0] = ImageIO.read(new File("images/quad1.png"));
			backgroundImage[1] = ImageIO.read(new File("images/quad2.png"));
			backgroundImage[2] = ImageIO.read(new File("images/quad3.png"));
			backgroundImage[3] = ImageIO.read(new File("images/quad4.png"));

			forwardGreen[0] = ImageIO.read(new File("images/playerSkeleton/facing_forward.png"));
			forwardGreen[1] = ImageIO.read(new File("images/teamColor/green/facing_forward.png"));
			forwardGreen[2] = ImageIO.read(new File("images/armor/basic/facing_forward.png"));
			forwardGreen[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_forward.png"));
			leftGreen[0] = ImageIO.read(new File("images/playerSkeleton/facing_left.png"));
			leftGreen[1] = ImageIO.read(new File("images/teamColor/green/facing_left.png"));
			leftGreen[2] = ImageIO.read(new File("images/armor/basic/facing_left.png"));
			leftGreen[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_left.png"));
			rightGreen[0] = ImageIO.read(new File("images/playerSkeleton/facing_right.png"));
			rightGreen[1] = ImageIO.read(new File("images/teamColor/green/facing_right.png"));
			rightGreen[2] = ImageIO.read(new File("images/armor/basic/facing_right.png"));
			rightGreen[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_right.png"));
			downGreen[0] = ImageIO.read(new File("images/playerSkeleton/facing_backwards.png"));
			downGreen[1] = ImageIO.read(new File("images/teamColor/green/facing_backwards.png"));
			downGreen[2] = ImageIO.read(new File("images/armor/basic/facing_backwards.png"));
			downGreen[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_backwards.png"));

			forwardRed[0] = ImageIO.read(new File("images/playerSkeleton/facing_forward.png"));
			forwardRed[1] = ImageIO.read(new File("images/teamColor/red/facing_forward.png"));
			forwardRed[2] = ImageIO.read(new File("images/armor/basic/facing_forward.png"));
			forwardRed[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_forward.png"));
			leftRed[0] = ImageIO.read(new File("images/playerSkeleton/facing_left.png"));
			leftRed[1] = ImageIO.read(new File("images/teamColor/red/facing_left.png"));
			leftRed[2] = ImageIO.read(new File("images/armor/basic/facing_left.png"));
			leftRed[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_left.png"));
			rightRed[0] = ImageIO.read(new File("images/playerSkeleton/facing_right.png"));
			rightRed[1] = ImageIO.read(new File("images/teamColor/red/facing_right.png"));
			rightRed[2] = ImageIO.read(new File("images/armor/basic/facing_right.png"));
			rightRed[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_right.png"));
			downRed[0] = ImageIO.read(new File("images/playerSkeleton/facing_backwards.png"));
			downRed[1] = ImageIO.read(new File("images/teamColor/red/facing_backwards.png"));
			downRed[2] = ImageIO.read(new File("images/armor/basic/facing_backwards.png"));
			downRed[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_backwards.png"));
			
			doneImage = ImageIO.read(new File("images/GameOver.jpg"));
		} catch(IOException e) {
			System.out.println(e.getStackTrace());
		}

		setSize(450,450);
		setLayout(null);
	} // end public ClientDrawingPanel(ClientApplication, ClientGamePanel) constructor

	public void setName(String playerName) {
		this.playerName = playerName;
	} // end public void setName(String)

	public void refreshMap() {
		repaint();
	} // end public void refreshMap

	public void setQuadrant(int n) {
		this.currentQuadrant = n;
	} // end public void setQuadrant(int)

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		//draw the background for the current quadrant.
		if(!firstDrawDone) {
			setQuadrant(ClientApplication.myGridMap.playerLookup.get(playerName).playerLocationQuarter);
		}
		if(firstDrawDone) {
			if(ClientApplication.myGridMap.playerLookup.get(playerName).playerCurrentHealth >= 0) {
				g.drawImage(backgroundImage[currentQuadrant], 0, 0, null);
			} else {
				g.drawImage(doneImage, 0, 0, null);
			}
		}

		// Go through GridMapModel and fill in map as necessary
		if(ClientApplication.myGridMap.playerLookup.get(playerName).playerCurrentHealth >= 0) {
		for(int i = 0; i < 10; i++) { // loop through x of GridMapModel
			for(int j = 0; j < 10; j++) { // loop through y of GridMapModel
				// Check if the current index holds a model
				if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] != null) {
					// If model exists check if it's a PlayerModel
					if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof PlayerModel) {
						// Get the PlayerModel
						PlayerModel playerModel = (PlayerModel)ClientApplication.myGridMap.allModels[currentQuadrant][i][j];
						int playerDirection = playerModel.playerDirection;

						// If not our player we draw them
						if(!playerModel.playerName.equals(this.playerName)) {
							if(playerModel.playerCurrentHealth >= 0) {
							for(int k = 0; k < 4; k++) {
								if(playerModel.playerTeam == 1) {
									g.drawImage(forwardRed[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
									g.drawImage(leftRed[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
									g.drawImage(rightRed[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
									g.drawImage(downRed[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
								} else {
									g.drawImage(forwardGreen[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
									g.drawImage(leftGreen[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
									g.drawImage(rightGreen[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
									g.drawImage(downGreen[k], playerModel.playerLocationX * 45, playerModel.playerLocationY * 45, null);
								}
							}
							}
						} else {
							// Refresh stats panel information
							theGame.statsPanel.setTeam(playerModel.playerTeam);
							theGame.statsPanel.setName(this.playerName);
							if(playerModel.playerCurrentHealth >= 0) {
								theGame.statsPanel.setCurrentHealth(playerModel.playerCurrentHealth);
							}
							theGame.statsPanel.setArmorPoints(playerModel.playerArmorPoints);
							theGame.statsPanel.setExperience(playerModel.playerExperiencePoints);
							theGame.statsPanel.refreshStats();

							// If our player isn't drawn yet, make him
							if(!firstDrawDone) {
								String teamColor = "";
								if(playerModel.playerTeam == 1) {
									teamColor = "red";
								} else {
									teamColor = "green";
								}
								
								// Create our player
								thisPlayer = new ClientPlayer(myApp, teamColor, this);
								thisPlayer.setX(playerModel.playerLocationX);
								thisPlayer.setY(playerModel.playerLocationY);
								thisPlayer.setXSq(i);
								thisPlayer.setYSq(j);
								thisPlayer.setDirection(playerDirection);
								thisPlayer.setQuadrant(playerModel.playerLocationQuarter);
								
								// Go through player sprite and draw the images.
								firstDrawDone = true;
								new Thread(thisPlayer).start();

								// Add player to panel
								add(thisPlayer);
							} else {
								continue;
							}
						}
					}

					else if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof SwordModel) {
						g.drawImage(imgSword, i * 45, j * 45, null);
					}
					else if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof ArmorModel) {
						g.drawImage(imgArmor, i * 45, j * 45, null);
					}
					else if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof HealthRefillModel) {
						g.drawImage(imgHealth, i * 45, j * 45, null);
					}
					else {

					}
				} // end if there is somthing in this square
			} // end loop through y
		} // end loop through x
		}
		//Render GridMapModel
	} //end protected void paintComponent
} //end class ClientDrawingPanel