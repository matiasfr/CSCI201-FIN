import java.io.IOException;
import java.net.Socket;


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
			for(int i=0;i < parent.allClientObjectWriters.size();i++) {
				try {parent.allClientObjectWriters.get(i).writeObject(parent.gmm);} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
}