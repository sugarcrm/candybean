package com.sugarcrm.voodoo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;

import com.sugarcrm.voodoo.autofw.SeleniumAutoFW;
import com.sugarcrm.voodoo.autofw.VAutoFW;
import com.sugarcrm.voodoo.autofw.VControl;


public class Voodoo {

	public enum BrowserType { FIREFOX, IE, CHROME, SAFARI; }
	public final Logger log;

	private static Voodoo instance = null;
	private final ResourceBundle props;
	private final VAutoFW vAutoFW;
	
	private Voodoo(String bundleNamePrefix) throws Exception {
		this.props = ResourceBundle.getBundle(bundleNamePrefix, Locale.getDefault());
		this.log = this.getLogger(); 
		this.vAutoFW = this.getVAutoFW();
	}
	
	public static Voodoo getInstance(String bundleNamePrefix) throws Exception {
		if (Voodoo.instance == null) Voodoo.instance = new Voodoo(bundleNamePrefix);
		return instance;
	}
	
	public VControl getControl(VAutoFW.Strategy strategy, String hook) throws Exception {
		return vAutoFW.getControl(strategy, hook);
	}
	
	private VAutoFW getVAutoFW() throws Exception {
		VAutoFW vAutoFW = null;
		Voodoo.BrowserType browserType = this.getBrowserType();
		String vAutoFWString = props.getString("AUTOMATION.FRAMEWORK");
		// TODO: Add multi-browser support
		// TODO: Add multi-automation framework support
		vAutoFW = new SeleniumAutoFW(props, browserType);
		return vAutoFW;
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

	// Load loggingVoodoo.properties
	final InputStream inputStream = getClass().getResourceAsStream(
		"/loggingVoodoo.properties");
	try {
	    LogManager.getLogManager().readConfiguration(inputStream);
	} catch (final IOException e) {
	    Logger.getAnonymousLogger().severe(
		    "Could not load loggingVoodoo.properties file");
	    Logger.getAnonymousLogger().severe(e.getMessage());
	}

	Logger logger = Logger.getLogger(Voodoo.class.getName());

	// In the loggingVoodoo.properties file, only the log file path for the
	// root
	// can be specified, and not for other loggers.
	// We thus put the other logger log paths in files such as
	// voodoo.properties.
	FileHandler fh = new FileHandler(this.getLogPath());

	// fh.setFormatter(new SimpleFormatter()); // the format inherits from
	// root as set in loggingVoodoo.properties
	logger.addHandler(fh);

	// The log level for this logger inherits the root level set in
	// loggingVoodoo.properties.
	// It can also be explicitly set in the same config file.
	// logger.setLevel(this.getLogLevel());

	// Try out some messages
	logger.info("Check out the format string. Testing only");
	logger.severe("Another test message.");

	return logger;
    }
	
    private String getLogPath() {
	return props.getString("SYSTEM.LOG_PATH");
    }
	
/******************************************************************************
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
******************************************************************************/
}
