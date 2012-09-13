package com.sugarcrm.voodoo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;


/**
 * Utils is simply a container for automation/Java-related helper functions that
 * are relatively non-specific to any particular library/framework.
 * 
 * @author cwarmbold
 *
 */
public class Utils {

	/**
	 * A property file such as voodoo.properties that contain keys such as 'AUTOMATION.BROWSER' and its value as 'chrome'
	 * Note that all the directories of the 'filepath' argument must exist
	 * 
	 * modifyPropertyKey() A helper function to modify any key from the .properties file to any given value
	 * @param filepath argument of type String that specifies the path of the properties file
	 * @param key argument of type String 
	 * @param value argument of type String
	 * @return value argument of type String
	 */
	public static String modifyPropertyKey(String filepath, String key, String value){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(filepath));
			prop.setProperty(key, value);
			prop.store(new FileOutputStream(filepath), null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return value;
	}
	
	/**
	 * getOSType() A helper function to check the OS type running this java project
	 * @return String type argument specifying the OS type 
	 */
	public static String getOSType() {
		String os;
		os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("mac") >= 0) return "mac";
		else if (os.indexOf("win") >= 0) return "win";
		else return "linux64";
	}
	
	
	/**
	 * 
	 * @param props
	 * @param defaultValue
	 * @param key
	 * @return
	 */
	public static String getCascadingPropertyValue(ResourceBundle props,
			String defaultValue, String key) {
		String value = defaultValue;
		if (props.containsKey(key))
			value = props.getString(key);
		if (System.getProperties().containsKey(key))
			value = System.getProperty(key);
		return value;
	}

	
	/**
	 * 
	 * trimString() 
	 * @param s 
	 * @param length 
	 * @return 
	 */
	public static String trimString(String s, int length) {
		if (s.length() <= length)
			return s;
		return s.substring(s.length() - length);
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
			/**
			 *
			 * toString() 
			 * @param <X> 
			 * @param <Y> 
			 */
		@Override public String toString() {
			  return "x:" + x.toString() + ",y:" + y.toString() + ",z:" + z.toString();
		  }
	}
}
