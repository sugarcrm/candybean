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
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Will configure any android test to use the desired capabilities from the configuration file.
 * 
 * @author Shehryar Farooq
 */
public abstract class AndroidTest extends MobileTest {
	
	public AndroidTest() {
		super(Type.ANDROID);
		
		try {
			List<String> appiumStartupCommands = new ArrayList<String>();
			appiumStartupCommands.add("/usr/local/bin/appium");
			appiumStartupCommands.add("-a");
			appiumStartupCommands.add("127.0.0.1");
			appiumStartupCommands.add("-p");
			appiumStartupCommands.add("4723");
			appiumStartupCommands.add("--device-ready-timeout");
			appiumStartupCommands.add("300");
	
			//List<String> envSetupCommands = new ArrayList<String>();
			//envSetupCommands.add("/bin/bash");
			//envSetupCommands.add(Candybean.CONFIG_DIR.getCanonicalPath()+File.separator+"env_install.sh");
			
			///AppiumProcess envSetupProcess = new AppiumProcess(envSetupCommands, logger);
			//envSetupProcess.start();
			
			List<String> avdCommands = new ArrayList<String>();
			avdCommands.add("/usr/local/adt/sdk/tools/emulator");
			avdCommands.add("-avd");
			avdCommands.add("tester");
			
			//AppiumProcess appiumProcess = new AppiumProcess(appiumStartupCommands, logger);
			//appiumProcess.start();
			
			
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

			//AppiumProcess avdProcess = new AppiumProcess(avdCommands, logger);
			//avdProcess.start();
		
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			String className = this.getClass().getSimpleName();
			capabilities.setCapability("app", new File(config.getValue(className + ".app")).getAbsolutePath());
			capabilities.setCapability("app-package", config.getValue(className + ".app-package"));
			capabilities.setCapability("app-activity", config.getValue(className + ".app-activity"));
			iface = candybean.getWebDriverInterface(type, capabilities);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	@Before
	public abstract void setUp() throws CandybeanException;

	@After
	public abstract void tearDown() throws CandybeanException;
}
