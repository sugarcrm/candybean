package com.sugarcrm.voodoo;

import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.SeleniumAutomation;
import com.sugarcrm.voodoo.automation.VAutomation;
import com.sugarcrm.voodoo.automation.VControl;


public class Voodoo implements VAutomation {

<<<<<<< HEAD
=======
/**
 * @author 
 *
 */
public class Voodoo {

	public enum BrowserType { FIREFOX, IE, CHROME, SAFARI; }
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
	public final Logger log;

	private static Voodoo instance = null;
	private final ResourceBundle props;
<<<<<<< HEAD
	private final VAutomation vAutomation;

	private Voodoo(ResourceBundle props) throws Exception {
		this.props = props;
		this.log = this.getLogger();
		this.vAutomation = this.getAutomation();
	}

	public static Voodoo getInstance(ResourceBundle props) throws Exception {
		if (Voodoo.instance == null) Voodoo.instance = new Voodoo(props);
		return instance;
	}

//	public static void closePopupWindow(WebDriver browser) throws Exception {
//		browser.close();
//		Set<String> windows = browser.getWindowHandles();
//		if (windows.size() > 1) throw new Exception("More than one window open; expecting 1");
//		for (String window : windows) browser.switchTo().window(window);
//	}
//	
//	public static void switchToMainWindow(WebDriver browser) throws Exception {
//		Set<String> windows = browser.getWindowHandles();
//		if (windows.size() > 1) throw new Exception("More than one window open; expecting 1");
//		for (String window : windows) browser.switchTo().window(window);
//	}

	@Override
	public void start(String url) throws Exception {
		this.log.info("Starting voodoo with url: " + url);
		this.vAutomation.start(url);
	}

	@Override
	public void stop() throws Exception {
		this.log.info("Stopping voodoo.");
		this.vAutomation.stop();
	}

	@Override
	public void acceptDialog() throws Exception {
		this.vAutomation.acceptDialog();
	}

	@Override
	public void switchToPopup() throws Exception {
		this.vAutomation.switchToPopup();
	}

	public void pause(long ms) throws Exception {
		Thread.sleep(ms);
=======
	private final VAutoFW vAutoFW;
	
	/**
	 * 
	 * Voodoo() 
	 * @param bundleNamePrefix 
	 * @throws Exception 
	 */
	private Voodoo(String bundleNamePrefix) throws Exception {
		this.props = ResourceBundle.getBundle(bundleNamePrefix, Locale.getDefault());
		this.log = this.getLogger(); 
		this.vAutoFW = this.getVAutoFW();
	}
	
	/**
	 * 
	 * getInstance() 
	 * @param bundleNamePrefix 
	 * @return 
	 * @throws Exception 
	 */
	public static Voodoo getInstance(String bundleNamePrefix) throws Exception {
		if (Voodoo.instance == null) Voodoo.instance = new Voodoo(bundleNamePrefix);
		return instance;
	}
	
	/**
	 * 
	 * getControl() 
	 * @param strategy 
	 * @param hook 
	 * @return 
	 * @throws Exception 
	 */
	public VControl getControl(VAutoFW.Strategy strategy, String hook) throws Exception {
		return vAutoFW.getControl(strategy, hook);
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
	}
	
	/**
	 * 
<<<<<<< HEAD
	 * @param props
	 * @param defaultValue
	 * @param key
	 * @return
	 */
	public static void interact(String s) {
		JOptionPane.showInputDialog(s);
	}

	@Override
	public String getText(Strategy strategy, String hook) throws Exception {
		return this.vAutomation.getText(strategy, hook);
	}

	@Override
	public void hover(Strategy strategy, String hook) throws Exception {
		vAutomation.hover(strategy, hook);
	}

	@Override
	public VControl getControl(VAutomation.Strategy strategy, String hook) throws Exception {
		return vAutomation.getControl(strategy, hook);
	}

	@Override
	public void click(VAutomation.Strategy strategy, String hook) throws Exception {
		vAutomation.click(strategy, hook);
	}

	@Override
	@Deprecated
	public void click(VControl control) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void input(VAutomation.Strategy strategy, String hook, String input) throws Exception {
		vAutomation.input(strategy, hook, input);
	}

	@Override
	@Deprecated
	public void input(VControl control, String input) throws Exception {
		// TODO Auto-generated method stub
		
	}

	private VAutomation getAutomation() throws Exception {
		VAutomation vAutomation = null;
=======
	 * getVAutoFW() 
	 * @return 
	 * @throws Exception 
	 */
	private VAutoFW getVAutoFW() throws Exception {
		VAutoFW vAutoFW = null;
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
		Voodoo.BrowserType browserType = this.getBrowserType();
		String vAutomationString = Utils.getCascadingPropertyValue(this.props, "selenium", "AUTOMATION.FRAMEWORK");
		switch (vAutomationString) {
		case "selenium":
			this.log.info("Instantiating Selenium automation with browserType: " + browserType.name());
			vAutomation = new SeleniumAutomation(this, props, browserType);
			break;
		case "robotium":
			throw new Exception("Robotium automation not yet supported.");
		default:
			throw new Exception("Automation framework not recognized.");
		}
		return vAutomation;
    }
<<<<<<< HEAD

=======
	
	/**
	 * 
	 * getBrowserType() 
	 * @return 
	 * @throws Exception 
	 */
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
	private Voodoo.BrowserType getBrowserType() throws Exception {
		Voodoo.BrowserType browserType = null;
		String browserTypeString = Utils.getCascadingPropertyValue(this.props, "chrome", "AUTOMATION.BROWSER");
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
<<<<<<< HEAD
=======
	
	/**
	 * 
	 * getLogger() 
	 * @return 
	 * @throws Exception 
	 */
	private Logger getLogger() throws Exception {
		
//		Logger logger = Logger.getLogger(Voodoo.class.getName());
//		FileHandler fh = new FileHandler(this.getLogPath());
//		fh.setFormatter(new SimpleFormatter());
//		logger.addHandler(fh);
//		logger.setLevel(this.getLogLevel());
//		return logger;

	        // logging settings are configured in src/main/resources/logback.xml.
	        // The logback.xml is placed there only for voodoo2 development purposes. 
	        // That is, when voodoo2 is running independent of Grimoire.
	        // Grimoire has a config file named logback-test.xml, which takes precedence
	        // over logback.xml.
	        // The sl4j/logback logger first looks for logback-test.xml, if not present,
	        // goes on to look for logback.xml. If no config file is found, the logger
	        // falls back to a default, which only logs to the console.
	        // logback.xml is generally meant for production use. In our case, 
	        // since voodoo2 is a library, it's not expected to provide logging settings.
	        // The settings choice should be with the application/end user.
	    
	        Logger logger = LoggerFactory.getLogger(Voodoo.class.getName());

	        logger.info("Trying out sl4j and logback");
	        logger.info("Using {}", "parameterized logging");
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b

	// logging settings are configured in src/main/resources/logback.xml.
    // The logback.xml is placed there only for voodoo2 development purposes. 
    // That is, when voodoo2 is running independent of Grimoire.
    // Grimoire has a config file named logback-test.xml, which takes precedence
    // over logback.xml.
    // The sl4j/logback logger first looks for logback-test.xml, if not present,
    // goes on to look for logback.xml. If no config file is found, the logger
    // falls back to a default, which only logs to the console.
    // logback.xml is generally meant for production use. In our case, 
    // since voodoo2 is a library, it's not expected to provide logging settings.
    // The settings choice should be with the application/end user.
	private Logger getLogger() throws Exception {
		Logger logger = Logger.getLogger(TestVoodoo.class.getName());
		FileHandler fh = new FileHandler(this.getLogPath());
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.setLevel(this.getLogLevel());
		return logger;

//		Logger logger = LoggerFactory.getLogger(Voodoo.class.getName());
//      logger.info("Trying out sl4j and logback");
//      logger.info("Using {}", "parameterized logging");
//		return logger;
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
