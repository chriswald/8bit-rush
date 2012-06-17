

import java.awt.Point;

abstract class Collider {
    public double posx;
    public double posy;
    public int    width;
    public int    height;
    public double velx;
    public double vely;

    public String ID = "";

    public boolean topside, rightside, bottomside, leftside;

    public abstract void onCollide(CollidedSide side, Collider c);

    public void checkCollide(Collider c) {
        boolean collided = ((this.posx + this.width) > c.posx
                && this.posx < (c.posx + c.width)
                && (this.posy + this.height) > c.posy && this.posy < (c.posy + c.height));

        if (collided) {
            Point tmid = this.getMid();
            Point cmid = c.getMid();

            int deltax = Math.abs(tmid.x - cmid.x);
            int deltay = Math.abs(tmid.y - cmid.y);

            if (deltax > deltay) {
                if (tmid.x < cmid.x) {
                    if (this.rightside)
                        c.onCollide(CollidedSide.LEFT, this);
                    if (c.leftside)
                        this.onCollide(CollidedSide.RIGHT, c);
                } else {
                    if (this.leftside)
                        c.onCollide(CollidedSide.RIGHT, this);
                    if (c.rightside)
                        this.onCollide(CollidedSide.LEFT, c);
                }
            } else {
                if (tmid.y <= cmid.y) {
                    if (this.bottomside)
                        c.onCollide(CollidedSide.TOP, this);
                    if (c.topside)
                        this.onCollide(CollidedSide.BOTTOM, c);
                } else {
                    if (this.topside)
                        c.onCollide(CollidedSide.BOTTOM, this);
                    if (c.bottomside)
                        this.onCollide(CollidedSide.TOP, c);
                }
            }
        }
    }

    public Point getMid() {
        return new Point((int) (this.posx + this.width / 2),
                (int) (this.posy + this.height / 2));
    }
}
