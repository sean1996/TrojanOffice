package xinghanw_CSCI201_Assignment2.Server;

import java.io.Serializable;

public class ServerResponse implements Serializable {
	public static final long serialVersionUID = 123456;
	private boolean success;
	private boolean isLogin;
	private String message;
	private String username;
	
	
	public ServerResponse(boolean success, boolean isLogin, String message, String username){
		this.message = message;
		this.isLogin  = isLogin;
		this.success = success;
		this.username = username;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public boolean isLogin() {
		return isLogin;
	}


	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
