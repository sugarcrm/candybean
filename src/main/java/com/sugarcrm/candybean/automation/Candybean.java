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
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.webdriver.AndroidInterface;
import com.sugarcrm.candybean.automation.webdriver.ChromeInterface;
import com.sugarcrm.candybean.automation.webdriver.FirefoxInterface;
import com.sugarcrm.candybean.automation.webdriver.InternetExplorerInterface;
import com.sugarcrm.candybean.automation.webdriver.IosInterface;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
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
	public static final String CONFIG_KEY = "config";
	
	/**
	 * The default name for the configuration file used to instantiate candybean.
	 */
	public static final String CONFIG_FILE_NAME = "candybean.config";

	/**
	 * The root directory of candybean
	 */
	public static final File ROOT_DIR = new File(System.getProperty("user.dir")
			+ File.separator);

	/**
	 * The default test configuration directory
	 */
	public static final File CONFIG_DIR = new File(System.getProperty("user.dir")
			+ File.separator + "config" + File.separator);

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
	private final Logger logger;

	/**
	 * Instantiates a Candybean object.
	 * 
	 * @throws Exception if instantiating the logger fails
	 */
	private Candybean(Configuration config) throws CandybeanException {
		try {
			this.config = config;
			logger = this.getLogger();
			logger.config("Instantiating Candybean with config: " + config.toString());
		} catch (Exception e) {
			throw new CandybeanException(e);
		}
	}

	/**
	 * Gets the global Candybean instance.
	 *
	 * @param config  {@link Configuration} object created from candybean.config
	 * @return singleton candybean instance
	 * @throws IOException 
	 * @throws Exception if instantiating the logger fails
	 */
	public static Candybean getInstance(Configuration config) throws CandybeanException {
		if (Candybean.instance == null) {
			Candybean.instance = new Candybean(config); 
		}
		return Candybean.instance;
	}
	
	/**
	 * Get the global candybean instance
	 * 
	 * @return global candybean instance based on a default configuration
	 * @throws Exception
	 */
	public static Candybean getInstance() throws CandybeanException {
		Configuration config = Candybean.getDefaultConfiguration();
		return Candybean.getInstance(config);
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
	
	/**
	 * Instantiates an interface given the type
	 * 
	 * @return WebDriverInterface
	 * @throws Exception
	 */
	public WebDriverInterface getWebDriverInterface() throws CandybeanException {
		logger.info("No webdriverinterface type specified from source code; will attempt to retrieve type from candybean configuration.");
		Type configType = AutomationInterface.parseType(this.config.getValue("automation.interface", "chrome"));
		logger.info("Found the following webdriverinterface type: " + configType + ", from configuration: " + this.config.configFile.getAbsolutePath());
		return this.getWebDriverInterface(configType);
	}
	
	/**
	 * Instantiates an interface given the type
	 * 
	 * @return WebDriverInterface
	 * @throws Exception
	 */
	public WebDriverInterface getWebDriverInterface(Type type) throws CandybeanException {
		switch (type) {
		case ANDROID:
			throw new CandybeanException(
					"Android interface cannot be instantiated without desired capabilities; please instantiate the interface"
							+ "using getWebDriverInterface(Type, DesiredCapabilities)");
		case IOS:
			throw new CandybeanException(
					"iOS interface cannot be instantiated without desired capabilities; please instantiate the interface"
							+ "using getWebDriverInterface(Type, DesiredCapabilities)");
		default:
			return this.getWebDriverInterface(type, null);
		}
	}
	
	public WebDriverInterface getWebDriverInterface(Type type, DesiredCapabilities capabilities) throws CandybeanException {
		WebDriverInterface iface = null;
		switch (type) {
		case FIREFOX:
			iface = new FirefoxInterface();
			break;
		case CHROME:
			iface = new ChromeInterface();
			break;
		case IE:
			iface = new InternetExplorerInterface();
			break;
		case SAFARI:
			throw new CandybeanException("Selenium: SAFARI interface type not yet supported");
		case ANDROID:
			iface = new AndroidInterface(capabilities);
			break;
		case IOS:
			iface = new IosInterface(capabilities);
			break;	
		default:
			throw new CandybeanException("WebDriver automation interface type not recognized: " + type);
		}
		return iface;
	}
	
	private static Configuration getDefaultConfiguration() throws CandybeanException {
		try {
			String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY);
			if (candybeanConfigStr == null) {
				candybeanConfigStr = Candybean.CONFIG_DIR.getCanonicalPath() + File.separator
						+ Candybean.CONFIG_FILE_NAME;
			}
			return new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		} catch (IOException ioe) {
			throw new CandybeanException(ioe);
		}
	}

	private Logger getLogger() throws Exception {
		// Add a system property so that LogManager loads the specified logging configuration file before getting logger.
		System.setProperty("java.util.logging.config.file", this.config.configFile.getCanonicalPath());
		// Gets the logger based the configuration file specified at 'java.util.logging.config.file'
		LogManager.getLogManager().reset();
		LogManager.getLogManager().readConfiguration();		
		return Logger.getLogger(Candybean.class.getSimpleName());
	}


}
