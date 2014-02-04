package com.sugarcrm.candybean.examples;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface.SwipeableWebDriver;
import com.sugarcrm.candybean.automation.VInterface.Type;
import com.sugarcrm.candybean.configuration.Configuration;

public class AndroidTest extends AbstractTest {
	
	/**
	 * 
	 */
	protected final Type iType = Type.ANDROID;
	
	/**
	 * 
	 */
	protected SwipeableWebDriver wd;
	
	/**
	 * 
	 */
	protected DesiredCapabilities capabilities = new DesiredCapabilities();
	
	/**
	 * Starts the android interface
	 * @throws IOException 
	 * @throws Exception Failed to start interface
	 */
	@Before
	public void startAndroidInterface() throws IOException, Exception {
		
		String className = this.getClass().getSimpleName();
		
		Configuration config = new Configuration(
				new File(new File(System.getProperty("user.dir")
						+ File.separator + "lib" + File.separator).getCanonicalPath() +File.separator+"capabilities.config"));
		
		capabilities.setCapability("app", new File(config.getValue(className+".app")).getAbsolutePath());
		capabilities.setCapability("app-package", config.getValue(className+".app-package"));
		capabilities.setCapability("app-activity", config.getValue(className+".app-activity"));
		
		iface.start(iType, capabilities);
		wd = (SwipeableWebDriver) iface.wd;
	}

}
