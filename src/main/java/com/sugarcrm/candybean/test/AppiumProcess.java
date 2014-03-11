package com.sugarcrm.candybean.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class AppiumProcess extends Thread{
	
	private String command;
	
	private Logger logger;
	
	public AppiumProcess(String command, Logger logger) {
		this.command = command;
		this.logger = logger;
	}

	@Override
	public void run() {
		Process cmdProc;
		try {
			cmdProc = Runtime.getRuntime().exec(command);
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
			String line;
			while ((line = stdoutReader.readLine()) != null) {
			   logger.info(line);
			}

			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
			String line2;
			while ((line2 = stderrReader.readLine()) != null) {
				logger.severe(line2);
			}
		} catch (IOException e1) {
			logger.severe("Error message:");
			logger.severe("Unable to start appium automatically, please start appiums erver manually!");
		}
		super.run();
	}
	

}
