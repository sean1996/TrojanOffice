package xinghanw_CSCI201_Assignment2.CustomGUI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class customComboBoxUI extends BasicComboBoxUI{
	@Override
	protected JButton createArrowButton() {
		// TODO Auto-generated method stub
		ImageIcon icon = new ImageIcon("resource/img/menu/red_sliderDown.png");
		JButton customButton = new JButton(icon);
		customButton.setBorderPainted(false);
		return customButton;
	}
}