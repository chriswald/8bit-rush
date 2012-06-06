package pkg;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

class Camera extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	
	public BufferedImage todraw;
	public BufferedImage buffer;
	public Level lvl;
	public ArrayList<CameraDrawable> drawables;
	public int posx;
	public int posy;
	
	public Camera() {
		int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		todraw = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_ARGB);
		drawables = new ArrayList<CameraDrawable>(0);
		posx = 0;
		posy = 0;
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.addKeyListener(this);
	}
	
	public void setLevel(Level l) {
		lvl = l;
		buffer = new BufferedImage(l.width, l.height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void addDrawable(CameraDrawable a) {
		drawables.add(a);
		for (CameraDrawable c : drawables)
			System.out.println(c.toString());
	}
	
	private void preparetodraw() {		
		Graphics gbuf = buffer.getGraphics();
		Graphics gtod = todraw.getGraphics();
		
		for (CameraDrawable c : drawables) {
			for (Artifact a : c.getArtifacts()) {
				gbuf.drawImage(a.img, a.x, a.y, null);
			}
		}
		
		gtod.drawImage(buffer, 0, 0, todraw.getWidth(), todraw.getHeight(), posx, posy, todraw.getWidth(), todraw.getHeight(), null);
	}
	
	public void paint(Graphics g) {
		preparetodraw();
		g.drawImage(todraw, 0, 0, null);
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
