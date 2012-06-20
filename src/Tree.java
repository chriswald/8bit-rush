import java.util.ArrayList;

class Tree<T> {
    public Node<T> root;

    public Tree() {
        root = new Node<T>();
    }

    public Node<T> getRoot() {
        return this.root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }

    public ArrayList<Node<T>> toList() {
        ArrayList<Node<T>> list = new ArrayList<Node<T>>();
        walk(this.root, list);
        return list;
    }

    public void walk(Node<T> element, ArrayList<Node<T>> list) {
        list.add(element);
        for (Node<T> data : element.getChildren()) {
            walk(data, list);
        }
    }
}
