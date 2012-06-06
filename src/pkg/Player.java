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
		handlejumping();
		handlerunning();

		move();
	}
	
	private void handlejumping() {
		if (space) {
			if (!ground) {
				if (rightwall) {
					velx = -10;
					vely = -7;
				}
				if (leftwall) {
					velx = 10;
					vely = -7;
				}
			} else {
				vely = -9;
			}
		}
	}
	
	private void handlerunning() {
		if (right) {
			changevelx(1);
		}
		else if (left) {
			changevelx(-1);
		}
		else {
			if (ground)
				velx = 0;
			else
				if (velx > 0)
					velx -= 1;
				else if (velx < 0)
					velx += 1;
		}
	}
	
	private void checkbounds() {
		if (posx <= 0) {
			leftwall = true;
		}
		else {
			leftwall = false;
		}
		
		if (posx >= 1600 - img.getWidth()) { 
			rightwall = true;
		}
		else {
			rightwall = false;
		}
		
		if (posy >= 900 - img.getHeight()) {
			ground = true;
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
			if (rightwall || leftwall)
				vely += .25;
			else
				vely += .5;
	}
	
	private void changevelx(double delta) {
		if (velx > -5 || velx < 5)
			velx += delta;
		
		/*velx += delta;
		if (ground) {
			if (velx < -5)
				velx = -5;
			if (velx > 5)
				velx = 5;
		}*/
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_W:
			up = true;
			down = false;
			break;
		case KeyEvent.VK_D:
			right = true;
			left = false;
			break;
		case KeyEvent.VK_S:
			down = true;
			up = false;
			break;
		case KeyEvent.VK_A:
			left = true;
			right = false;
			break;
		case KeyEvent.VK_SPACE:
			space = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_W:
			up = false;
			break;
		case KeyEvent.VK_D:
			right = false;
			break;
		case KeyEvent.VK_S:
			down = false;
			break;
		case KeyEvent.VK_A:
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
	
	public String toString() {
		return this.getClass() + " " + img.getWidth() + " " + img.getHeight();
	}

}
