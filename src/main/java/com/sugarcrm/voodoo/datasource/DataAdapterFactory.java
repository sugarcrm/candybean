package com.sugarcrm.voodoo.datasource;


/**
 *  DataAdapterFactory is a public interface used to get various DataAdapters. 
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
			//return new XmlDataAdapter();
			return null;   // TODO 
		} else {
			return new CsvDataAdapter();   // CSV is default 
		}
	}
}
