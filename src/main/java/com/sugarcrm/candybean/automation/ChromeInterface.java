package com.sugarcrm.candybean.automation;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeInterface extends VInterface {

	protected ChromeInterface(DesiredCapabilities capabilities)
			throws Exception {
		super(capabilities);
		this.start();
	}

	@Override
	public void start() throws Exception {
		ChromeOptions chromeOptions = new ChromeOptions();
		String chromeDriverLogPath = candybean.config.getPathValue("browser.chrome_driver_log_path");
		logger.info("chromeDriverLogPath: " + chromeDriverLogPath);
		chromeOptions.addArguments("--log-path=" + chromeDriverLogPath);
		String chromeDriverPath = candybean.config.getPathValue("browser.chrome_driver_path");
		logger.info("chromeDriverPath: " + chromeDriverPath);
		// chromeOptions.setBinary(new File(chromeDriverPath));
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		logger.info("Instantiating Chrome with:\n    log path:"
				+ chromeDriverLogPath + "\n    driver path: "
				+ chromeDriverPath);
		wd = new ChromeDriver(chromeOptions);
		super.start();
	}
	
	@Override
	public void stop() throws Exception {
		logger.info("Stopping automation interface with type: CHROME");
		super.stop();
	}

	@Override
	public void restart() throws Exception {
		logger.info("Restarting automation interface with type: CHROME");
		super.stop();
	}

}
