package com.sugarcrm.candybean.automation.webdriver;

import java.io.File;
import java.io.IOException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sugarcrm.candybean.utilities.OSValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.openqa.selenium.support.ThreadGuard;

public class ChromeInterface extends WebDriverInterface {

	public ChromeInterface() throws CandybeanException {
		super(Type.CHROME);
	}

	@Override
	/**
	 * Starts Chromedriver with options from the config file
	 * @throws CandybeanException
	 */
	public void start() throws CandybeanException {
		// Handle Chromedriver options from the config file
		String chromeDriverPath = candybean.config.getPathValue("browser.chrome.driver.path");
		validateChromeDriverExist(chromeDriverPath);
		String chromeDriverLogPath = candybean.config.getPathValue("browser.chrome.driver.log.path");
		logger.info("chromeDriverLogPath: " + chromeDriverLogPath);

		// Whitelisted-ips are required as of chromedriver v2.10
		String whiteListOption = "--whitelisted-ips=" + candybean.config.getValue("browser.chrome.driver.whitelisted-ips", "");
		String portStr = candybean.config.getValue("browser.chrome.driver.port");
		if (portStr == null) {
			portStr = "9001";
			logger.warning("browser.chrome.driver.port is not set, defaulting to port 9001");
		}
		String portOption = "--port=" + portStr;
		String logPath = "--log-path=" + chromeDriverLogPath;

		ImmutableList<String> argList = ImmutableList.of(whiteListOption, portOption, logPath);
		ImmutableMap<String,String> envList = ImmutableMap.of();
		ChromeDriverService chromeDriverService;
		try {
			chromeDriverService = new ChromeDriverService(new File(chromeDriverPath), Integer.parseInt(portStr), argList, envList);
		} catch (IOException e) {
			logger.severe("Could not process chromedriver options");
			throw new CandybeanException();
		}

		// Arguments can be passed to google-chrome using ChromeOptions with
		//     chromeOptions.addArgument("argument");
		// We don't need any right now, but we may in the future, so we leave it as is
		ChromeOptions chromeOptions = new ChromeOptions();

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
		logger.info("Starting Chromedriver with:\n    log path:"+ chromeDriverLogPath +
				"\n    driver path: " + chromeDriverPath);

		super.wd = ThreadGuard.protect(new ChromeDriver(chromeDriverService, chromeOptions));
		super.start(); // requires wd to be instantiated first
	}

	private void validateChromeDriverExist(String chromeDriverPath) throws CandybeanException {
		if(StringUtils.isEmpty(chromeDriverPath) || !new File(chromeDriverPath).exists()) {
			String error = "Unable to find chromedriver from the specified location ("+chromeDriverPath+") " +
					"in the configuration file! \nPlease add a configuration to the candybean config file for key \"browser.chrome.driver.path\" "
					+ "that indicates the absolute or relative location the driver.\nYou can download chromedriver from" +
					" https://sites.google.com/a/chromium.org/chromedriver/";
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
