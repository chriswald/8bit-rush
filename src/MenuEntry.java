import java.util.ArrayList;

class MenuEntry {
    public String               title;
    public String               text;

    public ArrayList<MenuActor> menuactors = new ArrayList<MenuActor>();

    public MenuEntry(String title) {
        this.title = title;
    }

    public MenuEntry(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public void addMenuActor(MenuActor actor) {
        menuactors.add(actor);
    }
}
