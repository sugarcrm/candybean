package com.sugarcrm.voodoo.translations.translatetests;

import java.sql.*;
import java.util.ArrayList;

public class FindSingleTranslation {
	private static Connection connection;
	private static ArrayList<String> modules;
	private static String dbServer = "10.8.31.10";
	private static String dbName = "Translations_6_7_latest";

	public static void main(String args[]) {
		try {
			String english = args[0];
			String language = args[1];
			System.out.println("Translating '" + english + "' to " + language + " using " + dbServer + "/" + dbName + ".\n");
			connection = connectToDB();
			modules = getDBTables();
			Translate(modules, english, language);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Connection connectToDB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://" + dbServer + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8", "translator", "Sugar123!");
	}

	private static ArrayList<String> getDBTables() throws SQLException {
		DatabaseMetaData dbmd = connection.getMetaData();
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
		PreparedStatement statement = null; 
		ResultSet rs = null;

		for (String module : modules) {
			statement = connection.prepareStatement("SELECT * FROM " + module + " WHERE en_us='" + en_string + "'");
			rs = statement.executeQuery();
			while (rs.next()) {
				String translated = rs.getString(lang);
				System.out.println(module + ": " + translated);
			}
		}
	}
}
