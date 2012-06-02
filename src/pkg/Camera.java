package pkg;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

class Camera extends JFrame{
	private static final long serialVersionUID = 1L;
	private BufferedImage todraw;
	private BufferedImage buffer;
	private Level lev;
	private ArrayList<Artifact> artifacts;
	
	private int posx = 0;
	private int posy = 0;
	
	public Camera() {
		int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		todraw = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setLevel(Level l) {
		this.lev = l;
		buffer = new BufferedImage(this.lev.width(), this.lev.height(), BufferedImage.TYPE_INT_ARGB);
	}
	
	public void addArtifact(Artifact a) {
		artifacts.add(a);
	}
	
	public void addArtifact(CameraDrawable c) {
		artifacts.addAll(c.getArtifacts());
	}
	
	private void preparetodraw() {
		Graphics gbuf = buffer.getGraphics();
		Graphics gtod = todraw.getGraphics();
		for (Artifact a : artifacts) {
			gbuf.drawImage(a.img, a.x, a.y, null);
		}
		
		gtod.drawImage(buffer, 0, 0, todraw.getWidth(), todraw.getHeight(), posx, posy, todraw.getWidth(), todraw.getHeight(), null);
	}
	
	public void paint(Graphics g) {
		preparetodraw();
		g.drawImage(todraw, 0, 0, null);
	}
}
