package br.com.recatalog.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory {
	
	List<InventoryItem> itens;

	public List<InventoryItem> getItens() {
		return itens;
	}
	
	public void setItens(List<InventoryItem> itens) {
		this.itens = itens;
	}

	public Inventory() {
		this.itens = new ArrayList<>();
	}
	
	public void add(String id, String value) {
		  Optional<InventoryItem> o = itens.stream()
										.filter(item -> item.getId().equals(id))
										.findFirst();
			
			o.ifPresentOrElse(item -> item.getValues().add(value) ,
					          () -> this.getItens().add(new InventoryItem(id,value)));
		}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itens == null) ? 0 : itens.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inventory other = (Inventory) obj;
		if (itens == null) {
			if (other.itens != null)
				return false;
		} else if (!itens.equals(other.itens))
			return false;
		return true;
	}

	public static void main(String args[]) {
		Inventory inventory = new Inventory();
		Inventory inventory1 = new Inventory();
		
		inventory.add("REFERENCE","REF01");
		inventory.add("REFERENCE","REF02");
		inventory.add("REFERENCE1","REF01");
		
		inventory1.add("REFERENCE","REF01");
		inventory1.add("REFERENCE","REF02");
		inventory1.add("REFERENCE1","REF01");

		System.out.println(inventory.getItens());
		System.out.println(inventory1.getItens());

		System.out.println(inventory.equals(inventory1));
	}	
}