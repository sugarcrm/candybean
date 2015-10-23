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

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import com.sugarcrm.candybean.configuration.Configuration;

/**
 * CsvDataAdapter is used by client to convert csv files into a list of
 * DataSource.
 * 
 */
public class CsvDataAdapter extends DataAdapter {
	
	private static Logger log = Logger.getLogger(CsvDataAdapter.class.getSimpleName());

	public CsvDataAdapter(Configuration config) {
		super(config);
	}

	/**
	 * getData is used by client (end user) to obtain a HashMap of DataSource
	 * 
	 * @param testData
	 *            : String
	 * @return dataSourceHashMap : HashMap&lt;String, DataSource&gt;
	 */
	public HashMap<String, DataSource> getData(String testData) {

		List<File> csvFileList = getCsvFileList(testData);
		HashMap<String, DataSource> dataSourceHashMap = convertIt(csvFileList);

		return dataSourceHashMap;
	}

	public Map<String, DataSource> getData(String testData, DataAdapter.Selection select) {
		// determines whether to select all the files based on file pattern
		selection = select;  

		
		List<File> csvFileList = getCsvFileList(testData);
		Map<String, DataSource> dataSourceHashMap = convertIt(csvFileList);

		return dataSourceHashMap;
	}

	/**
	 * getCsvFileList returns a list of File object based on an input pattern
	 * 
	 * @param testData
	 *            : String
	 * @return fileList : List<File>
	 */
	private static List<File> getCsvFileList(String dataPath) {

		File dataFileAbsolute = getDataFullPath(dataPath);

		String dataFilename = dataFileAbsolute.getName();
		String dataParent = dataFileAbsolute.getParent();

		File[] files = getAllFilesBasedOnPattern(dataParent, dataFilename,
				"csv");

		List<File> fileList = new ArrayList<File>(Arrays.asList(files));

		return fileList;
	}

	private static String getDataBaseDirFromProp(Configuration config, String property) {

		String currDir = System.getProperty("user.dir");
		String csvBaseDir = config.getValue(property, "/home/testData");
		String fileFullPath = currDir + File.separator + csvBaseDir;

		return fileFullPath; // returning just filePath also works
	}

	private static File getDataFullPath(String dataPath) {
		String dataBaseDir = getDataBaseDirFromProp(DataAdapter.configuration, DataAdapter.dataBasePath);
		String dataFullPath = dataBaseDir + File.separator + dataPath;

		File dataFile = new File(dataFullPath);
		File dataFileAbsolute = null;
		try {
			dataFileAbsolute = dataFile.getAbsoluteFile();
		} catch (Exception e) {
			log.info(e.getMessage());
		}

		return dataFileAbsolute;
	}

	/**
	 * convertIt converts the input list of File objects into a list of
	 * DataSource
	 * 
	 * @param csvFileList
	 *            : List<File>
	 * @return dataSourceHashMap : HashMap<String, DataSource>
	 */
	private HashMap<String, DataSource> convertIt(List<File> csvFileList) {

		HashMap<String, DataSource> dataSourceHashMap = new HashMap<String, DataSource>();

		for (File f : csvFileList) {
			String absPath = f.getAbsolutePath();
			
			// CSV parses CSV file. It inherits from DataSource
			CSV csv = new CSV(absPath); 
			DataSource ds = csv.getDataSource();
			
			String filenameNoExt = f.getName().replace(".csv", "");
			dataSourceHashMap.put(filenameNoExt, ds);
		}

		return dataSourceHashMap;
	}
}
