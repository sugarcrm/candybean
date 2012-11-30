package com.sugarcrm.voodoo.configuration;

import java.io.File;

import com.sugarcrm.voodoo.configuration.Configuration;
import junit.framework.Assert;
import org.junit.Test;

public class ConfigurationTest {
	String configFilePath = System.getProperty("user.dir") + File.separator + "myPropFile.properties";
	Configuration prop = null;

	@Test
	public void CreateConfigFileTest() throws Exception {
		// Creating Configuration Object
		prop = new Configuration();

		// Defining configuration properties keys/values
		prop.setProperty("database", "localhost");
		prop.setProperty("dbuser", "username");
		prop.setProperty("dbpassword", "pass123!");

		// Store Configuration
		prop.store(configFilePath, null);

		// Checking that the created file exists
		File propFile = new File (configFilePath);
		Assert.assertTrue("The property files created does not exist!", propFile.exists());
		
		prop.load(configFilePath);

		// Checking if the keys/values exist within the file
		Assert.assertEquals("Wrong Value", "localhost", prop.getProperty("database", "WrongValue"));
		Assert.assertEquals("Wrong Value", "username", prop.getProperty("dbuser", "WrongValue"));
		Assert.assertEquals("Wrong Value", "pass123!", prop.getProperty("dbpassword", "WrongValue"));

		// Checking the cascading functionality using default values
		Assert.assertEquals("Wrong Value", "localhost", prop.getProperty("WrongKey", "localhost"));
		Assert.assertEquals("Wrong Value", "username", prop.getProperty("WrongKey", "username"));
	}

	@Test
	public void DeleteConfigFile() throws Exception {
		// Deleting the File created
		File propFile = new File (configFilePath);
		propFile.delete();
		Assert.assertTrue("File did not get deleted!", !propFile.exists());
	}

}
