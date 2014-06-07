package mapEntries;

//This class resembles building block of the map.(In bucketing and separate chaining)
//contains generic Key K and Generic value V.

public class MapBlock <K,V>{

	private K key;
	private V value;
	
	//Block Constructor.
	public MapBlock(K key, V value){
		setKey(key);
		setValue(value);
	}
	
	public K getKey() {
		return key;
	}
	
	public void setKey(K key) {
		this.key = key;
	}
	
	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
	
	
}
