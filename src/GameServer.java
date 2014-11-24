import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class GameServer 
{
	//shared data
	boolean gameState[] = {true, false, false}; //0: joining game  1:game play  2:game over
	GridMapModel gmm;
	Map<Integer, PrintWriter> allClientWriters;
	Map<Integer, ObjectOutputStream> allClientObjectWriters;
	Set<PlayerModel> team1;
	Set<PlayerModel> team2;
	int timeLeftInLobby = 30;
	int id = 0;
	boolean gameStart = false;
	boolean gridMapInit = false;

	//Threads
	ArrayList<ServerLobbyThread> lobbyThreads;
	ArrayList<ServerGameThread> gameThreads;
	ArrayList<ServerUpdateClientThread> updateClientThreads; 
	ArrayList<ServerChatThread> chatThreads;

	public GameServer() 
	{
		while(true) 
		{
			//initialize server socket ss
			ServerSocket ss = null;
			try {
				ss = new ServerSocket(5001);
			} catch (IOException e) {}
			//initialize other varables as needed
			
			while(gameState[0] && !gameStart) 
			{
				//accept connections from clients
				Socket s = null;
				PrintWriter pw = null;
				ObjectOutputStream oos = null;
				try 
				{
					s = ss.accept();
					pw = new PrintWriter(s.getOutputStream());
					oos = new ObjectOutputStream(s.getOutputStream());
				} 
				catch (IOException e) {}
				
				
				allClientWriters.put(id, pw);
				allClientObjectWriters.put(id, oos);
				
				ServerLobbyThread slt = new ServerLobbyThread(s, id);
				lobbyThreads.add(slt);
				gameThreads.add(new ServerGameThread(s, id, this));
				updateClientThreads.add(new ServerUpdateClientThread(s, id));
				chatThreads.add(new ServerChatThread(s, id));
				
				id++;
				slt.start();
			}
			
			while(gameState[1] && gameStart) 
			{
				//kill all ServerLobbyThread threads
				
				//start all ServerGameThread threads
				Iterator<ServerGameThread> itGame = gameThreads.iterator();
				while(itGame.hasNext())
				{
					itGame.next().start();
				}
				
				//start all ServerChatThread threads
				Iterator<ServerChatThread> itChat = chatThreads.iterator();
				while(itChat.hasNext())
				{
					itChat.next().start();
				}
				
				//start all ServerUpdateClientThread threads
				Iterator<ServerUpdateClientThread> itUpdate = updateClientThreads.iterator();
				while(itUpdate.hasNext())
				{
					itUpdate.next().start();
				}
			}
			while(gameState[2]) 
			{
			//kill all ServerGameThread threads
			Iterator<ServerGameThread> itGame = gameThreads.iterator();
			while(itGame.hasNext())
			{
				itGame.next().killThread();
			}
			//kill all ServerChatThread threads
			//kill all ServerUpdateClientThread threads
			}
		}
	}
	public void isGridMapInit()
	{
		gridMapInit = true;
	}
	public static void main(String[] args) 
	{
		GameServer s = new GameServer();
	}


}
