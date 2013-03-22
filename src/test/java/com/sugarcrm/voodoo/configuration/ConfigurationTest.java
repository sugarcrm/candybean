package com.sugarcrm.voodoo.configuration;

import java.io.File;
import java.util.ArrayList;

import com.sugarcrm.voodoo.configuration.Configuration;
import junit.framework.Assert;
import org.junit.Test;

public class ConfigurationTest {
	String filePath1 = System.getProperty("user.dir") + File.separator + "config1.properties";
	String filePath2 = System.getProperty("user.dir") + File.separator + "config2.properties";
	Configuration config1;
	Configuration config2;

	@Test
	public void CreateAndLoadConfigurationTest() {
		// Create Configuration objects
		config1 = new Configuration();
		config2 = new Configuration();

		// Set properties (keys-values pairs)
		config1.setProperty("database", "localhost");
		config1.setProperty("dbuser", "username");
		config1.setProperty("dbpassword", "pass123!");
		
		config2.setProperty("database", "localhost");
		config2.setProperty("dbuser", "username");
		config2.setProperty("dbpassword", "pass123!");

		// Store Configuration using store(String filePath, String comments)
		config1.store(filePath1, null);
		// Store Configuration using store(File file, String comments)
		config2.store(new File(filePath2), null);

		// Checking that the created files exist
		File file1 = new File (filePath1);
		File file2 = new File (filePath2);
		Assert.assertTrue("The property files created does not exist!", file1.exists());
		Assert.assertTrue("The property files created does not exist!", file2.exists());
		
		// Load Configuration using load(String filePath)
		config1.load(filePath1);
		// Load Configuration using load(File file)
		config2.load(file2);

		// Checking if the keys/values exist in config1
		Assert.assertEquals("Values not equal, assertion failed!", "localhost", config1.getProperty("database", "wrongvalue"));
		Assert.assertEquals("Values not equal, assertion failed!", "username", config1.getProperty("dbuser", "wrongvalue"));
		Assert.assertEquals("Values not equal, assertion failed!", "pass123!", config1.getProperty("dbpassword", "wrongvalue"));
		// Checking if the keys/values exist in config2
		Assert.assertEquals("Values not equal, assertion failed!", "localhost", config2.getProperty("database", "wrongvalue"));
		Assert.assertEquals("Values not equal, assertion failed!", "username", config2.getProperty("dbuser", "wrongvalue"));
		Assert.assertEquals("Values not equal, assertion failed!", "pass123!", config2.getProperty("dbpassword", "wrongvalue"));
		
		// Checking the cascading functionality using default values
		Assert.assertEquals("Values not equal, assertion failed!", "default_1", config1.getProperty("non-existant_key_1", "default_1"));
		Assert.assertEquals("Values not equal, assertion failed!", "default_2", config2.getProperty("non-existant_key_2", "default_2"));
	}
	
	@Test
	public void GetAndSetMultiplePropertiesTest() {
		// Create Configuration objects
		config1 = new Configuration();
		config2 = new Configuration();
		
		// Create an ArrayList containing multiple properties
		ArrayList<String> properties1 = new ArrayList<String>();
		properties1.add("char1=A");
		properties1.add("char2=B");
		properties1.add("chars=A B C");
		
		// Create an Array containing multiple properties
		String[] properties2 = new String[3];
		properties2[0] = "firstname=Borat";
		properties2[1] = "lastname=Sagdiyev";
		properties2[2] = "fullname=Borat Sagdiyev";
		
		// Create a String containing multiple properties
		String properties3 = "fruit1=apple; fruit2=pear; fruitlist=apple, pear, banana, orange, cherry";
		
		// Set properties
		config1.setPropertiesArrayList(properties1);
		config1.setPropertiesArray(properties2);
		config2.setPropertiesString(properties3, ";");
		
		Assert.assertEquals("Values not equal, assertion failed!", "A", config1.getProperty("char1", "wrongvalue"));
		Assert.assertEquals("Values not equal, assertion failed!", "B", config1.getProperty("char2", "wrongvalue"));
		String[] values1 = config1.getPropertiesArray("chars", " ");
		Assert.assertEquals("Values not equal, assertion failed!", "C", values1[2]);
		
		Assert.assertEquals("Values not equal, assertion failed!", "Borat", config1.getProperty("firstname", "wrongvalue"));
		Assert.assertEquals("Values not equal, assertion failed!", "Sagdiyev", config1.getProperty("lastname", "wrongvalue"));
		ArrayList<String> values2 = config1.getPropertiesArrayList("fullname", " ");
		Assert.assertEquals("Values not equal, assertion failed!", "Borat", values2.get(0));
		
		Assert.assertEquals("Values not equal, assertion failed!", "apple", config2.getProperty("fruit1", "wrongvalue"));
		Assert.assertEquals("Values not equal, assertion failed!", "pear", config2.getProperty("fruit2", "wrongvalue"));
		String[] values3 = config2.getPropertiesArray("fruitlist", ",");
		Assert.assertEquals("Values not equal, assertion failed!", "cherry", values3[4]);
	}

	@Test
	public void DeleteConfigFile() {
		// Deleting the File created
		File file1 = new File (filePath1);
		File file2 = new File (filePath2);
		
		file1.delete();
		file2.delete();
		
		Assert.assertTrue("File did not get deleted!", !file1.exists());
		Assert.assertTrue("File did not get deleted!", !file2.exists());
	}

}
