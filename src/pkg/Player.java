package pkg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Player extends Collider implements CameraDrawable, KeyListener {
    public boolean          up             = false;
    public boolean          right          = false;
    public boolean          down           = false;
    public boolean          left           = false;
    public boolean          space          = false;

    public boolean          rightwall      = false;
    public boolean          leftwall       = false;
    public boolean          ground         = false;
    public boolean          ceiling        = false;

    public BufferedImage    img;

    public static final int MAXGROUNDSPEED = 5;
    public static final int MAXAIRSPEED    = 4;

    public String           imgfile        = "player.jpg";

    public Player(String filename) {
        imgfile = filename;
        rightside = leftside = topside = bottomside = true;
        loadImage();
    }

    private void loadImage() {
        String filename = GameLoop.RESDIR + GameLoop.IMGDIR + imgfile;
        BufferedImage tmp = new BufferedImage(10, 20,
                BufferedImage.TYPE_INT_ARGB);
        try {
            tmp = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.println("Cannot load the image file for the player.\n"
                    + filename + "\n" + e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null,
                    "Cannot load the image file for the player.\n" + filename);
            drawPlayer(tmp);
        }

        img = tmp;
        width = img.getWidth();
        height = img.getHeight();
    }

    private void drawPlayer(BufferedImage img) {
        Graphics g = img.getGraphics();
        g.setColor(Color.red.darker());
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public void update() {
        handlejumping();
        handlerunning();

        move();
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
}
