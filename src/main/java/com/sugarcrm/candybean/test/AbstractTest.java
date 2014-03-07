package com.sugarcrm.candybean.test;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * An abstract class to be inherited by any test using candybean. Whenever a test class inherited by this class
 * is instantiated, the {@link Candybean} class variable will be instantiated automatically for use. Furthermore,
 * the logger will be configured to use the candybean logging configuration and will log all test-specific messages to
 * a separate log file. 
 * 
 * @author Shehryar Farooq, Conrad Warmbold
 */
public abstract class AbstractTest {

	/**
	 * Candybean used to conduct this test
	 */
	protected Candybean candybean;
	
	/**
	 * Test-specific logger
	 */
	protected Logger logger;
	
	/**
	 * Creates a new file handler for this test with a class-named file.
	 * 
	 * @throws Exception 
	 */
	public AbstractTest() throws CandybeanException {
		FileHandler fh;
		try {
			fh = new FileHandler("./log/"
					+ this.getClass().getSimpleName() + ".log");
		} catch (Exception e) {
			throw new CandybeanException(e);
		}
		logger = Logger.getLogger(AbstractTest.class.getSimpleName());
		logger.addHandler(fh);
	}
	
	/**
	 * @throws CandybeanException 
	 */
	@Before
	public abstract void setUp() throws CandybeanException;
	
	/**
	 * @throws CandybeanException 
	 */
	@After
	public abstract void tearDown() throws CandybeanException;
}
