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
package com.sugarcrm.candybean.automation;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.utilities.CandybeanLogger;
import com.sugarcrm.candybean.utilities.Utils;

/**
 * Candybean is the primary object for tests to use.  It provides
 * logging facilities and enables the loading of the appropriate
 * automation interface for use by the test's runtime.
 *
 * @author Conrad Warmbold
 */
public final class Candybean {
	
	/**
	 * The default name of the system property that may contain the candybean configuration file path.
	 */
	public static final String CONFIG_KEY = "cbconfig";
	
	/**
	 * The root directory of candybean
	 */
	public static final File ROOT_DIR = new File(System.getProperty("user.dir") + File.separator);

	/**
	 * The default configuration file name to instantiate candybean.
	 */
	public static final List<String> DEFAULT_CONFIG_FILES = Arrays.asList(
			ROOT_DIR + File.separator + "candybean.config",
			ROOT_DIR + File.separator + "src" + File.separator + "test" +
					File.separator + "resources" + File.separator + "candybean.config",
			// Using the "Voodoo" name is deprecated and Candybean should be used instead
			// https://sugarcrm.atlassian.net/browse/CB-270
			ROOT_DIR + File.separator + "src" + File.separator + "test" +
					File.separator + "resources" + File.separator + "voodoo.properties"
			);

	/**
	 * {@link Configuration} object created by loading the candybean
	 * configuration file.
	 */
	public final Configuration config;

	/**
	 * The singleton Candybean instance.  Created when a Candybean instance is
	 * first called and persistent throughout the life of the tests.
	 */
	private static Candybean instance = null;

	/**
	 * {@link Logger} object for use by Candybean.
	 */
	public final Logger log;

	/**
	 * Instantiates a Candybean object.
	 * 
	 * @throws Exception if instantiating the logger fails
	 */
	private Candybean(Configuration config) throws CandybeanException {
		try {
			this.config = config;
			log = this.createLogger();
			LogManager.getLogManager().addLogger(log);
			log.config("Instantiating Candybean with config: " + config.toString());
		} catch (Exception e) {
			throw new CandybeanException(e);
		}
	}
	
	/**
	 * Returns an InterfaceBuilder responsible for creating an interface for the test class.
	 * @param cls The test class that required the interface builder.
	 * @return An interface builder specific to the test class.
	 */
	public AutomationInterfaceBuilder getAutomationInterfaceBuilder(Class<?> cls) {
		return new AutomationInterfaceBuilder(cls);
	}

	
	/**
	 * Returns an InterfaceBuilder responsible for creating an interface for the test class.
	 * If the user does not specify a test class, candybean will attempt to find the calling class using
	 * a slightly hacky way (by reading the stack trace)
	 * @return An interface builder specific to the test class.
	 */
	public AutomationInterfaceBuilder getAIB(Class<?> cls) {
		return new AutomationInterfaceBuilder(cls);
	}
	
	/**
	 * Returns an InterfaceBuilder responsible for creating an interface for the test class, without a passed
	 * Reference to the calling class.
	 * If the user does not specify a test class, candybean will attempt to find the calling class using
	 * an undesired way (by reading the stack trace). The use of this method is <b>not</b> recommended,
	 * as it is an expensive operation.
	 * @return An interface builder specific to the test class.
	 * @throws CandybeanException 
	 */
	
	public AutomationInterfaceBuilder getAutomationInterfaceBuilder() throws CandybeanException {
		return getAutomationInterfaceBuilder(getCallingClass(2));
	}
	
	/**
	 * Returns an InterfaceBuilder responsible for creating an interface for the test class, without a passed
	 * Reference to the calling class.
	 * If the user does not specify a test class, candybean will attempt to find the calling class using
	 * an undesired way (by reading the stack trace). The use of this method is <b>not</b> recommended,
	 * as it is an expensive operation.
	 * @return An interface builder specific to the test class.
	 * @throws CandybeanException 
	 */
	public AutomationInterfaceBuilder getAIB() throws CandybeanException {
		return getAIB(getCallingClass(2));
	}
	
	/*
	 * Gets the calling class from a stack trace. Used strictly to get the calling class
	 * when creating a builder object.
	 */
	private Class<?> getCallingClass(int stackTracePosition) throws CandybeanException {
		String className = new Throwable().getStackTrace()[stackTracePosition].getClassName();
		Class<?> cls;
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new CandybeanException(e);
		}
		return cls;
	}

	/**
	 * Gets the global Candybean instance.
	 *
	 * @param config  {@link Configuration} object created from candybean.config
	 * @return singleton candybean instance
	 * @throws CandybeanException if instantiating the logger fails
	 */
	public static synchronized Candybean getInstance(Configuration config) throws CandybeanException {
		if (Candybean.instance == null) {
			Candybean.instance = new Candybean(config); 
		}
		return Candybean.instance;
	}
	
	/**
	 * Get the global candybean instance
	 * 
	 * @return global candybean instance based on a default configuration
	 * @throws CandybeanException
	 */
	public static synchronized Candybean getInstance() throws CandybeanException {
		if (Candybean.instance == null) {
			Configuration config = Candybean.getDefaultConfiguration();
			Candybean.instance = new Candybean(config); 
		}
		return Candybean.instance;
	}
	
	/**
	 * Instantiates a generalized automation interface given the type
	 * 
	 * @return AutomationInterface
	 * @throws Exception
	 */
	@Deprecated
	public AutomationInterface getInterface() throws Exception {
		throw new Exception("There are no non-webdriver automation interfaces currently defined.");
	}

	public static String getDefaultConfigFile() throws CandybeanException {
		final String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY);
		if (candybeanConfigStr != null) {
			return candybeanConfigStr;
		}
		for (String f: Candybean.DEFAULT_CONFIG_FILES) {
			if (new File(f).exists()) {
				return f;
			}
		}
		throw new CandybeanException("Could not find appropriate config file");
	}
	private static Configuration getDefaultConfiguration() throws CandybeanException {
		try {
			return new Configuration(new File(Utils.adjustPath(getDefaultConfigFile())));
		} catch (IOException ioe) {
			throw new CandybeanException(ioe);
		}
	}

	private CandybeanLogger createLogger() throws Exception {
		// Add a system property so that LogManager loads the specified logging configuration file before getting logger.
		System.setProperty("java.util.logging.config.file", this.config.configFile.getCanonicalPath());
		// Gets the logger based the configuration file specified at 'java.util.logging.config.file'
		LogManager.getLogManager().reset();
		LogManager.getLogManager().readConfiguration();
		return new CandybeanLogger(this.getClass().getSimpleName());
	}

	/**
	 * @return Candybean logger
	 */
	public Logger getLogger() {
		return log;
	}

}
