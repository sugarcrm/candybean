package com.sugarcrm.candybean.automation.webdriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.test.AppiumStartProcess;

public class AndroidInterface extends WebDriverInterface {

	protected DesiredCapabilities capabilities;
	
	private AppiumStartProcess appiumProcess;

	public AndroidInterface(DesiredCapabilities capabilities)
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
		
		boolean automateAppium = Boolean.parseBoolean(candybean.config.getValue("appium.automate"));
		try {
			if(automateAppium) {
				if(appiumProcess == null || 
						(appiumProcess != null && !appiumProcess.isAlive())) {
					//Create the appium startup command from configuration file
//					List<String> appiumStartupCommands = new ArrayList<String>();
//					appiumStartupCommands.add(candybean.config.getValue("appium.path"));
//					appiumStartupCommands.add("-a");
//					appiumStartupCommands.add(candybean.config.getValue("appium.ip"));
//					appiumStartupCommands.add("-p");
//					appiumStartupCommands.add(candybean.config.getValue("appium.port"));
//					appiumStartupCommands.add("--avd");
//					appiumStartupCommands.add(candybean.config.getValue("avd.emulator.name"));
//					//Execute the automated start of appium
//					appiumProcess = new AppiumStartProcess(appiumStartupCommands, logger);
//					appiumProcess.start();
					List<String> appiumStartupCommands = new ArrayList<String>();
					appiumStartupCommands.add("sh");
					appiumStartupCommands.add("resources/scripts/start-appium.sh");
					appiumStartupCommands.add(candybean.config.getValue("android.home"));
					appiumStartupCommands.add(candybean.config.getValue("android.platform.tools"));
					appiumStartupCommands.add(candybean.config.getValue("android.tools"));
					appiumStartupCommands.add(candybean.config.getValue("android.build.tools"));
					appiumStartupCommands.add(candybean.config.getValue("appium.path"));
					appiumStartupCommands.add(candybean.config.getValue("appium.ip"));
					appiumStartupCommands.add(candybean.config.getValue("appium.port"));
					appiumStartupCommands.add(candybean.config.getValue("avd.emulator.name"));
					//Execute the automated start of appium
					appiumProcess = new AppiumStartProcess(appiumStartupCommands, logger);
					appiumProcess.start();
				}
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String errorMessage = "A valid appium session could not be found, please ensure that appium was started manually, of if using automated" +
				" appium startup, please ensure that it is properly configured in the configuration file";
		try {
			super.wd = new SwipeableWebDriver(new URL(
					"http://127.0.0.1:4723/wd/hub"), capabilities);
			super.start(); // requires wd to be instantiated first
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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

	@SuppressWarnings("deprecation")
	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		if(appiumProcess != null){
			appiumProcess.destroy();
		}
		super.wd.close();
		super.stop();
	}

	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}
}
