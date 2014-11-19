/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.automation.webdriver;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.sugarcrm.candybean.automation.AutomationInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Element;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.utilities.Utils.Pair;

/**
 * Drives the creation of multi-platform automation tests by providing a resourceful API
 * containing several helper methods to write automation tests. The {@link Candybean} configuration
 * will build a {@link WebDriverInterface} based on the platform specified in the configuration. An appropriate platform-specific
 * driver is instantiated for use to write tests.
 *
 */
public abstract class WebDriverInterface extends AutomationInterface {
	
	public WebDriver wd = null;
	private String baseUrl = "";
	private Stack<Pair<Integer, String>> windows = new Stack<Pair<Integer, String>>();
	
	protected WebDriverInterface(Type iType) throws CandybeanException {
		super(iType);
	}
	
	/**
	 * Handle the way this interface is to be started. 
	 * This routine must be implemented specific to the type of interface.
	 * 
	 * @throws CandybeanException
	 */
	@Override
	public void start() throws CandybeanException {
		long implicitWait = Long.parseLong(candybean.config.getValue("perf.implicit.wait.seconds"));
		wd.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		if (System.getProperty("headless") == null
				&& !(iType != Type.IOS)
				&& !(iType != Type.ANDROID)
				&& !System.getProperty("os.name").contains("mac")) {
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			wd.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
			this.windows.push(new Pair<Integer, String>(new Integer(0), this.wd.getWindowHandle()));
		}
	}
	
	/**
	 * @throws CandybeanException
	 */
	@Override
	public void stop() throws CandybeanException {
		this.windows.clear();
		this.wd.quit();
	}
	
	/**
	 * @throws CandybeanException
	 */
	public void restart() throws CandybeanException {
		this.stop();
		this.start();
	}
	
	/**
	 * Display a modal dialog box to the test user.
	 *
	 * @param message	 	String to display on the dialog box
	 */
	public void interact(String message) {
		logger.info("Interaction via popup dialog with message: " + message);
		JOptionPane.showInputDialog(message);
	}
	
	/**
	 * Takes a full screenshot and saves it to the given file.
	 * 
	 * @param file			The file to which a screenshot is saved
	 * @throws IOException 
	 * @throws AWTException 
	 */
	public void screenshot(File file) throws CandybeanException {
		logger.info("Taking screenshot; saving to file: " + file.toString());
		Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screenshot;
		try {
			screenshot = (new Robot()).createScreenCapture(screen);
		} catch (AWTException awte) {
			throw new CandybeanException(awte);
		}
		try {
			ImageIO.write(screenshot, "png", file);
		} catch (IOException ioe) {
			throw new CandybeanException(ioe);
		}
	}
	
	/**
	 * Executes any javascript command
	 * @param javascript The javascript code to execute
	 */
	public void executeJavascript(String javascript){
		logger.info("Executing explicit javascript");
		((JavascriptExecutor) this.wd).executeScript(javascript);
	}
	
	/**
	 * Refreshes the interface.  If refresh is undefined, it does nothing.
	 * 
	 */
	public void refresh() throws CandybeanException {
		logger.info("Refreshing the interface.");
		this.wd.navigate().refresh();
	}

	/**
	 * Load a URL in the browser window.
	 *
	 * @param url	the URL to be loaded by the browser
	 */
	public void go(String url) throws CandybeanException {
		logger.info("Going to URL and switching to window: " + url);
		this.wd.get(url);
	}
	
	/**
	 * Returns the current URL of the current window
	 * 
	 * @return		Returns the current window's URL as a String
	 */
	public String getURL() {
		String url = this.wd.getCurrentUrl();
		logger.info("Getting URL " + url);
		return url;
	}

	/**
	 * Navigates the interface backward.  If backward is undefined, it does nothing.
	 */
	public void backward() throws CandybeanException {
		logger.info("Navigating the interface backward.");
		this.wd.navigate().back();
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
	 */
	public boolean contains(String s, boolean caseSensitive) throws CandybeanException {
		logger.info("Searching if the interface contains the following string: " + s + " with case sensitivity: " + caseSensitive);
		if (!caseSensitive) s = s.toLowerCase();
		List<WebElement> wes = this.wd.findElements(By.xpath("//*[not(@visible='false')]"));
		for (WebElement we : wes) {
			String text = we.getText();
			if (!caseSensitive) text = text.toLowerCase();
			if (text.contains(s)) return true;
		}
		return false;
	}
	
	/**
	 * Switches focus to default content.
	 * 
	 */
	public void focusDefault() throws CandybeanException {
		logger.info("Focusing to default content.");
		this.wd.switchTo().defaultContent();
	}
	
	/**
	 * Switches focus to the IFrame identified by the given zero-based index
	 * 
	 * @param index		the serial, zero-based index of the iframe to focus
	 */
	public void focusFrame(int index) throws CandybeanException {
		logger.info("Focusing to frame by index: " + index);
		this.wd.switchTo().frame(index);
	}
	
	/**
	 * Switches focus to the IFrame identified by the given name or ID string
	 * 
	 * @param nameOrId	the name or ID identifying the targeted IFrame
	 */
	public void focusFrame(String nameOrId) throws CandybeanException {
		logger.info("Focusing to frame by name or ID: " + nameOrId);
		this.wd.switchTo().frame(nameOrId);
	}
	
	/**
	 * Switches focus to the IFrame identified by the given {@link Element}
	 * 
	 * @param wde		The element representing a focus-targeted IFrame
	 */
	public void focusFrame(WebDriverElement wde) throws CandybeanException {
		logger.info("Focusing to frame by element: " + wde.toString());
		this.wd.switchTo().frame(wde.we);
	}
	
	/**
	 * Close the current browser window.
	 */
	public void closeWindow() throws CandybeanException {
		logger.info("Closing window with handle: " + windows.peek());
		this.wd.close();
		this.windows.pop();
		logger.info("Refocusing to previous window with handle: " + windows.peek());
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
	 * @throws CandybeanException	if the specified window index is out of range
	 */
	public void focusWindow(int index) throws CandybeanException {
		if (index == windows.peek().x.intValue()) {
			logger.warning("No focus was made because the given index matched the current index: " + index);
		} else if (index < 0) {
			throw new CandybeanException("Given focus window index is out of bounds: " + index + "; current size: " + windows.size());
		} else {
			Set<String> windowHandlesSet = this.wd.getWindowHandles();
			String[] windowHandles = windowHandlesSet.toArray(new String[] {""});
			if (index >= windowHandles.length) {
				throw new CandybeanException("Given focus window index is out of bounds: " + index + "; current size: " + windows.size());
			} else {
				this.wd.switchTo().window(windowHandles[index]);
				windows.push(new Pair<Integer, String>(new Integer(index), this.wd.getWindowHandle()));
				logger.info("Focused by index: " + index + " to window: " + windows.peek());
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
	 * @throws CandybeanException	if the specified window cannot be found
	 */
	public void focusWindow(String titleOrUrl) throws CandybeanException {
		String curTitle = this.wd.getTitle();
		String curUrl = this.wd.getCurrentUrl();
		if (titleOrUrl.equals(curTitle) || titleOrUrl.equals(curUrl)) {
			logger.warning("No focus was made because the given string matched the current title or URL: " + titleOrUrl);
		} else {
			Set<String> windowHandlesSet = this.wd.getWindowHandles();
			String[] windowHandles = windowHandlesSet.toArray(new String[] {""});
			int i = 0;
			boolean windowFound = false;
			while (i < windowHandles.length && !windowFound) {
				WebDriver window = this.wd.switchTo().window(windowHandles[i]);
				if (window.getTitle().equals(titleOrUrl) || window.getCurrentUrl().equals(titleOrUrl)) {
					windows.push(new Pair<Integer, String>(new Integer(i), this.wd.getWindowHandle()));
					logger.info("Focused by title or URL: " + titleOrUrl + " to window: " + windows.peek());
					windowFound = true;
				}
				i++;
			}
			if (!windowFound) {
				this.wd.switchTo().window(windows.peek().y);
				throw new CandybeanException("The given focus window string matched no title or URL: " + titleOrUrl);
			}
		}	
	}
	
	/**
	 * Navigates the interface forward.  If forward is undefined, it does nothing.
	 */
	public void forward() throws CandybeanException {
		logger.info("Navigating the interface forward.");
		this.wd.navigate().forward();
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
	 */
	public void maximize() {
		logger.info("Maximizing window");
		this.wd.manage().window().maximize();
	}

	/**
	 * Get an element from the current page.
	 *
	 * @param hook	 description of how to find the control
	 * @throws CandybeanException 
	 */
	public WebDriverElement getWebDriverElement(Hook hook) throws CandybeanException {
		return this.getWebDriverElement(hook, 0);
	}

	/**
	 * Get an element from the current page by index.
	 *
	 * @param strategy		method to use to search for the control
	 * @param hookString	string to find using the specified strategy
	 * @param index
	 * @throws CandybeanException 
	 */
	public WebDriverElement getWebDriverElement(Strategy strategy, String hookString, int index) throws CandybeanException {
		return this.getWebDriverElement(new Hook(strategy, hookString), index);
	}

	/**
	 * Get an element from the current page.
	 *
	 * @param strategy  	method to use to search for the control
	 * @param hookString	string to find using the specified strategy
	 * @throws CandybeanException 
	 */
	public WebDriverElement getWebDriverElement(Strategy strategy, String hookString) throws CandybeanException {
		return this.getWebDriverElement(new Hook(strategy, hookString), 0);
	}
	
	/**
	 * Get an element from the current page by index.
	 *
	 * @param hook	 description of how to find the control
	 * @param index
	 * @throws CandybeanException 
	 */
	public WebDriverElement getWebDriverElement(Hook hook, int index) throws CandybeanException {
		return new WebDriverElement(hook, index, this.wd);
	}
	
	/**
	 * @param strategy The strategy used to search for the control
	 * @param hook The associated hook for the strategy
	 * @return The list of all controls that match the strategy and hook
	 * @throws CandybeanException 
	 */
	public List<WebDriverElement> getWebDriverElements(Strategy strategy, String hook) throws CandybeanException {
		return this.getWebDriverElements(new Hook(strategy, hook));
	}
	
	/**
	 * @param hook The associated hook for the strategy
	 * @return The list of all controls that match the strategy and hook
	 * @throws CandybeanException 
	 */
	public List<WebDriverElement> getWebDriverElements(Hook hook) throws CandybeanException {
		List<WebDriverElement> elements = new ArrayList<WebDriverElement>();
		List<WebElement> wes = this.wd.findElements(WebDriverElement.By(hook.getHookStrategy(), hook.getHookString()));
		for (WebElement we : wes)
			elements.add(new WebDriverElement(hook, 0, this.wd, we));
		return elements;
	}

	/**
	 * @param strategy	method to use to search for the control
	 * @param hook		string to find using the specified strategy
	 * @throws CandybeanException 
	 */
	public WebDriverSelector getSelect(Strategy strategy, String hook) throws CandybeanException {
		return this.getSelect(new Hook(strategy, hook));
	}
	
	/**
	 * @param hook	description of how to find the control
	 * @throws CandybeanException 
	 */
	public WebDriverSelector getSelect(Hook hook) throws CandybeanException {
		return new WebDriverSelector(hook, wd);
	}

	/**
	 * Click &quot;OK&quot; on a modal dialog box (usually referred to
	 * as a &quot;javascript dialog&quot;).
	 */
	public void acceptDialog() {
		try {
			logger.info("Accepting dialog.");
			this.wd.switchTo().alert().accept();
			this.waitForAlertDismissal();
		} catch(UnhandledAlertException uae) {
			logger.warning("Unhandled alert exception");
		}
	}
	
	/**
	 * Waits for an alert to be dismissed
	 * The use of a while loop is not recommended, use this method with caution
	 */
	private void waitForAlertDismissal() {
		long timeoutSec = Long.parseLong(candybean.config.getValue("perf.implicit.wait.seconds", "20"));
		logger.info("Waiting for alert to be dismissed, timeout in " + timeoutSec + " seconds.");
		long startTime = System.currentTimeMillis();
		while(true) {
			if(!isDialogVisible()) {
				logger.info("Wait for alert dismissal successful, alert was dismissed");
				break;
			} else if(waitForTimeout(startTime, timeoutSec)) {
				logger.info("Waiting for alert to be dismissed timed out, continuing");
				break;
			}
		}
	}
	
	/**
	 * Determines whether a timeout has occurred since the start time
	 * @param startTimeMs The start time in milliseconds
	 * @param timeoutSec The time in seconds for timeout
	 * @return
	 */
	private boolean waitForTimeout(long startTimeMs, long timeoutSec) {
		long timePassed = (System.currentTimeMillis() - startTimeMs) / 1000;
		return timePassed > timeoutSec;
	}

	/**
	 * Dismisses a modal dialog box (usually referred to
	 * as a &quot;javascript dialog&quot;).
	 *
	 */
	public void dismissDialog() {
		try {
			logger.info("Dismissing dialog.");
			this.wd.switchTo().alert().dismiss();
			this.waitForAlertDismissal();
		} catch(UnhandledAlertException uae) {
			logger.warning("Unhandled alert exception");
		}
	}

	/**
	 * Returns true if a modal dialog can be switched to 
	 * and switched back from; otherwise, returns false.
	 * 
	 * @return 	Boolean true only if a modal dialog can 
	 * be switched to, then switched back from.
	 */
	public boolean isDialogVisible() {
		try { 
			this.wd.switchTo().alert(); 
			logger.info("Dialog present?: true.");
			return true;
		} catch(UnhandledAlertException uae) {
			logger.info("(Unhandled alert in FF?) Dialog present?: true.  May have ignored dialog...");
			return true;
		} catch(NoAlertPresentException nape) {
			logger.info("Dialog present?: false.");
			return false;
		}
	}

	/**
	 * This saves the base URL that might be needed for various purposes
	 * (e.g. Query parameters to control application under test)
	 * @param baseUrl
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * This returns the base URL
	 * @return the base URL
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	public class SwipeableWebDriver extends RemoteWebDriver implements HasTouchScreen {
		private RemoteTouchScreen touch;

		public SwipeableWebDriver(URL remoteAddress,
				Capabilities desiredCapabilities) {
			super(remoteAddress, desiredCapabilities);
			touch = new RemoteTouchScreen(getExecuteMethod());
		}

		public TouchScreen getTouch() {
			return touch;
		}
	}
}
