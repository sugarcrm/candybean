package com.sugarcrm.voodoo.datasource;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  CsvDataAdapter is used by client to convert csv files into a list of DataSource.
 *
 */
public class CsvDataAdapter extends DataAdapter {

	public CsvDataAdapter() {
	}
	
	/**
	 * getData is used by client to obtain a list of DataSource 
	 * 
	 * @param testData : String
	 * @return dataSourceList : List<DataSource>         
	 */
	public List<DataSource> getData(String testData) {
		
		List<File> csvFileList = getCsvFileList(testData);
		printCsvFileList(csvFileList);
		List<DataSource> dataSourceList = convertIt(csvFileList);
		
		return dataSourceList;
	}

	/**
	 * getCsvFileList returns a list of File object based on an input pattern 
	 * 
	 * @param testData : String
	 * @return fileList : List<File>         
	 */
	private static List<File> getCsvFileList(String testData) {

		String fileFullPath = getFileFullDirPath();
		System.out.println("CsvDataAdapter.java: getCsvFileList(): fileFullPath = " + fileFullPath);
		File[] files = getAllFilesBasedOnPattern(fileFullPath, testData, "csv");
		
		List<File> fileList = new ArrayList<File>(Arrays.asList(files));

		return fileList;
	}
	
	private static String getFileFullDirPath() {
		
		String currDir = System.getProperty("user.dir");
		String grimoireDir = currDir.replace("Voodoo2", "VoodooGrimoire");  // TODO. Temporary
		
		String csvDirPath = "testData/csvDir";   // relative to VoodooGrimoire directory 

		String fileFullPath = grimoireDir + File.separator + csvDirPath;
		System.out.println("CsvDataAdapter.csv: getFileFullDirPath(): fileFullPath = " + fileFullPath);
		
		return fileFullPath;   // it seems returning just filePath still works
	}
	
	/**
	 * convertIt converts the input list of File objects into a list of DataSource 
	 * 
	 * @param csvFileList : List<File> 
	 * @return dataSourceList : List<DataSource>         
	 */
	private List<DataSource> convertIt(List<File> csvFileList) {
		
    	List<DataSource> dataSourceList = new ArrayList<DataSource>(); 
		
		for (File f : csvFileList) {
			//System.out.println(f.toString());
			String absPath = f.getAbsolutePath();
			//System.out.println(absPath);
   		    CSV csv = new CSV(absPath);   // CSV parses CSV file. It inherits from DataSource
   		    dataSourceList.add(csv);
		}
		
		return dataSourceList;
	}
	
	private static void	printCsvFileList(List<File> csvFileList) {
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
			FieldSetList csvData = csv.getData();   // CSVData changed to subclass of List<FieldSet>
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
