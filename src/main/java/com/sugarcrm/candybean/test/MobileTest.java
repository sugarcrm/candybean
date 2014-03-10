package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;

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
			this.config = new Configuration(new File(Candybean.CONFIG_DIR + File.separator + "capabilities.config"));
		} catch (IOException e) {
			logger.severe("Unable to find config/capabilities.config file required for mobile test execution");
			logger.severe(e.getMessage());
		}
	}
}
