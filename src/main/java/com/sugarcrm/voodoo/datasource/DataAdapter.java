package com.sugarcrm.voodoo.datasource;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import org.apache.commons.io.filefilter.RegexFileFilter;

import com.sugarcrm.voodoo.configuration.Configuration;

/**
 * DataAdapter is an "interface" class a client uses to convert csv, xml, etc.
 * into a HashMap of DataSource
 * 
 */
public abstract class DataAdapter {
	public enum Selection {ALL, SINGLE};
	static Configuration configuration;
	static String dataBasePath;
	static Selection selection = Selection.ALL;

	public DataAdapter(Configuration config) {
		DataAdapter.configuration = config;
	}

	public DataAdapter setDataBasePath(String dataBasePath) {
		DataAdapter.dataBasePath = dataBasePath;
		return this;
	}
	
	public abstract HashMap<String, DataSource> getData(String testData);
	public abstract HashMap<String, DataSource> getData(String testData, DataAdapter.Selection select);

	protected static File[] getAllFilesBasedOnPattern(String fileFullDirPath,
	//protected File[] getAllFilesBasedOnPattern(String fileFullDirPath,
			String testData, String ext) {
		File dir = new File(fileFullDirPath);
		//File[] files = dir.listFiles();
		File[] files;

		String pattern = testData + "." + ext + "|" + testData + "_[^_]+" + "."
				+ ext;
		
		if (selection == DataAdapter.Selection.SINGLE) {
			pattern = testData + "." + ext;
		}
		
		// System.out.println("\nFiles that match regular expression: " +
		// pattern);
		FileFilter filter = new RegexFileFilter(pattern);
		files = dir.listFiles(filter);

		return files;
	}
}