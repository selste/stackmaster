package de.dennismaass.emp.stonemaster.stackmaster.controller.util;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtils {

	public static ImageIcon getResizedImage(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		return icon;
	}

}
