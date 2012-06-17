import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

class Player extends Character implements CameraDrawable, KeyListener {
    public boolean    up             = false;
    public boolean    right          = false;
    public boolean    down           = false;
    public boolean    left           = false;
    public boolean    space          = false;

    public boolean    rightwall      = false;
    public boolean    leftwall       = false;
    public boolean    ground         = false;
    public boolean    ceiling        = false;

    public static int MAXGROUNDSPEED = 5;
    public static int MAXAIRSPEED    = 4;

    public Player(String filename) {
        super(filename);
        ID = "player";
        rightside = leftside = topside = bottomside = true;
    }

    @Override
    public void drawPlayer() {
        Graphics g = img.getGraphics();
        g.setColor(Color.red.darker());
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void update() {
        handlejumping();
        handlerunning();

        move();

        checkbounds();
    }

    public void checkbounds() {
        if (posx + width < 0 || posx > GameLoop.l.widthpx || posy + height < 0
                || posy > GameLoop.l.heightpx)
            this.die();
    }

    private void handlejumping() {
        if (space) {
            if (!ground) {
                if (rightwall) {
                    velx = -5;
                    vely = -7;
                }
                if (leftwall) {
                    velx = 5;
                    vely = -7;
                }
            } else {
                vely = -7;
            }
        }
    }

    private void handlerunning() {
        if (right) {
            if (ground) {
                if (velx < MAXGROUNDSPEED) {
                    velx += .5;
                } else {
                    velx -= .5;
                }
            } else {
                if (velx < MAXAIRSPEED) {
                    velx += .5;
                }
            }
        }

        if (left) {
            if (ground) {
                if (velx > -MAXGROUNDSPEED) {
                    velx -= .5;
                } else {
                    velx += .5;
                }
            } else {
                if (velx > -MAXAIRSPEED) {
                    velx -= .5;
                }
            }
        }
    }

    private void move() {
        posx += velx;
        posy += vely;

        // Slide down walls more slowly than
        // through free space
        if ((rightwall || leftwall) && vely >= 0)
            vely += .1;
        else
            vely += .5;

        if (vely > this.img.getHeight() / 2)
            vely = this.img.getHeight() / 2;
    }

    public void startMoveRight() {
        if (!right)
            velx = 0;
        right = true;
        left = false;
    }

    public void endMoveRight() {
        velx = 0;
        right = false;
    }

    public void startMoveLeft() {
        if (!left)
            velx = 0;
        left = true;
        right = false;
    }

    public void endMoveLeft() {
        velx = 0;
        left = false;
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_W:
            up = true;
            down = false;
            break;
        case KeyEvent.VK_D:
            startMoveRight();
            break;
        case KeyEvent.VK_S:
            down = true;
            up = false;
            break;
        case KeyEvent.VK_A:
            startMoveLeft();
            break;
        case KeyEvent.VK_SPACE:
            space = true;
            break;
        case KeyEvent.VK_SHIFT:
            MAXGROUNDSPEED = 7;
            break;
        default:
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_W:
            up = false;
            break;
        case KeyEvent.VK_D:
            endMoveRight();
            break;
        case KeyEvent.VK_S:
            down = false;
            break;
        case KeyEvent.VK_A:
            endMoveLeft();
            break;
        case KeyEvent.VK_SPACE:
            space = false;
            break;
        case KeyEvent.VK_SHIFT:
            MAXGROUNDSPEED = 5;
        default:
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent evt) {}

    @Override
    public ArrayList<Artifact> getArtifacts() {
        ArrayList<Artifact> list = new ArrayList<Artifact>(1);
        list.add(new Artifact((int) posx, (int) posy, img));

        return list;
    }

    @Override
    public String toString() {
        return this.getClass() + " " + img.getWidth() + " " + img.getHeight();
    }

    @Override
    public void onCollide(CollidedSide side, Collider c) {
        if (c.ID.equals("block")) {
            switch (side) {
            case BOTTOM:
                this.posy = c.posy - this.height;
                this.vely = 0;
                this.ground = true;
                break;
            case RIGHT:
                this.posx = c.posx - this.width;
                this.velx = 0;
                this.rightwall = true;
                break;
            case LEFT:
                this.posx = c.posx + c.width;
                this.velx = 0;
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

        if (c.ID.equals("enemy")) {
            switch (side) {
            case BOTTOM:
                ((Enemy) c).die();
                this.vely = -this.vely / 2;
                break;
            case RIGHT:
            case LEFT:
            case TOP:
                this.die();
            }
        }

        if (c.ID.equals("nonplayer")) {
            ((NonPlayer) c).interact();
        }
    }

    @Override
    public void interact() {}

    @Override
    public void die() {
        try {
            System.out.println("YOU DIED");
            Thread.sleep(500);

            // Give it a Mario-eque death sequence.
            int yvel = -10;
            while (posy < GameLoop.camera.posy + GameLoop.SCREENH) {
                long starttime = System.currentTimeMillis();
                this.posy += yvel;
                yvel++;
                GameLoop.camera.update(GameLoop.l.getArtifacts());
                GameLoop.camera.repaint();
                long endtime = System.currentTimeMillis();

                long sleeptime = (1000 / 30) - (endtime - starttime);
                if (sleeptime > 0)
                    Thread.sleep(sleeptime);
            }

            Thread.sleep(500);
            System.exit(0);
        } catch (InterruptedException e) {}
    }
}
