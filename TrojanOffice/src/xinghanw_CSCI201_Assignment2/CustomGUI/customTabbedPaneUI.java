package xinghanw_CSCI201_Assignment2.CustomGUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class customTabbedPaneUI extends BasicTabbedPaneUI{
	public customTabbedPaneUI() {
		
	}
	
	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
			boolean isSelected) {
		// TODO Auto-generated method stub
		Color fillColor;
		if(isSelected){
			fillColor = new Color(248, 106, 43);
		}else{
			
			fillColor = Color.CYAN;
		}
		g.setColor(fillColor);
		g.fillRect(x, y, w, h);
	}
}