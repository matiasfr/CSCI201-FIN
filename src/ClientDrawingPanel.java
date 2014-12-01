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
	private String playerName = "";
	private Boolean firstDrawDone = false;
	public Boolean drawQuadChange = false;
	private ClientPlayer thisPlayer;
	private ClientApplication myApp;
	//IMAGES
	BufferedImage imgArmor = null;
	BufferedImage imgSword = null;
	BufferedImage imgHealth = null;
	BufferedImage backgroundImage[] = new BufferedImage[4];

	public ClientDrawingPanel(ClientApplication myApp, ClientGamePanel theGame) {
		this.myApp = myApp;

		try {
			//When we start these are the defaults.
			imgArmor = ImageIO.read(new File("images/armor/bronze/facing_forward.png"));
			imgSword = ImageIO.read(new File("images/items/sword.png"));
			imgHealth = ImageIO.read(new File("images/items/health.png"));

			backgroundImage[0] = ImageIO.read(new File("images/quad1.png"));
			backgroundImage[1] = ImageIO.read(new File("images/quad2.jpg"));
			backgroundImage[2] = ImageIO.read(new File("images/quad3.png"));
			backgroundImage[3] = ImageIO.read(new File("images/quad4.png"));
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
		g.drawImage(backgroundImage[currentQuadrant], 0, 0, null);
		
		// Go through GridMapModel and fill in map as necessary
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
							for(int k = 0; k < 4; k++) {
								//TODO:  Draw other players
							}
						} else {
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
								thisPlayer.setQuadrant(currentQuadrant);
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
		//Render GridMapModel
	} //end protected void paintComponent
} //end class ClientDrawingPanel