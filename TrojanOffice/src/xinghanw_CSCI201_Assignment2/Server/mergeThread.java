package xinghanw_CSCI201_Assignment2.Server;

import java.util.Map;

public class mergeThread extends Thread {
	private ProgramServer server;

	public mergeThread(ProgramServer server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		synchronized (server.updateHashmap) {
			for(Map.Entry<userNamefileNamePair, Merger_Assignment5> entry: server.updateHashmap.entrySet()){
				Assignment5RequestResponse response = server.updateHashmap.get(entry.getKey()).mergeUpdates(entry.getKey().getUserName(), entry.getKey().getFileName());
				server.broadcastToAllClient(response);
			}			
			System.out.println("here");
			//server.getUpdateThreadMain().finishMerging.signalAll();
		}
	}
	
}
