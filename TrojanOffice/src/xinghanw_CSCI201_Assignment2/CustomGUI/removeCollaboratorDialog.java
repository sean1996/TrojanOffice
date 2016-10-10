package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import xinghanw_CSCI201_Assignment2.TextEditorMain;
import xinghanw_CSCI201_Assignment2.Server.Assignment5RequestResponse;

public class removeCollaboratorDialog extends JDialog{
	public static final long serialVersionUID = 12123121;
	private int windowWidth = 200;
	private int windowHeight = 400;
	private JList<String> list;
	private String[] options;
	private TextEditorMain editorMain;
	private JButton removeUser;
	
	public removeCollaboratorDialog(TextEditorMain editorMain, String[] options) {
		super();
		this.editorMain = editorMain;
		this.options = options;
		instantiateComponents();
		createGUI();
		addActions();
		setLocation(500, 500);
		setSize(windowWidth, windowHeight);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void instantiateComponents(){
		removeUser = new JButton("Remove this user!");
		list = new JList<String>(options); 
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setSelectedIndex(list.getVisibleRowCount() - 1);
        
		
	}
	
	private void createGUI(){
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(400, 300));
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.add(listScroller);
        this.add(removeUser);
	}
	
	private void addActions(){
		removeUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Assignment5RequestResponse request = new Assignment5RequestResponse();
				request.setRequest(true);
				request.setOwnerName(editorMain.getUsername());
				request.setFileName(editorMain.getCurrentFileName());
				request.setCollaboratorName(list.getSelectedValue());
				request.setType("DELETE_USER");
				editorMain.getLoginGUI().SendFileRequest(request);
			}
		});
	}
	
	
	
	
	
	
	
}
