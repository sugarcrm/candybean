package com.sugarcrm.voodoo.translations.find_duplicates;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sugarcrm.voodoo.translations.find_duplicates.FindDuplicateEntries;
import com.sugarcrm.voodoo.utilities.Utils;

public class FindAffectedSodaTests {
	private static ArrayList<String> MODULES;
	private static ArrayList<String> EN_ENTRIES;
	private static ArrayList<String> DUP_ENTRIES;
	
	private static Pattern LINK_PATTERN = Pattern.compile("link text=\"(.*?)\"");
	private static Pattern ASSERT_PATTERN = Pattern.compile("assert=\"(.*?)\"");
	private static Matcher LINK_MATCHER;
	private static Matcher ASSERT_MATCHER;
	private static String LINK_MATCH;
	private static String ASSERT_MATCH;
	
	private static String INPUT_PATH;
	private static String OUTPUT_PATH;
	private static File OUTPUT;
	private static Scanner SCANNER;
	private static BufferedWriter BFWRITER;
	
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
			Utils.connectToDB(DB_SERVER, DB_NAME, DB_USER, DB_PASS);
			System.out.println("Connected to " + DB_SERVER + ", using database " + DB_NAME + "\n");
			
			INPUT_PATH = args[4];
			OUTPUT_PATH = args[5];
			OUTPUT = new File(OUTPUT_PATH);
			
			MODULES = Utils.getTables();
			EN_ENTRIES = FindDuplicateEntries.getAllENEntries(MODULES);
			DUP_ENTRIES = FindDuplicateEntries.getDupEntries(EN_ENTRIES);
			recursivelyFindAffectedTests(INPUT_PATH);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private static void isFileAffected(String testFile) throws IOException {
		BFWRITER = new BufferedWriter(new FileWriter(OUTPUT));
		SCANNER = new Scanner(new File(testFile));
		
		System.out.println("\nInside testFile:\n");		
		while (SCANNER.hasNext()) {
			String line = SCANNER.nextLine();
			LINK_MATCHER = LINK_PATTERN.matcher(line);
			ASSERT_MATCHER = ASSERT_PATTERN.matcher(line);
			LINK_MATCHER.find();
			ASSERT_MATCHER.find();
			LINK_MATCH = LINK_MATCHER.group(1);
			ASSERT_MATCH = ASSERT_MATCHER.group(1);
			
			if (DUP_ENTRIES.contains(LINK_MATCH)) {
				BFWRITER.write("\tDuplicate entry '" + LINK_MATCH + "' found in line:\n\t\t" + line + "\n");
			} else if (DUP_ENTRIES.contains(ASSERT_MATCH)) {
				BFWRITER.write("\tDuplicate entry '" + ASSERT_MATCH + "' found in line:\n\t\t" + line + "\n");
			}
		}
	}
	
	private static void recursivelyFindAffectedTests(String pathString) throws IOException {
		File path = new File(pathString);
		
		if (path.isFile()) {
			isFileAffected(pathString);
		} else {
			File[] files = path.listFiles();
			for (File file : files) {
				recursivelyFindAffectedTests(pathString + File.separator + file.getName());
			}
			
		}
		
		BFWRITER.close();
	}
}
