import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;

public class GridMapModel implements Serializable
{
	AbstractObjectModel[][][] allModels;
	
	//for quick lookup of id to player
	Map<Integer, PlayerModel> playerLookup;
	
	ImageIcon[] backgrounds = new ImageIcon[4];
	
	Lock lock = new ReentrantLock();
	public GridMapModel()
	{
		//45x45 and 
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
	public Map<Integer, PlayerModel> getPlayers()
	{
		return playerLookup;
	}
}
