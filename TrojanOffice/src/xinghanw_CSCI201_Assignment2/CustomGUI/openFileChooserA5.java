package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.xml.soap.Text;

import xinghanw_CSCI201_Assignment2.TextEditorMain;
import xinghanw_CSCI201_Assignment2.Server.FileRequestResponse;

public class openFileChooserA5 extends JDialog{
	public static final long serialVersionUID = 1212312211;
	private int windowWidth = 250;
	private int windowHeight = 400;
	private JList<String> list;
	private String[] options;
	private TextEditorMain editorMain;
	private JButton myFile,selectUser;
	private HashMap<String, ArrayList<String>> owner_File_Hash;
	
	
	
	public openFileChooserA5(TextEditorMain editorMain, HashMap<String, ArrayList<String>> owner_File_Hash) {
		super();
		this.editorMain = editorMain;
		this.owner_File_Hash = owner_File_Hash;
		instantiateComponents();
		createGUI();
		addActions();
		setLocation(500, 500);
		setSize(windowWidth, windowHeight);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void instantiateComponents(){
		Set<String> allOwners = owner_File_Hash.keySet();
		for(String owner: allOwners){
			System.out.println(owner);
		}
		options = new String[allOwners.size()];
		options = allOwners.toArray(options);
		myFile = new JButton("MyFile");
		selectUser = new JButton("Select User");
		list = new JList<String>(options); 
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
	}
	
	private void createGUI(){
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 200));
		this.setLayout(new BorderLayout());
		JLabel label = new JLabel("Choose a User");
		this.add(label, BorderLayout.NORTH);
		this.add(listScroller, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(myFile);
		buttonPanel.add(selectUser);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void addActions(){
		myFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileRequestResponse request = new FileRequestResponse(true);
				request.setUserName(editorMain.getUsername());
				request.setFileNameRequest(true);
				request.setFileSaveDialog(false);
				editorMain.getLoginGUI().SendFileRequest(request);
				openFileChooserA5.this.dispose();
			}
		});
		
		selectUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String user = list.getSelectedValue();
				ArrayList<String> filesArrayList = owner_File_Hash.get(user);
				String[] filesOfThisOwner = new String[filesArrayList.size()];
				filesOfThisOwner = filesArrayList.toArray(filesOfThisOwner);
				OnlineFileOpener fileOpener = new OnlineFileOpener(filesOfThisOwner, "Choose a File", editorMain);
				fileOpener.setOwnerName(user);
				openFileChooserA5.this.dispose();
			}
		});
	}
}
