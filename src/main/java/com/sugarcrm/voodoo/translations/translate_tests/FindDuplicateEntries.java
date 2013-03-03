package com.sugarcrm.voodoo.translations.translate_tests;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class FindDuplicateEntries {
	private static Connection CONNECTION;
	private static ArrayList<String> tables;

	public static void main(String args[]) {
		try {
			CONNECTION = connectToDB();
			System.out.println("Successfully connected to database [add db_name parameter]");
			tables = getDBTables();
			writeDuplicates(tables);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Connection connectToDB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/Translations_6_7?useUnicode=true&characterEncoding=utf-8", "root", "root");
	}

	private static ArrayList<String> getDBTables() throws SQLException {
		DatabaseMetaData dbmd = CONNECTION.getMetaData();
		ArrayList<String> tables = new ArrayList<String>();
		String[] types = { "TABLE" };
		ResultSet resultSet = dbmd.getTables(null, null, "%", types);
		while (resultSet.next()) {
			String tableName = resultSet.getString("TABLE_NAME");
			tables.add(tableName);
		}
		return tables;
	}

	private static ArrayList<String> getENTexts(String module) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = null;

		rs = execQuery("SELECT Label, en_us FROM " + module);
		while (rs.next()) {
			String label = rs.getString("Label");
			String value = rs.getString("en_us");
			if (label == null || value == null) {
				System.out.println("Label: " + label + ", en_us: " + value);
			} else {
				result.add(value + "=" + label);
			}
		}
		Collections.sort(result);
		return result;
	}

	private static void writeDuplicates(ArrayList<String> modules) throws SQLException, IOException {
		ArrayList<String> texts = new ArrayList<String>();
		ResultSet rs = null;
		String last_label = null;
		String last_value = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter("/var/lib/jenkins/DuplicateEntries.txt"));

		for (String module : modules) {
			bw.write("Inside module: " + module);
			texts = getENTexts(module);
			for (int i = 0; i < texts.size(); i++) {
				
				if (i == 0) {
					String[] s = texts.get(i).split("=");
					last_label = s[1];
					last_value = s[0];
				}
				String current_label = texts.get(i).split("=")[1];
				String current_value = texts.get(i).split("=")[0];
				if (i > 0 && last_value.equals(current_value)) {
					bw.write(current_label + "=" + current_value + "\n");
				} else {
					last_value = current_value;
				}
			}
			bw.write("\n");
		}
	}

	private static ResultSet execQuery(String query) throws SQLException {
		PreparedStatement ps = CONNECTION.prepareStatement(query);
		return ps.executeQuery();
	}
}