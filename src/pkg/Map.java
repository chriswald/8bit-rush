package pkg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map implements CameraDrawable {
    public Block[][] map;
    public int       w, h;
    public int       blockwidth, blockheight;

    public Map(int width, int height, int bwidth, int bheight) {
        map = new Block[width][height];
        w = width;
        h = height;
        this.blockwidth = bwidth;
        this.blockheight = bheight;
    }

    public void add(Block b) {
        map[b.x][b.y] = b;
    }

    public Block get(int x, int y) {
        return map[x][y];
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {
        BufferedImage tmp = new BufferedImage(w * this.blockwidth, h
                * blockheight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = tmp.getGraphics();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (map[i][j] != null) {
                    g.drawImage(map[i][j].img, i * this.blockwidth, j
                            * this.blockheight, null);
                }
            }
        }

        ArrayList<Artifact> list = new ArrayList<Artifact>(1);
        list.add(new Artifact(0, 0, tmp));

        return list;
    }
}
