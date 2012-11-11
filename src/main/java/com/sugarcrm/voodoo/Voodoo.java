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
import com.sugarcrm.voodoo.automation.VHook;
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
		return Voodoo.instance;
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#start(java.lang.String)
	 */
	@Override
	public void start() throws Exception {
		this.log.info("Starting voodoo.");
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
	 * @see com.sugarcrm.voodoo.IAutomation#go(java.lang.String)
	 */
	@Override
	public void go(String url) throws Exception {
		this.log.info("Going to: " + url);
		this.vAutomation.go(url);
	}

	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#closeWindow()
	 */
	public void closeWindow() throws Exception {
		this.log.info("Closing Window.");
		this.vAutomation.closeWindow();
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
	 * @see com.sugarcrm.voodoo.IAutomation#focusByIndex(int)
	 */
	@Override
	public void focusByIndex(int window) throws Exception {
		this.log.info("Switching to popup dialog by Index: " + window);
		this.vAutomation.focusByIndex(window);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#focusByTitle(java.lang.String)
	 */
	@Override
	public void focusByTitle(String title) throws Exception {
		this.log.info("Switching to popup dialog by title: " + title);
		this.vAutomation.focusByTitle(title);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#focusByUrl(java.lang.String)
	 */
	@Override
	public void focusByUrl(String url) throws Exception {
		this.log.info("Switching to popup dialog by url: " + url);
		this.vAutomation.focusByUrl(url);
	}
	
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#pause(long)
	 */
	@Override
	public void pause(long ms) throws Exception {
		this.log.info("Pausing for " + ms + "ms via thread sleep.");
		Thread.sleep(ms);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#interact(java.lang.String)
	 */
	@Override
	public void interact(String message) {
		this.log.info("Interaction via popup dialog with message: " + message);
		JOptionPane.showInputDialog(message);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#getControl(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public VControl getControl(VHook hook) throws Exception {
		this.log.info("Getting control with hook: " + hook);
		return vAutomation.getControl(hook.hookStrategy, hook.hookString);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#getText(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public String getText(VHook hook) throws Exception {
		this.log.info("Getting text for control with hook: " + hook);
		return this.vAutomation.getText(hook.hookStrategy, hook.hookString);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#getSelected(com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public String getSelected(VControl control) throws Exception {
		this.log.info("Getting selected option from drop-down menu");
		return this.vAutomation.getSelected(control);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#getSelected(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public String getSelected(VHook hook) throws Exception{
		this.log.info("getting selected option from drop-down menu for strategy: " + hook.hookStrategy + ", hook: " + hook.hookString + ", and value: ");
		return this.vAutomation.getSelected(hook.hookStrategy, hook.hookString);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#select(com.sugarcrm.voodoo.automation.VHook, java.lang.String)
	 */
	@Override
	public void select(VHook hook, String value) throws Exception {
		this.log.info("Selecting drop-down for strategy: " + hook.hookStrategy + ", hook: " + hook.hookString + ", and value: " + value);
		this.vAutomation.select(hook.hookStrategy, hook.hookString, value);
	}
		
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#select(com.sugarcrm.voodoo.automation.VControl, java.lang.String)
	 */
	@Override
	public void select(VControl control, String value) throws Exception {
		this.log.info("Selecting drop-down for control with value: " + value);
		this.vAutomation.select(control, value);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#getText(com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public String getText(VControl control) throws Exception {
		this.log.info("Getting text for control: " + control);
		return this.vAutomation.getText(control);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#wait(com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public void waitFor(VControl control) throws Exception {
		this.log.info("Executing wait for control: " + control);
		this.vAutomation.waitFor(control);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#wait(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public void waitFor(VHook hook) throws Exception {
		this.log.info("Executing wait for strategy: " + hook.hookStrategy + ", hook: " + hook.hookString);
		this.vAutomation.waitFor(hook.hookStrategy, hook.hookString);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#wait(com.sugarcrm.voodoo.automation.VControl, java.lang.String, java.lang.String)
	 */
	@Override
	public void waitFor(VControl control, String attribute, String value) throws Exception {
		this.log.info("Executing wait for control: " + control + ", attribute:" + attribute + ", value: " + value);
		this.vAutomation.waitFor(control, attribute, value);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#wait(com.sugarcrm.voodoo.automation.VHook, java.lang.String, java.lang.String)
	 */
	@Override
	public void waitFor(VHook hook, String attribute, String value) throws Exception {
		this.log.info("Executing wait for strategy: " + hook.hookStrategy + ", hook: " + hook.hookString + ", attribute:" + attribute + ", value: " + value);
		this.vAutomation.waitFor(hook.hookStrategy, hook.hookString, attribute, value);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#click(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public void click(VHook hook) throws Exception {
		this.log.info("Clicking on control with hook: " + hook);
		vAutomation.click(hook.hookStrategy, hook.hookString);
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
	 * @see com.sugarcrm.voodoo.IAutomation#dragAndDrop(com.sugarcrm.voodoo.automation.VHook, com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public void dragNDrop(VHook hook1, VHook hook2) throws Exception {
		this.log.info("Drag and drop on controls with hooks: " + hook1
				+ " and " + hook2 + " via strategy: " + hook1.hookStrategy);
		vAutomation.dragNDrop(hook1.hookStrategy, hook1.hookString, hook2.hookString);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#dragAndDrop(com.sugarcrm.voodoo.automation.VControl, com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public void dragNDrop(VControl control1, VControl control2) throws Exception {
		this.log.info("Drag and drop control " + control1 + " to " + control2);
		vAutomation.dragNDrop(control1, control2);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#hover(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public void hover(VHook hook) throws Exception {
		this.log.info("Hovering on control with hook: " + hook);
		vAutomation.hover(hook.hookStrategy, hook.hookString);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#hover(com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public void hover(VControl control) throws Exception {
		this.log.info("Hovering on control: " + control);
		vAutomation.hover(control);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#input(com.sugarcrm.voodoo.automation.VHook, java.lang.String)
	 */
	@Override
	public void input(VHook hook, String input) throws Exception {
		this.log.info("Inputting text for control with hook: " + hook);
		vAutomation.input(hook.hookStrategy, hook.hookString, input);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.VAutomation#input(com.sugarcrm.voodoo.automation.VControl, java.lang.String)
	 */
	@Override
	public void input(VControl control, String input) throws Exception {
		this.log.info("Inputting text for control: " + control);
		vAutomation.input(control, input);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#rightClick(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public void rightClick(VHook hook) throws Exception {
		this.log.info("Right Clicking on control with hook: " + hook);
		vAutomation.rightClick(hook.hookStrategy, hook.hookString);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#rightClick(com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public void rightClick(VControl control) throws Exception {
		this.log.info("Right-clicking on control: " + control);
		vAutomation.rightClick(control);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#rightClick(com.sugarcrm.voodoo.automation.VHook)
	 */
	@Override
	public void scroll(VHook hook) throws Exception {
		this.log.info("Scrolling to hook: " + hook);
		vAutomation.scroll(hook.hookStrategy, hook.hookString);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.IAutomation#rightClick(com.sugarcrm.voodoo.automation.VControl)
	 */
	@Override
	public void scroll(VControl control) throws Exception {
		this.log.info("Scrolling to control: " + control);
		vAutomation.scroll(control);
	}
	
    /* (non-Javadoc)
     * @see com.sugarcrm.voodoo.IAutomation#select(com.sugarcrm.voodoo.automation.VHook, boolean)
     */
    @Override
    public void select(VHook hook, boolean isSelected) throws Exception {
            this.log.info("Select for control with hook: " + hook);
            vAutomation.select(hook.hookStrategy, hook.hookString, isSelected);
    }

    /* (non-Javadoc)
     * @see com.sugarcrm.voodoo.VAutomation#select(com.sugarcrm.voodoo.automation.VControl, boolean)
     */
    @Override
    public void select(VControl control, boolean isSelected) throws Exception {
            this.log.info("Select for control: " + control);
            vAutomation.select(control, isSelected);
    }

    /* (non-Javadoc)
     * @see com.sugarcrm.voodoo.IAutomation#getAttributeValue(com.sugarcrm.voodoo.automation.VHook, java.lang.String)
     */
    @Override
    public String getAttributeValue(VHook hook, String attribute) throws Exception {
            this.log.info("getAttributeValue for control with hook: " + hook);
            return vAutomation.getAttributeValue(hook.hookStrategy, hook.hookString, attribute);
    }

    /* (non-Javadoc)
     * @see com.sugarcrm.voodoo.VAutomation#getAttributeValue(com.sugarcrm.voodoo.automation.VControl, java.lang.String)
     */
    @Override
    public String getAttributeValue(VControl control, String attribute) throws Exception {
            this.log.info("getAttributeValue for control: " + control);
            return vAutomation.getAttributeValue(control, attribute);
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
