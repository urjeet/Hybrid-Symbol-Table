/** An implementation of a hybrid Symbol Table that provides hash table access, indexed integer access,
and sequential access (using an iterator).
* @author Urjeet Deshmukh - October 19th, 2020
*/

import java.io.*;
import java.util.*;
import java.lang.*;

public class HybridSymbolTable<V> implements Iterable<V>
{
	private Node<V> head; // First node
    private  Node<V> tail; // Last node
    private  Node<V> curr; // Current node
    private Node<V>[] hashTable; // Hashtable
	private int M; // Hash number to be mod by
    private int keyValPairs = 0; // Length and amount of pairs
   
   	// Constructor to initialize hashTable and mod number to size
   	@SuppressWarnings("unchecked")
	public HybridSymbolTable(int size)
   	{
   		M = size;	// Set mod number to size of HybridSymbolTable
   		hashTable = new Node[size];	// Set hashTable to size of HybridSymbolTable
   		curr = head;	// Begin array cursor at head
   	}

   	// Iterate over values()
   	public Iterator<V> iterator()
   	{
        return values().iterator();
    }

	/**
	* put() if int argument is passed in
	* Convert to string
	* @param key
	* @param value
	*/
	public void put(int key, V value)
   	{
   		put(String.valueOf(key), value);	// String.valueOf() converts int to String and pass into put() with value
   	}

   	/** Modified from Lab5
	* put() if String argument is passed in and creates new node
	* Calls resize() if more than 50% is taken
	* Follows linear probing processes if collision occurs
	* @param key
	* @param value
	*/
   	public void put(String key, V value)
   	{
   		if(value == null){	// If the value to add is null, delete (unset) it
   			unset(key);
   		}

   		if(keyValPairs >= M/2){		// If 50% of table is filled, then resize
   			resize(2*M);
   		}

   		int i;
   		for(i = hash(key); hashTable[i] != null; i = (i + 1) % M){	// Modified from Lab5, iterate and use hash function
   			if(hashTable[i].key.equals(key)){
   				hashTable[i].value = value;
   				return;
   			}
   		}

   		hashTable[i] = new Node<V>(key, value);		// Insert new node with key and value
   		hashTable[i].key = key;
        hashTable[i].value = value;

   		if(keyValPairs == 0){	// If head is null (If there are no pairs)
   			head = hashTable[i];
   			tail = head;
   			curr = head;
   		}else{
   			tail.next = hashTable[i];
   			hashTable[i].prev = tail;
   			tail = hashTable[i];
   		}

   		keyValPairs++;	// Increment amount of pairs after put()
   	}

   	/**
	* get() if int argument is passed in
	* Convert to string
	* @param key
	* @return get(key converted to string)
	*/
   	public V get(int key)
   	{
        return get(String.valueOf(key));	// String.valueOf() converts int to String and pass into get()
    }

    /** Adopted from Lab5
	* get() if String argument is passed in and search hashTable for match
	* @param key
	* @return Value of passed in key, return null if no value exists
	*/
	public V get(String key)
	{
		for (int i = hash(key); hashTable[i] != null; i = (i + 1) % M){	// Iterate and use hash function to find value of a certain key
			if (hashTable[i].key.equals(key)){
				return hashTable[i].value;
			}
		}
    	return null;
  	}

  	/** Modified from Lab5
	* rehash() a node while keeping it in place in the linked list
	* @param rehashNode
	*/
	private void rehash(Node<V> rehashNode)
	{
		System.out.println("\t\tKey " + rehashNode.key + " rehashed...\n");	// Print statement according to output
		if (rehashNode.value == null){
			unset(rehashNode.key);		// Delete certain key if value is null
		}

		int i;
		for (i = hash(rehashNode.key); hashTable[i] != null; i = (i + 1) % M){	// Iterate and use hash function to set new value
			if (hashTable[i].key.equals(rehashNode.key)){
                hashTable[i].value = rehashNode.value;
                return;
            }
		}
		hashTable[i] = rehashNode;
    }

    /** Modified from Lab5
	* resize() if 50% full, create new HybridSymbolTable with parameter
	* @param capacity
	*/
    private void resize	(int capacity){
    	System.out.println("\t\tSize: " + keyValPairs + " -- resizing array from " + M + " to " + (M*2));	// Print statement according to output
	    HybridSymbolTable<V> resizeArray = new HybridSymbolTable<V>(capacity);

	    //rehash the entries in the order of insertion
	    Node<V> current = head;		
	    while(current != null){		// If cursor node is not null, copy each node into new hashTable from resizeArray
	        resizeArray.put(current.key, current.value);
	        current = current.next;
	    }
	    hashTable = resizeArray.hashTable;
	    head    = resizeArray.head;
	    tail    = resizeArray.tail;
	    curr 	= resizeArray.curr;
	    M       = resizeArray.M;
  	}

    /**
	* unset() if int argument is passed in
	* Convert to string
	* @param key
	*/
    public void unset(int key)
    {
        unset(String.valueOf(key));	// String.valueOf() converts int to String and pass into unset()
    }

  	/** Modified from Lab5
	* unset() deletes key and value pair from the symbol table
	* Rehash() all keys into the same cluster
	* @param key
	*/
  	public void unset(String key)
  	{
  		if(get(key) == null){		// If nothing to delete, quality check
  			return;
  		}

  		int i = hash(key);
  		while (!hashTable[i].key.equals(key)) {
            i = (i + 1) % M;		// Use hash function to find deleteNode
        }

        Node<V> deleteNode = hashTable[i];
       	hashTable[i] = null; 
	
		if(deleteNode.value == head.value && deleteNode.value == tail.value){	// If deleteNode is head or tail
	        head = null;
	        tail = null;
	    }else if(deleteNode.value == head.value){ 	// If deleteNode is head
	        deleteNode.next.prev = null;
	        head = head.next;
	        deleteNode.next = null;

	    }else if(deleteNode.value == tail.value){	// If deleteNode is tail
	        tail.prev.next = null;
	        tail = tail.prev;
	        tail.next = null; // Efficiency
	        // deleteNode.prev = null;
	    }else{										// deleteNode is an inner Node
	        deleteNode.prev.next = deleteNode.next;
	        deleteNode.next.prev = deleteNode.prev;
	        // deleteNode.prev = null;
	        // deleteNode.next = null;
	    }

	    i = (i + 1) % M;		// Rehash the keys in the same cluster
	    while (hashTable[i] != null) {
	      Node<V> rehashNode = hashTable[i];	// Delete and reinsert
	      hashTable[i] = null;
	      rehash(rehashNode);
	      i = (i + 1) % M;
	    }

	    keyValPairs--;		// Decerement amount of pairs
	    if (keyValPairs > 0 && keyValPairs <= M/8){
	    	resize(M/2);	// Resize array by half after deleting
	    }

  	}

  	/** 
  	* each() method, follows Pair class and uses curr node to evaluate Pair
	* @return key and value Pair, returns null when end of the list is reached
	*/
  	public Pair<V> each()
  	{
		if (curr == null){
			return null;
		}
		Pair<V> nextPair = new Pair<V>(curr.key, curr.value);		// Use pair class to create new pair through key and value
		curr = curr.next;
		return nextPair;
	}

	/** 
	* keys() method returns keysList ArrayList of keys
	* @return ArrayList of keys
	*/
	public ArrayList<String> keys()
	{
		ArrayList<String> keysList = new ArrayList<String>(keyValPairs);
		Node<V> cursorNode = head;
		for (int i = 0; i < keyValPairs; i++){
			keysList.add(cursorNode.key);		// Add only key to the ArrayList
			cursorNode = cursorNode.next;		// Increment cursor
		}
			return keysList;	// Return ArrayList after creating only key list
	}

	/** values() method returns valuesList ArrayList of values
	* @return ArrayList of values
	*/
	public ArrayList<V> values()
	{
		ArrayList<V> valuesList = new ArrayList<V>(keyValPairs);
		Node<V> cursorNode = head;
		for (int i = 0; i < keyValPairs; i++){
			valuesList.add(cursorNode.value);	// Add only value to the ArrayList
			cursorNode = cursorNode.next;		// Increment cursor
		}
		return valuesList;		// Return ArrayList after creating only value list
	}

	/**
	* Prints the hashTable and its contents
	*/
	public void showTable()
	{
		System.out.println("\tRaw Hash Table Contents:");
		int i = 0;
		while(i < hashTable.length){
			if(hashTable[i] != null){
				System.out.println(i + ": Key: " + hashTable[i].key + " Value: " + hashTable[i].value);	// Print the associated key and value
				i++;
			}else{
				System.out.println(i + ": null");	// Otherwise print null
				i++;
			}
		}
	}

	/**
	* sort() the hashTable using Comparable interface through value and then sets indices 0 to .size()
	* StringBuilder is Comparable in Java 12. This made it difficult to test this try catch block that I implemented
	*/
	@SuppressWarnings("unchecked")
    public <T extends Comparable<? super T>> void sort()	// Extends comparable as needed by instructions
    {	
    	try{
	        ArrayList<T> sortList = (ArrayList<T>)values();
	        Collections.sort(sortList);		// Sort the value list before adding to new HybridSymbolTable
	        HybridSymbolTable<V> sortPHP = new HybridSymbolTable<>(M);

	        int i = 0;
	        while(i < sortList.size()){
	        	sortPHP.put(i, (V)sortList.get(i));		// Add value to new HybridSymbolTable
	        	i++;
	        }

	        M = sortPHP.M;		// Equalize everything else to new M, head, tail, cursor and hashTable
	        head = sortPHP.head;
	        tail = sortPHP.tail;
	        curr = head;
	        hashTable = sortPHP.hashTable; 
	    }catch(Exception e){	// If not comparable, print Exception statement as needed by instructions
	    	System.out.println("HybridSymbolTable values are not Comparable -- cannot be sorted");
	    }
    }

   	/**
	* asort() the hashTable using Comparable interface through value, keeps indices intact
	*/
    @SuppressWarnings("unchecked")
    public <T extends Comparable<? super T>> void asort()
    {
        ArrayList<KeyValNode<T>> aSortList = new ArrayList<>();		// Use KeyValNode to make new ArrayList
        Node<V> aSortCurr = head;	// Set cursor to head
        while (aSortCurr != null) {
            KeyValNode<T> coNode = new KeyValNode();	// Create new KeyValNode by iterating over HybridSymbolTable
            coNode.key = aSortCurr.key;	// Set new Node
            coNode.value = (T)aSortCurr.value;
            aSortList.add(coNode);
            aSortCurr = aSortCurr.next;
        }
        Collections.sort(aSortList);		// Sort it after indices intact
        HybridSymbolTable<V> coNode = new HybridSymbolTable<>(M);

        int i = 0;
        while(i < aSortList.size()){
            coNode.put(aSortList.get(i).key, (V)aSortList.get(i).value);
            i++;
        }

        head = coNode.head;	// Equalize everyting else to new head, tail, cursor and hashTable 
        tail = coNode.tail;
        curr = head;
        hashTable = coNode.hashTable;
    }

	/**
	* Transposes keys and values of original array into new HybridSymbolTable
	* Checks if value is a String, if not, throw ClassCastException
	* @return flipped HybridSymbolTable
	*/
	public HybridSymbolTable<String> array_flip()
	{
		if(!(head.value instanceof String)){
			throw new ClassCastException("Cannot convert class java.lang.Integer to String");	// Print required Exception by instructions
		}else{
			HybridSymbolTable<String> flipPHP = new HybridSymbolTable<String>(M);
			Node<V> flipNode = head;
			while(flipNode != null){
				flipPHP.put((String)flipNode.value, flipNode.key);	// Pass in value first then key, this flips it
				flipNode = flipNode.next;	// Increment until the end of the HybridSymbolTable is reached
			}
			return flipPHP;
		}
	}

	/**
	* reset() curr to head
	*/
	public void reset()
	{
		curr = head;	// Start at the beginning
	}

	/**
	* Length of hashTable
	* @return length in integer
	*/
	public int length()
    {
        return keyValPairs;		// keyValPairs is the amount of Pairs, which should be equal to the length()
    }

   	/** Adopted from Lab5
	* Simple Node class
	* Initializes key, value and action calls
	*/
	private class Node<V>
	{
		private String key;
	    private V value;
	    private Node<V> next;
	    private Node<V> prev;

	    Node(String key, V value)
	    {
	      this(key, value, null, null);
	    }

	    Node(String key, V value, Node<V> next, Node<V> prev)
	    {
	      this.key = key;
	      this.value = value;
	      this.next = next;
	      this.prev = prev;
	    }
	}	

	/** 
	* Pair class
	* Used to associate key and value
	*/
	public static class Pair<V> 
	{
		public String key;
		public V value;
		
		public Pair(String key, V value)
		{
			this.key = key;
			this.value = value;
		}
	}

	/**
	* Used in asort(), allows for key and value pair to be unchanged
	*/
    private static class KeyValNode<T extends Comparable<? super T>> implements Comparable<KeyValNode<T>>
    {
        String key;		// Uses the same variables as basic Node
        T value;
        public int compareTo(KeyValNode<T> kvNode) {		// compareTo implemented in new KeyValNode class
             return this.value.compareTo(kvNode.value);
        }
    }

   	/** Adopted from Lab5
	* Hash function for keys
	* @return value between 0 and M-1
	*/
	private int hash(String key)
	{
    	return (key.hashCode() & 0x7fffffff) % M;
  	}

  	/** Bonus method array_sum()
	* Calculates sum of values in the HybridSymbolTable
	* @return int of the sum of all values
	*/
    public int array_sum()
    {	
    	int sum = 0;
		ArrayList<V> sumList = values();
        for(int i = 0; i < sumList.size(); i++){
            sum += (int)sumList.get(i);
        }
        return sum;
    }


} // End of class HybridSymbolTable<V>