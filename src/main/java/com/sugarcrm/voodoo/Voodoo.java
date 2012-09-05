package com.sugarcrm.voodoo;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.sugarcrm.voodoo.automation.SeleniumAutomation;
import com.sugarcrm.voodoo.automation.VAutomation;
import com.sugarcrm.voodoo.automation.VControl;


public class Voodoo {

	public enum BrowserType { FIREFOX, IE, CHROME, SAFARI; }
	public final Logger log;

	private static Voodoo instance = null;
	private final ResourceBundle props;
	private final VAutomation vAutomation;
	
	private Voodoo(String bundleNamePrefix) throws Exception {
		this.props = ResourceBundle.getBundle(bundleNamePrefix, Locale.getDefault());
		this.log = this.getLogger();
		this.vAutomation = this.getAutomation();
	}
	
	public static Voodoo getInstance(String bundleNamePrefix) throws Exception {
		if (Voodoo.instance == null) Voodoo.instance = new Voodoo(bundleNamePrefix);
		return instance;
	}
	
	public VControl getControl(VAutomation.Strategy strategy, String hook) throws Exception {
		return vAutomation.getControl(strategy, hook);
	}
	
	private VAutomation getAutomation() throws Exception {
		VAutomation vAutomation = null;
		Voodoo.BrowserType browserType = this.getBrowserType();
		String vAutomationString = props.getString("AUTOMATION.FRAMEWORK");
		// TODO: Add multi-browser support
		// TODO: Add multi-automation framework support
		vAutomation = new SeleniumAutomation(props, browserType);
		return vAutomation;
    }
	
	private Voodoo.BrowserType getBrowserType() throws Exception {
		Voodoo.BrowserType browserType = null;
		String browserTypeString = props.getString("AUTOMATION.BROWSER");
		for (Voodoo.BrowserType browserTypeIter : Voodoo.BrowserType.values()) {
			if (browserTypeIter.name().equalsIgnoreCase(browserTypeString)) {
				browserType = browserTypeIter;
				break;
			}
		}
		return browserType;
	}
	
//	public Collection<Object[]> getBrowsers() throws Exception {
//		ArrayList<Object[]> browsers = new ArrayList<Object[]>(); // takes this form in order to work with JUnit.Parameterized
//		if(System.getProperties().containsKey("browser") || System.getProperties().containsKey("browsers")) {
//			String browserProperty = null;
//			if (System.getProperty("browser") != null) browserProperty = System.getProperty("browser");
//			if (System.getProperty("browsers") != null) browserProperty = System.getProperty("browsers");
//			String[] browserNames = browserProperty.split(",");
//			for(String b : browserNames) {
//				switch(b) {
//				case("firefox"):
//					initFFBrowser(browsers);
//					break; 
//				case("chrome"):
//					String chromeDriverPath = props.getString("BROWSER.CHROME_DRIVER_PATH");
//					System.setProperty("webdriver.chrome.driver", chromeDriverPath);
//					browsers.add(new Object[] { this.initBrowser(new ChromeDriver()) });
//					break;
//				case("ie"):
//					browsers.add(new Object[] { this.initBrowser(new InternetExplorerDriver()) });
//					break;
//				case("safari"):
//			    	browsers.add(new Object[] { Automation.initBrowser(new SafariDriver(), testProps) });
//					break;
//				default: 
//					throw new Exception("System property 'browser/s' not recognized.");
//				}
//			}
//		} else {
//			initFFBrowser(browsers);
//		}
//		return browsers;
//    }
	
//	public long getPageLoadTimeout() {
//		return Long.parseLong(props.getString("PERF.PAGE_LOAD_TIMEOUT"));
//	}

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
