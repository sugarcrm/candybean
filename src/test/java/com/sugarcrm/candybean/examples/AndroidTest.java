package com.sugarcrm.candybean.examples;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.sugarcrm.candybean.automation.AndroidInterface;
import com.sugarcrm.candybean.configuration.Configuration;

/**
 * Will configure any android test to use the desired capabilities from the configuration file.
 * @author Shehryar Farooq
 *
 */
public class AndroidTest extends AbstractTest {
	
	/**
	 * The desired capabilities requested for the SwipeableWebDriver
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();
	
	public AndroidTest() throws IOException, Exception {
		String className = this.getClass().getSimpleName();
		Configuration config = new Configuration(
				new File(new File(System.getProperty("user.dir")
						+ File.separator + "lib" + File.separator).getCanonicalPath() +File.separator+"capabilities.config"));
		
		capabilities.setCapability("app", new File(config.getValue(className+".app")).getAbsolutePath());
		capabilities.setCapability("app-package", config.getValue(className+".app-package"));
		capabilities.setCapability("app-activity", config.getValue(className+".app-activity"));
		iface = new AndroidInterface(capabilities);
	}

}
