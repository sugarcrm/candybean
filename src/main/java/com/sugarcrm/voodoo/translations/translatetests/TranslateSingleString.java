package com.sugarcrm.voodoo.translations.translatetests;

import java.sql.*;
import java.util.ArrayList;

import com.sugarcrm.voodoo.utilities.Utils;

public class TranslateSingleString {
	private static Connection CONNECTION;
	private static ArrayList<String> MODULES;
	
	private static String DB_SERVER;
	private static String DB_NAME;
	private static String DB_USER;
	private static String DB_PASS;

	public static void main(String args[]) {
		try {
			DB_SERVER = args[0];
			DB_NAME = args[1];
			DB_USER = args[2];
			DB_PASS = args[3];
			
			CONNECTION = Utils.getDBConnection(DB_SERVER, DB_NAME, DB_USER, DB_PASS);
			MODULES = Utils.getTables(CONNECTION);
			
			Translate(MODULES, args[4], args[5]);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void Translate(ArrayList<String> modules, String en_string, String lang) throws SQLException {
		ResultSet rs = null;
		ResultSet rs2 = null;

		for (String module : modules) {
			rs = Utils.execQuery("SELECT * FROM " + module + " WHERE en_us='" + en_string + "'", CONNECTION);
			rs2 = Utils.execQuery("SHOW COLUMNS FROM " + module + " WHERE FIELD='" + lang + "'", CONNECTION);
			if (rs2.next()) {
				while (rs.next()) {
					String label = rs.getString("Label");
					String translated = rs.getString(lang);
					System.out.println("Module: " + module + ", Label: " + label + "\n\ten_us: " + en_string + "\n\t" + lang + ": " + translated);
				}
			}
		}
	}
}
