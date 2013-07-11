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

import java.util.HashMap;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DataAdapter;
import com.sugarcrm.candybean.datasource.DataAdapterFactory;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.DataAdapterFactory.DataAdapterType;

public class DS {
	public enum DataType { CSV, XML };
	String testName;
	DataAdapterFactory adapterFactory;
	DataAdapter dataAdapter;
	String propKey;
	String propValue;
	Configuration config;
	
	public DS(String testName) {
		this.testName = testName;
	}
	
	public void init(DataType dataType, String propKey, String propValue) {
		this.propKey = propKey; 
		this.propValue = propValue; 
		config = new Configuration();
		config.setProperty(propKey, propValue);
//		config.createFile(System.getProperty("user.dir") + File.separator + "TemporaryConfigFiles" + File.separator + testName + ".properties");
		
        DataAdapterType type = getDataType(dataType);
		adapterFactory = new DataAdapterFactory(config);
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
				.setDataBasePath(propKey).getData(dataSet,
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
				.setDataBasePath(propKey).getData(dataSet);
		//printDataSource(dataSourceHashMap);
		
		return dataSourceHashMap;
	}
	
	public void cleanup() {
//		config.deleteFile();
	}
	
	private DataAdapterType getDataType(DataType dataType) {
		switch (dataType) {
		case CSV: 
		default:
			return DataAdapterType.CSV;
		}
	}

//	private static void printDataSourceSingle(DataSource ds) {
//		System.out
//				.println("DS: printDataSourceSingle(): dataSource filenameNoExt = "
//						+ ds.getFilename());
//		//printDataSourceFieldSet(fieldSetList);
//		printDataSourceFieldSet(ds);
//	}
//	
//	private static void printDataSource(
//			HashMap<String, DataSource> dataSourceHashMap) {
//		for (String filenameNoExt : dataSourceHashMap.keySet()) {
//			System.out
//					.println("DS: printDataSourceData(): dataSource filenameNoExt = "
//							+ filenameNoExt);
//			DataSource ds = dataSourceHashMap.get(filenameNoExt);
//			printDataSourceFieldSet(ds);
//		}
//	}
//
//	private static void printDataSourceFieldSet(DataSource ds) {
//		System.out
//				.println("DS: printDataSourceFieldSet(): fsList.size() = "
//						+ ds.size());
//		for (FieldSet fs : ds) {
//			for (String key : fs.keySet()) {
//				System.out.println(key + " : " + fs.get(key));
//			}
//		}
//	}
}
