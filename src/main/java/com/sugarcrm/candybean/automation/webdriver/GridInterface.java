package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.junit.Ignore;
import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is the interface for running a test through Selenium Grid
 * @author Eric Tam etam@sugarcrm.com
 */
public class GridInterface extends WebDriverInterface {
	private DesiredCapabilities capabilities = new DesiredCapabilities();

	public GridInterface(Type iType) throws CandybeanException {
		super(iType);
	}

	@Override
	public void start() throws CandybeanException {
		capabilities.setBrowserName(candybean.config.getValue("grid.browser"));
		capabilities.setCapability("platform",candybean.config.getValue("grid.platform"));
		String ip = candybean.config.getValue("grid.ip");
		String port = candybean.config.getValue("grid.port");
		logger.info("Starting interface with ip " + ip + " and port " + port);
		try {
			URL url = new URL("http://" + ip + ":" + port +  "/wd/hub");
			System.out.println(url.toString());
			wd = new RemoteWebDriver(url, capabilities);
		} catch (MalformedURLException e) {
			throw new CandybeanException(e);
		}
		super.start(); // requires wd to be instantiated first
	}

	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		super.stop();
	}

	@Override
	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}

	public DesiredCapabilities getCapabilities() {
		return capabilities;
	}

}
