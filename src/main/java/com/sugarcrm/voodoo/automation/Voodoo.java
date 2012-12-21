package com.sugarcrm.voodoo.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.sugarcrm.voodoo.automation.IInterface.Type;
import com.sugarcrm.voodoo.utilities.Utils;


/**
 * @author Conrad Warmbold
 *
 */
public class Voodoo {
	
	public final Logger log;
	public final Properties props;

	private static Voodoo instance = null;

	private Voodoo(Properties props) throws Exception {
		this.props = props;
		this.log = this.getLogger();
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
	

	/**
	 * @return
	 * @throws Exception
	 */
	public VInterface getInterface() throws Exception {
		String iType = Utils.getCascadingPropertyValue(this.props, "chome", "automation.interface");
		return this.getInterface(this.parseInterfaceType(iType));
	}
	
	/**
	 * @param browserType
	 * @return
	 * @throws Exception
	 */
	public VInterface getInterface(IInterface.Type iType) throws Exception {
		return new VInterface(this, this.props, iType);
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	private IInterface.Type parseInterfaceType(String iTypeString) throws Exception {
		IInterface.Type iType = null;
		for (IInterface.Type iTypeIter : IInterface.Type.values()) {
			if (iTypeIter.name().equalsIgnoreCase(iTypeString)) {
				iType = iTypeIter;
				break;
			}
		}
		//if (iType == Type.ANDROID) throw new Exception("Android interface type not yet implemented.");
		if (iType == Type.IOS) throw new Exception("iOS interface type not yet implemented.");
		return iType;
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
