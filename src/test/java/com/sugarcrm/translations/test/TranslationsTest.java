package com.sugarcrm.translations.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import junit.framework.Assert;
import com.sugarcrm.voodoo.translations.Translations;
import org.junit.Test;

public class TranslationsTest {
	String currentWorkingDir = System.getProperty("user.dir");

	/**
	 *  This method will test for the translation of 1 single test (Accounts_0105.java)
	 *  which uses arguments presented by the user (instead of using the properties file)
	 */
	@Test
	public void SingleTestTranslation() throws Exception {
		try {
			// Accounts File create
			String AccountsFolderName = "Accounts_0105.java";
			String AccountsFolderNamePath = currentWorkingDir + File.separator + AccountsFolderName;
			String codeLine = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Contracts\", argument3);";
			createFile(AccountsFolderNamePath, codeLine, "new");

			// translations details
			String language = "es_ES";
			String database = "translate_6_3";
			String module = "Accounts";

			// Perform translations on the the above File
			System.out.println("Translation will now begin!");
			Translations.Translate(database, module, language, AccountsFolderNamePath);
			System.out.println("Translation successfull!");

			// A folder named translated_Files_es_ES is created along with the translated test file (Accounts_0105.java_es_ES)
			System.out.println("Checking translated test file");
			String directoryPath = currentWorkingDir + File.separator + "translated_Files" + "_" + language;
			String translatedFilePath = directoryPath + File.separator + AccountsFolderName + "_" + language;
			String originalFilePath = currentWorkingDir + File.separator + AccountsFolderName;

			// Check assert Statement
			String expected = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Contratos\", argument3);";
			assertCheck(translatedFilePath, expected);

			// remove folder and file from directory including the original non-translated file
			System.out.println("File Checking finished! will now begin to delete files and folders");
			CleanUpFileDirectory(directoryPath);
			CleanUpFile(originalFilePath);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

	}

	/**
	 *  This method will test for the translation of multiple test files (e.g. directory with tests)
	 *  - It will also use a properties file instead of argument inputs (properties file path given as a argument)
	 */
	@Test
	public void MutilpleTestsTranslation() throws Exception {
		// Create a temporary folder containing two directories with two test files each
		String TempFolder = "tempFolder";
		String TempFolderPath = currentWorkingDir + File.separator + TempFolder;
		createDirectory(TempFolderPath);

		// Accounts Folder
		String AccountsFolderName = "Accounts";
		String AccountsFolderNamePath = TempFolderPath + File.separator + AccountsFolderName;
		createDirectory(AccountsFolderNamePath);

		// Accounts File 1
		String Account1 = "Accounts_1234.java";
		String Account1Path = AccountsFolderNamePath + File.separator + Account1;
		String Account1codeLine = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Contracts\", argument3);";
		createFile(Account1Path, Account1codeLine, "new");

		// Accounts File 2
		String Account2 = "Accounts_5678.java";
		String Account2Path = AccountsFolderNamePath + File.separator + Account2;
		String Account2codeLine = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Projects\", argument3);";
		createFile(Account2Path, Account2codeLine, "new");	

		// Quotes Folder
		String QuotesFolderName = "Quotes";
		String QuotesFolderNamePath = TempFolderPath + File.separator + QuotesFolderName;
		createDirectory(QuotesFolderNamePath);

		// Quotes File 1
		String Quotes1 = "Quotes_1234.java";
		String Quotes1Path = QuotesFolderNamePath + File.separator + Quotes1;
		String Quotes1codeLine = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"ERROR: cant move_pdf to $destination. You should try making the directory writable by the webserver\", argument3);";
		createFile(Quotes1Path, Quotes1codeLine, "new");

		// Quotes File 2
		String Quotes2 = "Quotes_5678.java";
		String Quotes2Path = QuotesFolderNamePath + File.separator + Quotes2;
		String Quotes2codeLine = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Actual Close\", argument3);";
		createFile(Quotes2Path, Quotes2codeLine, "new");

		// CreateModule List
		String modList = "module-list";
		String modListPath = TempFolderPath + File.separator + modList;
		String module1 = "Accounts";
		String module2 = module1 + "\r\n" + "Projects";
		String module3 = module2 + "\r\n" + "Quotes";
		createFile(modListPath, module3, "new");

		// Create Properties File 
		String propName = "myProp.properties";
		String propNamePath = TempFolderPath + File.separator + propName;
		String prop1 = "translate.database = translate_6_3";
		String prop2 = prop1 + "\r\n" + "translate.language = es_ES";
		String prop3 = prop2 + "\r\n" + "translate.testpath = " + TempFolderPath;
		String prop4 = prop3 + "\r\n" + "translate.module = " + modListPath;
		String prop5 = prop4 + "\r\n" + "translate.serverName = 10.8.31.10";
		String prop6 = prop5 + "\r\n" + "translate.username = translator";
		String prop7 = prop6 + "\r\n" + "translate.password = Sugar123!";
		String prop8 = prop7 + "\r\n" + "translate.output = " + currentWorkingDir + File.separator + "translated_Folder";
		createFile(propNamePath, prop8, "new");


		// Perform translations on the the above File
		System.out.println("Translation will now begin!");
		Translations.Translate(propNamePath);
		System.out.println("Translation successfull!");

		// Check assert Statement
		String outputDir = currentWorkingDir + File.separator + "translated_Folder" + "_es_ES" + File.separator;
		System.out.println("Performing Assert Test");
		String expected1 = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Contratos\", argument3);";
		assertCheck(outputDir + Account1 + "_es_ES", expected1);
		String expected2 = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Proyectos\", argument3);";
		assertCheck(outputDir + Account2 + "_es_ES", expected2);
		String expected3 = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"ERROR: no puede moverse el archivo PDF a $destination. Intente dar permisos de escritura al servidor web para ese directorio\", argument3);";
		assertCheck(outputDir + Quotes1 + "_es_ES", expected3);
		String expected4 = "Assert.assertEquals(\"This is a hello, \\\" omg,\" + \"arugment1, argment 1 more\", \"Cierre Real\", argument3);";
		assertCheck(outputDir + Quotes2 + "_es_ES", expected4);
		
		// Delete Folder and Files
		System.out.println("File Checking finished! will now begin to delete files and folders");
		CleanUpFileDirectory(TempFolderPath);
		CleanUpFileDirectory(outputDir);
	}

	/**
	 * This method will delete the given directory (including its files)
	 * @param path
	 */
	public void CleanUpFileDirectory(String path) {
		File filePath = new File (path);
		if (filePath.isFile())CleanUpFile(filePath.getAbsolutePath());
		else if (filePath.isDirectory()) {
			File[] files = new File(path).listFiles();
			for (File file: files) {
				if (file.isFile()) CleanUpFile(file.getAbsolutePath());
				else if (file.isDirectory()) CleanUpFileDirectory(file.getAbsolutePath());
				else System.out.println("Unable to delete file: " + filePath.getName());
			}
			filePath.delete();
			System.out.println(filePath.getName() + " directory has been deleted");
		} 
		else System.out.println("Unable to delete file: " + filePath.getName());
	}

	/**
	 * This method will delete the given file
	 * @param path
	 */
	public void CleanUpFile(String path) {
		File filePath = new File (path);
		try {
			filePath.delete();
			System.out.println(filePath.getName() + " file has been deleted");
		} catch (Exception e) {
			System.out.println("Unable to delete file: " + filePath.getName());
		}
	}

	/**
	 * Creates a file from the given path
	 * @param filePath
	 * @param message
	 */
	public void createFile(String filePath, String codeLine, String cond) {
		try {
			if (cond.equals("new")) {
				File file = new File (filePath);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(codeLine);
				System.out.println(filePath + " file created!");
				bw.close();
			} else if (cond.equals("add")) {
				File file = new File (filePath);
				FileWriter fw = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(codeLine);
				System.out.println(filePath + " file created!");
				bw.close();
			} else {
				System.out.println("Invalid condition, expected 'add' or 'new' but got: " + cond);
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Creates a folder from the given path
	 * @param path
	 */
	public void createDirectory(String path) {
		try {
			File directory = new File(path);
			directory.mkdir();
			System.out.println(directory.getName() + " directory created!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * This method will look for an assert statement in the file and check against the expected argument
	 * @param filepath
	 */
	public void assertCheck(String filepath, String expected) {
		try {
			File translatedFile = new File(filepath);
			// Read the content and match with the expected -> Contracts should have been translated to Contratos
			BufferedReader br = new BufferedReader( new FileReader (translatedFile.getAbsolutePath()));
			String content;
			while ((content = br.readLine()) != null) {
				if (content.contains("assert")) {
					System.out.println("Checking actual: " + content + " -> expected: " + expected);
					Assert.assertEquals("Assert line does not match!", expected, content);
				}
			}
			br.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
}
