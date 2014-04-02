package com.sugarcrm.candybean.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Will configure any android test to use the desired capabilities from the configuration file.
 * 
 * @author Shehryar Farooq
 */
public class AndroidTest  {
	
	public AndroidTest() {
//		boolean automateAppium = Boolean.parseBoolean(candybean.config.getValue("appium.automate"));
//		try {
//			if(automateAppium) {
//				List<String> appiumStartupCommands = new ArrayList<String>();
//				appiumStartupCommands.add(candybean.config.getValue("appium.path"));
//				appiumStartupCommands.add("-a");
//				appiumStartupCommands.add(candybean.config.getValue("appium.ip"));
//				appiumStartupCommands.add("-p");
//				appiumStartupCommands.add(candybean.config.getValue("appium.port"));
//				
//				List<String> avdCommands = new ArrayList<String>();
//				avdCommands.add(candybean.config.getValue("avd.emulator.path"));
//				avdCommands.add("-avd");
//				avdCommands.add(candybean.config.getValue("avd.emulator.name"));
//				
//				AppiumProcess avdProcess = new AppiumProcess(avdCommands, logger);
//				avdProcess.start();
//			
//				try {
//					Thread.sleep(Long.parseLong(candybean.config.getValue("avd.device.timeout")));
//				} catch (InterruptedException e1) {
//					logger.warning("Unable to wait for emulator to start up");
//				}
//				
//				AppiumProcess appiumProcess = new AppiumProcess(appiumStartupCommands, logger);
//				appiumProcess.start();
//			}
//			String className = this.getClass().getSimpleName();
//			capabilities.setCapability("app", new File(config.getValue(className + ".app")).getAbsolutePath());
//			capabilities.setCapability("app-package", config.getValue(className + ".app-package"));
//			capabilities.setCapability("app-activity", config.getValue(className + ".app-activity"));
//			super.iface = candybean.getWebDriverInterface(super.type, capabilities);
//		} catch (Exception e) {
//			logger.severe(e.getMessage());
//		}
	}
}
