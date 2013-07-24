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

import com.sugarcrm.candybean.configuration.Configuration;


/**
 *  DataAdapterFactory is a public interface used to get various DataAdapters. 
 *
 */
public class DataAdapterFactory {
	public enum DataAdapterType { CSV, XML };
	protected Configuration config;

	public DataAdapterFactory(Configuration config) {
		this.config = config; 
	}
	
	public DataAdapter createDataAdapter(DataAdapterType adapterType) {
		
		if (adapterType == DataAdapterType.CSV) {
			return new CsvDataAdapter(config);   
		} else if (adapterType == DataAdapterType.XML) {
			return null;
		}
		else {
			throw new RuntimeException();
		}
	}
}
