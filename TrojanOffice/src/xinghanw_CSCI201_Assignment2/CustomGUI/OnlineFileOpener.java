package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import xinghanw_CSCI201_Assignment2.TextEditorMain;
import xinghanw_CSCI201_Assignment2.Server.FileRequestResponse;

public class OnlineFileOpener extends JDialog {
	public static final long serialVersionUID = 123344;
	private JList<String> list;
	private String[] options;
	private TextEditorMain editorMain;
	private JButton cancelButton, openButton;
	private JTextField fileNameTextField;
	private JPanel mainPanel;
	private int windowWidth = 500;
	private int windowHeight = 300;
	private String ownerName;
	
	public OnlineFileOpener (String[] options, String title, TextEditorMain editor){
		super(editor, title);
		editorMain = editor;
		this.options = options;
		instantiateComponents();
		createGUI();
		addActions();
		setLocation(500, 300);
		setSize(windowWidth, windowHeight);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void instantiateComponents(){
		list = new JList<String>(options); 
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
		fileNameTextField = new JTextField();
		ownerName = editorMain.getUsername();
		cancelButton = new JButton("Cancel");
		openButton = new JButton("Open");
		mainPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(boxLayout);
		mainPanel.setSize(new Dimension(windowWidth,windowHeight));
		
	}
	
	private void createGUI(){
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(400, 300));
        //listScroller.setAlignmentX(LEFT_ALIGNMENT);
        JLabel selectFile = new JLabel("Select a file:");
        selectFile.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(selectFile);
        mainPanel.add(listScroller);
        JPanel filenamePanel = new JPanel();
        JLabel filenameLabel = new JLabel("File");
        fileNameTextField.setPreferredSize(new Dimension(400, 20));
        filenamePanel.add(filenameLabel);
        filenamePanel.add(fileNameTextField);
        filenamePanel.setPreferredSize(new Dimension(windowWidth, 30));
        mainPanel.add(filenamePanel);
        JPanel buttonPanel =new JPanel();
        BoxLayout boxLayout2 = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
        buttonPanel.add(Box.createGlue());
        buttonPanel.add(openButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setPreferredSize(new Dimension(windowWidth, 30));
        mainPanel.add(buttonPanel);
        this.add(mainPanel);
        
	}
	
	private void addActions(){
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				OnlineFileOpener.this.dispose();
			}
		});
		
		openButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String filename = list.getSelectedValue();
				FileRequestResponse  request = new FileRequestResponse(true);
				request.setFileNameRequest(false);
				request.setSaveFileRequest(false);
				request.setFileName(filename);
				request.setUserName(ownerName);
				editorMain.getLoginGUI().SendFileRequest(request);
				OnlineFileOpener.this.dispose();
			}
		});
	}
	
	public void setOwnerName(String ownerName){
		this.ownerName = ownerName;
	}
	
}

