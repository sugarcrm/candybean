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

	/**
	 * Find translations for an English string in a given language quickly by
	 * changing the parameters for Translate() and running in Eclipse
	 * 
	 * @author Jason Lin (ylin)
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			DB_SERVER = "10.8.31.10";
			DB_NAME = "Translations_6_7_latest";
			DB_USER = "translator";
			DB_PASS = "Sugar123!";

			CONNECTION = Utils.getDBConnection(DB_SERVER, DB_NAME, DB_USER,
					DB_PASS);
			MODULES = Utils.getTables(CONNECTION);

			Translate(MODULES, "Accounts", "zh_CN");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void Translate(ArrayList<String> modules,
			String inputString, String lang) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;

		System.out.println("Matching '" + inputString
				+ "' with English strings");
		for (String module : modules) {
			ps = CONNECTION.prepareStatement("SELECT Label, " + lang + " FROM "
					+ module + " WHERE en_us='" + inputString + "'");
			rs = ps.executeQuery();
			while (rs.next()) {
				String label = rs.getString("Label");
				String translated = rs.getString(lang);
				System.out.println(module + ": " + translated + "(" + label
						+ ")");
			}
		}

		System.out.println("\nMatching '" + inputString + "' with labels");
		for (String module : modules) {
			ps = CONNECTION.prepareStatement("SELECT Label, " + lang + " FROM "
					+ module + " WHERE Label='" + inputString + "'");
			rs = ps.executeQuery();
			while (rs.next()) {
				String translated = rs.getString(lang);
				System.out.println(module + ": " + translated);
			}
		}
		ps.close();
	}
}
