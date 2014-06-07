package implementation;

import java.util.ArrayList;
import java.util.LinkedList;

import mapEntries.MapBlock;

public class SeparateChaining<K, V> implements HashTable<K, V> {

	// Array of linkedLists of Type MapBlock class , each holds <key K,value V>.
	private LinkedList<MapBlock<K, V>>[] map;

	// initial array size.
	private int size = 10;
	
	//Elements Counter.
	private int elementCounter;

	//Array list to keep track for the keys for the getIterator.
	private ArrayList< K> keys;
	
	private int collisions;
	
	// Constructor of the LinkedList array.
	public SeparateChaining() {
		collisions=0;
		map = new LinkedList[size];
		for (int i = 0; i < map.length; i++) {
			map[i] = new LinkedList<MapBlock<K, V>>();
		}
		elementCounter=0;
		keys=new ArrayList<K>();
	}

	@Override
	// Assume hashing Function is simple modulo hashing function.
	
	public void put(K key, V value) {

		// value of the hashFunction.
		int hashIndex = getHash(key);
		if (map[hashIndex].size()!=0)
			collisions++;
		
		//boolean to track duplicate.
		boolean duplicate=false;
		
		// first check for duplicates to update value.
		for (int i = 0; i < map[hashIndex].size(); i++) {

			// duplicate found.
			if (map[hashIndex].get(i).getKey().equals(key)) {
				// remove the old block.
				map[hashIndex].remove(i);
				duplicate=true;
			}
		}

		// Make new Block and Add it to the linkedList.
		MapBlock<K, V> newBlock = new MapBlock<>(key, value);
		map[hashIndex].add(newBlock);
		
		//if there was no duplicate ,increment the counter.
		if(!duplicate){
			elementCounter++;
			keys.add(key);
			
			if (elementCounter/size >= 3)
				updateHashing();
		}

	}

	@Override
	public V get(K key) {

		// Checks if the map contains the key.
		if (contains(key)) {
			// value of the hashFunction.
			int hashIndex = getHash(key);

			// Searching
			for (int i = 0; i < map[hashIndex].size(); i++) {
				// Found
				if (map[hashIndex].get(i).getKey().equals(key)) {
					return map[hashIndex].get(i).getValue();
				}
			}
		}
		// Not Found.
		return null;
	}

	@Override
	public void delete(K key) {
		// value of the hashFunction.
		int hashIndex = getHash(key);

		// Searching
		for (int i = 0; i < map[hashIndex].size(); i++) {

			// Found.
			if (map[hashIndex].get(i).getKey().equals(key)) {
				// Remove the required Block ,decrement the counter and exit.
				map[hashIndex].remove(i);
				elementCounter--;
				keys.remove(key);
				break;
			}
		}
	}

	@Override
	public boolean contains(K key) {
		// value of the hashFunction.
		int hashIndex = getHash(key);
		// Search for the element.
		
		for (int i = 0; i < map[hashIndex].size(); i++) {
			// key Found.
			if (map[hashIndex].get(i).getKey().equals(key)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isEmpty() {
		if (elementCounter==0)
			return false;
		
		return true;
	}

	@Override
	public int size() {
		
		return elementCounter;
	}

	@SuppressWarnings("unchecked")
	@Override
	//return iterator on keys.
	public Iterable<K> keys() {
		return (Iterable<K>) keys;
	}

	// This method will hold the HashFuntion.
	private int getHash(K key) {
		return  Math.abs(key.hashCode() % size);
	}
	
	//This method is used to double the size of the array when load factor >= 3. 
	private void updateHashing(){
		//construct a new hashTable to be a copy of the old one.
		LinkedList<MapBlock<K, V>> mapCopy[]=new LinkedList[size];
		
		for (int i = 0; i < mapCopy.length; i++) {
			mapCopy[i]=new LinkedList<MapBlock<K, V>>();
			for (int j = 0; j < map[i].size(); j++) {
				//copy the elements one by one.
				mapCopy[i].add(map[i].get(j));
			}
		}
		
		//update the size to be doubled.
		size*=2;
		
		//rehash the elements to the old map.
		map = new LinkedList[size];
		for (int i = 0; i < map.length; i++) {
			map[i] = new LinkedList<MapBlock<K, V>>();
		}
		
		//loop over the copy map and rehash every element.
		for (int i = 0; i < mapCopy.length; i++) {
			for (int j = 0; j < mapCopy[i].size(); j++) {
				// value of the hashFunction.
				int hashIndex = getHash(mapCopy[i].get(j).getKey());
				// Make new Block and Add it to the linkedList.
				MapBlock<K, V> newBlock = new MapBlock<>(mapCopy[i].get(j).getKey(), mapCopy[i].get(j).getValue());
				map[hashIndex].add(newBlock);
				
			}
		}
		
	}
	
	public int getCollisions(){
		return collisions;
	}

}
