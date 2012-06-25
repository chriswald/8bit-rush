import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

class StartScreen implements CameraDrawable, KeyListener {
    public int           posx, posy;
    public Artifact      back;
    public BufferedImage fore;

    public StartScreen() {
        try {
            BufferedImage bg = ImageIO.read(new File(G.RESDIR + G.IMGDIR
                    + "background1.png"));
            BufferedImage fg = ImageIO.read(new File(G.RESDIR + G.IMGDIR
                    + "startscreen.png"));
            BufferedImage back = new BufferedImage(G.WINDOWW, G.WINDOWH,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = back.getGraphics();
            g.drawImage(bg, 0, 0, null);
            g.drawImage(fg, 0, 0, null);
            this.back = new Artifact(0, 0, back);
            fore = ImageIO
                    .read(new File(G.RESDIR + G.IMGDIR + "forscreen.png"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            G.GAMESTATE = G.State.PLAY;
        }

        posx = G.WINDOWW / 2 - fore.getWidth() / 2;
        posy = G.WINDOWH;
    }

    public void update() {
        if (posy > G.WINDOWH / 2 - fore.getHeight() / 2) {
            posy -= 5;
        }
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {
        ArrayList<Artifact> tmp = new ArrayList<Artifact>(2);
        tmp.add(back);
        tmp.add(new Artifact(posx, posy, fore));
        return tmp;
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_SPACE:
            G.GAMESTATE = G.State.PLAY;
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {}

    @Override
    public void keyTyped(KeyEvent evt) {}

}
