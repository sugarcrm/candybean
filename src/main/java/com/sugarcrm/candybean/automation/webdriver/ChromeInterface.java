package com.sugarcrm.candybean.automation.webdriver;

import java.io.File;
import java.io.IOException;

import com.sugarcrm.candybean.utilities.OSValidator;
import org.apache.commons.io.FileUtils;
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
		validateChromeDriverExist(chromeDriverPath);

		// If parallel is enabled and the chromedriver-<os>_<thread-name> doesn't exist, duplicate one
		// from chromedriver-<os> and give it executable permission.
		if("true".equals(candybean.config.getPathValue("parallel.enabled"))) {
			String originalChromePath = chromeDriverPath;
			// Cross platform support
			if(OSValidator.isWindows()) {
				chromeDriverPath = chromeDriverPath.replaceAll("(.*)(\\.exe)", "$1_" +
						Thread.currentThread().getName() + "$2");
			} else {
				chromeDriverPath = chromeDriverPath.replaceAll("$", "_" + Thread.currentThread().getName());
			}

			if(!new File(chromeDriverPath).exists()) {
				try {
					FileUtils.copyFile(new File(originalChromePath), new File(chromeDriverPath));
					if(!OSValidator.isWindows()) { //Not needed in Windows
						Runtime.getRuntime().exec("chmod u+x " + chromeDriverPath);
					}
				} catch(IOException e) {
					String error = "Cannot duplicate a new chromedriver for parallelization";
					logger.severe(error);
					throw new CandybeanException(error);
				}
			}
		}

		logger.info("chromeDriverPath: " + chromeDriverPath);
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		logger.info("Instantiating Chrome with:\n    log path:"+ chromeDriverLogPath +
				"\n    driver path: " + chromeDriverPath);

		super.wd = ThreadGuard.protect(new ChromeDriver(chromeOptions));
		super.start(); // requires wd to be instantiated first
	}

	private void validateChromeDriverExist(String chromeDriverPath) throws CandybeanException {
		if(StringUtils.isEmpty(chromeDriverPath) || !new File(chromeDriverPath).exists()) {
			String error = "Unable to find chrome browser driver from the specified location ("+chromeDriverPath+") " +
					"in the configuration file! \n Please add a configuration to the candybean config file for key \"browser.chrome.driver.path\" "
					+ "that indicates the absolute or relative location the driver.";
			logger.severe(error);
			throw new CandybeanException(error);
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
