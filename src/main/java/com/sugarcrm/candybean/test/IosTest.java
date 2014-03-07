package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public class IosTest extends MobileTest {

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
	public void instantiateInterface() throws CandybeanException {
//		iface = new IosInterface(capabilities);
	}

	@Override
	public void setUp() throws CandybeanException {
		// TODO Auto-generated method stub
	}

	@Override
	public void tearDown() throws CandybeanException {
		// TODO Auto-generated method stub
	}
}
