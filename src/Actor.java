import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

abstract class Actor extends Collider implements Interactable, CameraDrawable {
    public BufferedImage img;
    public String        imgfile = "";

    public boolean       alive   = true;
    public boolean       rightwall, leftwall, ground, ceiling;

    public Actor(String filename) {
        imgfile = filename;
        ID = "actor";

        filename = GameLoop.RESDIR + GameLoop.IMGDIR + imgfile;
        img = new BufferedImage(10, 20, BufferedImage.TYPE_INT_ARGB);
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.println("Cannot load the image file for the player.\n"
                    + filename + "\n" + e.getLocalizedMessage());
            drawPlayer();
        }

        width = img.getWidth();
        height = img.getHeight();
    }

    public boolean onscreen() {
        return (this.posx < GameLoop.camera.posx + GameLoop.SCREENW
                && this.posx + this.width > GameLoop.camera.posx
                && this.posy < GameLoop.camera.posy + GameLoop.SCREENH && this.posy
                + this.height > GameLoop.camera.posy);
    }

    public abstract void drawPlayer();

    public abstract void die();

    public abstract void update();
}