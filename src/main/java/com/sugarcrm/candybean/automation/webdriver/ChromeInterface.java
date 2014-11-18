package com.sugarcrm.candybean.automation.webdriver;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.openqa.selenium.support.ThreadGuard;

public class ChromeInterface extends WebDriverInterface {

	public ChromeInterface() throws CandybeanException {
		super(Type.CHROME);
	}

	@Override
	public void start() throws CandybeanException {
		ChromeOptions chromeOptions = new ChromeOptions();
		String chromeDriverLogPath = candybean.config.getPathValue("browser.chrome.driver.log.path");
		logger.info("chromeDriverLogPath: " + chromeDriverLogPath);
		chromeOptions.addArguments("--log-path=" + chromeDriverLogPath);

		String chromeDriverPath = candybean.config.getPathValue("browser.chrome.driver.path");
		if("true".equals(candybean.config.getPathValue("parallel.enabled")) &&
				Integer.parseInt(candybean.config.getPathValue("parallel.threads")) > 1) {
			chromeDriverPath = chromeDriverPath.replaceAll("$", "_" + Thread.currentThread().getName());
		}

		logger.info("chromeDriverPath: " + chromeDriverPath);
		if(StringUtils.isEmpty(chromeDriverPath) || !new File(chromeDriverPath).exists()){
			String error = "Unable to find chrome browser driver from the specified location ("+chromeDriverPath+")  in the configuration file! \n"
					+ "Please add a configuration to the candybean config file for key \"browser.chrome.driver.path\" "
					+ "that indicates the absolute or relative location the driver.";
			logger.severe(error);
			throw new CandybeanException(error);
		}else{
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			logger.info("Instantiating Chrome with:\n    log path:"
					+ chromeDriverLogPath + "\n    driver path: "
					+ chromeDriverPath);
			super.wd = ThreadGuard.protect(new ChromeDriver(chromeOptions));
			super.start(); // requires wd to be instantiated first
		}
	}
	
	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		super.wd.close();
		super.stop();
	}

	@Override
	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}
}
