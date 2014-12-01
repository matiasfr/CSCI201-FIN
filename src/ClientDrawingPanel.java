import java.awt.*;

import javax.swing.*;

import java.io.*;
import java.awt.image.*;

import javax.imageio.*;

import Models.*;

class ClientDrawingPanel extends JPanel {
	private static final long serialVersionUID = 6563046194798024970L;
	public static BufferedImage image;
	private int currentQuadrant = 0;
	private String playerName = "";
	private Boolean firstDrawDone = false;
	public Boolean drawQuadChange = false;
	private ClientPlayer thisPlayer;
	private ClientApplication myApp;
	@SuppressWarnings("unused")
	private ClientGamePanel theGame;
	//IMAGES
	BufferedImage imgArmor = null;
	BufferedImage imgSword = null;
	BufferedImage imgHealth = null;
	BufferedImage backgroundImage[] = new BufferedImage[4];

	public ClientDrawingPanel(ClientApplication myApp, ClientGamePanel theGame) {
		this.myApp = myApp;
		this.theGame = theGame;
		try {
			//When we start these are the defaults.
			imgArmor = ImageIO.read(new File("images/items/awesome_sword.jpg"));
			imgSword = ImageIO.read(new File("images/items/awesome_sword.jpg"));
			imgHealth = ImageIO.read(new File("images/playerSkeleton/facing_forward.png"));

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
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				//need to check what kind of model is in the current index.
				if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] != null) {
					//if it is a player model, check the id, if not our own then
					if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof PlayerModel) {
						PlayerModel myPlayer = (PlayerModel)ClientApplication.myGridMap.allModels[currentQuadrant][i][j];
						//if the player names are not the same then we can draw them.
						if(!((myPlayer.playerName).equals(this.playerName))) {
							//go into the player model and get the player direction so we know which images to get
							@SuppressWarnings("unused")
							int playerDirection = myPlayer.playerDirection;
							//, and  playerImage image icon
							//go through the players image icons
							for(int k = 0; k < 4; k++) {
							//	g.drawImage(tile, i*45, j*45,null);
							}
						}
						else if(!firstDrawDone) {
							//draw our own player
							//but we should also start up the other player model to get the position of the player.
							PlayerModel gammaPlayer = (PlayerModel)ClientApplication.myGridMap.allModels[currentQuadrant][i][j];
							String teamColor = "";
							if(gammaPlayer.playerTeam == 1) {
								teamColor = "red";
							} else {
								teamColor = "green";
							}

							//also we should probably update the stats with this.
							thisPlayer = new ClientPlayer(myApp, teamColor, this);
							thisPlayer.setX(gammaPlayer.playerLocationX);
							thisPlayer.setY(gammaPlayer.playerLocationY);
							thisPlayer.setXSq(i);
							thisPlayer.setYSq(j);
							thisPlayer.setDirection(gammaPlayer.playerDirection);
							thisPlayer.setQuadrant(currentQuadrant);
							// GO through player sprite and draw the images.
							firstDrawDone = true;
							new Thread(thisPlayer).start();

							// Add player
							//Dimension playerDimension = new Dimension(45, 45);
							//thisPlayer.setBounds(gammaPlayer.playerLocationX, gammaPlayer.playerLocationY, 45, 45);
							add(thisPlayer);
						} else if(drawQuadChange) {
							PlayerModel gammaPlayer = (PlayerModel)ClientApplication.myGridMap.allModels[currentQuadrant][i][j];
							thisPlayer.setX(gammaPlayer.playerLocationX);
							thisPlayer.setY(gammaPlayer.playerLocationY);
							thisPlayer.setXSq(i);
							thisPlayer.setYSq(j);
							thisPlayer.setDirection(gammaPlayer.playerDirection);
							thisPlayer.setQuadrant(currentQuadrant);
							thisPlayer.setPlayerChange(gammaPlayer.playerSprite[2], gammaPlayer.playerSprite[3]);
							drawQuadChange = false;
						} else {
							//dont draw this player.
						}
					}
					//draw all of that player's images.

					//if it is a sword model, show an image of a sword
					else if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof SwordModel) {
						System.out.println("THERE SHOULD BE A SHARP POINTY THING");
						g.drawImage(imgSword, i * 45, j * 45, null);
					}
					//if it is a armor model, show an image of a armor
					else if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof ArmorModel) {
						g.drawImage(imgArmor, i * 45, j * 45, null);
					}
					//if it is a health model, show an image of a health
					else if(ClientApplication.myGridMap.allModels[currentQuadrant][i][j] instanceof HealthRefillModel) {
						g.drawImage(imgHealth, i * 45, j * 45, null);
					}
					else {

					}
				}
				//BufferedImage tile=ImageIO.read(new File(gmm[currentQuadrant][i][j]));
				//g.drawImage(tile, i*45, j*45,null);
			}
		}
		//Render GridMapModel
	} //end protected void paintComponent
} //end class ClientDrawingPanel