/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.datasource;

import java.util.LinkedHashMap;


/**
 * FieldSet is the basic type returned by the DataAdapter from converting the original csv, xml, etc. 
 *
 */
public class FieldSet extends LinkedHashMap<String, String> {
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