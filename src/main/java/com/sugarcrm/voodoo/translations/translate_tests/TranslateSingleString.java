package com.sugarcrm.voodoo.translations.translate_tests;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class TranslateSingleString {
	private static Connection CONNECTION;
	private static ArrayList<String> MODULES;
	private static ArrayList<String> ENTRIES;

	public static void main(String args[]) {
		try {
			CONNECTION = connectToDB();
			System.out.println("Successfully connected to database");
			MODULES = getDBTables();
			Translate(MODULES, args[0], args[1]);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Connection connectToDB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://10.8.31.10/Translations_6_7?useUnicode=true&characterEncoding=utf-8", "translator", "Sugar123!");
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

	private static void Translate(ArrayList<String> modules, String en_string, String lang) throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = null;
		ResultSet rs2 = null;

		for (String module : modules) {
			rs = execQuery("SELECT * FROM " + module + " WHERE en_us='" + en_string + "'");
			rs2 = execQuery("SHOW COLUMNS FROM " + module + " WHERE FIELD='" + lang + "'");
			if (rs2.next()) {
				while (rs.next()) {
					String label = rs.getString("Label");
					String translated = rs.getString(lang);
					System.out.println("Module: " + module + ", Label: " + label + "\n\ten_us: " + en_string + "\n\t" + lang + ": " + translated);
				}
			}
		}
	}

	private static ResultSet execQuery(String query) throws SQLException {
		PreparedStatement ps = CONNECTION.prepareStatement(query);
		return ps.executeQuery();
	}
}
