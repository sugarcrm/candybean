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

	private static void Translate(ArrayList<String> modules, String inputString, String lang) throws SQLException {
		ResultSet rs = null;
		ResultSet rs2 = null;

		System.out.println("Matching '" + inputString + "' with English strings");
		for (String module : modules) {
			rs = Utils.execQuery("SELECT Label, " + lang + " FROM " + module + " WHERE en_us='" + inputString + "'", CONNECTION);
			while (rs.next()) {
				String label = rs.getString("Label");
				String translated = rs.getString(lang);
				System.out.println("Module: " + module + ", Label: " + label + "\n\ten_us: " + inputString + "\n\t" + lang + ": " + translated);
			}
		}

		System.out.println("\nMatching '" + inputString + "' with labels");
		for (String module : modules) {
			rs = Utils.execQuery("SELECT Label, " + lang + " FROM " + module + " WHERE Label='" + inputString + "'", CONNECTION);
			while (rs.next()) {
				String label = rs.getString("Label");
				String translated = rs.getString(lang);
				System.out.println("Module: " + module + ", Label: " + inputString + "\n\t" + lang + ": " + translated);
			}
		}
	}
}
