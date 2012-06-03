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
	
	public String gets(int id) {
		return list.get(id).value;
	}
	
	public Key getk(int id) {
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
}
