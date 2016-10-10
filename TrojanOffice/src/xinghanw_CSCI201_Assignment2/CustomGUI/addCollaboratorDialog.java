package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import xinghanw_CSCI201_Assignment2.TextEditorMain;
import xinghanw_CSCI201_Assignment2.Server.Assignment5RequestResponse;

public class addCollaboratorDialog extends JDialog{
	public static final long serialVersionUID = 1212312;
	private int windowWidth = 300;
	private int windowHeight = 90;
	private JTextField userNameField;
	private JLabel userNameLabel;
	private JButton addUser, cancel;
	private TextEditorMain editorMain;
	
	public addCollaboratorDialog(TextEditorMain editorMain) {
		super();
		this.editorMain = editorMain;
		instantiateComponents();
		createGUI();
		addActions();
		setLocation(500, 500);
		setSize(windowWidth, windowHeight);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void instantiateComponents(){
		userNameField = new JTextField();
		
		userNameLabel = new JLabel("ADD USER:");
		addUser = new JButton("OK");
		cancel = new JButton("CANCEL");
	}
	
	private void createGUI(){
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		JPanel labelPanel = new JPanel();
		userNameLabel.setAlignmentX(LEFT_ALIGNMENT);
		labelPanel.add(userNameLabel);
		this.add(labelPanel);
		this.add(userNameField);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addUser);
		buttonPanel.add(cancel);
		this.add(buttonPanel);
	}
	
	private void addActions(){
		addUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//create a request to server :add a collaborator to this file
				String collaboratorName = userNameField.getText();
				Assignment5RequestResponse request = new Assignment5RequestResponse();
				request.setRequest(true);
				request.setType("ADDCOLLABORATOR");
				request.setCollaboratorName(collaboratorName);
				request.setOwnerName(editorMain.getUsername());
				request.setFileName(editorMain.getCurrentFileName());
				editorMain.getLoginGUI().SendFileRequest(request);
				addCollaboratorDialog.this.dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				addCollaboratorDialog.this.dispose();
			}
		});
		
	}
}
