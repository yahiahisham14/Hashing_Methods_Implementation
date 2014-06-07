package implementation;

import java.util.ArrayList;
import java.util.LinkedList;

import mapEntries.MapBlock;

public class Bucketing<K, V> implements HashTable<K, V> {

	// Array of linkedLists of Type MapBlock class , each holds <key K,value V>.
	private MapBlock<K, V>[] map;

	// initial array size.
	private int size = 25;

	// initial Bucket Size
	private int BucketSize = 5;

	// Elements Counter.
	private int elementCounter;

	// Array list to keep track for the keys for the getIterator.
	private ArrayList<K> keys;

	// Array for the overFlow.
	private MapBlock<K, V> overFlow[];

	// index used to track the last element in the overflow array.
	private int overFlowIndex;

	private int collisions;
	// constructor
	public Bucketing() {
		map = new MapBlock[size];
		overFlow = new MapBlock[BucketSize * 2];
		collisions=0;
		// initialize both arrays with null objects/
		for (int i = 0; i < map.length; i++) {
			map[i] = new MapBlock<>(null, null);
		}

		for (int i = 0; i < overFlow.length; i++) {
			overFlow[i] = new MapBlock<>(null, null);
		}

		elementCounter = 0;
		keys = new ArrayList<K>();
		overFlowIndex = 0;
		
	}

	@Override
	public void put(K key, V value) {
		// value of the hashFunction.
		int hashIndex = getHash(key);

		// boolean to track duplicate.
		boolean duplicate = false;

		int bucketBegin = hashIndex / 5;

		// first check for duplicates to update value.
		for (int i = bucketBegin; i < bucketBegin + 5; i++) {

			// duplicate found.
			if (map[i].getKey()!=null && map[i].getKey().equals(key)) {
				// update the old block.
				map[i].setValue(value);
				duplicate = true;
			}
		}

		if (!duplicate) {
			int index = getFreeIndex(key);
			if (index != -1) {
				// set the key and value of the new element.
				map[index].setKey(key);
				map[index].setValue(value);

			} else {
				// store in the overFlow array
				if (overFlowIndex==overFlow.length){
					//Make a copy from the overflow
					MapBlock<K, V> oFlowCpy[]=new MapBlock[overFlowIndex];
					for (int i = 0; i < oFlowCpy.length; i++) {
						oFlowCpy[i]=new MapBlock(null,null);
						oFlowCpy[i].setKey(overFlow[i].getKey());
						oFlowCpy[i].setValue(overFlow[i].getValue());
					}
					int oldIndex=overFlow.length;
					overFlow=new MapBlock[oldIndex*2];
					
					for (int i = 0; i < oFlowCpy.length; i++) {
						overFlow[i]= new MapBlock(oFlowCpy[i].getKey(), oFlowCpy[i].getValue());
						
					}
					for (int i = overFlowIndex ; i < overFlow.length; i++) {
						overFlow[i]=new MapBlock(null, null);
					}

				}
				overFlow[overFlowIndex].setKey(key);
				overFlow[overFlowIndex].setValue(value);
				overFlowIndex++;
			}
			// increment the count and add key to the ArrayList.
			elementCounter++;
			keys.add(key);
		}

		// check for rehashing
		double loadFactor=elementCounter/map.length;
		if (loadFactor >= 0.75){
			updateSize();
		}

	}

	
	// this method used to get the first free index in the bucket
	// returns -1 if it's to be put in the overflow.
	private int getFreeIndex(K key) {

		// first check in its home
		int hashIndex = getHash(key);
		if (map[hashIndex].getKey() == null)
			return hashIndex;

		// then search sequentially in the bucket.
		int bucketBegin = hashIndex / 5;
		for (int i = bucketBegin; i < bucketBegin + 5; i++) {
			// check if place is empty
			if (map[i].getKey() == null) {
				return i;
			}
			collisions++;
		}

		// no empty place found in the bucket --> store in the overFlow
		return -1;
	}

	@Override
	public V get(K key) {

		// Checks if the map contains the key.
		if (contains(key)) {

			// value of the hashFunction.
			int hashIndex = getHash(key);

			// first check home.
			if (map[hashIndex].getKey()!=null && map[hashIndex].getKey().equals(key)) {
				return map[hashIndex].getValue();
			}

			// search sequentially.
			int bucketBegin = hashIndex / BucketSize;

			for (int i = bucketBegin; i < bucketBegin + 5; i++) {
				// key Found.
				if (map[i].getKey()!=null && map[i].getKey().equals(key)) {
					return map[i].getValue();
				}
			}
		}
		// not found in bucket , Search overFlow
		for (int i = 0; i < overFlow.length; i++) {
			// Found
			if (map[i].getKey()!=null && map[i].getKey().equals(key)) {
				return map[i].getValue();
			}
		}

		// Not Found.
		return null;

	}

	@Override
	//find the element and set the values of key and value null. 
	public void delete(K key) {
		// value of the hashFunction.
		int hashIndex = getHash(key);
		// first check home.
		if (map[hashIndex].getKey()!=null && map[hashIndex].getKey().equals(key)) {
			map[hashIndex].setKey(null);
			map[hashIndex].setValue(null);
			//remove from keys list. 
			keys.remove(key);
			elementCounter--;
			//exit
			
			return;
		}

		// search sequentially.
		int bucketBegin = hashIndex / BucketSize;

		for (int i = bucketBegin; i < bucketBegin + 5; i++) {
			// key Found.
			if (map[i].getKey() !=null && map[i].getKey().equals(key)) {
				map[i].setKey(null);
				map[i].setValue(null);
				//remove from keys list. 
				keys.remove(key);
				elementCounter--;
				return;
			}
		}
		//Search in the overflow
		for (int i = 0; i < overFlow.length; i++) {
			if (overFlow[i].getKey()!=null && overFlow[i].getKey()==key){
				overFlow[i].setKey(null);
				overFlow[i].setValue(null);
				//remove from keys list. 
				keys.remove(key);
				//decrement the counter and the index of the overflow.
				elementCounter--;
				overFlowIndex--;
				//exit
				return;
			}
		}

	}

	@Override
	public boolean contains(K key) {
		// value of the hashFunction.
		int hashIndex = getHash(key);
		// first check home.
		if (map[hashIndex].getKey()!=null && map[hashIndex].getKey().equals(key)) {
			return true;
		}

		// search sequentially.
		int bucketBegin = hashIndex / BucketSize;

		for (int i = bucketBegin; i < bucketBegin + 5; i++) {
			// key Found.
			if (map[i].getValue()!=null && map[i].getKey().equals(key)) {
				return true;
			}
		}

		// not found in bucket , Search overFlow
		for (int i = 0; i < overFlow.length; i++) {
			// Found
			if (map[i].getKey()!=null && map[i].getKey().equals(key)) {
				return true;
			}
		}
		

		return false;

	}

	@Override
	public boolean isEmpty() {
		if (elementCounter==0)
			return true;
		return false;
	}

	@Override
	public int size() {
		return elementCounter;
	}

	@Override
	public Iterable<K> keys() {
		
		return (Iterable<K>) keys();
	}

	private int getHash(K key) {
		int k=Math.abs(key.hashCode());
		return k % map.length;
	}
	private void updateSize() {
		//make a clone array, copy all values in the old map to it.
		MapBlock<K, V> clone[]=new MapBlock[size];
		for (int i = 0; i < clone.length; i++) {
			clone[i]=new MapBlock(null,null);
			if (map[i].getKey()!=null){
				clone[i].setKey(map[i].getKey());
				clone[i].setValue(map[i].getValue());
			}
		}
		
		int oldOverFlow=overFlow.length;
		//Make a copy from the overflow
		MapBlock<K, V> oFlowCpy[]=new MapBlock[overFlowIndex];
		for (int i = 0; i < oFlowCpy.length; i++) {
			oFlowCpy[i]=new MapBlock(null,null);
			oFlowCpy[i].setKey(overFlow[i].getKey());
			oFlowCpy[i].setValue(overFlow[i].getValue());
		}
		
		
		size*=2;
		map=new MapBlock[size];
		overFlow=new MapBlock[overFlowIndex*2];
		overFlowIndex=0;
		elementCounter=0;
		keys=new ArrayList<K>();
		
		//reInitialize the two arrays with nulls.
		for (int i = 0; i < map.length; i++) {
			map[i]=new MapBlock(null, null);
		}
		for (int i = 0; i < overFlow.length; i++) {
			overFlow[i]= new MapBlock(null, null);
		}
		
		for (int i = 0; i < clone.length; i++) {
			if (clone[i].getKey()!=null){
				put(clone[i].getKey(), clone[i].getValue());
			}
		}
		
		
		for (int i = 0; i < oFlowCpy.length; i++) {
			put(oFlowCpy[i].getKey(), oFlowCpy[i].getValue());
		}
		
	}
	public int getCollisions(){
		return collisions;
	}
	


}
