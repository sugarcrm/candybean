package com.sugarcrm.voodoo.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class Configuration extends Properties {
	public Logger log;
	
	// Default zero argument constructor
	public Configuration() {}
	
	public Configuration(Logger log) {
		this.log = log;
	}

	/**
	 * NOTE: This method takes in a path of type String instead of a FileInputStream object
	 * to add path robustness by calling 'adjustPath' and then the actual load method
	 * 
	 * @author wli
	 * 
	 * @param filePath
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public void load(String filePath) {
		String adjustedPath = adjustPath(filePath);
		try {
			load(new FileInputStream(new File(adjustedPath)));
		} catch (IOException e) {
			log.severe("Configuration file " + adjustedPath.substring(adjustedPath.lastIndexOf('/')) + " was not properly loaded.");
		}
	}

	public void load(File file) {
		try {
			load(new FileInputStream(file));
		} catch (IOException e) {
			log.severe("Configuration file " + file.getName() + " was not properly loaded.");
		}
	}

	/**
	 * NOTE: This method takes in a path of type String instead of a FileOutputStream object
	 * to add path robustness by calling 'adjustPath' and then the actual store method
	 * 
	 * @author wli
	 * 
	 * @param filePath
	 * @param comments
	 * @throws IOException 
	 * @throws Exception
	 */
	public void store(String filePath, String comments) {
		try {
			store(new FileOutputStream(new File(adjustPath(filePath))), comments);
		} catch (IOException e) {
			log.severe("Configuration file was not properly created.");
		}
	}

	public void store(File file, String comments) {
		try {
			store(new FileOutputStream(file), comments);
		} catch (IOException e) {
			log.severe("Configuration file was not properly created.");
		}
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
	public String[] getPropertiesArray(String key, String delimiter) {
		String values = getProperty(key);
		return values.split(delimiter);
	}

	public ArrayList<String> getPropertiesArrayList(String key, String delimiter) {
		String values = getProperty(key);
		String[] arrayOfValues = values.split(delimiter);
		ArrayList<String> result = new ArrayList<String>();
		for (String value : arrayOfValues) {
			result.add(value);
		}
		return result;
	}

	/**
	 * Consume a ArrayList of type String containing properties (ie, "USERNAME=root")
	 * and set all the properties onto a file. Key/value are separated by a equal sign '='
	 * 
	 * @param listOfProperties
	 */
	public void setPropertiesArrayList(ArrayList<String> listOfProperties) {
		for (String property : listOfProperties) {
			String[] keyValueHolder = property.split("=");
			String key = keyValueHolder[0].trim();
			String value = keyValueHolder[1].trim();
			setProperty(key, value);
		}
	}

	public void setPropertiesArray(String[] listOfProperties) {
		for (String property : listOfProperties) {
			String[] keyValueHolder = property.split("=");
			String key = keyValueHolder[0].trim();
			String value = keyValueHolder[1].trim();
			setProperty(key, value);
		}
	}
	
	public void setPropertiesString(String listOfProperties, String delimiter) {
		for (String property : listOfProperties.split(delimiter)) {
			String[] keyValueHolder = property.split("=");
			String key = keyValueHolder[0].trim();
			String value = keyValueHolder[1].trim();
			setProperty(key, value);
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
	private String adjustPath(String path){
		String tempPath = path.replaceAll("(?<!^)(\\\\|/){2,}", Matcher.quoteReplacement(File.separator));  
		if (!tempPath.equals(path)) log.info("The following path: " + path + " has been adjusted to: " + tempPath);
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
