package com.sugarcrm.candybean.examples;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.VInterface.SwipeableWebDriver;
import com.sugarcrm.candybean.automation.VInterface.Type;
import com.sugarcrm.candybean.configuration.Configuration;

public class IosTest extends AbstractTest {

	protected final Type iType = Type.IOS;

	/**
	 * The driver for this interface
	 */
	protected SwipeableWebDriver wd;

	/**
	 * The desired capabilities requested for the SwipeableWebDriver
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();

	/**
	 * Starts the android interface
	 * 
	 * @throws IOException
	 * @throws Exception
	 *             Failed to start interface
	 */
	@Before
	public void startIosInterface() throws IOException, Exception {

		String className = this.getClass().getSimpleName();

		Configuration config = new Configuration(new File(new File(
				System.getProperty("user.dir") + File.separator + "lib"
						+ File.separator).getCanonicalPath()
				+ File.separator + "capabilities.config"));

		capabilities
				.setCapability("app",
						new File(config.getValue(className + ".app"))
								.getAbsolutePath());

		iface.start(iType, capabilities);
		wd = (SwipeableWebDriver) iface.wd;
	}

}
