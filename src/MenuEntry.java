class MenuEntry {
    public String title;
    public String text;
    public String actionfile;

    public MenuEntry(String title) {
        this.title = title;
    }

    public MenuEntry(String title, String text) {
        this.title = title;
        this.text = text;
    }
}
