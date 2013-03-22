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
		this.config.log = new OptionalLogger(this.log);
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
	 * @return a new {@link VInterface}
	 * @throws Exception if the browser specified in voodoo.properties
	 *							cannot be run or if WebDriver cannot be started
	 */
	public VInterface getInterface() throws Exception {
		String iType = this.config.getProperty("automation.interface", "chrome");
		return this.getInterface(this.parseInterfaceType(iType));
	}

	/**
	 * Get an {@link VInterface} for use by a test.
	 *
	 * @param iType  type of web browser to load
	 * @return a new {@link VInterface}
	 * @throws Exception if the browser specified cannot be run or if
	 *							WebDriver cannot be started
	 */
	public VInterface getInterface(VInterface.Type iType) throws Exception {
		return new VInterface(this, this.config, iType);
	}

	/**
	 * Convert a browser string into {@link IInterface.Type}
	 *
	 * @return browser type or null if the type is unrecognized
	 * @throws Exception if the browser type is unimplemented
	 */
	private VInterface.Type parseInterfaceType(String iTypeString) throws Exception {
		VInterface.Type iType = null;
		for (VInterface.Type iTypeIter : VInterface.Type.values()) {
			if (iTypeIter.name().equalsIgnoreCase(iTypeString)) {
				iType = iTypeIter;
				break;
			}
		}
		//if (iType == Type.ANDROID) throw new Exception("Android interface type not yet implemented.");
		if (iType == Type.IOS) throw new Exception("iOS interface type not yet implemented.");
		return iType;
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
