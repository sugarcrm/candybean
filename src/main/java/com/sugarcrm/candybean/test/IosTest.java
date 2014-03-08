package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class IosTest extends MobileTest {

	/**
	 * The desired capabilities requested for the SwipeableWebDriver
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();
	
	public IosTest() throws CandybeanException, IOException {
		String className = this.getClass().getSimpleName();
		capabilities.setCapability("app", new File(config.getValue(className + ".app")).getAbsolutePath());
	}

	@Override
	@Before
	public abstract void setUp() throws CandybeanException;

	@Override
	@After
	public abstract void tearDown() throws CandybeanException;
	
}
