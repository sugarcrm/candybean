package com.sugarcrm.voodoo.translations.translatetests;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import com.sugarcrm.voodoo.utilities.Utils;

public class FindSingleTranslation {
	private static Connection connection;
	private static ArrayList<String> modules;

	public static void main(String args[]) {
		try {
			connection = Utils.getDBConnection("10.8.31.10", "Translations_6_7_latest", "translator", "Sugar123!");
			System.out.println("Successfully connected to database [add db_name parameter]");
			modules = Utils.getTables(connection);
			Translate(modules, "Create", "zh_CN");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void Translate(ArrayList<String> modules, String en_string, String lang) throws SQLException {
		PreparedStatement statement = null; 
		ResultSet rs = null;

		for (String module : modules) {
			System.out.println(module);
			statement = connection.prepareStatement("SELECT * FROM " + module + " WHERE en_us='" + en_string + "'");
			rs = statement.executeQuery();
			while (rs.next()) {
				String translated = rs.getString(lang);
				System.out.println("module: " + module + ", en_us: " + en_string + ", " + lang + ": " + translated);
			}
		}
	}
}
