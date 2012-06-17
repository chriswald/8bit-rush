package pkg;

import java.awt.Toolkit;

class GameLoop {
    public static String RESDIR  = "res/";
    public static String IMGDIR  = "img/";
    public static String SNDDIR  = "snd/";
    public static String LVLDIR  = "lvl/";

    public static Camera camera;
    public static Level  l;

    public static int    SCREENW = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int    SCREENH = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static void main(String[] args) {
        SCREENW = Toolkit.getDefaultToolkit().getScreenSize().width;
        SCREENH = Toolkit.getDefaultToolkit().getScreenSize().height;

        camera = new Camera();
        l = new Level("lvl1.txt");
        camera.setLevel(l);

        camera.setVisible(true);

        while (true) {
            long starttime = System.currentTimeMillis();
            l.update();
            l.checkCollide();

            camera.setPostition(l.player);
            camera.update(l.getArtifacts());
            camera.repaint();
            long endtime = System.currentTimeMillis();

            try {
                long sleeptime = (1000 / 30) - (endtime - starttime);
                if (sleeptime > 0)
                    Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}
