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
import java.util.logging.Logger;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.configuration.Configuration;

/**
 * Voodoo is the primary interface for tests to use.	It provides
 * logging facilities and enables the loading of the appropriate
 * {@link IInterface} for use by the test's runtime.
 *
 * @author Conrad Warmbold
 */
public class Candybean {
	
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
	public final Logger log;

	/**
	 * {@link Properties} object created by loading the voodoo
	 * properties configuration file.
	 */
	public final Configuration config;

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
	 * The one Voodoo instance.  Created when a Voodoo instance is
	 * first called and persistent throughout the life of the tests.
	 */
	private static Candybean instance = null;
	private static boolean debug = false;


	/**
	 * Instantiate a Voodoo object.
	 *
	 * @throws Exception if instantiating the logger fails
	 */
	private Candybean(Configuration config) throws Exception {
		this.config = config;
		this.log = this.getLogger();
		debug = Boolean.parseBoolean(this.config.getValue("debug", "false"));
	}

	public boolean debug() { return Candybean.debug; }

	/**
	 * Get the global Voodoo instance.
	 *
	 * @param props  {@link Properties} object created from voodoo.properties
	 * @return global Voodoo instance
	 * @throws Exception if instantiating the logger fails
	 */
	public static Candybean getInstance(Configuration config) throws Exception {
		if (Candybean.instance == null) Candybean.instance = new Candybean(config); 
		return Candybean.instance;
	}

	/**
	 * Load the loggers specified in logging.properties.
	 *
	 * @return the initialized {@link Logger} object
	 * @throws Exception if initializing a logger fails
	 */
	private Logger getLogger() throws Exception {
		// Add a system property so that LogManager loads the specified logging configuration file before getting logger.
		System.setProperty("java.util.logging.config.file", Candybean.getConfigrationFilePath());
		// Gets the logger based the configuration file specified at 'java.util.logging.config.file'
		Logger logger = Logger.getLogger(Candybean.class.getName());
		return logger;
	}
	
	/**
	 * Instantiates an interface based on the configuration
	 * @return VInterface
	 * @throws Exception
	 */
	public VInterface getInterface() throws Exception {
		VInterface iface = null;
		if (Candybean.instance == null) {
			throw new Exception("Candybean has not yet been instantiated with a configuration");
		} else {
			switch (parseInterfaceType(this.config.getValue("automation.interface", "chrome"))) {
			case FIREFOX:
				iface = new FirefoxInterface(new DesiredCapabilities());
				break;
			case CHROME:
				iface = new ChromeInterface(new DesiredCapabilities());
				break;
			case IE:
				iface = new InternetExplorerInterface(new DesiredCapabilities());
			case SAFARI:
				throw new Exception("Selenium: SAFARI interface type not yet supported");
			default:
				throw new Exception("Selenium: browser type not recognized.");
			}
		}
		return iface;
	}
	
	/**
	 * Returns the interface type configured for use by the user.
	 * @param iTypeString
	 * @return
	 * @throws Exception
	 */
	private InterfaceType parseInterfaceType(String iTypeString) throws Exception {
		InterfaceType iType = null;
		for (InterfaceType iTypeIter : InterfaceType.values()) {
			if (iTypeIter.name().equalsIgnoreCase(iTypeString)) {
				iType = iTypeIter;
				break;
			}
		}
		if (iType == InterfaceType.ANDROID) throw new Exception("Android interface type not yet implemented.");
		if (iType == InterfaceType.IOS) throw new Exception("iOS interface type not yet implemented.");
		return iType;
	}

	/**
	 * @return The complete path to the candybean configuration file in this JRE
	 * @throws IOException
	 */
	public static String getConfigrationFilePath() throws IOException {
		return CONFIG_DIR.getCanonicalPath() + File.separator
				+ CONFIG_FILE_NAME;
	}
}
