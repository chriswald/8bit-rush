package pkg;

class GameLoop {
    public static String RESDIR = "res/";
    public static String IMGDIR = "img/";
    public static String SNDDIR = "snd/";
    public static String LVLDIR = "lvl/";

    public static Camera camera = new Camera();

    public static void main(String[] args) {
        Level l = new Level("lvl1.txt");

        camera.setVisible(true);

        while (true) {
            l.update();
            l.checkCollide();

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
