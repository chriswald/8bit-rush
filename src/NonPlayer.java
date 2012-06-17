import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class NonPlayer extends Character implements KeyListener {

    public Tree<MenuEntry> menu          = new Tree<MenuEntry>();
    public boolean         showingmenu   = false;
    public BufferedImage   imgmenu;
    public long            showtime;
    public Node<MenuEntry> currentmenu;
    public int             selectedchild = 0;

    public NonPlayer(String filename) {
        super(filename);
        ID = "nonplayer";

        this.rightside = true;
        this.leftside = true;
        this.topside = true;
        this.bottomside = true;
        buildtmpmenu();
        currentmenu = this.menu.getRoot();

        draw();
    }

    public void buildtmpmenu() {
        this.menu.setRoot(new Node<MenuEntry>(new MenuEntry("Hello!")));
        this.menu.root
                .addChild(new Node<MenuEntry>(
                        new MenuEntry("Talk",
                                "I used to be an adventurer like you before I developed weak wrists.")));
        this.menu.root.addChild(new Node<MenuEntry>(new MenuEntry("Walk",
                "No, I don't feel like going anywhere.")));
    }

    public void draw() {
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
            String text = currentmenu.getData().text;

            // If the pixel length of the text is greater than 250 we need to
            // break it into lines.
            if (text.length() * scalar > 250) {
                // Break the line into words, set the width to 250, set up a
                // temporary width (w) as 0, and initialize a line variable to
                // hold the line we are currently formatting.
                String toks[] = text.split(" ");
                width = 250;
                int w = 0;
                String line = "";

                // For every word in "toks" calculate the word's pixel width. If
                // that width plus the width of the rest of the line is greater
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
            // If the line's pixel length is greater than the base 150 but not
            // the max 250 just up the width of the menu box.
            else if (text.length() * scalar > 150) {
                width = (int) (text.length() * scalar);
            }
            height += 14 * lines.size() + 1;
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
        g.setColor(Color.blue);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void die() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        if (ground) {
            vely = 0;
        } else {
            vely += .5;
        }

        if (rightwall || leftwall)
            velx = -velx;

        if (vely > this.height / 2)
            vely = this.height / 2;

        posx += velx;
        posy += vely;

        if (System.currentTimeMillis() - showtime > 2000) {
            this.showingmenu = false;
            this.currentmenu = this.menu.getRoot();
        }
    }

    @Override
    public void onCollide(CollidedSide side, Collider c) {
        switch (side) {
        case BOTTOM:
            this.posy = c.posy - this.height;
            this.vely = 0;
            this.ground = true;
            break;
        case RIGHT:
            this.rightwall = true;
            break;
        case LEFT:
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
                    draw();
                    this.showtime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_K:
            case KeyEvent.VK_DOWN:
                if (this.currentmenu.hasChildren()) {
                    this.selectedchild = (this.selectedchild + 1)
                            % this.currentmenu.getNumChildren();
                    draw();
                    this.showtime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_ENTER:
                if (currentmenu.hasChildren()) {
                    this.currentmenu = this.currentmenu.getChild(selectedchild);
                    draw();
                }
                break;
            case KeyEvent.VK_J:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_BACK_SPACE:
                if (this.currentmenu.getParent() != null) {
                    this.currentmenu = this.currentmenu.getParent();
                }
                draw();
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
