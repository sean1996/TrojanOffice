package xinghanw_CSCI201_Assignment2.Server;

import java.util.LinkedList;
import java.util.Vector;

import xinghanw_CSCI201_Assignment2.ThridPartyAPI.diff_match_patch;
import xinghanw_CSCI201_Assignment2.ThridPartyAPI.diff_match_patch.Patch;

public class Merger_Assignment5 {
	private String lastMerge = new String("");
	private Vector<String> newUpdateFromUsers = new Vector<String>();
	
	public Merger_Assignment5() {
	}

	public String getLastMerge() {
		return lastMerge;
	}

	public void setLastMerge(String lastMerge) {
		this.lastMerge = lastMerge;
	}

	public Vector<String> getNewUpdateFromUsers() {
		return newUpdateFromUsers;
	}

	public void setNewUpdateFromUsers(Vector<String> newUpdateFromUsers) {
		this.newUpdateFromUsers = newUpdateFromUsers;
	}

	public void addNewUpdateFromUsers(String update){
		synchronized (newUpdateFromUsers) {
			newUpdateFromUsers.addElement(update);
		}
	}
	
	public Assignment5RequestResponse mergeUpdates(String username, String filename){
		synchronized (newUpdateFromUsers) {
			String merge = this.lastMerge;
			diff_match_patch dmp = new diff_match_patch();
			for(String udpate: newUpdateFromUsers){
				LinkedList<Patch> patch1 = dmp.patch_make(lastMerge, udpate);
				merge = (String)(dmp.patch_apply(patch1, merge)[0]);
			}
			//after merging updates for this file, update last merge and clean up the vector
			newUpdateFromUsers.clear();
			this.lastMerge = merge;
			//send the result back
			System.out.println("afterMerging, the result is: " + lastMerge);
			Assignment5RequestResponse response = new Assignment5RequestResponse();
			response.setRequest(false);
			response.setOwnerName(username);
			response.setFileName(filename);
			response.setFileContent(merge);
			response.setType("MergeResult");
			return response;
		}
	}
	
	
}
