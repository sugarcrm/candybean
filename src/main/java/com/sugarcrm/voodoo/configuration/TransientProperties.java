package com.sugarcrm.voodoo.configuration;

import java.io.File;
import java.util.Properties;

import junit.framework.Assert;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.configuration.Configuration;

public class TransientProperties {
	String utils = null;
	String propFileDirPath;
	String propFilePath;
	File propFileDir;
	Configuration config = new Configuration();
	Voodoo voodoo = null;
	
	public TransientProperties(String testName) {
	    //propFilePath = System.getProperty("user.dir") + File.separator
		//	+ "transient." + testName + ".properties";
	    propFileDirPath = System.getProperty("user.dir") + "/transientProperties";
	    propFilePath = propFileDirPath + File.separator + "transient." + testName + ".properties";
	    
		try {
			// Access logger via a voodoo instance.
			// Calling Voodoo.getInstance(null), implicitly assuming 
			// voodoo already exists. Not good. 
			// Need an easy way to access the logger anywhere in the code.
			voodoo = Voodoo.getInstance(null); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TransientProperties(String testName, String property, String value) {
	    this(testName);
		config.setProperty(property, value);
	}
	
	public void setProperties(String property, String value) {
		config.setProperty(property, value);
	}
	
	public Properties getProperties() {
		return createPropFile();
	}
	
	public Properties createPropFile() {
		
		propFileDir = new File(propFileDirPath);
		boolean success = propFileDir.mkdir();
		if (success) {
			voodoo.log.info("Success creating " + propFileDir);
		} else {
			voodoo.log.severe("Failed creating " + propFileDir);
		}
		
		try {
			config.store(propFilePath, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Checking that the created file exists
		File propFile = new File(propFilePath);
		// TODO. Should use voodoo assert
		Assert.assertTrue("Error: " + propFilePath + " does not exist. Its creation failed",
				propFile.exists());

		try {
			config.load(propFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return config;
	}

	public void cleanup() { 
		File propFile = new File(propFilePath);
		//System.out.println("deletePropFile(): deleting " + propFile.getName());
		voodoo.log.info("deletePropFile(): deleting " + propFile.getName() + " ...");
		boolean Ok = propFile.delete();
		Assert.assertTrue("File did not get deleted!", !propFile.exists());
		
		if (Ok && !propFile.exists()) {
			//System.out.println("deletePropFile(): OK deleted " + propFile.getName());
			voodoo.log.info("OK deleted " + propFile.getName());
		} else {
			voodoo.log.severe("Failed to delete " + propFile.getName());
		}
		
        //try {
	//		FileUtils.deleteDirectory(propFileDir);
	//		voodoo.log.info("OK deleted directory " + propFileDir.getName());
	//	} catch (IOException e) {
	//		voodoo.log.severe("Failed to delete directory " + propFileDir.getName());
	//		e.printStackTrace();
	//	}
	}
}
