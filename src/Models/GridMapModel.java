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
	private static final long serialVersionUID = 834616762513297878L;

	public AbstractObjectModel[][][] allModels;
	
	//for quick lookup of id to player, has a getter method
	public Map<String, PlayerModel> playerLookup;
	
	public ImageIcon[] backgrounds = new ImageIcon[4];
	
	Lock lock = new ReentrantLock();
	public GridMapModel()
	{
		allModels = new AbstractObjectModel[4][10][10];
		playerLookup = new HashMap<String, PlayerModel>();
	}
	public void moveObjects(int level1, int sx, int sy, int level2,int dx,int dy)
	{
		lock.lock();
		PlayerModel pm = (PlayerModel) allModels[level1][sx][sy];
		allModels[level2][dx][dy] = pm;
		allModels[level1][sx][sy] = null;
		lock.unlock();
	}
	public Map<String, PlayerModel> getPlayers()
	{
		return playerLookup;
	}
}
