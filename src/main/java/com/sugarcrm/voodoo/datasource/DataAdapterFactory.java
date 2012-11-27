package com.sugarcrm.voodoo.datasource;

import java.util.Properties;

/**
 *
 *
 */
public class DataAdapterFactory {
	public enum DataAdapterType {CSV, XML;};
	protected DataAdapterType adpaterType;

	public DataAdapterFactory(DataAdapterType adapterType) {
		this.adpaterType = adapterType;
	}

	public DataAdapter createDataAdapter() {
		if (adpaterType == DataAdapterType.XML) {
			return new XmlDataAdapter();
		} else {
			return new CsvDataAdapter();   // CSV is default 
		}
	}
}
