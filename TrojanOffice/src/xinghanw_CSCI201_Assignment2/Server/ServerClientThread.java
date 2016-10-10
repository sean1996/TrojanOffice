package xinghanw_CSCI201_Assignment2.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerClientThread extends Thread {
	
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private ProgramServer server;
	private Socket socket;
	
	private static Lock lock = new ReentrantLock();

	
	public ServerClientThread(Socket socket, ProgramServer server){
		try{
			this.server = server;
			this.socket = socket;
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());	
			objectInputStream = new ObjectInputStream(socket.getInputStream());	
			this.start();
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
			//get thrown maybe when client drop the exception
		}
	}
	
	
	@Override
	public void run() {
		//constantly listen for client's request, and query to server
		try{
			while(true){
				Object object = objectInputStream.readObject();
				if(object instanceof ServerRequest){
					ServerRequest request = (ServerRequest)object;
					System.out.println("get a request");
					ServerResponse response;
					if(request.isLoginRequest()){
						response = queryServer(request);
						if(response.isSuccess()){
							server.serverGUI.addNewLineToLogBoard("Log-in Success: User " + request.getUsername());
						}else{
							server.serverGUI.addNewLineToLogBoard("Log-in Failure: User " + request.getUsername());
						}
						sendResponseBack_File(response);
					}else if(!request.isLoginRequest()){
						//sign-up request
						response = queryServer(request);
						
						if(response.isSuccess()){
							//update the log board accordingly
							server.serverGUI.addNewLineToLogBoard("Sign-up Success: User " + request.getUsername());
							//make the directory for the new user
							makeNewDirectory(request.getUsername());
							
						}else{
							//update the log board accordingly
							server.serverGUI.addNewLineToLogBoard("Sign-up Failure: User " + request.getUsername());
						}
						sendResponseBack_File(response);
					}
				}else if(object instanceof FileRequestResponse){
					FileRequestResponse fileRequest = (FileRequestResponse)object;
					if(fileRequest.isRequest()){
						if(fileRequest.isFileNameRequest()){
							//return all available files this user can open
							File directory = new File("userFiles/" + fileRequest.getUserName());
							String[] availableFiles = new String[]{};
							availableFiles = directory.list();
							FileRequestResponse response = new FileRequestResponse(false);
							response.setFileNameReponse(true);
							response.setFileSavingResultReponse(false);
							response.setAvailableFiles(availableFiles);
							response.setFileSaveDialog(fileRequest.isFileSaveDialog());
							sendResponseBack_File(response);
						}
						else if(!fileRequest.isFileNameRequest()){
							if(fileRequest.isSaveFileRequest()){
								saveFile(fileRequest);
							}else{
								sendFile(fileRequest);
							}
							
						}
					}
				}else if(object instanceof Assignment5RequestResponse){
					Assignment5RequestResponse request = (Assignment5RequestResponse) object;
					processRequest(request);
				}
				
			}
		}catch(IOException ioe){
		}catch (ClassNotFoundException e) {
		}finally {
			server.removeThread_concurrent(this);
		}
	}
	
	private ServerResponse queryServer(ServerRequest request){
		//add new log to log board
		StringBuilder stringBuilder = new StringBuilder();
		if(request.isLoginRequest()){
			stringBuilder.append("Log-in Attempt: ");
		}else{
			stringBuilder.append("Sign-up Attempt: ");
		}
		stringBuilder.append("User: " + request.getUsername() +" Password: " + request.getPassword());
		server.serverGUI.addNewLineToLogBoard(stringBuilder.toString());
		//query to databse
		MySQLJDBC jdbc = new MySQLJDBC();
		jdbc.connect();
		if(request.isLoginRequest()){
			if(jdbc.authenticate(request)){
				jdbc.stop();
				return new ServerResponse(true, true, "Sign-in Success", request.getUsername());
			}else{
				jdbc.stop();
				return new ServerResponse(false, true, "", request.getUsername());
			}
		}else{
			//registration request
			if(jdbc.doesExist(request)){
				//userName already exist in the server, registration fails
				jdbc.stop();
				return new ServerResponse(false, false, "USERNAME ALREADY EXIST IN DATABSE", request.getUsername());
			}else{
				jdbc.add(request);
				jdbc.stop();
				return new ServerResponse(true, false, "Sign-up Success", request.getUsername());
			}
		}
			
	}

	public synchronized void sendResponseBack_File(Object response){
		try {
			objectOutputStream.writeObject(response);
			objectOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void makeNewDirectory(String username){
		File dir = new File("userFiles/" + username);
		boolean successs = dir.mkdir();
		if(successs){
			System.out.println("Successfully create directory for user " + username);
		}else{
			System.out.println("Failed to create directory for user " + username);
		}
		
	}
	
	private void saveFile(FileRequestResponse fileRequest){
		//in this case, user request to save the file content
		String filename = fileRequest.getFileName();
		String content = fileRequest.getFileContent();
		System.out.println("get saved request " + filename);
		System.out.println("content " + content);
		try {
			FileOutputStream overwriteStream = new FileOutputStream("userFiles/" + fileRequest.getUserName()+"/"+filename, false);
			overwriteStream.write(content.getBytes());
			overwriteStream.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
			server.serverGUI.addNewLineToLogBoard("Fail to save the file. User: " + fileRequest.getUserName() + " File: " + filename);
			//create a new response notifying the client about the successfulness of saving files
			FileRequestResponse response = new FileRequestResponse(false);
			response.setFileNameReponse(false);
			response.setFileSavingResultReponse(true);
			response.setSuccessful(false);
			sendResponseBack_File(response);
		}
		server.serverGUI.addNewLineToLogBoard("File saved. User: " + fileRequest.getUserName() + " File: " + filename);
		//write a record to the databsae
		MySQLJDBC jdbc = new MySQLJDBC();
		jdbc.connect();
		jdbc.writeFile_OwnerPair(filename, fileRequest.getUserName());
		jdbc.stop();
		
		
		//create a new response notifying the client about the successfulness of saving files
		FileRequestResponse response = new FileRequestResponse(false);
		response.setFileNameReponse(false);
		response.setFileSavingResultReponse(true);
		response.setSuccessful(true);
		response.setFileName(filename);
		sendResponseBack_File(response);
	}
	
	private synchronized void sendFile(FileRequestResponse fileRequest){
		//in this case, user request to open the file
		String filename = fileRequest.getFileName();
		String userName = fileRequest.getUserName();
		String content = null;
		try {
			 content = new String(Files.readAllBytes(Paths.get("userFiles/" + userName + "/" + filename)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Fail to get the content for the file that user choose");
			server.serverGUI.addNewLineToLogBoard("File Download FAILED. User: " + fileRequest.getUserName() + " File: " + filename);
		}
		server.serverGUI.addNewLineToLogBoard("File Downloaded. User: " + fileRequest.getUserName() + " File: " + filename);
		FileRequestResponse response = new FileRequestResponse(false);
		response.setFileNameReponse(false);
		response.setFileSavingResultReponse(false);
		response.setUserName(userName);
		response.setFileName(filename);
		response.setFileContent(content);
		sendResponseBack_File(response);
	}
	
	public void closeSocketInThisThread(){
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Assignement 5 part
	
	private void processRequest(Assignment5RequestResponse request){
		MySQLJDBC jdbc = new MySQLJDBC();
		jdbc.connect();
		if(request.isRequest()){	//it's a request！
			if(request.getType().equalsIgnoreCase("ADDCOLLABORATOR")){	//client is request to add a collaborator!
				String result = jdbc.addCollaborator(request);
				if(!result.equalsIgnoreCase("success")){
					Assignment5RequestResponse response = new Assignment5RequestResponse();
					response.setRequest(false);
					response.setType("ADDCOLLABORATOR_RESULT");
					response.setAddCollaborator_result_msg(result);
					sendResponseBack_File(response);
				}
			}else if(request.getType().equalsIgnoreCase("GET_REMOVABLE_USERS")){ //client is requesting to get all the collaborator of a specific file they own!
				System.out.println("1");
				ArrayList<String> collaborators = jdbc.getCollaborator(request);
				System.out.println(collaborators.toString());
				String[] options = new String[collaborators.size()];
				
				options = collaborators.toArray(options);
				System.out.println(options.toString());
				Assignment5RequestResponse response = new Assignment5RequestResponse();
				response.setRequest(false);
				response.setType("GET_REMOVABLE_USERS_RESULT");
				response.setCollaborators(options);
				sendResponseBack_File(response);
			}else if(request.getType().equalsIgnoreCase("DELETE_USER")){
				if(jdbc.removeUser(request)){
					//that means the user is succesfully removed, send a response back to clients
					Assignment5RequestResponse response = new Assignment5RequestResponse();
					System.out.println("Successfully delete the user!");
					response.setRequest(false);
					response.setFileName(request.getFileName());
					response.setOwnerName(request.getOwnerName());
					System.out.println("getOwnerName " + request.getOwnerName());
					response.setCollaboratorName(request.getCollaboratorName());
					System.out.println("getCollaborator name " + request.getCollaboratorName());
					response.setType("DELETE_USER_RESULT");
					server.broadcastToAllClient(response);
				};
			}else if(request.getType().equalsIgnoreCase("GET_COLLABORATING_FILES")){
				System.out.println("get request from GET_COLLABORTING_FILES from" + request.getCollaboratorName());
				HashMap<String, ArrayList<String>> owner_File_hashmap = jdbc.getCollaborationFile(request);
				Assignment5RequestResponse response = new Assignment5RequestResponse();
				response.setRequest(false);
				response.setCollaboratorName(request.getCollaboratorName());
				response.setType("GET_COLLABORATING_FILES_RESULT");
				response.setOwner_File_Hashmap(owner_File_hashmap);
				sendResponseBack_File(response);
			}else if(request.getType().equalsIgnoreCase("SINGLE_CLIENT_UPDATE_VECTOR")){	
				if(!server.getUpdateThreadMain().finished){
					System.out.println("2.getting back result from server." + "The nubmber of client update received are: " + server.getUpdateThreadMain().getCounter()+1 );
					Thread updateMapThread = new updateMapThread(server, request);
					try{
						updateMapThread.start();
						updateMapThread.join();
					}catch(InterruptedException ie){
						ie.printStackTrace();
					}
					System.out.println("3.finish update one user.");
					server.getUpdateThreadMain().incrementCounter();	
				}else{
					System.out.println("Got update when request haven't been sent yet, discard");
				}
					
			}
		}
		jdbc.stop();
	}
}
