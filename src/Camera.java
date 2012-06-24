import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

class Camera extends JFrame implements KeyListener {
    private static final long serialVersionUID = 1L;

    public BufferedImage      todraw;
    public int                posx;
    public int                posy;

    public Camera() {
        posx = 0;
        posy = 0;

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.addKeyListener(this);
    }

    public void setPostition(Player p) {
        posx = p.getMid().x - (G.SCREENW / 2);
        posy = p.getMid().y - (3 * G.SCREENH / 4);

        if (posx < 0)
            posx = 0;
        if (posx + G.SCREENW > todraw.getWidth())
            posx = todraw.getWidth() - G.SCREENW;

        if (posy < 0)
            posy = 0;
        if (posy + G.SCREENH > todraw.getHeight())
            posy = todraw.getHeight() - G.SCREENH;
    }

    public void setLevel(Level l) {
        todraw = new BufferedImage(l.widthpx, l.heightpx,
                BufferedImage.TYPE_INT_ARGB);
    }

    public void update(ArrayList<Artifact> artifacts) {
        Graphics gtod = todraw.getGraphics();
        for (Artifact a : artifacts) {
            gtod.drawImage(a.img, a.x, a.y, null);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(todraw, 0, 0, G.SCREENW, G.SCREENH, posx, posy, posx
                + G.SCREENW, posy + G.SCREENH, null);
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            System.exit(0);
            break;
        default:
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}
}
