package com.sugarcrm.voodoo.translations.find_duplicates;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import com.sugarcrm.voodoo.utilities.Utils;

public class FindDuplicateEntries {
	private static ArrayList<String> MODULES;
	private static ArrayList<String> EN_ENTRIES;
	private static ArrayList<String> DUP_ENTRIES;

	public static void main(String args[]) {
		try {
			Utils.connectToDB("10.8.31.10", "Translations_6_7_latest", "translator", "Sugar123!");
			System.out.println("Successfully connected to database [add db_name parameter]");
			MODULES = Utils.getTables();
			EN_ENTRIES = getAllENEntries(MODULES);
			DUP_ENTRIES = getDupEntries(EN_ENTRIES);
			writeDuplicates(DUP_ENTRIES, "/var/lib/jenkins/DuplicateEntries.txt");

		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getAllENEntries(ArrayList<String> modules) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = null;

		for (String module : modules) {
			rs = Utils.execQuery("SELECT Label, en_us FROM " + module);
			while (rs.next()) {
				String label = rs.getString("Label");
				String value = rs.getString("en_us");
				if (label == null || value == null) {
					System.out.println("module: " + module + ", Label: " + label + ", en_us: " + value);
				} else {
					//adding value first, module second, the result of sort is a list that is first sorted alphabetically by value, then sorted alphabetically by module
					result.add(value + "===" + module + "===" + label);
				}
			}
		}
		Collections.sort(result);
		return result;
	}

	public static ArrayList<String> getDupEntries(ArrayList<String> en_entries) {
		ArrayList<String> result = new ArrayList<String>();
		String last_value = null;
		Boolean added_last = false;

		for (int i = 0; i < en_entries.size(); i++) {
			if (i == 0) {
				String[] s = en_entries.get(i).split("===", 3);
				last_value = s[0];
			}
			String current_value = en_entries.get(i).split("===")[0];
			if (i > 0 && last_value.equals(current_value)) {
				if (!added_last) {
					result.add("\n" + en_entries.get(i));
					added_last = true;
				}
				result.add(en_entries.get(i));
			} else {
				last_value = current_value;
				added_last = false;
			}
		}

		return result;
	}

	private static void writeDuplicates(ArrayList<String> dup_entries, String output_file) throws SQLException, IOException {
		String label = null;
		String value = null;
		String module = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(output_file)); 

		for (int i = 0; i < dup_entries.size(); i++) {
			String[] s = dup_entries.get(i).split("===", 3);
			label = s[2];
			value = s[0];
			module = s[1];
			if (value.contains("\n")) {
				bw.write("\nIn module " + module + ", " + label + "='" + value.substring(1) + "'\n");
			} else {
				bw.write("In module " + module + ", " + label + "='" + value + "'\n");
			}
		}
		bw.close();
	}
}
