package pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level implements CameraDrawable{
	public int width;
	public int height;
	
	public Level(int w, int h) {
		this.width = w;
		this.height = h;
	}

	@Override
	public ArrayList<Artifact> getArtifacts() {
		ArrayList<Artifact> list = new ArrayList<Artifact>(1);
		BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tmp.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		list.add(new Artifact(0, 0, tmp));
		return list;
	}
	
	public String toString() {
		return this.getClass() + " " + width + " " + height;
	}
}
