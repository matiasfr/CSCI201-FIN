package Models;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class ArmorModel extends ItemModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7664639009794107256L;
	
	public ImageIcon armorImage;  // what it looks like on the map
	public int armorPoints;
	public ArmorModel(int armorPoints)
	{
		this.armorPoints = armorPoints;
	}
}
