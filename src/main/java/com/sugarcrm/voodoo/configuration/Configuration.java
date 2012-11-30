package com.sugarcrm.voodoo.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

public class Configuration extends Properties {

	/**
	 * NOTE: This method takes in a path of type String instead of a FileInputStream object
	 * to add path robustness by calling 'adjustPath' and then the actual load method
	 * 
	 * @author wli
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public void load(String filePath) throws Exception {
		String adjustedFilePath = adjustPath(filePath);
		FileInputStream fileIS = new FileInputStream(new File(adjustedFilePath));
		this.load(fileIS);
	}

	/**
	 * NOTE: This method takes in a path of type String instead of a FileOutputStream object
	 * to add path robustness by calling 'adjustPath' and then the actual store method
	 * 
	 * @author wli
	 * 
	 * @param filePath
	 * @param comments
	 * @throws Exception
	 */
	public void store(String filePath, String comments) throws Exception {
		String adjustedFilePath = adjustPath(filePath);
		FileOutputStream fileOS = new FileOutputStream(new File(adjustedFilePath));
		this.store(fileOS, comments);
	}

	/**
	 * This method overrides the extended getProperty(key, defaultValue) method 
	 * to support cascading value
	 * 
	 * @author wli
	 * 
	 * @param path
	 * @return a cascaded value
	 */
	public String getProperty(String key, String defaultValue) {
		return getCascadingPropertyValue(this, defaultValue, key);
	}

	/**
	 * This is a newly added method (with defaultValue) to retrieve a path
	 * from the properties file and safely return it after calling adjustPath
	 * 
	 * @author wli
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getPathProperty(String key, String defaultValue) {
		String pathValue = getCascadingPropertyValue(this, key, defaultValue);
		return adjustPath(pathValue);
	}

	/**
	 * This is a newly added method (without defaultValue) to retrieve a path 
	 * from the properties file and safely return it after calling adjustPath
	 * 
	 * @author wli
	 * 
	 * @param key
	 * @return
	 */
	public String getPathProperty(String key) {
		String pathValue = getProperty(key);
		return adjustPath(pathValue);
	}

	/**
	 * Takes the value and split them according to the given
	 * delimiter and return a String[]. 
	 * (Example, "FRUITS = apple pear banana)
	 * delimiter: " " 
	 * and returns String[]: apple pear banana
	 * 
	 * @param key
	 * @param delimiter
	 * @return
	 */
	public String[] getPropertyValues(String key, String delimiter) {
		String propValues = getProperty(key);
		return propValues.split(delimiter);
	}

	/**
	 * Consume a ArrayList of type String containing properties (ie, "USERNAME=root")
	 * and set all the properties onto a file. Key/value are separated by a equal sign '='
	 * 
	 * @param listOfProperties
	 */
	public void setProperties(ArrayList<String> listOfProperties) {
		for (String property : listOfProperties) {
			String[] keyValueHolder = property.split("=");
			String key = keyValueHolder[0].trim();
			String value = keyValueHolder[1].trim();
			setProperty(key,value);
		}
	}

	/**
	 * This method adds robustness to a given path for different platforms.
	 * 
	 * @author wli
	 * 
	 * @param path
	 * @return
	 */
	private static String adjustPath(String path){
		String tempPath = path;
		// replace all single backslash (not followed by space) with forward slash
		tempPath = tempPath.replaceAll("\\\\(?! )", "/"); 
		// replace all one or more consecutive forward slashes with a File Separator
		tempPath = tempPath.replaceAll("/+", File.separator);
		if (!tempPath.equals(path)) System.out.println("The following path: " + path + " has been adjusted to: " + tempPath);
		return tempPath;
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
	private static String getCascadingPropertyValue(Properties props,
			String defaultValue, String key) {
		String value = defaultValue;
		if (props.containsKey(key))
			value = props.getProperty(key);
		if (System.getProperties().containsKey(key))
			value = System.getProperty(key);
		return value;
	}

}