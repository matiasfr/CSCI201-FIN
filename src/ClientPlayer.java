import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientPlayer extends JPanel implements Runnable{
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
	BufferedImage left[]=new BufferedImage[4];
	BufferedImage right[]=new BufferedImage[4];
	BufferedImage down[]=new BufferedImage[4];
	private ClientApplication myApp;
	int xpos;
	int ypos;
	int quadrant=9;
	int direction=2;
	public ClientPlayer(ClientApplication myApp){
		this.myApp = myApp;
		DrawKeyListener ls = new DrawKeyListener();
		addKeyListener(ls);
		setVisible(true);
		setLayout(null);
		
		xpos=0;
		ypos=0;
		try {
			forward[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_forward.png"));
			forward[1] = ImageIO.read(new File("image stuff/teamColor/red/facing_forward.png"));
			forward[2]  = ImageIO.read(new File("image stuff/armor/basic/facing_forward.png"));
			forward[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_forward.png"));
			left[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_left.png"));
			left[1] = ImageIO.read(new File("image stuff/teamColor/red/facing_left.png"));
			left[2] = ImageIO.read(new File("image stuff/armor/basic/facing_left.png"));
			left[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_left.png"));
			right[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_right.png"));
			right[1]= ImageIO.read(new File("image stuff/teamColor/red/facing_right.png"));
			right[2] = ImageIO.read(new File("image stuff/armor/basic/facing_right.png"));
			right[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_right.png"));
			down[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_backwards.png"));
			down[1] = ImageIO.read(new File("image stuff/teamColor/red/facing_backwards.png"));
			down[2] = ImageIO.read(new File("image stuff/armor/basic/facing_backwards.png"));
			down[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_backwards.png"));
		} catch (IOException e) {}
	}
	
	public void run(){
		while(true){
			
			//constantly checking the x and y coordinates to be able to update the position of the character on the 
			//grid map model. FOr instance, if a player is in the bounds x 
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(direction==0)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(down[i], xpos, ypos,null);
			}
		}
		else if(direction==1)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(right[i], xpos, ypos,null);
			}
		}
		else if(direction==2)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(forward[i], xpos, ypos,null);
			}
		}
		else if(direction==3)
		{
			for(int i=0;i<4;i++)
			{
				g.drawImage(left[i], xpos, ypos,null);
			}
		}
	}
	
	

	class DrawKeyListener extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
	            //Right arrow key code
				//SEND THE SERVER THE MESSAGE R
				myApp.sendServerMessage("R:");
				direction = 1;
				xpos+=5;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT ) {
	            //Left arrow key code
				myApp.sendServerMessage("L:");
				direction = 3;
				xpos-=5;
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				myApp.sendServerMessage("U:");
	            //Up arrow key code
				direction = 0;
				ypos-=5;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN ) {
				myApp.sendServerMessage("D:");
	            //Down arrow key code
				direction = 2;
				ypos+=5;
			} else if (e.getKeyCode() == KeyEvent.VK_A ) {
				//ATTACK
				myApp.sendServerMessage("A:");

			}
		}
		
		
	}
}
