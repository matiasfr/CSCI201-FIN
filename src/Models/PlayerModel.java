import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	int playerLocationQuarter;
	boolean playerTeam;
	ArrayList<ItemModel> playerInventory;
	ArrayList<ImageIcon> playerSprites; // all possible images for animation
	ImageIcon playerImage; // current sprite
	
	int playerDirection;//0-up  1-right  2-down  3-left
	
	//lock on this object for the server
	Lock lock = new ReentrantLock();
	public PlayerModel(int playerID, String playerName, boolean playerTeam)
	{
		this.playerID = playerID;
		this.playerName = playerName;
		this.playerTeam = playerTeam;
		init();
	}
	public void init()
	{
		playerInventory = new ArrayList<ItemModel>();
		playerSprites = new ArrayList<ImageIcon>(); 
		
		//set health
		playerCurrentHealth = 100;
		playerTotalHealth = 100;
	}
	public void loadDefaults()
	{
		playerArmorPoints = 0;
		playerExperiencePoints = 0;
		playerExperienceLevel = 0;
		playerLocationX = 0;
		playerLocationY = 0;
		playerLocationQuarter = 0;
	}
	public void loadSprites()
	{
		
	}
	public void setArmor(int armor)
	{
		lock.lock();
		playerArmorPoints = armor;
		lock.unlock();
	}
	public void setExperience(int ex)
	{
		lock.lock();
		playerExperiencePoints += ex;
		lock.unlock();
	}
	public void setPostion(int dx, int dy)
	{
		lock.lock();
		playerLocationX += dx;
		playerLocationY += dy;
		lock.unlock();
	}
	public void setHealth(int health)//could be positive or negative
	{
		lock.lock();
		playerCurrentHealth += health;
		lock.unlock();
	}
	public void addItem(ItemModel item)
	{
		playerInventory.add(item);
	}
	public void setDirection(int dir)
	{
		lock.lock();
		playerDirection = dir;
		lock.unlock();
	}
}
