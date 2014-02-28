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
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.sugarcrm.candybean.configuration.Configuration;

/**
 * Voodoo is the primary interface for tests to use.	It provides
 * logging facilities and enables the loading of the appropriate
 * {@link IInterface} for use by the test's runtime.
 *
 * @author Conrad Warmbold
 */
public final class Candybean {
	
	/**
	 * The default name of the system property that may contain the candybean configuration file path.
	 */
	public static final String CONFIG_SYSTEM_PROPERTY = "candybean_config";
	
	/**
	 * The default name for the configuration file used to instantiate candybean.
	 */
	public static final String CONFIG_FILE_NAME = "candybean.config";

	/**
	 * {@link Logger} object for use by tests.
	 */
	public static Logger LOG;

	/**
	 * The root directory of candybean
	 */
	public static File ROOT_DIR = new File(System.getProperty("user.dir")
			+ File.separator);

	/**
	 * The default test configuration directory
	 */
	public static File CONFIG_DIR = new File(System.getProperty("user.dir")
			+ File.separator + "config" + File.separator);

	/**
	 * {@link Properties} object created by loading the voodoo
	 * properties configuration file.
	 */
	private static Configuration CONFIG;

	/**
	 * The one Voodoo instance.  Created when a Voodoo instance is
	 * first called and persistent throughout the life of the tests.
	 */
	private static Candybean INSTANCE = null;


	/**
	 * Instantiate a Voodoo object.
	 * @throws IOException 
	 *
	 * @throws Exception if instantiating the logger fails
	 */
	private Candybean(Configuration config) throws IOException{
		CONFIG = config;
		// Add a system property so that LogManager loads the specified logging configuration file before getting logger.
		System.setProperty("java.util.logging.config.file", Candybean.getConfigrationFilePath());
		// Gets the logger based the configuration file specified at 'java.util.logging.config.file'
		LogManager.getLogManager().readConfiguration();
		LOG = Logger.getLogger(Candybean.class.getName());
		LOG.info("Instantiating Candybean with config: " + config.toString());
	}

	/**
	 * Get the global Voodoo instance.
	 *
	 * @param props  {@link Properties} object created from voodoo.properties
	 * @return global Voodoo instance
	 * @throws IOException 
	 * @throws Exception if instantiating the logger fails
	 */
	public static Candybean getInstance(Configuration config) throws IOException{
		if (Candybean.INSTANCE == null) {
			Candybean.INSTANCE = new Candybean(config); 
		} else {
			Candybean.LOG.info("Returning singleton Candybean with config:" + config.toString());
		}
		return Candybean.INSTANCE;
	}

	/**
	 * Get an {@link VInterface} for use by a test.
	 *
	 * @return a new {@link VInterface}
	 * @throws Exception
	 */
	public VInterface getInterface() {
		return new VInterface(this, this.getConfig());
	}

	//	public long getPageLoadTimeout() {
	//		return Long.parseLong(props.getString("perf.page_load_timeout"));
	//	}

	//	public String getTime() {
	//		return Utils.pretruncate("" + (new Date()).getTime(), 6);
	//	}

	/**
	 * @return The complete path to the candybean configuration file in this JRE
	 * @throws IOException
	 */
	public static String getConfigrationFilePath() throws IOException {
		return CONFIG_DIR.getCanonicalPath() + File.separator
				+ CONFIG_FILE_NAME;
	}

	public Configuration getConfig() {
		return CONFIG;
	}

	//	private Level getLogLevel() {
	//		String logLevel = Utils.getCascadingPropertyValue(props, "INFO", ".level");
	//		switch(logLevel) {
	//		case "SEVERE": return Level.SEVERE;
	//		case "WARNING": return Level.WARNING;
	//		case "INFO": return Level.INFO;
	//		case "FINE": return Level.FINE;
	//		case "FINER": return Level.FINER;
	//		case "FINEST": return Level.FINEST;
	//		default:
	//			LOG.warning("Configured system.log_level not recognized; defaulting to Level.INFO");
	//			return Level.INFO;
	//		}
	//	}
}
