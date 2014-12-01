package server;
import java.io.IOException;
import java.net.Socket;

import Models.AbstractObjectModel;
import Models.ArmorModel;
import Models.HealthRefillModel;
import Models.ItemModel;
import Models.PlayerModel;
import Models.SwordModel;

public class ServerGameThread extends Thread
{
	int id;
	String name;
	Socket s;
	GameServer server;
	boolean threadActive;
	
	public ServerGameThread(Socket s, String name, int id, GameServer server) {	
		this.s = s;
		this.id = id;
		this.server = server;
		this.name = name;
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
						System.out.println("incoming: " + message);
						String messages[] = message.split(":");
						String typeMessage = messages[0];
					    if(messages[0].equals("CHAT")) {
					    	processMessage(messages);
					    }
					    else{
							int distance;
							
							//client attacked another client			
							if(typeMessage.equals("A")) {
								//value of attack (strength)
								int attack = Integer.parseInt(messages[1]);
								
								int xpos = server.gmm.playerLookup.get(this.name).playerLocationX;
								int ypos = server.gmm.playerLookup.get(this.name).playerLocationY;
								int quarterPos = server.gmm.playerLookup.get(this.name).playerLocationQuarter;
								int direction = server.gmm.playerLookup.get(this.name).playerDirection;
								
								//check if anyone was hit
								if(direction == 0 && server.gmm.playerLookup.get(this.name).playerLocationY != 0){
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos-1];
									if(destination instanceof PlayerModel){
										PlayerModel pm = (PlayerModel) destination;
										int attackValue = attack/pm.playerArmorPoints;
										((PlayerModel) destination).setHealth(-1*attackValue);
									}
								}
								else if(direction == 1 && server.gmm.playerLookup.get(this.name).playerLocationX != 9){
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos+1][ypos];
									if(destination instanceof PlayerModel){
										PlayerModel pm = (PlayerModel) destination;
										int attackValue = attack/pm.playerArmorPoints;
										((PlayerModel) destination).setHealth(-1*attackValue);
									}
								}
								else if(direction == 2 && server.gmm.playerLookup.get(this.name).playerLocationY != 9)
								{
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos+1];
									if(destination instanceof PlayerModel){
										PlayerModel pm = (PlayerModel) destination;
										int attackValue = attack/pm.playerArmorPoints;
										((PlayerModel) destination).setHealth(-1*attackValue);
									}
								}
								else if(server.gmm.playerLookup.get(this.name).playerLocationX != 0) //direction == 3
								{
									AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos-1][ypos];
									if(destination instanceof PlayerModel){
										PlayerModel pm = (PlayerModel) destination;
										int attackValue = attack/pm.playerArmorPoints;
										((PlayerModel) destination).setHealth(-1*attackValue);
									}
								}
							}
							
							//client moved
							else if(typeMessage.equals("U")) { 
								distance = Integer.parseInt(messages[1]);
								server.gmm.playerLookup.get(this.name).setDirection(0);
								boolean isMoveable = true;
								boolean movingQuarter = false;
								int xpos = server.gmm.playerLookup.get(this.name).playerLocationX;
								int ypos = server.gmm.playerLookup.get(this.name).playerLocationY;
								int quarterPos = server.gmm.playerLookup.get(this.name).playerLocationQuarter;
								
								if(distance == 0){}
								else{
									if(server.gmm.playerLookup.get(this.name).playerLocationY <= 0){
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
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, 9);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,0, xpos, 9);
													
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 0;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, 9);		
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,0, xpos, 9);
													
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 0;
												}	
											}
											else//quarterPos==3
											{
												AbstractObjectModel destination = server.gmm.allModels[1][xpos][9];
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, 9);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,1, xpos, 9);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 1;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, 9);		
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,1, xpos, 9);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 1;
												}	
											}
										}
										else
										{
											AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos-distance];
											if(destination instanceof ItemModel){
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
												}
												else if(destination instanceof ArmorModel){
													server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
												}
												else if(destination instanceof SwordModel){
													//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
												}
												
												//got an item and add it
												server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
												
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(0, -1*distance);
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos-distance);													
											}
											else {
												//nothing there
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(0, -distance);		
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos-distance);
											}	
										}
									}
								}
							}
							else if(typeMessage.equals("D")){ 
								distance = Integer.parseInt(messages[1]);
								server.gmm.playerLookup.get(this.name).setDirection(2);
								boolean isMoveable = true;
								boolean movingQuarter = false;
								int xpos = server.gmm.playerLookup.get(this.name).playerLocationX;
								int ypos = server.gmm.playerLookup.get(this.name).playerLocationY;
								int quarterPos = server.gmm.playerLookup.get(this.name).playerLocationQuarter;
			
								if(distance == 0){}
								else{
									if(server.gmm.playerLookup.get(this.name).playerLocationY >= 9){
										if(quarterPos==2||quarterPos==3){
											isMoveable=false;
										}
										else{movingQuarter=true;}
									}
									
									if(isMoveable){
										if(movingQuarter){
											if(quarterPos==0){
												AbstractObjectModel destination = server.gmm.allModels[2][xpos][0];
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, -9);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,2, xpos, 0);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 2;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, -9);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,2, xpos, 0);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 2;
												}
											}
											else{//quarterPos == 1
												AbstractObjectModel destination = server.gmm.allModels[3][xpos][0];
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, -9);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,3, xpos, 0);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 3;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(0, -9);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,3, xpos, 0);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 3;
												}
											}
										}
										else
										{
											AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos+distance];
											if(destination instanceof ItemModel){
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
												}
												else if(destination instanceof ArmorModel){
													server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
												}
												else if(destination instanceof SwordModel){
													//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
												}
												
												//got an item and add it
												server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
												
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(0, distance);
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos+distance);
											}
											else 
											{
												//nothing there
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(0, distance);
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos, ypos+distance);
											}
										}
									}
								}
							}
							else if(typeMessage.equals("L")) 
							{ 
								distance = Integer.parseInt(messages[1]);
								server.gmm.playerLookup.get(this.name).setDirection(3);
								boolean isMoveable = true;
								boolean movingQuarter = false;
								int xpos = server.gmm.playerLookup.get(this.name).playerLocationX;
								int ypos = server.gmm.playerLookup.get(this.name).playerLocationY;
								int quarterPos = server.gmm.playerLookup.get(this.name).playerLocationQuarter;
								
								if(distance == 0){}
								else{
									if(server.gmm.playerLookup.get(this.name).playerLocationX <= 0){
										if(quarterPos==0||quarterPos==2){
											isMoveable=false;
										}
										else{movingQuarter=true;}
									}
									
									if(isMoveable){
										if(movingQuarter){
											if(quarterPos==1){
												AbstractObjectModel destination = server.gmm.allModels[0][9][ypos];
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,0, 9, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 0;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,0, 9, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 0;
												}
											}
											else//quarterPos==3
											{
												AbstractObjectModel destination = server.gmm.allModels[2][9][ypos];
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,2, 9, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 2;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,2, 9, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 2;
												}
											}
										}
										else
										{
											AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos-distance][ypos];
											if(destination instanceof ItemModel){
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
												}
												else if(destination instanceof ArmorModel){
													server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
												}
												else if(destination instanceof SwordModel){
													//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
												}
												
												//got an item and add it
												server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
												
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(-1*distance, 0);
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos-distance, ypos);
											}
											else {
												//nothing there
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(-1*distance, 0);
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos-distance, ypos);
											}
										}
									}				
								}
							}
							else if(typeMessage.equals("R")) { 
								distance = Integer.parseInt(messages[1]);
								server.gmm.playerLookup.get(this.name).setDirection(1);
								boolean isMoveable = true;
								boolean movingQuarter = false;
								int xpos = server.gmm.playerLookup.get(this.name).playerLocationX;
								int ypos = server.gmm.playerLookup.get(this.name).playerLocationY;
								int quarterPos = server.gmm.playerLookup.get(this.name).playerLocationQuarter;
			
								if(distance == 0){}
								else{
									if(server.gmm.playerLookup.get(this.name).playerLocationX >= 9){
										if(quarterPos==1||quarterPos==3){
											isMoveable=false;
										}
										else{movingQuarter=true;}
									}
									if(isMoveable){
										if(movingQuarter){
											if(quarterPos==0){
												AbstractObjectModel destination = server.gmm.allModels[1][0][ypos];
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(-9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,1, 0, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 1;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(-9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,1, 0, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 1;
												}
											}
											else{//quarterPos ==2
												AbstractObjectModel destination = server.gmm.allModels[3][0][ypos];
												if(destination instanceof ItemModel){
													if(destination instanceof HealthRefillModel){
														server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
													}
													else if(destination instanceof ArmorModel){
														server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
													}
													else if(destination instanceof SwordModel){
														//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
													}
													
													//got an item and add it
													server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
													
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(-9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,3, 0, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 3;
												}
												else {
													//nothing there
													//update PlayerModel
													server.gmm.playerLookup.get(this.name).setPostion(-9, 0);
													
													//update the GridMapModel
													server.gmm.moveObjects(quarterPos, xpos, ypos,3, 0, ypos);
													server.gmm.playerLookup.get(this.name).playerLocationQuarter = 3;
												}
											}
										}
										else{
											AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos+distance][ypos];
											if(destination instanceof ItemModel){
												if(destination instanceof HealthRefillModel){
													server.gmm.playerLookup.get(this.name).setHealth(((HealthRefillModel) destination).healthPoints);
												}
												else if(destination instanceof ArmorModel){
													server.gmm.playerLookup.get(this.name).setArmor(((ArmorModel) destination).armorPoints);
												}
												else if(destination instanceof SwordModel){
													//adding sword is all that is needed
														server.gmm.playerLookup.get(this.name).setExperience(((SwordModel) destination).damage);
												}
												
												//got an item and add it
												server.gmm.playerLookup.get(this.name).playerInventory.add((ItemModel) destination);
												
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(distance, 0);
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos+distance, ypos);
											}
											else 
											{
												//nothing there
												//update PlayerModel
												server.gmm.playerLookup.get(this.name).setPostion(distance, 0);
												
												//update the GridMapModel
												server.gmm.moveObjects(quarterPos, xpos, ypos,quarterPos, xpos+distance, ypos);
											}
										}
									}
								}
							}
						}
					} 
					catch (IOException e) {} 
					catch (ClassNotFoundException e) {e.printStackTrace();}
					
					int xpos = server.gmm.playerLookup.get(this.name).playerLocationX;
					int ypos = server.gmm.playerLookup.get(this.name).playerLocationY;
					int quarterPos = server.gmm.playerLookup.get(this.name).playerLocationQuarter;
//					System.out.println("X: "+xpos+" Y: "+ypos+" Q: "+quarterPos);
			}
			else{
				break;
			}
		}
	}
	private void processMessage(String[] m) {
		String content = m[m.length-1];//message content is last
		String[] recipients  = m[1].split(",");
		//boradcast
		for(int i=0; i<recipients.length;i++) {
			String str = "CHAT:"+server.gmm.playerLookup.get(this.name).playerName+ ":"+content;
			
			try {
				server.allClientObjectWriters.get(server.gmm.getPlayers().get(recipients[i]).playerID).writeObject(str);
				server.allClientObjectWriters.get(server.gmm.getPlayers().get(recipients[i]).playerID).reset();
			}
			catch (IOException e) {e.printStackTrace();}
		}
	}

	public void killThread(){
		threadActive = false;
	}
}