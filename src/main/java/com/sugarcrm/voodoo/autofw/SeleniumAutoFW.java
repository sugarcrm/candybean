package com.sugarcrm.voodoo.autofw;

import java.awt.Toolkit;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.sugarcrm.voodoo.Voodoo;

/**
 * @author 
 *
 */
public class SeleniumAutoFW implements VAutoFW {
	
	private final ResourceBundle props;
	private final WebDriver browser;
	
	/**
	 * 
	 * SeleniumAutoFW() 
	 * @param props 
	 * @param browserType 
	 * @throws Exception 
	 */
	public SeleniumAutoFW(ResourceBundle props, Voodoo.BrowserType browserType) throws Exception {
		this.props = props;
		this.browser = this.getBrowser(browserType);
	}
	
	/**
	 * 
	 * getControl()
	 * @param strategy
	 * @param hook
	 * @return 
	 * @throws Exception
	 * 
	 */
	@Override
	public VControl getControl(VAutoFW.Strategy strategy, String hook) throws Exception {
		WebElement webElement = null;
		switch (strategy) {
		case CSS:
			webElement = this.browser.findElement(By.cssSelector(hook));
			break;
		case XPATH:
			webElement = this.browser.findElement(By.xpath(hook));
			break;
		case ID:
			webElement = this.browser.findElement(By.id(hook));
			break;
		case NAME:
			webElement = this.browser.findElement(By.name(hook));
			break;
		}
		return new VSeleniumControl(webElement);
	}

	/**
	 * 
	 * VClick() 
	 * @param control 
	 * @throws Exception 
	 */
	@Override
	public void VClick(VControl control) throws Exception {
		if (control instanceof VSeleniumControl) {
			((WebElement) control).click();
		}
		else throw new Exception("Selenium: VControl not selenium-based.");
	}

	/**
	 * 
	 * VInput() 
	 * @param control 
	 * @param s 
	 * @throws Exception 
	 */
	@Override
	public void VInput(VControl control, String s) throws Exception {
		if (control instanceof VSeleniumControl) {
			((WebElement) control).sendKeys(s);
		}
		else throw new Exception("Selenium: VControl not selenium-based.");
	}
	
	/**
	 * 
	 * getBrowser() 
	 * @param browserType 
	 * @return 
	 * @throws Exception 
	 */
	private WebDriver getBrowser(Voodoo.BrowserType browserType) throws Exception {
		WebDriver webDriver = null;
		switch (browserType) {
		case FIREFOX:
//			FirefoxProfile ffProfile = (new ProfilesIni()).getProfile(props.getString("BROWSER.FIREFOX_PROFILE"));
//			if (System.getProperty("headless") != null) {
//				FirefoxBinary ffBinary = new FirefoxBinary();//new File("//home//conrad//Applications//firefox-10//firefox"));
//				ffBinary.setEnvironmentProperty("DISPLAY", ":1"); 
//				webDriver = new FirefoxDriver(ffBinary, ffProfile);
//			}
			webDriver = new FirefoxDriver();
			break;
		case CHROME:
			String chromeDriverPath = props.getString("BROWSER.CHROME_DRIVER_PATH");
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			webDriver = new ChromeDriver();
			break;
		case IE:
			webDriver = new InternetExplorerDriver();
			break;
		case SAFARI:
			throw new Exception("Selenium: safari browser not yet supported.");
		default:
			throw new Exception("Selenium: browser type not recognized.");
		}
		long implicitWait = Long.parseLong(props.getString("PERF.IMPLICIT_WAIT"));
		if (System.getProperty("headless") == null) {
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			webDriver.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
		}
		webDriver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		return webDriver;
	}
	
	/**
	 * @author 
	 *
	 */
	public class VSeleniumControl implements VControl {
		private final WebElement webElement;
		/**
		 * 
		 * VSeleniumControl() 
		 * @param webElement 
		 */
		public VSeleniumControl(WebElement webElement) { this.webElement = webElement; }
	}
}
