package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
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

public class GameServer {
	////////////////////////////////////ALL SHARED DATA////////////////////////////////////
	boolean gameState[] = {true, false, false}; //0: joining game  1:game play  2:game over
	GridMapModel gmm;
	//Map<Integer, PrintWriter> allClientWriters = new HashMap<Integer, PrintWriter>();
	Map<Integer, ObjectOutputStream> allClientObjectWriters = new HashMap<Integer, ObjectOutputStream>();
	Map<Integer, ObjectInputStream> allClientObjectReaders = new HashMap<Integer, ObjectInputStream>();
	Set<PlayerModel> team1 = new HashSet<PlayerModel>();
	Set<PlayerModel> team2 = new HashSet<PlayerModel>();
	int timeLeftInLobby = 30;
	int id = 0;
	boolean allPlayersReady = false;
	boolean updateThreadMade = false;
	boolean team = false;
	boolean gridMapInit = false;
	boolean readyState[] = {false, false, false, false}; //fixed at player cap
	///////////////////////////////////////////////////////////////////////////////////////
	
	//////////////LOCKS///////////////////////////
	ReentrantLock queryLock = new ReentrantLock();
	Lock stateLock = new ReentrantLock();
	//////////////////////////////////////////////

	//Threads
	//ArrayList<ServerLobbyThread> lobbyThreads = new ArrayList<ServerLobbyThread>();
	//ArrayList<ServerGameThread> gameThreads = new ArrayList<ServerGameThread>();
	//ArrayList<ServerUpdateClientThread> updateClientThreads = new ArrayList<ServerUpdateClientThread>(); 

	//////////////////CONNECT TO SERVER///////////////////////////
	public GameServer(){	
		try{
			@SuppressWarnings("resource")
			ServerSocket ss= new ServerSocket(5001);
			while(gameState[0]) {
				//now listening
				new ServerThread(ss.accept(), id++, this).start();			
			}
		} 
		catch (IOException ioe) {}
	}
	/////////////////////////////////////////////////////////////
	
	public void flipTeam() {
		stateLock.lock();
		if(team == true) {
			team = false;
		} else {
			team = true;
		}
		stateLock.unlock();
	}
	
	public void isGridMapInit(){
		stateLock.lock();
		gridMapInit = true;
		stateLock .unlock();
	}
	
	public void changeState(int current, int next){
		stateLock.lock();
		gameState[next] = true;
		gameState[current] = false;	
		stateLock.unlock();
	}
	
	public void setPlayersReady()
	{
		stateLock.lock();
		allPlayersReady = true;
		stateLock.unlock();
	}
	
	public void setUpdateThread()
	{
		stateLock.lock();
		updateThreadMade = true;
		stateLock.unlock();
	}
	
	///////////////STARTING SERVER///////////
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		GameServer s = new GameServer();
	}
	/////////////////////////////////////////
	
	
	////////////////////INITIAL SERVER THREAD////////////////////
	/*	created for each instance of a connection
	 *	controls flow of game-play
	 * 	creates additional threads
	 */
	public class ServerThread extends Thread
	{
		Socket s;
		int id;
		GameServer gs;
		//PrintWriter pw = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		boolean gameStart = false;
		
		public ServerThread(Socket s, int id, GameServer gs){
			try {
				this.s = s;
				this.id = id;
				this.gs = gs; 
				oos = new ObjectOutputStream(s.getOutputStream());
				ois = new ObjectInputStream(s.getInputStream());
				allClientObjectWriters.put(id, oos);
				allClientObjectReaders.put(id, ois);
			} 
			catch (IOException e) {}
			
			//start the login/lobby
			ServerLobbyThread slt = new ServerLobbyThread(id, gs);
			//lobbyThreads.add(slt);
			slt.start();
		}
		
		public void run(){
			while(true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				if(gameState[0]) {
					//still in lobby, do nothing until all threads end
				}
				else if(gameState[1]) {
					//kill all ServerLobbyThread threads (should be dead)
					//this code will only run once
	
					if(!gameStart){
						
						Iterator<PlayerModel> teamOneIt = team1.iterator();
						Iterator<PlayerModel> teamTwoIt = team2.iterator();
						String name ="";
						//setting up map
						while(teamOneIt.hasNext()){
							PlayerModel pm = teamOneIt.next();
							if(pm.playerID == id){
								name = pm.playerName;
							}
						}
						while(teamTwoIt.hasNext()){
							PlayerModel pm = teamTwoIt.next();
							if(pm.playerID == id){
								name = pm.playerName;
							}
						}
						
						//start all ServerGameThread threads
						ServerGameThread sgt = new ServerGameThread(s,name, id, gs);
						//gameThreads.add(sgt);
						sgt.start();
							
						//start all ServerUpdateClientThread threads
						if(!updateThreadMade){
							setUpdateThread();
							ServerUpdateClientThread suc = new ServerUpdateClientThread(s, id, gs);
							//updateClientThreads.add(suc);
							suc.start();
						}
						gameStart = true;
					}
				}
				else if(gameState[2]){
					//kill all ServerGameThread/ServerUpdateClientThreads threads (should be dead)
					//the game is finished
				}
			}
		}
	}

}
