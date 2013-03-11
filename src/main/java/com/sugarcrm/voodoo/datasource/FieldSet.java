package com.sugarcrm.voodoo.datasource;

import java.util.HashMap;


/**
 * FieldSet is the basic type returned by the DataAdapter from converting the original csv, xml, etc. 
 *
 */
public class FieldSet extends HashMap<String, String> {
	private static final long serialVersionUID = -6700016461709667889L;
	
	/**
	 * Creates deep copy of a FieldSet. All items will be cloned.
	 * @return - deepClone of a FieldSet
	 * @author mlouis
	 */
	public FieldSet deepClone() {
		FieldSet newHash = new FieldSet();
		for(String key : this.keySet()){
			newHash.put(key, this.get(key));
		}
		return newHash;
	}
} 