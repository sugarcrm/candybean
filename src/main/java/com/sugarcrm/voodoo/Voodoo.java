package com.sugarcrm.voodoo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.Selenium;
import com.sugarcrm.voodoo.automation.IFramework;
import com.sugarcrm.voodoo.automation.VControl;
import com.sugarcrm.voodoo.Utils;


/**
 * @author Conrad Warmbold
 *
 */
public class Voodoo implements IAutomation {

	public enum InterfaceType { FIREFOX, IE, CHROME, SAFARI, ANDROID; }
	public final Logger log;

	private static Voodoo instance = null;
	private final Properties props;
	private final IFramework vAutomation;

	private Voodoo(Properties props) throws Exception {
		this.props = props;
		this.log = this.getLogger();
		this.vAutomation = this.getAutomation();
	}

	/**
	 * @param props
	 * @return
	 * @throws Exception
	 */
	public static Voodoo getInstance(Properties props) throws Exception {
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

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#start(java.lang.String)
	 */
	@Override
	public void start(String url) throws Exception {
		this.log.info("Starting voodoo with url: " + url);
		this.vAutomation.start(url);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#stop()
	 */
	@Override
	public void stop() throws Exception {
		this.log.info("Stopping voodoo.");
		this.vAutomation.stop();
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#acceptDialog()
	 */
	@Override
	public void acceptDialog() throws Exception {
		this.log.info("Accepting popup dialog.");
		this.vAutomation.acceptDialog();
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#switchToPopup()
	 */
	@Override
	public void switchToPopup() throws Exception {
		this.log.info("Switching to popup dialog.");
		this.vAutomation.switchToPopup();
	}

	/**
	 * @param ms
	 * @throws Exception
	 */
	@Override
	public void pause(long ms) throws Exception {
		this.log.info("Pausing for " + ms + "ms via thread sleep.");
		Thread.sleep(ms);
	}
	
	/**
	 * @param message
	 * @throws Exception
	 */
	public void interact(String message) {
		this.log.info("Interaction via popup dialog with message: " + message);
		JOptionPane.showInputDialog(message);
	}

	@Override
	public String getText(VControl control) throws Exception {
		this.log.info("Getting text for control: " + control);
		return this.vAutomation.getText(control);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#getText(com.sugarcrm.voodoo.VAutomation.Strategy, java.lang.String)
	 */
	@Override
	public String getText(Strategy strategy, String hook) throws Exception {
		this.log.info("Getting text for control with hook: " + hook + " via strategy: " + strategy);
		return this.vAutomation.getText(strategy, hook);
	}

	@Override
	public void hover(VControl control) throws Exception {
		this.log.info("Hovering on control: " + control);
		vAutomation.hover(control);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#hover(com.sugarcrm.voodoo.VAutomation.Strategy, java.lang.String)
	 */
	@Override
	public void hover(Strategy strategy, String hook) throws Exception {
		this.log.info("Hovering on control with hook: " + hook + " via strategy: " + strategy);
		vAutomation.hover(strategy, hook);
	}

	/**
	 * 
	 * getControl() 
	 * @param strategy 
	 * @param hook 
	 * @return 
	 * @throws Exception 
	 */
	@Override
	public VControl getControl(IAutomation.Strategy strategy, String hook) throws Exception {
		this.log.info("Getting control with hook: " + hook + " via strategy: " + strategy);
		return vAutomation.getControl(strategy, hook);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#click(com.sugarcrm.voodoo.VAutomation.Strategy, java.lang.String)
	 */
	@Override
	public void click(IAutomation.Strategy strategy, String hook) throws Exception {
		this.log.info("Clicking on control with hook: " + hook + " via strategy: " + strategy);
		vAutomation.click(strategy, hook);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#click(com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public void click(VControl control) throws Exception {
		this.log.info("Clicking on control: " + control);
		vAutomation.click(control);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#input(com.sugarcrm.voodoo.VAutomation.Strategy, java.lang.String, java.lang.String)
	 */
	@Override
	public void input(IAutomation.Strategy strategy, String hook, String input) throws Exception {
		this.log.info("Inputting text for control with hook: " + hook + " via strategy: " + strategy);
		vAutomation.input(strategy, hook, input);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#input(com.sugarcrm.voodoo.automation.VControl, java.lang.String)
	 */
	@Override
	public void input(VControl control, String input) throws Exception {
		this.log.info("Inputting text for control: " + control);
		vAutomation.input(control, input);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private IFramework getAutomation() throws Exception {
		IFramework vAutomation = null;
		Voodoo.InterfaceType iType = this.getInterfaceType();
		String vAutomationString = Utils.getCascadingPropertyValue(this.props, "selenium", "automation.framework");
		switch (vAutomationString) {
		case "selenium":
			this.log.info("Instantiating Selenium automation with interface type: " + iType.name());
			vAutomation = new Selenium(this, props, iType);
			break;
		case "robotium":
			throw new Exception("Robotium automation not yet supported.");
		default:
			throw new Exception("Automation framework not recognized.");
		}
		return vAutomation;
    }
	
	/**
	 * @return
	 * @throws Exception
	 */
	private Voodoo.InterfaceType getInterfaceType() throws Exception {
		Voodoo.InterfaceType interfaceType = null;
		String interfaceTypeString = Utils.getCascadingPropertyValue(this.props, "chrome", "automation.interface");
		for (Voodoo.InterfaceType interfaceTypeIter : Voodoo.InterfaceType.values()) {
			if (interfaceTypeIter.name().equalsIgnoreCase(interfaceTypeString)) {
				interfaceType = interfaceTypeIter;
				break;
			}
		}
		return interfaceType;
	}

//	public long getPageLoadTimeout() {
//		return Long.parseLong(props.getString("perf.page_load_timeout"));
//	}

//	public String getTime() {
//		return Utils.pretruncate("" + (new Date()).getTime(), 6);
//	}

	private Logger getLogger() throws Exception {
		// check for Log directory existence
		String currentWorkingPath = System.getProperty("user.dir");
		File tempLogPropsFile = new File(currentWorkingPath + File.separator + "logging.properties");
		tempLogPropsFile.createNewFile();
//		String defaultLogPath = logDirPath + File.separator + "voodoo.log";
//		String logPath = Utils.getCascadingPropertyValue(props, defaultLogPath, "system.log_path");
		OutputStream output = new FileOutputStream(tempLogPropsFile);
		this.props.store(output, "");
//		JOptionPane.showInputDialog("pause");
		InputStream input = new FileInputStream(tempLogPropsFile);
		Logger logger = Logger.getLogger(Voodoo.class.getName());
		LogManager.getLogManager().readConfiguration(input);
//		logger.setLevel(this.getLogLevel());
		tempLogPropsFile.delete();
		return logger;
	}

//	private Level getLogLevel() {
//		String logLevel = Utils.getCascadingPropertyValue(props, "INFO", ".level");
//		switch(logLevel) {
//		case "SEVERE": return Level.SEVERE;
//		case "WARNING": return Level.WARNING;
//		case "INFO": return Level.INFO;
//		case "FINE": return Level.FINE;
//		case "FINER": return Level.FINER;
//		case "FINEST": return Level.FINEST;
//		default:
//			log.warning("Configured system.log_level not recognized; defaulting to Level.INFO");
//			return Level.INFO;
//		}
//	}
}