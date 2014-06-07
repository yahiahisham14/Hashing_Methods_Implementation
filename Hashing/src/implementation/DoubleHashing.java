package implementation;

import java.util.ArrayList;

import mapEntries.ProbeBlock;

public class DoubleHashing<K, V> implements HashTable<K, V> {
	// Array of linkedLists of Type MapBlock class , each holds <key K,value V>.
	private ProbeBlock<K, V>[] map;

	// initial array size.
	private int size = 8;

	// Elements Counter.
	private int elementCounter;

	// Array list to keep track for the keys for the getIterator.
	private ArrayList<K> keys;

	private int collisions;
	
	// constructor
	public DoubleHashing() {

		map = new ProbeBlock[size];
		collisions=0;
		// initialize The array with null mapBlock objects.
		for (int i = 0; i < map.length; i++) {
			map[i] = new ProbeBlock(null, null);
		}

		// set number of elements to zero and initialize the iterator array.
		elementCounter = 0;
		keys = new ArrayList<K>();

	}

	@Override
	public void put(K key, V value) {

		// value of the home hashFunction.
		int hashIndex = getHash(key);
		int index = hashIndex;
		// first check for home position
		if (map[hashIndex] == null) {
			// home place is free.
			map[hashIndex].setKey(key);
			map[hashIndex].setValue(value);
			map[hashIndex].setTombStone(false);
			// increment the count and add key to the ArrayList.
			elementCounter++;
			keys.add(key);
		}
		// second check for duplicates to update value.
		else if (duplicateCheck(key) != -1) {

			index = duplicateCheck(key);
			map[index].setKey(key);
			map[index].setValue(value);
			map[index].setTombStone(false);

		}
		// Third : no duplicates , probe the array for the first free place.
		else {

			index = getFreeIndex(key);

			// set the key and value of the new element.
			map[index].setKey(key);
			map[index].setValue(value);
			map[index].setTombStone(false);
			// increment the count and add key to the ArrayList.
			elementCounter++;
			keys.add(key);
		}

		// check for rehashing
		double loadFactor = elementCounter / size;
		if (loadFactor >= 0.75) {
			updateSize();
		}

	}

	@Override
	public V get(K key) {
		// Checks if the map contains the key.
		int index = duplicateCheck(key);
		if (index != -1) {
			return map[index].getValue();
		}
		// value not Found
		return null;
	}

	@Override
	public void delete(K key) {
		// Checks if the map contains the key.
		int index = duplicateCheck(key);
		if (index != -1) {
			// value is Found.
			map[index].setKey(null);
			map[index].setValue(null);
			map[index].setTombStone(true);
		}
		// value not Found

	}

	@Override
	public boolean contains(K key) {
		// home place.
		int homePlace = getHash(key);
		int index = homePlace;
		int iteration = 0;

		// The loop will exit when it find the first null un-tombed slot.
		while (map[index].getKey() != null && !map[index].isTombStone()) {

			// duplicate found.
			if (map[index].getKey() != null && map[index].getKey().equals(key)) {
				// return the index of the duplicate.
				return true;
			}

			iteration++;
			// increment index by probe step.
			index = getProbe(key, iteration);
		}
		// Element is not Found.
		return false;

	}

	@Override
	public boolean isEmpty() {
		if (elementCounter == 0)
			return true;
		return false;
	}

	@Override
	public int size() {

		return elementCounter;
	}

	@Override
	public Iterable<K> keys() {

		return (Iterable<K>) keys;
	}

	// this function gets the probe value of the next iteration.
	private int getProbe(K key, int iteration) {
	
		int k=Math.abs(key.hashCode());
		int x = k / map.length;
		int y = map.length / 2;
		int z = (x % y) * 2;
		int linearProbeConstant = z + 1;
		int m = (iteration * linearProbeConstant) % map.length;
		return m;
		
	}

	// Represents the hash function of the map.
	private int getHash(K key) {
		
		return Math.abs(key.hashCode() % size);
	}

	private void updateSize() {
		// make a clone array, copy all values in the old map to it.
		ProbeBlock<K, V> clone[] = new ProbeBlock[size];
		for (int i = 0; i < clone.length; i++) {
			clone[i] = new ProbeBlock(null, null);
			if (map[i].getKey() != null) {
				clone[i].setKey(map[i].getKey());
				clone[i].setValue(map[i].getValue());
			}
		}

		size *= 2;
		map = new ProbeBlock[size];
		elementCounter = 0;
		keys = new ArrayList<K>();
		for (int i = 0; i < map.length; i++) {
			map[i] = new ProbeBlock(null, null);
		}

		// Rehashing
		for (int i = 0; i < clone.length; i++) {
			if (clone[i].getKey()!=null){
				put(clone[i].getKey(), clone[i].getValue());
			}
			
		}

	}

	// This method aims to find if the key is a duplicate and return its index.
	private int duplicateCheck(K key) {
		// home place.
		int homePlace = getHash(key);
		int index = homePlace;
		int iteration = 0;

		// The loop will exit when it find the first null un-tombed slot.
		while (map[index].getKey() != null && !map[index].isTombStone()) {

			// duplicate found.
			if (map[index].getKey() != null && map[index].getKey().equals(key)) {
				// return the index of the duplicate.
				return index;
			}

			iteration++;
			// increment index by probe step.
			index = getProbe(key, iteration);
		}
		// No duplicate Found.
		return -1;
	}

	// This method aims to probe the array to find the first free space index.
	private int getFreeIndex(K key) {
		// home place.
		int homePlace = getHash(key);
		int index = homePlace;
		int iteration = 0;

		// probe till finding a null place.
		while (map[index].getKey() != null) {

			// returned to the original position.
			if (index == homePlace && iteration != 0) {
				// for debugging.
//				System.out
//						.println("Error in Double hashing Probing can't find a free space ");
				break;
			}
			collisions++;
			iteration++;
			// increment index by probe step.
			index = getProbe(key, iteration);
		}

		// return the value of the free space.
		return index;

	}
	
	public int getCollisions(){
		return collisions;
	}

}
