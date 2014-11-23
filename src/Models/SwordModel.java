package Models;
import javax.swing.ImageIcon;

public class SwordModel extends ItemModel
{
	int damage;
	ImageIcon swordImage; // what it looks like on the map
	double cooldownTime;
	public SwordModel(int damage, double cooldownTime)
	{
		this.damage = damage;
		this.cooldownTime = cooldownTime;
	}
}
