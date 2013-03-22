package com.sugarcrm.voodoo.automation;

import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VSelect;
import com.sugarcrm.voodoo.configuration.Configuration;

public class VInterface {

	public enum Type { FIREFOX, IE, CHROME, SAFARI, ANDROID, IOS; }

	public final WebDriver wd;
	//	public final AndroidInterface vac; //vac as in voodoo android control

	private final Voodoo voodoo;
	private final Configuration config;
	private HashMap<Integer, String> windowHandles = new HashMap<Integer, String>();
	private int windowIndex = 0;

	/**
	 * Instantiate VInterface
	 *
	 * @param voodoo  {@link Voodoo} object
	 * @param props   {@link Properties} for this test run
	 * @param iType   {@link IInterface.Type} of web browser to run
	 * @throws Exception
	 */
	public VInterface(Voodoo voodoo, Configuration config, Type iType)
			throws Exception {
		this.voodoo = voodoo;
		this.config = config;
		if (iType == Type.ANDROID) {
			//			this.vac = this.getAndroidControl();
			this.wd = null;
		}
		else {
			this.wd = this.getWebDriver(iType);
			//			this.vac = null;
			this.start();
		}
	}

	/**
	 * Pause the test for the specified duration.
	 *
	 * @param ms  duration of pause in milliseconds
	 * @throws Exception	 if the underlying {@link Thread#sleep} is interrupted
	 */
	public void pause(long ms) throws Exception {
		voodoo.log.info("Pausing for " + ms + "ms via thread sleep.");
		Thread.sleep(ms);
	}

	/**
	 * Display a modal dialog box to the test user.
	 *
	 * @param message	 String to display on the dialog box
	 * @throws Exception	 if the program is running headless (with no GUI)
	 */
	public void interact(String message) {
		voodoo.log.info("Interaction via popup dialog with message: " + message);
		JOptionPane.showInputDialog(message);
	}

	/**
	 * Launch and initialize a web browser.
	 * @throws Exception	 <i>not thrown</i>
	 */
	public void start() throws Exception {
		voodoo.log.info("Starting browser.");
	}

	/**
	 * Close the web browser and perform final cleanup.
	 *
	 * @throws Exception	  <i>not thrown</i>
	 */
	public void stop() throws Exception {
		voodoo.log.info("Stopping automation.");
		this.wd.quit();
	}

	/**
	 * Close the current browser window.
	 *
	 * @throws Exception	  <i>not thrown</i>
	 */
	public void closeWindow() throws Exception {
		voodoo.log.info("Closing window.");
		this.wd.close();
	}

	/**
	 * Load a URL in the browser window.
	 *
	 * @param url	the URL to be loaded by the browser
	 * @throws Exception		<i>not thrown</i>
	 */
	public void go(String url) throws Exception {
		voodoo.log.info("Going to URL and switching to window: " + url);
		this.wd.get(url);
		this.wd.switchTo().window(this.wd.getWindowHandle());
	}

	/**
	 * Click &quot;OK&quot; on a modal dialog box (usually referred to
	 * as a &quot;javascript dialog&quot;).
	 *
	 * @throws Exception	 if no dialog box is present
	 */
	public void acceptDialog() throws Exception {
		voodoo.log.info("Accepting dialog.");
		Alert alert = this.wd.switchTo().alert();
		alert.accept();
	}

	/**
	 * Dismisses a modal dialog box (usually referred to
	 * as a &quot;javascript dialog&quot;).
	 *
	 * @throws Exception	 if no dialog box is present
	 */
	public void dismissDialog() throws Exception {
		voodoo.log.info("Dismissing dialog.");
		Alert alert = this.wd.switchTo().alert();
		alert.dismiss();
	}

	/**
	 * Focus a browser window by its index.
	 *
	 * <p>The order of browser windows is somewhat arbitrary and not
	 * guaranteed, although window creation time ordering seems to be
	 * the most common.</p>
	 *
	 * @param index  the window index
	 * @throws Exception	 if the specified window cannot be found
	 */
	public void focusByIndex(int index) throws Exception {
		voodoo.log.info("Focusing window by index: " + index);
		Set<String> Handles = this.wd.getWindowHandles();
		while (Handles.iterator().hasNext()) {
			String windowHandle = Handles.iterator().next();
			if (!windowHandles.containsValue(windowHandle)) {
				windowHandles.put(windowIndex, windowHandle);
				windowIndex++;
			}
			Handles.remove(windowHandle);
		}
		this.wd.switchTo().window(windowHandles.get(index));
	}

	/**
	 * Focus a browser window by its window title.
	 *
	 * <p>If more than one window has the same title, the first
	 * encountered is the one that is focused.</p>
	 *
	 * @param title  the exact window title to be matched
	 * @throws Exception	  if the specified window cannot be found
	 */
	public void focusByTitle(String title) throws Exception {
		voodoo.log.info("Focusing window by title: " + title);
		Set<String> handles = this.wd.getWindowHandles();
		while (handles.iterator().hasNext()) {
			String windowHandle = handles.iterator().next();
			WebDriver window = this.wd.switchTo().window(windowHandle);
			if (window.getTitle().equals(title)) {
				break;
			}
			handles.remove(windowHandle);
		}
	}

	/**
	 * Focus a browser window by its URL.
	 *
	 * <p>If more than one window has the same URL, the first
	 * encountered is the one that is focused.</p>
	 *
	 * @param url	the URL to be matched
	 * @throws Exception	  if the specified window cannot be found
	 */
	public void focusByUrl(String url) throws Exception {
		voodoo.log.info("Focusing window by url: " + url);
		Set<String> handles = this.wd.getWindowHandles();
		while (handles.iterator().hasNext()) {
			String windowHandle = handles.iterator().next();
			WebDriver window = this.wd.switchTo().window(windowHandle);
			if (window.getCurrentUrl().equals(url)) {
				break;
			}
			handles.remove(windowHandle);
		}
	}

	/**
	 * Maximize the browser window.
	 *
	 * @throws Exception	 <i>not thrown</i>
	 */
	public void maximize() {
		voodoo.log.info("Maximizing window");
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		this.wd.manage().window().maximize();//.setSize(new Dimension(screenSize.width, screenSize.height));
	}

	/**
	 * Get a control from the current page.
	 *
	 * @param hook	 description of how to find the control
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VControl getControl(VHook hook) throws Exception {
		return new VControl(this.voodoo, this, hook);
	}

	/**
	 * Get a control from the current page by index.
	 *
	 * @param hook	 description of how to find the control
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VControl getControl(VHook hook, int index) throws Exception {
		return new VControl(this.voodoo, this, hook, index);
	}

	/**
	 * Get a control from the current page.
	 *
	 * @param strategy  method to use to search for the control
	 * @param hook		  string to find using the specified strategy
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VControl getControl(Strategy strategy, String hook) throws Exception {
		return this.getControl(new VHook(strategy, hook));
	}

	/**
	 * Get a control from the current page by index.
	 *
	 * @param strategy  method to use to search for the control
	 * @param hook		  string to find using the specified strategy
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VControl getControl(Strategy strategy, String hook, int index) throws Exception {
		return this.getControl(new VHook(strategy, hook), index);
	}

	/**
	 * Get a &lt;SELECT&gt; control from the current page.
	 *
	 * @param hook	 description of how to find the control
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VSelect getSelect(VHook hook) throws Exception {
		return new VSelect(this.voodoo, this, hook);
	}

	/**
	 * Get a &lt;SELECT&gt; control from the current page.
	 *
	 * @param strategy  method to use to search for the control
	 * @param hook		  string to find using the specified strategy
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VSelect getSelect(Strategy strategy, String hook) throws Exception {
		return this.getSelect(new VHook(strategy, hook));
	}

	private WebDriver getWebDriver(Type iType) throws Exception {
		WebDriver wd = null;
		switch (iType) {
		case FIREFOX:
			String profileName = this.config.getProperty("browser.firefox_profile", "default");
			String ffBinaryPath = this.config.getProperty("browser.firefox_binary", "/Applications/Firefox.app/Contents/MacOS/firefox");
			FirefoxProfile ffProfile = (new ProfilesIni())
					.getProfile(profileName);
			FirefoxBinary ffBinary = new FirefoxBinary(new File(ffBinaryPath));
			// if (System.getProperty("headless") != null) {
			// FirefoxBinary ffBinary = new FirefoxBinary();//new
			// File("//home//conrad//Applications//firefox-10//firefox"));
			// ffBinary.setEnvironmentProperty("DISPLAY", ":1");
			// webDriver = new FirefoxDriver(ffBinary, ffProfile);
			// }
			voodoo.log.info("Instantiating Firefox with profile name: "
					+ profileName + " and binary path: " + ffBinaryPath);
			wd = new FirefoxDriver(ffBinary, ffProfile);
			break;
		case CHROME:
			String workingDir = System.getProperty("user.dir");
			ChromeOptions chromeOptions = new ChromeOptions();
			String chromeDriverLogPath = this.config.getProperty("browser.chrome_driver_log_path");
			System.out.println("chromeDriverLogPath: " + chromeDriverLogPath);
			chromeOptions.addArguments("--log-path=" + chromeDriverLogPath);
			String chromeDriverPath = this.config.getPathProperty("browser.chrome_driver_path");
			System.out.println("chromeDriverPath: " + chromeDriverPath);
			// chromeOptions.setBinary(new File(chromeDriverPath));
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			voodoo.log.info("Instantiating Chrome with:\n    log path:"
					+ chromeDriverLogPath + "\n    driver path: "
					+ chromeDriverPath);
			wd = new ChromeDriver(chromeOptions);
			break;
		case IE:
			throw new Exception("Selenium: ie browser not yet supported.");
		case SAFARI:
			throw new Exception("Selenium: safari browser not yet supported.");
		default:
			throw new Exception("Selenium: browser type not recognized.");
		}
		long implicitWait = Long.parseLong(config.getProperty("perf.implicit_wait"));
		if (System.getProperty("headless") == null) {
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			wd.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
		}
		wd.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		return wd;
	}


	// ANDROID ROBOTIUM FUNCTIONALITY
	//	private AndroidInterface getAndroidControl() throws Exception {
	//		AndroidInterface vac = new AndroidInterface(this.props);
	//		return vac;
	//	}
	//	
	//	public void startApp() throws Exception {
	//		this.vac.startApp();
	//	}
	//	
	//	public void finishApp() throws Exception {
	//		this.vac.finishApp();
	//	}
	//	
	//	public void setApkPath(String aut, String messenger, String testrunner) {
	//		this.vac.setApkPath(aut, messenger, testrunner);
	//	}
	//	
	//	public void ignoreInstallAUT() throws Exception {
	//		this.vac.ignoreInstallAUT();
	//	}
	//	
	//	public void ignoreInstallMessenger() throws Exception {
	//		this.vac.ignoreInstallMessenger();
	//	}
	//	
	//	public void ignoreInstallRunner() throws Exception {
	//		this.vac.ignoreInstallRunner();
	//	}
	//	
	//	public VAControl getAControl() throws Exception{
	//		return new VAControl(this.voodoo, this);
	//	}


	//	/**
	//	 * @param selectElement
	//	 * @param actionElement
	//	 */
	//	public static void allOptionsAction(Select selectElement, WebElement actionElement) {
	//		List<WebElement> options = selectElement.getOptions();
	//		for (WebElement option : options) {
	//			selectElement.selectByVisibleText(option.getText());
	//			actionElement.click();
	//		}
	//	}
	//	
	//	
	//	/**
	//	 * @param selectElement
	//	 * @param actionOptionValues
	//	 * @param actionElement
	//	 * @throws Exception
	//	 */
	//	public static void optionAction(Select selectElement, Set<String> actionOptionValues, WebElement actionElement) throws Exception {
	//		List<WebElement> allOptions = selectElement.getOptions();
	//		HashSet<String> optionValues = new HashSet<String>();
	//		for(WebElement option : allOptions) {
	//			optionValues.add(option.getText());
	////			System.out.println("Adding to options set:" + option.getText());
	//		}
	//		if(optionValues.containsAll(actionOptionValues)) {
	//			for(String option : actionOptionValues) {
	//				selectElement.selectByVisibleText(option);
	//				actionElement.click();
	//			}
	//		} else throw new Exception("Specified select option unavailable...");
	//	}
	//	
	//	
	//
	//	/**
	//	 * @param element
	//	 * @return
	//	 */
	//	public static String webElementToString(WebElement element) {
	//		List<WebElement> childElements = element.findElements(By.xpath("*"));
	//		String s = element.getTagName() + ":" + element.getText() + " ";
	//		for(WebElement we : childElements) {
	//			s += we.getTagName() + ":" + we.getText() + " ";
	//		}
	//		return s;
	//	}
	//	
	//	
	//	/**
	//	 * @param nativeOptions
	//	 * @param queryOptionNames
	//	 * @return
	//	 */
	//	public static boolean optionValuesEqual(List<WebElement> nativeOptions, Set<String> queryOptionNames) {
	//		Set<String> nativeOptionNames = new HashSet<String>();
	//		for (WebElement option : nativeOptions) {
	//			nativeOptionNames.add(option.getText());
	//		}
	//		if (nativeOptionNames.containsAll(queryOptionNames) && queryOptionNames.containsAll(nativeOptionNames)) return true;
	//		else return false;
	//	}
}
