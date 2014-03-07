package com.sugarcrm.candybean.test;

import org.junit.Before;
import org.junit.BeforeClass;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class BrowserTest extends WebDriverTest {

	public BrowserTest() throws Exception {
		super();
	}

	@Override
	@BeforeClass
	public void instantiateInterface() throws CandybeanException {
		iface = Candybean.getInstance().getWebDriverInterface();
	}

	@Override
	@Before
	public abstract void setUp() throws CandybeanException;

	@Override
	@Before
	public abstract void tearDown() throws CandybeanException;
}
