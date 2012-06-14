package pkg;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Map implements CameraDrawable {
    public ArrayList<Block> map = new ArrayList<Block>();
    public BufferedImage    image;
    public int              w, h;
    public int              blockwidth, blockheight;

    public Map(int width, int height, int bwidth, int bheight) {
        w = width;
        h = height;
        this.blockwidth = bwidth;
        this.blockheight = bheight;
    }

    public void makeImage() {
        image = new BufferedImage(w * this.blockwidth, h * blockheight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        for (Block b : map) {
            g.drawImage(b.img, (int) b.posx, (int) b.posy, null);
        }
    }

    public void add(Block b) {
        map.add(b);
        makeImage();
    }

    public Block get(int x, int y) {
        return find(x, y);
    }

    public Block find(int x, int y) {
        for (Block b : map) {
            if (b.x == x && b.y == y)
                return b;
        }
        return null;
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {

        ArrayList<Artifact> list = new ArrayList<Artifact>(1);
        list.add(new Artifact(0, 0, image));

        return list;
    }
}
