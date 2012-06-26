import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

abstract class Actor extends Collider implements Interactable, CameraDrawable {
    public BufferedImage img;
    public String        file         = "";

    public boolean       alive        = true;
    public boolean       rightwall, leftwall, ground, ceiling;

    public double        initvelx, initvely;

    public static String IMGEXTENSION = ".png";

    public Actor(String filename) {
        file = filename;
        ID = "actor";

        filename = G.RESDIR + G.IMGDIR + file;
        img = new BufferedImage(10, 20, BufferedImage.TYPE_INT_ARGB);
        try {
            img = ImageIO.read(new File(filename + IMGEXTENSION));
        } catch (IOException e) {
            System.err.println("Cannot load " + filename + ".png\n"
                    + e.getLocalizedMessage());
            drawPlayer();
        }

        width = img.getWidth();
        height = img.getHeight();
    }

    public boolean nearscreen() {
        return (this.posx < G.camera.posx + G.WINDOWW + 50
                && this.posx + this.width > G.camera.posx - 50
                && this.posy < G.camera.posy + G.WINDOWH + 50 && this.posy
                + this.height > G.camera.posy - 50);
    }

    public abstract void drawPlayer();

    public abstract void die();

    public abstract void update();
}
