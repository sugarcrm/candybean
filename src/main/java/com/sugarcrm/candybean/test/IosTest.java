package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.AndroidInterface;
import com.sugarcrm.candybean.automation.IOsInterface;

public class IosTest extends MobileTest {

	/**
	 * The desired capabilities requested for the SwipeableWebDriver
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();
	
	public IosTest() throws Exception {
		String className = this.getClass().getSimpleName();
		capabilities.setCapability("app",new File(config.getValue(className + ".app")).getAbsolutePath());
	}

	@Override
	@Before
	public void instantiateInterface() throws Exception {
		iface = new IOsInterface(capabilities);
	}

}
