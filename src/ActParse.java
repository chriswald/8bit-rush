import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ActParse {
    public static void run(String filename, Actor a) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(G.RESDIR + G.ACTDIR
                    + filename + (filename.endsWith(".act") ? "" : ".act")));

            String line = "";
            while ((line = in.readLine()) != null) {
                parseCommand(line, a);
            }

            in.close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return;
        }
    }

    public static void parseCommand(String command, Actor a) {
        command = command.toUpperCase();

        if (command.equals("JUMP"))
            jump(a);
        if (command.equals("TURN"))
            turn(a);

        if (command.startsWith("SET"))
            set(command, a);
    }

    public static void jump(Actor a) {
        if (!a.ground) {
            if (a.rightwall) {
                a.velx = -5;
                a.vely = -7;
            }
            if (a.leftwall) {
                a.velx = 5;
                a.vely = -7;
            }
        } else {
            a.vely = -7;
        }
    }

    public static void turn(Actor a) {
        a.initvelx = -a.initvelx;
    }

    public static void set(String command, Actor a) {
        command = command.substring("SET".length()).trim();

        if (command.startsWith("NONPLAYER")) {
            command = command.substring("NONPLAYER.".length());
            if (command.startsWith("INITVELX")) {
                ((NonPlayer) a).initvelx = Integer.parseInt(command.substring(
                        "INITVELX".length()).trim());
                System.out.println(((NonPlayer) a).initvelx);
            }
        }
    }
}
