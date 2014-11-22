import javax.swing.ImageIcon;

public class ArmorModel extends ItemModel
{
	ImageIcon armorImage;  // what it looks like on the map
	int armorPoints;
	public ArmorModel(ImageIcon armorImage, int armorPoints)
	{
		this.armorImage = armorImage;
		this.armorPoints = armorPoints;
	}
}
