import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import Models.AbstractObjectModel;
import Models.ArmorModel;
import Models.GridMapModel;
import Models.HealthRefillModel;
import Models.ItemModel;
import Models.PlayerModel;
import Models.SwordModel;


public class ServerGameThread extends Thread
{
	int id;
	Socket s;
	GameServer server;
	BufferedReader br;
	boolean threadActive;
	
	public ServerGameThread(Socket s, int id, GameServer server) 
	{
		this.s = s;
		this.id = id;
		this.server = server;
		threadActive = true;
		
		try
		{
			br= new BufferedReader( new InputStreamReader(s.getInputStream() ) );
		} 
		catch (IOException e) {}
		
		if(!server.gridMapInit)
		{
			generateGameMap();
		}
	}
	
	public void generateGameMap() 
	{
		server.isGridMapInit();
		
		//creates the GridMapModel object using the two sets of playerModels (team1, team2)
		GridMapModel gmm = new GridMapModel();
		AbstractObjectModel[][][] allModels = new AbstractObjectModel[4][10][10];
		Map<Integer, PlayerModel> playerLookup = new HashMap<Integer, PlayerModel>();
		Iterator<PlayerModel> teamOneIt = server.team1.iterator();
		Iterator<PlayerModel> teamTwoIt = server.team2.iterator();
		
		//setting up map
		while(teamOneIt.hasNext())
		{
			PlayerModel pm = teamOneIt.next();
			playerLookup.put(pm.playerID, pm);
		}
		while(teamTwoIt.hasNext())
		{
			PlayerModel pm = teamTwoIt.next();
			playerLookup.put(pm.playerID, pm);
		}
		teamOneIt = server.team1.iterator();
		teamTwoIt = server.team2.iterator();
		
		//iterate through and populate
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<10;j++)
			{
				for(int k=0;k<10;k++)
				{
					if(i==0)//first quarter
					{
						if(teamOneIt.hasNext())
						{
							PlayerModel pm = teamOneIt.next();
							pm.setPostion(j, k);
							pm.playerLocationQuarter = i;
							allModels[i][j][k] = pm;
						}
						else{allModels[i][j][k] = null;}
					}
					else if(i==1 || i==2)//second and third quarter
					{
						int randGen = (int)(Math.random()*10);
						if(randGen == 0)//10% chance
						{
							ItemModel item = null;
							randGen = (int)(Math.random()*3);
							if(randGen == 0)//health
							{
								item = new HealthRefillModel((int)(Math.random()*10));
							}
							else if(randGen == 1)//armor
							{
								item = new ArmorModel((int)(Math.random()*50));
							}
							else//sword
							{
								item = new SwordModel((int)(Math.random()*20), (int)(Math.random()*3));
							}
							allModels[i][j][k] = item;
						}
						else{allModels[i][j][k] = null;}
					}
					else if(i==3)//fourth quarter
					{
						if(teamTwoIt.hasNext())
						{
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
	
	public void run() 
	{
		if(threadActive)
		{
			if(server.gameStart)
			{
				//listens for incoming messages from the client
				String message = "";
				try 
				{
					message = br.readLine();
				} 
				catch (IOException e) {}
				
				//parses the incoming string
				StringTokenizer st = new StringTokenizer(message," :");
				String typeMessage = st.nextToken();
				
				/*String messages[] = message.split(":")
				 * String typeMessage = messages[0];
				 * String distance = messages[1];
				 * etc. 
				 * 
				 * if(messages[0] == CHAT) {
				 * delegate to processMessage(String[] message) {
				 * 
				 * when sending	server.gmm.getPlayers().get(id).getName
				 * 
				 * }
				 */
				
				String distance;
				//client attacked another client
				if(typeMessage.equals("A")) 
				{
					//value of attack (strength)
					String attack = st.nextToken();
					
					int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
					int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
					int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
					int direction = server.gmm.playerLookup.get(this.id).playerDirection;
					
					//check if anyone was hit
					if(direction == 0 && server.gmm.playerLookup.get(this.id).playerLocationY != 0)
					{
						AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos-1];
						if(destination instanceof PlayerModel)
						{
							((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
						}
					}
					else if(direction == 1 && server.gmm.playerLookup.get(this.id).playerLocationX != 9)
					{
						AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos+1][ypos];
						if(destination instanceof PlayerModel)
						{
							((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
						}
					}
					else if(direction == 2 && server.gmm.playerLookup.get(this.id).playerLocationY != 9)
					{
						AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos+1];
						if(destination instanceof PlayerModel)
						{
							((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
						}
					}
					else if(server.gmm.playerLookup.get(this.id).playerLocationX != 0) //direction == 3
					{
						AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos-1][ypos];
						if(destination instanceof PlayerModel)
						{
							((PlayerModel) destination).setHealth(-1*Integer.parseInt(attack));
						}
					}
				}
				
				//client moved
				else if(typeMessage.equals("U")) 
				{ 
					distance = st.nextToken();
					server.gmm.playerLookup.get(this.id).setDirection(0);
					boolean isMoveable = true;
					int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
					int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
					int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
					
					if(distance.equals("0")){}
					else
					{
						if(server.gmm.playerLookup.get(this.id).playerLocationY==0)
						{
							isMoveable=false;
						}
						
						if(isMoveable)
						{
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos-1];
							if(destination instanceof PlayerModel)
							{
								//another player so we cant move
							}
							else if(destination instanceof ItemModel)
							{
								if(destination instanceof HealthRefillModel)
								{
									server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
								}
								else if(destination instanceof ArmorModel)
								{
									server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
								}
								else if(destination instanceof SwordModel)
								{
									//adding sword is all that is needed
								}
								
								//got an item and add it
								server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
								
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(0, -1);
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos, ypos-1);
								
								
							}
							else 
							{
								//nothing there
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(0, -1);		
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos, ypos-1);
							}	
						}
					}
				}
				else if(typeMessage.equals("D")) 
				{ 
					distance = st.nextToken();
					server.gmm.playerLookup.get(this.id).setDirection(2);
					boolean isMoveable = true;
					int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
					int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
					int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;

					if(distance.equals("0")){}
					else
					{
						if(server.gmm.playerLookup.get(this.id).playerLocationY==9)
						{
							isMoveable=false;
						}
						
						if(isMoveable)
						{
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos][ypos+1];
							if(destination instanceof PlayerModel)
							{
								//another player so we cant move
							}
							else if(destination instanceof ItemModel)
							{
								if(destination instanceof HealthRefillModel)
								{
									server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
								}
								else if(destination instanceof ArmorModel)
								{
									server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
								}
								else if(destination instanceof SwordModel)
								{
									//adding sword is all that is needed
								}
								
								//got an item and add it
								server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
								
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(0, 1);
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos, ypos+1);
							}
							else 
							{
								//nothing there
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(0, 1);
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos, ypos+1);
							}
						}
					}
				}
				else if(typeMessage.equals("L")) 
				{ 
					distance = st.nextToken();
					server.gmm.playerLookup.get(this.id).setDirection(3);
					boolean isMoveable = true;
					int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
					int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
					int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;
					
					if(distance.equals("0")){}
					else
					{
						if(server.gmm.playerLookup.get(this.id).playerLocationX==0)
						{
							isMoveable=false;
						}
						
						if(isMoveable)
						{
							
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos-1][ypos];
							if(destination instanceof PlayerModel)
							{
								//another player so we cant move
							}
							else if(destination instanceof ItemModel)
							{
								if(destination instanceof HealthRefillModel)
								{
									server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
								}
								else if(destination instanceof ArmorModel)
								{
									server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
								}
								else if(destination instanceof SwordModel)
								{
									//adding sword is all that is needed
								}
								
								//got an item and add it
								server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
								
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos-1, ypos);
							}
							else 
							{
								//nothing there
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(-1, 0);
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos-1, ypos);
							}
						}				
					}
				}
				else if(typeMessage.equals("R")) 
				{ 
					distance = st.nextToken();
					server.gmm.playerLookup.get(this.id).setDirection(1);
					boolean isMoveable = true;
					int xpos = server.gmm.playerLookup.get(this.id).playerLocationX;
					int ypos = server.gmm.playerLookup.get(this.id).playerLocationY;
					int quarterPos = server.gmm.playerLookup.get(this.id).playerLocationQuarter;

					if(distance.equals("0")){}
					else
					{
						if(server.gmm.playerLookup.get(this.id).playerLocationX==9)
						{
							isMoveable=false;
						}
						if(isMoveable)
						{
							
							AbstractObjectModel destination = server.gmm.allModels[quarterPos][xpos+1][ypos];
							if(destination instanceof PlayerModel)
							{
								//another player so we cant move
							}
							else if(destination instanceof ItemModel)
							{
								if(destination instanceof HealthRefillModel)
								{
									server.gmm.playerLookup.get(this.id).setHealth(((HealthRefillModel) destination).healthPoints);
								}
								else if(destination instanceof ArmorModel)
								{
									server.gmm.playerLookup.get(this.id).setArmor(((ArmorModel) destination).armorPoints);
								}
								else if(destination instanceof SwordModel)
								{
									//adding sword is all that is needed
								}
								
								//got an item and add it
								server.gmm.playerLookup.get(this.id).playerInventory.add((ItemModel) destination);
								
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(1, 0);
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos+1, ypos);
							}
							else 
							{
								//nothing there
								//update PlayerModel
								server.gmm.playerLookup.get(this.id).setPostion(1, 0);
								
								//update the GridMapModel
								server.gmm.moveObjects(quarterPos, xpos, ypos, xpos+1, ypos);
							}
						}			
					}
				}
			}
			else
			{
				//don’t listen anymore the game is over
			}
		}
	}
	public void killThread()
	{
		threadActive = false;
	}
}