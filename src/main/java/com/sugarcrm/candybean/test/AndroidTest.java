package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.sugarcrm.candybean.automation.AndroidInterface;

/**
 * Will configure any android test to use the desired capabilities from the configuration file.
 * @author Shehryar Farooq
 *
 */
public class AndroidTest extends MobileTest {
	
	/**
	 * The desired capabilities requested for the SwipeableWebDriver
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();
	
	public AndroidTest() throws IOException, Exception {
		String className = this.getClass().getSimpleName();
		capabilities.setCapability("app", new File(config.getValue(className+".app")).getAbsolutePath());
		capabilities.setCapability("app-package", config.getValue(className+".app-package"));
		capabilities.setCapability("app-activity", config.getValue(className+".app-activity"));
	}

	@Override
	@Before
	public void instantiateInterface() throws Exception {
		iface = new AndroidInterface(capabilities);
	}

}
