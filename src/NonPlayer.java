import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class NonPlayer extends Character {

    public Tree<Menu> menus       = new Tree<Menu>();
    public Menu       currentmenu = new Menu();
    public boolean    showingmenu = false;
    public long       showtime;

    public NonPlayer(String filename) {
        super(filename);
        ID = "nonplayer";

        this.rightside = true;
        this.leftside = true;
        this.topside = true;
        this.bottomside = true;
        this.menus.root = new Node<Menu>();
        this.menus.root.data = currentmenu;
    }

    @Override
    public void interact() {
        showingmenu = true;
        currentmenu.setLocation((int) this.posx - 25, (int) this.posy - 40);
        showtime = System.currentTimeMillis();
    }

    @Override
    public ArrayList<Artifact> getArtifacts() {
        ArrayList<Artifact> tmp = new ArrayList<Artifact>(1);
        tmp.add(new Artifact((int) this.posx, (int) this.posy, this.img));
        if (showingmenu)
            tmp.add(new Artifact((int) this.posx - 25, (int) this.posy - 70,
                    this.currentmenu.img));

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

        if (System.currentTimeMillis() - showtime > 1000)
            this.showingmenu = false;
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

}
