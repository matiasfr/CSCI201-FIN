package Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;

public class GridMapModel implements Serializable
{
	public AbstractObjectModel[][][] allModels;
	
	//for quick lookup of id to player
	public Map<Integer, PlayerModel> playerLookup;
	
	ImageIcon[] backgrounds = new ImageIcon[4];
	
	Lock lock = new ReentrantLock();
	public GridMapModel()
	{
		allModels = new AbstractObjectModel[4][10][10];
		playerLookup = new HashMap<Integer, PlayerModel>();
	}
	public void moveObjects(int level1, int sx, int sy, int level2,int dx,int dy)
	{
		lock.lock();
		PlayerModel pm = (PlayerModel) allModels[level1][sx][sy];
		allModels[level2][dx][dy] = pm;
		allModels[level1][sx][sy] = null;
		lock.unlock();
	}

	public Map<Integer, PlayerModel> getPlayers()
	{
		return playerLookup;
	}
}
