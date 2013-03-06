package com.sugarcrm.voodoo.translations.translate_tests;

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
import java.util.Arrays;

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
	private static String TEST_FORMAT;

	// if the string to be replace is not in such module then search through all DB modules
	// currently disabled, set variable to 'true' to enable
	private static boolean SEARCH_ALL_MODULES = true;

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
	public static void Translate(String db, String mod, String lang, String testP) {
		DATABASE = db;
		//MODULE = mod;
		LANGUAGE = lang;
		TEST_PATH = testP;
		String defaultOutputPath = System.getProperty("user.dir") + File.separator	+ "translated_Files_" + LANGUAGE;
		OUTPUT_FOLDER = getCascadingPropertyValue(null, defaultOutputPath, "translate.output");
		try {
			DB_CONNECTION = connectToDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//populateListOfModules(MODULE);			
		try {
			createLoginXMLFile(TEST_PATH + "/../../login_translate.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createFolder(OUTPUT_FOLDER);
		try {
			recursePathForTranslations(TEST_PATH, OUTPUT_FOLDER);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public static void Translate(String propPath) {
		translateProp = new Properties();
		try {
			translateProp.load((new FileInputStream(propPath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DATABASE = getCascadingPropertyValue(translateProp, "", "translate.database");
		//MODULE = getCascadingPropertyValue(translateProp, "", "translate.module");
		LANGUAGE = getCascadingPropertyValue(translateProp, "", "translate.language");
		TEST_PATH = getCascadingPropertyValue(translateProp, "", "translate.testpath");
		OUTPUT_FOLDER = getCascadingPropertyValue(translateProp, "", "translate.output");
		try {
			DB_CONNECTION = connectToDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//populateListOfModules(MODULE);
		try {
			createLoginXMLFile(TEST_PATH + "/../../login_translate.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createFolder(OUTPUT_FOLDER);

		try {
			recursePathForTranslations(TEST_PATH, OUTPUT_FOLDER);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws Exception
	 */
	private static void recursePathForTranslations(String testPath, String outputFolder) throws SQLException, IOException {
		File inputFile = new File(testPath);
		//System.out.println("Current path: " + testPath);
		if (inputFile.isFile()) { // testPath: Path to a test file
			//System.out.println("   IS FILE");
			String outputSubFolder = outputFolder + File.separator + inputFile.getName();
			String moduleFileName = getFileModuleName(inputFile.getName());
			// If the inputFile is a file that contains a name from the module(s) and is of java format then perform translation
			if (setAndCheckTestFormat(inputFile.getName())) {
				// perform translation
				fileReaderWriter(moduleFileName, testPath, outputSubFolder);
			}
		} else { // testPath: path to a folder containing test file(s)
			//System.out.println("   IS FOLDER");
			File[] files = new File(testPath).listFiles();
			// System.out.println(Arrays.deepToString(files));
			for (File file : files) {
				String testPathSubFolder = file.getAbsolutePath();
				String outputSubFolder = outputFolder + File.separator + file.getName();
				String moduleFileName = getFileModuleName(file.getName());
				// If the item is a directory, then recurse the function with the item's path
				if (file.isDirectory()) {
					createFolder(outputSubFolder);
					recursePathForTranslations(file.getAbsolutePath(), outputSubFolder);
				}
				else if (file.isFile() && setAndCheckTestFormat(file.getName())) {
					// perform translation
					fileReaderWriter(moduleFileName, testPathSubFolder, outputSubFolder);
				}
			}
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
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws Exception
	 */
	private static void fileReaderWriter(String module, String inputFile, String outputFile) throws SQLException, IOException {
		File file = new File(inputFile);
		Scanner fileScanner = new Scanner(file);
		Writer output = null;

		String assertType;
		if (TEST_FORMAT.equals("JAVA")) { assertType = "assertEquals\\((.*?)\\)"; }//JAVA FORMAT
		else { assertType = "assert=\"(.*?)\""; }//XML FORMAT

		Pattern pattern_assert = Pattern.compile(assertType); // set depending on test file format
		//For Voodoo (test with .xml format) use only
		Pattern pattern_link = Pattern.compile("link text=\"(.*?)\"");
		Pattern pattern_moduletab = Pattern.compile("id=\"moduleTab_(" + module + ")\"");
		Pattern pattern_menuextra_all = Pattern.compile("id=\"moduleTabExtraMenu(All)\"");
		Pattern pattern_variable = Pattern.compile("\\{");
		Pattern pattern_pageNumber = Pattern.compile("\\(");
		Matcher match_assert = null;
		Matcher match_link = null;
		Matcher match_moduletab = null;
		Matcher match_menuextra_all = null;
		Matcher match_variable = null;
		Matcher match_pageNumber = null;


		System.out.println("Writing file: " + outputFile);
		output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			//System.out.println("Scanned line: " + line);
			match_assert = pattern_assert.matcher(line);

			// XML use only
			match_link = pattern_link.matcher(line);
			match_moduletab = pattern_moduletab.matcher(line);
			match_menuextra_all = pattern_menuextra_all.matcher(line);
			match_variable = pattern_variable.matcher(line);
			match_pageNumber = pattern_pageNumber.matcher(line);

			if (match_assert.find()) { // Assert replacement
				if (TEST_FORMAT.equals("JAVA")) {
					String stringToBeReplaced = getToBeReplacedAssertString(match_assert.group(1));
					String ReplacementString = getDatabaseReplacementString(module, stringToBeReplaced);
					String newLine = line.replace(stringToBeReplaced, ReplacementString);
					output.write(newLine + "\r\n");
				} else {
					if (match_variable.find() || match_pageNumber.find()){
						//printErrorMsg("Not finding translation for " + match_assert.group(1));
						output.write(line + "\r\n");
					} else { // no strange characters, therefore proceed in translation
						System.out.println("ASSERT MATCH");
						String newLine = line.replace(match_assert.group(1), getDatabaseReplacementString(module, match_assert.group(1)));
						output.write(newLine + "\r\n");
					}
				}
			} else if (match_link.find()) { // Link replacement for (xml only)
				System.out.println("LINK MATCH");
				if(match_variable.find() || match_pageNumber.find()){
					//printErrorMsg("Not finding translation for " + match_link.group(1));
					output.write(line + "\r\n");
				} else {
					//System.out.println("Replacing link text");
					//System.out.println(match_link.group(1));
					//System.out.println(getDatabaseReplacementString(module, match_link.group(1))); // ERRORS WHEN THIS IS NULL
					String newLine = line.replace(match_link.group(1), getDatabaseReplacementString(module, match_link.group(1)));
					output.write(newLine + "\r\n");
				}
			} else if (match_moduletab.find()) { // match_moduletab replacement for (xml only)
				System.out.println("MODULETAB MATCH");
				String newLine = line.replace(match_moduletab.group(1), getDatabaseReplacementString("SugarFeed", "All") + "_" + module);
				output.write(newLine);
			} else if (match_menuextra_all.find()) { // match_menuextra_all replacement for (xml only)
				System.out.println("MENU MATCH");
				String newLine = line.replace(match_menuextra_all.group(1), getDatabaseReplacementString("SugarFeed", match_menuextra_all.group(1)));
				output.write(newLine);
			} else {
				//System.out.println("NO MATCH");
				output.write(line + "\r\n");
			}
		}
		output.close();
		fileScanner.close();
		//System.out.println("Finished writing");
	}

	/**
	 * Establish connection to the database
	 * 
	 * @author Wilson Li
	 * @return - a MySQL connection object that is used for database queries
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws Exception
	 */
	private static Connection connectToDatabase() throws ClassNotFoundException, SQLException {
		Connection con = null;
		String serverName = getCascadingPropertyValue(translateProp, "", "translate.serverName");
		String username = getCascadingPropertyValue(translateProp, "", "translate.username");
		String password = getCascadingPropertyValue(translateProp, "", "translate.password");
		printMsg("Creating database connection: \n\tDatabase: " + serverName + "\n\tUsername: " + username + "\n\tPassword: " + password);
		// Create a connection to the database
		String driverName = "com.mysql.jdbc.Driver"; // MySQL MM JDBC driver
		Class.forName(driverName);
		String url = "jdbc:mysql://" + serverName + File.separator + DATABASE; // a JDBC url
		con = DriverManager.getConnection(url, username, password);
		printMsg("Connection to database successfull!");
		return con;

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
	 * @throws SQLException 
	 * 
	 */
	private static String getDatabaseReplacementString(String module, String englishString) throws SQLException {
		String result = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		String query = "SELECT " + LANGUAGE + " FROM " + module + " WHERE english = " + "\'" + englishString + "\'";
		pst = DB_CONNECTION.prepareStatement(query);
		rs = pst.executeQuery();

		// if there is a result due to the query, the translated value is returned. 
		if (rs.next()) {
			result = (rs.getString(1));
			printMsg("Replaced English: '" + englishString + "' with " + LANGUAGE + ": '" + result + "'" + " from " + module + " module");
		} else if (SEARCH_ALL_MODULES){  // Search through the rest of the modules
			result = searchAllModules(englishString);
		}
		return result;
	}

	/**
	 * Perform a Query search on all modules. This can be called when a string is not found 
	 * on a specified module. However it is currently disabled. Enable this feature by changing 
	 * the constant SEARCH_ALL_MODULES to true.
	 * 
	 * @author Wilson Li
	 * @param englishString
	 * @return the translated string
	 * @throws SQLException 
	 * @throws Exception
	 */
	private static String searchAllModules(String englishString) throws SQLException {
		String result = englishString;
		String[] tables = getAllModuleNamesFromDB();

		for (String table : tables) {
			PreparedStatement pst_ifExists = DB_CONNECTION.prepareStatement("SHOW columns from `" + table + "` where field='" + LANGUAGE + "'");
			ResultSet rs_ifExists = pst_ifExists.executeQuery();

			// checks to see if the language is in that particular module
			if (rs_ifExists.next()) {
				PreparedStatement pst_temp = null;
				ResultSet rs_temp = null;

				pst_temp = DB_CONNECTION.prepareStatement("SELECT " + LANGUAGE + " FROM " + table + " WHERE en_us ='" + englishString + "'");
				rs_temp = pst_temp.executeQuery();
				if (rs_temp.next()) {
					result = (rs_temp.getString(1));
					while (result == null && rs_temp.next()) {
						result = rs_temp.getString(1);
						printErrorMsg("Database translation entry is 'null', look for possible translation in next module");
					} 
					if (result != null) {
						printMsg("Replaced English: '" + englishString + "' with " + LANGUAGE + ": '" + result + "' from the '" + table + "' module");
						break;
					} 
				}
				else {
					if (table == "WorkFlowTriggerShells") {
						printMsg("Could not find the translation for '" + englishString + "'.");
					}
					//printErrorMsg("Could not find the translation for " + englishString + " in the " + table + " module");
					rs_temp.close();
					pst_temp.close();
				} 
			}
			else {
				printErrorMsg("The table: " + table + " does not contain the language: " + LANGUAGE);
			}
		}
		return result;
	}


	/**
	 * Get an array of all the module names from the Database
	 * 
	 * @author Wilson Li
	 * @return an array of module names within the database that is being queried
	 * @throws SQLException 
	 */
	private static String[] getAllModuleNamesFromDB() throws SQLException {
		int counter = 0;
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
	 * @throws Exception 
	 */
	private static String getToBeReplacedAssertString(String assertStr) {
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
	private static void setAssertPosition(int numberOfArguments) {
		switch (numberOfArguments) {
		case 2: ASSERT_POSITION = 0; break; // assertEquals(expectedValue, actualValue); 
		case 3:	ASSERT_POSITION = 1; break; // assertEquals(message, expectedValue, actualValue);
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
			if (props.containsKey(key) && value.equals(""))
				value = props.getProperty(key);
			if (props.containsKey(key) && !value.equals(""))
				value = props.getProperty(key);
			if (System.getProperties().containsKey(key))
				value = System.getProperty(key);
			if (!props.containsKey(key) && value.equals("")) {
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
		System.out.println(message);
	}

	/**
	 * Simple wrapper function to do a System.out.println for Translations' Errors
	 * @author Wilson Li
	 * @param message
	 */
	private static void printErrorMsg(String message) {
		System.out.println("ERROR: " + message);		
	}

	/**
	 *   * This is to verify if the test file is either .java or .xml to 
	 *       * perform translations accordingly
	 *           * 
	 *               * @param filename
	 *                   */
	private static boolean setAndCheckTestFormat(String filename) {
		if (filename.contains(".java")) { 
			TEST_FORMAT = "JAVA";
		}
		else if (filename.contains(".xml")) { 
			TEST_FORMAT = "XML";
		}
		else { 
			return false;   
		}
		return true;
	}

	private static void createLoginXMLFile(String outputFile) throws IOException {
		File file = new File(outputFile);
		Writer output = null;
		if (file.exists()) {
			System.out.println("\nFile login_translate.xml exists\n");
		} else {	
			System.out.println("Writing login_translate.xml: " + outputFile);
			output = new BufferedWriter(new FileWriter(outputFile));
			output.write("<soda>" +
					"\n\t<script file=\"tests/modules/lib/login.xml\" />" +
					"\n\t<var var=\"nav_action\" set=\"admin_link\"/>" +
					"\n\t<script file=\"{@global.scriptsdir}/modules/lib/Nav_UserActions.xml\" />" +
					"\n\t<link text=\"Locale\"/>" +
					"\n\t<select name=\"default_language\" setreal=\"" + LANGUAGE + "\" />" +
					"\n\t<button name=\"save\" required=\"false\" timeout=\"2\" />" +
					"\n\t<script file=\"tests/modules/lib/logout.xml\" />" +
					"\n\t<script file=\"tests/modules/lib/browserclose.xml\" />" +
					"\n</soda>");
		}
		output.close();
	}
}
