import java.io.Serializable;
import javax.swing.ImageIcon;

public class GridMapModel implements Serializable
{
	AbstractObjectModel[][][] allModels = new AbstractObjectModel[4][10][10];
	ImageIcon[] backgrounds = new ImageIcon[4];
}
