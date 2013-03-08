package com.sugarcrm.voodoo.translations.find_duplicates;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import com.sugarcrm.voodoo.utilities.Utils;

public class FindDuplicateEntries {
	private static ArrayList<String> MODULES;
	private static ArrayList<String> ENTRIES;

	public static void main(String args[]) {
		try {
			Utils.connectToDB("10.8.31.10", "Translations_6_7_latest", "translator", "Sugar123!");
			System.out.println("Successfully connected to database [add db_name parameter]");
			MODULES = Utils.getTables();
			ENTRIES = getAllENEntries(MODULES);
			writeDuplicates(ENTRIES, "/var/lib/jenkins/DuplicateEntries.txt");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

	private static void writeDuplicates(ArrayList<String> db_entries, String output_file) throws SQLException, IOException {
		String last_label = null;
		String last_value = null;
		String last_module = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(output_file)); 
		Boolean written_last = false;

		for (int i = 0; i < db_entries.size(); i++) {
			if (i == 0) {
				String[] s = db_entries.get(i).split("===", 3);
				last_label = s[2];
				last_value = s[0];
				last_module = s[1];
			}
			String current_label = db_entries.get(i).split("===")[2];
			String current_value = db_entries.get(i).split("===")[0];
			String current_module = db_entries.get(i).split("===")[1];
			if (i > 0 && last_value.equals(current_value)) {
				if (!written_last) {
					bw.write("\nIn module " + last_module + ", " + last_label + "='" + last_value + "'\n");
					written_last = true;
				}
				bw.write("In module " + current_module + ", " + current_label + "='" + current_value + "'\n");
			} else {
				last_label = current_label;
				last_value = current_value;
				last_module = current_module;
				written_last = false;
			}
		}
		bw.close();
	}
}
