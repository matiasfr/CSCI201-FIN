import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
	Map<Integer, PrintWriter> allClientWriters;
	Map<Integer, ObjectOutputStream> allClientObjectWriters;
	Set<PlayerModel> team1;
	Set<PlayerModel> team2;
	int timeLeftInLobby = 30;
	int id = 0;
	boolean gameStart = false;
	boolean gridMapInit = false;
	boolean readyState[] = {false, false, false, false}; //fixed at player cap
	///////////////////////////////////////////////////////////////////////////////////////
	
	//////////////LOCKS///////////////////////////
	ReentrantLock queryLock = new ReentrantLock();
	Lock stateLock = new ReentrantLock();
	//////////////////////////////////////////////

	//Threads
	ArrayList<ServerLobbyThread> lobbyThreads;
	ArrayList<ServerGameThread> gameThreads;
	ArrayList<ServerUpdateClientThread> updateClientThreads; 

	//////////////////CONNECT TO SERVER///////////////////////////
	public GameServer(){	
		try{
			ServerSocket ss= new ServerSocket(5001);
			while(true) {
				//now listening
				new ServerThread(ss.accept(), ++id, this).start();			
			}
		} 
		catch (IOException ioe) {}
	}
	/////////////////////////////////////////////////////////////
	
	public void isGridMapInit(){
		stateLock.lock();
		gridMapInit = true;
		stateLock .unlock();
	}
	
	public void changeState(int current, int next){
		stateLock.lock();
		gameState[current] = false;
		gameState[next] = true;
		stateLock.unlock();
	}
	
	public void setGameStart()
	{
		stateLock.lock();
		gameStart = true;
		stateLock.unlock();
	}
	
	///////////////STARTING SERVER///////////
	public static void main(String[] args) {
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
		PrintWriter pw = null;
		ObjectOutputStream oos = null;
		
		public ServerThread(Socket s, int id, GameServer gs){
			try {
				this.s = s;
				this.id = id;
				this.gs = gs; 
				pw = new PrintWriter(s.getOutputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				allClientWriters.put(id, pw);
				allClientObjectWriters.put(id, oos);
			} 
			catch (IOException e) {}
			
			//start the login/lobby
			ServerLobbyThread slt = new ServerLobbyThread(s, id, gs);
			lobbyThreads.add(slt);
			slt.start();
		}
		
		public void run(){
			while(gameState[0]) {
				//still in lobby, do nothing until all threads end
			}
			while(gameState[1]) {
				//kill all ServerLobbyThread threads (should be dead)
				
				//this code will only run once
				if(!gameStart){
					//start all ServerGameThread threads
					gameThreads.add(new ServerGameThread(s, id, gs));
						
					//start all ServerUpdateClientThread threads
					updateClientThreads.add(new ServerUpdateClientThread(s, id, gs));
					setGameStart();
				}
			}
			while(gameState[2]){
				//kill all ServerGameThread/ServerUpdateClientThreads threads (should be dead)
				//the game is finished
			}
		}
	}

}
