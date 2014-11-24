package Models;

import javax.swing.ImageIcon;

public class HealthRefillModel extends ItemModel
{
	public int healthPoints;
	public ImageIcon healthRefillImage;
	public HealthRefillModel(int healthPoints)
	{
		this.healthPoints = healthPoints;
	}
}
