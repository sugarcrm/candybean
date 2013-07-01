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
package com.sugarcrm.voodoo.translations.findsuitabletests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sugarcrm.voodoo.translations.findsuitabletests.FindDuplicateEntries;
import com.sugarcrm.voodoo.utilities.Utils;

public class FindAffectedSodaTests {
	private static ArrayList<String> MODULES;
	private static ArrayList<String> EN_ENTRIES;
	private static ArrayList<String> DUP_ENTRIES;
	public static ArrayList<String> AFFECTED_FILES;

	private static Pattern LINK_PATTERN = Pattern
			.compile("link text=\"(.*?)\"");
	private static Pattern ASSERT_PATTERN = Pattern
			.compile("browser assert=\"(.*?)\"");
	private static Matcher LINK_MATCHER;
	private static Matcher ASSERT_MATCHER;
	private static String LINK_MATCH;
	private static String ASSERT_MATCH;

	private static String INPUT_PATH;
	private static String OUTPUT_PATH;
	private static File OUTPUT;
	private static String MODE;

	private static Scanner SCANNER;
	private static BufferedWriter BFWRITER;

	private static Connection CONNECTION;
	private static String DB_SERVER;
	private static String DB_NAME;
	private static String DB_USER;
	private static String DB_PASS;

	public static void main(String[] args) {
		try {
			DB_SERVER = args[0];
			DB_NAME = args[1];
			DB_USER = args[2];
			DB_PASS = args[3];
			INPUT_PATH = args[4];
			OUTPUT_PATH = args[5];
			MODE = args[6];

			CONNECTION = Utils.getDBConnection(DB_SERVER, DB_NAME, DB_USER,
					DB_PASS);
			System.out.println("Connected to " + DB_SERVER
					+ ", using database " + DB_NAME + "\n");

			OUTPUT = new File(OUTPUT_PATH);

			MODULES = Utils.getTables(CONNECTION);
			EN_ENTRIES = FindDuplicateEntries.getAllENEntries(MODULES, CONNECTION);
			DUP_ENTRIES = FindDuplicateEntries.getDupEntries(EN_ENTRIES);

			if (MODE.equals("write"))
				BFWRITER = new BufferedWriter(new FileWriter(OUTPUT));
			recursivelyFindAffectedTests(INPUT_PATH, MODE);
			if (MODE.equals("write"))
				BFWRITER.close();
			
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static boolean isDuplicate(String text) {
		for (String entry : DUP_ENTRIES) {
			String[] s = entry.split("===", 3);
			String value;

			if (s[0].contains("\n")) {
				value = s[0].substring(1);
			} else {
				value = s[0];
			}

			if (text.equals(value)) {
				return true;
			}
		}

		return false;
	}

	private static void writeAffectedFile(String testFile) throws IOException {
		SCANNER = new Scanner(new File(testFile));
		boolean foundDup = false;
		boolean written = false;
		int lineNumber = 0;

		while (SCANNER.hasNext()) {
			lineNumber++;
			String line = SCANNER.nextLine();
			LINK_MATCHER = LINK_PATTERN.matcher(line);
			ASSERT_MATCHER = ASSERT_PATTERN.matcher(line);

			if (LINK_MATCHER.find()) {
				LINK_MATCH = LINK_MATCHER.group(1);
				if (isDuplicate(LINK_MATCH)) {
					foundDup = true;
					if (foundDup && !written) {
						BFWRITER.write("\nInside " + testFile + "\n");
						written = true;
					}
					BFWRITER.write("\tDuplicate entry '" + LINK_MATCH
							+ "' found on line " + lineNumber + ":\n\t\t" + line.trim() + "\n");
				}
			}
			if (ASSERT_MATCHER.find()) {
				ASSERT_MATCH = ASSERT_MATCHER.group(1);
				if (isDuplicate(ASSERT_MATCH)) {
					foundDup = true;
					if (foundDup && !written) {
						BFWRITER.write("\nInside " + testFile + "\n");
						written = true;
					}
					BFWRITER.write("\tDuplicate entry '" + ASSERT_MATCH
							+ "' found in line " + lineNumber + ":\n\t\t" + line.trim() + "\n");
				}
			}
		}
	}

	private static void addAffectedFile(String testFile) throws IOException {
		SCANNER = new Scanner(new File(testFile));
		boolean foundDup = false;
		boolean written = false;

		while (SCANNER.hasNext()) {
			String line = SCANNER.nextLine();
			LINK_MATCHER = LINK_PATTERN.matcher(line);
			ASSERT_MATCHER = ASSERT_PATTERN.matcher(line);

			if (LINK_MATCHER.find()) {
				LINK_MATCH = LINK_MATCHER.group(1);	
				if (isDuplicate(LINK_MATCH)) {
					foundDup = true;
					if (foundDup && !written) {
						AFFECTED_FILES.add(testFile);
						written = true;
					}
				}
			}
			if (ASSERT_MATCHER.find()) {
				ASSERT_MATCH = ASSERT_MATCHER.group(1);
				if (isDuplicate(ASSERT_MATCH)) {
					foundDup = true;
					if (foundDup && !written) {
						AFFECTED_FILES.add(testFile);
						written = true;
					}
				}
			}
		}
	}

	private static void recursivelyFindAffectedTests(String pathString, String mode)
			throws IOException {
		File path = new File(pathString);
		String fileName = path.getName();
		if (mode.equals("add") && AFFECTED_FILES == null) {
			System.out.println("Initializing AFFECTED_FILES");
			AFFECTED_FILES = new ArrayList<String>();
		}

		if (path.isFile() && fileName.contains(".xml")) {
			if (mode.equals("add"))
				addAffectedFile(pathString);
			if (mode.equals("write"))
				writeAffectedFile(pathString);
			
		} else if (path.isDirectory()) {
			File[] files = path.listFiles();

			for (File file : files) {
				recursivelyFindAffectedTests(pathString + File.separator
						+ file.getName(), mode);
			}

		}
	}
}
