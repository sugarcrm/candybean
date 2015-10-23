package com.sugarcrm.candybean.automation;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.sugarcrm.candybean.automation.webdriver.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.utilities.CandybeanLogger;

public class AutomationInterfaceBuilder {

	/*
	 * The type of interface that the user wants to build.
	 */
	private Type type = null;
	
	/*
	 * The path to the APP. Required for Android and IOS interface.
	 */
	private String appPath = null;
	
	/*
	 * The APP package, required for Android interface.
	 */
	private String appPackage = null;

	/*
	 * The APP activity, required for Android interface.
	 */
	private String appActivity = null;
	
	/*
	 * Logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/*
	 * The class that calls this builder, required for logging and mobile interfaces.
	 */
	private Class<?> cls;
	
	/*
	 * Candybean instance
	 */
	private Candybean candybean = null;
	
	/**
	 * @param cls Typically the test class which uses this interface builder
	 */
	public AutomationInterfaceBuilder(Class<?> cls) {
		this.cls = cls;
	}

	/**
	 * Builds the interface specified. If an interface cannot be built using the specified parameters, an attempt
	 * will be made using candybean configuration files. If an interface cannot be built, an exception will be thrown.
	 * <!--
	 * <br><br>
	 * Below are the minimum settings required to build a specific type of interface.
	 * <br><br>
	 * <table border="1">
	 * 		<thead>
	 * 			<th>Interface</th>
	 * 			<th>Configuration Required</th>
	 * 		</thead>
	 *		<tbody>
	 *			<tr>
	 *				<td>Chrome, Firefox, Safari, IE</td>
	 *				<td>
	 *					To build a browser interface, the {@link Type} must be specified. If the type is not specified, the builder will attempt to build
	 *					the interface from the <b>automation.interface</b> setting from the candybean configuration file. If the interface can still not be built,
	 *					candybean will use a <b>chrome</b> interface by default.
	 *				</td>
	 *			</tr>
	 *			<tr>
	 *				<td>Android</td>
	 *				<td>
	 *					To build an Android interface, the {@link Type} <b>must</b> be set to {@link Type.Android}. If a type is not specified, the builder will
	 *					throw an exception. Furthermore, for an android test, the following settings must be specified:
	 *					<ul>
	 *						<li>appPath</li>
	 *						<li>appPackage</li>
	 *						<li>appActivity</li>
	 *					</ul>
	 *					If these settings are not specified in the builder, candybean will attempt to retrieve them from the configuration file. If the name of the
	 *					test class is 'AndroidTest', then candybean will look for the key 'AndroidTest.app-path' in the configuration file, and etc.<br><br>
	 *					If an Android interface cannot be build from the builder settings or the configuration settings, an exception will be thrown.
	 *				</td>
	 *			</tr>
	 *			<tr>
	 *				<td>Apple IOS</td>
	 *				<td>
	 *					To build an IOS interface, the {@link Type} <b>must</b> be set to {@link Type.IOS}. If a type is not specified, the builder will
	 *					throw an exception. Furthermore, for an IOS test, the following setting must be specified:
	 *					<ul>
	 *						<li>appPath</li>
	 *					</ul>
	 *					If the name of the test class is 'IOSTest', then candybean will look for the key 'IOSTest.app-path' in the configuration file.<br><br>
	 *					If an IOS interface cannot be built from the builder settings or the configuration settings, an exception will be thrown.
	 *				</td>
	 *			</tr>
	 *		</tbody> 	
	 * </table>
	 * -->
	 * @return
	 */
	public WebDriverInterface build() throws CandybeanException{
		WebDriverInterface iface = null;
		candybean = Candybean.getInstance();
		CandybeanLogger cbLogger;
		try {
			cbLogger = (CandybeanLogger) Logger.getLogger(Candybean.class.getSimpleName());
			if (!cbLogger.containsHandler(cls.getSimpleName())) {
				FileHandler fh = new FileHandler("./log/" + cls.getSimpleName() + ".log");
				cbLogger.addHandler(fh, cls.getSimpleName());
			}
		} catch (IOException e) {
			logger.severe("Unable to read/write to file " + "./log/" + cls.getSimpleName() + ".log");
		}
		if(type == null) {
			logger.info("Interface type not specified in the builder, attempting to build an interface using 'automation.interfrace' in the configuration.");
			iface = getWebDriverInterface();
		}else {
			iface = getWebDriverInterface(type);
		}
		return iface;
	}
	
	/**
	 * Instantiates an interface given the type
	 * 
	 * @return WebDriverInterface
	 * @throws Exception
	 */
	private WebDriverInterface getWebDriverInterface() throws CandybeanException {
		logger.info("No webdriverinterface type specified from source code; will attempt to retrieve type from candybean configuration.");
		Type configType = AutomationInterface.parseType(candybean.config.getValue("automation.interface", "chrome"));
		logger.info("Found the following webdriverinterface type: " + configType + ", from configuration: " + candybean.config.configFile.getAbsolutePath());
		return this.getWebDriverInterface(configType);
	}

	
	private WebDriverInterface getWebDriverInterface(Type type) throws CandybeanException {
		WebDriverInterface iface = null;
		DesiredCapabilities capabilities;
		String testClassName = cls.getSimpleName();
		// Required if we are using saucelabs.
		SaucelabsInterface sauceInterface = new SaucelabsInterface(type);
		GridInterface gridInterface = new GridInterface(type);
		switch (type) {
		case FIREFOX:
			iface = new FirefoxInterface();
			break;
		case CHROME:
			iface = new ChromeInterface();
			break;
		case IE:
			iface = new InternetExplorerInterface();
			break;
		case SAFARI:
			throw new CandybeanException("Selenium: SAFARI interface type not yet supported");
		case ANDROID:
			if(isAndroidFullyConfigured(true)) {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability("app", appPath);
				capabilities.setCapability("app-package", appPackage);
				capabilities.setCapability("app-activity", appActivity);
				sauceInterface.getCapabilities().setCapability("app", appPath);
				sauceInterface.getCapabilities().setCapability("app-package", appPackage);
				sauceInterface.getCapabilities().setCapability("app-activity", appActivity);
				iface = new AndroidInterface(capabilities);
			}else {
				logger.info("Builder was not fully configured to create an Android interface. \n" +
						"The variables appPath, appPackage, appActivity must all be set to create an android interface. \n" +
						"Trying to create Android interface from configuration file.");
				if(isAndroidFullyConfigured(false)){
					capabilities = new DesiredCapabilities();
					capabilities.setCapability("app", new File(candybean.config.getValue(testClassName + ".app")).getAbsolutePath());
					capabilities.setCapability("app-package", candybean.config.getValue(testClassName + ".app-package"));
					capabilities.setCapability("app-activity", candybean.config.getValue(testClassName + ".app-activity"));
					sauceInterface.getCapabilities().setCapability("app", new File(candybean.config.getValue(testClassName + ".app")).getAbsolutePath());
					sauceInterface.getCapabilities().setCapability("app-package", candybean.config.getValue(testClassName + ".app-package"));
					sauceInterface.getCapabilities().setCapability("app-activity", candybean.config.getValue(testClassName + ".app-activity"));
					iface = new AndroidInterface(capabilities);
				}else {
					String message = "Builder was unable to create an Android Interface from the provided settings in the builder, or \n" +
							"from the configuration file. If creating an android interface using the builder, the variables appPath, appPackage, \n " +
							"appActivity must all be set. If you want the builder to create the interface from the configuration file, please ensure that \n" +
							"the keys " + testClassName + ".app"  + ", " + testClassName + ".app-package" + ", and " + testClassName + ".app-activity" + " have \n" +
							"all been configured.";
					logger.severe(message);
					throw new CandybeanException(message);
				}
			}
			break;
		case IOS:
			if(isIOSFullyConfigured(true)) {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability("app", appPath);
				sauceInterface.getCapabilities().setCapability("app", appPath);
				iface = new IosInterface(capabilities);
			}else {
				logger.info("Builder was not fully configured to create an IOS interface. \n" +
						"The variable appPath must be set to create an ios interface. \n" +
						"Trying to create IOS interface from configuration file.");
				if(isIOSFullyConfigured(false)){
					capabilities = new DesiredCapabilities();
					capabilities.setCapability("app", new File(candybean.config.getValue(testClassName + ".app")).getAbsolutePath());
					sauceInterface.getCapabilities().setCapability("app", new File(candybean.config.getValue(testClassName + ".app")).getAbsolutePath());
					iface = new IosInterface(capabilities);
				}else {
					String message = "Builder was unable to create an IOS Interface from the provided settings in the builder, or \n" +
							"from the configuration file. If creating an IOS interface using the builder, the variable appPath \n " +
							"must be set. If you want the builder to create the interface from the configuration file, please ensure that \n" +
							"the key " + testClassName + ".app"  + " has been configured.";
					logger.severe(message);
					throw new CandybeanException(message);
				}
			}
			break;	
		default:
			throw new CandybeanException("WebDriver automation interface type not recognized: " + type);
		}

		boolean isSaucelabsEnabled = Boolean.parseBoolean(candybean.config.getValue("saucelabs.enabled"));
		boolean isGridEnabled = Boolean.parseBoolean(candybean.config.getValue("grid.enabled"));

		if(isGridEnabled && isSaucelabsEnabled) {
			throw new CandybeanException("Saucelabs and Grid should not be enabled at the same time. Check your config"
					+ " file again.");
		} else if(isGridEnabled) {
			logger.info("Grid was enabled by the user, using grid to carry out the tests for the interface: "+ type);
			return gridInterface;
		} else if (isSaucelabsEnabled) {
			logger.info("Saucelabs was enabled by the user, using saucelabs to carry out the tests for the interface: "
					+ type);
			// Add any desired capabilities if we are running mobile tests on saucelabs.
			return sauceInterface;
		} else {
			return iface;
		}
	}
	
	/*
	 * Checks whether the builder was sufficiently configured to create an IOS interface, but only if
	 * using the builder. If not using the builder, then the method will check if IOS was fully configured 
	 * in the configuration file.
	 * 
	 * @param usingBuilder Whether we are using the builder to completely configure the IOS interface.
	 * @return If IOS is fully configured from the builder, or the configuration.
	 */
	private boolean isIOSFullyConfigured(boolean usingBuilder) {
		String testClassName = cls.getSimpleName();
		if(usingBuilder){
			if(StringUtils.isEmpty(appPath)){
				return false;
			}else {
				return true;
			}
		}else if(candybean.config.hasKey(testClassName+".app")){
			return true;
		}else {
			return false;
		}
	}

	/*
	 * Checks whether the builder was sufficiently configured to create an ANDROID interface, but only if
	 * using the builder. If not using the builder, then the method will check if ANDROID was fully configured 
	 * in the configuration file.
	 * 
	 * @param usingBuilder Whether we are using the builder to completely configure the ANDROID interface.
	 * @return If ANDROID is fully configured from the builder, or the configuration.
	 */
	private boolean isAndroidFullyConfigured(boolean usingBuilder) {
		String testClassName = cls.getSimpleName();
		if(usingBuilder){
			if(StringUtils.isEmpty(appPath) || StringUtils.isEmpty(appPackage) || StringUtils.isEmpty(appActivity)){
				return false;
			}else {
				return true;
			}
		}else if(candybean.config.hasKey(testClassName+".app") && candybean.config.hasKey(testClassName+".app") && candybean.config.hasKey(testClassName+".app")){
			return true;
		}else {
			return false;
		}
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Set the path to the APP file. This is required only when you are
	 * building an Android or IOS interface. If an APP path is not specified,
	 * then the builder will attempt to retrieve it from the candybean configuration file.
	 * If it is not found using the {@link Configuration} object, an exception will be thrown.
	 * 
	 * @param appPath The local or remote path to the application package
	 */
	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}
	/**
	 * Set the app package. This is required only when you are
	 * building an Android interface. If an APP package is not specified,
	 * then the builder will attempt to retrieve it from the candybean configuration file.
	 * If it is not found using the {@link Configuration} object, an exception will be thrown.
	 * 
	 * @param appPackage The APP package
	 */
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	/**
	 * Set the APP activity package. This is required only when you are
	 * building an Android interface. If an APP activity package is not specified,
	 * then the builder will attempt to retrieve it from the candybean configuration file.
	 * If it is not found using the {@link Configuration} object, an exception will be thrown.
	 * 
	 * @param appActivity The APP activity
	 */
	public void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}

}
