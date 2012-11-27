package com.sugarcrm.voodoo.datasource;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public class CsvDataAdapter extends DataAdapter {

	public CsvDataAdapter() {
	}
	
	public List<DataSource> getData(String testData) {
		
		List<File> csvFileList = getCsvFileList(testData);
		printCsvFileList(csvFileList);
		List<DataSource> dataSourceList = convertIt(csvFileList);
		
		return dataSourceList;
	}

	public static List<File> getCsvFileList(String testData) {

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
	
	private List<DataSource> convertIt(List<File> csvFileList) {
		
    	List<DataSource> csvList = new ArrayList<DataSource>(); 
		
		for (File f : csvFileList) {
			//System.out.println(f.toString());
			String absPath = f.getAbsolutePath();
			//System.out.println(absPath);
   		    CSV csv = new CSV(absPath);   // CSV parses CSV file. It inherits from DataSource
   		    csvList.add(csv);
		}
		
		return csvList;
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
