package com.sugarcrm.candybean.automation.webdriver;

import java.net.MalformedURLException;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public class SaucelabsInterface extends WebDriverInterface {
	
	private DesiredCapabilities capabilities = new DesiredCapabilities();

	public SaucelabsInterface(Type iType) throws CandybeanException {
		super(iType);
	}
	
	@Override
	public void start() throws CandybeanException {
		String username = candybean.config.getValue("saucelabs.username");
		String accessKey = candybean.config.getValue("saucelabs.accessKey");
		capabilities.setBrowserName(candybean.config.getValue("saucelabs.browser"));
		capabilities.setCapability("version",candybean.config.getValue("saucelabs.version"));
		capabilities.setCapability("platform",candybean.config.getValue("saucelabs.platform"));
		try {
			logger.info("Attempting to connect to saucelabs with username: "+username+" and accesskey: "+accessKey);
			wd = new SaucelabsWebDriver(username,accessKey,capabilities);
		} catch (MalformedURLException e) {
			logger.severe("Unable to connect to saucelabs server with username: "+username+" and accesskey: "+accessKey);
			throw new CandybeanException(e);
		}
		super.start(); // requires wd to be instantiated first
	}
	
	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		super.stop();
	}

	@Override
	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}

	public DesiredCapabilities getCapabilities() {
		return capabilities;
	}

}