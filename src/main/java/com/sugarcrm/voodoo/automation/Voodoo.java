package com.sugarcrm.voodoo.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.IAutomation.InterfaceType;
import com.sugarcrm.voodoo.automation.Utils;
import com.sugarcrm.voodoo.automation.framework.Selenium;


/**
 * @author Conrad Warmbold
 *
 */
public class Voodoo {

	public final Logger log;
	public final IAutomation auto;

	private static Voodoo instance = null;
	private final Properties props;

	private Voodoo(Properties props) throws Exception {
		this.props = props;
		this.log = this.getLogger();
		this.auto = this.getAutomation();
	}

	
	/**
	 * @param props
	 * @return
	 * @throws Exception
	 */
	public static Voodoo getInstance(Properties props) throws Exception {
		if (Voodoo.instance == null) Voodoo.instance = new Voodoo(props); 
		return Voodoo.instance;
	}

	
	public void pause(long ms) throws Exception {
		this.log.info("Pausing for " + ms + "ms via thread sleep.");
		Thread.sleep(ms);
	}

	
	public void interact(String message) {
		this.log.info("Interaction via popup dialog with message: " + message);
		JOptionPane.showInputDialog(message);
	}

	
	/**
	 * @return
	 * @throws Exception
	 */
	private IAutomation getAutomation() throws Exception {
		IAutomation auto = null;
		InterfaceType iType = this.getInterfaceType();
		String autoString = Utils.getCascadingPropertyValue(this.props, "selenium", "automation.framework");
		switch (autoString) {
		case "selenium":
			this.log.info("Instantiating Selenium automation with interface type: " + iType.name());
			auto = new Selenium(this, props, iType);
			break;
		case "robotium":
			throw new Exception("Robotium automation not yet supported.");
		default:
			throw new Exception("Automation framework not recognized.");
		}
		return auto;
    }

	
	/**
	 * @return
	 * @throws Exception
	 */
	private InterfaceType getInterfaceType() throws Exception {
		InterfaceType interfaceType = null;
		String interfaceTypeString = Utils.getCascadingPropertyValue(this.props, "chrome", "automation.interface");
		for (InterfaceType interfaceTypeIter : InterfaceType.values()) {
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
