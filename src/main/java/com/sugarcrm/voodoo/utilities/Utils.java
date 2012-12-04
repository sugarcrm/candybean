package com.sugarcrm.voodoo.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * Utils is simply a container for automation/Java-related helper functions that
 * are relatively non-specific to any particular library/framework.
 * 
 * @author cwarmbold
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
	 * Given a properties file, a default key-value pair value, and a key, this
	 * function returns:\n a) the default value\n b) or, if exists, the
	 * key-value value in the properties file\n c) or, if exists, the system
	 * property key-value value. This function is used to override configuration
	 * files in cascading fashion.
	 * 
	 * @param props
	 * @param defaultValue
	 * @param key
	 * @return
	 */
	public static String getCascadingPropertyValue(Properties props,
			String defaultValue, String key) {
		String value = defaultValue;
		if (props.containsKey(key))
			value = props.getProperty(key);
		if (System.getProperties().containsKey(key))
			value = System.getProperty(key);
		return value;
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
		tempPath = tempPath.replaceAll("/+", File.separator);
		if (!tempPath.equals(path)) System.out.println("The following path: " + path + " has been adjusted to: " + tempPath);
		return tempPath;
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
