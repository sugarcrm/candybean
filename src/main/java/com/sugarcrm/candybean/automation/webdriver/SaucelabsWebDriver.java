package com.sugarcrm.candybean.automation.webdriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.Capabilities;

public class SaucelabsWebDriver extends RemoteWebDriver {
	
	/**
	 * The user name to connect to the Saucelabs server
	 */
	private String username;
	
	/**
	 * The access key to be authenticated to the saucelabs server
	 */
	private String accessKey;
	
	/**
	 * The desired capabilities for the test using this driver
	 */
	private Capabilities desiredCapabilities;

	/**
	 * @param username 
	 * @param accessKey
	 * @param desiredCapabilities
	 * @throws MalformedURLException
	 */
	public SaucelabsWebDriver(String username, String accessKey, Capabilities desiredCapabilities) throws MalformedURLException {
		super(new URL("http://" + username + ":" + accessKey
				+ "@ondemand.saucelabs.com:80/wd/hub"), desiredCapabilities);
		this.username = username;
		this.accessKey = accessKey;
		this.desiredCapabilities = desiredCapabilities;
	}

	public String getUsername() {
		return username;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public Capabilities getDesiredCapabilities() {
		return desiredCapabilities;
	}

}