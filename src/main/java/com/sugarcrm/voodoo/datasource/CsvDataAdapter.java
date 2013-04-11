package com.sugarcrm.voodoo.datasource;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.sugarcrm.voodoo.configuration.Configuration;

/**
 * CsvDataAdapter is used by client to convert csv files into a list of
 * DataSource.
 * 
 */
public class CsvDataAdapter extends DataAdapter {

	public CsvDataAdapter(Configuration config) {
		super(config);
	}

	/**
	 * getData is used by client (end user) to obtain a HashMap of DataSource
	 * 
	 * @param testData
	 *            : String
	 * @return dataSourceHashMap : HashMap<String, DataSource>
	 */
	public HashMap<String, DataSource> getData(String testData) {

		List<File> csvFileList = getCsvFileList(testData);
		//printCsvFileList(csvFileList);
		HashMap<String, DataSource> dataSourceHashMap = convertIt(csvFileList);

		return dataSourceHashMap;
	}

	public HashMap<String, DataSource> getData(String testData, DataAdapter.Selection select) {

		selection = select;  // determines whether to select all the files based on file pattern
		
		List<File> csvFileList = getCsvFileList(testData);
		//printCsvFileList(csvFileList);
		HashMap<String, DataSource> dataSourceHashMap = convertIt(csvFileList);

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

		File dataFileCanonical = getDataFullPathCanonical(dataPath);

		String dataFilename = dataFileCanonical.getName();
		String dataParent = dataFileCanonical.getParent();

		File[] files = getAllFilesBasedOnPattern(dataParent, dataFilename,
				"csv");

		List<File> fileList = new ArrayList<File>(Arrays.asList(files));

		return fileList;
	}

	private static String getDataBaseDirFromProp(Configuration config, String property) {

		String currDir = System.getProperty("user.dir");

		String csvBaseDir = config.getProperty(property, "/home/testData");
		//System.out
		//		.println("CsvDataAdapter.java: getDataBaseDirFromProp(): csvBaseDir = "
		//				+ csvBaseDir);

		String fileFullPath = currDir + File.separator + csvBaseDir;

		return fileFullPath; // it seems returning just filePath still works
	}

	private static File getDataFullPathCanonical(String dataPath) {
		String dataBaseDir = getDataBaseDirFromProp(DataAdapter.configuration, DataAdapter.dataBasePath);
		//System.out
		//		.println("CsvDataAdapter.java: getCsvFileList(): fileFullDirPath = "
		//				+ dataBaseDir);

		String dataFullPath = dataBaseDir + File.separator + dataPath;

		File dataFile = new File(dataFullPath);
		File dataFileCanonical = null;
		try {
			dataFileCanonical = dataFile.getCanonicalFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//printDataFilePath(dataFileCanonical);

		return dataFileCanonical;
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

	private static void printDataFilePath(File dataFileCanonical) {
		System.out
				.println("CsvDataAdapter.java: getCsvFileList(): dataCanonicalPath = "
						+ dataFileCanonical.getPath());
		String dataFilename = dataFileCanonical.getName();
		String dataParent = dataFileCanonical.getParent();
		System.out
				.println("CsvDataAdapter.java: getCsvFileList(): dataFilename = "
						+ dataFilename + "  dataParent = " + dataParent);
	}

	private static void printCsvFileList(List<File> csvFileList) {
		try {
			for (File f : csvFileList) {
				System.out
						.println("CsvDataAdapter.java: printCsvFileList(): file = "
								+ f.getCanonicalPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printCsvList(List<CSV> csvList) {
		for (CSV csv : csvList) {
			System.out.println(csv.toString());
			//printCSVData(csvData);
			printCSVData(csv);
		}
	}

	private static void printCSVData(DataSource csvData) {
		System.out.println("printCSVData(): fsList.size() = " + csvData.size());
		for (FieldSet fs : csvData) {
			System.out.println(fs.toString());
		}
	}
}
