package br.com.recatalog.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ParserInventory {
	Map<String,Map<String,Integer>> inventory; 
	
	public ParserInventory() {
		inventory =  new LinkedHashMap<String,Map<String,Integer>>();
	}
	
	public Map<String,Map<String,Integer>> getInventory(){
		return inventory;
	}
	
	public int add(String id, String elementClass) {
		// id = CG_ATTR_NORMAL
		// elementClass = DEFVAR	
		inventory.putIfAbsent(elementClass, new LinkedHashMap<String,Integer>());
		Map<String,Integer> statElementClass = inventory.get(elementClass);
		statElementClass.putIfAbsent(id, 0);
		Integer elementClassNum = statElementClass.get(id);
		statElementClass.put(id, ++elementClassNum);
		return elementClassNum;
	}
	
	public boolean hasDuplicated() {
		for(Map<String,Integer> m : inventory.values()) {
		 for(Entry<String, Integer> e : m.entrySet()) {
			 if(e.getValue() > 1) return true;
		 }}
		return false;
	}
	
	public Map<String,Integer> sumByelementClass() {
		Map<String,Integer> totalByElement = new LinkedHashMap<String,Integer>();
		for(Entry<String, Map<String, Integer>> e : inventory.entrySet()) {
			int total = 0;
			for(Entry<String, Integer> k : e.getValue().entrySet()) {
				total = total + k.getValue();
			}
			totalByElement.put(e.getKey(), total);
		}
		return totalByElement;
	}
	
	public void print() {
		System.out.println(getInventory());
		System.out.println(sumByelementClass());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj.getClass().getName().equals(getClass().getName()))) return false;
		
//		String other = ((ParserInventory)obj).getInventory().toString();
//		return getInventory().toString().equals(other);
		ParserInventory other = (ParserInventory)obj;
		return getInventory().equals(other.getInventory());		
	}
}