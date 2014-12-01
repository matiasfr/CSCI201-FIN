import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientLoginPanel extends JPanel {
	private static final long serialVersionUID = -5826597878789842372L;
	JLabel usernameLabel;
	JLabel passwordLabel;
	JTextField usernameInput;
	JTextField passwordInput;
	JButton loginButton;
	JButton createAccountButton;
	ObjectOutputStream oos;
	Container frame;
	BufferedImage backgroundImage;

	public ClientLoginPanel(final ObjectOutputStream oos) {
		frame = this.getParent();
		this.oos = oos;
		try {
			backgroundImage = ImageIO.read(new File("images/Login.png"));
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		JPanel centerPane = new JPanel() {
			private static final long serialVersionUID = -9099767103509630898L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(backgroundImage, 0, 0, null);
			}
		};
		setLayout(new BorderLayout());
		
		centerPane.setLayout(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		usernameLabel = new JLabel("Username: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		centerPane.add(usernameLabel, cs);

		usernameInput = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		centerPane.add(usernameInput, cs);
		centerPane.setBorder(BorderFactory.createLineBorder(Color.black));

		/*passwordLabel = new JLabel("Password: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		centerPane.add(passwordLabel, cs);
		passwordInput = new JPasswordField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		centerPane.add(passwordInput, cs);
		centerPane.setBorder(new LineBorder(Color.GRAY));*/

		loginButton = new JButton("Login/Connect");
		
		this.add(centerPane, BorderLayout.CENTER);
		this.add(loginButton, BorderLayout.SOUTH);
		
		
		
		loginButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	String str = usernameInput.getText();
	            	while(true) {
	            		if(!str.equals("")) {
	            			try {
								oos.writeObject(str);
							} catch(IOException e1) {
								e1.printStackTrace();
							}
	            			break;
	            		}
	            		else {
	            			JOptionPane.showMessageDialog(frame, "Username Field can't be less than 4 characters");
	            			usernameInput.setText("");
	            			continue;
	            		}
	            	} // end while(true)
	            } // end public void actionPerformed(ActionEvent)
	        }); // end loginButton.addActionListener(new ActionListener))
	} // end public ClientLoginPanel(ObjectOutputStream) constructor
} // end public class ClientLoginPanel extends JPanel