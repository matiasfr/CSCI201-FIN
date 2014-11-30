import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;


public class ServerUpdateClientThread extends Thread{
	
	private Socket s;
	int id;
	GameServer parent;
	public boolean running = true;
	
	public ServerUpdateClientThread(Socket s, int id, GameServer parent) 
	{
		this.s = s;
		this.id = id;
		this.parent = parent;
	}
	public void run() {
		while(running) {
		try {sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
		//Broadcast GridMapModel to allClientsObjectWriters
			
			Set<Integer> pwSet = parent.allClientObjectWriters.keySet();
			Iterator<Integer> itPrint = pwSet.iterator();
			while(itPrint.hasNext())
			{
				int nextKey = itPrint.next();
				try {
					parent.allClientObjectWriters.get(nextKey).writeObject(parent.gmm);
				} 
				catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
}