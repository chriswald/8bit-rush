package pkg;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

class Level implements CameraDrawable {
    public int                      widthpx;
    public int                      heightpx;
    public int                      blockwidthpx;
    public int                      blockheightpx;

    public Map                      map;
    public ArrayList<BufferedImage> backgrounds = new ArrayList<BufferedImage>();
    public BufferedImage            genbackground;

    public Player                   player;

    public ArrayList<Enemy>         enemies     = new ArrayList<Enemy>();

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
                    heightpx = blockshigh * this.blockheightpx;
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
                        this.generateBackground();
                    } catch (IOException e) {
                        System.err.println("Cannot load the background image "
                                + toks[1]);
                        System.err.println(e.getLocalizedMessage());
                        JOptionPane.showMessageDialog(null,
                                "Cannot load the background image " + toks[1]);
                    }
                } else if (line.startsWith("%")) {
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    Enemy enemy = new Enemy(imagefilename);
                    enemy.posx = x * this.blockwidthpx;
                    enemy.posy = y * this.blockheightpx;
                    enemy.velx = Integer.parseInt(toks[4]);
                    enemy.vely = Integer.parseInt(toks[5]);
                    this.enemies.add(enemy);
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

    public void generateBackground() {
        genbackground = new BufferedImage(this.widthpx, this.heightpx,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = genbackground.getGraphics();

        for (BufferedImage b : backgrounds) {
            int x = 0;
            do {
                g.drawImage(b, x, 0, null);
                x += b.getWidth();
            } while (x < widthpx);
        }
    }

    public void update() {
        updatePlayer();
        updateEnemies();
    }

    public void updatePlayer() {
        this.player.update();
    }

    public void updateEnemies() {
        for (Enemy e : enemies)
            e.update();
    }

    public void checkCollide() {
        checkPlayerCollide();
        checkEnemyCollide();
    }

    public void checkPlayerCollide() {
        this.player.ground = false;
        this.player.rightwall = false;
        this.player.leftwall = false;
        this.player.ceiling = false;

        for (Block b : map.map) {
            this.player.checkCollide(b);
        }
    }

    public void checkEnemyCollide() {
        for (Enemy e : enemies) {
            e.ground = false;
            e.rightwall = false;
            e.leftwall = false;
            e.ceiling = false;

            for (Block b : map.map) {
                e.checkCollide(b);
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
        ArrayList<Artifact> tmp = new ArrayList<Artifact>(3);

        tmp.add(new Artifact(0, 0, genbackground));
        tmp.addAll(map.getArtifacts());
        for (Enemy e : enemies)
            tmp.addAll(e.getArtifacts());
        tmp.addAll(player.getArtifacts());

        return tmp;
    }

    @Override
    public String toString() {
        return this.getClass() + " " + widthpx + " " + heightpx;
    }
}
