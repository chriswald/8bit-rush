package pkg;

import java.awt.image.BufferedImage;

public class Artifact {
	private static int ID = 0;
	private static Database db = new Database();
	
	private final String Xid = "artifact" + ID + ".x";
	private final String Yid = "artifact" + ID + ".y";
	private final String IMGid = "artifact" + ID + ".img";
	
	public int x, y, img;
	
	public Artifact(int x, int y, BufferedImage img) {
		ID ++;
		this.x = db.add(Xid, x);
		this.y = db.add(Yid, y);
		this.img = db.add(IMGid, img);
	}
	
	public BufferedImage img() {
		return (BufferedImage) db.get(img);
	}
	
	public int x() {
		return (Integer) db.get(x);
	}
	
	public int y() {
		return (Integer) db.get(y);
	}
}
