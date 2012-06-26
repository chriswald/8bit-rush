import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

class StartScreen implements CameraDrawable, KeyListener {
    public int           posx, posy;
    public Artifact      back;
    public BufferedImage fore;

    public StartScreen() {
        makeBackground();
        makeForground();
        posx = G.WINDOWW / 2 - fore.getWidth() / 2;
        posy = G.WINDOWH;
    }

    public void makeBackground() {
        try {
            BufferedImage bg = ImageIO.read(new File(G.RESDIR + G.IMGDIR
                    + "sky.png"));
            BufferedImage fg = ImageIO.read(new File(G.RESDIR + G.IMGDIR
                    + "startscreen.png"));
            BufferedImage back = new BufferedImage(G.WINDOWW, G.WINDOWH,
                    BufferedImage.TYPE_INT_ARGB);

            Calendar cal = Calendar.getInstance();
            double seconds = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60
                    + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
            // double seconds = 22 * 60 * 60;
            double secsinday = 24 * 60 * 60;
            int scalar = (int) (seconds / secsinday * bg.getWidth());

            Graphics g = back.getGraphics();
            Color sky = new Color(bg.getRGB(scalar, 0));
            // Color haze = new Color(bg.getRGB(scalar, 1));
            g.setColor(sky);
            g.fillRect(0, 0, back.getWidth(), back.getHeight());
            g.drawImage(fg, 0, 0, null);
            // g.setColor(haze);
            // g.fillRect(0, 0, back.getWidth(), back.getHeight());
            this.back = new Artifact(0, 0, back);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            G.GAMESTATE = G.State.PLAY;
        }
    }

    public void makeForground() {
        try {
            fore = ImageIO
                    .read(new File(G.RESDIR + G.IMGDIR + "forscreen.png"));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            G.GAMESTATE = G.State.PLAY;
        }
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
