package com.sugarcrm.candybean.automation.webdriver;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public class FirefoxInterface extends WebDriverInterface {

	public FirefoxInterface() throws CandybeanException {
		super(Type.FIREFOX);
	}

	@Override
	public void start() throws CandybeanException {
		String profileName = candybean.config.getValue("browser.firefox.profile", "default");
		File ffBinaryPath = new File(candybean.config.getPathValue("browser.firefox.binary"));
		if(!ffBinaryPath.exists()){
			String error = "Unable to find firefox browser driver from the specified location in the configuration file! \n"
					+ "Please add a configuration to the candybean config file for key \"browser.firefox.binary\" that"
					+ "indicates the location of the binary.";
			logger.severe(error);
			throw new CandybeanException(error);
		} else {
			FirefoxProfile ffProfile = (new ProfilesIni()).getProfile(profileName);
			FirefoxBinary ffBinary = new FirefoxBinary(ffBinaryPath);
			logger.info("Instantiating Firefox with profile name: "
					+ profileName + " and binary path: " + ffBinaryPath);
			super.wd = new FirefoxDriver(ffBinary, ffProfile);
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
