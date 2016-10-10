package xinghanw_CSCI201_Assignment2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.xml.soap.Text;

import xinghanw_CSCI201_Assignment2.CustomGUI.MyButton;
import xinghanw_CSCI201_Assignment2.CustomGUI.MyPanel;
import xinghanw_CSCI201_Assignment2.CustomGUI.OnlineFileOpener;
import xinghanw_CSCI201_Assignment2.CustomGUI.OnlineFilechooser;
import xinghanw_CSCI201_Assignment2.CustomGUI.openFileChooserA5;
import xinghanw_CSCI201_Assignment2.CustomGUI.removeCollaboratorDialog;
import xinghanw_CSCI201_Assignment2.Server.Assignment5RequestResponse;
import xinghanw_CSCI201_Assignment2.Server.FileRequestResponse;
import xinghanw_CSCI201_Assignment2.Server.MD5;
import xinghanw_CSCI201_Assignment2.Server.ServerRequest;
import xinghanw_CSCI201_Assignment2.Server.ServerResponse;

public class LoginGUI extends JFrame implements Runnable {
	public static final long serialVersionUID = 12345;
	private TextEditorMain editorMain;
	
	private static final int windowLength = 800;
	private static final int windowWidth = 600;
	
	private MyButton login_main, signup_main, offline, login_detail, signup_detail; 
	private JTextField usernameField_signup, usernameField_login;
	private JPasswordField passwordField_signup, passwordField_login, passwordField_Repeat;
	private Font customFont, customFontBigger;
	private MyPanel mainPanel;
	private MyPanel threeButtonSubPanel, loginSubPanel, signupSubPanel;
	
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream obOutputStream;
	private String hostname;
	private int portnumber;
	private Socket socket = null;
	
	
	
	public LoginGUI(TextEditorMain editor){
		//set look and feel
		super("Trojan Office");
		try {
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch (Exception e){
		System.out.println("Warning! Cross-platform L&F not used!");
		}
	
		//change global fonts
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("kenvector_future.ttf")).deriveFont(12f);
			customFontBigger = Font.createFont(Font.TRUETYPE_FONT, new File("kenvector_future.ttf")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("kenvector_future.ttf")));
			setUIFont (new javax.swing.plaf.FontUIResource(customFont));
		} catch (IOException e) {
    	 e.printStackTrace();
		}
		catch(FontFormatException e)
		{
			e.printStackTrace();
		}
		
		editorMain = editor;
		
		
		instantiateComponents();
		createGUI();
		addActions();
		setLocation(500, 300);
		setSize(windowLength, windowWidth);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//parse the configuration file
		parseConfigurationFile("clientConfig.txt");
		//create socket and start listening for result
		createSocket();
	}
	
	private void instantiateComponents(){
		login_main = new MyButton("LOGIN");
		signup_main = new MyButton("SIGNUP");
		offline = new MyButton("OFFLINE");
		login_detail = new MyButton("LOGIN");
		signup_detail = new MyButton("LOGIN");
		mainPanel  = new MyPanel(customFontBigger);
		threeButtonSubPanel = new MyPanel();
		loginSubPanel = new MyPanel();
		signupSubPanel = new MyPanel();
		usernameField_signup = new JTextField(); 
		usernameField_signup.setPreferredSize(new Dimension(100, 20));
		usernameField_login = new JTextField();
		usernameField_login.setPreferredSize(new Dimension(100, 20));
		passwordField_login = new JPasswordField();
		passwordField_login.setPreferredSize(new Dimension(100, 20));
		passwordField_signup = new JPasswordField();
		passwordField_signup.setPreferredSize(new Dimension(100, 20));
		passwordField_Repeat = new JPasswordField();
		passwordField_Repeat.setPreferredSize(new Dimension(100, 20));
		
	}
	
	private void createGUI(){
		this.add(mainPanel);
		//configure threeButtonSubpanel
		threeButtonSubPanel.setLayout(new BoxLayout(threeButtonSubPanel, BoxLayout.Y_AXIS));
		MyPanel panel = new MyPanel();
		panel.add(login_main);
		panel.add(signup_main);
		threeButtonSubPanel.add(panel);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		offline.setAlignmentX(Component.CENTER_ALIGNMENT);
		threeButtonSubPanel.add(offline);
		
		//configure loginSubPanel
		loginSubPanel.setLayout(new BoxLayout(loginSubPanel, BoxLayout.Y_AXIS));
		MyPanel panelUsername_login = new MyPanel();
		MyPanel panelPassword_login = new MyPanel();
		JLabel labelUsername_login = new JLabel("UserName");
		JLabel labelPassword_login = new JLabel("Password");
		panelUsername_login.add(labelUsername_login);
		panelUsername_login.add(usernameField_login);
		panelPassword_login.add(labelPassword_login);
		panelPassword_login.add(passwordField_login);
		loginSubPanel.add(panelUsername_login);
		loginSubPanel.add(panelPassword_login);
		loginSubPanel.add(login_detail);
		
		//configure signupSubPanel
		signupSubPanel.setLayout(new BoxLayout(signupSubPanel, BoxLayout.Y_AXIS));
		MyPanel panelUsername_signup = new MyPanel();
		MyPanel panelPassword_signup = new MyPanel();
		MyPanel panelRepeat_signup = new MyPanel();
		JLabel labelUsername_signup = new JLabel("UserName");
		JLabel labelPassword_singup = new JLabel("Password");
		JLabel labelRepeat_singup = new JLabel("Repeat");
		panelUsername_signup.add(labelUsername_signup);
		panelUsername_signup.add(usernameField_signup);
		panelPassword_signup.add(labelPassword_singup);
		panelPassword_signup.add(passwordField_signup);
		panelRepeat_signup.add(labelRepeat_singup);
		panelRepeat_signup.add(passwordField_Repeat);
		signupSubPanel.add(panelUsername_signup);
		signupSubPanel.add(panelPassword_signup);
		signupSubPanel.add(panelRepeat_signup);
		signupSubPanel.add(signup_detail);
	
		//Add three panels to main panel	
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		mainPanel.add(threeButtonSubPanel,constraints);
		//threeButtonSubPanel.setVisible(false);
		mainPanel.add(loginSubPanel,constraints);
		loginSubPanel.setVisible(false);
		mainPanel.add(signupSubPanel,constraints);
		signupSubPanel.setVisible(false);
		
	}
	
	private void addActions(){
		//action Listeners for threeButtonSubpanel
		login_main.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				threeButtonSubPanel.setVisible(false);
				loginSubPanel.setVisible(true);
				
			}
		});
		
		signup_main.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				threeButtonSubPanel.setVisible(false);
				signupSubPanel.setVisible(true);
				
			}
		});
		
		offline.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginGUI.this.setVisible(false);
				editorMain.setVisible(true);
				editorMain.setOffline(true);
				editorMain.setUsername("guest");
			}
		});
		
		//action Listeners for signupSubpanel
		signup_detail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = new String(usernameField_signup.getText());
				String password = new String(passwordField_signup.getPassword());
				String repeat = new String(passwordField_Repeat.getPassword());
				if(!password.equals(repeat) || !checkValid(password)){
					if(!password.equals(repeat)){
						JOptionPane.showMessageDialog(mainPanel,
								"Two passwords are not the same",
								"Signup Failed",
								JOptionPane.WARNING_MESSAGE);
					}
					if(!checkValid(password)){
						JOptionPane.showMessageDialog(mainPanel,
								"PASSWORD MUST CONTAIN AT LEAST: 1-NUMBER AND 1-UPPERCASE LETTER",
								"Signup Failed",
								JOptionPane.WARNING_MESSAGE);
					}	
				}else{
					//register the user
					String encryptedPassword = MD5.crypt(password);
					ServerRequest request = new ServerRequest(username, encryptedPassword, false);
					RegisterOrAuthenciateUser(request);
					
				}
				
			}
		});
		//action Listeners for loginSubpanel
		login_detail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = new String(usernameField_login.getText());
				String password = new String(passwordField_login.getPassword());
				//register the user
				String encryptedPassword = MD5.crypt(password);
				ServerRequest request = new ServerRequest(username, encryptedPassword, true);
				RegisterOrAuthenciateUser(request);	
			}
		});
		
		
		
	}
	
	@Override
	public void run() {
		//constantly listen for reponses from server
		try{
			while(!Thread.currentThread().isInterrupted()){
				//System.out.println("This Client is listening for reponses from server....");
				Object object = objectInputStream.readObject();
				if(object instanceof ServerResponse){
					//user login/authentication related responses
					ServerResponse response =(ServerResponse)object;
					if(response.isLogin()){
						if(response.isSuccess()){
							//successful reponse of login authentication from server
							System.out.println("Log-in Success. Process to editor");
							proceedToMainEditor(false, response.getUsername());
							
						}else{
							//unscuccessful reposne of login authentication from server
							JOptionPane.showMessageDialog(mainPanel,
									"USERNAME OR PASSWORD IS INVALID."
									+ response.getMessage().toUpperCase(),
									"Log-in Failed",
									JOptionPane.ERROR_MESSAGE);
						}
					}else if(!response.isLogin()){
						if(response.isSuccess()){
							//succesful reponse of registration from server
							System.out.println("Sign-up Success. Process to editor");
							proceedToMainEditor(false, response.getUsername());
							
						}else{
							//unsuccessful response of registration from server
							JOptionPane.showMessageDialog(mainPanel,
									"USERNAME OR PASSWORD IS INVALID."
									+ response.getMessage().toUpperCase(),
									"Signup Failed",
									JOptionPane.ERROR_MESSAGE);
							
						}
					}
				}else if(object instanceof FileRequestResponse){
					//file saving related reponses
					FileRequestResponse fileRequestResponse = (FileRequestResponse)object;
					if(!fileRequestResponse.isRequest()){
						if(fileRequestResponse.isFileNameReponse()){
							String[] availableFiles = fileRequestResponse.getAvailableFiles();
							if(fileRequestResponse.isFileSaveDialog()){
								//pop up the custom filechooser
								OnlineFilechooser filechooser = new OnlineFilechooser(availableFiles, "FileChooser", editorMain);
							}else{
								OnlineFileOpener fileOpener = new OnlineFileOpener(availableFiles, "FileChooser", editorMain);
							}
							
						}else if(fileRequestResponse.isFileSavingResultReponse()){
							//the response contains result of saving files
							if(fileRequestResponse.isSuccessful()){
								if(editorMain.isOwnerOfCurrentTab()){
									JOptionPane.showMessageDialog(editorMain,
											"FILE SUCCESSFULLY SAVED",
											"Message",
											JOptionPane.INFORMATION_MESSAGE);
									editorMain.setFileName(fileRequestResponse.getFileName());
								}else{
									JOptionPane.showMessageDialog(editorMain,
											"The copy has been saved to your account. To open the copy, go to OPEN-ONLINE-MYFILES. The file in current tab does not belong to you",
											"Message",
											JOptionPane.INFORMATION_MESSAGE);
								}
	
							}else{
								JOptionPane.showMessageDialog(editorMain,
										"FILE FAIL TO SAVE",
										"Message",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}else{
							//the reponse contains actually content that user try to download
							editorMain.openNewFile(fileRequestResponse.getFileName(), fileRequestResponse.getFileContent(), fileRequestResponse.getUserName());
						}
					}
				}else if(object instanceof Assignment5RequestResponse){
					Assignment5RequestResponse response = (Assignment5RequestResponse)object;
					if(response.getType().equalsIgnoreCase("ADDCOLLABORATOR_RESULT")){
						JOptionPane.showMessageDialog(editorMain,
								response.getAddCollaborator_result_msg(),
								"error",
								JOptionPane.ERROR_MESSAGE);
					}else if(response.getType().equalsIgnoreCase("GET_REMOVABLE_USERS_RESULT")){
						new removeCollaboratorDialog(editorMain, response.getCollaborators());
					}else if(response.getType().equalsIgnoreCase("DELETE_USER_RESULT")){
						if(response.getCollaboratorName().equalsIgnoreCase(editorMain.getUsername())){
							//this user was removed as a collaborator
							if(editorMain.getUsername().equalsIgnoreCase(response.getCollaboratorName())){
								JOptionPane.showMessageDialog(editorMain,
										"You have been removed by " + response.getOwnerName() + " from file: " + response.getFileName(),
										"removed",
										JOptionPane.INFORMATION_MESSAGE);
							}
							editorMain.removeTab(response.getOwnerName(), response.getFileName());
						}
						
					}else if(response.getType().equalsIgnoreCase("GET_COLLABORATING_FILES_RESULT")){
						//contains all the files that this user is collaborting on
						if(response.getCollaboratorName().equalsIgnoreCase(editorMain.getUsername())){
							new openFileChooserA5(editorMain, response.getOwner_File_Hashmap());
						}
					}else if(response.getType().equalsIgnoreCase("ASK_TO_UPDATE")){
						editorMain.sendCurrentStateToServer();
					}else if(response.getType().equalsIgnoreCase("MergeResult")){
						editorMain.updateAllTabs(response);
					}
				}
				
				
			}
		}catch(ClassNotFoundException e){
			System.out.println("ClassNotFoundException "+ e.getMessage());
		} catch (IOException e) {
//			// TODO Auto-generated catch block
			System.out.println("IOException "+ e.getMessage());
			System.out.println("Cannot reach server");
			JOptionPane.showMessageDialog(mainPanel,
					"SERVER CANNOT BE REACHED.PROGRAM IN OFFLINE MODE",
					"Signup Failed",
					JOptionPane.WARNING_MESSAGE);
			proceedToMainEditor(true, "");
		}finally {
			try {
				socket.close();
				System.out.println("Closed the socket");
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

	
	
	//helper method to help set UI for each element
	private static void setUIFont (javax.swing.plaf.FontUIResource f){
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	    }
	} 
	
	private boolean checkValid(String password){
		boolean oneDigit = false;
		boolean oneLetter = false;
		
		for(int i = 0; i < password.length(); i++){
			if(Character.isDigit(password.charAt(i))){
				oneDigit = true;
				break;
			}
		}
		
		for(int i = 0; i < password.length(); i++){
			if(Character.isUpperCase(password.charAt(i))){
				oneLetter = true;
				break;
			}
		}
		
		if(oneDigit && oneLetter){
			return true;
		}else{
			return false;
		}
	}
	
	private void parseConfigurationFile(String filePath){
		FileReader freader = null;
		BufferedReader bReader = null;
		Scanner PortnumberScanner = null;
		String nextLine;
		try {
			freader = new FileReader(filePath);
			bReader = new BufferedReader(freader);
			while((nextLine = bReader.readLine()) != null){
				if(nextLine.startsWith("portnumber:")){
					int index = nextLine.indexOf(':');
					PortnumberScanner = new Scanner(nextLine.substring(index + 1));
					StringBuilder sBuilder = new StringBuilder();
					while(PortnumberScanner.hasNextInt()){
						sBuilder.append(PortnumberScanner.nextInt());
					}
					this.portnumber = Integer.parseInt(sBuilder.toString());
					System.out.println("Portnumber"+ portnumber);
					continue;
				}
				if(nextLine.startsWith("hostname")){
					int index = nextLine.indexOf(':');
					StringBuilder builder = new StringBuilder();
					for(int i = index + 1; i < nextLine.length(); i++){
						if(nextLine.charAt(i) != ' '){
							builder.append(nextLine.charAt(i));
						}
					}
					this.hostname = builder.toString();
					System.out.println("Hostname"+ hostname);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (freader != null) {freader.close();}
				if (bReader != null) {bReader.close();}
				if(PortnumberScanner!= null){PortnumberScanner.close();}
			} catch (IOException ioe) {
				System.out.println("IOException closing file: " + ioe.getMessage());
			}	
		}
		
	}
	
	private void createSocket(){
		
		try{
			socket = new Socket(hostname, portnumber);
			System.out.println(hostname);
			System.out.println(portnumber);
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println("1");
			obOutputStream = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("2");
			Thread thread = new Thread(this);
			thread.start();
		}catch (IOException e) {
		}
	}
	
	private void RegisterOrAuthenciateUser(ServerRequest request){
		try {
			obOutputStream.writeObject(request);
			obOutputStream.flush(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException" + e.getMessage());
		}
	}
	
	public void SendFileRequest(Object request){
		try {
			obOutputStream.writeObject(request);
			obOutputStream.flush(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException" + e.getMessage());
		}
	}
	
	private void proceedToMainEditor(boolean offline, String userName){
		if(!editorMain.isHasBeenAuthenticated()){
			LoginGUI.this.setVisible(false);
			editorMain.setVisible(true);
			editorMain.setOffline(offline);
			editorMain.setUsername(userName);
			editorMain.setHasBeenAuthenticated(true);
		}
	}


}
