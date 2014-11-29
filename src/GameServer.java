import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import Models.GridMapModel;
import Models.PlayerModel;

/*				CLIENT-SERVER Communication Nomenclature
 * 				Standard policy: all caps, delimited by colons, no spaces.
 * -Lobby	"USERNAME:theactualusername"	"STATUS:READY" "TIMER:timetoadd"
 * -chat    -server receives "CHAT:recipient1,recipient2,recipient3:messageContent"
 * 			 -server sends	   "CHAT:senderName:messageContent"
 * 
 * 
 * 
 * 
 * 						ADD YOUR OWN SO EVERYONE CAN STAY IN SYNC
 */
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
	boolean readyState[] = {false, false, false, false}; //fixed at player cap
	ReentrantLock queryLock = new ReentrantLock();
	Socket s = null;
	PrintWriter pw = null;
	ObjectOutputStream oos = null;
	//Threads
	ArrayList<ServerLobbyThread> lobbyThreads;
	ArrayList<ServerGameThread> gameThreads;
	ArrayList<ServerUpdateClientThread> updateClientThreads; 

	public GameServer() 
	{
		
		allClientWriters = new HashMap<Integer, PrintWriter>();
		allClientObjectWriters = new HashMap<Integer, ObjectOutputStream>();
		lobbyThreads = new ArrayList<ServerLobbyThread>();
		gameThreads = new ArrayList<ServerGameThread>();
		updateClientThreads = new ArrayList<ServerUpdateClientThread>();
		team1 = new HashSet<PlayerModel>();
		team2 = new HashSet<PlayerModel>();
		//initialize server socket ss
		
		
		
		while(true) 
		{
			ServerSocket ss = null;
			try {
				ss = new ServerSocket(5001);
			} catch (IOException e) {}
			
			while(gameState[0] && !gameStart) 
			{
				//System.out.println("hello im in server");
				//accept connections from clients
				//initialize other varables as needed
				//accept connections from clients
				
				Socket s = null;
				PrintWriter pw = null;
				ObjectOutputStream oos = null;
				
				try 
				{
					s = ss.accept();
					pw = new PrintWriter(this.s.getOutputStream());
					oos = new ObjectOutputStream(this.s.getOutputStream());
				} 
				catch (IOException e) {}
				

				allClientWriters.put(id, pw);
				allClientObjectWriters.put(id, oos);
				
				ServerLobbyThread slt = new ServerLobbyThread(s, id, this);
				lobbyThreads.add(slt);
				gameThreads.add(new ServerGameThread(s, id, this));
				updateClientThreads.add(new ServerUpdateClientThread(s, id, this));
				
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
