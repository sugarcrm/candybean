package com.sugarcrm.voodoo.datasource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.sugarcrm.voodoo.configuration.Configuration;
import com.sugarcrm.voodoo.datasource.DataAdapter;
import com.sugarcrm.voodoo.datasource.DataAdapterFactory;
import com.sugarcrm.voodoo.datasource.DataAdapterFactory.DataAdapterType;
import com.sugarcrm.voodoo.datasource.DataSource;
import com.sugarcrm.voodoo.datasource.FieldSet;

/**
 *
 *
 */
public class CsvDataAdapterTest {
	private static MyConfiguration myConfiguration;
	private static String testDataDir = "testData";

	@Test
	public void testIt() { 
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

		HashMap<String, DataSource>
		dataSourceHashMap = dataAdapter.setDataBasePath(
				"datasource.csv.baseDir").getData("csvs/Companies_0001",
				DataAdapter.Selection.SINGLE);
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
				testDataPath = dir.getCanonicalPath();
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
						+ file.getCanonicalPath());
			} else {
				System.out.println("createFile(): " + file.getCanonicalPath()
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
			String current = new java.io.File(".").getCanonicalPath();
			System.out.println("CsvDataAdapterTest: Current dir using getCanonicalPath():"
					+ current);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String currentDir = System.getProperty("user.dir");
		System.out.println("CsvDataAdapterTest: Current dir using System.getProperty:"
				+ currentDir);
	}

	private static void printDataSourceSingle(DataSource ds) {
		System.out
				.println("CsvDataAdapterTest: printDataSourceSingle(): dataSource filenameNoExt = "
						+ ds.getFilename());
		printDataSourceFieldSet(ds);
	}

	private static void printDataSource(
			HashMap<String, DataSource> dataSourceHashMap) {
		for (String filenameNoExt : dataSourceHashMap.keySet()) {
			System.out
					.println("CsvDataAdapterTest: printDataSourceData(): dataSource filenameNoExt = "
							+ filenameNoExt);
			DataSource ds = dataSourceHashMap.get(filenameNoExt);
			printDataSourceFieldSet(ds);
		}
	}

	private static void printDataSourceFieldSet(DataSource ds) {
		System.out
				.println("CsvDataAdapterTest: printDataSourceFieldSet(): fsList.size() = "
						+ ds.size());
		for (FieldSet fs : ds) {
			for (String key : fs.keySet()) {
				System.out.println(key + " : " + fs.get(key));
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
			config.setProperty("datasource.csv.baseDir", "testData");
			config.setProperty("datasource.csv.subDir", "testData/csvs/subDir");

			// Store Configuration
			try {
				config.store(configFilePath, null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Checking that the created file exists
			File propFile = new File(configFilePath);
			Assert.assertTrue("The property files created does not exist!",
					propFile.exists());

			try {
				config.load(configFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return config;
		}

		@Test
		public void deleteConfigFile() {
			// Deleting the File created
			File propFile = new File(configFilePath);
			boolean Ok = propFile.delete();
			Assert.assertTrue("File did not get deleted!", !propFile.exists());
			
			if (Ok) {
				System.out.println("Deleted " + propFile.getName());
			}
		}
	}
}