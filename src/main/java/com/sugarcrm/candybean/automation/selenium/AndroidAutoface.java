package com.sugarcrm.candybean.automation.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.sugarcrm.candybean.exceptions.CandybeanException;

public class AndroidAutoface extends SeleniumBrowserAutoface {

	protected DesiredCapabilities capabilities;

	public AndroidAutoface(DesiredCapabilities capabilities)
			throws CandybeanException {
		super(Type.ANDROID);
		this.capabilities = capabilities;
	}

	@Override
	public void start() throws CandybeanException {
		logger.info("Starting automation interface with type: " + this.iType);
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
		capabilities.setCapability(CapabilityType.VERSION, "4.4.2");
		capabilities.setCapability("device", "Android");
		String errorMessage = "A valid appium session could not be found, please ensure that appium was started manually, of if using automated" +
				" appium startup, please ensure that it is properly configured in the configuration file";
		try {
			super.wd = new SwipeableWebDriver(new URL(
					"http://127.0.0.1:4723/wd/hub"), capabilities);
			super.start(); // requires wd to be instantiated first
		} catch (MalformedURLException mue) {
			throw new CandybeanException(
					"Unable to connect to the appium server at the specified host and port. Please ensure that"
							+ "appium is already started or configure it to automatically start in the configuration file.");
		} catch (SessionNotFoundException mue) {
			logger.severe(errorMessage);
			throw new CandybeanException(errorMessage);
		} catch (SessionNotCreatedException mue) {
			logger.severe(errorMessage);
			throw new CandybeanException(errorMessage);
		} catch (UnreachableBrowserException mue) {
			logger.severe(errorMessage);
			throw new CandybeanException(errorMessage);
		} 
	}

	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		super.wd.close();
		super.stop();
	}

	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}
}
