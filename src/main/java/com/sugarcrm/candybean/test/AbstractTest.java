package com.sugarcrm.candybean.test;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.junit.Before;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.configuration.CB;

public abstract class AbstractTest {
	/**
	 * The VInterface used to conduct this testß
	 */
	protected static Candybean candybean;

	/**
	 * Candybean logger
	 */
	protected static Logger logger;

	@Before
	/**
	 * Starts the VInterface to be used for this test, and initializes the logger for this test
	 * by adding a new FileHandler specific to this tests class.
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		candybean = CB.configureCandybean();
		FileHandler fh = new FileHandler("./log/" + this.getClass().getSimpleName() + ".log");
		logger = Logger.getLogger(this.getClass().getSimpleName());
		logger.addHandler(fh);
	}

}