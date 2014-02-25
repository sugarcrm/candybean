package com.sugarcrm.candybean.automation;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class InternetExplorerInterface extends VInterface {

	protected InternetExplorerInterface(DesiredCapabilities capabilities)
			throws Exception {
		super(capabilities);
		this.start();
	}

	@Override
	public void start() throws Exception {
		String ieDriverPath = candybean.config.getPathValue("browser.ie_driver_path");
		logger.info("ieDriverPath: " + ieDriverPath);
		System.setProperty("webdriver.ie.driver", ieDriverPath);
		capabilities = DesiredCapabilities.internetExplorer();
		wd = new InternetExplorerDriver(capabilities);
	}
	
	@Override
	public void stop() throws Exception {
		logger.info("Stopping automation interface with type: IE");
		super.stop();
	}

	@Override
	public void restart() throws Exception {
		logger.info("Restarting automation interface with type: IE");
		super.stop();
	}

}
