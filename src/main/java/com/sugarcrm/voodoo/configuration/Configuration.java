package com.sugarcrm.voodoo.configuration;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

import com.sugarcrm.voodoo.utilities.Utils;

/**
 * Configuration is an object that represents a set of key-value pairs.
 *
 * When requesting a value for a given key, the configuration object will first try to
 * return the system defined environment variable for the key. If it is not defined,
 * configuration will return the value defined in the properties file, and finally return
 * a default value if it not found using the first two methods.
 */
public class Configuration {

    //================================================================================
    // Properties
    //================================================================================

    /* The default Properties to load from if a key is not found by other methods. */
    private static final Properties defaults;

    /* Load defaults */
    static {
        defaults = new Properties();
//        InputStream defaultsInputStream = Configuration.class.getResourceAsStream("defaults.properties");
//        try {
//            defaults.load(defaultsInputStream);
//            defaultsInputStream.close();
//        } catch (IOException e) {
//            System.err.print("Error loading defaults: " + e.getMessage());
//        }
    }

    /* The .properties file associated with this configuration object. */
    private Properties properties;

    private Logger logger;

    //================================================================================
    // Constructors
    //================================================================================

    /* A Configuration object with no properties file path given. The properties initialized
     * with the default properties. */
    public Configuration() {
        this.properties = new Properties(defaults);
        this.logger = Logger.getLogger(Configuration.class.getName());

    }

    /* Normal constructor. */
    public Configuration(String propertiesPath) {

        this.properties = new Properties(defaults);
        this.logger = Logger.getLogger(Configuration.class.getName());

        load(propertiesPath);
    }

    //================================================================================
    // Accessors
    //================================================================================

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
    public Object setProperty(String key, String value) {
        logger.info("Set key value property: {" + key + ", " + value + "}");
        return properties.setProperty(key, value);
    }

    /**
     * Returns a copy of the properties
     */
    public Properties getPropertiesCopy() {
        return new Properties(properties);
    }

    /**
     * This is a newly added method (without defaultValue) to retrieve a path
     * from the properties file and safely return it after calling Utils.adjustPath
     *
     * @author wli
     *
     * @param key
     * @return adjusted path or null if it does not exist.
     */
    public String getPathValue(String key) {
        String pathValue = getValue(key);
        return Utils.adjustPath(pathValue);
    }

    /**
     * Reads a property list (key and element pairs) from the input byte stream. Loading using an InputStream
     * is faster than with a Reader.

     * @param in
     */
    private void load(InputStream in) throws IOException {
        properties.load(in);
    }

    /**
     * NOTE: This method takes in a path of type String instead of a FileInputStream object
     * to add path robustness by calling 'Utils.adjustPath' and then the actual load method
     *
     * @author wli
     *
     * @param filePath
     */
    private void load(String filePath) {
        String adjustedPath = Utils.adjustPath(filePath);
        try {
            load(new FileInputStream(new File(adjustedPath)));
        } catch (FileNotFoundException e) {
            // get file name using substring of adjustedPath that starts after the last /
            logger.warning(adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1) + " not found.\n");
            e.printStackTrace();
        } catch (IOException e) {
            logger.warning("Unable to load " + adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1) + ".\n");
            e.printStackTrace();
        }
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
     * Writes the property list from the Properties table to the file path in a format suitable for loading into
     * a properties table using the load(InputStream) or load(String) method.
     *
     * @param filePath
     */
    public void store(String filePath) {
        String adjustedPath = Utils.adjustPath(filePath);
        File configFile = new File(adjustedPath);
        try {
            store(new FileOutputStream(configFile));
        } catch (IOException e) {
            logger.warning("Unable to store " + adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1) + ".\n");
            e.printStackTrace();
        }

    }

    /**
     * Clears the Properties object so that it contains no keys.
     */
    public void clear() {
        properties.clear();
        logger.warning("Clearing properties...");
    }

}


