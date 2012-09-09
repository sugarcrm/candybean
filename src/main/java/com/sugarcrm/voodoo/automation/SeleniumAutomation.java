package com.sugarcrm.voodoo.automation;

import java.awt.Toolkit;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;

import com.sugarcrm.voodoo.Utils;
import com.sugarcrm.voodoo.Voodoo;


public class SeleniumAutomation implements VAutomation {
	
	private final Voodoo voodoo;
	private final ResourceBundle props;
	private final WebDriver browser;
	
	public SeleniumAutomation(Voodoo voodoo, ResourceBundle props, Voodoo.BrowserType browserType) throws Exception {
		this.voodoo = voodoo;
		this.props = props;
		this.browser = this.getBrowser(browserType);
	}
	
	@Override
	public void start(String url) throws Exception {
		browser.get(url);
	}

	@Override
	public void stop() throws Exception {
		browser.close();
	}
	
	@Override
	public String getText(Strategy strategy, String hook) throws Exception {
		WebElement we = ((VSeleniumControl) this.getControl(strategy, hook)).webElement;
		return we.getText();
	}
	
	@Override
	public void hover(Strategy strategy, String hook) throws Exception {
		WebElement we = ((VSeleniumControl) this.getControl(strategy, hook)).webElement;
		Actions action = new Actions(browser);
		action.moveToElement(we).perform();
	}
	
	@Override
	public VControl getControl(VAutomation.Strategy strategy, String hook) throws Exception {
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

	@Override
	public void click(VAutomation.Strategy strategy, String hook) throws Exception {
		this.click(this.getControl(strategy, hook));
	}
	
	@Deprecated
	@Override
	public void click(VControl control) throws Exception {
		if (control instanceof VSeleniumControl) {
			((VSeleniumControl) control).webElement.click();
		}
		else throw new Exception("Selenium: VControl not selenium-based.");
	}

	@Override
	public void input(VAutomation.Strategy strategy, String hook, String input) throws Exception {
		this.input(this.getControl(strategy, hook), input);
	}
	
	@Deprecated
	@Override
	public void input(VControl control, String input) throws Exception {
		if (control instanceof VSeleniumControl) {
			((VSeleniumControl) control).webElement.sendKeys(input);
		}
		else throw new Exception("Selenium: VControl not selenium-based.");
	}
	
	@Override
	public void acceptDialog() throws Exception {
		Alert alert = browser.switchTo().alert();
		alert.accept();
	}
	
	@Override
	public void switchToPopup() throws Exception {
		String currentWindowHandle = browser.getWindowHandle();
		Set<String> windowHandles = browser.getWindowHandles();
		windowHandles.remove(currentWindowHandle);
		if (windowHandles.size() > 1) throw new Exception("Selenium: more than one popup/window detected");
		else browser.switchTo().window(windowHandles.iterator().next());
	}
	
	private WebDriver getBrowser(Voodoo.BrowserType browserType) throws Exception {
		WebDriver webDriver = null;
		switch (browserType) {
		case FIREFOX:
			String profileName = Utils.getCascadingPropertyValue(this.props, "default", "BROWSER.FIREFOX_PROFILE");
			String ffBinaryPath = Utils.getCascadingPropertyValue(this.props, "//home//conrad//Applications//firefox-10//firefox", "BROWSER.FIREFOX_BINARY");
			FirefoxProfile ffProfile = (new ProfilesIni()).getProfile(profileName);
			FirefoxBinary ffBinary = new FirefoxBinary(new File(ffBinaryPath));
//			if (System.getProperty("headless") != null) {
//				FirefoxBinary ffBinary = new FirefoxBinary();//new File("//home//conrad//Applications//firefox-10//firefox"));
//				ffBinary.setEnvironmentProperty("DISPLAY", ":1"); 
//				webDriver = new FirefoxDriver(ffBinary, ffProfile);
//			}
			voodoo.log.info("Instantiating Firefox with profile name: " + profileName + " and binary path: " + ffBinaryPath);
			webDriver = new FirefoxDriver(ffBinary, ffProfile);
			break;
		case CHROME:
			String chromeDriverPath = Utils.getCascadingPropertyValue(props, "/Users/cwarmbold/Documents/Workspace/voodoo2/lib/chromedriver-mac", "BROWSER.CHROME_DRIVER_PATH");
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			voodoo.log.info("Instantiating Chrome with driver path: " + chromeDriverPath);
			webDriver = new ChromeDriver();
			break;
		case IE:
			throw new Exception("Selenium: ie browser not yet supported.");
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
	
	public class VSeleniumControl implements VControl {
		private final WebElement webElement;
		public VSeleniumControl(WebElement webElement) { this.webElement = webElement; }
	}
}
