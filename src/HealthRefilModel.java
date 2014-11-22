import javax.swing.ImageIcon;

public class HealthRefilModel extends ItemModel
{
	int healthPoints;
	ImageIcon healthRefillImage;
	public HealthRefilModel(int healthPoints, ImageIcon healthRefillImage)
	{
		this.healthPoints = healthPoints;
		this.healthRefillImage = healthRefillImage;
	}
}
