package com.sugarcrm.candybean.test;

import org.junit.After;
import org.junit.Before;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class BrowserTest extends WebDriverTest {

	public BrowserTest() {
//		try {
//			iface = candybean.getWebDriverInterface();
//		} catch (CandybeanException e) {
//			logger.severe(e.getMessage());
//		}
	}

	/**
	 * Do anything before running the test, such as starting the iface
	 */
	@Before
	public abstract void setUp() throws CandybeanException;

	/**
	 * Do anything after running the test, such as stopping the iface
	 */
	@After
	public abstract void tearDown() throws CandybeanException;
}
