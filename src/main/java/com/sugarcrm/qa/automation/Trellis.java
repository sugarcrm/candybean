package com.sugarcrm.qa.automation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
//import org.openqa.selenium.safari.SafariDriver;

public class Trellis {
	private final ResourceBundle testProps;
	private final Map<String, String> keywordMap = new HashMap<String, String>();
	public static enum USER_STATUS { ANY, ACTIVE, INACTIVE }
	public static enum FILTER_TYPE { CODE, NAME }
	public static enum GEO_US_STATE { CA, OK, TX } //CW: Fill-in...
	public static enum METER_FILTER_TYPE {  
		MTR_PRD_RNG ("Meter Production Date Range"),
		IMBAL_MON ("Imbalance Month");
		private final String s;
	    METER_FILTER_TYPE(String s) {
	        this.s = s;
	    }
	    public String toString() { return this.s; }
	}
	public Logger log;
	public WebDriver[] browsers;

	public Trellis(ResourceBundle testProps) throws Exception {
		this.testProps = testProps;
		this.initLogger(); // init log first; it's used here
		this.initBrowsers();
	}
	
	public Trellis(ResourceBundle testProps, ResourceBundle keywordProps) throws Exception {
		this.testProps = testProps;
		this.initLogger(); // init log first; it's used here
		this.initBrowsers();
		this.initKeywordMap(keywordProps);
	}
	
	public String getLogPath() {
		return testProps.getString("SYSTEM.LOG_PATH");
	}
	
	public Level getLogLevel() {
		switch(testProps.getString("SYSTEM.LOG_LEVEL")) {
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
	
	public String getBaseURL() {
		return testProps.getString(testProps.getString("ENV.BASE_URL"));
	}
		
	public long getPageLoadTimeout() {
		return Long.parseLong(testProps.getString("PERF.PAGE_LOAD_TIMEOUT"));
	}

	public long getImplicitWait() {
		return Long.parseLong(testProps.getString("PERF.IMPLICIT_WAIT"));
	}

	public String getUsername() {
		return testProps.getString("SESSION.USERNAME");
	}

	public String getPassword() {
		return testProps.getString("SESSION.PASSWORD");
	}
	
	public String getClassMethod(String keyword) throws Exception {
		if (this.keywordMap.isEmpty()) throw new Exception("keywordMap is empty.");
		return this.keywordMap.get(keyword);
	}
	
	private void initKeywordMap(ResourceBundle keywordProps) throws Exception {
		log.info("Building keyword map from properties file");
		for (String keyword : keywordProps.keySet()) {
			this.keywordMap.put(keyword, keywordProps.getString(keyword));
		}
	}

	private void initBrowsers() {
		if(System.getProperties().containsKey("browsers")) {
			log.info("Browser target(s) specified via system property:");
			List<WebDriver> aBrowsers = new ArrayList<WebDriver>();
			String[] browserNames = System.getProperty("browsers").split(",");
			for(String b : browserNames) {
				switch(b) {
				case("firefox"):
					log.info("Adding firefox browser as test target...");
					aBrowsers.add(new FirefoxDriver());
					break;
				case("chrome"):
					log.info("Adding chrome browser as test target");
					String chromeDriverPath = testProps.getString("BROWSER.CHROME_DRIVER_PATH");
					log.info("Setting chrome driver path:" + chromeDriverPath);
					System.setProperty("webdriver.chrome.driver", chromeDriverPath);
					aBrowsers.add(new ChromeDriver());
					break;
				case("ie"):
					log.info("Adding ie browser as test target");
					aBrowsers.add(new InternetExplorerDriver());
					break;
//				case("safari"):
//					log.info("Adding safari browser as test target");
//					aBrowsers.add(new SafariDriver());
//					break;
				default: 
					log.warning("  System property browser not recognized; skipping");
				}
			}
			browsers = aBrowsers.toArray(browsers);
		} else {
			log.warning("No system property-specified browser provided; defaulting to Firefox");
			browsers = new WebDriver[] { new FirefoxDriver() };
		}
		for(WebDriver browser : browsers) {
			browser.manage().timeouts().implicitlyWait(this.getImplicitWait(), TimeUnit.SECONDS);
		}
	}
	
	private void initLogger() throws Exception {
		log = Logger.getLogger(Trellis.class.getName());
		FileHandler fh = new FileHandler(this.getLogPath());
		fh.setFormatter(new SimpleFormatter());
		log.addHandler(fh);
		log.setLevel(this.getLogLevel());
	}
}
