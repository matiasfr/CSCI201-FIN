import javax.swing.ImageIcon;

public class SwordModel extends ItemModel
{
	int damage;
	ImageIcon swordImage; // what it looks like on the map
	double cooldownTime;
	public SwordModel(int damage, ImageIcon swordImage, double cooldownTime)
	{
		this.damage = damage;
		this.swordImage = swordImage;
		this.cooldownTime = cooldownTime;
	}
}
