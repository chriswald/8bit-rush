import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

class Level implements CameraDrawable, MouseListener {
    public int              widthpx;
    public int              heightpx;
    public int              blockwidthpx;
    public int              blockheightpx;

    public Map              map;
    public BufferedImage    genbackground;

    public Player           player;

    public ArrayList<Actor> actorgo   = new ArrayList<Actor>();
    public ArrayList<Actor> actorwait = new ArrayList<Actor>();

    public Level(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(G.RESDIR
                    + G.LVLDIR + filename));
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

                    BufferedImage tmp = ImageIO.read(new File(G.RESDIR
                            + G.IMGDIR + imagefilename));
                    map.add(new Block(tmp, x, y, c));
                } else if (line.startsWith("#")) { // Player info
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    int x = Integer.parseInt(toks[2]);
                    int y = Integer.parseInt(toks[3]);
                    this.player = new Player(imagefilename);
                    this.player.posx = x * this.blockwidthpx;
                    this.player.posy = y * this.blockheightpx;
                    G.camera.addKeyListener(this.player);
                } else if (line.startsWith("$")) { // Background images
                    String[] toks = line.split(" ");
                    try {
                        if (genbackground == null)
                            genbackground = new BufferedImage(this.widthpx,
                                    this.heightpx, BufferedImage.TYPE_INT_ARGB);
                        this.generateBackground(
                                ImageIO.read(new File(G.RESDIR + G.IMGDIR
                                        + toks[1])), Integer.parseInt(toks[2]),
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
                    enemy.velx = Double.parseDouble(toks[4]);
                    enemy.vely = Double.parseDouble(toks[5]);
                    this.actorwait.add(enemy);
                } else if (line.startsWith("&")) { // NPC's
                    String[] toks = line.split(" ");
                    String imagefilename = toks[1];
                    double x = Double.parseDouble(toks[2]);
                    double y = Double.parseDouble(toks[3]);
                    NonPlayer npc = new NonPlayer(imagefilename);
                    npc.posx = x * this.blockwidthpx;
                    npc.posy = y * this.blockheightpx;
                    npc.initvelx = Double.parseDouble(toks[4]);
                    npc.initvely = Double.parseDouble(toks[5]);
                    npc.velx = npc.initvelx;
                    npc.vely = npc.initvely;
                    this.actorwait.add(npc);
                    G.camera.addKeyListener(npc);
                } else if (line.startsWith("//")) {
                    // Ignore lines that start with double slash
                    // These will be comments in the level files
                } else {
                    System.err.println("Warning: Unrecognized line - " + line);
                }
            }
            in.close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, "Could not load level file: "
                    + filename + "\nReinstalling the game may fix this.");
            System.exit(1);
        }
    }

    public void generateBackground(BufferedImage img, int x, int y) {
        Graphics g = genbackground.getGraphics();
        int initx = x;

        do {
            do {
                g.drawImage(img, x, y, null);
                x += img.getWidth();
            } while (x < widthpx);
            y += img.getHeight();
            x = initx;
        } while (y < heightpx);
    }

    public void update() {
        updatePlayer();
        updateActors();
    }

    public void updatePlayer() {
        this.player.update();
    }

    public void updateActors() {
        for (Actor a : actorwait) {
            if (a.nearscreen())
                if (actorgo.indexOf(a) == -1)
                    actorgo.add(a);
        }

        for (Actor a : actorgo)
            a.update();

        try {
            for (Actor a : actorgo) {
                if (!a.alive) {
                    actorgo.remove(a);
                    actorwait.remove(a);
                }
            }
        } catch (ConcurrentModificationException e) {}
    }

    public void checkCollide() {
        checkPlayerCollide();
        checkActorCollide();
        checkPlayerActorCollide();
        checkActorActorCollide();
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

    public void checkActorCollide() {
        for (Actor a : actorgo) {
            a.ground = false;
            a.rightwall = false;
            a.leftwall = false;
            a.ceiling = false;

            for (Block b : map.map) {
                a.checkCollide(b);
            }
        }
    }

    public void checkPlayerActorCollide() {
        for (Actor a : actorgo)
            this.player.checkCollide(a);
    }

    public void checkActorActorCollide() {
        for (Actor now : actorgo) {
            for (Actor a : actorgo) {
                if (now != a)
                    now.checkCollide(a);
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
        ArrayList<Artifact> tmp = new ArrayList<Artifact>(3 + actorgo.size());

        tmp.add(new Artifact(0, 0, genbackground));
        tmp.addAll(map.getArtifacts());
        for (Actor a : actorgo)
            tmp.addAll(a.getArtifacts());
        tmp.addAll(player.getArtifacts());

        return tmp;
    }

    @Override
    public String toString() {
        return this.getClass() + " " + widthpx + " " + heightpx;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        int x = G.camera.posx + evt.getX();
        int y = G.camera.posy + evt.getY();

        System.out.println((x / this.blockwidthpx) + ", "
                + (y / this.blockheightpx));
    }

    @Override
    public void mouseEntered(MouseEvent evt) {}

    @Override
    public void mouseExited(MouseEvent evt) {}

    @Override
    public void mousePressed(MouseEvent evt) {}

    @Override
    public void mouseReleased(MouseEvent evt) {}
}
