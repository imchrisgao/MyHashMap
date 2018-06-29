package myhashmap;
//the put uses the get //no it doesn'ts

import java.util.Iterator; 

interface MyHashMapInterface{
	void put(String k, int v);
	int get(String k);
	void remove(String k);
	//Iterator iterator();
}

class Entry{
	final String key;
	private int value;
	final int hash; //this is how we find an entry
	Entry next; //make this private later
	
	Entry(String key, int value){
		this.key = key;
		this.value = value;
		this.hash = key.hashCode();
		this.next = null;
	}
	
	void setValue(int v){ //because only way to manipulate value is to replace it with new (same key, new value)
		//Only time I need to call this is if I'm replacing old value with new value
		this.value = v;
	}
	
	int getValue(){
		return this.value;
	}
	
	public String toString(){ //just to check if Entry works; DO NOT USE TO RETRIEVE ENTRY FROM MAP
		return "Key: " + key + ", Value: " + value;// + ",Hash: " + hash + ", Next: " + "this.next.key";
	}
	
	@Override
	public boolean equals(Object o){ //compareTo would allow me to use >, <, or ==; i.e. not the same as ==
		Entry e = (Entry)(o);
		if(this.key == e.key){ //I want to be able to replace old value with new value
			return true;
		} else{
			return false;
		}
	}
}

public class MyHashMap implements MyHashMapInterface{
	//ARRAYS HAVE A TOSTRING METHOD
	private Entry[] e; //array-list or array? but I can resize with Array.copyOf()
	private int counter;
	private final double loadFact;
	
	MyHashMap(int cap){ //don't test with anything larger than 10
		this.e = new Entry[cap];
		counter = 0;
		loadFact = 0.8;
	}
	
	private int index(int hash){ //calculates an index based on hashCode and capacity of array
		return hash % e.length; 
	}
	
	int hashSize(){ //use in conjunction with loadFact
		return counter;
	}

	//***********************
	//Unfinished methods
	Iterator<Entry> iterator(){ //enhanced for loop doesn't call this? why?
		//is signature right? does MyIterator need to be Iterator?
		return new MyIterator();
	}

	
	//***********************
	//finished...I think
	public int get(String k) throws NullPointerException{
		int i = index(k.hashCode());
		Entry search = e[i];
		
		if(e[i] == null){
			System.out.println("Null pointer in get method");
			throw new NullPointerException();
		} 
		
		while(k != search.key){ //haven't tested for "key that doesn't exist"
			search = search.next;
			if(search == null){
				System.out.println("Null pointer in get method");
				throw new NullPointerException();
			}
		} 
		return search.getValue();
	}
	
	public void put(String k, int v){//uses built in hashCode()	
		if(counter >= (int)(loadFact * e.length)){ //check for rehash before inserting
			this.rehash();
		}
		
		Entry newEntry = new Entry(k, v);
		int i = index(newEntry.hash); //rehashing should be automatic if I'm checking index here
		
		if(e[i] == null){
			e[i] = newEntry;
			counter++;
			return;
		} 
		
		Entry search = e[i]; //is this only possible because search references the actual entry?
		if(search.key == k){ //need to explicitly search first Entry for duplicate
			search.setValue(v);
			return;
		}

		while(search.next != null){ //I need to insert using search.next, not search itself
			if(k == search.key){ //counter shouldn't increment if I'm just changing value
				search.setValue(v);
				return;
			}
			search = search.next;
		} 
		counter++;
		search.next = newEntry;
	}
	
	public void remove(String k) throws NullPointerException{ //not that simple
		int i = index(k.hashCode());

		
		if(e[i] == null){
			System.out.println("Null pointer in remove method"); 
			throw new NullPointerException(); //try and modify this later so it doesn't stop whole program
			//maybe just empty return?
		} 
		if(e[i].key == k){
			e[i] = e[i].next;
			counter--;
			return;
		}
		
		Entry search = e[i];		
		while(search.next.key != k){ //need to search next, not search itself
			if(search.next == null){ //then Entry doesn't exist //next, not current
				System.out.println("There's going to be a Null pointer in remove method");
				throw new NullPointerException();
			}
			search = search.next;
		}
		counter--;
		search.next = search.next.next;
	}
	
	class MyIterator implements Iterator<Entry>{ //check generic type with someone else
		int current;
		Entry depth; 
		
		MyIterator(){
			current = 0;
			depth = e[0];
		}
	
		@Override
		public boolean hasNext() {
			if(current < hashSize()){ 
				return true;
			}
			return false;
		}
	
		@Override
		public Entry next() {
			while(e[current] == null){ 
				current++;
				depth = e[current];
			}
			Entry temp = depth;
			if(depth.next == null){
				current++;
				if(current<e.length){
					depth = e[current]; //this can throw array out of bounds...
				}
			} else{
				depth = depth.next;
			}
			return temp;
		}
		
	}
	
	void rehash(){ //this should be an automatic process; fix later
		MyHashMap temp = new MyHashMap((int)(e.length*4));
		//for(Entry en: this.e){ //en is type Entry
		//	temp.put(en.key, en.getValue());
		//}
		Iterator<Entry> i = iterator(); //only iterates through e
		while(i.hasNext()){
			Entry tempEntry = i.next(); //why did this fix everything
			temp.put(tempEntry.key, tempEntry.getValue());
		}
		this.e = temp.e;
	}
	
	//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
	//Print whole map
	void printHashMap(){
		for(int i=0; i<e.length; i++){
			System.out.print("**|| " + e[i] + " ||**");
		}
	}
	
	//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
	//-------------------------------------------
	public static void main(String[] args){
		try{
			MyHashMap m = new MyHashMap(2);
			m.put("Key1", 15);
			System.out.println(m.hashSize());
			m.printHashMap();
			
			System.out.println();
			System.out.println();
			
			m.put("Key1", 18);
			System.out.println(m.hashSize());
			m.printHashMap();
			
			System.out.println();
			System.out.println();
			
			m.put("Key2", 14);
			System.out.println(m.hashSize());
			m.printHashMap();
			
			System.out.println();
			System.out.println();
			
			m.put("Key3", 8);
			System.out.println(m.hashSize());
			m.printHashMap();
			
			System.out.println();
			System.out.println();
			
			m.put("Key4",  100);
			System.out.println(m.hashSize());
			m.printHashMap();
			
			System.out.println();
			System.out.println(m.get("Key4"));
			System.out.println();
			
			m.put("Key5", 1);
			System.out.println(m.hashSize());
			m.printHashMap();
			
			System.out.println();
			System.out.println(m.get("Key5"));
			
			System.out.println();
			System.out.println();
			
			m.remove("Key5");
			m.printHashMap();
			System.out.println();
			System.out.println(m.get("Key5"));
			
		} catch(NullPointerException e){
			System.out.println("Somewhere, a key probably doesn't exist");
		} finally{
			System.out.println();
			System.out.println("I think I'm done");
		}
		
	}
	//-------------------------------------------
}
