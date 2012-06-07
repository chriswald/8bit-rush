package pkg;

class GameLoop {
    public static String RESDIR = "res/";
    public static String IMGDIR = "img/";
    public static String SNDDIR = "snd/";
    public static String LVLDIR = "lvl/";

    public static Camera camera = new Camera();

    public static void main(String[] args) {
        Level l = new Level("lvl1.txt");
        Player p = new Player();

        l.addPlayer(p);

        camera.addKeyListener(p);
        camera.setVisible(true);

        while (true) {
            l.checkCollide(p);
            p.update();

            camera.update(l.getArtifacts());
            camera.repaint();

            try {
                Thread.sleep(1000 / 30);
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}
