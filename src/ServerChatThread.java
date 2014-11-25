import java.net.Socket;


public class ServerChatThread extends Thread{

	private Socket s;
	int id;
	GameServer parent;
	public ServerChatThread(Socket s, int id, GameServer parent) 
	{
		this.s = s;
		this.id = id;
		this.parent = parent;
	}

	public void run() {
		
		//if(game has started)
	//listens for incoming strings

	//broadcasts them to certain PrintWriters, based on the separation of the teams. The id of a player can be mapped to the PrintWriter
	}
}
