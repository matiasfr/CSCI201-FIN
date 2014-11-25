import java.awt.*;

import javax.swing.*;

import java.io.*; 
import java.awt.image.*; 

import javax.imageio.*; 
import Models.*;

class ClientDrawingPanel extends JPanel {
	public static BufferedImage image; 
	private GridMapModel gmm;
	private int currentQuadrant=0;
	private String playerName="";
	
	//IMAGES
	BufferedImage imgArmor = null;
	BufferedImage imgSword = null;
	BufferedImage imgHealth = null;
	
	
	public ClientDrawingPanel(GridMapModel gmm){
		this.gmm = gmm;	
		try{
			imgArmor = ImageIO.read(new File("image stuff/playerSkeleton/facing_forward.png"));
			imgSword = ImageIO.read(new File("image stuff/playerSkeleton/facing_forward.png"));
			imgHealth = ImageIO.read(new File("image stuff/playerSkeleton/facing_forward.png"));
		}catch (IOException e) {}
		
	}
	
	public void setName(String playerName){
		this.playerName= playerName;
	}
	
	public void refreshMap(GridMapModel gmm){

		this.gmm = gmm;
		repaint();
	}
	
	public void setQuadrant(int n){
		this.currentQuadrant=n;
	}
	
	protected void paintComponent(Graphics g) {   
	
		super.paintComponent(g);

		for(int i=0; i<10; i++){
			for( int j= 0; j<10; j++){
				
				//need to check what kind of model is in the current index. 
				
				//if it is a player model, check the id, if not our own then 
				if(gmm.allModels[currentQuadrant][i][j] instanceof PlayerModel){
					PlayerModel myPlayer=(PlayerModel) gmm.allModels[currentQuadrant][i][j];
					//if the player names are not the same then we can draw them. 
					if(! ((myPlayer.playerName).equals(this.playerName)) ){
						//go into the player model and get the player direction so we know which images to get
						int playerDirection = myPlayer.playerDirection;
						//, and  playerImage image icon 
						//go through the players image icons 
						for(int k=0; k<4; k++){
							g.drawImage(tile, i*45, j*45,null);
						}
					}
					else if (firstDrawDone){
						//draw our own player
						//but we should also start up the other player model to get the position of the player. 
						PlayerModel gammaPlayer=(PlayerModel) gmm.allModels[currentQuadrant][i][j];
						//also we should probably update the stats with this. 
						ClientPlayer anotherPlayer = new ClientPlayer();
						anotherPlayer.xpos= gammaPlayer.playerLocationX;
						anotherPlayer.ypos= gammaPlayer.playerLocationY;
						anotherPlayer.direction= gammaPlayer.playerDirection;
						firstDrawDone=true;
						new Thread(anotherPlayer).start();
					}
					
					
				}
				//draw all of that player's images. 
				
				//if it is a sword model, show an image of a sword
				if(gmm.allModels[currentQuadrant][i][j] instanceof SwordModel){
					g.drawImage(imgSword, i*45, j*45,null);
				}
				//if it is a armor model, show an image of a armor
				if(gmm.allModels[currentQuadrant][i][j] instanceof ArmorModel){
					g.drawImage(imgArmor, i*45, j*45,null);
				}
				//if it is a health model, show an image of a health
				if(gmm.allModels[currentQuadrant][i][j] instanceof HealthRefillModel){
					g.drawImage(imgHealth, i*45, j*45,null);
				}	
				
				//BufferedImage tile=ImageIO.read(new File(gmm[currentQuadrant][i][j]));
				//g.drawImage(tile, i*45, j*45,null);
			}
		}
	
				//Render GridMapModel
	} //end protected void paintComponent
			
} //end class ClientDrawingPanel

