import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
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

        this.setSize(new Dimension(G.WINDOWW, G.WINDOWH));
        this.setLocation(new Point(G.SCREENW / 2 - G.WINDOWW / 2, G.SCREENH / 2
                - G.WINDOWH / 2));
        this.setResizable(false);
        this.setTitle("8Bit-Rush");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
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

    public void update(ArrayList<Artifact> artifacts) {
        Graphics gtod = todraw.getGraphics();
        for (Artifact a : artifacts) {
            gtod.drawImage(a.img, a.x, a.y, null);
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(todraw, 0, 0, G.WINDOWW, G.WINDOWH, posx, posy, posx
                + G.WINDOWW, posy + G.WINDOWH, null);
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            G.GAMESTATE = G.State.END;
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
