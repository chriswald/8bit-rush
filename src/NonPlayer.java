import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class NonPlayer extends Character implements KeyListener {

    public Tree<String>  menus         = new Tree<String>();
    public boolean       showingmenu   = false;
    public BufferedImage menu;
    public long          showtime;
    public Node<String>  currentmenu;
    public int           selectedchild = 0;

    public NonPlayer(String filename) {
        super(filename);
        ID = "nonplayer";

        this.rightside = true;
        this.leftside = true;
        this.topside = true;
        this.bottomside = true;
        buildtmpmenu();
        currentmenu = this.menus.getRoot();

        draw();
    }

    public void buildtmpmenu() {
        this.menus.setRoot(new Node<String>("Hello!"));
        this.menus.root.addChild(new Node<String>("Talk"));
        this.menus.root.addChild(new Node<String>("Walk"));
    }

    public void draw() {
        int width = 100;
        int height = (currentmenu.getNumChildren() + 1) * 14;
        BufferedImage tmp = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = tmp.getGraphics();
        g.setFont(new Font("Courier New", Font.PLAIN, 12));
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.white);
        g.drawString(currentmenu.getData(), 1, 13);

        for (int i = 0; i < currentmenu.getNumChildren(); i++) {
            if (i == selectedchild) {
                g.setColor(Color.white);
                g.fillRect(0, (i + 1) * 14, width, 14);
                g.setColor(Color.black);
                g.drawString(currentmenu.getChild(i).getData(), 3,
                        (i + 2) * 14 - 2);
            } else {
                g.setColor(Color.black);
                g.fillRect(0, (i + 1) * 14, width, 14);
                g.setColor(Color.white);
                g.drawString(currentmenu.getChild(i).getData(), 3,
                        (i + 2) * 14 - 2);
            }
        }

        menu = tmp;
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
            tmp.add(new Artifact((int) this.posx - 25, (int) this.posy - 70,
                    menu));

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

        if (System.currentTimeMillis() - showtime > 1000) {
            this.showingmenu = false;
            this.currentmenu = this.menus.getRoot();
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
            case KeyEvent.VK_UP:
                if (this.currentmenu.getNumChildren() > 0) {
                    this.selectedchild--;
                    if (this.selectedchild < 0)
                        this.selectedchild = this.currentmenu.getNumChildren() - 1;
                    draw();
                    this.showtime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (this.currentmenu.getNumChildren() > 0) {
                    this.selectedchild = (this.selectedchild + 1)
                            % this.currentmenu.getNumChildren();
                    draw();
                    this.showtime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_ENTER:
                this.currentmenu = this.currentmenu.getChild(selectedchild);
                draw();
                break;
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
