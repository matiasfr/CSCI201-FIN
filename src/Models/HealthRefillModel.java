package Models;

import javax.swing.ImageIcon;

public class HealthRefillModel extends ItemModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4852485251347258619L;
	public int healthPoints;
	public ImageIcon healthRefillImage;
	public HealthRefillModel(int healthPoints)
	{
		this.healthPoints = healthPoints;
	}
}
