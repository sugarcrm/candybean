package com.sugarcrm.voodoo.datasource;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.apache.commons.io.filefilter.RegexFileFilter;

/**
 *  DataAdapter is an "interface" class a client uses to convert csv, xml, etc. formatted contents into a list of DataSource 
 *
 */
public abstract class DataAdapter {
	public DataAdapter() {
	}

	public abstract List<DataSource> getData(String testData);   
	
	protected static File[] getAllFilesBasedOnPattern(String fileFullDirPath, String testData, String ext) {
		File dir = new File(fileFullDirPath);
		File[] files = dir.listFiles();

		String pattern = testData + "." + ext + "|" + testData + "_[^_]+" + "." + ext;
		//System.out.println("\nFiles that match regular expression: " + pattern);
		FileFilter filter = new RegexFileFilter(pattern);
		files = dir.listFiles(filter);
		
		return files;	
	}
}