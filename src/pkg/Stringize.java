package pkg;

import java.awt.image.BufferedImage;

public class Stringize {
	public static String tos(int i) {
		return "" + i;
	}
	
	public static String tos(boolean b) {
		return (b ? "true" : "false");
	}
	
	public static String tos(String s) {
		return s;
	}
	
	public static String tos(BufferedImage img) {
		String tmp = "";
		for (int x = 0; x < img.getWidth(); x ++) {
			for (int y = 0; y < img.getHeight(); y ++) {
				int color = img.getRGB(x, y);
				int r = (color & 0x00ff0000) >> 16;
				int g = (color & 0x0000ff00) >> 8;
				int b = (color & 0x000000ff);
				tmp += r + "," + g + "," + b + ",";
			}
		}
		return tmp;
	}
	
	public static String tos(Level lvl) {
		return null;
	}
}
