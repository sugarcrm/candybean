/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.utilities;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * Utils is simply a container for automation/Java-related helper functions that
 * are relatively non-specific to any particular library/framework.
 * 
 * @author cwarmbold, wli, Jason Lin (ylin)
 *
 */
public class Utils {
	
	private static Logger log = Logger.getLogger(Utils.class.getSimpleName());

	/**
	 * Executes a forked process that runs some given command string.  Prints the output of the command execution to console.
	 * 
	 * @param cmd 
	 * @throws IOException 
	 */
	public static void run(String cmd) throws IOException {
		Process process = new ProcessBuilder(cmd).start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = reader.readLine();
		log.info("Run command: " + cmd);
		while (line != null) {
			log.info(line);
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
		if (s.length() <= length) {
			return s;
		}
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
		String comparePath = tempPath;
		tempPath = tempPath.replaceAll("\\\\(?! )", "/");
		while (!comparePath.equals(tempPath)) {
			comparePath = tempPath;
			tempPath = tempPath.replaceAll("\\\\(?! )", "/");
		}
		// replace all one or more consecutive forward slashes with a File Separator
		tempPath = tempPath.replaceAll("/+", Matcher.quoteReplacement(File.separator));
		if (!tempPath.equals(path)) {
			log.info("The following path: " + path + " has been adjusted to: " + tempPath);
		}
		return tempPath;
	}

	/**
	 * Can be used to close any closeable stream. For example, Scanners and Writers.
	 * 
	 * @author Jason Lin (ylin)
	 * 
	 * @param s
	 */
	public static void closeStream(Closeable s) {
		try {
			if (s != null) {
				s.close();
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
	}

	/**
	 * Retrieves all tables from the database currently connected to and store them in an ArrayList of Strings.
	 * 
	 * @author Jason Lin (ylin)
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getTables(Connection connection) throws SQLException {
		DatabaseMetaData dbmd = connection.getMetaData();
		List<String> tables = new ArrayList<String>();
		String[] types = { "TABLE" };
		ResultSet resultSet = dbmd.getTables(null, null, "%", types);
		while (resultSet.next()) {
			String tableName = resultSet.getString("TABLE_NAME");
			tables.add(tableName);
		}
		resultSet.close();
		return tables;
	}

	/**
	 * Establishes a connection to a database. 
	 * 
	 * @author Jason Lin (ylin)
	 * 
	 * @param dbServer - database server
	 * @param dbName - name of the database to use
	 * @param dbUser - database username
	 * @param dbPass - database password
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getDBConnection(String dbServer, String dbName, String dbUser, String dbPass) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://" + dbServer + "/" + dbName + "?useUnicode=true&characterEncoding=utf-8", dbUser, dbPass);
	}
	
	/**
	 * Returns a string representation of elapsed time from seconds to days:hr:min:s
	 * @param seconds The number of seconds
	 * @return
	 */
	public static String calculateTime(float seconds) {
		double partialSeconds =  (double) (seconds - ((int)seconds));
		int milliseconds = (int) (partialSeconds * 1000);
		long secs = (long)seconds;
		int day = (int) TimeUnit.SECONDS.toDays(secs);
		long hours = TimeUnit.SECONDS.toHours(secs) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(secs)
				- (TimeUnit.SECONDS.toHours(secs) * 60);
		long second = TimeUnit.SECONDS.toSeconds(secs)
				- (TimeUnit.SECONDS.toMinutes(secs) * 60);
		return day + "days " + hours + "hr " + minute + "min " + second + "s " + milliseconds + "ms";
    }
	

    /**
     * Utility function to get the current operating system.
     *
     * @author Larry Cao
     *
     * @return string representing the current operating system.
     */
    public static String getCurrentPlatform() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac")) {
            return "mac";
        } else if (os.contains("nux")) {
            return "linux";
        } else if (os.contains("win")) {
            return "windows";
        } else {
            return "unknown";
        }
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
			return "x:" + this.x.toString() + ",y:" + this.y.toString();
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
			return "x:" + this.x.toString() + ",y:" + this.y.toString() + ",z:" + this.z.toString();
		}
	}
}
