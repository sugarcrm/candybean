package com.sugarcrm.candybean.automation;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sugarcrm.candybean.automation.selenium.AndroidAutoface;
import com.sugarcrm.candybean.automation.selenium.ChromeAutoface;
import com.sugarcrm.candybean.automation.selenium.FirefoxAutoface;
import com.sugarcrm.candybean.automation.selenium.InternetExplorerAutoface;
import com.sugarcrm.candybean.automation.selenium.IosAutoface;
import com.sugarcrm.candybean.automation.selenium.SeleniumBrowserAutoface;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.utilities.CandybeanLogger;

public class BrowserAutofaceBuilder extends AutofaceBuilder {
	
	public enum Type { CHROME, FIREFOX, IE, SAFARI }

	/**
	 * @param cls Typically the test class which uses this autoface builder
	 */
	public BrowserAutofaceBuilder(Class<?> cls) {
		super(cls);
	}

	/**
	 * Builds the autoface specified. If an autoface cannot be built using the specified parameters, an attempt
	 * will be made using candybean configuration files. If an autoface cannot be built, an exception will be thrown.
	 * <br><br>
	 * Below are the minimum settings required to build a specific type of autoface.
	 * <br><br>
	 * <table border="1">
	 * 		<thead>
	 * 			<th>Interface</th>
	 * 			<th>Configuration Required</th>
	 * 		</thead>
	 *		<tbody>
	 *			<tr>
	 *				<td>Chrome</td>
	 *				<td>
	 *					To build a Chrome autoface, the {@link Type} must be specified. If the type is not specified, the builder will attempt to build
	 *					the autoface from the <b>autoface.browser.type</b> setting from the candybean configuration file. If the autoface can still not be built,
	 *					candybean will use a <b>chrome</b> autoface by default.
	 *				</td>
	 *			</tr>
	 *			<tr>
	 *				<td>Firefox</td>
	 *				<td>
	 *					To build a Firefox autoface, the {@link Type} must be specified. If the type is not specified, the builder will attempt to build
	 *					the autoface from the <b>autoface.browser.type</b> setting from the candybean configuration file. If the autoface can still not be built,
	 *					candybean will use a <b>chrome</b> autoface by default.
	 *				</td>
	 *			</tr>
	 *			<tr>
	 *				<td>IE</td>
	 *				<td>
	 *					To build an IE autoface, the {@link Type} must be specified. If the type is not specified, the builder will attempt to build
	 *					the autoface from the <b>autoface.browser.type</b> setting from the candybean configuration file. If the autoface can still not be built,
	 *					candybean will use a <b>chrome</b> autoface by default.
	 *				</td>
	 *			</tr>
	 *			<tr>
	 *				<td>Safari</td>
	 *				<td>
	 *					To build a Safari autoface, the {@link Type} must be specified. If the type is not specified, the builder will attempt to build
	 *					the autoface from the <b>automation.autoface</b> setting from the candybean configuration file. If the autoface can still not be built,
	 *					candybean will use a <b>chrome</b> autoface by default.
	 *				</td>
	 *			</tr>
	 *		</tbody> 	
	 * </table>
	 * @return
	 */
	@Override
	public Autoface build() throws CandybeanException {
		BrowserAutoface autoface = null;
		super.candybean = Candybean.getInstance();
		CandybeanLogger cbLogger;
		try {
			cbLogger = (CandybeanLogger) Logger.getLogger(Candybean.class.getSimpleName());
			FileHandler fh = new FileHandler("./log/" + cls.getSimpleName() + ".log");
			cbLogger.addHandler(fh, cls.getSimpleName());
		} catch (IOException e) {
			logger.severe("Unable to read/write to file " + "./log/" + cls.getSimpleName() + ".log");
		}
		if(type == null) {
			logger.info("Interface type not specified in the builder, attempting to build an autoface using 'automation.interfrace' in the configuration.");
			autoface = getWebDriverInterface();
		}else {
			autoface = getWebDriverInterface(type);
		}
		return autoface;
	}
	
	/**
	 * Instantiates an autoface given the type
	 * 
	 * @return WebDriverInterface
	 * @throws Exception
	 */
	private SeleniumBrowserAutoface getWebDriverInterface() throws CandybeanException {
		logger.info("No webdriverautoface type specified from source code; will attempt to retrieve type from candybean configuration.");
		Type configType = Autoface.parseType(candybean.config.getValue("automation.autoface", "chrome"));
		logger.info("Found the following webdriverautoface type: " + configType + ", from configuration: " + candybean.config.configFile.getAbsolutePath());
		return this.getWebDriverInterface(configType);
	}

	
	private SeleniumBrowserAutoface getWebDriverInterface(Type type) throws CandybeanException {
		SeleniumBrowserAutoface autoface = null;
		DesiredCapabilities capabilities;
		String testClassName = cls.getSimpleName();
		switch (type) {
		case FIREFOX:
			autoface = new FirefoxAutoface();
			break;
		case CHROME:
			autoface = new ChromeAutoface();
			break;
		case IE:
			autoface = new InternetExplorerAutoface();
			break;
		case SAFARI:
			throw new CandybeanException("Selenium: SAFARI autoface type not yet supported");
		case ANDROID:
			if(isAndroidFullyConfigured(true)) {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability("app", appPath);
				capabilities.setCapability("app-package", appPackage);
				capabilities.setCapability("app-activity", appActivity);
				autoface = new AndroidAutoface(capabilities);
			}else {
				logger.info("Builder was not fully configured to create an Android autoface. \n" +
						"The variables appPath, appPackage, appActivity must all be set to create an android autoface. \n" +
						"Trying to create Android autoface from configuration file.");
				if(isAndroidFullyConfigured(false)){
					capabilities = new DesiredCapabilities();
					capabilities.setCapability("app", new File(candybean.config.getValue(testClassName + ".app")).getAbsolutePath());
					capabilities.setCapability("app-package", candybean.config.getValue(testClassName + ".app-package"));
					capabilities.setCapability("app-activity", candybean.config.getValue(testClassName + ".app-activity"));
					autoface = new AndroidAutoface(capabilities);
				}else {
					String message = "Builder was unable to create an Android Interface from the provided settings in the builder, or \n" +
							"from the configuration file. If creating an android autoface using the builder, the variables appPath, appPackage, \n " +
							"appActivity must all be set. If you want the builder to create the autoface from the configuration file, please ensure that \n" +
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
				autoface = new IosAutoface(capabilities);
			}else {
				logger.info("Builder was not fully configured to create an IOS autoface. \n" +
						"The variable appPath must be set to create an ios autoface. \n" +
						"Trying to create IOS autoface from configuration file.");
				if(isIOSFullyConfigured(false)){
					capabilities = new DesiredCapabilities();
					capabilities.setCapability("app", new File(candybean.config.getValue(testClassName + ".app")).getAbsolutePath());
					autoface = new IosAutoface(capabilities);
				}else {
					String message = "Builder was unable to create an IOS Interface from the provided settings in the builder, or \n" +
							"from the configuration file. If creating an IOS autoface using the builder, the variable appPath \n " +
							"must be set. If you want the builder to create the autoface from the configuration file, please ensure that \n" +
							"the key " + testClassName + ".app"  + " has been configured.";
					logger.severe(message);
					throw new CandybeanException(message);
				}
			}
			break;	
		default:
			throw new CandybeanException("WebDriver automation autoface type not recognized: " + type);
		}
		return autoface;
	}
	
	/*
	 * Checks whether the builder was sufficiently configured to create an IOS autoface, but only if
	 * using the builder. If not using the builder, then the method will check if IOS was fully configured 
	 * in the configuration file.
	 * 
	 * @param usingBuilder Whether we are using the builder to completely configure the IOS autoface.
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
	 * Checks whether the builder was sufficiently configured to create an ANDROID autoface, but only if
	 * using the builder. If not using the builder, then the method will check if ANDROID was fully configured 
	 * in the configuration file.
	 * 
	 * @param usingBuilder Whether we are using the builder to completely configure the ANDROID autoface.
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
	 * building an Android or IOS autoface. If an APP path is not specified,
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
	 * building an Android autoface. If an APP package is not specified,
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
	 * building an Android autoface. If an APP activity package is not specified,
	 * then the builder will attempt to retrieve it from the candybean configuration file.
	 * If it is not found using the {@link Configuration} object, an exception will be thrown.
	 * 
	 * @param appActivity The APP activity
	 */
	public void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}

	@Override
	public boolean isFullyConfigured() {
		// TODO Auto-generated method stub
		return false;
	}

}
