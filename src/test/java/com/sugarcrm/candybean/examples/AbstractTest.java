package com.sugarcrm.candybean.examples;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import org.junit.Before;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

/**
 * An abstract class to be inherited by any test using candybean. Whenever a test class inherited by this class
 * is instantiated, the {@link Candybean} class variable will be instantiated automatically for use. Furthermore,
 * the logger will be configured to use the candybean logging configuration and will log all test-specific messages to
 * a separate log file. 
 * 
 * @author Shehryar Farooq
 *
 */
public abstract class AbstractTest {

	protected static Candybean candybean;

	/**
	 * The VInterface used to conduct this test
	 */
	protected static VInterface iface;

	/**
	 * Candybean logger
	 */
	protected static Logger logger;

	/**
	 * Starts the VInterface to be used for this test, and initializes the logger for this test
	 * by adding a new FileHandler specific to this tests class.
	 * @throws Exception
	 */
	public void initialize() throws IOException{
		candybean = AbstractTest.configureCandybean();
		iface = candybean.getInterface();
		FileHandler fh = new FileHandler("./log/"
				+ this.getClass().getSimpleName() + ".log");
		logger = Logger.getLogger(this.getClass().getSimpleName());
		logger.addHandler(fh);
	}

	/**
	 * Build a VInterface based on default configuration.
	 * 
	 * @return The VInterface
	 * @throws IOException 
	 *             If default configuration files do not exist.
	 */
	public static Candybean configureCandybean() throws IOException{
		Candybean candybean;
		String candybeanConfigStr = System
				.getProperty(Candybean.CONFIG_SYSTEM_PROPERTY);
		if (candybeanConfigStr == null)
			candybeanConfigStr = Candybean.CONFIG_DIR.getCanonicalPath() + File.separator
					+ Candybean.CONFIG_FILE_NAME;
		Configuration candybeanConfig = new Configuration(new File(
				Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		return candybean;
	}

}
