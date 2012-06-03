package pkg;

public class Key {
	public String name;
	public String value;
	
	public Key() {
		this.name = "unnamed";
		this.value = "null";
	}
	
	public Key(String name, String value) {
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
