package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import org.junit.Before;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

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
	 * The default test configuration directory
	 */
	public static File CONFIG_DIR = new File(System.getProperty("user.dir")
			+ File.separator + "config" + File.separator);

	/**
	 * The root directory of candybean
	 */
	public static File ROOT_DIR = new File(System.getProperty("user.dir")
			+ File.separator);
	
	/**
	 * Starts the VInterface to be used for this test, and initializes the logger for this test
	 * by adding a new FileHandler specific to this tests class.
	 * @throws Exception
	 */
	@Before
	public void initialize() throws Exception {
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
	 * @throws Exception
	 *             If default configuration files do not exist.
	 */
	public static Candybean configureCandybean() throws Exception {
		Candybean candybean;
		String candybeanConfigStr = System
				.getProperty(Candybean.CONFIG_SYSTEM_PROPERTY);
		if (candybeanConfigStr == null)
			candybeanConfigStr = CONFIG_DIR.getCanonicalPath() + File.separator
					+ Candybean.CONFIG_FILE_NAME;
		Configuration candybeanConfig = new Configuration(new File(
				Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		return candybean;
	}

	/**
	 * @return The complete path to the candybean configuration file in this JRE
	 * @throws IOException
	 */
	public static String getConfigrationFilePath() throws IOException {
		return CONFIG_DIR.getCanonicalPath() + File.separator
				+ Candybean.CONFIG_FILE_NAME;
	}

}
