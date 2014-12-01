package server;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Models.AbstractObjectModel;
import Models.ArmorModel;
import Models.GridMapModel;
import Models.HealthRefillModel;
import Models.ItemModel;
import Models.PlayerModel;
import Models.SwordModel;


public class ServerUpdateClientThread extends Thread{

	@SuppressWarnings("unused")
	private Socket s;
	int id;
	GameServer parent;
	public boolean running = true;

	public ServerUpdateClientThread(Socket s, int id, GameServer parent)
	{
		this.s = s;
		this.id = id;
		this.parent = parent;
		generateGameMap();
	}

	public void generateGameMap() {
		parent.isGridMapInit();

		//creates the GridMapModel object using the two sets of playerModels (team1, team2)
		GridMapModel gmm = new GridMapModel();
		AbstractObjectModel[][][] allModels = new AbstractObjectModel[4][10][10];
		Map<String, PlayerModel> playerLookup = new HashMap<String, PlayerModel>();
		Iterator<PlayerModel> teamOneIt = parent.team1.iterator();
		Iterator<PlayerModel> teamTwoIt = parent.team2.iterator();

		//setting up map
		while(teamOneIt.hasNext()){
			PlayerModel pm = teamOneIt.next();
			playerLookup.put(pm.playerName, pm);
		}
		while(teamTwoIt.hasNext()){
			PlayerModel pm = teamTwoIt.next();
			playerLookup.put(pm.playerName, pm);
		}
		teamOneIt = parent.team1.iterator();
		teamTwoIt = parent.team2.iterator();

		//iterate through and populate
		for(int i=0;i<4;i++){
			for(int j=0;j<10;j++){
				for(int k=0;k<10;k++){
					if(i==0){//first quarter
						if(teamOneIt.hasNext()){
							PlayerModel pm = teamOneIt.next();
							pm.setPostion(j, k);
							pm.playerLocationQuarter = i;
							allModels[i][j][k] = pm;
						}
						else{allModels[i][j][k] = null;}
					}
					else if(i==1 || i==2){//second and third quarter
						int randGen = (int)(Math.random()*10);
						if(randGen == 0){//10% chance
							ItemModel item = null;
							randGen = (int)(Math.random()*3);
							if(randGen == 0){//health
								item = new HealthRefillModel((int)(Math.random()*10));
							}
							else if(randGen == 1){//armor
								item = new ArmorModel((int)(Math.random()*3));
							}
							else{//sword
								item = new SwordModel((int)(Math.random()*20), (int)(Math.random()*3));
							}
							allModels[i][j][k] = item;
						}
						else{allModels[i][j][k] = null;}
					}
					else if(i==3){//fourth quarter
						if(teamTwoIt.hasNext()){
							PlayerModel pm = teamTwoIt.next();
							pm.setPostion(j, k);
							pm.playerLocationQuarter = i;
							allModels[i][j][k] = pm;
						}
						else{allModels[i][j][k] = null;}
					}
				}
			}
		}

		gmm.allModels = allModels;
		gmm.playerLookup = playerLookup;
		
		parent.gmm = gmm;
		//Also generates random items to place around the map
	}

	public void run() {
		while(running) {
		try {sleep(69);} catch (InterruptedException e) {e.printStackTrace();}
		//Broadcast GridMapModel to allClientsObjectWriters

			Set<Integer> pwSet = parent.allClientObjectWriters.keySet();
			Iterator<Integer> itPrint = pwSet.iterator();
			while(itPrint.hasNext())
			{
				int nextKey = itPrint.next();
				try {
					
					
					parent.allClientObjectWriters.get(nextKey).writeObject(parent.gmm);
					parent.allClientObjectWriters.get(nextKey).reset();
				}
				catch (IOException e) {e.printStackTrace();}
			}
		}
	}

}