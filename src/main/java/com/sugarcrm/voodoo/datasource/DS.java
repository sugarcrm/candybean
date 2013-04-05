package com.sugarcrm.voodoo.datasource;

import java.util.HashMap;
import java.util.Properties;

import com.sugarcrm.voodoo.datasource.DataAdapter;
import com.sugarcrm.voodoo.datasource.DataAdapterFactory;
import com.sugarcrm.voodoo.datasource.DataAdapterFactory.DataAdapterType;
//import com.sugarcrm.voodoo.datasource.DataSource_bak;
import com.sugarcrm.voodoo.datasource.FieldSet;
import com.sugarcrm.voodoo.datasource.DataSource;
import com.sugarcrm.voodoo.configuration.TransientProperties;

public class DS {
	public enum DataType { CSV, XML };
	String testName;
	DataAdapterFactory adapterFactory;
	DataAdapter dataAdapter;
	String propName;
	String propValue;
	TransientProperties config = null;
	
	public DS(String testName) {
		this.testName = testName;
	}
	
	public void init(DataType dataType, String propName, String propValue) {
		this.propName = propName; 
		this.propValue = propValue; 
		config = new TransientProperties(testName);
		config.setProperties(this.propName, this.propValue);
		Properties myProps = config.getProperties();
		
        DataAdapterType type = getDataType(dataType);
		adapterFactory = new DataAdapterFactory(myProps);
		dataAdapter = adapterFactory.createDataAdapter(type);
	}
	
	/**
     * @param dataSet is a pattern string indicating the data files 
     *        whose contents are to be converted to the desired format
     * @return A DataSource corresponding to a data file with name that 
     *         matches most closely to dataSet string 
     */
	public DataSource getDataSource(String dataSet) {
		// Eg of a dataSet is "Account_0001"
		HashMap<String, DataSource> dataSourceHashMap = dataAdapter
				.setDataBasePath(propName).getData(dataSet,
						DataAdapter.Selection.SINGLE);
		DataSource ds = dataSourceHashMap.get(dataSet);
		//printDataSourceSingle(ds);
		//printDataSource(dataSourceHashMap);
		
		return ds;
	}
	
	/**
     * @param dataSet is a pattern string indicating the data files whose 
     *        contents are to be converted to the desired format
     * @return A HashMap of DataSource's. Each key/value corresponds to a 
     *         data file. The HashMap uses the data file name without the 
     *         extension as the key, the value is a DataSource
     *         
     */
	public HashMap<String, DataSource> getDataSources(String dataSet) {
		// Eg of a dataSet is "Account_0001"
		HashMap<String, DataSource> dataSourceHashMap = dataAdapter
				.setDataBasePath(propName).getData(dataSet);
		//printDataSource(dataSourceHashMap);
		
		return dataSourceHashMap;
	}
	
	public void cleanup() {
		config.cleanup();
	}
	
	private DataAdapterType getDataType(DataType dataType) {
		switch (dataType) {
		case CSV: 
		default:
			return DataAdapterType.CSV;
		}
	}

	private static void printDataSourceSingle(DataSource ds) {
		System.out
				.println("main(): printDataSourceSingle(): dataSource filenameNoExt = "
						+ ds.getFilename());
		//printDataSourceFieldSet(fieldSetList);
		printDataSourceFieldSet(ds);
	}
	
	private static void printDataSource(
			HashMap<String, DataSource> dataSourceHashMap) {
		for (String filenameNoExt : dataSourceHashMap.keySet()) {
			System.out
					.println("main(): printDataSourceData(): dataSource filenameNoExt = "
							+ filenameNoExt);
			DataSource ds = dataSourceHashMap.get(filenameNoExt);
			printDataSourceFieldSet(ds);
		}
	}

	private static void printDataSourceFieldSet(DataSource ds) {
		System.out
				.println("main(): printDataSourceFieldSet(): fsList.size() = "
						+ ds.size());
		for (FieldSet fs : ds) {
			for (String key : fs.keySet()) {
				System.out.println(key + " : " + fs.get(key));
			}
		}
	}
}
