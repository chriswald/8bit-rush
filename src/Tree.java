import java.util.ArrayList;

class Tree <T>{
    public Node<T> root;

    public Node<T> getRootElement() {
	return this.root;
    }

    public void setRootElement(Node<T> root) {
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
