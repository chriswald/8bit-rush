package pkg;

import java.util.ArrayList;

public class Database {
	private ArrayList<Key> list;
	
	public Database() {
		list = new ArrayList<Key>();
	}
	
	public int add(Key key) {
		list.add(key);
		return list.size()-1;
	}
	
	public int add(String name, Object o) {
		return add(new Key(name, o));
	}
	
	public void remove(Key key) {
		for(int i = 0; i < list.size(); i ++) {
			Key k = list.get(i);
			if (k.equals(key)) {
				list.remove(i);
				return;
			}
		}
	}
	
	public void remove(int index) {
		list.remove(index);
	}
	
	public void remove(String name) {
		for (int i = 0; i < list.size(); i ++) {
			Key k = list.get(i);
			if (k.name.equals(name)) {
				list.remove(i);
				return;
			}
		}
	}
	
	public void set(int id, Object o) {
		this.list.get(id).value = o;
	}
	
	public Object get(int id) {
		return list.get(id).value;
	}
	
	public Key getkey(int id) {
		return list.get(id);
	}
	
	public int find(String name) {
		for (int i = 0; i < list.size(); i ++) {
			Key k = list.get(i);
			if (k.name.equals(name))
				return i;
		}
		
		return -1;
	}
	
	public boolean has(String name) {
		return (find(name) != -1);
	}
	
	public int getint(String name, Object notfound) {
		int i = this.find(name);
		
		if (i == -1)
			return this.add(name, notfound);
		else
			return i;
	}
}
