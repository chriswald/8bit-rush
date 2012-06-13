package pkg;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Level implements CameraDrawable {
    public int                      widthpx;
    public int                      heightpx;
    public int                      blockwidthpx;
    public int                      blockheightpx;

    public Map                      map;
    public ArrayList<BufferedImage> backgrounds = new ArrayList<BufferedImage>();

    public Player                   player;

    public Level(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    GameLoop.RESDIR + GameLoop.LVLDIR + filename));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("!")) {
                    String[] toks = line.split(" ");
                    int blockswide = Integer.parseInt(toks[1]);
                    int blockshigh = Integer.parseInt(toks[2]);
                    this.blockwidthpx = Integer.parseInt(toks[3]);
                    this.blockheightpx = Integer.parseInt(toks[4]);
                    widthpx = blockswide * this.blockwidthpx;
                    heightpx = blockswide * this.blockheightpx;
                    map = new Map(blockswide, blockshigh, this.blockwidthpx,
                            this.blockheightpx);
                } else if (line.startsWith("@")) {
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    String c = toks[4];

                    BufferedImage tmp = ImageIO.read(new File(GameLoop.RESDIR
                            + GameLoop.IMGDIR + imagefilename));
                    map.add(new Block(tmp, x, y, c));
                } else if (line.startsWith("#")) {
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    this.player = new Player(imagefilename);
                    this.player.posx = x * this.blockwidthpx;
                    this.player.posy = y * this.blockheightpx;
                    GameLoop.camera.addKeyListener(this.player);
                } else if (line.startsWith("$")) {
                    String[] toks = line.split(" ");
                    try {
                        backgrounds.add(ImageIO.read(new File(GameLoop.RESDIR
                                + GameLoop.IMGDIR + toks[1])));
                    } catch (IOException e) {
                        System.err.println("Cannot load the background image "
                                + toks[1]);
                        System.err.println(e.getLocalizedMessage());
                        JOptionPane.showMessageDialog(null,
                                "Cannot load the background image " + toks[1]);
                    }
                } else if (line.startsWith("//")) {
                    // Ignore lines that start with double slash
                    // These will be comments in the level files
                }
            }

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, "Could not load level file: "
                    + filename + "\nReinstalling the game may fix this.");
        }
    }

    public void update() {
        this.player.update();
    }

    public void checkCollide() {
        this.player.ground = false;
        this.player.rightwall = false;
        this.player.leftwall = false;

        for (int i = 0; i < map.w; i++) {
            for (int j = 0; j < map.h; j++) {
                if (map.map[i][j] != null)
                    this.player.checkCollide(map.map[i][j]);
            }
        }
    }

    public Point onBlock() {
        return new Point((int) this.player.posx / this.blockwidthpx,
                (int) this.player.posy / this.blockheightpx);
    }

    public Block blockAt(int x, int y) {
        return map.get(x / this.blockwidthpx, y / this.blockheightpx);
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {
        ArrayList<Artifact> tmp = new ArrayList<Artifact>();

        for (BufferedImage b : backgrounds) {
            tmp.add(new Artifact(0, 0, b));
        }

        tmp.addAll(map.getArtifacts());
        tmp.addAll(player.getArtifacts());

        return tmp;
    }

    @Override
    public String toString() {
        return this.getClass() + " " + widthpx + " " + heightpx;
    }
}
