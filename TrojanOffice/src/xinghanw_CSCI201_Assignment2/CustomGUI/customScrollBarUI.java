package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.metal.MetalScrollBarUI;

public class customScrollBarUI extends MetalScrollBarUI{
	private ImageIcon imageUP, imageDown;
	private Image imageMain, imageBlank;
	
	public customScrollBarUI() {
		try{
			imageUP = new ImageIcon("resource/img/scrollbar/red_sliderUp.png");
        	imageDown =new ImageIcon("resource/img/scrollbar/red_sliderDown.png");
        	imageMain = ImageIO.read(new File("resource/img/scrollbar/red_button05.png"));
        	imageBlank = ImageIO.read(new File("resource/img/scrollbar/red_button03.png"));
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
         
    }
	
	
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		((Graphics2D) g).drawImage(imageBlank, trackBounds.x, trackBounds.y, (int)trackBounds.getWidth(), (int)trackBounds.getHeight(), null);
	}
	
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		// TODO Auto-generated method stub
		((Graphics2D) g).drawImage(imageMain, thumbBounds.x, thumbBounds.y, (int)thumbBounds.getWidth(), (int)thumbBounds.getHeight(), null);
	}
	
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
		JButton decreaseButton = new JButton(getCorrectIcon(orientation)){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(15, 15);
            }
        };
        return decreaseButton;
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
		// TODO Auto-generated method stub
		JButton IncreaseButton = new JButton(getCorrectIcon(orientation)){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(15, 15);
            }
        };
        return IncreaseButton;
	}
	
	private ImageIcon getCorrectIcon(int orientation){
		switch(orientation){
        case SwingConstants.SOUTH: return imageDown;
        case SwingConstants.NORTH: return imageUP;
            default: return imageUP;
		}	
	}
}
