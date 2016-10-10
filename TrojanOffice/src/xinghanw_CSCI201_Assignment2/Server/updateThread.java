package xinghanw_CSCI201_Assignment2.Server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class updateThread extends Thread {

	private int updateInaterval;
	private ProgramServer server;
	private int counter = 0;
	private static Lock lock = new ReentrantLock();
	private static Condition allClientsUpdate = lock.newCondition();
	public static Condition finishMerging = lock.newCondition();
	public static boolean finished = false;
 
	
	public updateThread(int interval, ProgramServer server) {
		this.updateInaterval = interval;
		this.server = server;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			lock.lock();
			try{
				if(server.clientThreads.capacity() != 0){
					finished = false;
					System.out.println("");
					System.out.println("----------------------------------");
					System.out.println("");
					System.out.println("0. send request.");
					Assignment5RequestResponse updateRequest = new Assignment5RequestResponse();
					updateRequest.setRequest(false);
					updateRequest.setType("ASK_TO_UPDATE");
					server.broadcastToAllClient(updateRequest);
					System.out.println("1. Start to await.");
					allClientsUpdate.await();
					//here all the clients have finished update in that round
					System.out.println("6.after merging, notify udpateThread");
				}
				finished = true;
				Thread.sleep(updateInaterval);	
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
		}
				
			
				
		
		
		
	}
	
	public void incrementCounter(){
		lock.lock();
		try{
			counter = counter + 1;
//			System.out.println("update the current counter to" + counter + " Current capacity: "+ ProgramServer.clientThreads.capacity());
			if(counter >= ProgramServer.clientThreads.size()){
				counter = 0;
				System.out.println("larger than capacity");
				if(!ProgramServer.updateHashmap.isEmpty()){
					//IF the updatehashmap is not empty, it needs to be merged
					//wait till the merging is finished before it signifies the current thread to send out another round of update request
//					try {
//						
//						System.out.println("waiting for merging!");
//						
//						
//						finishMerging.await();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					System.out.println("4. finish update all users in map, start merging");
					 Thread thread = new mergeThread(server);
					 try {
						thread.start();
						thread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("5.Finish merging!");
				allClientsUpdate.signal();
			}
		}finally{
			lock.unlock();
		}
		
	}
	
	public int getCounter(){
		return this.counter;
	}
	
}
