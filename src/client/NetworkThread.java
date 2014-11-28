import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class NetworkThread extends Thread{
	
	ClientApplication myClient;
	BufferedReader myBR;
	public NetworkThread(ClientApplication myClient, BufferedReader br){
		System.out.println("in network threaad");
		this.myClient=myClient;
		this.myBR=br;
	}

	public void run(){
		try{
			while(true){
			System.out.println("in run");
			String msg= myBR.readLine(); //this blocks a thread, sits in this statement until message comes. 
			myClient.sendToServer(msg);
			}
		}
		catch(IOException io){
		
		}
	}
}
