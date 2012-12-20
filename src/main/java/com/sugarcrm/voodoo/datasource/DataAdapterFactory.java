package com.sugarcrm.voodoo.datasource;

import java.util.Properties;

import com.sugarcrm.voodoo.configuration.Configuration;


/**
 *  DataAdapterFactory is a public interface used to get various DataAdapters. 
 *
 */
public class DataAdapterFactory {
	public enum DataAdapterType { CSV, XML };
	protected Properties props;

	public DataAdapterFactory(Properties props) {
		this.props = props; 
	}
	
	public DataAdapter createDataAdapter(DataAdapterType adapterType) {
		
		if (adapterType == DataAdapterType.CSV) {
			return new CsvDataAdapter(props);   
		} else if (adapterType == DataAdapterType.XML) {
			//return new CsvDataAdapter(props);  
			return null;
		}
		else {
			throw new RuntimeException();
		}
	}
}
