package xinghanw_CSCI201_Assignment2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Desktop.Action;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Dimension2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.TabableView;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.xml.bind.JAXBPermission;

import org.omg.CORBA.StringHolder;

import xinghanw_CSCI201_Assignment1.WordSuggestion;
import xinghanw_CSCI201_Assignment2.CustomGUI.MyButton;
import xinghanw_CSCI201_Assignment2.CustomGUI.MyTabbedPane;
import xinghanw_CSCI201_Assignment2.CustomGUI.OnlineFilechooser;
import xinghanw_CSCI201_Assignment2.CustomGUI.addCollaboratorDialog;
import xinghanw_CSCI201_Assignment2.CustomGUI.customComboBoxUI;
import xinghanw_CSCI201_Assignment2.CustomGUI.customScrollBarUI;
import xinghanw_CSCI201_Assignment2.CustomGUI.customTabbedPaneUI;
import xinghanw_CSCI201_Assignment2.CustomGUI.openFileChooserA5;
import xinghanw_CSCI201_Assignment2.CustomGUI.removeCollaboratorDialog;
import xinghanw_CSCI201_Assignment2.Server.Assignment5RequestResponse;
import xinghanw_CSCI201_Assignment2.Server.FileRequestResponse;
import xinghanw_CSCI201_Assignment2.ThridPartyAPI.diff_match_patch;
import xinghanw_CSCI201_Assignment2.ThridPartyAPI.diff_match_patch.Patch;

public class TextEditorMain extends JFrame {
	private boolean offline;
	private String username = "";
	private LoginGUI loginGUI;
	private boolean hasBeenAuthenticated = false;
	

	//constant declaration
	public static final long serialVersionUID = 1;
	private static final int windowLength = 800;
	private static final int windowWidth = 600;
	//GUI componets for menubars
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuEdit;
	private JMenu menuSpellCheck;
	private JMenu menuUsers;
	private JMenuItem menuFile_New;
	private JMenuItem menuFile_Open;
	private JMenuItem menuFile_Save;
	private JMenuItem menuFile_Close;
	private JMenuItem menuEdit_Undo;
	private JMenuItem menuEdit_Redo;
	private JMenuItem menuEdit_Cut;
	private JMenuItem menuEdit_Copy;
	private JMenuItem menuEdit_Paste;
	private JMenuItem menuEdit_SelectAll;
	private JMenuItem menuSpellCheck_Run;
	private JMenuItem menuSpellChcek_Configure;
	private JMenuItem menuUsers_add, menuUsers_remove;
	//tabbedPane for switching files
	private MyTabbedPane switchFileTabbedPane;
	private boolean fileOpen = false;
	private Vector<JPanel> jPanelList = new Vector<JPanel>(0);
	private Vector<JTextArea> fileList = new Vector<JTextArea>(0);				
//	private ArrayList<UndoManager> undoManagerList = new ArrayList<UndoManager>(0);
	private ArrayList<String> wordlistList = new ArrayList<String>(0);
	private ArrayList<String> keyboardConfigList = new ArrayList<String>(0);
	private ArrayList<Boolean> configurationPanelOpened = new ArrayList<Boolean>(0);
	private ArrayList<Boolean> spellcheckProgramOpened = new ArrayList<Boolean>(0);
	private Hashtable<JPanel, JPanel>configurationPanelDictionary = new Hashtable<JPanel, JPanel>(0);
	private Hashtable<JPanel, JPanel>spellcheckPanelDicionary = new Hashtable<JPanel, JPanel>(0);
	private Hashtable<JPanel, Hashtable<String, Vector<String> > >   spellcheckProgramMap = new Hashtable<JPanel, Hashtable<String, Vector<String> > > (0);
	private Hashtable<JPanel, Vector<String> > wrongWordsMap =  new Hashtable<JPanel, Vector<String> >(0);
	private Hashtable<JPanel, Integer>currentIndexMap = new Hashtable<JPanel, Integer>(0);
	
	private HashMap<JPanel, String> file_OwnerHashmap = new HashMap<JPanel, String>(0);
	private static  Font customFont;
	private static Font customFontBigger;
	public TextEditorMain(){
		super("Trojan Office");
		instantiateComponets();
		createGUI();
		addActions();
	}
	
	
	public static void main(String args[]){
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch (Exception e){
			System.out.println("Warning! Cross-platform L&F not used!");
		}
		
		//change global fonts
		try {
          customFont = Font.createFont(Font.TRUETYPE_FONT, new File("kenvector_future.ttf")).deriveFont(12f);
          customFontBigger = Font.createFont(Font.TRUETYPE_FONT, new File("kenvector_future.ttf")).deriveFont(13f);
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

		TextEditorMain textEditor = new TextEditorMain();
		 LoginGUI GUI = new LoginGUI(textEditor);
		 textEditor.setLoginGUI(GUI);
		textEditor.setVisible(false);
		String original = "The dog jumped over the fox.";
		String edit1 = "The brown dog jumped over the fox.";
		String edit2 = "The dog jumped over the lazy fox.";
		String edit3 = "The dog is over the fox.";


	}
	
	
	private void instantiateComponets(){

		
		//instantiate menu bar and items
		menuBar = new JMenuBar(){
			@Override
			public void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				Dimension size = this.getSize();
				g.drawImage(Toolkit.getDefaultToolkit().getImage("resource/img/backgrounds/red_panel.png"),0,0, size.width, size.height,this);
			}
		};
		
		menuFile = new JMenu("File");
		menuEdit = new JMenu("Edit");
		menuSpellCheck = new JMenu("SpellCheck");
		menuUsers = new JMenu("Users");
		menuUsers_add = new JMenuItem("ADD");
		menuUsers_remove = new JMenuItem("Remove");
		menuFile_New = new JMenuItem("New");
		menuFile_Open = new JMenuItem("Open");
		menuFile_Save = new JMenuItem( "Save");
		menuFile_Close = new JMenuItem("Close");
//		menuEdit_Undo = new JMenuItem("Undo");
//		menuEdit_Redo = new JMenuItem("Redo");
		menuEdit_Cut = new JMenuItem("Cut");
		menuEdit_Copy = new JMenuItem("Copy");
		menuEdit_Paste = new JMenuItem("Paste");
		menuEdit_SelectAll = new JMenuItem("Select All");
		menuSpellCheck_Run = new JMenuItem("Run");
		menuSpellChcek_Configure = new JMenuItem("Configure");
		//instantiate tabbed pane
		switchFileTabbedPane = new MyTabbedPane();

	}
	
	private void createGUI(){
					

		//create GUI for menu parts
		menuFile_New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuFile.add(menuFile_New);
		menuFile_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuFile.add(menuFile_Open);
		menuFile_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuFile.add(menuFile_Save);
		menuFile_Close.setEnabled(false);
		menuFile.add(menuFile_Close);
		menuBar.add(menuFile);
//		menuEdit_Undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
//		menuEdit_Undo.setEnabled(false);
//		menuEdit.add(menuEdit_Undo);
//		menuEdit_Redo.setEnabled(false);
//		menuEdit_Redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
//		menuEdit.add(menuEdit_Redo);
		menuEdit.addSeparator();
		menuEdit_Cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuEdit.add(menuEdit_Cut);
		menuEdit_Copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuEdit.add(menuEdit_Copy);
		menuEdit_Paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menuEdit.add(menuEdit_Paste);
		menuEdit.addSeparator();
		menuEdit_SelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menuEdit.add(menuEdit_SelectAll);
		menuEdit.setVisible(false);
		menuBar.add(menuEdit);
		menuSpellCheck_Run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,0));
		menuSpellCheck.add(menuSpellCheck_Run);
		menuSpellCheck.add(menuSpellChcek_Configure);
		menuSpellCheck.setVisible(false);
		menuBar.add(menuSpellCheck);
		menuUsers.add(menuUsers_add);
		menuUsers.add(menuUsers_remove);
		menuUsers.setVisible(false);
		menuBar.add(menuUsers);
		
		//set Mnemonic
		menuFile.setMnemonic('F');
		menuEdit.setMnemonic('E');
		menuSpellCheck.setMnemonic('S');
		menuFile_New.setMnemonic('N');
		menuFile_Open.setMnemonic('O');
		menuFile_Save.setMnemonic('S');
		menuFile_Close.setMnemonic('C');
//		menuEdit_Undo.setMnemonic('U');
//		menuEdit_Redo.setMnemonic('R');
		menuEdit_Cut.setMnemonic('C');
		menuEdit_Copy.setMnemonic('C');
		menuEdit_Paste.setMnemonic('P');
		menuEdit_SelectAll.setMnemonic('S');
		menuSpellCheck_Run.setMnemonic('R');
		menuSpellChcek_Configure.setMnemonic('C');
		setSize(windowLength, windowWidth);
		setLocation(500, 300);
		this.setLayout(new BorderLayout());
		this.add(menuBar, BorderLayout.NORTH);	
		switchFileTabbedPane.setUI(new customTabbedPaneUI());
		switchFileTabbedPane.setForeground(Color.white);
		switchFileTabbedPane.setBackground(new Color(248, 106, 43));
		this.add(switchFileTabbedPane, BorderLayout.CENTER);

		//Set menu icons
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("resource/img/icon/office.png");
		this.setIconImage(image);
		
		//set cursor icon
		image = toolkit.getImage("resource/img/icon/cursor.png");
		Cursor c = toolkit.createCustomCursor(image , new Point(0, 
		           0), "img");
		this.setCursor (c);
		
		//set menubaritem icon
		menuFile_New.setIcon(new ImageIcon("resource/img/menuitems/new.png"));
		menuFile_Open.setIcon(new ImageIcon("resource/img/menuitems/open.png"));
		menuFile_Save.setIcon(new ImageIcon("resource/img/menuitems/save.png"));
		menuFile_Close.setIcon(new ImageIcon("resource/img/menuitems/close.png"));
		menuEdit_Copy.setIcon(new ImageIcon("resource/img/menuitems/copy.png"));
		menuEdit_Paste.setIcon(new ImageIcon("resource/img/menuitems/paste.png"));
		menuEdit_Cut.setIcon(new ImageIcon("resource/img/menuitems/cut.png"));
		menuEdit_SelectAll.setIcon(new ImageIcon("resource/img/menuitems/select.png"));
//		menuEdit_Redo.setIcon(new ImageIcon("resource/img/menuitems/redo.png"));
//		menuEdit_Undo.setIcon(new ImageIcon("resource/img/menuitems/undo.png"));
		menuSpellChcek_Configure.setIcon(new ImageIcon("resource/img/menuitems/configure.png"));
		menuSpellCheck_Run.setIcon(new ImageIcon("resource/img/menuitems/run.png"));
		
		//change the text of tabs in Jtabbedpane to white
		UIManager.put("TabbedPane.selected", Color.white);
     
	}
	
	private void addActions(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Add a listener for changing focus on tabs in JTabbedPane
		//Change UndoManagerCorrespondingly
		switchFileTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(!fileList.isEmpty()){
					//focusManagersOnThisFile(switchFileTabbedPane.getSelectedIndex()); 
				    toggleUserMenu();
				    System.out.println("List:");
//				    for(int i = 0;  i < jPanelList.size(); i++){
//				    	System.out.println(switchFileTabbedPane.getTitleAt(i) + "  " + file_OwnerHashmap.get(jPanelList.get(i)));
//				    }
//				    sendCurrentStateToServer();
				}
			}
		});
		
		
		//------------------------------------actions for menu_File-----------------------------------------------
		//when menuFile_New is pressed, create a new file(TextField)
		menuFile_New.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel newFile = new JPanel();
				jPanelList.add(newFile);
				file_OwnerHashmap.put(newFile, username);
				newFile.setLayout(new BorderLayout());
				JTextArea textArea = new JTextArea();
				textArea.setSelectionColor(new Color(248, 106, 43));
//				UndoManager undoManager = new UndoManager();
//				undoManagerList.add(undoManager);
				JScrollPane textScrollPane = new JScrollPane(textArea);
				textScrollPane.getVerticalScrollBar().setUI(new customScrollBarUI());
				newFile.add(textScrollPane, BorderLayout.CENTER);
				switchFileTabbedPane.add("new", newFile);
				fileOpen = true;
				toggleMenuBar(fileOpen);
				fileList.add(textArea);
				switchFileTabbedPane.setSelectedIndex(fileList.size()- 1);
				//add UndoManager
//				textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
//					@Override
//					public void undoableEditHappened(UndoableEditEvent e) {
//						undoManager.addEdit(e.getEdit());
//						updateUndoRedoMenuItems(undoManager);
//					}
//				});
				//Add default keyboard and wordlist filepath for this newly created file
				String defaultKeyboardConfig = new String("qwerty-us.kb");
				String defaultWordlistpath = new String("wordlist.wl");
				keyboardConfigList.add(defaultKeyboardConfig);
				wordlistList.add(defaultWordlistpath);
				configurationPanelOpened.add(false);
				spellcheckProgramOpened.add(false);
				toggleUserMenu();
			}
		});
		
		//when menuFile_Open is pressed, create a JFileChooser instance
		menuFile_Open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(offline){
					offline_OpenFile();
				}else{
					//pop up an dialog to provide user options to save offline or online
					String[] options = {"Online", "Offline"};
					int selection = JOptionPane.showOptionDialog(TextEditorMain.this,
							"Where would you like to open the file",
							"Open..", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if(selection == 0){
						online_OpenFile();
					}else if(selection == 1){
						offline_OpenFile();
					}
				}	

			}
		});
		
		
		//when saveButton is pressed, save the file
		menuFile_Save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fileOpen){
					if(offline){
						offline_SaveFile();
					}else{
						//pop up an dialog to provide user options to save offline or online
						String[] options = {"Online", "Offline"};
						int selection = JOptionPane.showOptionDialog(TextEditorMain.this,
								"Where would you like to save the file",
								"Save..", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						if(selection == 0){
							online_saveFile();
						}else if(selection == 1){
							offline_SaveFile();
						}
					}	
				}

			}
		});
		
		menuFile_Close.addActionListener(new ActionListener() {
			//when tab menuitem is pressed, close current file
			@Override
			public void actionPerformed(ActionEvent e) {
				//remove this file from file list, and all related data from arrays.
				
				synchronized (jPanelList) {
					synchronized (fileList) {
						synchronized (file_OwnerHashmap) {
							fileList.remove(switchFileTabbedPane.getSelectedIndex());
							jPanelList.remove(switchFileTabbedPane.getSelectedIndex());
							file_OwnerHashmap.remove(switchFileTabbedPane.getSelectedComponent());
						}
					}
				}

				
//				undoManagerList.remove(switchFileTabbedPane.getSelectedIndex());
				keyboardConfigList.remove(switchFileTabbedPane.getSelectedIndex());
				wordlistList.remove(switchFileTabbedPane.getSelectedIndex());

				
				configurationPanelOpened.remove(switchFileTabbedPane.getSelectedIndex());
				spellcheckProgramOpened.remove(switchFileTabbedPane.getSelectedIndex());
				switchFileTabbedPane.remove(switchFileTabbedPane.getSelectedIndex());

				//if no file is opened, edit menu and spellcheck menu should be disabled.
				if(fileList.isEmpty()){
					fileOpen = false;
					toggleMenuBar(fileOpen);
				}
			}
		});
		
	
	
//----------------------------Till here all the actions for file pane is added----------------------------------------
//		menuEdit_Undo.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				UndoManager currentManager = undoManagerList.get(switchFileTabbedPane.getSelectedIndex());
//				currentManager.undo();
//				updateUndoRedoMenuItems(currentManager);
//			}
//		});
//		
//		menuEdit_Redo.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				UndoManager currentManager = undoManagerList.get(switchFileTabbedPane.getSelectedIndex());
//				currentManager.redo();
//				updateUndoRedoMenuItems(currentManager);
//			}
//		});
		
		menuEdit_Copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea currentEditingTextArea = fileList.get(switchFileTabbedPane.getSelectedIndex());
				currentEditingTextArea.copy();
			}
		});
		
		menuEdit_Paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea currentEditingTextArea = fileList.get(switchFileTabbedPane.getSelectedIndex());
				currentEditingTextArea.paste();
			}
		});
		
		menuEdit_Cut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea currentEditingTextArea = fileList.get(switchFileTabbedPane.getSelectedIndex());
				currentEditingTextArea.cut();
			}
		});
		menuEdit_SelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea currentTextArea = fileList.get(switchFileTabbedPane.getSelectedIndex());
				currentTextArea.requestFocusInWindow();
				currentTextArea.selectAll();
			}
		});
	//----------------------------Till here all the actions for edit pane is added----------------------------------------
		menuSpellCheck_Run.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		menuSpellChcek_Configure.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
			//Create GUI for configuration panel
				if(configurationPanelOpened.get(switchFileTabbedPane.getSelectedIndex())){
					//if a panel already exists for this file, return.
					JOptionPane.showMessageDialog(switchFileTabbedPane, "Configuration is running!"); 
					return;
				}
				menuSpellCheck_Run.setEnabled(false);
				//get current selected panel
				JPanel currentPanel = jPanelList.get(switchFileTabbedPane.getSelectedIndex());
				configurationPanelOpened.set(switchFileTabbedPane.getSelectedIndex(), true);			
				JPanel configurationPanel = new JPanel(new GridBagLayout());
				configurationPanel.setBackground(new Color(118, 118, 118));
				configurationPanelDictionary.put(currentPanel, configurationPanel);
				GridBagConstraints constraints = new GridBagConstraints();
				JLabel currentWL = new JLabel(wordlistList.get(switchFileTabbedPane.getSelectedIndex()));
				constraints.fill = GridBagConstraints.HORIZONTAL;
				constraints.anchor = GridBagConstraints.LINE_START;
				constraints.gridx = 0;
				constraints.gridy = 0;
				constraints.weightx = 1;
				configurationPanel.add(currentWL, constraints);
				MyButton changeWordlistButton = new MyButton("Select Wordlist..");
				constraints.gridx = 0;
				constraints.gridy = 1;
				constraints.gridwidth = 2;
				configurationPanel.add(changeWordlistButton, constraints);
				JLabel currentKB = new JLabel(keyboardConfigList.get(switchFileTabbedPane.getSelectedIndex()));
				constraints.gridx = 0;
				constraints.gridy = 3;
				constraints.gridwidth = 1;
				constraints.ipadx = 0;
				configurationPanel.add(currentKB, constraints);
				MyButton changeKeyboardFileButton = new MyButton("Select Keyboard..");
				constraints.gridx = 0;
				constraints.gridy = 4;
				constraints.gridwidth = 2;
				configurationPanel.add(changeKeyboardFileButton, constraints);
				MyButton closeButton = new MyButton("Close");
				constraints.ipadx = 0;
				constraints.gridwidth = 1;
				constraints.gridheight = 1;
				constraints.insets = new Insets(400, 0, 0, 0);
				constraints.gridx = 0;
				constraints.gridy = 6;
				configurationPanel.add(closeButton,constraints);
				configurationPanel.setBorder(new TitledBorder("Configure"));
				int height = fileList.get(switchFileTabbedPane.getSelectedIndex()).getHeight();
				int width = fileList.get(switchFileTabbedPane.getSelectedIndex()).getWidth();
				width = width/4;
				configurationPanel.setPreferredSize(new Dimension(width, height));
				currentPanel.add(configurationPanel, BorderLayout.EAST);
				//Add Actions for Configuration panel;
				changeWordlistButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
					//create a new filechooser for users to choose keyboard file
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Choose Wordlist File...");
						FileNameExtensionFilter filter = new FileNameExtensionFilter(
						        "Wordlist files (*.wl)", "wl");
						fileChooser.setFileFilter(filter);
						int returnVal = fileChooser.showOpenDialog(configurationPanel);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	wordlistList.set(switchFileTabbedPane.getSelectedIndex(), fileChooser.getSelectedFile().getAbsolutePath());
					    	File file = new File(wordlistList.get(switchFileTabbedPane.getSelectedIndex()));
					    	currentWL.setText(file.getName());
					    }	
					    	
					}
				});
				changeKeyboardFileButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Choose Keyboard Configuration File...");
						FileNameExtensionFilter filter = new FileNameExtensionFilter(
						        "Wordlist files (*.kb)", "kb");
						fileChooser.setFileFilter(filter);
						int returnVal = fileChooser.showOpenDialog(configurationPanel);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	keyboardConfigList.set(switchFileTabbedPane.getSelectedIndex(), fileChooser.getSelectedFile().getAbsolutePath());
					    	File file = new File(keyboardConfigList.get(switchFileTabbedPane.getSelectedIndex()));
					    	currentKB.setText(file.getName());
					    }
						
					}
				});
				closeButton.addActionListener(new ActionListener() {
					//when close button is pressed, remove the configuration panel;
					@Override
					public void actionPerformed(ActionEvent e) {
						menuSpellCheck_Run.setEnabled(true);
						configurationPanelOpened.set(switchFileTabbedPane.getSelectedIndex(), false);
						configurationPanelDictionary.remove(currentPanel);
						configurationPanel.removeAll();
						currentPanel.remove(configurationPanel);
						
					}
				});
			}
		}); 
		menuSpellCheck_Run.addActionListener(new ActionListener() {
			


			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel currentPanel = jPanelList.get(switchFileTabbedPane.getSelectedIndex());
				menuSpellChcek_Configure.setEnabled(false);
				if(spellcheckProgramOpened.get(switchFileTabbedPane.getSelectedIndex())){
					JOptionPane.showMessageDialog(switchFileTabbedPane, "Spellcheck is running!"); 
					return;
				}
				else{ 
					if(configurationPanelOpened.get(switchFileTabbedPane.getSelectedIndex())){
						//If configuration panel is opened, close configuration panel and replace it with spellcheck program panel
						//currentPanel.remove(configurationPanelDictionary.get(currentPanel));
						currentPanel.removeAll();
						currentPanel.invalidate();
						currentPanel.repaint();
						currentPanel.add(fileList.get(switchFileTabbedPane.getSelectedIndex()), BorderLayout.CENTER);
						currentPanel.invalidate();
						currentPanel.repaint();
					}
					//some variables used for spellcheck program
				    Hashtable<String, Integer> occuranceMap = new Hashtable<String, Integer>(0); //keep track of the occurrance of repeated words, 0 represents the first occurance
					int selectedIndex = switchFileTabbedPane.getSelectedIndex();
					String text = fileList.get(selectedIndex).getText();
					File wordlistFile =new File(wordlistList.get(selectedIndex)) ;
					File configurationFile =new File (keyboardConfigList.get(selectedIndex));
					if(!wordlistFile.exists() || !configurationFile.exists()){
						JOptionPane.showMessageDialog(switchFileTabbedPane, "Missing Configuration File or WordList File, Stop"); 
						return;
					}
					
					WordSuggestion wordSuggestionprogram = new WordSuggestion(wordlistList.get(selectedIndex), keyboardConfigList.get(selectedIndex), text);
					currentIndexMap.put(currentPanel, 0);
					wrongWordsMap.put(currentPanel, wordSuggestionprogram.getWrongWords());
					spellcheckProgramMap.put(currentPanel, wordSuggestionprogram.getSuggestions());
					if(wordSuggestionprogram.getWrongWords().isEmpty()){
						JOptionPane.showMessageDialog(switchFileTabbedPane, "No Error Found!");
						return;
					}
					spellcheckProgramOpened.set(selectedIndex, true);
					JPanel spellcheckProgramPanel = new JPanel(new GridBagLayout());
					spellcheckProgramPanel.setBackground(new Color(118, 118, 118));
					GridBagConstraints constraints = new GridBagConstraints();
					JPanel spellcheckProgramPanelMain = new JPanel(new GridBagLayout());
					spellcheckProgramPanelMain.setBackground(new Color(118, 118, 118));
					GridBagConstraints Mainconstraints = new GridBagConstraints();
					spellcheckProgramPanelMain.setBorder(new TitledBorder("Spell Check"));
					String firstWrongWord = wrongWordsMap.get(currentPanel).get(currentIndexMap.get(currentPanel));
					JLabel wrongWordDisplayLabel = new JLabel("Spelling: " + firstWrongWord);
					wrongWordDisplayLabel.setFont(customFontBigger);
					constraints.gridx = 0;
					constraints.gridy = 0;
					constraints.fill = GridBagConstraints.BOTH;
					constraints.gridwidth = 2;
					constraints.weightx = 0.5;
					constraints.weighty = 0.1;
					spellcheckProgramPanelMain.add(wrongWordDisplayLabel, constraints);
					MyButton ignoreButton = new MyButton("Ignore");
					constraints.gridwidth = 1;
					constraints.gridy = 1;
					spellcheckProgramPanelMain.add(ignoreButton, constraints);
					MyButton addButton = new MyButton("Add");
					constraints.gridx = 1;
					spellcheckProgramPanelMain.add(addButton, constraints);
					Vector<String> options = spellcheckProgramMap.get(currentPanel).get(wrongWordsMap.get(currentPanel).firstElement());
					
				    JComboBox choices = new JComboBox( options);
				    choices.setUI(new customComboBoxUI());
					constraints.gridx = 0;
					constraints.gridy = 2;
					spellcheckProgramPanelMain.add(choices, constraints);
					MyButton changeButton = new MyButton("Change");
					constraints.gridx = 1;
					spellcheckProgramPanelMain.add(changeButton, constraints);
					int height = fileList.get(switchFileTabbedPane.getSelectedIndex()).getHeight();
					int width = fileList.get(switchFileTabbedPane.getSelectedIndex()).getWidth();
					width = width/4;
					spellcheckProgramPanel.setSize(new Dimension(300, height));
					Mainconstraints.fill = GridBagConstraints.BOTH;
					Mainconstraints.gridx = 0;
					Mainconstraints.gridy = 0;
					Mainconstraints.weighty =0.5;
					Mainconstraints.weightx = 1;
					Mainconstraints.gridwidth = 2;
					spellcheckProgramPanel.add(spellcheckProgramPanelMain, Mainconstraints);
					Mainconstraints.gridx = 0;
					Mainconstraints.gridy = 5;
					Mainconstraints.anchor = GridBagConstraints.PAGE_END;
					Mainconstraints.gridheight = 1;
					Mainconstraints.insets = new Insets(400, 0, 0, 0);
					Mainconstraints.weighty = 1;
					MyButton closeButton = new MyButton("Close");
					spellcheckProgramPanel.add(closeButton, Mainconstraints);
					currentPanel.add(spellcheckProgramPanel, BorderLayout.EAST);
					//highlight the first word in text area
					Highlighter highlighter = fileList.get(selectedIndex).getHighlighter();
					String textString = fileList.get(selectedIndex).getText();
					occuranceMap.put(wrongWordsMap.get(currentPanel).get(currentIndexMap.get(currentPanel)), 0); 
					int startIndex = textString.indexOf(firstWrongWord);
					try {
						highlighter.addHighlight(startIndex, firstWrongWord.length() + startIndex, DefaultHighlighter.DefaultPainter);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					fileList.get(selectedIndex).setEditable(false);
					
					//add listeners
					ignoreButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							//ignore this word, go highlight next word（Updating iterator index）
							int currentIndex = currentIndexMap.get(currentPanel);
							String currentWord = wrongWordsMap.get(currentPanel).get(currentIndex);
							currentIndex += 1;
							highlighter.removeAllHighlights();
							if (currentIndex == wrongWordsMap.get(currentPanel).size()) {
								//All wrong words have been dealt at this point, remove the panel
								fileList.get(selectedIndex).setEditable(true);
								spellcheckProgramPanel.removeAll();
								currentPanel.remove(spellcheckProgramPanel);
								spellcheckProgramOpened.set(switchFileTabbedPane.getSelectedIndex(), false);
								currentIndexMap.remove(currentPanel);
								spellcheckProgramMap.remove(currentPanel);
								wrongWordsMap.remove(currentPanel);
								return;	
							}
							//update the display label
							currentIndexMap.replace(currentPanel, currentIndex);
							String nextWord = wrongWordsMap.get(currentPanel).get(currentIndex);
							wrongWordDisplayLabel.setText("Spelling "+ nextWord);
							//update Jcombobox's options
							if(!currentWord.equals(nextWord)){
								choices.removeAllItems();
								for(String suggestion: spellcheckProgramMap.get(currentPanel).get(nextWord)){
									choices.addItem(suggestion);
								}
							}
							//highlight next word
							Highlighter highlighter = fileList.get(selectedIndex).getHighlighter();
							String textString = fileList.get(selectedIndex).getText();
							if(occuranceMap.containsKey(nextWord)){
								int numberOfPreviousOccurance = occuranceMap.get(nextWord);
								numberOfPreviousOccurance += 1;
								occuranceMap.replace(nextWord, numberOfPreviousOccurance);
							}else{
								occuranceMap.put(nextWord, 0);
							}
							int startIndex = -1;
							for(int i=0; i<=occuranceMap.get(nextWord); i++){
								startIndex = textString.indexOf(nextWord, startIndex + 1);
								if(startIndex!=(textString.length() - nextWord.length())){
									while(Character.isLetter(textString.charAt(startIndex + nextWord.length()))){
										startIndex = textString.indexOf(nextWord, startIndex + 1);
									}
								}
							}
							try {
								highlighter.addHighlight(startIndex, nextWord.length() + startIndex, DefaultHighlighter.DefaultPainter);
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}
						}
					});
					
					
					addButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							
							int currentIndex = currentIndexMap.get(currentPanel);
							//remove this word from wrong word list vector
							String currrentWord = wrongWordsMap.get(currentPanel).get(currentIndex);
							Vector<String> temp = new Vector<String>(0);
							for(int i=currentIndex + 1; i<wrongWordsMap.get(currentPanel).size(); i++){
								if(!wrongWordsMap.get(currentPanel).elementAt(i).equals(currrentWord)){
									//System.out.println(wrongWordsMap.get(currentPanel).get(i));
									temp.add(wrongWordsMap.get(currentPanel).get(i));
								}
							}
							
							for(int i=currentIndex; i>=0; i--){
								temp.add(0, wrongWordsMap.get(currentPanel).get(i));
							}
							wrongWordsMap.replace(currentPanel, temp);
							//add current word to wl file
							currrentWord += '\n';
							try {
								String wlFileName = wordlistList.get(switchFileTabbedPane.getSelectedIndex());
								FileOutputStream overwriteStream = new FileOutputStream(wlFileName, true);
								overwriteStream.write(currrentWord.getBytes());
								overwriteStream.close();
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							//display next word
							currentIndex += 1;
							highlighter.removeAllHighlights();
							if (currentIndex == wrongWordsMap.get(currentPanel).size()) {
								//All wrong words have been dealt at this point, remove the panel
								fileList.get(selectedIndex).setEditable(true);
								highlighter.removeAllHighlights();
								spellcheckProgramPanel.removeAll();
								currentPanel.remove(spellcheckProgramPanel);
								spellcheckProgramOpened.set(switchFileTabbedPane.getSelectedIndex(), false);
								currentIndexMap.remove(currentPanel);
								spellcheckProgramMap.remove(currentPanel);
								wrongWordsMap.remove(currentPanel);
								return;	
							}
							//update the display label
							currentIndexMap.replace(currentPanel, currentIndex);
							String nextWord = wrongWordsMap.get(currentPanel).get(currentIndex);
							wrongWordDisplayLabel.setText("Spelling "+ nextWord);
							//update Jcombobox's options
							if(!currrentWord.equals(nextWord)){
								choices.removeAllItems();
								for(String suggestion: spellcheckProgramMap.get(currentPanel).get(nextWord)){
									choices.addItem(suggestion);
								}
							}
							//highlight next word
							Highlighter highlighter = fileList.get(selectedIndex).getHighlighter();
							String textString = fileList.get(selectedIndex).getText();
							if(occuranceMap.containsKey(nextWord)){
								int numberOfPreviousOccurance = occuranceMap.get(nextWord);
								numberOfPreviousOccurance += 1;
								occuranceMap.replace(nextWord, numberOfPreviousOccurance);
							}else{
								occuranceMap.put(nextWord, 0);
							}
							int startIndex = -1;
							for(int i=0; i<=occuranceMap.get(nextWord); i++){
								startIndex = textString.indexOf(nextWord, startIndex + 1);
								if(startIndex!=(textString.length() - nextWord.length())){
									while(Character.isLetter(textString.charAt(startIndex + nextWord.length()))){
										startIndex = textString.indexOf(nextWord, startIndex + 1);
									}
								}
							}
							try {
								highlighter.addHighlight(startIndex, nextWord.length() + startIndex, DefaultHighlighter.DefaultPainter);
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}
							
						}
					});
					
					closeButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							fileList.get(selectedIndex).setEditable(true);
							highlighter.removeAllHighlights();
							spellcheckProgramPanel.removeAll();
							currentPanel.remove(spellcheckProgramPanel);
							spellcheckProgramOpened.set(switchFileTabbedPane.getSelectedIndex(), false);
							currentIndexMap.remove(currentPanel);
							spellcheckProgramMap.remove(currentPanel);
							wrongWordsMap.remove(currentPanel);
							menuSpellChcek_Configure.setEnabled(true);
						}
					});	
					
					changeButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							int currentIndex = currentIndexMap.get(currentPanel);
							//change this word 
							String textfieldString = fileList.get(selectedIndex).getText();
							String currentWord = wrongWordsMap.get(currentPanel).get(currentIndex);
							
							int occuracnce = occuranceMap.get(currentWord);
							int startIndex = -1;
							for(int i=0; i<=occuracnce; i++){
								startIndex = textfieldString.indexOf(currentWord, startIndex + 1);
								if(startIndex!=(textString.length() - currentWord.length())){
									while(Character.isLetter(textfieldString.charAt(startIndex + currentWord.length()))){
										startIndex = textString.indexOf(currentWord, startIndex + 1);
									}
								}
							}
							String selection = String.valueOf(choices.getSelectedItem());
							StringBuilder stringBuilder = new StringBuilder();
							stringBuilder.append(textfieldString.substring(0, startIndex));
							stringBuilder.append(selection);
							stringBuilder.append(textfieldString.substring(startIndex + currentWord.length(), textfieldString.length()));
							fileList.get(selectedIndex).setText(stringBuilder.toString());
							occuracnce = occuracnce -1;
							if(occuracnce < 0){
								occuranceMap.remove(currentWord);
							}else{
								occuranceMap.replace(currentWord, occuracnce);
							}
		
							currentIndex += 1;
							highlighter.removeAllHighlights();
							if (currentIndex == wrongWordsMap.get(currentPanel).size()) {
								//All wrong words have been dealt at this point, remove the panel
								fileList.get(selectedIndex).setEditable(true);
								spellcheckProgramPanel.removeAll();
								currentPanel.remove(spellcheckProgramPanel);
								spellcheckProgramOpened.set(switchFileTabbedPane.getSelectedIndex(), false);
								currentIndexMap.remove(currentPanel);
								spellcheckProgramMap.remove(currentPanel);
								wrongWordsMap.remove(currentPanel);
								return;	
							}
							//update the display label
							currentIndexMap.replace(currentPanel, currentIndex);
							String nextWord = wrongWordsMap.get(currentPanel).get(currentIndex);
							wrongWordDisplayLabel.setText("Spelling "+ nextWord);
							//update Jcombobox's options
							if(!currentWord.equals(nextWord)){
								choices.removeAllItems();
								for(String suggestion: spellcheckProgramMap.get(currentPanel).get(nextWord)){
									choices.addItem(suggestion);
								}
							}
							//highlight next word
							Highlighter highlighter = fileList.get(selectedIndex).getHighlighter();
						    String textString = fileList.get(selectedIndex).getText();
							if(occuranceMap.containsKey(nextWord)){
								int numberOfPreviousOccurance = occuranceMap.get(nextWord);
								numberOfPreviousOccurance += 1;
								occuranceMap.replace(nextWord, numberOfPreviousOccurance);
							}else{
								occuranceMap.put(nextWord, 0);
							}
							startIndex = -1;
							for(int i=0; i<=occuranceMap.get(nextWord); i++){
								startIndex = textString.indexOf(nextWord, startIndex + 1);
								if(startIndex!=(textString.length() - nextWord.length())){
									while(Character.isLetter(textString.charAt(startIndex + nextWord.length()))){
										startIndex = textString.indexOf(nextWord, startIndex + 1);
									}
								}
							}
							try {
								highlighter.addHighlight(startIndex, nextWord.length() + startIndex, DefaultHighlighter.DefaultPainter);
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}
						}
					});
					
				}
			}
		});		
		
		//the Assignment 5 part
		menuUsers_add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new addCollaboratorDialog(TextEditorMain.this);
			}
		});
		
		menuUsers_remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				String[] options = new String[]{"swag", "swerf"};
//				new removeCollaboratorDialog(TextEditorMain.this, options);
				//create a request to server and wait for response, when the reponse is received in client's LoginGUI, the remove user panel will pop up and be populated with data
				Assignment5RequestResponse request = new Assignment5RequestResponse();
				request.setRequest(true);
				request.setFileName(getCurrentFileName());
				request.setOwnerName(getUsername());
				request.setType("GET_REMOVABLE_USERS");
				loginGUI.SendFileRequest(request);
				
			}
		});
	}
	
	//Helper functions part
	private void toggleMenuBar(Boolean fileOpen){
		if(fileOpen){
			menuEdit.setVisible(true);
			menuSpellCheck.setVisible(true);
			menuFile_Close.setEnabled(true);
			//guest will not be able to see the users panel
			System.out.println(username);
			if(!offline){
				menuUsers.setVisible(true);
			}
		}else{
			menuEdit.setVisible(false);
			menuSpellCheck.setVisible(false);
			menuFile_Close.setEnabled(false);
			menuUsers.setVisible(false);
		}	
	}
	
//	private void updateUndoRedoMenuItems(UndoManager manager){
//		if(manager.canRedo()){
//			menuEdit_Redo.setEnabled(true);
//		}else{
//			menuEdit_Redo.setEnabled(false);
//		}
//		if(manager.canUndo()){
//			menuEdit_Undo.setEnabled(true);
//		}else{
//			menuEdit_Undo.setEnabled(false);
//		}
//	}
//	
//	private void focusManagersOnThisFile(int focusIndex){
//		//bind the current focusManager, update the availibility of undo and redo buttons.
//		updateUndoRedoMenuItems(undoManagerList.get(focusIndex));
//		
//	}
	
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	    }
	} 
	
	private void offline_SaveFile(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save As..");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "txt files (*.txt)", "txt");
		fileChooser.setFileFilter(filter);
		File currentDirectory = fileChooser.getCurrentDirectory();
		String nameOfCurrentSelectedFile = switchFileTabbedPane.getTitleAt(switchFileTabbedPane.getSelectedIndex());
		String filepath = fileChooser.getCurrentDirectory().getPath() +"/"+ nameOfCurrentSelectedFile;
		File toBeSaved = new File(filepath);
		if(toBeSaved.exists()){
			fileChooser.setSelectedFile(new File(nameOfCurrentSelectedFile));
		}
		int returnVal = fileChooser.showSaveDialog(menuBar);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			//if the file already exists, overwrite the content
			toBeSaved = fileChooser.getSelectedFile();
			if(toBeSaved.exists()){
				int chosenOption = JOptionPane.showConfirmDialog(fileChooser, toBeSaved.getName() + "already exists. Do you want to replace it?", "Confirm Save as", JOptionPane.YES_NO_OPTION);
				if(chosenOption == JOptionPane.NO_OPTION){
					return;
				}
			}
			//if the file does not exist, or do exist but users choose to overwrite, save the file
			try {
				FileOutputStream overwriteStream = new FileOutputStream(toBeSaved, false);
				String currentFileContent = fileList.get(switchFileTabbedPane.getSelectedIndex()).getText();
				overwriteStream.write(currentFileContent.getBytes());
				overwriteStream.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//update the tab name to the name of saved file
			 switchFileTabbedPane.setTitleAt(switchFileTabbedPane.getSelectedIndex(), toBeSaved.getName());
			 file_OwnerHashmap.replace(jPanelList.get(switchFileTabbedPane.getSelectedIndex()), "offline");
			 toggleUserMenu();
		}
	}
	
	private void online_saveFile(){
		//query string from Server
		FileRequestResponse request = new FileRequestResponse(true);
		request.setUserName(this.username);
		request.setFileNameRequest(true);
		request.setFileSaveDialog(true);
		loginGUI.SendFileRequest(request);
		
		if(!isOwnerOfCurrentTab()){
			JOptionPane.showMessageDialog(this,
					"You're trying to save a copy of someone else's file!",
					"Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	private void online_OpenFile(){
		//query string from Server
		Assignment5RequestResponse request = new Assignment5RequestResponse();
		request.setRequest(true);
		request.setCollaboratorName(this.username);
		request.setType("GET_COLLABORATING_FILES");
		loginGUI.SendFileRequest(request);
	}
	
	private void offline_OpenFile(){

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open File...");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "txt files (*.txt)", "txt");
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(menuBar);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	//create a new file panel for this new opened file
		    JPanel newFile = new JPanel();
		    jPanelList.add(newFile);
		    file_OwnerHashmap.put(newFile, "offline");
		    newFile.setLayout(new BorderLayout());
		    JTextArea textArea = new JTextArea();
//		    //add undoManager on textarea
//		    UndoManager undoManager = new UndoManager();
//			undoManagerList.add(undoManager);
		    JScrollPane textScrollPane = new JScrollPane(textArea);
		    textScrollPane.getVerticalScrollBar().setUI(new customScrollBarUI());
		    newFile.add(textScrollPane, BorderLayout.CENTER);
			switchFileTabbedPane.add(fileChooser.getSelectedFile().getName(), newFile);
			File file = new File(fileChooser.getSelectedFile().getPath());
			try {
				Scanner scanner = new Scanner(file);
				StringBuilder stringBuilder = new StringBuilder();
				while(scanner.hasNextLine()){
					stringBuilder.append(scanner.nextLine());
					stringBuilder.append("\n");
				}
				textArea.setText(stringBuilder.toString());
				scanner.close();
			} catch (FileNotFoundException fnfe) {
				System.out.println("FileNotFoundException: " + fnfe.getMessage());
			}
			//add UndoManagerListener
//			textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
//				@Override
//				public void undoableEditHappened(UndoableEditEvent e) {
//					undoManager.addEdit(e.getEdit());
//					updateUndoRedoMenuItems(undoManager);
//				}
//			});
			//focus tabbedPane to the newly opened file
			fileOpen = true;
			toggleMenuBar(fileOpen);
			fileList.add(textArea);
			switchFileTabbedPane.setSelectedIndex(fileList.size() - 1);
			//Add default keyboard and wordlist filepath for this newly opened file
			String defaultKeyboardConfig = new String("qwerty-us.kb");
			String defaultWordlistpath = new String("wordlist.wl");
			keyboardConfigList.add(defaultKeyboardConfig);
			wordlistList.add(defaultWordlistpath);
			configurationPanelOpened.add(false);
			spellcheckProgramOpened.add(false);
		    toggleUserMenu();
	    }
	
	}
	
	public void openNewFile(String filename, String content, String owner){
    	//create a new file panel for this new opened file
	    JPanel newFile = new JPanel();
	    jPanelList.add(newFile);
	    file_OwnerHashmap.put(newFile, owner);
	    newFile.setLayout(new BorderLayout());
	    JTextArea textArea = new JTextArea();
	    //add undoManager on textarea
//	    UndoManager undoManager = new UndoManager();
//		undoManagerList.add(undoManager);
	    JScrollPane textScrollPane = new JScrollPane(textArea);
	    textScrollPane.getVerticalScrollBar().setUI(new customScrollBarUI());
	    newFile.add(textScrollPane, BorderLayout.CENTER);
		switchFileTabbedPane.add(filename, newFile);
		textArea.setText(content);
		//add UndoManagerListener
//		textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
//			@Override
//			public void undoableEditHappened(UndoableEditEvent e) {
//				undoManager.addEdit(e.getEdit());
//				updateUndoRedoMenuItems(undoManager);
//			}
//		});
		//focus tabbedPane to the newly opened file
		fileOpen = true;
		toggleMenuBar(fileOpen);
		fileList.add(textArea);
		switchFileTabbedPane.setSelectedIndex(fileList.size() - 1);
		//Add default keyboard and wordlist filepath for this newly opened file
		String defaultKeyboardConfig = new String("qwerty-us.kb");
		String defaultWordlistpath = new String("wordlist.wl");
		keyboardConfigList.add(defaultKeyboardConfig);
		wordlistList.add(defaultWordlistpath);
		configurationPanelOpened.add(false);
		spellcheckProgramOpened.add(false);
	    toggleUserMenu();
	}
	
	private void toggleUserMenu(){
		int index = switchFileTabbedPane.getSelectedIndex();
		
		if(file_OwnerHashmap.get(jPanelList.get(index)).equalsIgnoreCase(this.username)){
			menuUsers.setVisible(true);
		}else{
			menuUsers.setVisible(false);
		}
	}
	
	public void setFileName(String fileName){
		 switchFileTabbedPane.setTitleAt(switchFileTabbedPane.getSelectedIndex(), fileName);
	}
	
	
	//getters and setters
	public void setOffline(boolean offline){
		this.offline = offline;
	}
	
	public boolean getOffline(){
		return offline;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setLoginGUI(LoginGUI gui){
		this.loginGUI = gui;
	}
	
	public LoginGUI getLoginGUI(){
		return this.loginGUI;
	}
	
	public String getCurrentContent(){
		return fileList.get(switchFileTabbedPane.getSelectedIndex()).getText();
	}
	
	public String getCurrentFileName(){
		return switchFileTabbedPane.getTitleAt(switchFileTabbedPane.getSelectedIndex());
	}


	public boolean isHasBeenAuthenticated() {
		return hasBeenAuthenticated;
	}


	public void setHasBeenAuthenticated(boolean hasBeenAuthenticated) {
		this.hasBeenAuthenticated = hasBeenAuthenticated;
	}
	
	public boolean isOwnerOfCurrentTab(){
		int index = switchFileTabbedPane.getSelectedIndex();
		if(file_OwnerHashmap.get(jPanelList.get(index)).equalsIgnoreCase(this.username)){
			return true;
		}else{
			return false;
		}
	}
	
	public synchronized void sendCurrentStateToServer(){
		String ownerName; 
		String fileName;
		synchronized (jPanelList) {
			synchronized (fileList) {
				synchronized (file_OwnerHashmap) {
					Assignment5RequestResponse overAllrequest = new Assignment5RequestResponse();
					overAllrequest.setRequest(true);
					overAllrequest.setType("SINGLE_CLIENT_UPDATE_VECTOR");
					//iterate through all tabs, send updates to server
					for(int i = 0;  i < jPanelList.size(); i++){
				    	ownerName = file_OwnerHashmap.get(jPanelList.get(i));
				    	fileName = switchFileTabbedPane.getTitleAt(i);
				    	if(!ownerName.equalsIgnoreCase("offline") && !fileName.equalsIgnoreCase("new")) {
							Assignment5RequestResponse request1 = new Assignment5RequestResponse();
							request1.setRequest(true);
							request1.setType("SINGLE_CLIENT_UPDATE_SINGLE");
							request1.setFileName(fileName);
							request1.setOwnerName(ownerName);
							request1.setFileContent(fileList.get(i).getText());
							overAllrequest.addUpdates(request1);
						}
					}
					//after interation, send a finish singal to server
					System.out.println("Sending updating vector from client: ");
					loginGUI.SendFileRequest(overAllrequest);

				}
			}
		}
	}
	
	public synchronized void updateAllTabs(Assignment5RequestResponse response) {
		String ownerName = response.getOwnerName();
		String fileName = response.getFileName();
		String ownerNameLocal;
		String fileNameLocal;
		synchronized (jPanelList) {
			synchronized (fileList) {
				synchronized (file_OwnerHashmap) {
					for(int i = 0;  i < jPanelList.size(); i++){
						ownerNameLocal = file_OwnerHashmap.get(jPanelList.get(i));
						fileNameLocal = switchFileTabbedPane.getTitleAt(i);
						if(!ownerName.equalsIgnoreCase("offline")){
							if(fileNameLocal.equalsIgnoreCase(fileName)&&
									ownerNameLocal.equalsIgnoreCase(ownerName)&&
									!fileNameLocal.equalsIgnoreCase("new")){
								fileList.get(i).setText(response.getFileContent());
							}
						}
					}
				}
			}
		}
	}
	
	public void removeTab(String ownerName, String filename){
		String ownerNameLocal;
		String fileNameLocal;
		synchronized (jPanelList) {
			synchronized (fileList) {
				synchronized (file_OwnerHashmap) {
					for(int i = 0;  i < jPanelList.size(); i++){
						ownerNameLocal = file_OwnerHashmap.get(jPanelList.get(i));
						fileNameLocal = switchFileTabbedPane.getTitleAt(i);
						if(ownerNameLocal.equalsIgnoreCase(ownerName) &&
								fileNameLocal.equalsIgnoreCase(filename)){
							switchFileTabbedPane.setTitleAt(i, "new");
						}
					}
				}
			}
		}
	}
	
	
	
	
}

