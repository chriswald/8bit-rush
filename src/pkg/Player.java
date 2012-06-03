package pkg;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Player implements KeyListener, CameraDrawable{
	private static int ID = 0;
	private final String UPid = "player" + ID + ".up";
	private final String RIGHTid = "player" + ID + ".right";
	private final String DOWNid = "player" + ID + ".down";
	private final String LEFTid = "player" + ID + ".left";
	private final String IMGid = "player" + ID + ".img";
	private final String IMGFILEid = "player" + ID + ".imgfile";
	private final String POSXid = "player" + ID + ".posx";
	private final String POSYid = "player" + ID + ".posy";
	
	public int up, right, down, left, img, imgfile, posx, posy;
	
	public Player() {
		ID ++;
		up = GameLoop.db.add(UPid, false);
		right = GameLoop.db.add(RIGHTid, false);
		down = GameLoop.db.add(DOWNid, false);
		left = GameLoop.db.add(LEFTid, false);
		imgfile = GameLoop.db.getint(IMGFILEid, "player.jpg");
		posx = GameLoop.db.add(POSXid, 0);
		posy = GameLoop.db.add(POSYid, 0);
		
		if (!GameLoop.db.has(IMGid))
			loadImage();
		else
			img = GameLoop.db.find(IMGid);
	}
	
	private void loadImage() {
		String filename = (String) GameLoop.db.get(GameLoop.resourcedir) + (String) GameLoop.db.get(GameLoop.imagedir) + (String) GameLoop.db.get(imgfile);
		try {
			img = GameLoop.db.add(IMGid, ImageIO.read(new File(filename)));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(null, "Cannot load the image file for the player.\n" + filename);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}


	@Override
	public ArrayList<Artifact> getArtifacts() {
		// TODO Auto-generated method stub
		return null;
	}

}
