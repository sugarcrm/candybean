package com.sugarcrm.candybean.automation;

import java.io.File;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxInterface extends VInterface {

	protected FirefoxInterface(DesiredCapabilities capabilities)
			throws Exception {
		super(capabilities);
		this.start();
	}

	@Override
	public void start() throws Exception {
		String profileName = candybean.config.getValue("browser.firefox_profile", "default");
		File ffBinaryPath = new File(candybean.config.getPathValue("browser.firefox_binary"));
		FirefoxProfile ffProfile = (new ProfilesIni()).getProfile(profileName);
		FirefoxBinary ffBinary = new FirefoxBinary(ffBinaryPath);
		logger.info("Instantiating Firefox with profile name: "
				+ profileName + " and binary path: " + ffBinaryPath);
		wd = new FirefoxDriver(ffBinary, ffProfile);
		super.start();
	}

	@Override
	public void stop() throws Exception {
		logger.info("Stopping automation interface with type: FIREFOX");
		super.stop();
	}

	@Override
	public void restart() throws Exception {
		logger.info("Restarting automation interface with type: FIREFOX");
		super.stop();
	}

}
