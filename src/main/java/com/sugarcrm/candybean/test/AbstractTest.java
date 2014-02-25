package com.sugarcrm.candybean.test;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import org.junit.Before;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface;

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

	/**
	 * The VInterface used to conduct this test
	 */
	protected VInterface iface;

	/**
	 * Candybean logger
	 */
	protected Logger logger;
	
	/**
	 * Creates a new file handler for this test
	 * @throws Exception 
	 */
	public AbstractTest() throws Exception {
		FileHandler fh = new FileHandler("./log/"
				+ this.getClass().getSimpleName() + ".log");
		logger = Logger.getLogger(this.getClass().getSimpleName());
		logger.addHandler(fh);
	}
	
	/**
	 * Instantiate the appropriate interface for iface
	 * @throws Exception 
	 */
	@Before
	public abstract void instantiateInterface() throws Exception;

}
