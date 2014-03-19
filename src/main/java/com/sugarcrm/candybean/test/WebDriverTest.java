package com.sugarcrm.candybean.test;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * An abstract class to be inherited by any test using candybean. Whenever a test class inherited by this class
 * is instantiated, the {@link Candybean} class variable will be instantiated automatically for use. Furthermore,
 * the logger will be configured to use the candybean logging configuration and will log all test-specific messages to
 * a separate log file. 
 * 
 * @author Shehryar Farooq, Conrad Warmbold
 */
public abstract class WebDriverTest {

	/**
	 * Candybean used to conduct this test
	 */
	protected static Candybean candybean;
	
	/**
	 * AutomationInterface used to automate actions
	 */
	protected static WebDriverInterface iface;
	
	/**
	 * Test-specific logger
	 */
	protected Logger logger;
	
	/**
	 * Creates a new file handler for this test with a class-named file.
	 * 
	 * @throws Exception 
	 */
	public WebDriverTest() {
		try {
			candybean = Candybean.getInstance();
			logger = Logger.getLogger(Candybean.class.getSimpleName());
			// Only add the handler if it hasn't previously been added
			if (logger.getHandlers().length == 0) {
				FileHandler fh = new FileHandler("./log/" + this.getClass().getSimpleName() + ".log");
				logger.addHandler(fh);
			}
		} catch (IOException e) {
			logger = Logger.getLogger(Candybean.class.getSimpleName());
			logger.severe("Unable to read/write to file " + "./log/" + this.getClass().getSimpleName() + ".log");
		} catch (CandybeanException e) {
			logger = Logger.getLogger(Candybean.class.getSimpleName());
			logger.severe("Unable to instantiate candybean using default configuration settings");
		}
	}
	
}
