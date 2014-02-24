package com.sugarcrm.candybean.automation;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class AndroidInterface extends VInterface {

	public AndroidInterface(DesiredCapabilities capabilities) throws Exception {
		super(capabilities);
		this.start();
	}

	@Override
	protected void start() throws MalformedURLException {
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
		capabilities.setCapability(CapabilityType.VERSION, "4.4.2");
		capabilities.setCapability("device", "Android");
        wd = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	}

	@Override
	public void stop() throws Exception {
		logger.info("Stopping automation interface with type: ANDROID");
		if (wd != null) {
			wd.quit();
			wd = null;
		} else logger.warning("Automation interface already stopped.");
	}

	@Override
	public void restart() throws Exception {
		logger.info("Restarting automation interface with type: ANDROID");
		if (this.wd == null) throw new Exception("Automation interface not yet started; cannot restart.");
		this.stop();
		start();
	}

}
