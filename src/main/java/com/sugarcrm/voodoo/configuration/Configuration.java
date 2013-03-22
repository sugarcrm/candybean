package com.sugarcrm.voodoo.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import com.sugarcrm.voodoo.utilities.OptionalLogger;
import com.sugarcrm.voodoo.utilities.Utils;

public class Configuration extends Properties {
	private static final long serialVersionUID = 1L;
	public OptionalLogger log;

	public Configuration() {
		this.log = new OptionalLogger();
	}

	public Configuration(Logger log) {
		this.log = new OptionalLogger(log);
	}

	/**
	 * NOTE: This method takes in a path of type String instead of a FileInputStream object
	 * to add path robustness by calling 'Utils.adjustPath' and then the actual load method
	 * 
	 * @author wli
	 * 
	 * @param filePath
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public void load(String filePath) {
		String adjustedPath = Utils.adjustPath(filePath);
		try {
			load(new FileInputStream(new File(adjustedPath)));
		} catch (FileNotFoundException e) {
			// get file name using substring of adjustedPath that starts after the last /
			log.severe(adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1) + " not found. ");
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("Unable to load " + adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1) + ". ");
			e.printStackTrace();
		}
	}


	public void load(File file) {
		try {
			load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			log.severe(file.getName() + " not found. ");
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("Unable to load " + file.getName() + ". ");
			e.printStackTrace();
		}
	}

	/**
	 * NOTE: This method takes in a path of type String instead of a FileOutputStream object
	 * to add path robustness by calling 'Utils.adjustPath' and then the actual store method
	 * 
	 * @author wli
	 * 
	 * @param filePath
	 * @param comments
	 * @throws IOException 
	 * @throws Exception
	 */
	public void store(String filePath, String comments) {
		String adjustedPath = Utils.adjustPath(filePath);
		try {
			store(new FileOutputStream(new File(adjustedPath)), comments);
		} catch (IOException e) {
			log.severe("Unable to store " + adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1));
			e.printStackTrace();
		}
	}

	public void store(File file, String comments) {
		try {
			store(new FileOutputStream(file), comments);
		} catch (IOException e) {
			log.severe("Unable to store "	+ file.getName());
			e.printStackTrace();
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
	 * from the properties file and safely return it after calling Utils.adjustPath
	 * 
	 * @author wli
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getPathProperty(String key, String defaultValue) {
		String pathValue = getCascadingPropertyValue(this, defaultValue, key);
		return Utils.adjustPath(pathValue);
	}

	/**
	 * This is a newly added method (without defaultValue) to retrieve a path 
	 * from the properties file and safely return it after calling Utils.adjustPath
	 * 
	 * @author wli
	 * 
	 * @param key
	 * @return
	 */
	public String getPathProperty(String key) {
		String pathValue = getProperty(key);
		return Utils.adjustPath(pathValue);
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
		String[] result = values.split(delimiter);
		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}

	public ArrayList<String> getPropertiesArrayList(String key, String delimiter) {
		String values = getProperty(key);
		String[] arrayOfValues = values.split(delimiter);
		ArrayList<String> result = new ArrayList<String>();
		for (String value : arrayOfValues) {
			result.add(value.trim());
		}
		return result;
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
			setProperty(key, value);
		}
	}

	public void setProperties(String[] listOfProperties) {
		for (String property : listOfProperties) {
			String[] keyValueHolder = property.split("=");
			String key = keyValueHolder[0].trim();
			String value = keyValueHolder[1].trim();
			setProperty(key, value);
		}
	}

	/**
	 * NOTE: If one of the properties has multiple values (ex. fruits=apple, pear, banana),
	 *       make sure the delimiter used to separate properties is not the same delimiter
	 *       used to separate values (ex. fruit1=apple; fruit2=pear; fruits=apple, pear, banana)
	 *       
	 * @author ylin
	 * 
	 * @param listOfProperties
	 * @param delimiter
	 */
	public void setProperties(String listOfProperties, String delimiter) {
		for (String property : listOfProperties.split(delimiter)) {
			String[] keyValueHolder = property.split("=");
			String key = keyValueHolder[0].trim();
			String value = keyValueHolder[1].trim();
			setProperty(key, value);
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
