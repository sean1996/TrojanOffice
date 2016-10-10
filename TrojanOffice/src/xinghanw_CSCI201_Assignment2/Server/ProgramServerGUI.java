package xinghanw_CSCI201_Assignment2.Server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import xinghanw_CSCI201_Assignment2.CustomGUI.MyButton;
import xinghanw_CSCI201_Assignment2.CustomGUI.customScrollBarUI;

public class ProgramServerGUI extends JFrame {
	public static final long serialVersionUID = 123;
	
	protected JTextArea textArea;
	private JScrollPane scrollPane;
	private MyButton startButton;
	private Font customFont;
	private ProgramServer programServer;
	private Thread currentThread;
	

	public ProgramServerGUI(ProgramServer programServer){
		super("Server");
		//set global look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch (Exception e){
			System.out.println("Warning! Cross-platform L&F not used!");
		}
		
		instantiateComponets();
		createGUI();
		addActions();
		
		setSize(800, 500);
		setLocation(500, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.programServer = programServer; 
	}
	
	private void instantiateComponets(){
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setEnabled(false);
		startButton = new MyButton("Start");
		
	}
	
	private void createGUI(){
		scrollPane.getVerticalScrollBar().setUI(new customScrollBarUI());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(startButton, BorderLayout.SOUTH);
		
	}
	
	private void addActions(){
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(startButton.getText().equals("Start")){
					Thread thread = new Thread(programServer);
					currentThread = thread;
					thread.start();
					startButton.setText("Stop");
				}else{
					currentThread.interrupt();
					programServer.stopListening();
					textArea.setText(textArea.getText() + "Server stopped." +"\n" );
					startButton.setText("Start");
				}
				
			}
		});
	}
	
	public void addNewLineToLogBoard(String message){
		textArea.setText(textArea.getText() + message +  "\n" );
	}
	
	
}
