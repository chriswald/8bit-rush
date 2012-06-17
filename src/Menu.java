import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Menu {
    public BufferedImage img;
    public int           posx, posy;

    public Menu() {
        img = new BufferedImage(50, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public void setLocation(int x, int y) {
        posx = x;
        posy = y;
    }
}
