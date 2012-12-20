package com.sugarcrm.voodoo.translations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.lang.System;

/**
 * @author Wilson Li
 *
 */
public class Translations {
	private static String DATABASE;
	private static String MODULE;
	private static String LANGUAGE;
	private static String TEST_PATH;
	private static String OUTPUT_FOLDER;
	private static Connection DB_CONNECTION = null;
	private static ArrayList<String> listOfDatabaseModules = new ArrayList<String>();
	private static Properties translateProp = null;

	// if the string to be replace is not in such module then search through all DB modules
	// currently disabled, set variable to 'true' to enable
	private static boolean SEARCH_ALL_MODULES = false;

	// This variable indicates which assert string-position to replace
	// This variable is set by the following method: setAssertPosition()
	private static int ASSERT_POSITION;

	//*** NOTE, This is to run the program with command line
	// Refer to Voodoo Translations Wiki for more info on usage
	public static void main(String[] args) {	
		try {
		if (args.length == 1) Translate(args[0]);	
		else if (args.length == 4) Translate(args[0], args[1], args[2], args[3]);	
		else { 
			System.out.println("Invalid number of arguments. Given number of arguments: " + args.length); 	
			System.exit(1);
		}	
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Translation Method 1: translate a single/set of test(s) using the given
	 * input arguments
	 * 
	 * @author Wilson Li
	 * @param db - database name (ie: translate_6_3, translate_6_2, translate_6_1)
	 * @param mod - module(s) name (ie: Account, Quotes, Contacts, etc)
	 * @param lang - language (ie: fr_FR, ja_JP, zh_CH, en_UK, etc)
	 * @param testP - path to a test file or to a folder containing multiple tests
	 * @throws Exception
	 */
	public static void Translate(String db, String mod, String lang, String testP) throws Exception {
		try {
			DATABASE = db;
			MODULE = mod;
			LANGUAGE = lang;
			TEST_PATH = testP;
			String defaultOutputPath = System.getProperty("user.dir") + File.separator	+ "translated_Files_" + LANGUAGE;
			OUTPUT_FOLDER = getCascadingPropertyValue(null, defaultOutputPath, "translate.output");
			DB_CONNECTION = connectToDatabase();

			populateListOfModules(MODULE);
			createFolder(OUTPUT_FOLDER);
			recursePathForTranslations(TEST_PATH, OUTPUT_FOLDER);

		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * Translation Method 2: translate a single/set of test(s) from the given
	 * properties file's path containing all the required information to perform
	 * a translation
	 * 
	 * @author Wilson Li
	 * @param propPath - path to a properties file containing required translation information (ie: translate.properties)
	 * @throws Exception
	 */
	public static void Translate(String propPath) throws Exception {
		try {
			translateProp = new Properties();
			translateProp.load((new FileInputStream(propPath)));

			DATABASE = getCascadingPropertyValue(translateProp, "null", "translate.database");
			MODULE = getCascadingPropertyValue(translateProp, "null", "translate.module");
			LANGUAGE = getCascadingPropertyValue(translateProp, "null", "translate.language");
			TEST_PATH = getCascadingPropertyValue(translateProp, "null", "translate.testpath");
			OUTPUT_FOLDER = getCascadingPropertyValue(translateProp, "null", "translate.output") + "_" + LANGUAGE;
			DB_CONNECTION = connectToDatabase();

			populateListOfModules(MODULE);
			createFolder(OUTPUT_FOLDER);
			recursePathForTranslations(TEST_PATH, OUTPUT_FOLDER);

		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * A wrapper function that will recursively run all tests (translate) from the given path
	 * Condition:
	 * - If testPath is a test file then perform translation (fileReaderWriter) 
	 * - If testPath is a folder then recursively run each item on the conditional check 
	 *         
	 * @author Wilson Li
	 * @param testPath - a path to a single test file or a folder containing multiple test files
	 * @param outputFolder - a path to a output folder
	 * @throws Exception
	 */
	private static void recursePathForTranslations(String testPath, String outputFolder) throws Exception {
		try {
			File inputFile = new File(testPath);
			if (inputFile.isFile()) { // testPath: Path to a test file
				String outputSubFolder = outputFolder + File.separator + inputFile.getName() + "_" + LANGUAGE;
				String moduleFileName = getFileModuleName(inputFile.getName());
				// If the inputFile is a file that contains a name from the module(s) and is of java format then perform translation
				if (isModuleExist(moduleFileName) && inputFile.getName().contains(".java")) {
					// perform translation
					fileReaderWriter(moduleFileName, testPath, outputSubFolder);
				}
			} else { // testPath: path to a folder containing test file(s)
				File[] files = new File(testPath).listFiles();
				for (File file : files) {
					String testPathSubFolder = file.getAbsolutePath();
					String outputSubFolder = outputFolder + File.separator + file.getName();
					String moduleFileName = getFileModuleName(file.getName());
					// If the item is a file that contains a name from the module(s) and is of java format then perform translation
					if (file.isFile() && isModuleExist(moduleFileName) && file.getName().contains(".java")) {
						// perform translation
						fileReaderWriter(moduleFileName, testPathSubFolder, outputSubFolder + "_" + LANGUAGE);
					}
					// If the item is a directory, then recurse the function with the item's path
					if (file.isDirectory()) {
						createFolder(outputSubFolder);
						recursePathForTranslations(file.getAbsolutePath(), outputSubFolder);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * Scan through the test file to replace (translate) the assert string
	 * to a language-specified string and write to a new file
	 * 
	 * @author Wilson Li
	 * @param module - database module 
	 * @param inputFile - the test file to be translated
	 * @param outputFile - translated test file
	 * @throws Exception
	 */
	private static void fileReaderWriter(String module, String inputFile, String outputFile) throws Exception {
		File file = new File(inputFile);
		File convertedFile = new File(outputFile);
		Scanner fileScanner = new Scanner(file);
		Writer output = null;
		Pattern pattern_assert = Pattern.compile("assertEquals\\((.*?)\\)");

		try {
			Matcher match_assert = null;
			output = new BufferedWriter(new FileWriter(convertedFile));
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				match_assert = pattern_assert.matcher(line);
				if (match_assert.find()) { // Assert replacement
					String stringToBeReplaced = getToBeReplacedAssertString(match_assert.group(1));
					String ReplacementString = getDatabaseReplacementString(module, stringToBeReplaced);
					String newLine = line.replace(stringToBeReplaced, ReplacementString);
					output.write(newLine + "\r\n");
				} else {
					output.write(line + "\r\n");
				}
			}
			output.close();
			fileScanner.close();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Establish connection to the database
	 * 
	 * @author Wilson Li
	 * @return - a MySQL connection object that is used for database queries
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	private static Connection connectToDatabase() throws Exception {
		Connection con = null;
		// TODO: May use centralized DB here
		String serverName = getCascadingPropertyValue(translateProp, "10.8.31.10", "translate.serverName");
		String username = getCascadingPropertyValue(translateProp, "translator", "translate.username");
		String password = getCascadingPropertyValue(translateProp, "Sugar123!", "translate.password");
		printMsg("connecting with: " + serverName + " " + username + " " + password);
		try {
			// Create a connection to the database
			String driverName = "com.mysql.jdbc.Driver"; // MySQL MM JDBC driver
			Class.forName(driverName);
			String url = "jdbc:mysql://" + serverName + File.separator + DATABASE; // a JDBC url
			con = DriverManager.getConnection(url, username, password);
			printMsg("Connection to database successfull!");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			return con;
		}
	}

	/**
	 * Queries the module table that is passed. However, if the translation does not exist
	 * within the current module, there will be a search in all modules by calling searchAllModules
	 * method (That is if SEARCH_ALL_MODULES is set to true)
	 * 
	 * @author Wilson Li
	 * @param module
	 * @param englishString
	 * @return the translated string
	 * 
	 */
	@SuppressWarnings("finally")
	private static String getDatabaseReplacementString(String module, String englishString) throws Exception {
		String result = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "SELECT " + LANGUAGE + " FROM " + module + " WHERE english = " + "\'" + englishString + "\'";
			pst = DB_CONNECTION.prepareStatement(query);
			rs = pst.executeQuery();

			// if there is a result due to the query, the translated value is returned. 
			if (rs.next()) {
				result = (rs.getString(1));
				printMsg("Replaced english: '" + englishString + "' with " + LANGUAGE + ": '" + result + "'.");
			} else if (SEARCH_ALL_MODULES){  // Search through the rest of the modules
				printErrorMsg("Could not find the translation for " + englishString + " in the " + module + " module");
				printMsg("Proceeding to search through all modules");
				result = searchAllModules(englishString);
			} else { // Else return ERROR message no such replacement string
				printErrorMsg("Could not find the translation for " + englishString + " in the " + module + " module");
				result = englishString;
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			return result;
		}
	}

	/**
	 * Perform a Query search on all modules. This can be called when a string is not found 
	 * on a specified module. However it is currently disabled. Enable this feature by changing 
	 * the constant SEARCH_ALL_MODULES to true.
	 * 
	 * @author Wilson Li
	 * @param englishString
	 * @return the translated string
	 * @throws Exception
	 */
	private static String searchAllModules(String englishString) throws Exception {
		String result = null;
		int counter = 0;
		String[] tables = getAllModuleNamesFromDB();

		while (tables[counter] != null) {
			PreparedStatement pst_ifExists = null;
			ResultSet rs_ifExists = null;

			pst_ifExists = DB_CONNECTION.prepareStatement("SHOW columns from `" + tables[counter] + "` where field='" + LANGUAGE + "'");
			rs_ifExists = pst_ifExists.executeQuery();

			// checks to see if the language is in that particular module
			if (rs_ifExists.next()) {
				PreparedStatement pst_temp = null;
				ResultSet rs_temp = null;

				pst_temp = DB_CONNECTION.prepareStatement("SELECT " + LANGUAGE + " FROM " + tables[counter] + " WHERE english = " + "\'" + englishString + "\'");
				rs_temp = pst_temp.executeQuery();
				if (rs_temp.next()) {
					result = (rs_temp.getString(1));
					printMsg("Replaced english: '" + englishString + "' with " + LANGUAGE + ": '" + result + "' from the '" + tables[counter] + "' module");
					break;
				} else {
					printErrorMsg("Could not find the translation for " + englishString + " in the " + tables[counter] + " module");
					result = englishString;
					rs_temp.close();
					pst_temp.close();
					counter++;
				}
			} else {
				printErrorMsg("The table: " + tables[counter] + " does not contain the language: " + LANGUAGE);
				counter++;
			}
		}
		return result;
	}


	/**
	 * Get an array of all the module names from the Database
	 * 
	 * @author Wilson Li
	 * @return an array of module names within the database that is being queried
	 */
	private static String[] getAllModuleNamesFromDB() throws Exception {
		int counter = 0;
		try {
			DatabaseMetaData dbmd = DB_CONNECTION.getMetaData();
			String[] tables = new String[dbmd.getMaxTablesInSelect()];
			String[] types = { "TABLE" };
			ResultSet resultSet = dbmd.getTables(null, null, "%", types);
			// Get the table names
			while (resultSet.next()) {
				String tableName = resultSet.getString(3);
				tables[counter] = tableName;
				counter++;
			}
			return tables;
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * This function was implemented for Voodoo2 to replace the
	 * [Expected Value] from the following assert statement:
	 * Assert.assertEquals( [Message], [Expected Value], [Actual Value]); 
	 * to a language-specific value retrieved from the database
	 * 
	 * @author Wilson Li
	 * @param assertStr
	 * @return a string representing the value to be translated
	 */
	private static String getToBeReplacedAssertString(String assertStr) throws Exception {
		String tempString = "";
		String prevString = "";
		String[] statement = assertStr.split("");
		ArrayList<String> argumentListString = new ArrayList<String>();
		boolean withinQuote = false;

		// loop through each character to find the desire string. In this 
		// case, the second argument from an assert statement
		for (int index = 0; index < statement.length; index++) {
			if (statement[index].equals("\"") && !withinQuote) {
				// beginning of a quoted string
				prevString = statement[index];
				tempString += statement[index]; // build tempString
				withinQuote = true;
			} else if (withinQuote) { // within a quoted string
				if (!prevString.equals("\\") && statement[index].equals("\"")) {
					// Is within quote but has reached the end fo the quoted string
					prevString = statement[index];
					tempString += statement[index];
					withinQuote = false;
				} else { // still within quoted string and not yet the end
					// keep building the string
					prevString = statement[index];
					tempString += statement[index];
				}
			} else { // This is not within a quote
				// simply a variable - loop until a comma is seen
				if (statement[index].equals(",")) {
					// if a comma is seen then place tempString into the
					// ArgumentList
					prevString = statement[index];
					argumentListString.add(tempString);
					tempString = "";
				} else {
					// else keep building the tempString
					prevString = statement[index];
					tempString += statement[index];
				}
			}
		}
		// Concatenating the last element
		argumentListString.add(tempString);
		// Defining assertType according to the number of arguments retrieved
		setAssertPosition(argumentListString.size());
		// get argument string and trim off white spaces
		String ret = argumentListString.get(ASSERT_POSITION).trim();
		// if needed, trim off beginning and ending quote marks
		return trimBorderQuoteMarks(ret);
	}

	/**
	 * This method will set the ASSERT_POSITION for specific string replacement in an assert statement.
	 * The position indicates the expectedValue of an assert statement. Two types of asserts currently
	 * support.
	 * 
	 * @author Wilson Li
	 * @param numberOfArguments
	 * @throws Exception
	 */
	private static void setAssertPosition(int numberOfArguments) throws Exception {
		switch (numberOfArguments) {
		case 2: ASSERT_POSITION = 0; break; // assertEquals(expectedValue, actualValue); 
		case 3:	ASSERT_POSITION = 1; break; // assertEquals(message, expectedValue, actualValue);
		default: throw new Exception("Invalid Number of Arguments, got " + numberOfArguments + " argument(s)");	
		}
	}

	/**
	 * This will trim off any start and end quote marks from the string (if it has)
	 * 
	 * @author Wilson Li
	 * @param value - a string that could contain a beginning and ending quote marks 
	 * @return - a string that does not contain any beginning and ending quote marks
	 */
	private static String trimBorderQuoteMarks(String value) {
		if (value.substring(0, 1).equals("\"") && value.substring(value.length() - 1, value.length()).equals("\"")) {
			value = value.substring(1, value.length() - 1);
			return value;
		} else {
			return value;
		}
	}


	/**
	 * Get values from the properties file/System property with cascading functionality
	 * Precedence (left-to-right): System property > properties file > default value 
	 * 
	 * @author Wilson Li
	 * @param props
	 * @param defaultValue
	 * @param key
	 * @return a string representing the value from a given key
	 */
	private static String getCascadingPropertyValue(Properties props, String defaultValue, String key) {
		String value = defaultValue;
		if (props != null) {
			if (props.containsKey(key) && value.equals("null"))
				value = props.getProperty(key);
			if (props.containsKey(key) && !value.equals("null"))
				value = props.getProperty(key);
			if (System.getProperties().containsKey(key))
				value = System.getProperty(key);
			if (!props.containsKey(key) && value.equals("null")) {
				printErrorMsg("System's property or property file does not contain such key: " + key);
				System.exit(0);
			}
		} else {
			if (System.getProperties().containsKey(key))
				value = System.getProperty(key);
		}
		printMsg("Using value: '" + value + "' for key: " + "'" + key + "'.");
		return value;
	}

	/**
	 * Gets the Module string from the file name (e.g. Accounts_0123.java = Accounts)
	 * 
	 * @author Wilson Li
	 * @param fileName
	 * @return a string representing the the module name
	 */
	private static String getFileModuleName(String fileName) {
		String fileModuleName = "";
		for (int index = 0; index < fileName.length(); index++) {
			String character = fileName.substring(index, index + 1);
			if (character.equals("_"))
				break;
			else {
				fileModuleName += character;
			}
		}
		return fileModuleName;
	}

	/**
	 * Check whether a module name (String) exists in the listOfDatabaseModules list 
	 * 
	 * @author Wilson Li
	 * @param moduleName
	 * @return a boolean value, true if the module name exists in the listOfDatabaseModules list, else return false
	 */
	private static boolean isModuleExist(String moduleName) {
		for (String module : listOfDatabaseModules) {
			if (module.equals(moduleName))
				return true;
		}
		return false;
	}

	/**
	 * Read from a file (a list of modules from the given path) and
	 * place them into a list (listOfDatabaseModules)
	 * 
	 * @author Wilson Li
	 * @param path - can either be a path to a file containing a list of modules or 
	 * it can just be a single module name (ie, Accounts)
	 */
	private static void populateListOfModules(String path) throws Exception {
		BufferedReader BR = null;
		String line = null;
		try {
			File testFile = new File(path);
			if (!testFile.isFile()) {
				printMsg("Using the following module: " + path);
				listOfDatabaseModules.add(path.trim());
			} else {
				printMsg("Obtaining modules from file: " + path);
				BR = new BufferedReader(new FileReader(path));
				while ((line = BR.readLine()) != null) {
					listOfDatabaseModules.add(line.trim());
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (BR != null)
					BR.close();
			} catch (IOException e) {
				throw new Exception(e.getMessage());
			}
		}
	}

	/**
	 * Create a folder with the given path
	 * 
	 * @author Wilson Li
	 * @param path
	 */
	private static void createFolder(String path) {
		File outputFolder = new File(path);
		if (!outputFolder.exists())
			outputFolder.mkdir();
	}
	
	/**
	 * Simple wrapper function to do a System.out.println for Translations
	 * 
	 * @author Wilson Li
	 * @param message
	 */
	private static void printMsg(String message) {
		System.out.println("[Translations]: " + message);
	}

	/**
	 * Simple wrapper function to do a System.out.println for Translations' Errors
	 * @author Wilson Li
	 * @param message
	 */
	private static void printErrorMsg(String message) {
		System.out.println("[Translations ERROR]: " + message);		
	}
}