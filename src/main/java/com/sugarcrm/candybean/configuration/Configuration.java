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
package com.sugarcrm.candybean.configuration;

import java.io.*;
import java.util.Enumeration;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import com.sugarcrm.candybean.utilities.Utils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Configuration is an object that represents a set of key-value pairs.
 *
 * When requesting a value for a given key, the configuration object will first try to
 * return the system defined environment variable for the key. If it is not defined,
 * configuration will return the value defined in the properties file, and finally return
 * a default value if it not found using the first two methods.
 */
public class Configuration {

	/**
     * The tangible file associated with this object.
     */
    public final File configFile;
	
    /**
     * The .properties file associated with this object.
     */
    private Properties properties;

    /**
     * A private logger for this object.
     */
    private Logger logger;

    /**
     * A Configuration object with no physical file path given. The properties initialized
     * with the default properties.
     */
    public Configuration() {
    	this.configFile = null;
        properties = new Properties();
        this.logger = Logger.getLogger(Configuration.class.getSimpleName());
    }

    /**
     * @param propertiesPath
     * @throws IOException 
     */
    public Configuration(File configFile) throws IOException {
    	this.configFile = configFile;
        properties = new Properties();
        this.logger = Logger.getLogger(Configuration.class.getSimpleName());
        this.load(configFile);
    }

    /**
     * This method finds and returns the proper cascading configuration value for a given key.
     *
     * Returns the system value if defined otherwise it looks in the properties and
     * finally the defaults.
     *
     * @param key
     * @return cascading configuration value
     */
    public String getValue(String key) {
        String systemValue = System.getProperty(key);
        return systemValue != null ? systemValue : properties.getProperty(key);
    }

    /**
     * This method looks for the key-value pair in the system and properties object and returns
     * the default value if it is not found.
     *
     * @param key
     * @param defaultValue
     * @return cascading value or default value
     */
    public String getValue(String key, String defaultValue) {
        return getValue(key) != null ? getValue(key) : defaultValue;
    }

    /**
     * Sets the key value pair in the properties variable.
     *
     * @param key
     * @param value
     * @return the previously stored value for the key or null if none.
     */
    public Object setValue(String key, String value) {
        logger.info("Set key value property: {" + key + ", " + value + "}");
        return properties.setProperty(key, value);
    }

    /**
     * @return copy of the properties
     */
    public Properties getPropertiesCopy() {
        return (Properties) properties.clone();
    }

    /**
     * This is a newly added method (without defaultValue) to retrieve a path
     * from the properties file and safely return it after calling Utils.adjustPath
     *
     * @param key
     * @return adjusted path or null if it does not exist.
     */
    public String getPathValue(String key) {
        String pathValue = getValue(key);
        return Utils.adjustPath(pathValue);
    }

    public void load(File file) throws IOException {
    	try {
        	if (file == null) {
        		throw new FileNotFoundException("Given file is null.");
        	} else {
        		this.load(new FileInputStream(file));
        	}
        } catch (FileNotFoundException e) {
            // get file name using substring of adjustedPath that starts after the last /
            logger.severe(e.getMessage());
        } catch (IOException e) {
            logger.warning("Unable to load " + file.getCanonicalPath() + ".\n");
            logger.severe(e.getMessage());
        } catch (NullPointerException e) {
            logger.warning("File path is null.\n");
            logger.severe(e.getMessage());
        }

    }

    private void load(InputStream in) throws IOException {
        this.properties.load(in);
        String platform = Utils.getCurrentPlatform();
        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            JSONParser parser = new JSONParser();
            String newValue;
            try {
                Object valueObject = parser.parse(value);
                //get value for current platform key
                if (valueObject instanceof Map) {
                    JSONObject valueMap = (JSONObject) valueObject;
                    newValue = (String) valueMap.get(platform);
                } else {
                    newValue = value;
                }
            } catch (ParseException pe) {
                //parsedString is not a smartValue/json object.
                newValue = value;
            }
            properties.setProperty(key, newValue);
        }
    }
    
    public static String getPlatformValue(Properties props, String key) {
	    String platform = Utils.getCurrentPlatform();
	    String valueStr = props.getProperty(key);
        JSONParser parser = new JSONParser();
        try {
            Object valueObject = parser.parse(valueStr);
            if (valueObject instanceof Map) {
                JSONObject valueMap = (JSONObject) valueObject;
                return (String) valueMap.get(platform);
            } else {
                return valueStr;
            }
        } catch (ParseException pe) {
            return valueStr;
        }
    }

    /**
     * Writes the property list from the Properties table to the file path in a format suitable for loading into
     * a properties table using the load(InputStream) or load(String) method.
     *
     * @param filePath
     * @throws IOException 
     */
    public void store(File file) throws IOException {
        try {
            store(new FileOutputStream(file));
        } catch (IOException e) {
            logger.warning("Unable to store " + file.getCanonicalPath() + ".\n");
            logger.severe(e.getMessage());
        }
    }
    
    /*
     * Stores the current properties to the provided file
     * @param properties
     * @param file
     * @throws IOException
     */
    private void store(Properties properties, File file) throws IOException {
        try {
        	properties.store(new FileOutputStream(file), "");
        } catch (IOException e) {
            logger.warning("Unable to store " + file.getCanonicalPath() + ".\n");
            logger.severe(e.getMessage());
        }
    }
    
    public boolean hasKey(String key){
    	return properties.containsKey(key);
    }

    /**
     * Writes the property list from the Properties table to the output stream in a format suitable for loading into
     * a properties table using the load(InputStream) method.
     *
     * @param out
     */
    public void store(OutputStream out) throws IOException {
        properties.store(out, null);
    }
    
    /**
     * Overwrites the config file associated with this Configuration with the new properties
     * @param properties
     * @throws IOException
     */
    public void overwrite(Properties properties) throws IOException {
        store(properties,configFile);
    }
    
    /**
     * Describes this Configuration object in string format.
     */
    @Override
    public String toString() {
        return "Configuration(" + properties.toString() + ")";
    }

    /**
     * Clears the Properties object so that it contains no keys.
     */
    public void clear() {
        logger.warning("Clearing properties...");
        properties.clear();
    }
}
