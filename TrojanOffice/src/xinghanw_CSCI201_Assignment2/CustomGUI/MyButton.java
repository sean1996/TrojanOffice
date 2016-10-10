package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public  class MyButton extends JButton{
	
	Boolean mouseOver = false;
	Image background;
	Image backgroundMouseOver;
		
	 public MyButton(String name) {
		// TODO Auto-generated constructor stub
		 super(name);
		 //setBackground(new Color(248, 106, 43));
		 setContentAreaFilled(false);
		 setBorderPainted(false);
		 setFocusable(false);
		 
		 try {
			background = ImageIO.read(new File("resource/img/menu/red_button11.png"));
			backgroundMouseOver = ImageIO.read(new File("resource/img/menu/red_button11_selected.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		 
		 this.addMouseListener(new MouseAdapter() {
			 
			 @Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				mouseOver = true;
				Graphics graphics= ((MyButton)e.getSource()).getGraphics();
				((MyButton)e.getSource()).paintComponent(graphics);
			}
			 
			 @Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				mouseOver = false;
				Graphics graphics= ((MyButton)e.getSource()).getGraphics();
				((MyButton)e.getSource()).paintComponent(graphics);
				
			}
		});
		 
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		if(mouseOver){
			g.drawImage(backgroundMouseOver, 0, 0, this.getWidth(), this.getHeight(), null);
		}else{
			g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
		}
		super.paintComponent(g);
	}
	

}
