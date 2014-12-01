import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Models.PlayerModel;

public class ClientPlayer extends JPanel implements Runnable, MouseListener {
	private static final long serialVersionUID = -3929233179764541207L;
	BufferedImage imgPlayerSkeletonF = null;
	BufferedImage imgPlayerColorF = null;
	BufferedImage imgPlayerArmorF = null;
	BufferedImage imgPlayerArmF = null;
	BufferedImage imgPlayerSkeletonL = null;
	BufferedImage imgPlayerColorL = null;
	BufferedImage imgPlayerArmorL = null;
	BufferedImage imgPlayerArmL = null;
	BufferedImage imgPlayerSkeletonR = null;
	BufferedImage imgPlayerColorR = null;
	BufferedImage imgPlayerArmorR = null;
	BufferedImage imgPlayerArmR = null;
	BufferedImage imgPlayerSkeletonD = null;
	BufferedImage imgPlayerColorD = null;
	BufferedImage imgPlayerArmorD = null;
	BufferedImage imgPlayerArmD = null;
	
	BufferedImage forward[] = new BufferedImage[4];
	BufferedImage left[] = new BufferedImage[4];
	BufferedImage right[] = new BufferedImage[4];
	BufferedImage down[] = new BufferedImage[4];
	private ClientApplication myApp;
	private ClientDrawingPanel drawPanel;
	private int xSquare;
	private int ySquare;
	private int xPixel;
	private int yPixel;
	private int direction = 2;
	private int currentQuadrant = 9;
	@SuppressWarnings("unused")
	private String teamColor;
	int movementVar = 0;
	boolean quadrantChange = false;
	
	public ClientPlayer(ClientApplication myApp, String teamColor, ClientDrawingPanel drawPanel) {
		this.myApp = myApp;
		this.drawPanel = drawPanel;
		this.teamColor= teamColor;
		DrawKeyListener ls = new DrawKeyListener();
		myApp.addKeyListener(ls);
		myApp.setFocusable(true);
		this.drawPanel.addMouseListener(this);
		setVisible(true);
		setLayout(null);
		setLocation(0,0);
		setSize(450,450);
		setOpaque(false);
		
		xPixel = 0;
		yPixel = 0;
		xSquare = 0;
		ySquare = 0;

		try {
			forward[0] = ImageIO.read(new File("images/playerSkeleton/facing_forward.png"));
			forward[1] = ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_forward.png"));
			forward[2] = ImageIO.read(new File("images/armor/basic/facing_forward.png"));
			forward[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_forward.png"));
			left[0] = ImageIO.read(new File("images/playerSkeleton/facing_left.png"));
			left[1] = ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_left.png"));
			left[2] = ImageIO.read(new File("images/armor/basic/facing_left.png"));
			left[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_left.png"));
			right[0] = ImageIO.read(new File("images/playerSkeleton/facing_right.png"));
			right[1] = ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_right.png"));
			right[2] = ImageIO.read(new File("images/armor/basic/facing_right.png"));
			right[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_right.png"));
			down[0] = ImageIO.read(new File("images/playerSkeleton/facing_backwards.png"));
			down[1] = ImageIO.read(new File("images/teamColor/"+teamColor+"/facing_backwards.png"));
			down[2] = ImageIO.read(new File("images/armor/basic/facing_backwards.png"));
			down[3] = ImageIO.read(new File("images/arms/withoutWeapon/still/facing_backwards.png"));
		} catch(IOException e) {
			System.out.println(e.getStackTrace());
		}
	} // end public ClientPlayer(ClientApplication, String, ClientDrawingPanel) constructor
	
	public void run() {
		while(true) {
			//constantly checking the x and y coordinates to be able to update the position of the character on the 
			//grid map model. FOr instance, if a player is in the bounds x 
			try {
				Thread.sleep(5);
				
				if(ClientApplication.myGridMap.playerLookup.get(drawPanel.playerName).playerCurrentHealth <= 0) {
					break;
				}
				
				//sending quadrant movement to server
				if(direction == 0) { // up
					if(ySquare != 0 && ClientApplication.myGridMap.allModels[currentQuadrant][xSquare][ySquare-1] instanceof PlayerModel) {
						continue;
					} else if(yPixel <= 0) {
						continue;
					} else {
						yPixel -= movementVar;
						int deltaMovement = Math.abs(((int)(yPixel + 22.5) / 45) - ySquare);
						if(deltaMovement != 0) {
							ySquare = (int)(yPixel + 22.5) / 45;
							if(deltaMovement != 9) {
								myApp.sendServerMessage("U:1");	
							}
						}
					}
				} // end if direction is up
				else if(direction == 1) { // right
					if(xSquare != 9 && ClientApplication.myGridMap.allModels[currentQuadrant][xSquare+1][ySquare] instanceof PlayerModel) {
						continue;
					} else if(xPixel >= 405) {
						continue;
					} else {
						xPixel += movementVar;
						int deltaMovement = Math.abs(((int)(xPixel + 22.5) / 45) - xSquare);
						if(deltaMovement != 0) {
							xSquare = (int)(xPixel + 22.5) / 45;
							if(deltaMovement != 9) {
								myApp.sendServerMessage("R:1");	
							}
						}
					}
				} // end if direction is right
				else if(direction == 2) { // down
					if(ySquare != 9 && ClientApplication.myGridMap.allModels[currentQuadrant][xSquare][ySquare+1] instanceof PlayerModel) {
						continue;
					} else if(yPixel >= 405) {
						continue;
					} else {
						yPixel += movementVar;
						int deltaMovement = Math.abs(((int)(yPixel + 22.5) / 45) - ySquare);
						if(deltaMovement != 0) {
							ySquare = (int)(yPixel + 22.5) / 45;
							if(deltaMovement != 9) {
								myApp.sendServerMessage("D:1");	
							}
						}
					}
				} // end if direction is down
				else if(direction == 3) { // left
					if(xSquare != 0 && ClientApplication.myGridMap.allModels[currentQuadrant][xSquare-1][ySquare] instanceof PlayerModel) {
						continue;
					} else if(xPixel <= 0) {
						continue;
					} else {
						xPixel -= movementVar;
						int deltaMovement = Math.abs(((int)(xPixel + 22.5) / 45) - xSquare);
						if(deltaMovement != 0) {
							xSquare = (int)(xPixel + 22.5) / 45;
							if(deltaMovement != 9) {
								myApp.sendServerMessage("L:1");	
							}
						}
					}
				} // end if direction is left
				
				repaint();
				drawPanel.repaint();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		} // end while(true)
	} // end public void run
	
	public void setPlayerChange(String armorType, String weaponType) {
		try {
			forward[2] = ImageIO.read(new File(armorType));
			forward[3] = ImageIO.read(new File(weaponType));
			
			left[2] = ImageIO.read(new File(armorType));
			left[3] = ImageIO.read(new File(weaponType));
			
			right[2] = ImageIO.read(new File(armorType));
			right[3] = ImageIO.read(new File(weaponType));
			
			down[2] = ImageIO.read(new File(armorType));
			down[3] = ImageIO.read(new File(weaponType));
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end public void setPlayerChange(String, String)
	
	public void setX(int x) {
		this.xPixel = x;
	} // end public void setX(int)
	
	public void setY(int y) {
		this.yPixel = y;
	} // end public void setY(int)
	
	public void setXSq(int x) {
		this.xSquare = x;
	} // end public void setXSq(int)
	
	public void setYSq(int y) {
		this.ySquare = y;
	} // end public void setYSq(int)
	
	public void setQuadrant(int q) {
		this.currentQuadrant = q;
	} // end public void setQuadrant(int)
	
	public void setDirection(int d) {
		this.direction = d;
	} // end public void setDirection(int)
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(ClientApplication.myGridMap.playerLookup.get(drawPanel.playerName).playerCurrentHealth >= 0) {
			if(direction == 0) { //up
				for(int i = 0; i < 4; i++) {
					g.drawImage(down[i], xPixel, yPixel, null);
				}
			} // end if(direction == 0)
			else if(direction == 1) { // right
				for(int i = 0; i < 4; i++) {
					g.drawImage(right[i], xPixel, yPixel, null);
				}
			} // end if(direction == 1)
			else if(direction == 2) { // down
				for(int i = 0; i < 4; i++) {
					g.drawImage(forward[i], xPixel, yPixel, null);
				}
			} // end if(direction == 2)
			else if(direction == 3) { // left
				for(int i = 0; i < 4; i++) {
					g.drawImage(left[i], xPixel, yPixel, null);
				}
			} // end if(direction == 3)
		}
	} // end public void paintComponent(Graphics)
	
	class DrawKeyListener extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			movementVar = 0;
		} // end public void keyReleased(KeyEvent)
		
		public void keyPressed(KeyEvent e) {
			//Check the potential new position. If it is in a different square
			//if the new potential position is within 5 of a border, then check. Set quad change boolean to true; 
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if(xPixel >= 405) { // If you go past the right border
					if(currentQuadrant == 0 || currentQuadrant == 2) { // Change to next quadrant to the right 
						xPixel = 0;
						currentQuadrant++;
						drawPanel.currentQuadrant++;
						myApp.sendServerMessage("R:1");
					} else { // Don't do anything
						xPixel = 405;
					}
				} else {
					// If moving into a new square
					direction = 1;
					movementVar = 1;
				} // end if not past right border
			} // end if right key pressed
			else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(xPixel <= 0) { // If you go past left border
					if(currentQuadrant == 1 || currentQuadrant == 3) { // Change to the next quadrant to left
						xPixel = 405;
						currentQuadrant--;
						drawPanel.currentQuadrant--;
						myApp.sendServerMessage("L:1");
					} else { // Don't do anything
						xPixel = 0;
					}
				} else {
					direction = 3;
					movementVar = 1;
				} // end if not past left border
			} // end if left key pressed
			else if(e.getKeyCode() == KeyEvent.VK_UP) {
				if(yPixel <= 0) { // If you go past top border
					if(currentQuadrant == 2 || currentQuadrant == 3) { // Change to next quadrant up
						yPixel = 405;
						currentQuadrant -= 2;
						drawPanel.currentQuadrant -= 2;
						myApp.sendServerMessage("U:1");
					} else { // Don't do anything
						yPixel = 0;
					}
				} else {
					direction = 0;
					movementVar = 1;
				}
			} // end if up key pressed
			else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(yPixel >= 405) { // If you go past bottom border
					if(currentQuadrant == 0 || currentQuadrant == 1) { // Change to next quadrant down
						yPixel = 0;
						currentQuadrant += 2;
						drawPanel.currentQuadrant += 2;
						myApp.sendServerMessage("D:1");
					} else { // Don't do anything
						yPixel = 405;
					}
				} else {
					direction = 2;
					movementVar = 1;
				}
			} // end if down key pressed
			else if(e.getKeyCode() == KeyEvent.VK_A) {
				//ATTACK
				myApp.sendServerMessage("A:" + ClientApplication.myGridMap.playerLookup.get(drawPanel.playerName).playerExperiencePoints);
			} // end if 'A' key pressed
		} // end public void keyPressed(KeyEvent)
	} // end class DrawKeyListener extends KeyAdapter

	//// MouseListener methods ////
	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		myApp.requestFocusInWindow();
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

} // end public class ClientPlayer extends JPanel implements Runnable