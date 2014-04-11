package com.sugarcrm.candybean.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

public class AppiumStartProcess extends Thread{
	
	private List<String> commands;
	
	private Logger logger;
	
	public AppiumStartProcess(List<String> commands, Logger logger) {
		this.commands = commands;
		this.logger = logger;
	}

	@Override
	public void run() {
        try {
			ProcessBuilder pb = new ProcessBuilder(commands);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			BufferedReader stream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str;
			while ((str = stream.readLine()) != null) {
				logger.info(str);
			}
		} catch (IOException e) {
			logger.severe(e.getMessage());
			logger.severe("Unable to start appium automatically, please start appium server manually!");
		}
		super.run();
	}
	

}
