package com.sugarcrm.voodoo.translations.find_duplicates;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sugarcrm.voodoo.utilities.Utils;

public class FindAffectedSodaTests {
	private static Connection CONNECTION;
	private static ArrayList<String> MODULES;
	private static Pattern LINK_PATTERN = Pattern.compile("link text=\"(.*?)\"");
	private static Pattern ASSERT_PATTERN = Pattern.compile("assert=\"(.*?)\"");
	private static Matcher LINK_MATCHER;
	private static Matcher ASSERT_MATCHER;
	
	private static void main(String[] args) {
		try {
			CONNECTION = Utils.getConnection(args[0], args[1], args[2], args[3]);
			MODULES = Utils.getTables();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}