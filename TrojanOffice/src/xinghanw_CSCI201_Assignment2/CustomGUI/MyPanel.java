package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MyPanel extends JPanel{
	private Font customFont = null;
	
	public MyPanel(){
		super();
	}
	
	public MyPanel(Font customFont) {
		// TODO Auto-generated constructor stub
		super();
		this.customFont = customFont;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//paint background 
		Image background;
		try {
			background = ImageIO.read(new File("resource/img/backgrounds/darkgrey_panel.png"));
			g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
		//paint string Trojan Office
		if(customFont != null){
			g.setFont(customFont);
			g.setColor(new Color(248, 106, 43));
			String text = "Trojan Office";
			FontMetrics fMetrics = getFontMetrics(customFont);
			g.drawString(text, (getWidth() - fMetrics.stringWidth(text))/2, (getHeight() - fMetrics.getHeight())/2 - 100);
		}
		
	}
	
	

}
