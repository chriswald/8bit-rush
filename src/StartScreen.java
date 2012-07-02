import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;

class StartScreen implements CameraDrawable, KeyListener {
    public int           forx, fory;
    public int           backx, backy;
    public int           arrowx, arrowy;
    public int           STEP     = 50;
    public BufferedImage back;
    public BufferedImage arrow;
    public BufferedImage fore;
    public boolean       anim_done;
    public int           selected = 0;

    public StartScreen() {
        makeBackground();
        makeForground();
        forx = G.WINDOWW / 2 - fore.getWidth() / 2;
        fory = G.WINDOWH;

        backx = 0;
        backy = 0;

        arrowx = G.WINDOWW / 4 + 22;
        arrowy = fory + G.WINDOWH / 4;
    }

    public void makeBackground() {
        try {
            BufferedImage bg = ImageIO.read(new File(G.RESDIR + G.IMGDIR
                    + "sky.png"));
            BufferedImage fg = ImageIO.read(new File(G.RESDIR + G.IMGDIR
                    + "startscreen.png"));
            BufferedImage back = new BufferedImage(G.WINDOWW, G.WINDOWH,
                    BufferedImage.TYPE_INT_ARGB);
            arrow = ImageIO.read(new File(G.RESDIR + G.IMGDIR + "arrow.png"));

            Calendar cal = Calendar.getInstance();
            double seconds = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60
                    + cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
            // seconds = 19 * 60 * 60; //Used only for testing purposes.
            // Represents 7:00 PM
            double secsinday = 24 * 60 * 60;
            int scalar = (int) (seconds / secsinday * bg.getWidth());

            Graphics2D g = (Graphics2D) back.getGraphics();
            Color sky = new Color(bg.getRGB(scalar, 0));
            g.setColor(sky);
            g.fillRect(0, 0, back.getWidth(), back.getHeight());
            g.drawImage(fg, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    .30f));
            g.fillRect(0, 0, back.getWidth(), back.getHeight());
            this.back = back;
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
        if (fory > G.WINDOWH / 2 - fore.getHeight() / 2) {
            fory -= 5;
            arrowy = (fory + G.WINDOWH / 4) + (selected * STEP);
        }
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {
        this.update();

        ArrayList<Artifact> tmp = new ArrayList<Artifact>(3);
        tmp.add(new Artifact(0, 0, back));
        tmp.add(new Artifact(forx, fory, fore));
        tmp.add(new Artifact(arrowx, arrowy, arrow));
        return tmp;
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (G.GAMESTATE == G.State.STARTUP) {
            switch (evt.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT:
                if (selected == 0)
                    G.GAMESTATE = G.State.SELECT;
                else if (selected == 1)
                    G.GAMESTATE = G.State.HOWTO;
                else if (selected == 2)
                    G.GAMESTATE = G.State.CREDITS;
                selected = 0;
                break;
            case KeyEvent.VK_K:
            case KeyEvent.VK_DOWN:
                selected++;
                if (selected > 2)
                    selected = 0;
                arrowy = (fory + G.WINDOWH / 4) + (selected * STEP);
                break;
            case KeyEvent.VK_I:
            case KeyEvent.VK_UP:
                selected--;
                if (selected < 0)
                    selected = 2;
                arrowy = (fory + G.WINDOWH / 4) + (selected * STEP);
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {}

    @Override
    public void keyTyped(KeyEvent evt) {}

}
