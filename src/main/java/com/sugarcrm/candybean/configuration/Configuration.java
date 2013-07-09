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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.Assert;

import com.sugarcrm.candybean.utilities.OptionalLogger;
import com.sugarcrm.candybean.utilities.Utils;

public class Configuration extends Properties {
	private static final long serialVersionUID = 1L;
	private static int defaultName = 0;
	private OptionalLogger log;
	private String configPath;
	private Boolean createdFile = false;
	// static type to track directory and files created by multiple 
	// instances of Configuration
	static private Boolean createdDir = false;
	static private ArrayList<File> filesCreated = new ArrayList<File>(); 

	public OptionalLogger getLogger() {
		return log;
	}

	public void setLogger(OptionalLogger log) {
		this.log = log;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public Configuration() {
		this(null);
	}

	public Configuration(Logger log) {
		this.setLogger(new OptionalLogger(log));
	}

	/**
	 * @author Jason Lin (ylin)
	 */
	public void createFile() {
		createFile(Integer.toString(defaultName) + ".properties", true);
		defaultName++;
	}

	/**
	 * @author Jason Lin (ylin)
	 * 
	 * @param absolutePath
	 */
	public void createFile(String absolutePath) {
		createFile(absolutePath, false);
	}

	/**
	 * 
	 * 
	 * @author Jason Lin (ylin)
	 * 
	 * @param log
	 * @param fileName
	 * @param temporary
	 */
	private void createFile(String fileName, Boolean temporary) {
		if (temporary) {
			String tempPath = System.getProperty("user.home") + File.separator
					+ "TemporaryConfigurationFiles" + File.separator + fileName;
			log.info("Using temporary path " + tempPath + ".\n");
			setConfigPath(tempPath);
		} else {
			log.info("Using path " + fileName + ".\n");
			setConfigPath(fileName);
		}
		File file = new File(getConfigPath());
		File dir = new File(file.getParent());

		if (!dir.exists()) {
			log.info("Parent folder does not exist for " + file.getName()
					+ ", creating folder(s).\n");
			boolean createdDirsStatus = dir.mkdirs();
			
			// check and log directory creation
			if (createdDirsStatus) {
				createdDir = true;
				log.info("Created folder(s) " + file.getParent() + ".\n");
			} else {
				log.severe("Unable to create folder(s) " + file.getParent()
						+ ".\n");
			}
		} else {
			log.info("Parent folder " + dir.getAbsolutePath() + " exists for "
					+ file.getName() + ", did not create folder(s).\n");
		}

		Boolean fileExisted = file.exists();
		if (fileExisted)
			log.info(file.getName()
					+ " exists, proceeding to overwrite file.\n");
		// write the Configuration object to the path specified by file
		try {
			store(file, null);
			filesCreated.add(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// check and log whether created file exists
		if (!fileExisted) {
			if (file.exists())
				log.info("Created file " + file.getName() + ".\n");
			else
				log.severe("Unable to create file " + file.getName() + ".\n");

			// assert file exists
			Assert.assertTrue(file.getName() + " was not created!",
					file.exists());
		}

		// load() has logging built in
		this.load(getConfigPath());

		// set createdFile to true, allow deleteFile() to delete file
		createdFile = true;
	}
	
	/**
	 * 
	 * 
	 * @author Jason Lin (ylin)
	 */
	public void deleteFile() {
		if (!createdFile) {
			log.severe("deleteFile(): No file was created, deleteFile() aborted.\n");
		} else {
			File file = new File(getConfigPath());
			File dir = file.getParentFile();

			boolean filesDeleted = deleteAllCreatedFiles();
			if (filesDeleted) {
				configPath = null;
				createdFile = false;
			}

			// Only delete the directory if it's created in this test session
			if (createdDir) {
				// delete all empty parent folders
				recursiveFolderDelete(file);
				createdDir = false;
				Assert.assertTrue(dir.getAbsolutePath() + " was not deleted!",
						!dir.exists());
			} else {
				log.info("deletedFile(): Folder "
						+ dir.getAbsolutePath()
						+ " was not deleted, as it was not created in this test session.\n");
			}
		}
	}

	/**
	 * Delete all the property files that're created/overwritten in the 
	 * test session. These files are kept in a static-typed arrayList.
	 * 
	 * @author Soon Han 
	 * 
	 * @param None 
	 * @return true if all the files in the arrayList are successfully deleted.   
	 *         Otherwise returns false
	 */
	private boolean deleteAllCreatedFiles() {
		boolean deleteStatus = true;
		for (File f : Configuration.filesCreated) {
			// file could have been previously deleted by an instance of
			// Configuration
			if (!f.exists()) {
				continue;
			}

			boolean ok = f.delete();
			if (ok) {
				log.info("deleteAllCreatedFiles(): Deleted file "
						+ f.getAbsolutePath() + ".\n");
			} else {
				deleteStatus = false;
				log.info("deleteAllCreatedFiles(): Unable to delete file "
						+ f.getAbsolutePath() + ".\n");
			}
			Assert.assertTrue(f.getName() + " was not deleted!", !f.exists());
		}

		return deleteStatus;
	}

	/**
	 * Delete the entire config path that was created in the test session. 
	 * The delete works from the the leaf directory up, and stops at the 
	 * 1st non-empty directory. 
	 * Any pre-existing empty directories along the config path are also 
	 * deleted.
	 * 
	 * @param file is the config path to be deleted 
	 * @return None 
	 * 
	 */
	private void recursiveFolderDelete(File file) {
		File dir = file.getParentFile();
		
		if (dir.list().length == 0) {
			boolean dirDeleted = dir.delete();
			if (dirDeleted && !file.exists())
				log.info("recursiveFolderDelete(): Deleted folder "
						+ dir.getAbsolutePath() + ".\n");
			else
				log.info("recursiveFolderDelete(): Unable to delete folder "
						+ dir.getAbsolutePath() + ".\n");
			recursiveFolderDelete(file.getParentFile());
		}
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
			getLogger().severe(adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1) + " not found.\n");
			e.printStackTrace();
		} catch (IOException e) {
			getLogger().severe("Unable to load " + adjustedPath.substring(adjustedPath.lastIndexOf('/') + 1) + ".\n");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @author Jason Lin (ylin)
	 * 
	 * @param file
	 */
	public void load(File file) {
		try {
			load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			getLogger().severe(file.getName() + " not found.\n");
			e.printStackTrace();
		} catch (IOException e) {
			getLogger().severe("Unable to load " + file.getName() + ".\n");
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
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public void store(String filePath, String comments) throws FileNotFoundException, IOException {
		String adjustedPath = Utils.adjustPath(filePath);
		store(new FileOutputStream(new File(adjustedPath)), comments);
	}

	/**
	 * 
	 * 
	 * @author Jason Lin (ylin)
	 * 
	 * @param file
	 * @param comments
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void store(File file, String comments) throws FileNotFoundException, IOException {
		store(new FileOutputStream(file), comments);
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
	 * @author wli
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
	 * @author wli
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
	 * @author Jason Lin (ylin)
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
