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

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DataAdapter;
import com.sugarcrm.candybean.datasource.DataAdapterFactory;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.candybean.datasource.DataAdapterFactory.DataAdapterType;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class CsvDataAdapterUnitTest {
	private static MyConfiguration myConfiguration;
	private static String testDataDir = "testData";

	@Test
	public void testIt() throws Exception { 
		DataAdapterFactory adapterFactory;
		DataAdapter dataAdapter;

		printCurrentDir();

		String dataDir = testDataDir + File.separator + "csvs";
		dataDir = createDataDir(dataDir);

		String filename = "Companies_0001.csv";
		String content = getContent1();
		createFile(dataDir, filename, content);

		filename = "Companies_0001_note.csv";
		content = getContent1A();
		createFile(dataDir, filename, content);

		dataDir = testDataDir + File.separator + "csvs" + File.separator
				+ "subDir";
		dataDir = createDataDir(dataDir);

		filename = "Companies_0001_2.csv";
		content = getContent2();
		createFile(dataDir, filename, content);

		myConfiguration = new MyConfiguration();
		Configuration myConfig = myConfiguration.createConfigFile();

		adapterFactory = new DataAdapterFactory(myConfig);
		dataAdapter = adapterFactory.createDataAdapter(DataAdapterType.CSV);

		Map<String, DataSource> dataSourceHashMap = dataAdapter.setDataBasePath(
				"datasource.csv.baseDir").getData("csvs/Companies_0001", DataAdapter.Selection.SINGLE);
		DataSource ds = dataSourceHashMap.get("Companies_0001");
		printDataSourceSingle(ds);
		printDataSource(dataSourceHashMap);

		dataSourceHashMap = dataAdapter
				.setDataBasePath("datasource.csv.subDir").getData(
						"Companies_0001_2");
		printDataSource(dataSourceHashMap);
		
		cleanup(); 
	}

	private static String createDataDir(String dirStr) {
		File dir = new File(dirStr);

		String testDataPath = dirStr;
		try {
			// Create directory if not exist
			if (!dir.exists()) {
				testDataPath = dir.getAbsolutePath();
				boolean result = dir.mkdirs();
				if (result) {
					System.out.println("createDataDir(): created "
							+ testDataPath);
				}

			} else {
				System.out.println("createDataDir(): " + testDataPath
						+ " already exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testDataPath;
	}

	private static void createFile(String dir, String filename, String content) {
		try {
			File file = new File(dir + File.separator + filename);

			// Create file if not exist
			if (!file.exists()) {
				file.createNewFile();
				System.out.println("createFile(): created "
						+ file.getAbsolutePath());
			} else {
				System.out.println("createFile(): " + file.getAbsolutePath()
						+ " already exists");
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getContent1() {
		StringBuilder sb = new StringBuilder();
		sb.append("COMPANY,CATEGORY,  NUM_EMPLOYEES, \"PUBLIC COMPANY\"\n");
		sb.append("SugarCRM,CRM, 300, false\n");
		sb.append("Yahoo, Media, 7500, true\n");
		sb.append("Agilent, Measurements and Tests, 7000");

		return sb.toString();
	}

	private static String getContent1A() {
		StringBuilder sb = new StringBuilder();
		sb.append("COMPANY,CATEGORY,  NUM_EMPLOYEES, \"PUBLIC COMPANY\"\n");
		sb.append("IBM,IT, 9000, true");

		return sb.toString();
	}

	private static String getContent2() {
		StringBuilder sb = new StringBuilder();
		sb.append("COMPANY,CATEGORY,  NUM_EMPLOYEES, \"PUBLIC COMPANY\"\n");
		sb.append("SugarCRM,CRM, 300, false\n");
		sb.append("Yahoo, Media, 7500, true\n");
		sb.append("Agilent, Measurements and Tests, 7000\n");
		sb.append("Facebook, \"Social Media\", 5000, true");

		return sb.toString();
	}

	private static void printCurrentDir() {
		try {
			String current = new java.io.File(".").getAbsolutePath();
			System.out.println("CsvDataAdapterTest: Current dir using getAbsolutePath():"
					+ current);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String currentDir = System.getProperty("user.dir");
		System.out.println("CsvDataAdapterTest: Current dir using System.getProperty:"
				+ currentDir);
	}

	private static void printDataSourceSingle(DataSource ds) {
		System.out.println("CsvDataAdapterTest: printDataSourceSingle(): dataSource filenameNoExt = "
						+ ds.getFilename());
		printDataSourceFieldSet(ds);
	}

	private static void printDataSource(Map<String, DataSource> dataSourceHashMap) {
		for (String filenameNoExt : dataSourceHashMap.keySet()) {
			System.out.println("CsvDataAdapterTest: printDataSourceData(): dataSource filenameNoExt = "
							+ filenameNoExt);
			DataSource ds = dataSourceHashMap.get(filenameNoExt);
			printDataSourceFieldSet(ds);
		}
	}

	private static void printDataSourceFieldSet(DataSource ds) {
		System.out.println("CsvDataAdapterTest: printDataSourceFieldSet(): fsList.size() = "
						+ ds.size());
		for (FieldSet fs : ds) {
			for (String key : fs.keySet()) {
				System.out.println(key + ":" + fs.get(key));
			}
		}
	}
	
	private static void cleanup() {
		
		// Delete properties file created
		System.out.println("");
		myConfiguration.deleteConfigFile();
		
		// Delete data directory
		try {
			FileUtils.deleteDirectory(new File(testDataDir));
			System.out.println("Deleted " + testDataDir);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static class MyConfiguration {
		String configFilePath = System.getProperty("user.dir") + File.separator
				+ "myPropFile.properties";
		Configuration config = null;

		@Test
		public Configuration createConfigFile() {
			// Creating Configuration Object
			config = new Configuration();

			// Defining configuration properties keys/values
			config.setValue("datasource.csv.baseDir", "testData");
			config.setValue("datasource.csv.subDir", "testData/csvs/subDir");

			return config;
		}

		//@Test
		public void deleteConfigFile() {
			// Deleting the File created
			File propFile = new File(configFilePath);
			boolean Ok = propFile.delete();
			assertTrue("File did not get deleted!", !propFile.exists());
			
			if (Ok) {
				System.out.println("Deleted " + propFile.getName());
			}
		}
	}
}