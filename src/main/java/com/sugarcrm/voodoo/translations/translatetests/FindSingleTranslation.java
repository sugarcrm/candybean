package com.sugarcrm.voodoo.translations.translatetests;

import java.sql.*;
import java.util.ArrayList;

public class FindSingleTranslation {
	private static Connection CONNECTION;
	private static ArrayList<String> MODULES;

	public static void main(String args[]) {
		try {
			CONNECTION = connectToDB();
			System.out.println("Successfully connected to database [add db_name parameter]");
			MODULES = getDBTables();
			Translate(MODULES, "Show More", "ja_JP");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
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

	private static void Translate(ArrayList<String> modules, String en_string, String lang) throws SQLException {
		ResultSet rs = null;

		for (String module : modules) {
			rs = execQuery("SELECT * FROM " + module + " WHERE en_us='" + en_string + "'");
			while (rs.next()) {
				String translated = rs.getString(lang);
				System.out.println("module: " + module + ", en_us:" + en_string + ", " + lang + ": " + translated);
			}
		}
	}

	private static ResultSet execQuery(String query) throws SQLException {
		PreparedStatement ps = CONNECTION.prepareStatement(query);
		return ps.executeQuery();
	}
}
