package pkg;

import java.awt.image.BufferedImage;

public class Block extends Collider {
    public BufferedImage img;
    public int           x, y;

    public Block(BufferedImage img, int x, int y, String collide) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.posx = this.x * 50;
        this.posy = this.y * 50;

        this.width = 50;
        this.height = 50;

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
            leftside = true;
    }

    @Override
    public void onCollide(CollidedSide side, Collider c) {
        // TODO Auto-generated method stub

    }
}
