package com.sugarcrm.voodoo.configuration;

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
import java.io.OutputStream;

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
     * Sets the key value pair in the properties variable.
     *
     * @param key
     * @param value
     * @return the previously stored value for the key or null if none.
     */
    public Object setProperty(String key, String value) {
        return properties.setProperty(key, value);
    }

    /**
     * Returns a copy of the properties
     */
    public Properties getProperties() {
        return new Properties(properties);
    }

    /**
     * Reads a property list (key and element pairs) from the input byte stream. Loading using an InputStream
     * is faster than with a Reader.

     * @param in
     */
    public void load(InputStream in) throws IOException {
        properties.load(in);
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
     * Clears the Properties object so that it contains no keys.
     */
    public void clear() {
        properties.clear();
    }



}


