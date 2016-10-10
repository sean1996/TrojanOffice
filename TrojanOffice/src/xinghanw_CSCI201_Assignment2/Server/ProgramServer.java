package xinghanw_CSCI201_Assignment2.Server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProgramServer implements Runnable {
	
	private int portNumber = 0;
	private int udpateInterval = 3000;
	private String hostName = "";
	ServerSocket serverSocket = null;
	ProgramServerGUI serverGUI;
	boolean terminate = false;
	public static Vector<ServerClientThread> clientThreads = new Vector<ServerClientThread>(0);
	private updateThread updateThreadMain;
	public static ConcurrentHashMap<userNamefileNamePair, Merger_Assignment5> updateHashmap = new ConcurrentHashMap<userNamefileNamePair, Merger_Assignment5>();
	
	public ProgramServer(String configurationFilePath) {
		serverGUI = new ProgramServerGUI(this);
		parseConfigFile(configurationFilePath);
		updateThreadMain = new updateThread(udpateInterval, this);
		updateThreadMain.start();
		new autoSavingThread(this).start();
	}
	
	
	
	private void parseConfigFile(String path){
		FileReader freader = null;
		BufferedReader bReader = null;
		Scanner PortnumberScanner = null;
		String nextLine;
		try {
			freader = new FileReader(path);
			bReader = new BufferedReader(freader);
			
			while((nextLine = bReader.readLine()) != null){
				if(nextLine.startsWith("portnumber:")){
					int index = nextLine.indexOf(':');
					PortnumberScanner = new Scanner(nextLine.substring(index + 1));
					StringBuilder sBuilder = new StringBuilder();
					while(PortnumberScanner.hasNextInt()){
						sBuilder.append(PortnumberScanner.nextInt());
					}
					portNumber = Integer.parseInt(sBuilder.toString());
				}
				if(nextLine.startsWith("updateInterval(ms):")){
					int index = nextLine.indexOf(':');
					PortnumberScanner = new Scanner(nextLine.substring(index + 1));
					StringBuilder sBuilder = new StringBuilder();
					while(PortnumberScanner.hasNextInt()){
						sBuilder.append(PortnumberScanner.nextInt());
					}
					udpateInterval = Integer.parseInt(sBuilder.toString());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (freader != null) {freader.close();}
				if (bReader != null) {bReader.close();}
				if(PortnumberScanner!= null){PortnumberScanner.close();}
			} catch (IOException ioe) {
				System.out.println("IOException closing file: " + ioe.getMessage());
			}	
		}
		
	}
	
	@Override
	public void run() {
		try{
			serverSocket = new ServerSocket(portNumber);
			serverGUI.textArea.setText(serverGUI.textArea.getText() + "Server started on port " + portNumber + "\n" );
			while(true){
				//start listening
				Socket socket = serverSocket.accept();
				ServerClientThread clientThread = new ServerClientThread(socket, this);
				clientThreads.add(clientThread);				
			}
		}catch(IOException ioe){
			System.out.println("socket is closed. Stop listening.");
		}finally {

		}
	}
	
	protected void stopListening() {
		if(serverSocket != null){
			try{
				serverSocket.close();
				System.out.println("Closed the serverSocket");
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}
		}
		for(ServerClientThread serverClientThread: clientThreads){
			removeThread(serverClientThread);
		}
		clientThreads.clear();
		
	}
	
	public static void main(String[] args){
		new ProgramServer("serverConfig.txt");
	}
	
	public void removeThread(ServerClientThread clientThread){
		clientThread.interrupt();
		clientThread.closeSocketInThisThread();
	}
	
	public void removeThread_concurrent(ServerClientThread clientThread){
		clientThread.interrupt();
		clientThread.closeSocketInThisThread();
		clientThreads.remove(clientThread);
	}
	
	public void broadcastToAllClient(Assignment5RequestResponse response){
		synchronized (clientThreads) {
			for(ServerClientThread clientThread: clientThreads){
				clientThread.sendResponseBack_File(response);
			}
		}
	}



	public updateThread getUpdateThreadMain() {
		return updateThreadMain;
	}



	public void setUpdateThreadMain(updateThread updateThreadMain) {
		this.updateThreadMain = updateThreadMain;
	}
	
//	public void updateTheNewHashmap(Assignment5RequestResponse request){
//		synchronized (updateHashmap) {
//			userNamefileNamePair pair = new userNamefileNamePair(request.getOwnerName(), request.getFileName());
//			for(Map.Entry<userNamefileNamePair, Merger_Assignment5> entry: updateHashmap.entrySet()){
//				if(entry.getKey().equals(pair)){
//					System.out.println("hashmap does contain " + request.getOwnerName() + ",  " + request.getFileName() + "updating");
//					updateHashmap.get(entry.getKey()).addNewUpdateFromUsers(request.getFileContent());
//					System.out.println("after update: " + updateHashmap.get(entry.getKey()).getNewUpdateFromUsers().toString());
//					return;
//				}
//			}
//			
//			System.out.println("hashmap does not contain " + request.getOwnerName() + ",  " + request.getFileName());
//			Merger_Assignment5 merger_Assignment5 = new Merger_Assignment5();
//			merger_Assignment5.addNewUpdateFromUsers(request.getFileContent());
//			updateHashmap.put(pair, merger_Assignment5);
//			
//		}
//	}
//	
//	public void mergeUpdates(){
//		synchronized (updateHashmap) {
//			for(Map.Entry<userNamefileNamePair, Merger_Assignment5> entry: updateHashmap.entrySet()){
//				Assignment5RequestResponse response = updateHashmap.get(entry.getKey()).mergeUpdates(entry.getKey().getUserName(), entry.getKey().getFileName());
//				broadcastToAllClient(response);
//			}			
//			System.out.println("here");
//			updateThreadMain.finishMerging.signalAll();
//		}
//	}
	
}
