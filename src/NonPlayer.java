import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class NonPlayer extends Character implements KeyListener {

    public Tree<MenuEntry> menu          = new Tree<MenuEntry>();
    public boolean         showingmenu   = false;
    public BufferedImage   imgmenu;
    public long            showtime;
    public Node<MenuEntry> currentmenu;
    public int             selectedchild = 0;

    public static String   DLGEXTENSION  = ".dlg";
    public static String   ACTEXTENSION  = ".act";

    public NonPlayer(String filename) {
        super(filename);
        ID = "nonplayer";

        this.rightside = true;
        this.leftside = true;
        this.topside = true;
        this.bottomside = true;
        buildMenu();
        currentmenu = this.menu.getRoot();

        drawMenu();
    }

    public void buildMenu() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(G.RESDIR
                    + G.DLGDIR + file + DLGEXTENSION));
            String line = "";
            String curtitle = "";
            int tabs = 0;
            while ((line = in.readLine()) != null) {
                while (line.startsWith("\t") || line.startsWith("    ")) {
                    tabs++;

                    if (line.startsWith("\t"))
                        line = line.substring(1);
                    else if (line.startsWith("       "))
                        line = line.substring(7);
                    else if (line.startsWith("    "))
                        line = line.substring(4);
                }

                if (tabs == 0) {
                    curtitle = line.substring(1);
                    menu.root = new Node<MenuEntry>(new MenuEntry(curtitle, ""));
                } else {
                    Node<MenuEntry> current = menu.getRoot();
                    for (int i = tabs; i > 1; i--) {
                        current = current.getLastChild();
                    }

                    if (line.startsWith("^")) {
                        curtitle = line.substring(1);
                        current.addChild(new MenuEntry(curtitle, ""));
                    } else {
                        if (line.startsWith("*")) {
                            current.data.actionfile = line.substring(1)
                                    + ACTEXTENSION;
                        } else {
                            current.data.text = line;
                        }
                    }
                }

                tabs = 0;
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Cannot load the dialogue file for the NPC.\n"
                    + file + "\n" + e.getLocalizedMessage());
            System.exit(1);
        }

    }

    public void drawMenu() {
        // Start with a default width of 150 and a default height of 14.
        // Define a scalar value for the width of a character.
        // Define a list of strings to store lines of the NPC's talk text as it
        // is broken apart to fit into the menu box.
        int width = 150;
        int height = (currentmenu.getNumChildren() + 1) * 14;
        double scalar = 7.1;
        ArrayList<String> lines = new ArrayList<String>();

        // If the current menu has no children then we want to display it's
        // title and text (as opposed to just the title as part of an
        // interactable menu).
        if (!currentmenu.hasChildren()) {
            if (currentmenu.data.actionfile == null
                    || currentmenu.data.actionfile.equals("")) {
                String text = currentmenu.getData().text;

                // If the pixel length of the text is greater than 250 we need
                // to
                // break it into lines.
                if (text.length() * scalar > 250) {
                    // Break the line into words, set the width to 250, set up a
                    // temporary width (w) as 0, and initialize a line variable
                    // to
                    // hold the line we are currently formatting.
                    String toks[] = text.split(" ");
                    width = 250;
                    int w = 0;
                    String line = "";

                    // For every word in "toks" calculate the word's pixel
                    // width. If
                    // that width plus the width of the rest of the line is
                    // greater
                    // than 250 move that word to a new line.
                    for (String s : toks) {
                        w += (s.length() + 1) * scalar;
                        if (w < width) {
                            line += s + " ";
                        } else {
                            lines.add(line);
                            line = s + " ";
                            w = 0;
                        }
                    }
                    // Add the last line if its not empty.
                    if (line != "")
                        lines.add(line);

                    // Determine what the new maximum line length is and set the
                    // final width to that.
                    int maxw = 0;
                    for (String s : lines) {
                        if (s.length() * scalar > maxw)
                            maxw = (int) (s.length() * scalar);
                    }
                    width = maxw;
                }
                // If the line's pixel length is greater than the base 150 but
                // not
                // the max 250 just up the width of the menu box.
                else if (text.length() * scalar > 150) {
                    width = (int) (text.length() * scalar);
                    lines.add(text);
                } else {
                    lines.add(text);
                }
                height += 14 * lines.size() + 1;
            } else {
                ActParse.run(currentmenu.data.actionfile, this);
                this.showingmenu = false;
            }
        }
        // If the node has children calculate the pixel length of the longest
        // title string among those children.
        else {
            int w = 0;
            for (Node<MenuEntry> n : currentmenu.children) {
                if (n.data.title.length() * scalar > w)
                    w = (int) (n.data.title.length() * scalar);
            }

            // I don't care about an upper bound, so if it's greater than the
            // base 150 just set it.
            if (w > 150)
                width = w;
        }

        Dimension dim = new Dimension(width, height);
        BufferedImage tmp = new BufferedImage(dim.width, dim.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = tmp.getGraphics();

        g.setFont(new Font("Courier New", Font.PLAIN, 12));
        g.setColor(Color.black);
        g.fillRect(0, 0, dim.width, dim.height);
        g.setColor(Color.white);
        g.drawString(currentmenu.getData().title, 1, 13);

        if (currentmenu.hasChildren()) {
            for (int i = 0; i < currentmenu.getNumChildren(); i++) {
                if (i == selectedchild) {
                    g.setColor(Color.white);
                    g.fillRect(0, (i + 1) * 14, dim.width, 14);
                    g.setColor(Color.black);
                    g.drawString(currentmenu.getChild(i).getData().title, 3,
                            (i + 2) * 14 - 2);
                } else {
                    g.setColor(Color.black);
                    g.fillRect(0, (i + 1) * 14, dim.width, 14);
                    g.setColor(Color.white);
                    g.drawString(currentmenu.getChild(i).getData().title, 3,
                            (i + 2) * 14 - 2);
                }
            }
        } else {
            g.setColor(Color.black);
            g.fillRect(0, 14, width, 14);
            g.setColor(Color.white);
            for (int i = 0; i < lines.size(); i++) {
                g.drawString(lines.get(i), 3, (i + 2) * 14 - 2);
            }
        }

        imgmenu = tmp;
    }

    @Override
    public void interact() {
        showingmenu = true;
        showtime = System.currentTimeMillis();
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {
        ArrayList<Artifact> tmp = new ArrayList<Artifact>(1);
        tmp.add(new Artifact((int) this.posx, (int) this.posy, this.img));
        if (showingmenu)
            tmp.add(new Artifact((int) this.posx - imgmenu.getWidth() / 4,
                    (int) this.posy - imgmenu.getHeight() - 20, imgmenu));

        return tmp;
    }

    @Override
    public void drawPlayer() {
        Graphics g = img.getGraphics();
        g.setColor(Color.yellow);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void die() {}

    @Override
    public void update() {
        if (ground) {
            vely = 0;
        } else {
            vely += .5;
        }

        if (!rightwall && !leftwall)
            velx = -velx;

        if (vely > this.height / 2)
            vely = this.height / 2;

        posx += velx;
        posy += vely;

        if (System.currentTimeMillis() - showtime > 2000) {
            this.showingmenu = false;
            this.currentmenu = this.menu.getRoot();
            this.selectedchild = 0;
            drawMenu();
        }
    }

    @Override
    public void onCollide(CollidedSide side, Collider c) {
        switch (side) {
        case BOTTOM:
            if (this.vely > 0) {
                this.posy = c.posy - this.height;
                this.vely = 0;
                this.ground = true;
            }
            break;
        case RIGHT:
            if (c.ID.equals("player"))
                this.velx = 0;
            else
                this.velx = -this.velx;
            this.rightwall = true;
            break;
        case LEFT:
            if (c.ID.equals("player"))
                this.velx = 0;
            else
                this.velx = -this.velx;
            this.leftwall = true;
            break;
        case TOP:
            this.ceiling = true;
            break;
        default:
            break;
        }
    }

    @Override
    public void keyPressed(KeyEvent evt) {
        if (showingmenu) {
            switch (evt.getKeyCode()) {
            case KeyEvent.VK_I:
            case KeyEvent.VK_UP:
                if (this.currentmenu.hasChildren()) {
                    this.selectedchild--;
                    if (this.selectedchild < 0)
                        this.selectedchild = this.currentmenu.getNumChildren() - 1;
                    drawMenu();
                    this.showtime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_K:
            case KeyEvent.VK_DOWN:
                if (this.currentmenu.hasChildren()) {
                    this.selectedchild = (this.selectedchild + 1)
                            % this.currentmenu.getNumChildren();
                    drawMenu();
                    this.showtime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_ENTER:
                if (currentmenu.hasChildren()) {
                    this.currentmenu = this.currentmenu.getChild(selectedchild);
                    selectedchild = 0;
                    drawMenu();
                }
                break;
            case KeyEvent.VK_J:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_BACK_SPACE:
                if (this.currentmenu.getParent() != null) {
                    this.currentmenu = this.currentmenu.getParent();
                    selectedchild = 0;
                }
                drawMenu();
                break;
            default:
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        switch (evt.getKeyCode()) {
        case KeyEvent.VK_UP:
            break;
        case KeyEvent.VK_RIGHT:
            break;
        case KeyEvent.VK_DOWN:
            break;
        case KeyEvent.VK_LEFT:
            break;
        case KeyEvent.VK_ENTER:
            break;
        case KeyEvent.VK_BACK_SPACE:
            break;
        default:
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent evt) {}

}
