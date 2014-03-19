package com.sugarcrm.candybean.automation.webdriver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public class IosInterface extends WebDriverInterface {
	
	protected DesiredCapabilities capabilities;

	public IosInterface(DesiredCapabilities capabilities) throws CandybeanException {
		super(Type.IOS);
		this.capabilities = capabilities;
	}

	@Override
	public void start() throws CandybeanException {
		logger.info("Starting automation interface with type: " + super.iType);
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "iOS");
        capabilities.setCapability(CapabilityType.VERSION, "6.0");
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
        try {
			super.wd = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException mue) {
			throw new CandybeanException(mue);
		} 
        super.start(); // requires wd to be instantiated first
	}

	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		super.wd.close();
		super.stop();
	}

	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}
}
