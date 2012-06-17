import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

class Level implements CameraDrawable {
    public int                  widthpx;
    public int                  heightpx;
    public int                  blockwidthpx;
    public int                  blockheightpx;

    public Map                  map;
    public BufferedImage        genbackground;

    public Player               player;

    public ArrayList<NonPlayer> npcgo       = new ArrayList<NonPlayer>();
    public ArrayList<NonPlayer> npcwait     = new ArrayList<NonPlayer>();

    public ArrayList<Enemy>     enemiesgo   = new ArrayList<Enemy>();
    public ArrayList<Enemy>     enemieswait = new ArrayList<Enemy>();

    public Level(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    GameLoop.RESDIR + GameLoop.LVLDIR + filename));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("!")) { // Header info
                    String[] toks = line.split(" ");
                    int blockswide = Integer.parseInt(toks[1]);
                    int blockshigh = Integer.parseInt(toks[2]);
                    this.blockwidthpx = Integer.parseInt(toks[3]);
                    this.blockheightpx = Integer.parseInt(toks[4]);
                    widthpx = blockswide * this.blockwidthpx;
                    heightpx = blockshigh * this.blockheightpx;
                    map = new Map(blockswide, blockshigh, this.blockwidthpx,
                            this.blockheightpx);
                } else if (line.startsWith("@")) { // Blocks
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    String c = toks[4];

                    BufferedImage tmp = ImageIO.read(new File(GameLoop.RESDIR
                            + GameLoop.IMGDIR + imagefilename));
                    map.add(new Block(tmp, x, y, c));
                } else if (line.startsWith("#")) { // Player info
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    this.player = new Player(imagefilename);
                    this.player.posx = x * this.blockwidthpx;
                    this.player.posy = y * this.blockheightpx;
                    GameLoop.camera.addKeyListener(this.player);
                } else if (line.startsWith("$")) { // Background images
                    String[] toks = line.split(" ");
                    try {
                        if (genbackground == null)
                            genbackground = new BufferedImage(this.widthpx,
                                    this.heightpx, BufferedImage.TYPE_INT_ARGB);
                        this.generateBackground(
                                ImageIO.read(new File(GameLoop.RESDIR
                                        + GameLoop.IMGDIR + toks[1])),
                                Integer.parseInt(toks[2]),
                                Integer.parseInt(toks[3]));
                    } catch (IOException e) {
                        System.err.println("Cannot load the background image "
                                + toks[1]);
                        System.err.println(e.getLocalizedMessage());
                        JOptionPane.showMessageDialog(null,
                                "Cannot load the background image " + toks[1]);
                    }
                } else if (line.startsWith("%")) { // Enemies
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    Enemy enemy = new Enemy(imagefilename);
                    enemy.posx = x * this.blockwidthpx;
                    enemy.posy = y * this.blockheightpx;
                    enemy.velx = Integer.parseInt(toks[4]);
                    enemy.vely = Integer.parseInt(toks[5]);
                    this.enemieswait.add(enemy);
                } else if (line.startsWith("&")) {
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    NonPlayer npc = new NonPlayer(imagefilename);
                    npc.posx = x * this.blockwidthpx;
                    npc.posy = y * this.blockheightpx;
                    npc.velx = Integer.parseInt(toks[4]);
                    npc.vely = Integer.parseInt(toks[5]);
                    this.npcwait.add(npc);
                } else if (line.startsWith("//")) {
                    // Ignore lines that start with double slash
                    // These will be comments in the level files
                } else {
                    System.err.println("Warning: Unrecognized line - " + line);
                }
            }

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, "Could not load level file: "
                    + filename + "\nReinstalling the game may fix this.");
        }
    }

    public void generateBackground(BufferedImage img, int x, int y) {
        Graphics g = genbackground.getGraphics();

        do {
            g.drawImage(img, x, y, null);
            x += img.getWidth();
        } while (x < widthpx);
    }

    public void update() {
        updatePlayer();
        updateEnemies();
        updateNPCs();
    }

    public void updatePlayer() {
        this.player.update();
    }

    public void updateEnemies() {
        for (Enemy e : enemieswait) {
            if (e.onscreen())
                if (enemiesgo.indexOf(e) == -1)
                    enemiesgo.add(e);
        }

        for (Enemy e : enemiesgo)
            e.update();

        try {
            for (Enemy e : enemiesgo)
                if (!e.alive) {
                    this.enemiesgo.remove(e);
                    this.enemieswait.remove(e);
                }
        } catch (ConcurrentModificationException ex) {}
    }

    public void updateNPCs() {
        for (NonPlayer n : npcwait) {
            if (n.onscreen())
                if (npcgo.indexOf(n) == -1)
                    npcgo.add(n);
        }

        for (NonPlayer n : npcgo)
            n.update();

        try {
            for (NonPlayer n : npcgo)
                if (!n.alive) {
                    this.npcgo.remove(n);
                    this.npcwait.remove(n);
                }
        } catch (ConcurrentModificationException ex) {}
    }

    public void checkCollide() {
        checkPlayerCollide();
        checkEnemyCollide();
        checkNPCCollide();
        checkPlayerEnemyCollide();
        checkPlayerNPCCollide();
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
        for (Enemy e : enemiesgo) {
            e.ground = false;
            e.rightwall = false;
            e.leftwall = false;
            e.ceiling = false;

            for (Block b : map.map) {
                e.checkCollide(b);
            }
        }
    }

    public void checkNPCCollide() {
        for (NonPlayer n : npcgo) {
            n.ground = false;
            n.rightwall = false;
            n.leftwall = false;
            n.ceiling = false;

            for (Block b : map.map) {
                n.checkCollide(b);
            }
        }
    }

    public void checkPlayerEnemyCollide() {
        for (Enemy e : enemiesgo) {
            this.player.checkCollide(e);
        }
    }

    public void checkPlayerNPCCollide() {
        for (NonPlayer n : npcgo) {
            this.player.checkCollide(n);
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
        ArrayList<Artifact> tmp = new ArrayList<Artifact>(3 + enemiesgo.size()
                + npcgo.size());

        tmp.add(new Artifact(0, 0, genbackground));
        tmp.addAll(map.getArtifacts());
        for (Enemy e : enemiesgo)
            tmp.addAll(e.getArtifacts());
        for (NonPlayer n : npcgo)
            tmp.addAll(n.getArtifacts());
        tmp.addAll(player.getArtifacts());

        return tmp;
    }

    @Override
    public String toString() {
        return this.getClass() + " " + widthpx + " " + heightpx;
    }
}
