import java.awt.*;
import javax.swing.*;
import java.io.*; 
import java.awt.image.*; 
import javax.imageio.*; 


class ClientDrawingPanel extends JPanel {
	public static BufferedImage image; 
	private GridMapModel gmm;
	
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

	int xpos;
	int ypos;
	int direction=2;
	
	public ClientDrawingPanel(GridMapModel gmm){
		
		
			this.gmm = gmm;	
			//listenForKey ls = new listenForKey();
		//	addKeyListener(ls);
			xpos=0;
			ypos=0;

			try {
					image = ImageIO.read(new File("src/gameview.png")); 
					forward[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_forward.png"));
					forward[1] = ImageIO.read(new File("image stuff/teamColor/red/facing_forward.png"));
					forward[2] = ImageIO.read(new File("image stuff/armor/basic/facing_forward.png"));
					forward[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_forward.png"));
					left[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_left.png"));
					left[1] = ImageIO.read(new File("image stuff/teamColor/red/facing_left.png"));
					left[2] = ImageIO.read(new File("image stuff/armor/basic/facing_left.png"));
					left[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_left.png"));
					right[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_right.png"));
					right[1] = ImageIO.read(new File("image stuff/teamColor/red/facing_right.png"));
					right[2] = ImageIO.read(new File("image stuff/armor/basic/facing_right.png"));
					right[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_right.png"));
					down[0] = ImageIO.read(new File("image stuff/playerSkeleton/facing_backwards.png"));
					down[1] = ImageIO.read(new File("image stuff/teamColor/red/facing_backwards.png"));
					down[2] = ImageIO.read(new File("image stuff/armor/basic/facing_backwards.png"));
					down[3] = ImageIO.read(new File("image stuff/arms/withoutWeapon/still/facing_backwards.png"));
				} catch (IOException e) {}
			
	}
	

	public void refreshMap(GridMapModel gmm){
		this.gmm = gmm;
	}
	
	protected void paintComponent(Graphics g) {   
	
		super.paintComponent(g);

		g.drawImage(image, 0, 0, null);
		//g.drawImage(image, 0, 0, null);
		
		
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
	
				//Render GridMapModel
	} //end protected void paintComponent
			
} //end class ClientDrawingPanel

