import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import java.awt.*;

public class ServerUI extends JFrame{
	ServerSocket serverSocket;
	int numPlayers;
	TextArea ta = new TextArea();
	ServerSideConnection ssc;
	Socket s;
	ServerSideConnection player1 , player2;
	ServerUI(){
		setLayout(new BorderLayout());
		setSize(300,300);
		add(ta);
		ta.append("-----Game Start------\n");
		numPlayers = 0 ;
		try {
			serverSocket = new ServerSocket(50101);
		}catch(Exception e) {
			System.out.println("Exception from ServerUI");
		}
		setVisible(true);
	}
	
	public void acceptConnection(ServerUI serverUI) {
		try {
			serverUI.ta.append("Waiting for connecttions ....\n");
			while(numPlayers < 2) {
				s = serverSocket.accept();
				numPlayers++;
				serverUI.ta.append("Player "+ numPlayers + "has connected\n");
				ssc = new ServerSideConnection(s,numPlayers);
				if(numPlayers == 1) {
					player1 = ssc;
				}else if(numPlayers == 2) {
					player2 = ssc;
				}
				
				Thread t = new Thread(ssc);
				t.start();
				ssc.dataOutput.writeInt(numPlayers);
				ssc.dataOutput.flush();
			}
			System.out.println("now we have "+numPlayers +"player");
			
		}catch(IOException io) {}
	}
	
	public void acceptSelect(ServerUI serverUI) {
		try {
			
			int player = player1.dataInput.readInt();
			serverUI.ta.append("player "+player+" select....\n");
			int player2 = this.player2.dataInput.readInt();
			serverUI.ta.append("player "+player2+" select....\n");
			
		}catch(IOException io) {}
	}
	public static void main(String[] args) {
		ServerUI si = new ServerUI();
		si.acceptConnection(si);
		si.acceptSelect(si);
		si.acceptSelect(si);
	}
	
}

class ServerSideConnection implements Runnable{
	Socket socket;
	DataInputStream dataInput ;
	DataOutputStream dataOutput;
	int playerID;
	ServerSideConnection(Socket s , int id){
		socket = s;
		playerID = id;
		try {
			dataInput = new DataInputStream(socket.getInputStream());
			dataOutput= new DataOutputStream(socket.getOutputStream());
		}catch(IOException ex) {System.out.println("IOException from SSC");}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			dataOutput.writeInt(playerID);
			dataOutput.flush();
		}catch(IOException ex) {System.out.println("IOException from run() SSC");} 
	}
	
}
