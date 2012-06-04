package pkg;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

class Camera extends JFrame{
	private static final long serialVersionUID = 1L;
	private static int ID = 0;
	private static Database db = new Database();
	
	private final String TODRAWid = "camera" + ID + ".todraw";
	private final String BUFFERid = "camera" + ID + ".buffer";
	private final String LEVid= "camera" + ID + ".lev";
	private final String ARTIFACTSid = "camera" + ID + ".artifacts";
	private final String POSXid = "camera" + ID + ".posx";
	private final String POSYid = "camera" + ID + ".posy";
	
	public int todraw;
	public int buffer;
	public int lev;
	public int artifacts;
	public int posx;
	public int posy;
	
	public Camera() {
		ID ++;
		int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
		int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		todraw = db.getint(TODRAWid, new BufferedImage(sw, sh, BufferedImage.TYPE_INT_ARGB));
		artifacts = db.getint(ARTIFACTSid, new ArrayList<Artifact>(0));
		posx = db.getint(POSXid, 0);
		posy = db.getint(POSYid, 0);
	}
	
	public void setLevel(Level l) {
		lev = db.getint(LEVid, l);
		buffer = db.getint(BUFFERid, new BufferedImage(l.width(), l.height(), BufferedImage.TYPE_INT_ARGB));
		
	}
	
	public void addArtifact(Artifact a) {
		@SuppressWarnings("unchecked")
		ArrayList<Artifact> list = (ArrayList<Artifact>) db.get(artifacts);
		list.add(a);
		db.set(artifacts, list);
	}
	
	public void addArtifact(CameraDrawable c) {
		@SuppressWarnings("unchecked")
		ArrayList<Artifact> list = (ArrayList<Artifact>) db.get(artifacts);
		list.addAll(c.getArtifacts());
		db.set(artifacts, list);
	}
	
	@SuppressWarnings("unchecked")
	private void preparetodraw() {
		BufferedImage buf = (BufferedImage) db.get(buffer);
		BufferedImage tod = (BufferedImage) db.get(todraw);
		ArrayList<Artifact> list = (ArrayList<Artifact>) db.get(artifacts);
		
		Graphics gbuf = buf.getGraphics();
		Graphics gtod = tod.getGraphics();
		for (Artifact a : list) {
			gbuf.drawImage(a.img(), a.x(), a.y(), null);
		}
		
		gtod.drawImage(buf, 0, 0, tod.getWidth(), tod.getHeight(), posx, posy, tod.getWidth(), tod.getHeight(), null);
		
		db.set(buffer, buf);
		db.set(todraw, tod);
	}
	
	public void paint(Graphics g) {
		preparetodraw();
		
		BufferedImage tod = (BufferedImage) db.get(todraw);
		
		g.drawImage(tod, 0, 0, null);
		
		db.set(todraw, tod);
	}
}
