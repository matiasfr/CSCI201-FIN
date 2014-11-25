package Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;

public class GridMapModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5209465409547936302L;

	public AbstractObjectModel[][][] allModels;
	
	//for quick lookup of id to player
	public Map<Integer, PlayerModel> playerLookup;
	
	public ImageIcon[] backgrounds = new ImageIcon[4];
	
	public Lock lock = new ReentrantLock();
	public GridMapModel()
	{
		allModels = new AbstractObjectModel[4][10][10];
		playerLookup = new HashMap<Integer, PlayerModel>();
	}
	public void moveObjects(int level, int sx, int sy, int dx,int dy)
	{
		lock.lock();
		PlayerModel pm = (PlayerModel) allModels[level][sx][sy];
		allModels[level][dx][dy] = pm;
		allModels[level][sx][sy] = null;
		lock.unlock();
	}
}
