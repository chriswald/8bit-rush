package pkg;

import java.awt.image.BufferedImage;

public class Block extends Collider {
    public BufferedImage img;
    public int           x, y;

    public Block(BufferedImage img, int x, int y, String collide) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.posx = this.x * img.getWidth();
        this.posy = this.y * img.getHeight();

        this.width = img.getWidth();
        this.height = img.getHeight();

        if (collide.charAt(0) == '1')
            topside = true;
        else
            topside = false;

        if (collide.charAt(1) == '1')
            rightside = true;
        else
            rightside = false;

        if (collide.charAt(2) == '1')
            bottomside = true;
        else
            bottomside = false;

        if (collide.charAt(3) == '1')
            leftside = true;
        else
            leftside = false;
    }

    @Override
    public void onCollide(CollidedSide side, Collider c) {
        // TODO Auto-generated method stub

    }
}
