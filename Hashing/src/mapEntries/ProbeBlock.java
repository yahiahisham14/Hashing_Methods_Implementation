package mapEntries;

//This class resembles the building block of map in probing it contains boolean tombstone to mark the 
//deleted objects in the map.
public class ProbeBlock<K, V> {

	private K key;
	private V value;
	private boolean tombStone;

	// Block Constructor.
	public ProbeBlock(K key, V value) {
		setKey(key);
		setValue(value);
		setTombStone(false);
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

	public boolean isTombStone() {
		return tombStone;
	}

	public void setTombStone(boolean tombStone) {
		this.tombStone = tombStone;
	}

}
