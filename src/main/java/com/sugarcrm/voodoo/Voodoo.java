package com.sugarcrm.voodoo;

import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Voodoo {

	public final Logger log;
	public final WebDriver browser;
	
	private static Voodoo instance = null;
	private final ResourceBundle props;
	
	private Voodoo(String bundleNamePrefix) throws Exception {
		this.props = ResourceBundle.getBundle(bundleNamePrefix, Locale.getDefault());
		this.log = this.getLogger();
		this.browser = this.getBrowser();
	}
	
	public static Voodoo getInstance(String bundleNamePrefix) throws Exception {
		if (Voodoo.instance == null) Voodoo.instance = new Voodoo(bundleNamePrefix);
		return instance;
	}
	
	public WebDriver getBrowser() throws Exception {
		String chromeDriverPath = props.getString("BROWSER.CHROME_DRIVER_PATH");
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		WebDriver webDriver = new ChromeDriver();
//		FirefoxProfile ffProfile = (new ProfilesIni()).getProfile(props.getString("BROWSER.FIREFOX_PROFILE"));
//		if (System.getProperty("headless") != null) {
//			FirefoxBinary ffBinary = new FirefoxBinary();//new File("//home//conrad//Applications//firefox-10//firefox"));
//			ffBinary.setEnvironmentProperty("DISPLAY", ":1"); 
//			webDriver = new FirefoxDriver(ffBinary, ffProfile);
//		} else {
//			webDriver = new FirefoxDriver(ffProfile);
//		}
		long implicitWait = Long.parseLong(props.getString("PERF.IMPLICIT_WAIT"));
		if (System.getProperty("headless") == null) {
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			webDriver.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
		}
		webDriver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		return webDriver;
    }
	
	public Collection<Object[]> getBrowsers() throws Exception {
		ArrayList<Object[]> browsers = new ArrayList<Object[]>(); // takes this form in order to work with JUnit.Parameterized
		if(System.getProperties().containsKey("browser") || System.getProperties().containsKey("browsers")) {
			String browserProperty = null;
			if (System.getProperty("browser") != null) browserProperty = System.getProperty("browser");
			if (System.getProperty("browsers") != null) browserProperty = System.getProperty("browsers");
			String[] browserNames = browserProperty.split(",");
			for(String b : browserNames) {
				switch(b) {
				case("firefox"):
					initFFBrowser(browsers);
					break; 
				case("chrome"):
					String chromeDriverPath = props.getString("BROWSER.CHROME_DRIVER_PATH");
					System.setProperty("webdriver.chrome.driver", chromeDriverPath);
					browsers.add(new Object[] { this.initBrowser(new ChromeDriver()) });
					break;
				case("ie"):
					browsers.add(new Object[] { this.initBrowser(new InternetExplorerDriver()) });
					break;
//				case("safari"):
//			    	browsers.add(new Object[] { Automation.initBrowser(new SafariDriver(), testProps) });
//					break;
				default: 
					throw new Exception("System property 'browser/s' not recognized.");
				}
			}
		} else {
			initFFBrowser(browsers);
		}
		return browsers;
    }
	
	private void initFFBrowser(ArrayList<Object[]> browsers) {
		FirefoxProfile ffProfile = (new ProfilesIni()).getProfile(props.getString("BROWSER.FIREFOX_PROFILE"));
		if (System.getProperty("headless") != null) {
			FirefoxBinary ffBinary = new FirefoxBinary(new File("//Applications//Firefox 13"));
			ffBinary.setEnvironmentProperty("DISPLAY", ":1"); 
			browsers.add(new Object[] { this.initBrowser(new FirefoxDriver(ffBinary, ffProfile)) });
		} else {
			browsers.add(new Object[] { this.initBrowser(new FirefoxDriver(ffProfile)) });
		}
	}
	
	public WebDriver initBrowser(WebDriver browser) {
		long implicitWait = Long.parseLong(props.getString("PERF.IMPLICIT_WAIT"));
		if (System.getProperty("headless") == null) {
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			browser.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
		}
		browser.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		return browser;
	}
	
	public long getPageLoadTimeout() {
		return Long.parseLong(props.getString("PERF.PAGE_LOAD_TIMEOUT"));
	}

//	public String getTime() {
//		return Utils.trimString("" + (new Date()).getTime(), 6);
//	}
	
//	public void initializeBrowser(WebDriver browser) {
//		browser.get(this.getBaseURL());
//		this.log.info("Logging into:" + this.getBaseURL() + ", username:" + this.getUsername() + ", password:" + this.getPassword());
//	}
	
//	public void shutdownBrowser(WebDriver browser) {
//		browser.quit();
//	}
	
	private Logger getLogger() throws Exception {
		Logger logger = Logger.getLogger(Voodoo.class.getName());
		FileHandler fh = new FileHandler(this.getLogPath());
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.setLevel(this.getLogLevel());
		return logger;
	}
	
	private String getLogPath() {
		return props.getString("SYSTEM.LOG_PATH");
	}
	
	private Level getLogLevel() {
		switch(props.getString("SYSTEM.LOG_LEVEL")) {
		case "SEVERE": return Level.SEVERE;
		case "WARNING": return Level.WARNING;
		case "INFO": return Level.INFO;
		case "FINE": return Level.FINE;
		case "FINER": return Level.FINER;
		case "FINEST": return Level.FINEST;
		default:
			log.warning("Configured SYSTEM.LOG_LEVEL not recognized; defaulting to Level.INFO");
			return Level.INFO;
		}
	}
}
