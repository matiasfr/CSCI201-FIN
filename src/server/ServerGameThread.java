import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;


public class ServerGameThread extends Thread{
	int id;
	Socket s;
	GameServer server;
	BufferedReader br;
	boolean threadActive;
	
	public ServerGameThread(Socket s, int id, GameServer server) {	
		try{
			this.s = s;
			this.id = id;
			this.server = server;
			threadActive = true;
			br= new BufferedReader( new InputStreamReader(s.getInputStream() ) );
		} 
		catch (IOException e) {}
		
		//only generates map for the first thread to run
		if(!server.gridMapInit){
			generateGameMap();
		}
	}
	
	public void generateGameMap() {
		server.isGridMapInit();
		
		//creates the GridMapModel object using the two sets of playerModels (team1, team2)
		GridMapModel gmm = new GridMapModel();
		AbstractObjectModel[][][] allModels = new AbstractObjectModel[4][10][10];
		Map<Integer, PlayerModel> playerLookup = new HashMap<Integer, PlayerModel>();
		Iterator<PlayerModel> teamOneIt = server.team1.iterator();
		Iterator<PlayerModel> teamTwoIt = server.team2.iterator();
		
		//setting up map
		while(teamOneIt.hasNext()){
			PlayerModel pm = teamOneIt.next();
			playerLookup.put(pm.playerID, pm);
		}
		while(teamTwoIt.hasNext()){
			PlayerModel pm = teamTwoIt.next();
			playerLookup.put(pm.playerID, pm);
		}
		teamOneIt = server.team1.iterator();
		teamTwoIt = server.team2.iterator();
		
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
								item = new HealthRefilModel((int)(Math.random()*10));
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
		//Also generates random items to place around the map
	}
	
	
	
	public void run() {
		while(true){
			if(threadActive){
				if(server.gameStart){
					
					//listens for incoming messages from the client
					String message = "";
					try {
						message = br.readLine();
					} 
					catch (IOException e) {}
					
					//parses the incoming string
					//StringTokenizer st = new StringTokenizer(message," :");
					//String typeMessage = st.nextToken();
					
					String messages[] = message.split(":");
					String typeMessage = messages[0];
				    if(messages[0] == "CHAT") {
				    	processMessage(messages);
				    }
					  
					String distance;
					//client attacked another client
					if(typeMessage.equals("A")) {
						//value of attack (strength)
						String attack = messages[1];
						
						int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
						int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
						int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
						int direction = server.gmm.playerLookup.get(this.id).playerDirection;
						
						//check if anyone was hit
						if(direction == 0 && server.gmm.playerLookup.get(this.id).playerLocationY != 0){
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos-1];
							if(destination instanceof PlayerModel){
								((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
							}
						}
						else if(direction == 1 && server.gmm.playerLookup.get(this.id).playerLocationX != 9){
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos+1][ypos];
							if(destination instanceof PlayerModel){
								((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
							}
						}
						else if(direction == 2 && server.gmm.playerLookup.get(this.id).playerLocationY != 9)
						{
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos+1];
							if(destination instanceof PlayerModel){
								((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
							}
						}
						else if(server.gmm.playerLookup.get(this.id).playerLocationX != 0) //direction == 3
						{
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos-1][ypos];
							if(destination instanceof PlayerModel){
								((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
							}
						}
					}
					
					//client moved
					else if(typeMessage.equals("U")) { 
						distance = messages[1];
						server.gmm.playerLookup.get(this.id).setDirection(0);
						boolean isMoveable = true;
						boolean movingQuarter = false;
						int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
						int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
						int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
						
						if(distance.equals("0")){}
						else{
							if(server.gmm.playerLookup.get(this.id).playerLocationY==0){
								if(quarterPos==0||quarterPos==1){
									isMoveable=false;
								}
								else{movingQuarter=true;}
							}
							
							//the player can move 
							if(isMoveable){
								if(movingQuarter){
									if(quarterPos==2){
										AbstractObjectModel destination = server.gmm.allModels[0][xpos][9];
										if(destination instanceof PlayerModel){
											//another player so we cant move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, 9);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,0, xpos, 9);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, 9);		
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,0, xpos, 9);
										}	
									}
									else//quarterPos==3
									{
										AbstractObjectModel destination = server.gmm.allModels[1][xpos][9];
										if(destination instanceof PlayerModel){
											//another player so we can't move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, 9);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,1, xpos, 9);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, 9);		
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,1, xpos, 9);
										}	
									}
								}
								else
								{
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos-1];
									if(destination instanceof PlayerModel){
										//another player so we cant move
									}
									else if(destination instanceof ItemModel){
										if(destination instanceof HealthRefilModel){
											server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
										}
										else if(destination instanceof ArmorModel){
											server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
										}
										else if(destination instanceof SwordModel){
											//adding sword is all that is needed
										}
										
										//got an item and add it
										server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
										
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(0, -1);
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos-1);													
									}
									else {
										//nothing there
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(0, -1);		
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos-1);
									}	
								}
							}
						}
					}
					else if(typeMessage.equals("D")){ 
						distance = messages[1];
						server.gmm.playerLookup.get(this.id).setDirection(2);
						boolean isMoveable = true;
						boolean movingQuarter = false;
						int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
						int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
						int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
	
						if(distance.equals("0")){}
						else{
							if(server.gmm.playerLookup.get(this.id).playerLocationY==9){
								if(quarterPos==2||quarterPos==3){
									isMoveable=false;
								}
								else{movingQuarter=true;}
							}
							
							if(isMoveable){
								if(movingQuarter){
									if(quarterPos==0){
										AbstractObjectModel destination = server.gmm.allModels[2][xpos][0];
										if(destination instanceof PlayerModel){
											//another player so we cant move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, -9);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,2, xpos, 0);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, -9);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,2, xpos, 0);
										}
									}
									else{//quarterPos == 1
										AbstractObjectModel destination = server.gmm.allModels[3][xpos][0];
										if(destination instanceof PlayerModel){
											//another player so we cant move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, 9);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,3, xpos, 9);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(0, 9);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,3, xpos, 9);
										}
									}
								}
								else
								{
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos+1];
									if(destination instanceof PlayerModel){
										//another player so we cant move
									}
									else if(destination instanceof ItemModel){
										if(destination instanceof HealthRefilModel){
											server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
										}
										else if(destination instanceof ArmorModel){
											server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
										}
										else if(destination instanceof SwordModel){
											//adding sword is all that is needed
										}
										
										//got an item and add it
										server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
										
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(0, 1);
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos+1);
									}
									else 
									{
										//nothing there
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(0, 1);
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos+1);
									}
								}
							}
						}
					}
					else if(typeMessage.equals("L")) 
					{ 
						distance = messages[1];
						server.gmm.playerLookup.get(this.id).setDirection(3);
						boolean isMoveable = true;
						boolean movingQuarter = false;
						int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
						int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
						int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
						
						if(distance.equals("0")){}
						else{
							if(server.gmm.playerLookup.get(this.id).playerLocationX==0){
								if(quarterPos==0||quarterPos==2){
									isMoveable=false;
								}
								else{movingQuarter=true;}
							}
							
							if(isMoveable){
								if(movingQuarter){
									if(quarterPos==1){
										AbstractObjectModel destination = server.gmm.allModels[0][9][ypos];
										if(destination instanceof PlayerModel){
											//another player so we cant move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,0, 9, ypos);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,0, 9, ypos);
										}
									}
									else//quarterPos==3
									{
										AbstractObjectModel destination = server.gmm.allModels[2][9][ypos];
										if(destination instanceof PlayerModel){
											//another player so we cant move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,2, 9, ypos);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,2, 9, ypos);
										}
									}
								}
								else
								{
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos-1][ypos];
									if(destination instanceof PlayerModel){
										//another player so we cant move
									}
									else if(destination instanceof ItemModel){
										if(destination instanceof HealthRefilModel){
											server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
										}
										else if(destination instanceof ArmorModel){
											server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
										}
										else if(destination instanceof SwordModel){
											//adding sword is all that is needed
										}
										
										//got an item and add it
										server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
										
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos-1, ypos);
									}
									else {
										//nothing there
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos-1, ypos);
									}
								}
							}				
						}
					}
					else if(typeMessage.equals("R")) { 
						distance = messages[1];
						server.gmm.playerLookup.get(this.id).setDirection(1);
						boolean isMoveable = true;
						boolean movingQuarter = false;
						int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
						int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
						int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
	
						if(distance.equals("0")){}
						else{
							if(server.gmm.playerLookup.get(this.id).playerLocationX==9){
								if(quarterPos==1||quarterPos==3){
									isMoveable=false;
								}
								else{movingQuarter=true;}
							}
							if(isMoveable){
								if(movingQuarter){
									if(quarterPos==0){
										AbstractObjectModel destination = server.gmm.allModels[1][0][ypos];
										if(destination instanceof PlayerModel){
											//another player so we cant move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-9, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,1, 0, ypos);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-9, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,1, 0, ypos);
										}
									}
									else{//quarterPos ==2
										AbstractObjectModel destination = server.gmm.allModels[3][0][ypos];
										if(destination instanceof PlayerModel){
											//another player so we cant move
										}
										else if(destination instanceof ItemModel){
											if(destination instanceof HealthRefilModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
											}
											else if(destination instanceof ArmorModel){
												server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
											}
											else if(destination instanceof SwordModel){
												//adding sword is all that is needed
											}
											
											//got an item and add it
											server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
											
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-9, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,3, 0, ypos);
										}
										else {
											//nothing there
											//update PlayerModel
											server.gmm.playerLookup.get(this.id).setPostion(-9, 0);
											
											//update the GridMapModel
											server.gmm.moveObjects(quarterPos, xpos, ypos,3, 0, ypos);
										}
									}
								}
								else{
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos+1][ypos];
									if(destination instanceof PlayerModel){
										//another player so we cant move
									}
									else if(destination instanceof ItemModel){
										if(destination instanceof HealthRefilModel){
											server.gmm.playerLookup.get(this.id).setHealth(((HealthRefilModel) destination).healthPoints);
										}
										else if(destination instanceof ArmorModel){
											server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
										}
										else if(destination instanceof SwordModel){
											//adding sword is all that is needed
										}
										
										//got an item and add it
										server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
										
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(1, 0);
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos+1, ypos);
									}
									else 
									{
										//nothing there
										//update PlayerModel
										server.gmm.playerLookup.get(this.id).setPostion(1, 0);
										
										//update the GridMapModel
										server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos+1, ypos);
									}
								}
							}			
						}
					}
				}
				else{
					//don’t listen anymore the game is over
					break;
				}
			}
		}
	}
	private void processMessage(String[] m) {
		String content = m[m.length];//message content is last
		String[] recipients  = m[1].split(",");
		//boradcast
		for(int i=0; i<recipients.length;i++) {
			String str = "CHAT:"+server.gmm.playerLookup.get(recipients[i]).playerName+ ":"+content;
			server.allClientWriters.get(recipients[i]).println(str);
			server.allClientWriters.get(recipients[i]).flush();
		}
	}

	public void killThread(){
		threadActive = false;
	}
}