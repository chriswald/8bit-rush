import java.util.ArrayList;

class Node<T> {
    public T                  data;
    public ArrayList<Node<T>> children = new ArrayList<Node<T>>();

    public ArrayList<Node<T>> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<Node<T>> children) {
        this.children = children;
    }

    public int getNumChildren() {
        return this.children.size();
    }

    public void addChild(Node<T> child) {
        this.children.add(child);
    }

    public void insertChild(int index, Node<T> child)
            throws IndexOutOfBoundsException {
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
