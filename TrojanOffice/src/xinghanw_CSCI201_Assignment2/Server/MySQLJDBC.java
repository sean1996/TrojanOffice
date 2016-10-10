package xinghanw_CSCI201_Assignment2.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.PreparedStatement;

public class MySQLJDBC {
	private Connection con;
	
	private static final String  selectStatement = "SELECT * FROM USERAUTH WHERE userName =?";
	private final static String addStatement = "INSERT INTO USERAUTH(userName, userPassword) VALUES(?,?)";
	
	//Assignment 5 part SQL code
	private static final String selectStatement_FileCollaborate = "SELECT * FROM FILE_COLLABORATE WHERE ownerName = ? AND fileName = ? AND collaboratorName = ?";
	private static final String addStatement_FileCollaborate = "INSERT INTO FILE_COLLABORATE(fileName, ownerName, collaboratorName) VALUES(?,?,?)";
	
	private static final String selectStatement_RemoveColloborator = "SELECT * FROM FILE_COLLABORATE WHERE ownerName = ? AND fileName = ?";
	private static final String deleteStatement_FileCollaborate = "DELETE FROM FILE_COLLABORATE WHERE ownerName = ? AND fileName = ? AND collaboratorName = ?";
	
	private static final String selectStatement_getAllCollaborationFile = "SELECT * FROM FILE_COLLABORATE WHERE collaboratorName = ?";
	
	private static final String selectStatement_FileOwner = "SELECT * FROM FILE_OWNER WHERE fileName = ? AND ownerName = ?";
	private static final String addStatement_FileOwner = "INSERT INTO FILE_OWNER(fileName, ownerName) VALUES(?,?)";
	
	public MySQLJDBC(){
		try{
			new com.mysql.jdbc.Driver();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void connect(){
		try{
			con = DriverManager.getConnection("jdbc:mysql://localhost/xinghanw?user=root&password=xinghan333");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try{
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean doesExist(ServerRequest request){
		String userName = request.getUsername();
		
		try{
			PreparedStatement pStatement = con.prepareStatement(selectStatement);
			pStatement.setString(1, userName);
			ResultSet resultSet = pStatement.executeQuery();
			while(resultSet.next()){
				System.out.println("Database already exist user: " + userName);
				return true;
			}
		}catch (SQLException e) {
				e.printStackTrace();
		}
		System.out.println(userName + " is not in database yet");
		return false;	
	}
	
	public void add(ServerRequest request){
		String userName = request.getUsername();
		String password = request.getPassword();
		try{
			PreparedStatement pStatement = con.prepareStatement(addStatement);
			pStatement.setString(1, userName);
			pStatement.setString(2, password);
			pStatement.executeUpdate();
			System.out.println("Regiser user: " + userName +"to the server");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean authenticate(ServerRequest request){
		String userName = request.getUsername();
		String password = request.getPassword();
		try{
			PreparedStatement pStatement = con.prepareStatement(selectStatement);
			pStatement.setString(1, userName);
			ResultSet resultSet = pStatement.executeQuery();
			while(resultSet.next()){
				if(password.equals(resultSet.getString("userPassword"))){
					return true;
				}
			}
		}catch (SQLException e) {
				e.printStackTrace();
		}
		return false;
	}
	
	public String addCollaborator(Assignment5RequestResponse request){
		String owner = request.getOwnerName();
		String collaboratorName = request.getCollaboratorName();
		String fileName = request.getFileName();
		
		//First, query if the user exists in userAuth table
		try{
			PreparedStatement pStatement = con.prepareStatement(selectStatement);
			pStatement.setString(1, collaboratorName);
			ResultSet resultSet = pStatement.executeQuery();
			if(!resultSet.next()){
				return "The user does not exist!";	//if nothing is in the result set, the2 user does not exist  in the databse
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Secondly, query if the user is already an collaborator for the given file
		try{
			PreparedStatement pStatement = con.prepareStatement(selectStatement_FileCollaborate);
			pStatement.setString(1, owner);
			pStatement.setString(2, fileName);
			pStatement.setString(3, collaboratorName);
			ResultSet resultSet = pStatement.executeQuery();
			if(resultSet.next()){
				//that means the collaborator's already been added
				System.out.println(resultSet.getString("collaboratorName") + "is already a collaborator to file " + resultSet.getString("fileName"));
				return collaboratorName + " is already a collaborator to file: " + fileName;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Lastly, we add the user as a collaborator to database's records
		try{
			PreparedStatement pStatement = con.prepareStatement(addStatement_FileCollaborate);
			pStatement.setString(1, fileName);
			pStatement.setString(2, owner);
			pStatement.setString(3, collaboratorName);
			pStatement.executeUpdate();
			System.out.println("Add record of new collaborator to database");
			return "success";
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "success";
		
	}
	
	public ArrayList<String> getCollaborator(Assignment5RequestResponse request){
		String owner = request.getOwnerName();
		String fileName = request.getFileName();
		ArrayList<String> allColaborators = new ArrayList<String>();
		
		//query the database 
		try{
			PreparedStatement pStatement = con.prepareStatement(selectStatement_RemoveColloborator);
			pStatement.setString(1, owner);
			pStatement.setString(2, fileName);
			ResultSet resultSet = pStatement.executeQuery();
			while(resultSet.next()){
				allColaborators.add(resultSet.getString("collaboratorName"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return allColaborators;
	}
	
	public boolean removeUser(Assignment5RequestResponse request){
		String owner = request.getOwnerName();
		String fileName = request.getFileName();
		String collaboratorName = request.getCollaboratorName();
		try{
			PreparedStatement pStatement = con.prepareStatement(deleteStatement_FileCollaborate);
			pStatement.setString(1, owner);
			pStatement.setString(2, fileName);
			pStatement.setString(3, collaboratorName);
			pStatement.executeUpdate();
			System.out.println("Add record of new collaborator to database");
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public HashMap<String, ArrayList<String>> getCollaborationFile(Assignment5RequestResponse request){
		String collaborator = request.getCollaboratorName();
		HashMap<String, ArrayList<String>> owner_File_Map = new HashMap<String,ArrayList<String>>(0);
		
		try{
			PreparedStatement pStatement = con.prepareStatement(selectStatement_getAllCollaborationFile);
			pStatement.setString(1, collaborator);
			ResultSet resultSet = pStatement.executeQuery();
			while(resultSet.next()){
				if(owner_File_Map.containsKey(resultSet.getString("ownerName"))){
					owner_File_Map.get(resultSet.getString("ownerName")).add(resultSet.getString("fileName"));
				}else{
					ArrayList<String> files = new ArrayList<String>();
					files.add(resultSet.getString("fileName"));
					owner_File_Map.put(resultSet.getString("ownerName"), files);
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return owner_File_Map;
	}
	
	public void writeFile_OwnerPair(String fileName, String ownerName){
		//first, check if the currentpair exist in the records first
		try{
			PreparedStatement pStatement = con.prepareStatement(selectStatement_FileOwner);
			pStatement.setString(1, fileName);
			pStatement.setString(2, ownerName);
			ResultSet resultSet = pStatement.executeQuery();
			while(resultSet.next()){
				System.out.println("record already exists");
				return;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		//second, add the current pair to the database
		try{
			PreparedStatement pStatement = con.prepareStatement(addStatement_FileOwner);
			pStatement.setString(1, fileName);
			pStatement.setString(2, ownerName);
			pStatement.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
