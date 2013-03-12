package com.sugarcrm.voodoo.datasource;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.sugarcrm.voodoo.utilities.Utils;

/**
 * CsvDataAdapter is used by client to convert csv files into a list of
 * DataSource.
 * 
 */
public class CsvDataAdapter extends DataAdapter {

	public CsvDataAdapter(Properties grimoireProps) {
		super(grimoireProps);
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

	private static String getDataBaseDirFromProp(Properties props, String property) {

		String currDir = System.getProperty("user.dir");

		String csvBaseDir = Utils.getCascadingPropertyValue(props,
				//"/home/testData", "datasource.csv.basedir");
				"/home/testData", property);
		//System.out
		//		.println("CsvDataAdapter.java: getDataBaseDirFromProp(): csvBaseDir = "
		//				+ csvBaseDir);

		// String fileFullPath = grimoireDir + File.separator + csvDirPath;
		String fileFullPath = currDir + File.separator + csvBaseDir;
		//System.out
		//		.println("CsvDataAdapter.csv: getDataBaseDirFromProp(): fileFullPath = "
		//				+ fileFullPath);

		return fileFullPath; // it seems returning just filePath still works
	}

	private static File getDataFullPathCanonical(String dataPath) {
		//System.out.println("CsvDataAdapter.java: getCsvFileList(): testData = "
		//		+ dataPath);
		String dataBaseDir = getDataBaseDirFromProp(DataAdapter.properties, DataAdapter.dataBasePath);
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
			// System.out.println(f.toString());
			String absPath = f.getAbsolutePath();
			// System.out.println(absPath);
			CSV csv = new CSV(absPath); // CSV parses CSV file. It inherits from
										// DataSource

			String filenameNoExt = f.getName().replace(".csv", "");
			dataSourceHashMap.put(filenameNoExt, csv);
		}

		return dataSourceHashMap;
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
			FieldSetList csvData = csv.getData();
			System.out.println(csvData.toString());
			printCSVData(csvData);
		}
	}

	private static void printCSVData(FieldSetList csvData) {
		System.out.println("printCSVData(): fsList.size() = " + csvData.size());
		for (FieldSet fs : csvData) {
			System.out.println(fs.toString());
		}
	}
}
