package com.sugarcrm.candybean.test;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class BrowserTest extends WebDriverTest {

	public BrowserTest() throws Exception {
		super();
	}

	@BeforeClass
	public static void instantiateInterface() throws CandybeanException {
		candybean = Candybean.getInstance();
		iface = candybean.getWebDriverInterface();
	}

	/**
	 * Do anything before running the test, such as starting the iface
	 */
	@Override
	@Before
	public abstract void setUp() throws CandybeanException;

	/**
	 * Do anything after running the test, such as stopping the iface
	 */
	@Override
	@After
	public abstract void tearDown() throws CandybeanException;
}
