package Models;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class HealthRefillModel extends ItemModel implements Serializable
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
