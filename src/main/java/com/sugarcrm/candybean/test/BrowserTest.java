package com.sugarcrm.candybean.test;

import org.junit.Before;

import com.sugarcrm.candybean.automation.Candybean;

public class BrowserTest extends AbstractTest {

	public BrowserTest() throws Exception {
		super();
	}

	@Override
	@Before
	public void instantiateInterface() throws Exception {
		iface = Candybean.getInstance().getInterface();
	}
}
