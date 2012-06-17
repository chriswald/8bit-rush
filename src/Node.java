import java.util.ArrayList;

class Node<T> {
    public T                  data;
    public ArrayList<Node<T>> children = new ArrayList<Node<T>>();
    public Node<T>            parent;

    public Node() {}

    public Node(T data) {
        this.data = data;
    }

    public ArrayList<Node<T>> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<Node<T>> children) {
        this.children = children;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public int getNumChildren() {
        return this.children.size();
    }

    public Node<T> getChild(int index) {
        return this.children.get(index);
    }

    public Node<T> getParent() {
        return this.parent;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public void addChild(Node<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void insertChild(int index, Node<T> child)
            throws IndexOutOfBoundsException {
        child.setParent(this);
        if (index == getNumChildren()) {
            addChild(child);
        } else {
            children.get(index);
            children.add(index, child);
        }
    }

    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        this.children.remove(index);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
