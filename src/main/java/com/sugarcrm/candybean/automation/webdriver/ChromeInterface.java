package com.sugarcrm.candybean.automation.webdriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public class ChromeInterface extends WebDriverInterface {

	public ChromeInterface() throws CandybeanException {
		super(Type.CHROME);
	}

	@Override
	public void start() throws CandybeanException {
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
		super.wd = new ChromeDriver(chromeOptions);
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
