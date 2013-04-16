package com.sugarcrm.voodoo.datasource;

import com.sugarcrm.voodoo.configuration.Configuration;


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
