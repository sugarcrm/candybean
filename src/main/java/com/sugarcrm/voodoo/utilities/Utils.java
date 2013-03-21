package com.sugarcrm.voodoo.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Utils is simply a container for automation/Java-related helper functions that
 * are relatively non-specific to any particular library/framework.
 * 
 * @author cwarmbold, ylin
 *
 */
public class Utils {
	
	/**
	 * Executes a forked process that runs some given command string.  Prints the output of the command execution to console.
	 * 
	 * @param cmd 
	 * @throws Exception
	 */
	public static void run(String cmd) throws Exception {
		Process process = new ProcessBuilder(cmd).start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = reader.readLine();
		System.out.print("Run command: " + cmd);
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
		}
    }
	
	/**
	 * Given a string, this function returns the suffix of that string matching the given length.
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String pretruncate(String s, int length) {
		if (s.length() <= length)
			return s;
		return s.substring(s.length() - length);
	}

	/**
	 * adjustPath - This method adds robustness to a given path for different platforms.
	 * 
	 * @author wli
	 * 
	 * @param path
	 * @return
	 */
	public static String adjustPath(String path){
		String tempPath = path;
		// replace all single backslash (not followed by space) with forward slash
		tempPath = tempPath.replaceAll("\\\\(?! )", "/"); 
		// replace all one or more consecutive forward slashes with a File Separator
		tempPath = tempPath.replaceAll("/+", Matcher.quoteReplacement(File.separator));
		if (!tempPath.equals(path)) System.out.println("The following path: " + path + " has been adjusted to: " + tempPath);
		return tempPath;
	}
	
	/**
	 * Executes a query written as a String and return the ResultSet containing the results of the query.
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet execQuery(String query, Connection connection) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(query);
		return ps.executeQuery();
	}

	/**
	 * Retrieves all tables from the database currently connected to and store them in an ArrayList of Strings.
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<String> getTables(Connection connection) throws SQLException {
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

	/**
	 * Establishes a connection to a database. 
	 * @param dbServer - database server
	 * @param dbName - name of the database to use
	 * @param dbUser - database username
	 * @param dbPass - database password
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connectToDB(String dbServer, String dbName, String dbUser, String dbPass) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://" + dbServer + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8", dbUser, dbPass);
	}

	/**
	 * Pair is a python-2-tuple lightweight equivalent for convenience.
	 * 
	 * @author cwarmbold
	 * 
	 * @param <X>
	 * @param <Y>
	 */
	public static class Pair<X, Y> {
		public final X x;
		public final Y y;

		public Pair(X x, Y y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "x:" + x.toString() + ",y:" + y.toString();
		}
	}

	/**
	 * Triplet is a python-3-tuple lightweight equivalent for convenience.
	 * 
	 * @author cwarmbold
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param <Z>
	 */
	public static class Triplet<X, Y, Z> { 
		public final X x; 
		public final Y y; 
		public final Z z;
		
		public Triplet(X x, Y y, Z z) { 
			this.x = x; 
			this.y = y;
			this.z = z;
		} 
		
		@Override
		public String toString() {
			return "x:" + x.toString() + ",y:" + y.toString() + ",z:" + z.toString();
		}
	}
}
