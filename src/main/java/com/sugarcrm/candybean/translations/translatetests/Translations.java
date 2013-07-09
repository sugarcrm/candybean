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
package com.sugarcrm.candybean.translations.translatetests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

public class Translations {
	private static String language;
	private static Connection dbConnection;
	private static ArrayList<String> tables;
	private static Pattern pattern_assert = Pattern
			.compile("browser assert=\"(.*?)\"");
	private static Pattern pattern_link = Pattern.compile("link text=\"(.*?)\"");
	private static Pattern pattern_variable = Pattern.compile("\\{");
	private static Pattern pattern_pageNumber = Pattern.compile("\\(");
	private static Matcher match_assert;
	private static Matcher match_link;
	private static Matcher match_variable;
	private static Matcher match_pageNumber;
	private static Boolean testErrorFree;
	private static ArrayList<String> errorFreeTests = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			if (args.length == 1)
				Translate(args[0]);
			else if (args.length == 7)
				Translate(args[0], args[1], args[2], args[3], args[4], args[5],
						args[6]);
			else {
				System.out
				.println("Invalid number of arguments. Given number of arguments: "
						+ args.length);
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Perform translations using a Configuration or Properties file.
	 * 
	 * @author ylin
	 * 
	 * @param configPath
	 */
	private static void Translate(String configPath) {
		Configuration config = new Configuration(configPath);
		try {


			String dbServer = config.getValue("translations.database_server");
			String dbName = config.getValue("translations.database_name");
			String dbUser = config.getValue("translations.database_user");
			String dbPass = config.getValue("translations.database_pass");
			dbConnection = Utils.getDBConnection(dbServer, dbName, dbUser,
					dbPass);

			language = config.getValue("translations.language");
			String testPath = config.getValue("translations.test_path");
			String outputPath = config.getValue("translations.output_path");

			recursivelyTranslateFilesInDirectory(testPath, outputPath);
			
			writeErrorFreeList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Perform translations using command-line arguments.
	 * 
	 * @author ylin
	 * 
	 * @param dbServer
	 * @param dbName
	 * @param dbUser
	 * @param dbPass
	 * @param lang
	 * @param testPath
	 * @param outputPath
	 */
	private static void Translate(String dbServer, String dbName,
			String dbUser, String dbPass, String lang, String testPath,
			String outputPath) {
		try {
			dbConnection = Utils.getDBConnection(dbServer, dbName, dbUser,
					dbPass);

			language = lang;

			recursivelyTranslateFilesInDirectory(testPath, outputPath);

			writeErrorFreeList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recursively translate all test files in a given directory.
	 * 
	 * @author ylin
	 * 
	 * @param testPath
	 * @param outputPath
	 * @throws Exception
	 */
	private static void recursivelyTranslateFilesInDirectory(String testPath,
			String outputPath) throws Exception {
		File testDir = new File(testPath);

		// prevent path error in the case of using ~ in path name
		// if ~ is used, path is interpreted as relative instead of expanding to
		// the absolute path of home
		if (!testPath.equals(testDir.getAbsolutePath())) {
			throw new Exception(
					"testPath and the path of testDir are not equal! Try using an absolute path.");
		}

		// new output path
		String currentPath = outputPath + File.separator + testDir.getName();

		// found xml test file, perform translation
		if (testDir.isFile() && testDir.getName().contains(".xml")) {
			System.out.println("Translating file: "
					+ testPath.split("tests/")[1]);
			translateTest(testDir, currentPath);
		} 

		// found directory, recurse
		else if (testDir.isDirectory()) {

			// create output path if necessary
			File currentDir = new File(currentPath);
			if (!currentDir.exists()) {
				currentDir.mkdirs();
			}

			File[] files = testDir.listFiles();
			for (File file : files) {
				recursivelyTranslateFilesInDirectory(file.getAbsolutePath(),
						currentPath);
			}
		}
	}

	/**
	 * Translate a single test file line by line.
	 * 
	 * @author ylin
	 * 
	 * @param testPath
	 * @param outputPath
	 * @throws Exception
	 */
	private static void translateTest(File testFile, String outputPath) {
		try {
			testErrorFree = true;
			Scanner scanner = new Scanner(testFile);

			// encode in UTF-8
			Writer output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputPath), "UTF-8"));

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				match_assert = pattern_assert.matcher(line);
				match_link = pattern_link.matcher(line);
				match_variable = pattern_variable.matcher(line);
				match_pageNumber = pattern_pageNumber.matcher(line);

				// found something other than plain text, no translation
				// necessary
				if (match_variable.find() || match_pageNumber.find()) {
					output.write(line + "\n");
				}

				// found <browser assert=...>
				else if (match_assert.find()) {
					String englishText = match_assert.group(1);
					//					String adjustedText = englishText.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
					//					if (!englishText.equals(adjustedText)) System.out.println("\t\tAfter replacing quotes: " + englishText);
					String translation = getTranslationFromDB(englishText);
					//					String adjustedTranslation = translation.replaceAll("\\\\'", "'").replaceAll("\\\\\"", "\"");
					String newLine = line.replace(englishText, translation);
					output.write(newLine + "\n");
				}

				// found <link text=...>
				else if (match_link.find()) {
					String englishText = match_link.group(1);
					//					String adjustedText = englishText.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"");
					//					if (!englishText.equals(adjustedText)) System.out.println("\t\tAfter replacing quotes: " + englishText);
					String translation = getTranslationFromDB(englishText);
					//					String adjustedTranslation = translation.replaceAll("\\\\'", "'").replaceAll("\\\\\"", "\"");
					String newLine = line.replace(englishText, translation);
					output.write(newLine + "\n");
				} else {
					output.write(line + "\n");
				}
			}
			if (testErrorFree) addToErrorFreeList(testFile.getAbsolutePath().split("tests/")[1]);
			Utils.closeStream(scanner);
			Utils.closeStream(output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the translation of an English string from a database containing all
	 * language strings. See translations/updatedatabase for how the database is
	 * created/updated.
	 * 
	 * @author ylin
	 * 
	 * @param englishText
	 * @return
	 * @throws Exception
	 */
	private static String getTranslationFromDB(String englishText) {
		String result = englishText;

		try {
			tables = Utils.getTables(dbConnection);

			for (String table : tables) {

				// query to get the translation language column
				PreparedStatement statement1 = dbConnection.prepareStatement("SHOW COLUMNS FROM "
						+ table + " WHERE FIELD='" + language + "'");
				ResultSet rs = statement1.executeQuery();

				// translation language exists
				if (rs.next()) {

					// query to get all translations for englishText
					PreparedStatement statement2 = dbConnection.prepareStatement("SELECT "
							+ language + " FROM " + table + " WHERE en_us ='"
							+ englishText + "'");
					ResultSet translations = statement2.executeQuery();

					// translations exist (1 or more)
					if (translations.next()) {
						result = (translations.getString(1));

						// found valid translation
						if (result != null) {
							System.out.println("\tReplaced '" + englishText
									+ "' with '" + result + "'.");
							break;
						}

						// invalid translation (null), try next translation if
						// it exists
						while (translations.next()) {
							result = translations.getString(1);

							// use first translation that is not null, stop
							// iterating through translations
							if (result != null) {
								System.out.println("\tReplaced '" + englishText
										+ "' with '" + result + "'.");
								break;
							}
						}
					}
					statement2.close();
				}

				// translation language does not exist
				else {
					testErrorFree = false;
					System.out.println("\tThe table: " + table
							+ " does not contain the language: " + language);
				}
				statement1.close();

				// use first translation that is not null, stop iterating
				// through tables
				if (result != null && !result.equals(englishText))
					break;

				// translation not found*
				// *If a valid translation for englishText is englishText
				// itself and it is found in the last table,
				// WorkFlowTriggerShells, this message is incorrect. However,
				// the translated file is still correct.
				if ((result == null || result.equals(englishText))
						&& table.equals("WorkFlowTriggerShells")) {
					testErrorFree = false;
					if (result == null)
						result = englishText;
					System.out.println("\tCould not find the translation for '"
							+ englishText + "'.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static void addToErrorFreeList(String testName) {
		errorFreeTests.add(testName);
	}

	private static void writeErrorFreeList() {
		try {
			Writer output = new BufferedWriter(new FileWriter(new File("/home/suga/ErrorFreeTranslatedTests.txt")));
			
			Collections.sort(errorFreeTests);
			for (int i = 0; i < errorFreeTests.size(); i++) {
				output.write(errorFreeTests.get(i) + "\n");
			}
			
			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
