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

    public enum State {
        STARTUP, METAMAP, SELECT, PLAY, PAUSE, DEATH, CREDITS, HOWTO, END, RESET
    };

    public static State GAMESTATE = State.STARTUP;
}
