package pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Player implements KeyListener, CameraDrawable{	
	public boolean up = false;
	public boolean right = false;
	public boolean down = false;
	public boolean left = false;
	public boolean space = false;
	
	public boolean rightwall = false;
	public boolean leftwall = false;
	public boolean ground = false;
	public boolean ceiling = false;
	
	public BufferedImage img;
	
	public int posx = 0;
	public int posy = 0;
	public double velx = 0;
	public double vely = 0;
	
	public String imgfile = "player.jpg";
	
	public Player() {		
		loadImage();
	}
	
	private void loadImage() {
		String filename = GameLoop.RESDIR + GameLoop.IMGDIR + imgfile;
		BufferedImage tmp = new BufferedImage(10, 20, BufferedImage.TYPE_INT_ARGB);
		try {
			tmp = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(null, "Cannot load the image file for the player.\n" + filename);
			drawPlayer(tmp);
		}
		
		img = tmp;
	}
	
	private void drawPlayer(BufferedImage img) {
		Graphics g = img.getGraphics();
		g.setColor(Color.red);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}
	
	public void update() {
		if (right) {
			changevelx(1);
		}
		if (left) {
			changevelx(-1);
		}
		
		move();
	}
	
	private void checkbounds() {
		if (posx <= 0) {
			leftwall = true;
			velx = 0;
		}
		else {
			leftwall = false;
		}
		
		if (posx >= 1600 - img.getWidth()) { 
			rightwall = true;
			velx = 0;
		}
		else {
			rightwall = false;
		}
		
		if (posy >= 900 - img.getHeight()) {
			ground = true;
			vely = 0;
		}
		else {
			ground = false;
		}
	}
	
	private void move() {
		posx += velx;
		posy += vely;
		
		checkbounds();
		
		if (leftwall)
			posx = 0;
		if (rightwall)
			posx = 1600-img.getWidth();
		if (ground)
			posy = 900-img.getHeight();
		else
			vely -= .5;
	}
	
	private void changevelx(double delta) {
		velx += delta;
		if (velx < -5)
			velx = -5;
		if (velx > 5)
			velx = 5;
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			up = true;
			down = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			left = false;
			System.out.println("R");
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			up = false;
			break;
		case KeyEvent.VK_LEFT:
			left = true;
			right = false;
			System.out.println("L");
			break;
		case KeyEvent.VK_SPACE:
			space = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_SPACE:
			space = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		
	}


	@Override
	public ArrayList<Artifact> getArtifacts() {		
		ArrayList<Artifact> list = new ArrayList<Artifact>(1);
		list.add(new Artifact(posx, posy, img));
		
		return list;
	}

}
