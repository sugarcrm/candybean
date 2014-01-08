package com.sugarcrm.candybean.test;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.junit.Before;

import com.sugarcrm.candybean.CB;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface;

public abstract class AbstractTest {
	/**
	 * The VInterface used to conduct this testß
	 */
	protected static VInterface iface;
	
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
	public void initialize() throws Exception{
		iface = CB.buildInterface();
		FileHandler fh = new FileHandler("./log/"+this.getClass().getSimpleName()+".log");
		logger = Logger.getLogger(Candybean.class.getName());
		logger.addHandler(fh);
		iface.start();
	}
	
	
}
