package xinghanw_CSCI201_Assignment2.Server;

import java.util.Map;

public class updateMapThread extends Thread {
	private ProgramServer server;
	private Assignment5RequestResponse updateVector;
	
	public updateMapThread(ProgramServer server, Assignment5RequestResponse updateVector) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.updateVector = updateVector;
	}
	
	@Override
	public void run() {
		synchronized (server.updateHashmap) {
			System.out.println("2.0 - There are " + updateVector.getAllUpdates().size() +" file updates from this user");
			int counter = 0;
			for(Assignment5RequestResponse request: updateVector.getAllUpdates()){
				userNamefileNamePair pair = new userNamefileNamePair(request.getOwnerName(), request.getFileName());
				boolean exist = false;
				for(Map.Entry<userNamefileNamePair, Merger_Assignment5> entry: server.updateHashmap.entrySet()){
					if(entry.getKey().equals(pair)){
						System.out.println(request.getOwnerName() + " -  " + request.getFileName() + " is in hashmap.");
						server.updateHashmap.get(entry.getKey()).addNewUpdateFromUsers(request.getFileContent());
						System.out.println("after update: " + server.updateHashmap.get(entry.getKey()).getNewUpdateFromUsers().toString());
						exist = true;
						break;
					}
				}
				if(!exist){
					System.out.println(request.getOwnerName() + "-  " + request.getFileName() + "is not in hashmap");
					Merger_Assignment5 merger_Assignment5 = new Merger_Assignment5();
					merger_Assignment5.addNewUpdateFromUsers(request.getFileContent());
					server.updateHashmap.put(pair, merger_Assignment5);	
				}
				counter = counter + 1;
				System.out.println("2.1 - finish update file " + counter);
			}
			System.out.println("2.2 - finish update single user");
		}
		
	}
}
