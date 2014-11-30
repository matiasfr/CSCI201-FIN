import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import Models.*;

public class ServerGameThread extends Thread
{
	int id;
	Socket s;
	GameServer server;
	boolean threadActive;
	
	public ServerGameThread(Socket s, int id, GameServer server) {	
		this.s = s;
		this.id = id;
		this.server = server;
		threadActive = true;
	}
	
	public void run() {
		while(true){
			if(threadActive){
				
					//listens for incoming messages from the client
					String message = "";
					try {
						message = (String) server.allClientObjectReaders.get(id).readObject();
						//parses the incoming string
						
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
											if(destination instanceof HealthRefillModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
											if(destination instanceof HealthRefillModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
											if(destination instanceof HealthRefillModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
											if(destination instanceof HealthRefillModel){
												server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
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
					catch (IOException e) {} 
					catch (ClassNotFoundException e) {e.printStackTrace();}
					
					
			}
			else{
				break;
			}
		}
	}
	private void processMessage(String[] m) {
		String content = m[m.length];//message content is last
		String[] recipients  = m[1].split(",");
		//boradcast
		for(int i=0; i<recipients.length;i++) {
			String str = "CHAT:"+server.gmm.playerLookup.get(recipients[i]).playerName+ ":"+content;
			
			try {
				server.allClientObjectWriters.get(recipients[i]).writeObject(str);
			}
			catch (IOException e) {e.printStackTrace();}
			//server.allClientWriters.get(recipients[i]).flush();
		}
	}

	public void killThread(){
		threadActive = false;
	}
}