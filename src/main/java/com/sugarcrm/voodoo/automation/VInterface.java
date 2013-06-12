package com.sugarcrm.voodoo.automation;

import java.awt.Toolkit;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.net.URL;

import javax.swing.JOptionPane;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.HasTouchScreen;
import org.openqa.selenium.TouchScreen;
import org.openqa.selenium.Capabilities;


import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VSelect;
import com.sugarcrm.voodoo.configuration.Configuration;
import com.sugarcrm.voodoo.utilities.Utils.Pair;
import org.openqa.selenium.remote.DesiredCapabilities;

public class VInterface {

	public enum Type { FIREFOX, IE, CHROME, SAFARI, ANDROID, IOS; }

	private final Voodoo voodoo;
	private final Configuration config;

	public WebDriver wd = null;
	private Type iType = null;
//	public final AndroidInterface vac; //vac as in voodoo android control
	private Stack<Pair<Integer, String>> windows = new Stack<Pair<Integer, String>>();

	/**
	 * Instantiate VInterface; Deprecated in favor of the type-less
	 * constructor (where type is given during start or from config)
	 *
	 * @param voodoo  {@link Voodoo} object
	 * @param config   {@link Configuration} for this test run
	 * @param iType   {@link IInterface.Type} of interface to run
	 * @throws Exception
	 */
	@Deprecated
	public VInterface(Voodoo voodoo, Configuration config, Type iType)
			throws Exception {
		this.voodoo = voodoo;
		this.config = config;
		this.iType = iType;
	}
	
	/**
	 * Instantiate VInterface
	 *
	 * @param voodoo  {@link Voodoo} object
	 * @param config   {@link Configuration} for this test run
	 * @throws Exception
	 */
	public VInterface(Voodoo voodoo, Configuration config)
			throws Exception {
		this.voodoo = voodoo;
		this.config = config;
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
	 * Launch and initialize an interface with type defined
	 * during instantiation.
	 * 
	 * @throws Exception		if type is undefined during instantiation
	 */
	public void start() throws Exception {
		this.iType = this.parseInterfaceType(this.config.getProperty("automation.interface", "chrome"));
		this.start(this.iType);
	}

	/**
	 * Launch and initialize an interface.
	 * 
	 * @param iType			The interface type to start automation 
	 * @throws Exception
	 */
	public void start(Type iType) throws Exception {
		voodoo.log.info("Starting automation interface with type: " + iType);
//		if (iType == Type.ANDROID) {
//			this.vac = this.getAndroidControl();
//			this.wd = null;
//		}
//		else {
//			this.wd = this.getWebDriver(iType);
//			this.vac = null;
//			this.start();
//		}
		if (this.wd != null) throw new Exception("Automation interface already started with type: " + this.iType);
		this.iType = iType;
		this.wd = this.getWebDriver(iType);
		this.windows.push(new Pair<Integer, String>(new Integer(0), this.wd.getWindowHandle()));
	}

	/**
	 * Close the interface and perform final cleanup.
	 *
	 * @throws Exception
	 */
	public void stop() throws Exception {
		voodoo.log.info("Stopping automation interface with type: " + this.iType);
		this.windows.clear();
		this.iType = null;
		if (this.wd != null) {
			this.wd.quit();
			this.wd = null;
		} else voodoo.log.warning("Automation interface already stopped.");
	}
	
	/**
	 * Restarts the interface with the current interface type
	 * 
	 * @throws Exception
	 */
	public void restart() throws Exception {
		voodoo.log.info("Restarting automation interface with type: " + this.iType);
		if (this.wd == null) throw new Exception("Automation interface not yet started; cannot restart.");
		Type type = this.iType;
		this.stop();
		this.start(type);
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
//		this.wd.switchTo().window(this.wd.getWindowHandle());
	}
	
	/**
	 * Returns the current URL of the current window
	 * 
	 * @return		Returns the current window's URL as a String
	 * @throws Exception
	 */
	public String getURL() throws Exception {
		String url = this.wd.getCurrentUrl();
		voodoo.log.info("Getting URL " + url);
		return url;
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
	 * Returns true if the interface visibly contains the 
	 * given string in any non-visible=false element.
	 * 
	 * @param s					The target string searched 
	 * for in the interface		
	 * @param caseSensitive		Whether or not the search
	 * is case sensitive		
	 * @return		Returns true if the interface visibly 
	 * contains the given string
	 * @throws Exception
	 */
	public boolean contains(String s, boolean caseSensitive) throws Exception {
		voodoo.log.info("Searching if the interface contains the following string: " + s + " with case sensitivity: " + caseSensitive);
		if (!caseSensitive) s = s.toLowerCase();
		List<WebElement> wes = this.wd.findElements(By.xpath("//*[not(@visible='false')]"));
		for (WebElement we : wes) {
			String text = we.getText();
			if (!caseSensitive) text = text.toLowerCase();
//			System.out.println("text: " + text);
			if (text.contains(s)) return true;
		}
		return false;
	}
	
	/**
	 * Switches focus to default content.
	 * 
	 * @throws Exception
	 */
	public void focusDefault() throws Exception {
		voodoo.log.info("Focusing to default content.");
		this.wd.switchTo().defaultContent();
	}
	
	/**
	 * Switches focus to the IFrame identified by the given zero-based index
	 * 
	 * @param index		the serial, zero-based index of the iframe to focus
	 * @throws Exception
	 */
	public void focusFrame(int index) throws Exception {
		voodoo.log.info("Focusing to frame by index: " + index);
		this.wd.switchTo().frame(index);
	}
	
	/**
	 * Switches focus to the IFrame identified by the given name or ID string
	 * 
	 * @param nameOrId	the name or ID identifying the targeted IFrame
	 * @throws Exception
	 */
	public void focusFrame(String nameOrId) throws Exception {
		voodoo.log.info("Focusing to frame by name or ID: " + nameOrId);
		this.wd.switchTo().frame(nameOrId);
	}
	
	/**
	 * Switches focus to the IFrame identified by the given {@link VControl}
	 * 
	 * @param control		The VControl representing a focus-targeted IFrame
	 * @throws Exception
	 */
	public void focusFrame(VControl control) throws Exception {
		voodoo.log.info("Focusing to frame by control: " + control.toString());
		this.wd.switchTo().frame(control.we);
	}
	
	/**
	 * Close the current browser window.
	 *
	 * @throws Exception	  <i>not thrown</i>
	 */
	public void closeWindow() throws Exception {
		voodoo.log.info("Closing window with handle: " + windows.peek());
		this.wd.close();
		this.windows.pop();
		voodoo.log.info("Refocusing to previous window with handle: " + windows.peek());
		this.wd.switchTo().window(windows.peek().y);
	}

	/**
	 * Focus a browser window by its index if it is not the current index.
	 *
	 * <p>The order of browser windows is somewhat arbitrary and not
	 * guaranteed, although window creation time ordering seems to be
	 * the most common.</p>
	 *
	 * @param index  		the window index
	 * @throws Exception	if the specified window index is out of range
	 */
	public void focusWindow(int index) throws Exception {
		if (index == windows.peek().x.intValue()) {
			voodoo.log.warning("No focus was made because the given index matched the current index: " + index);
		} else if (index < 0) {
			throw new Exception("Given focus window index is out of bounds: " + index + "; current size: " + windows.size());
		} else {
			Set<String> windowHandlesSet = this.wd.getWindowHandles();
			String[] windowHandles = windowHandlesSet.toArray(new String[] {""});
			if (index >= windowHandles.length) {
				throw new Exception("Given focus window index is out of bounds: " + index + "; current size: " + windows.size());
			} else {
				this.wd.switchTo().window(windowHandles[index]);
				windows.push(new Pair<Integer, String>(new Integer(index), this.wd.getWindowHandle()));
				voodoo.log.info("Focused by index: " + index + " to window: " + windows.peek());
			}
		}
	}

	/**
	 * Focus a browser window by its window title or URL if it does not
	 * match the current title or URL.
	 *
	 * <p>If more than one window has the same title or URL, the first
	 * encountered is the one that is focused.</p>
	 *
	 * @param titleOrUrl  	the exact window title or URL to be matched
	 * @throws Exception	if the specified window cannot be found
	 */
	public void focusWindow(String titleOrUrl) throws Exception {
		String curTitle = this.wd.getTitle();
		String curUrl = this.wd.getCurrentUrl();
		if (titleOrUrl.equals(curTitle) || titleOrUrl.equals(curUrl)) {
			voodoo.log.warning("No focus was made because the given string matched the current title or URL: " + titleOrUrl);
		} else {
			Set<String> windowHandlesSet = this.wd.getWindowHandles();
			String[] windowHandles = windowHandlesSet.toArray(new String[] {""});
			int i = 0;
			boolean windowFound = false;
			while (i < windowHandles.length && !windowFound) {
				WebDriver window = this.wd.switchTo().window(windowHandles[i]);
				if (window.getTitle().equals(titleOrUrl) || window.getCurrentUrl().equals(titleOrUrl)) {
					windows.push(new Pair<Integer, String>(new Integer(i), this.wd.getWindowHandle()));
					voodoo.log.info("Focused by title or URL: " + titleOrUrl + " to window: " + windows.peek());
					windowFound = true;
				}
				i++;
			}
			if (!windowFound) {
				this.wd.switchTo().window(windows.peek().y);
				throw new Exception("The given focus window string matched no title or URL: " + titleOrUrl);
			}
		}	
	}
	
	
	/**
	 * Returns a string with the contents of the windows data structure.
	 * 
	 * @return	A string representation of all focused windows, with 
	 * chronological index of focus and handle
	 */
	public String getWindowsString() {
		String s = "Reverse stack:\n";
		Iterator<Pair<Integer, String>> winIter = windows.iterator();
		while (winIter.hasNext()) {
			s += winIter.next() + "\n";
		}
		return s;
	}

	/**
	 * Maximize the browser window.
	 *
	 * @throws Exception	 <i>not thrown</i>
	 */
	public void maximize() {
		voodoo.log.info("Maximizing window");
//		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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
	
	private VInterface.Type parseInterfaceType(String iTypeString) throws Exception {
		VInterface.Type iType = null;
		for (VInterface.Type iTypeIter : VInterface.Type.values()) {
			if (iTypeIter.name().equalsIgnoreCase(iTypeString)) {
				iType = iTypeIter;
				break;
			}
		}
		if (iType == Type.ANDROID) throw new Exception("Android interface type not yet implemented.");
		if (iType == Type.IOS) throw new Exception("iOS interface type not yet implemented.");
		return iType;
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
        case ANDROID:
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
            capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
            capabilities.setCapability("app", "https://s3.amazonaws.com/voodoo2/ApiDemos-debug.apk");
            wd = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            break;
        case IOS:
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.BROWSER_NAME, "iOS");
            capabilities.setCapability(CapabilityType.VERSION, "6.0");
            capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
            capabilities.setCapability("app", "https://s3.amazonaws.com/voodoo2/TestApp.zip");
            wd = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            break;
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

    public class SwipeableWebDriver extends RemoteWebDriver implements HasTouchScreen {
        private RemoteTouchScreen touch;

        public SwipeableWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
            super(remoteAddress, desiredCapabilities);
            touch = new RemoteTouchScreen(getExecuteMethod());
        }

        public TouchScreen getTouch() {
            return touch;
        }
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
