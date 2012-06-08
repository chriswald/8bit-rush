package pkg;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

class Camera extends JFrame implements KeyListener {
    private static final long        serialVersionUID = 1L;

    public BufferedImage             todraw;
    public ArrayList<CameraDrawable> drawables;
    public int                       posx;
    public int                       posy;

    public Camera() {
        int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
        int sh = Toolkit.getDefaultToolkit().getScreenSize().height;

        todraw = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_ARGB);
        drawables = new ArrayList<CameraDrawable>(0);
        posx = 0;
        posy = 0;

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.addKeyListener(this);
    }

    public void update(ArrayList<Artifact> artifacts) {
        Graphics gtod = todraw.getGraphics();
        for (Artifact a : artifacts) {
            gtod.drawImage(a.img, a.x, a.y, null);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(todraw, 0, 0, null);
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
