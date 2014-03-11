package com.sugarcrm.candybean.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
public abstract class AndroidTest extends MobileTest {
	
	public AndroidTest() {
		super(Type.ANDROID);
		List<String> commands = new ArrayList<String>();
		commands.add("/usr/local/bin/appium");
		commands.add("-a");
		commands.add("127.0.0.1");
		commands.add("-p");
		commands.add("4723");
		AppiumProcess appiumProcess = new AppiumProcess(commands, logger);
		appiumProcess.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String className = this.getClass().getSimpleName();
		capabilities.setCapability("app", new File(config.getValue(className + ".app")).getAbsolutePath());
		capabilities.setCapability("app-package", config.getValue(className + ".app-package"));
		capabilities.setCapability("app-activity", config.getValue(className + ".app-activity"));
		try {
			iface = candybean.getWebDriverInterface(type, capabilities);
		} catch (CandybeanException e) {
			logger.severe(e.getMessage());
		}
	}

	@Before
	public abstract void setUp() throws CandybeanException;

	@After
	public abstract void tearDown() throws CandybeanException;
}
