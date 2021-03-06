import java.awt.Toolkit;

class G {
    public static String      RESDIR  = "res/";
    public static String      IMGDIR  = "img/";
    public static String      SNDDIR  = "snd/";
    public static String      LVLDIR  = "lvl/";
    public static String      DLGDIR  = "dlg/";
    public static String      ACTDIR  = "act/";

    public static Camera      camera;
    public static Level       l;
    public static StartScreen ss;

    public static int         SCREENW = Toolkit.getDefaultToolkit()
                                              .getScreenSize().width;
    public static int         SCREENH = Toolkit.getDefaultToolkit()
                                              .getScreenSize().height;
    public static int         WINDOWW = 800;
    public static int         WINDOWH = 600;

    public static boolean     done    = false;

    public enum State {
        STARTUP, METAMAP, SELECT, PLAY, PAUSE, DEATH, CREDITS, HOWTO, END, RESET
    };

    public static State GAMESTATE = State.STARTUP;

    public static void rest(long starttime, long endtime) {
        // To make sure updates are consistent subtract the time it took
        // for the code to run from 1/30th of a second.
        G.rest((1000 / 30) - (endtime - starttime));
    }

    public static void rest(long millis) {
        try {
            if (millis > 0)
                Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
