package com.sugarcrm.voodoo.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import com.sugarcrm.voodoo.automation.VInterface.Type;
import com.sugarcrm.voodoo.configuration.Configuration;
import com.sugarcrm.voodoo.utilities.OptionalLogger;

/**
 * Voodoo is the primary interface for tests to use.	It provides
 * logging facilities and enables the loading of the appropriate
 * {@link IInterface} for use by the test's runtime.
 *
 * @author Conrad Warmbold
 */
public class Voodoo {

	/**
	 * {@link Logger} object for use by tests.
	 */
	public final Logger log;

	/**
	 * {@link Properties} object created by loading the voodoo
	 * properties configuration file.
	 */
	private Configuration config;

	/**
	 * The one Voodoo instance.  Created when a Voodoo instance is
	 * first called and persistent throughout the life of the tests.
	 */
	private static Voodoo instance = null;
	private static boolean debug = false;


	/**
	 * Instantiate a Voodoo object.
	 *
	 * @throws Exception if instantiating the logger fails
	 */
	private Voodoo(Configuration config) throws Exception {
		this.config = config;
		this.log = this.getLogger();
		this.config.setLogger(new OptionalLogger(this.log));
		debug = Boolean.parseBoolean(this.config.getProperty("debug", "false"));
	}

	public boolean debug() { return Voodoo.debug; }

	/**
	 * Get the global Voodoo instance.
	 *
	 * @param props  {@link Properties} object created from voodoo.properties
	 * @return global Voodoo instance
	 * @throws Exception if instantiating the logger fails
	 */
	public static Voodoo getInstance(Configuration config) throws Exception {
		if (Voodoo.instance == null) Voodoo.instance = new Voodoo(config); 
		return Voodoo.instance;
	}

	/**
	 * Get an {@link VInterface} for use by a test.
	 *
	 * @return 				a new {@link VInterface}
	 * @throws Exception
	 */
	public VInterface getInterface() throws Exception {
		return new VInterface(this, this.config);
	}

	//	public long getPageLoadTimeout() {
	//		return Long.parseLong(props.getString("perf.page_load_timeout"));
	//	}

	//	public String getTime() {
	//		return Utils.pretruncate("" + (new Date()).getTime(), 6);
	//	}

	/**
	 * Load the loggers specified in logging.properties.
	 *
	 * @return the initialized {@link Logger} object
	 * @throws Exception if initializing a logger fails
	 */
	private Logger getLogger() throws Exception {
		// check for Log directory existence
		String currentWorkingPath = System.getProperty("user.dir");
		File tempLogPropsFile = new File(currentWorkingPath + File.separator + "logging.properties");
		tempLogPropsFile.createNewFile();
		//		String defaultLogPath = logDirPath + File.separator + "voodoo.log";
		//		String logPath = Utils.getCascadingPropertyValue(props, defaultLogPath, "system.log_path");
		OutputStream output = new FileOutputStream(tempLogPropsFile);
		this.config.store(output, null);
		//		JOptionPane.showInputDialog("pause");
		InputStream input = new FileInputStream(tempLogPropsFile);
		Logger logger = Logger.getLogger(Voodoo.class.getName());
		LogManager.getLogManager().readConfiguration(input);
		//		logger.setLevel(this.getLogLevel());
		tempLogPropsFile.delete();
		return logger;
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
	//			log.warning("Configured system.log_level not recognized; defaulting to Level.INFO");
	//			return Level.INFO;
	//		}
	//	}
}
