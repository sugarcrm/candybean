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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationUnitTest {

    Configuration config;

    @Before
    public void setUp() {
        config = new Configuration();
    }

	@Test(expected = CandybeanException.class)
	public void testMissingConfig() throws CandybeanException, IOException {
		new Configuration(new File(Candybean.ROOT_DIR + File.separator + "test/resources/nonexistent.config"));
	}

    @Test
    public void testSystem() {
        //ensure that the system variable value is returned if it exists
        System.setProperty("key", "systemValue");
        config.setValue("key", "propertiesValue");
        assertEquals("systemValue", config.getValue("key"));
        assertEquals("systemValue", config.getValue("key", "defaultValue"));
        System.clearProperty("key");
    }

    @Test
    public void testProperties() {
        config.setValue("key", "propertiesValue");
        assertTrue(config.getValue("key", "defaultValue").equals("propertiesValue"));
    }

    @Test
    public void testDefault() {
        config.setValue("key", "propertiesValue");
        assertEquals("defaultValue", config.getValue("defaultKey", "defaultValue"));
    }

    @Test
    public void testSetProperty() {
        assertNull("value for \"key\" should be null", config.getValue("key"));
        config.setValue("key", "value");
        assertEquals("value", config.getValue("key"));
    }

//    @Ignore
    @Test
    public void testLoadAndStore() throws Exception {
    	try {
            config.setValue("key1", "value1");
	        config.setValue("key2", "value2");
	        File configFile = new File(System.getProperty("user.dir") + File.separator + "config.properties");
            config.store(new FileOutputStream(configFile));
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line = reader.readLine();
            String content = "";
            while (line != null) {
            	content += line;
            	line = reader.readLine();
            }
        	assertTrue(content.contains("value1"));
            reader.close();
	        Configuration loadConfig = new Configuration();
	        assertEquals(0, loadConfig.getPropertiesCopy().keySet().size());
            loadConfig.load(configFile);
	        assertEquals("value1", loadConfig.getValue("key1"));
	        configFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//	String filePath1 = System.getProperty("user.dir") + File.separator + "config1.properties";
//	String filePath2 = System.getProperty("user.dir") + File.separator + "config2.properties";
//	Configuration config1;
//	Configuration config2;
//
//	@Test
//	public void orderedSetOfTests() {
//		createAndLoadConfigurationTest();
////		getAndSetMultiplePropertiesTest();
////		deleteConfigurationTest();
////		fileCreationAndDeletionTest();
//	}
	
//	public void createAndLoadConfigurationTest() {
//		// Create Configuration objects
//		config1 = new Configuration();
//		config2 = new Configuration();
//
//		// Set properties (keys-values pairs)
//		config1.setProperty("database", "localhost");
//		config1.setProperty("dbuser", "root");
//		config1.setProperty("dbpassword", "root");
//
//		config2.setProperty("database", "10.8.31.10");
//		config2.setProperty("dbuser", "translator");
//		config2.setProperty("dbpassword", "Sugar123!");
//
//		// Store Configuration using store(String filePath, String comments)
//		try {
////			config1.store(filePath1, null);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// Store Configuration using store(File file, String comments)
//		try {
////			config2.store(new File(filePath2), null);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// Checking that the created files exist
//		File file1 = new File (filePath1);
//		File file2 = new File (filePath2);
//		assertTrue("The property files created does not exist!", file1.exists());
//		assertTrue("The property files created does not exist!", file2.exists());
//
//		// Load Configuration using load(String filePath)
////		config1.load(filePath1);
//		// Load Configuration using load(File file)
////		config2.load(file2);
//
//		// Checking if the keys/values exist in config1
//		assertEquals("Values not equal, assertion failed!", "localhost", config1.getValue("database", "wrongvalue"));
//		assertEquals("Values not equal, assertion failed!", "root", config1.getValue("dbuser", "wrongvalue"));
//		assertEquals("Values not equal, assertion failed!", "root", config1.getValue("dbpassword", "wrongvalue"));
//		// Checking if the keys/values exist in config2
//		assertEquals("Values not equal, assertion failed!", "10.8.31.10", config2.getValue("database", "wrongvalue"));
//		assertEquals("Values not equal, assertion failed!", "translator", config2.getValue("dbuser", "wrongvalue"));
//		assertEquals("Values not equal, assertion failed!", "Sugar123!", config2.getValue("dbpassword", "wrongvalue"));
//
//		// Checking the cascading functionality using default values
//		assertEquals("Values not equal, assertion failed!", "default_1", config1.getValue("non-existant_key_1", "default_1"));
//		assertEquals("Values not equal, assertion failed!", "default_2", config2.getValue("non-existant_key_2", "default_2"));
//	}

//	public void getAndSetMultiplePropertiesTest() {
//		// Create Configuration objects
//		config1 = new Configuration();
//		config2 = new Configuration();
//
//		// Create an ArrayList containing multiple properties
//		ArrayList<String> properties1 = new ArrayList<String>();
//		properties1.add("char1=A");
//		properties1.add("char2=B");
//		properties1.add("chars=A B C");
//
//		// Create an Array containing multiple properties
//		String[] properties2 = new String[3];
//		properties2[0] = "firstname=Borat";
//		properties2[1] = "lastname=Sagdiyev";
//		properties2[2] = "fullname=Borat Sagdiyev";
//
//		// Create a String containing multiple properties
//		String properties3 = "fruit1=apple; fruit2=pear; fruitlist=apple, pear, banana, orange, cherry";
//
//		// Set properties
//		config1.setProperties(properties1);
//		config1.setProperties(properties2);
//		config2.setProperties(properties3, ";");
	
//		assertEquals("Values not equal, assertion failed!", "A", config1.getProperty("char1", "wrongvalue"));
//		assertEquals("Values not equal, assertion failed!", "B", config1.getProperty("char2", "wrongvalue"));
//		String[] values1 = config1.getPropertiesArray("chars", " ");
//		assertEquals("Values not equal, assertion failed!", "C", values1[2]);
//
//		assertEquals("Values not equal, assertion failed!", "Borat", config1.getProperty("firstname", "wrongvalue"));
//		assertEquals("Values not equal, assertion failed!", "Sagdiyev", config1.getProperty("lastname", "wrongvalue"));
//		ArrayList<String> values2 = config1.getPropertiesArrayList("fullname", " ");
//		assertEquals("Values not equal, assertion failed!", "Borat", values2.get(0));
//
//		assertEquals("Values not equal, assertion failed!", "apple", config2.getProperty("fruit1", "wrongvalue"));
//		assertEquals("Values not equal, assertion failed!", "pear", config2.getProperty("fruit2", "wrongvalue"));
//		String[] values3 = config2.getPropertiesArray("fruitlist", ",");
//		assertEquals("Values not equal, assertion failed!", "cherry", values3[4]);
//	}
//
//	public void deleteConfigurationTest() {
//		// Deleting the File created
//		File file1 = new File(filePath1);
//		File file2 = new File(filePath2);
//
//		file1.delete();
//		file2.delete();
//
//		assertTrue("File did not get deleted!", !file1.exists());
//		assertTrue("File did not get deleted!", !file2.exists());
//	}
//
//	public void fileCreationAndDeletionTest() {
//		config1 = new Configuration();
//		config1.createFile();
//		config1.deleteFile();
//
//		config2 = new Configuration();
//		config2.createFile(System.getProperty("user.dir") + "/1/2/3/4/" + "somefile.properties");
//		config2.deleteFile();
//	}
}
