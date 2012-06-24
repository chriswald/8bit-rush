import java.awt.Toolkit;

class GameLoop {

    public static void main(String[] args) {
        G.SCREENW = Toolkit.getDefaultToolkit().getScreenSize().width;
        G.SCREENH = Toolkit.getDefaultToolkit().getScreenSize().height;

        G.camera = new Camera();
        G.l = new Level("lvl1.lvl");
        G.camera.setLevel(G.l);

        G.camera.setVisible(true);

        while (true) {
            long starttime = System.currentTimeMillis();
            G.l.update();
            G.l.checkCollide();

            G.camera.setPostition(G.l.player);
            G.camera.update(G.l.getArtifacts());
            G.camera.repaint();
            long endtime = System.currentTimeMillis();

            try {
                // To make sure updates are consistent subtract the time it took
                // for the code to run from 1/30th of a second.
                long sleeptime = (1000 / 30) - (endtime - starttime);
                if (sleeptime > 0)
                    Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}
