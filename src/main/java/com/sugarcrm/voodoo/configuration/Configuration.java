package com.sugarcrm.voodoo.configuration;

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

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
        InputStream defaultsInputStream = Configuration.class.getResourceAsStream("defaults.properties");
        try {
            defaults.load(defaultsInputStream);
            defaultsInputStream.close();
        } catch (IOException e) {
            System.err.print("Error loading defaults: " + e.getMessage());
        }
    }

    /* The .properties file associated with this configuration object. */
    private Properties properties;

    //================================================================================
    // Constructors
    //================================================================================

    /* A Configuration object with no properties file path given. The properties initialized
     * with the default properties. */
    public Configuration() {
        properties = new Properties(defaults);
    }

    /* Normal constructor. */
    public Configuration(String propertiesPath) {
        properties = new Properties(defaults);
        InputStream propertiesInputStream = Configuration.class.getResourceAsStream(propertiesPath);
        try {
            properties.load(propertiesInputStream);
            propertiesInputStream.close();
        } catch (IOException e) {
            System.err.print("Error loading properties: " + e.getMessage());
        }
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
     * Getter for Properties instance variable.
     * @return properties object
     */
    public Properties getProperties() {
        return properties;
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
        String pathValue = getValue(key, defaultValue);
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
        String pathValue = getValue(key);
        return Utils.adjustPath(pathValue);
    }

    /**
     * Sets the key value pair in the properties variable.
     *
     * @param key
     * @param value
     * @return the previously stored value for the key or null if none.
     */
    public Object setProperty(String key, String value) {
        return properties.setProperty(key, value);
    }



}


