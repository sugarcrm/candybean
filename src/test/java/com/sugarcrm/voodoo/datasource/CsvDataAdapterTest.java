package com.sugarcrm.voodoo.datasource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.sugarcrm.voodoo.configuration.Configuration;
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
	private static MyConfiguration myConfig;
	private static String testDataDir = "testData";

	@Test
	public void testIt() { 
		DataAdapterFactory adapterFactory;
		DataAdapter dataAdapter;

		printCurrentDir();

		String dataDir = testDataDir + File.separator + "csvs";
		dataDir = createDataDir(dataDir);
		// System.out.println("main(): " + dataDir + " created");

		String filename = "Companies_0001.csv";
		String content = getContent1();
		// System.out.println("main(): content1 = " + content);
		createFile(dataDir, filename, content);

		filename = "Companies_0001_note.csv";
		content = getContent1A();
		// System.out.println("main(): content1A = " + contentA);
		createFile(dataDir, filename, content);

		dataDir = testDataDir + File.separator + "csvs" + File.separator
				+ "subDir";
		dataDir = createDataDir(dataDir);

		filename = "Companies_0001_2.csv";
		content = getContent2();
		// System.out.println("main(): content2 = " + content);
		createFile(dataDir, filename, content);

		myConfig = new MyConfiguration();
		Properties myProps = myConfig.createConfigFile();

		// adapterFactory = new DataAdapterFactory(sugarProps);
		adapterFactory = new DataAdapterFactory(myProps);
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
			System.out.println("main(): Current dir using getCanonicalPath():"
					+ current);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String currentDir = System.getProperty("user.dir");
		System.out.println("main(): Current dir using System.getProperty:"
				+ currentDir);
	}

	private static void printDataSourceSingle(DataSource ds) {
		FieldSetList fieldSetList = ds.getData();
		System.out
				.println("main(): printDataSourceSingle(): dataSource filenameNoExt = "
						+ ds.getFilename());
		printDataSourceFieldSet(fieldSetList);
	}

	private static void printDataSource(
			HashMap<String, DataSource> dataSourceHashMap) {
		for (String filenameNoExt : dataSourceHashMap.keySet()) {
			System.out
					.println("main(): printDataSourceData(): dataSource filenameNoExt = "
							+ filenameNoExt);
			FieldSetList fieldSetList = dataSourceHashMap.get(filenameNoExt)
					.getData();
			printDataSourceFieldSet(fieldSetList);
		}
	}

	private static void printDataSourceFieldSet(FieldSetList fieldSetList) {
		System.out
				.println("main(): printDataSourceFieldSet(): fsList.size() = "
						+ fieldSetList.size());
		for (FieldSet fs : fieldSetList) {
			for (String key : fs.keySet()) {
				System.out.println(key + " : " + fs.get(key));
			}
		}
	}
	
	private static void cleanup() {
		
		// Delete properties file created
		System.out.println("");
		myConfig.deleteConfigFile();
		
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
		Configuration prop = null;

		@Test
		public Properties createConfigFile() {
			// Creating Configuration Object
			prop = new Configuration();

			// Defining configuration properties keys/values
			prop.setProperty("datasource.csv.baseDir", "testData");
			prop.setProperty("datasource.csv.subDir", "testData/csvs/subDir");

			// Store Configuration
			try {
				prop.store(configFilePath, null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Checking that the created file exists
			File propFile = new File(configFilePath);
			Assert.assertTrue("The property files created does not exist!",
					propFile.exists());

			try {
				prop.load(configFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return prop;
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