package pkg;

public class Key {
	public String name;
	public Object value;
	
	public Key(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Key) {
			Key k = (Key) obj;
			return (k.name.equals(this.name) && k.value.equals(this.value));
		}
		
		return false;
	}
}
