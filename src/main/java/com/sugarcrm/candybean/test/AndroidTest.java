package com.sugarcrm.candybean.test;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Will configure any android test to use the desired capabilities from the configuration file.
 * 
 * @author Shehryar Farooq
 */
public abstract class AndroidTest extends MobileTest {
	
	public AndroidTest() {
		super(Type.ANDROID);
		String className = this.getClass().getSimpleName();
		capabilities.setCapability("app", new File(config.getValue(className + ".app")).getAbsolutePath());
		capabilities.setCapability("app-package", config.getValue(className + ".app-package"));
		capabilities.setCapability("app-activity", config.getValue(className + ".app-activity"));
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
