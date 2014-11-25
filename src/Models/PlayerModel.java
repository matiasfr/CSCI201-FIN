package Models;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;

public class PlayerModel extends AbstractObjectModel 
{

	public int playerID;
	public String playerName;
	public int playerCurrentHealth;
	public int playerTotalHealth;
	public int playerArmorPoints; // reduces damage taken by certain %
	public int playerExperiencePoints;
	public int playerExperienceLevel; // i.e. Level 3
	public int playerLocationX;
	public int playerLocationY;
	public int playerLocationQuarter;
	public int playerTeam;
	public ArrayList<ItemModel> playerInventory;
	public ArrayList<ImageIcon> playerSprites; // all possible images for animation
	public ImageIcon playerImage; // current sprite
	
	public int playerDirection;//0-up  1-right  2-down  3-left
	
	//lock on this object for the server
	Lock lock = new ReentrantLock();

	public PlayerModel(int playerID, String playerName, int playerTeam)
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
