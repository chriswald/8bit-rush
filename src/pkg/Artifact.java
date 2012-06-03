package pkg;

import java.awt.image.BufferedImage;

public class Artifact {
	private static int ID = 0;
	
	private final String Xid = "artifact" + ID + ".x";
	private final String Yid = "artifact" + ID + ".y";
	private final String IMGid = "artifact" + ID + ".img";
	
	public int x, y, img;
	
	public Artifact(int x, int y, BufferedImage img) {
		ID ++;
		this.x = GameLoop.db.add(Xid, x);
		this.y = GameLoop.db.add(Yid, y);
		this.img = GameLoop.db.add(IMGid, img);
	}
	
	public BufferedImage img() {
		return (BufferedImage) GameLoop.db.get(img);
	}
	
	public int x() {
		return (Integer) GameLoop.db.get(x);
	}
	
	public int y() {
		return (Integer) GameLoop.db.get(y);
	}
}
