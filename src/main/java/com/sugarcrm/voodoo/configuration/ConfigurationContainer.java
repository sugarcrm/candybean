package com.sugarcrm.voodoo.configuration;

import java.io.File;
import java.util.logging.Logger;

import junit.framework.Assert;

import com.sugarcrm.voodoo.configuration.Configuration;

/**
 * NOTE: in create(), all directories leading to filePath will be created if necessary; 
 *       in cleanup(), only filePath will be deleted;
 */
public class ConfigurationContainer extends Configuration {
	private static final long serialVersionUID = 1L;
	Configuration config;
	String configPath;

	public ConfigurationContainer(String testName) {
		configPath = System.getProperty("user.home") + File.separator + "TemporaryConfigurationFiles" + File.separator + testName + ".properties";
		config = createConfiguration(null);
	}

	public ConfigurationContainer(String testName, Logger log) {
		configPath = System.getProperty("user.home") + File.separator + "TemporaryConfigurationFiles" + File.separator + testName + ".properties";
		config = createConfiguration(log);
	}

	private Configuration createConfiguration(Logger log) {
		config = new Configuration(log);
		File file = new File(configPath);
		File dir = new File(file.getParent());

		if (!dir.exists()) {
			config.log.info("Parent directory does not exist for " + file.getName() + ", creating folder. ");
			boolean createdDirs = dir.mkdirs();
			// check and log directory creation
			if (createdDirs) {
				config.log.info("Created folder for " + file.getName() + ". ");
			} else 
				config.log.severe("Unable to create folder for " + file.getName() + ". ");
		}

		// config.store() has logging built in
		config.store(file, null);

		// check and log whether created file exists
		if (file.exists())
			config.log.info("Created file " + file.getName());
		else
			config.log.severe("Unable to create file " + file.getName());

		// assert file exists
		Assert.assertTrue(file.getName() + " was not created!", file.exists());

		// config.load() has logging built in
		config.load(configPath);

		return config;
	}

	public void deleteConfiguration() { 
		File file = new File(configPath);
		File dir = file.getParentFile();

		String msg = "Deleting " + file.getName() + ". ";
		config.log.info(msg);

		boolean fileDeleted = file.delete();

		// check delete succeeded and file no longer exists
		if (fileDeleted && !file.exists())
			config.log.info("Deleted file " + file.getName() + ". ");
		else
			config.log.info("Unable to delete file " + file.getName() + ". ");

		// check parent directory is empty
		if (dir.list().length == 0) {
			boolean dirDeleted = dir.delete();
			if (dirDeleted && !file.exists()) 
				config.log.info("Deleted directory " + dir.getAbsolutePath() + ". ");
			else
				config.log.info("Unable to delete directory " + dir.getAbsolutePath() + ". ");
		}

		// assert file does not exist
		Assert.assertTrue(file.getName() + " was not deleted!", !file.exists());
		// assert parent directory does not exist
		Assert.assertTrue(dir.getAbsolutePath() + " was not deleted!", !dir.exists());
	}
}
