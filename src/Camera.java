import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

class Camera extends JFrame implements KeyListener, WindowListener {
    private static final long serialVersionUID = 1L;

    public BufferedImage      todraw;
    public int                posx;
    public int                posy;

    public Camera() {
        posx = 0;
        posy = 0;

        this.setSize(new Dimension(G.WINDOWW, G.WINDOWH));
        this.setLocation(new Point(G.SCREENW / 2 - G.WINDOWW / 2, G.SCREENH / 2
                - G.WINDOWH / 2));
        this.setResizable(false);
        this.setTitle("8Bit-Rush");
        this.addKeyListener(this);
        this.addWindowListener(this);
    }

    public void setPostition(Player p) {
        int fudgefactor = 30;

        if (p.posx - this.posx < G.WINDOWW / 2 - fudgefactor && p.velx < 0)
            this.posx = (int) (p.posx + fudgefactor - (G.WINDOWW / 2));
        if (p.posx - this.posx > G.WINDOWW / 2 + fudgefactor && p.velx > 0)
            this.posx = (int) (p.posx - fudgefactor - (G.WINDOWW / 2));

        if (p.posy - this.posy < 2 * G.WINDOWH / 3 - fudgefactor && p.vely < 0)
            this.posy = (int) (p.posy + fudgefactor - (2 * G.WINDOWH / 3));
        if (p.posy - this.posy > 2 * G.WINDOWH / 3 + fudgefactor && p.vely > 0)
            this.posy = (int) (p.posy - fudgefactor - (2 * G.WINDOWH / 3));

        if (posx < 0)
            posx = 0;
        if (posx + G.WINDOWW > todraw.getWidth())
            posx = todraw.getWidth() - G.WINDOWW;

        if (posy < 0)
            posy = 0;
        if (posy + G.WINDOWH > todraw.getHeight())
            posy = todraw.getHeight() - G.WINDOWH;
    }

    public void setLevel(Level l) {
        setBufferSize(l.widthpx, l.heightpx);
    }

    public void setBufferSize(int w, int h) {
        posx = posy = 0;
        todraw = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public void update(ArrayList<CameraDrawable> drawables) {
        Graphics gtod = todraw.getGraphics();
        for (CameraDrawable d : drawables)
            for (Artifact a : d.getArtifacts()) {
                gtod.drawImage(a.img, a.x, a.y, null);
            }
        this.repaint();
    }

    public void update(CameraDrawable drawable) {
        ArrayList<CameraDrawable> tmp = new ArrayList<CameraDrawable>(1);
        tmp.add(drawable);
        this.update(tmp);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(todraw, 0, 0, G.WINDOWW, G.WINDOWH, posx, posy, posx
                + G.WINDOWW, posy + G.WINDOWH, null);
    }

    @Override
    public void removeKeyListener(KeyListener l) {
        if (l != this)
            super.removeKeyListener(l);
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            G.GAMESTATE = G.State.END;
            break;
        case KeyEvent.VK_P:
        case KeyEvent.VK_PAUSE:
            if (G.GAMESTATE == G.State.PLAY)
                G.GAMESTATE = G.State.PAUSE;
            else if (G.GAMESTATE == G.State.PAUSE)
                G.GAMESTATE = G.State.PLAY;
        default:
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}

    @Override
    public void windowActivated(WindowEvent arg0) {
        if (G.GAMESTATE == G.State.PAUSE)
            G.GAMESTATE = G.State.PLAY;
    }

    @Override
    public void windowClosed(WindowEvent arg0) {}

    @Override
    public void windowClosing(WindowEvent arg0) {
        G.GAMESTATE = G.State.END;
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        if (G.GAMESTATE == G.State.PLAY)
            G.GAMESTATE = G.State.PAUSE;
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {}

    @Override
    public void windowIconified(WindowEvent arg0) {}

    @Override
    public void windowOpened(WindowEvent arg0) {}
}
