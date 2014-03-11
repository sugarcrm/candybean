package com.sugarcrm.candybean.test;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class IosTest extends MobileTest {

	/**
	 * The desired capabilities requested for the SwipeableWebDriver
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();
	
	public IosTest() {
		super(Type.IOS);
		String className = this.getClass().getSimpleName();
		capabilities.setCapability("app", new File(config.getValue(className + ".app")).getAbsolutePath());
		try {
			iface = candybean.getWebDriverInterface(type, capabilities);
		} catch (CandybeanException e) {
			logger.severe(e.getMessage());
		}
	}
	
	@Before
	public abstract void setUp() throws CandybeanException;

	@After
	public abstract void tearDown() throws CandybeanException;
	
}
