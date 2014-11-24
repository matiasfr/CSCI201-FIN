import java.awt.*;
import javax.swing.*;
import java.io.*; 
import java.awt.image.*; 
import javax.imageio.*; 

class ClientDrawingPanel extends JPanel implements Runnable {
	public static BufferedImage image; 
	
	public ClientDrawingPanel(){
			JLabel my = new JLabel("dlg");
			
			add(my);
	
			System.out.println("dog");
			
			
			 try 
			    {                
			      image = ImageIO.read(new File("src/press.png")); 
			      
			      
			    } 
			    catch (IOException e) 
			    { 
			      //Not handled. 
			    } 
	}
	
	public void run(){
		
		//We need to wait for the server to start sending something to us.
		
		//then we have to take the object that the server sends to us and deserialize it. 
		
		
		
	}
	
	protected void paintComponent(Graphics g) {   
	
		//super.paint(g);

		g.drawImage(image, 200, 0, null);
		g.drawImage(image, 400, 0, null);
				//Render GridMapModel
	} //end protected void paintComponent
			
} //end class ClientDrawingPanel