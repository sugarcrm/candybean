package com.sugarcrm.system.voodoo.tests;

import java.util.List;

import com.sugarcrm.voodoo.datasource.DataAdapter;
import com.sugarcrm.voodoo.datasource.DataAdapterFactory;
import com.sugarcrm.voodoo.datasource.DataAdapterFactory.DataAdapterType;
import com.sugarcrm.voodoo.datasource.DataSource;
import com.sugarcrm.voodoo.datasource.FieldSetList;
import com.sugarcrm.voodoo.datasource.FieldSet;

/**
 *
 *
 */
public class CsvDataAdapterTest {
	
	public CsvDataAdapterTest() {
	}

	public static void main(String[] args) {
		DataAdapterFactory adapterFactory;
		DataAdapter dataAdapter;
		
		printCurrentDir();
		
		adapterFactory = new DataAdapterFactory(DataAdapterType.CSV);
		dataAdapter = adapterFactory.createDataAdapter();
		
		List<DataSource> dataSourceList = dataAdapter.getData("Accounts_0198");
		printDataSource(dataSourceList);
		dataSourceList = dataAdapter.getData("Countries");
		printDataSource(dataSourceList);
	}
	
	private static void printCurrentDir() {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			System.out.println("main(): Current dir using getCanonicalPath():" + current);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String currentDir = System.getProperty("user.dir");
		System.out.println("main(): Current dir using System.getProperty:" + currentDir);
	}
	
	private static void printDataSource(List<DataSource> dataSourceList) {
		for (DataSource ds : dataSourceList) {
			//System.out.println(dataSourceList.toString());
			System.out.println("main(): printDataSourceData(): dataSource filename = " + ds.getFilename());
			FieldSetList fieldSetList = ds.getData();   
			//System.out.println(dataSourceList.toString());
			printDataSourceFieldSet(fieldSetList);
		}
	}
	
	private static void printDataSourceFieldSet(FieldSetList fieldSetList) {
		System.out.println("main(): printDataSourceFieldSet(): fsList.size() = " + fieldSetList.size()); 
		for (FieldSet fs : fieldSetList) { 
			for (String key : fs.keySet()) {
			    System.out.println(key + " : " + fs.get(key));
			}
		}
	}
}