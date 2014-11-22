import java.util.ArrayList;
import javax.swing.ImageIcon;

public class PlayerModel extends AbstractObjectModel 
{
	int playerID;
	String playerName;
	int playerCurrentHealth;
	int playerTotalHealth;
	int playerArmorPoints; // reduces damage taken by certain %
	int playerExperiencePoints;
	int playerExperienceLevel; // i.e. Level 3
	int playerLocationX;
	int playerLocationY;
	ArrayList<ItemModel> playerInventory;
	ArrayList<ImageIcon> playerSprites; // all possible images for animation
	ImageIcon playerImage; // current sprite
	
	public PlayerModel(int playerID, String playerName)
	{
		this.playerID = playerID;
		this.playerName = playerName;
	}
	
	public void init()
	{
		playerInventory = new ArrayList<ItemModel>();
		playerSprites = new ArrayList<ImageIcon>(); 
	}
	public void loadDefaults()
	{
		
	}
	public void loadSprites()
	{
		
	}
	public void setArmor()
	{
		
	}
	public void setExperience()
	{
		
	}
}
