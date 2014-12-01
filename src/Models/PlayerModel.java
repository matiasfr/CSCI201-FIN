package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PlayerModel extends AbstractObjectModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1122915319597978830L;
	static final String FOLDERS[] = {"armor/","arms/","playerSkeleton/","teamColor/"};
	static final String ARMOR[] = {"basic/","bronze/","platinum/"};
	static final String ARMS[] = {"withWeapon/","withoutWeapon/"};
	static final String TEAM_COLOR[] = {"red/","green/"};
	static final String WEAPON[] = {"still/","animated/"};
	static final String DIRECTION[] = {"facing_backwards.png","facing_right.png","facing_forwards.png","facing_left.png"};
	
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
	public int playerTeam;// 1 or 2
	public ArrayList<ItemModel> playerInventory;
	//public ArrayList<String> playerSprite; // image path for animation
	public String playerSprite[]; //0->skeleton 1->color 2->armor 3->arms
	
	public int playerDirection;//0-up  1-right  2-down  3-left
	
	//lock on this object for the server
	Lock lock = new ReentrantLock();
	public PlayerModel(int playerID, String playerName, int playerTeam)
	{
		this.playerID = playerID;
		this.playerName = playerName;
		this.playerTeam = playerTeam;
		init();
		loadDefaults();
	}
	public void init()
	{
		playerInventory = new ArrayList<ItemModel>();
		playerSprite = new String[4]; 
	}
	public void loadDefaults()
	{
		playerArmorPoints = 1;
		playerExperiencePoints = 0;
		playerExperienceLevel = 0;
		playerLocationX = 0;
		playerLocationY = 0;
		playerLocationQuarter = 0;
		
		//set health
		playerCurrentHealth = 100;
		playerTotalHealth = 100;
		
		playerSprite[0] = FOLDERS[0]+DIRECTION[0];
		playerSprite[1] = FOLDERS[1]+TEAM_COLOR[playerTeam-1]+DIRECTION[0];
		playerSprite[2] = FOLDERS[2]+ARMOR[0]+DIRECTION[0];
		playerSprite[3] = FOLDERS[3]+ARMS[0]+WEAPON[0]+DIRECTION[0];
	}
	public void setArmor(int armor)
	{
		lock.lock();
		playerArmorPoints += armor;
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
		playerSprite[0] = FOLDERS[0]+DIRECTION[dir];
		playerSprite[1] = FOLDERS[1]+TEAM_COLOR[playerTeam-1]+DIRECTION[dir];
		playerSprite[2] = FOLDERS[2]+ARMOR[0]+DIRECTION[dir];
		playerSprite[3] = FOLDERS[3]+ARMS[0]+WEAPON[0]+DIRECTION[dir];
		lock.unlock();
	}
}
