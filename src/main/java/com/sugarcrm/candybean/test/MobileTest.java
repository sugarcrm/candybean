package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

public class MobileTest extends WebDriverTest {
	
	/**
	 * The configuration containing the parameters to configure this desired capabilities
	 */
	protected Configuration config;
	
	/**
	 * The type of mobile test
	 */
	protected Type type;
	
	/**
	 * The desired capabilities requested for the SwipeableWebDriver
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();

	public MobileTest(Type type) {
		super();
		this.type = type;
		try {
			String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY, Candybean.DEFAULT_CONFIG_FILE);
			this.config = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		} catch (IOException e) {
			logger.severe("Unable to find config/capabilities.config file required for mobile test execution");
			logger.severe(e.getMessage());
		}
	}
}
