package pkg;

import java.util.ArrayList;

public class Database {
	private static ArrayList<Key> list = new ArrayList<Key>(0);
	
	/**
	 * Adds a new Key to the end of the list.
	 * @param key The key to add to the list.
	 * @return The index of the key in the list.
	 */
	public int add(Key key) {
		list.add(key);
		return list.size()-1;
	}
	
	/**
	 * Adds a new Key with parameters name and o to the end of the list.
	 * @param name The UID name of the key.
	 * @param o The value of the key. Can also be thought of as the information the key stores.
	 * @return The index of the key in the list.
	 */
	public int add(String name, Object o) {
		return add(new Key(name, o));
	}
	
	/**
	 * Removes a specific key from the list.
	 * @param key The key to attempt to remove.
	 * @return True if the key was removed, false otherwise.
	 */
	public boolean remove(Key key) {
		for(int i = 0; i < list.size(); i ++) {
			Key k = list.get(i);
			if (k.equals(key)) {
				list.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the key at a specified index from the list.
	 * @param index The index of the key to remove.
	 * @return True if the key was removed, false otherwise.
	 */
	public boolean remove(int index) {
		if (index >= 0 && index < list.size()) {
			list.remove(index);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes the key with the specified UID from the list.
	 * @param uid The UID of the key to remove.
	 * @return True if the key was removed, false otherwise.
	 */
	public boolean remove(String uid) {
		for (int i = 0; i < list.size(); i ++) {
			Key k = list.get(i);
			if (k.name.equals(uid)) {
				list.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Changes the value of the key at index id.
	 * @param id The index of the key to change the value of.
	 * @param o The value to set the key to.
	 * @return True if the key's value was changed, false otherwise.
	 */
	public boolean set(int id, Object o) {
		if (id >= 0 && id < list.size()) {
			list.get(id).value = o;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Changes the value of the key with the specified UID.
	 * @param uid The UID of the key to change.
	 * @param o The value to set the key to.
	 * @return True if the key's value was changed, false otherwise.
	 */
	public boolean set(String uid, Object o) {
		int index = find(uid);
		if (index == -1)
			return false;
		list.get(index).value = o;
		return true;
	}
	
	/**
	 * Returns the value of the key at the specified id.
	 * @param id The index of the key to return the value of.
	 * @return The value of the key at index id.
	 */
	public Object get(int id) {
		return list.get(id).value;
	}
	
	/**
	 * Returns the value of the key with the given name.
	 * @param uid The UID of the key to get the value of.
	 * @return The key's value (null if not found).
	 */
	public Object get(String uid) {
		int id = find(uid);
		if (id >= 0 && id < list.size()) {
			return list.get(id).value;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the entire key stored at location id.
	 * @param id The location of the key to return.
	 * @return The key at location id.
	 */
	public Key getkey(int id) {
		return list.get(id);
	}
	
	/**
	 * Finds the integer id of the key with the provided UID.
	 * @param uid The UID of key to find.
	 * @return The index of the key.
	 */
	public int find(String uid) {
		for (int i = 0; i < list.size(); i ++) {
			Key k = list.get(i);
			if (k.name.equals(uid))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Determines whether the list has a key with the provided UID.
	 * @param uid The UID to search for.
	 * @return True if found, false otherwise.
	 */
	public boolean has(String uid) {
		return (find(uid) != -1);
	}
	
	/**
	 * Returns the index of the key with the provided UID.
	 * If a key with the provided UID is not found then a new key is created
	 * (with name UID and value NOTFOUND) and appended to the end of the list,
	 * and its index is returned.
	 * @param uid The UID of the key to find.
	 * @param notfound The value to give a new key if one with name UID is not found.
	 * @return The index of the existing or new key.
	 */
	public int getint(String uid, Object notfound) {
		int i = this.find(uid);
		
		if (i == -1)
			return this.add(uid, notfound);
		else
			return i;
	}
}
