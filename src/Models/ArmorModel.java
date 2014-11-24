package Models;

import javax.swing.ImageIcon;

public class ArmorModel extends ItemModel
{
	ImageIcon armorImage;  // what it looks like on the map
	public int armorPoints;
	public ArmorModel(int armorPoints)
	{
		this.armorPoints = armorPoints;
	}
}
