package xinghanw_CSCI201_Assignment2.Server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Assignment5RequestResponse implements Serializable {
	public static final long serialVersionUID = 5676646;
	private String type;
	private boolean isRequest;	
	//enum (request Type): ADDCOLLABORATOR, GET_REMOVABLE_USERS, DELETE_USER, GET_COLLABORATING_FILES, UPDATE_FILES, SIGNAL_FINISH_SINGLE_CLIENT, SIGNAL_UPDATE_SINGLE_CLIENT
	//enum (response Type): ADDCOLLABORATOR_RESULT, GET_REMOVABLE_USERS_RESULT, DELETE_USER_RESULT,  GET_COLLABORATING_FILES_RESULT, ASK_TO_UPDATE, MergeResult
	
	private String collaboratorName;
	private String ownerName;
	private String fileName;
	private String fileContent;
	private String addCollaborator_result_msg;
	private String[] collaborators;
	private HashMap<String, ArrayList<String>> owner_File_Hashmap;
	private Vector<Assignment5RequestResponse> allUpdates = new Vector<Assignment5RequestResponse>();
	
	public Assignment5RequestResponse() {
		
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public boolean isRequest() {
		return isRequest;
	}


	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}


	public String getCollaboratorName() {
		return collaboratorName;
	}


	public void setCollaboratorName(String collaboratorName) {
		this.collaboratorName = collaboratorName;
	}


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getAddCollaborator_result_msg() {
		return addCollaborator_result_msg;
	}


	public void setAddCollaborator_result_msg(String addCollaborator_result_msg) {
		this.addCollaborator_result_msg = addCollaborator_result_msg;
	}


	public String[] getCollaborators() {
		return collaborators;
	}


	public void setCollaborators(String[] collaborators) {
		this.collaborators = collaborators;
	}


	public HashMap<String, ArrayList<String>> getOwner_File_Hashmap() {
		return owner_File_Hashmap;
	}


	public void setOwner_File_Hashmap(HashMap<String, ArrayList<String>> owner_File_Hashmap) {
		this.owner_File_Hashmap = owner_File_Hashmap;
	}


	public String getFileContent() {
		return fileContent;
	}


	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}


	@Override
	public String toString() {
		return "Assignment5RequestResponse [type=" + type + ", isRequest=" + isRequest + ", ownerName=" + ownerName
				+ ", fileName=" + fileName + ", fileContent=" + fileContent + "]";
	}


	public Vector<Assignment5RequestResponse> getAllUpdates() {
		return allUpdates;
	}


	public void setAllUpdates(Vector<Assignment5RequestResponse> allUpdates) {
		this.allUpdates = allUpdates;
	}
	
	public void addUpdates(Assignment5RequestResponse request){
		this.allUpdates.addElement(request);
	}
	
	
	
	
	
	
	

}
