package xinghanw_CSCI201_Assignment2.Server;

import java.io.Serializable;

public class ServerRequest implements Serializable {
	public static final long serialVersionUID = 123;
	private String username;
	private String password;
	private boolean isLoginRequest;
	
	public ServerRequest(String username, String password, boolean isLoginRequest){
		this.username = username;
		this.password = password;
		this.isLoginRequest = isLoginRequest;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoginRequest() {
		return isLoginRequest;
	}

	public void setLoginRequest(boolean isLoginRequest) {
		this.isLoginRequest = isLoginRequest;
	}
	
	
}
