package com.sugarcrm.candybean.automation.webdriver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public class AndroidInterface extends WebDriverInterface {
	
	protected DesiredCapabilities capabilities;

	public AndroidInterface(DesiredCapabilities capabilities) throws CandybeanException {
		super(Type.ANDROID);
		this.capabilities = capabilities;
	}

	@Override
	public void start() throws CandybeanException {
		logger.info("Starting automation interface with type: " + this.iType);
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
		capabilities.setCapability(CapabilityType.VERSION, "4.4.2");
		capabilities.setCapability("device", "Android");
        try {
			super.wd = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException mue) {
			throw new CandybeanException(mue);
		}
	}

	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		super.wd.close();
	}

	@Override
	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}
}
