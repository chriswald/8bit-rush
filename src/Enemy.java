import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class Enemy extends Actor {

    public Enemy(String filename) {
        super(filename);
        ID = "enemy";
        rightside = leftside = topside = bottomside = true;
        rightwall = leftwall = ground = ceiling = false;
    }

    @Override
    public void interact() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        if (ground) {
            vely = 0;
        } else {
            vely += .5;
        }

        if (rightwall || leftwall)
            velx = -velx;

        if (vely > this.height / 2)
            vely = this.height / 2;

        posx += velx;
        posy += vely;
    }

    @Override
    public void onCollide(CollidedSide side, Collider c) {
        switch (side) {
        case BOTTOM:
            this.posy = c.posy - this.height;
            this.vely = 0;
            this.ground = true;
            break;
        case RIGHT:
            this.posx = c.posx - this.width;
            this.rightwall = true;
            break;
        case LEFT:
            this.posx = c.posx + c.width;
            this.leftwall = true;
            break;
        case TOP:
            this.posy = c.posy + c.height;
            this.vely = Math.abs(this.vely);
            this.ceiling = true;
            break;
        default:
            break;
        }
    }

    @Override
    public void drawPlayer() {
        Graphics g = img.getGraphics();
        g.setColor(Color.green);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {
        ArrayList<Artifact> tmp = new ArrayList<Artifact>(1);
        tmp.add(new Artifact((int) posx, (int) posy, img));
        return tmp;
    }

    @Override
    public void die() {
        alive = false;
    }
}
