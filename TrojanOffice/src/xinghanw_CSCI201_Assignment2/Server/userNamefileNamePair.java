package xinghanw_CSCI201_Assignment2.Server;

public class userNamefileNamePair {
	private String userName;
	private String fileName;
	
	public userNamefileNamePair() {
		
	}
	
	public userNamefileNamePair(String userName, String fileName){
		this.fileName = fileName;
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		userNamefileNamePair beingCompard = (userNamefileNamePair)obj;
		if(beingCompard.getFileName().equalsIgnoreCase(fileName) && beingCompard.getUserName().equalsIgnoreCase(userName)){
			return true;
		}else{
			return false;
		}
	}
	
}
