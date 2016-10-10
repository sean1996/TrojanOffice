package xinghanw_CSCI201_Assignment2.Server;

import java.io.Serializable;

public class FileRequestResponse implements Serializable {
	public static final long serialVersionUID = 567;
	private boolean isRequest;
	private boolean isFileNameReponse;	//this is true when the reponse contains avaibleFilenames, false if response contains fileContent
	private boolean fileSaveDialog; //this is true when the reponse contains avaiable Filenames and for saving dialog, false if reponse is for opening dialog

	private boolean isSaveFileRequest; //this is true when user request server to save the file(Upload), false when user request server to send back to file(Download).
	private boolean isFileSavingResultReponse; // this is true is the reponse contains the reuslt of saving files
	private boolean successful;
	private boolean isFileNameRequest; //this is true when the client requests avaibleFilenames, false if clients send fileContent;

	private String  userName;

	private String fileContent;
	private String fileName;
	private String[] availableFiles;
	
	
	public FileRequestResponse(boolean isRequest){
		this.isRequest  = isRequest;
	}


	public boolean isRequest() {
		return isRequest;
	}


	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}


	public String getFileContent() {
		return fileContent;
	}


	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String[] getAvailableFiles() {
		return availableFiles;
	}


	public void setAvailableFiles(String[] availableFiles) {
		this.availableFiles = availableFiles;
	}


	public boolean isFileNameReponse() {
		return isFileNameReponse;
	}


	public void setFileNameReponse(boolean isFileNameReponse) {
		this.isFileNameReponse = isFileNameReponse;
	}


	public boolean isFileNameRequest() {
		return isFileNameRequest;
	}


	public void setFileNameRequest(boolean isFileNameRequest) {
		this.isFileNameRequest = isFileNameRequest;
	}
	public boolean isFileSavingResultReponse() {
		return isFileSavingResultReponse;
	}


	public void setFileSavingResultReponse(boolean isFileSavingResultReponse) {
		this.isFileSavingResultReponse = isFileSavingResultReponse;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public boolean isSuccessful() {
		return successful;
	}


	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}


	public boolean isFileSaveDialog() {
		return fileSaveDialog;
	}


	public void setFileSaveDialog(boolean fileSaveDialog) {
		this.fileSaveDialog = fileSaveDialog;
	}


	public boolean isSaveFileRequest() {
		return isSaveFileRequest;
	}


	public void setSaveFileRequest(boolean isSaveFileRequest) {
		this.isSaveFileRequest = isSaveFileRequest;
	}



	
}
