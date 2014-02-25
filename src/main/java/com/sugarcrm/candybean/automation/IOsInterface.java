package com.sugarcrm.candybean.automation;

import java.net.URL;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IOsInterface extends VInterface {

	public IOsInterface(DesiredCapabilities capabilities) throws Exception {
		super(capabilities);
		this.start();
	}

	@Override
	public void start() throws Exception {
		logger.info("Starting automation interface with type: IOS");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "iOS");
        capabilities.setCapability(CapabilityType.VERSION, "6.0");
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
        wd = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        super.start();
	}

	@Override
	public void stop() throws Exception {
		logger.info("Stopping automation interface with type: IOS");
		super.stop();
	}

	@Override
	public void restart() throws Exception {
		logger.info("Restarting automation interface with type: IOS");
		super.stop();
	}

}
