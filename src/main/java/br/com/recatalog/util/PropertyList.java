package br.com.recatalog.util;

import java.security.InvalidParameterException; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;

public class PropertyList {
	private HashMap<String,Object> properties;
	
	LinkedList<PropertyList> nestedProperties;
	
	public PropertyList(){
		properties = new HashMap<String,Object>();
		nestedProperties = new LinkedList<PropertyList>();
	}
	
	public PropertyList(PropertyList pList){
		properties = new HashMap<String,Object>();
		for ( Entry<String,Object> e   : pList.getProperties().entrySet()) {
			properties.put(e.getKey(), e.getValue());
		}
	}
//	
//	public void addProperty(String propertykey, Object propertyValue) {
//		properties.put(propertykey, propertyValue);
//	}
	
	public PropertyList addProperty(String propertykey, Object propertyValue) {
		properties.put(propertykey, propertyValue);
		return this;
	}
	
	public void addPropertyInNested(String propertykey, Object propertyValue, String nestedKey) {
        if(nestedKey == null) {
        	addProperty(propertykey,propertyValue);
        	return;
        }
        Object nestedProperty = properties.get(nestedKey);
        if(nestedProperty != null)
        	((PropertyList)properties.get(nestedKey)).addProperty(propertykey, propertyValue);
        else {
        	BicamSystem.printLog("DEBUG", "NULL NESTED PROPERTY");
        }
	}
	
	public void removeProperty(String propertyDescriptionP) {
		properties.remove(propertyDescriptionP);
	}	
	
	public Object getProperty(String propertyDescriptionP) {
		return properties.get(propertyDescriptionP);
	}
	
	public Object getNotNullProperty(String propertyDescriptionP) {
		Object value = getProperty(propertyDescriptionP);
		if(value != null) return value;
		BicamSystem.printLog("ERROR", "INVALID NULL PROPERTY VALUE OF KEY: " + propertyDescriptionP);
		return null;
	}	
	
	public PropertyList getNestedProperty(String nestedProperty) {
		return (PropertyList) getProperty(nestedProperty);
	}
	
	public boolean hasProperty(String _keyProp, String _valProp, boolean ..._ignoreCase ) {
		if(getProperty(_keyProp) != null && getProperty(_keyProp).getClass().getSimpleName().equalsIgnoreCase("String")){
			if(((String)getProperty(_keyProp)).equals(_valProp))
				return true;
			if(_ignoreCase.length > 0){
				if(_ignoreCase[0]  == true){
					if(((String)getProperty(_keyProp)).equalsIgnoreCase(_valProp))
						return true;
				}
			}
		}
		return false;
	}
	
	public Set<String> getKeys() {
		return properties.keySet();
	}
	
	public Collection<Object> getValues() {
		return properties.values();
	}
	
	public Set<Entry<String, Object>> getEntries() {
		return properties.entrySet();
	}	
	
	public boolean hasProperty(String _keyProp) {
		for(String key : properties.keySet()){
			if(_keyProp.equals(key)) return true;
		}
		return false;
	}	
	
	public Object getProperty(String _keyProp, String _valProp ) {
		if (!hasProperty(_keyProp,_valProp)) return null;
		return getProperty(_keyProp);
	}	

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> _prop) {
		this.properties = _prop;
	}
	
	public void setProperties(PropertyList pList) {
		for ( Entry<String,Object> e   : pList.getProperties().entrySet()) {
			properties.put(e.getKey(), e.getValue());
		}
	}
	
	public void clear() {
		properties.clear();
	}
	
	public Object mustProperty(String _key) {
		Object o = properties.get(_key);
		if(o == null) {
			try {
				throw new InvalidParameterException("PropertyList mustProperty");
			} catch (InvalidParameterException e) {
				BicamSystem.printLog("ERROR", "PROPERTY: '" + _key + "' CANT BE NULL");
			}
			finally {
				return null;
			}
		
		}
		return o;
	}	
	
	public PropertyList addNestedProperty(String _keyProp){
		PropertyList nestedProperties = (getProperty(_keyProp) instanceof PropertyList 
						? (PropertyList)getProperty(_keyProp) 
						: null);
		if(nestedProperties == null){
			nestedProperties = new PropertyList();
				addProperty(_keyProp, nestedProperties);
		}
		else {
			throw new RuntimeException("Error : Nested Property already defined: " 
					+ _keyProp);
		}
		return nestedProperties;
	}	
	
	/*
	 * 1  = property is nestedProperty
	 * 0  = property is not nestedProperty
	 * -1 = property not exist
	 */
	public int hasNestedProperty(String _keyProp){
		if(getProperty(_keyProp) == null){
			return  -1;
		}
		else if(getProperty(_keyProp) instanceof PropertyList){
			return 1;
		}
		else return 0;
	}
	
	public PropertyList addNestedProperty(String _keyProp, Boolean ..._append){
		PropertyList nestedProperties = (getProperty(_keyProp) instanceof PropertyList 
						? (PropertyList)getProperty(_keyProp) 
						: null);
		
		if(nestedProperties == null){
			nestedProperties = new PropertyList();
			if(_append.length > 0 && _append[0]==true)
				addProperty(_keyProp, nestedProperties,_append[0]);
			else
				addProperty(_keyProp, nestedProperties);
		}
		else if(_append.length > 0 && _append[0]==true) {
			//none
		}
		else {
			throw new RuntimeException("Error : Nested Property already defined: " 
					+ _keyProp);
		}
		return nestedProperties;
	}
	
	public void addProperty(String propertykey, Object propertyValue, Boolean ...append) {
        if(append.length > 0 && append[0] == true)
        	appendProperty(propertykey,propertyValue);
        else
		properties.put(propertykey, propertyValue);
	}
	
	public void addProperty(PropertyList _properties, String propertykey, Object propertyValue) {
		if(String.class.isInstance(propertyValue)) {
			_properties.addProperty(propertykey, propertyValue);
		}
		else {
		}
	}
	
	public int appendProperty(String propertyKey, Object propertyValue) {
		if(!properties.containsKey(propertyKey)) {
			properties.put(propertyKey, propertyValue);
			if(propertyValue instanceof ParserRuleContext){
				PropertyList prop = new PropertyList();
				ParserRuleContext prc = (ParserRuleContext)propertyValue;
				prop.addProperty("LINE", Integer.toString(prc.start.getLine()));
				prop.addProperty("START_INDEX", Integer.toString(prc.start.getStartIndex()));
				properties.put("CONTEXT_TOSTRING", prop);
			}			
			return 1;
		}
		else {
			if((properties.get(propertyKey) instanceof List)) {
				((ArrayList<PropertyList>)properties.get(propertyKey)).add((PropertyList) propertyValue);
			}
			else {
				ArrayList<PropertyList> list = new ArrayList<PropertyList>();
				list.add((PropertyList) getProperty(propertyKey));
				addProperty(propertyKey, list);
			}
		}
		return ((ArrayList)properties.get(propertyKey)).size();
	}	
	
	public int appendProperties(PropertyList _properties) {
		int appendedProperties = 0;
		if(_properties.size() == 0) return 0;
		for(Entry<String,Object> e : _properties.getEntries()) {
			properties.put(e.getKey(), e.getValue());
			appendedProperties++;
		}
		return appendedProperties;
	}
	
	
	
	
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for(String key : properties.keySet()){
			if(!sb.toString().equals("{")){
				sb.append(", ");
			}
			if(properties.get(key) != null) {
				sb.append(key + "=" + properties.get(key).toString());
			}
			else {
				sb.append(key + "=" + "NULL");
			}

//			if(properties.get(key) instanceof String){
//				sb.append(key + "=" + (String)properties.get(key));
//			}
//			else if ((properties.get(key) != null) &&   properties.get(key).getClass().getSuperclass().getSimpleName().endsWith("List")){
//				sb.append( key + "=" + ((ArrayList)properties.get(key)));
//			}			
//			else if ((properties.get(key) != null) && properties.get(key).getClass().getSuperclass().getSimpleName().endsWith("Set")){
//				sb.append( key + "=" + ((Set)properties.get(key)));
//			}
//			else if (properties.get(key) instanceof PropertyList){
//				sb.append(key + "=" + ((PropertyList)properties.get(key)).getProperties());
//			}
////			else if (properties.get(key).getClass().getName().startsWith("java.lang")) {
////				sb.append(key + "=" + properties.get(key).toString());
////			}
//			else {
//				if(properties.get(key) != null) {
//					sb.append("=" + properties.get(key).getClass() != null ?
//					key + "=" + properties.get(key).getClass().getName() : "NULL");
//				}
//				else{
//					sb.append(key + "=" + "NULL");
//				}
//			}
		}
		return sb.append("}").toString();
	}
	
	public  PropertyList getCopy(){
		PropertyList prop = new PropertyList();
		for(String key : this.getProperties().keySet()){
			prop.addProperty(key, this.getProperty(key));
		}
		return prop;
	}
	
	public int size() {
		return properties.size();
	}
	
	public static void main(String args[]) {
		testaNestedProperty();
	}
	
	private static void testaNestedProperty() {
		PropertyList properties = new PropertyList();
		properties.addNestedProperty("PROPERTIES");
		properties.getNestedProperty("PROPERTIES").addProperty("Prop01","VALUE01");
		properties.getNestedProperty("PROPERTIES").addProperty("Prop02","VALUE02");
		System.err.println(properties);
	}
}