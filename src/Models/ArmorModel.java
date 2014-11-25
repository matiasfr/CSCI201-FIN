package Models;

import javax.swing.ImageIcon;

public class ArmorModel extends ItemModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7664639009794107256L;
	
	ImageIcon armorImage;  // what it looks like on the map
	public int armorPoints;
	public ArmorModel(int armorPoints)
	{
		this.armorPoints = armorPoints;
	}
}
