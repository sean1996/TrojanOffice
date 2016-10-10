package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;

public class MyTabbedPane extends JTabbedPane {
	@Override
	protected void paintComponent(Graphics g) {
		Image background;
		try {
			background = ImageIO.read(new File("resource/img/backgrounds/darkgrey_panel.png"));
			g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
		
		//draw the string in the center
		if(getSelectedIndex() == -1){
				try {
		          Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("kenvector_future.ttf")).deriveFont(25f);
		          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		          ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("kenvector_future.ttf")));
		          g.setFont(customFont);
		          g.setColor(new Color(248, 106, 43));
		          String text = "Trojan Office";
		          FontMetrics fMetrics = getFontMetrics(customFont);
		  		  g.drawString(text, (getWidth() - fMetrics.stringWidth(text))/2, (getHeight() - fMetrics.getHeight())/2);
		  		  
				} catch (IOException e) {
		          e.printStackTrace();
				}
		     	catch(FontFormatException e)
				{
		          e.printStackTrace();
				}
		}
		
		
		super.paintComponent(g);
	}
	
	private void loadStartupUI(){
		
	}
	
	private void loadLoginUI(){
		
	}
	
	private void loadRegistrationUI(){
		
	}
}
