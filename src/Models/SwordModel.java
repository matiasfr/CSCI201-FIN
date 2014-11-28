package Models;

import javax.swing.ImageIcon;

public class SwordModel extends ItemModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -561261080992404691L;
	public int damage;
	public ImageIcon swordImage; // what it looks like on the map
	public double cooldownTime;
	public SwordModel(int damage, double cooldownTime)
	{
		this.damage = damage;
		this.cooldownTime = cooldownTime;
	}
}
