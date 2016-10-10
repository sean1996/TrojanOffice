package xinghanw_CSCI201_Assignment2.Server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class autoSavingThread extends Thread {

	private int saveInterval = 5000;
	private ProgramServer server;
	private String filenameTBS, ownerNameTBS, contentTBS;
	
	public autoSavingThread(ProgramServer server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		while(true){
			synchronized (server.updateHashmap) {
				for(Map.Entry<userNamefileNamePair, Merger_Assignment5> entry: server.updateHashmap.entrySet()){
					filenameTBS = entry.getKey().getFileName();
					ownerNameTBS = entry.getKey().getUserName();
					contentTBS = entry.getValue().getLastMerge();
					
					//try to save the file to user's account
					try {
						FileOutputStream overwriteStream = new FileOutputStream("userFiles/" + ownerNameTBS +"/"+filenameTBS, false);
						overwriteStream.write(contentTBS.getBytes());
						overwriteStream.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			try{
				sleep(saveInterval);
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
		}
		
	}
	
}
