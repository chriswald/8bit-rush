package pkg;

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
		list.add(new Artifact(0, 0, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)));
		return list;
	}
}
