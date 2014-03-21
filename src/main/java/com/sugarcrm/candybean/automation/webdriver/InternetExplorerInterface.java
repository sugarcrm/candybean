package com.sugarcrm.candybean.automation.webdriver;

import java.io.File;

import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public class InternetExplorerInterface extends WebDriverInterface {

	public InternetExplorerInterface() throws CandybeanException {
		super(Type.IE);
	}

	@Override
	public void start() throws CandybeanException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		String ieDriverPath = candybean.config.getPathValue("browser.ie.driver.path");
		if(StringUtils.isEmpty(ieDriverPath) || !new File(ieDriverPath).exists()){
			String error = "Unable to find internet explorer driver from the specified location("+ieDriverPath+") in the configuration file! \n"
					+ "Please add a configuration to the candybean config file for key \"browser.ie.driver.path\" "
					+ "that indicates the absolute location the driver.";
			logger.severe(error);
			throw new CandybeanException(error);
		}else{
			logger.info("ieDriverPath: " + ieDriverPath);
			System.setProperty("webdriver.ie.driver", ieDriverPath);
			capabilities = DesiredCapabilities.internetExplorer();
			super.wd = new InternetExplorerDriver(capabilities);
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
