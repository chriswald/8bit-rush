

class Item {
    public String name;
    public int    quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void add() {
        this.quantity++;
    }

    public void add(int amount) {
        this.quantity += amount;
    }

    public void sub() {
        this.quantity--;
    }

    public void sut(int amount) {
        this.quantity -= amount;
    }
}
