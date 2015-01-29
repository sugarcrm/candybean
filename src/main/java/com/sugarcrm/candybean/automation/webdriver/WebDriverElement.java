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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.sugarcrm.candybean.automation.element.Element;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Location;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Represents an identifiable (via {@link By}) element or element on a page that
 * can be interacted with. A {@link WebDriverElement} object should be used to
 * automate tasks that require interaction with elements on a page.
 * 
 * @author Conrad Warmbold
 */
public class WebDriverElement extends Element {

	protected WebDriver wd;
	protected WebElement we;

	public WebDriverElement(Strategy strategy, String hookString, WebDriver wd) throws CandybeanException {
		this(new Hook(strategy, hookString), wd);
	}

	public WebDriverElement(Strategy strategy, String hookString, int index, WebDriver wd) throws CandybeanException {
		this(new Hook(strategy, hookString), index, wd);
	}

	public WebDriverElement(Hook hook, WebDriver wd) throws CandybeanException {
		this(hook, 0, wd);
	}

	public WebDriverElement(Hook hook, int index, WebDriver wd) throws CandybeanException {
		super(hook, index);
		this.wd = wd;
		List<WebElement> wes = this.wd.findElements(Hook.getBy(hook));
		if (wes.size() == 0) {
			throw new CandybeanException("Control not found; zero web elements returned.");
		}
		this.we = wes.get(index);
	}

	public WebDriverElement(Hook hook, int index, WebDriver wd, WebElement we) throws CandybeanException {
		super(hook, index);
		this.wd = wd;
		this.we = we;
	}

	/**
	 * Get the value of an attribute of the element.
	 * 
	 * @param attribute
	 *            name of the attribute to get
	 * @return the value of the specified attribute
	 * @throws CandybeanException
	 *             if the attribute does not exist or the element cannot be
	 *             found
	 */
	public String getAttribute(String attribute) throws CandybeanException {
		logger.info("Getting attribute: " + attribute
				+ " for element: " + this.toString());
		String value = we.getAttribute(attribute);
		if (value == null)
			throw new CandybeanException("Attribute does not exist.");
		else
			return value;
	}

	public String getSource() throws CandybeanException {
		logger.info("Getting source for element: " + this.toString());
		return (String) ((JavascriptExecutor) this.wd)
				.executeScript("return arguments[0].innerHTML;", this.we);
	}

	/**
	 * Get the visible text of this element. If the element is a button, the
	 * value is returned.
	 * 
	 * @return the visible text of this element
	 */
	public String getText() throws CandybeanException {
		logger.info("Getting text for element: " + this.toString());
		String type = this.we.getAttribute("type");
		if (type != null
				&& (type.equalsIgnoreCase("button") || type
						.equalsIgnoreCase("input"))) {
			return this.we.getAttribute("value");
		}
		return this.we.getText();
	}

	/**
	 * Click the element.
	 */
	public void click() throws CandybeanException {
		logger.info("Clicking on element: " + this.toString());
		we.click();
	}

	/**
	 * Returns true if the element visibly contains the given string in any
	 * non-visible=false element.
	 * 
	 * @param s
	 *            The target string searched for in the interface
	 * @param caseSensitive
	 *            Whether or not the search is case sensitive
	 * @return Returns true if the interface visibly contains the given string
	 */
	public boolean contains(String s, boolean caseSensitive) {
		logger.info("Searching if the element contains the following string: '"
				+ s + "' with case sensitivity: " + caseSensitive);
		String lowercase = s;
		if (!caseSensitive) {
			lowercase = s.toLowerCase();
		}
		List<WebElement> wes = this.we.findElements(By
				.xpath(".//*[not(@visible='false')]"));
		wes.add(this.we);
		for (WebElement webElement : wes) {
			String text = webElement.getText();
			if (!caseSensitive) {
				text = text.toLowerCase();
			}
			if (text.contains(lowercase)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Double-click the element.
	 */
	public void doubleClick() throws CandybeanException {
		logger.info("Double-clicking on element: " + this.toString());
		Actions action = new Actions(this.wd);
		action.doubleClick(we).perform();
	}

	/**
	 * Drag this element and drop onto another element.
	 * 
	 * @param dropControl
	 *            target of the drag and drop
	 */
	public void dragNDrop(WebDriverElement dropControl)
			throws CandybeanException {
		logger.info("Dragging element: " + this.toString()
				+ " to element: " + dropControl.toString());
		Actions action = new Actions(this.wd);
		action.dragAndDrop(this.we, dropControl.we).build().perform();
	}

	 /**
	 * Get the select child of the given the hook of a parent element
	 * 
	 * @param hook
	 * 				The hook of the parent element
	 * @param index
	 *				The index of where the child locates
	 * @return Return a WebDriverSelector that is the child of the parent element
	 * @throws CandybeanException
	 */
	public Element getSelect(Hook hook, int index) throws CandybeanException {
		logger.info("Getting select: " + hook.toString() 
				+ " from element: " + this.toString() + " with index: " + index);
		WebElement childWe = this.we.findElements(Hook.getBy(hook)).get(index);
		return new WebDriverSelector(hook, index, this.wd, childWe); 
	}

	 /**
	 * Get the element child of the given the hook of a parent element
	 * 
	 * @param hook
	 *				The hook of the parent element
	 * @param index
	 *				The index of where the child locates
	 * @return Return a WebDriverSelector that is the child of the parent element
	 * @throws CandybeanException
	 */
	@Override
	public Element getElement(Hook hook, int index) throws CandybeanException {
		logger.info("Getting element: " + hook.toString()
				+ " from element: " + this.toString() + " with index: " + index);
		WebElement childWe = this.we.findElements(Hook.getBy(hook)).get(index);
		return new WebDriverElement(hook, index, this.wd, childWe);
	}

	/**
	 * Executes a javascript command against this element.
	 *
	 * @param	javascript	The javascript code to execute
	 * @param	args	The arguments to pass.  Note that indices of the arguments passed will
	 *                      be incremented by 1 because this element will be used as the first arg
	 * @return	an object representation of the return value of the executed Javascript.  Depending
	 * 			on the type returned from the Javascript, this may be of type WebElement, Double,
	 * 			Long, Boolean, String, List&lt;Object&gt;, or null.
	 */
	@Override
	public Object executeJavascript(String javascript, Object... args) {
		logger.info("Executing explicit javascript against " + toString());
		return ((JavascriptExecutor) wd).executeScript(javascript, we, args);
	}

    /**
	 * Executes an asynchronous javascript command against this element. The script executed with
     * this method must explicitly signal they are finished by invoking the provided callback. This
     * callback is always injected into the executed function as the last argument
	 *
	 * @param	javascript	The javascript code to execute
	 * @param	args	The arguments to pass.  Note that indices of the arguments passed will
	 *                      be incremented by 1 because this element will be used as the first arg
     * @return	an object representation of the first argument passed to the callback
     *          of the executed Javascript.  Depending on the type returned from the
     *          Javascript, this may be of type WebElement, Long, Boolean, String,
     *          List&lt;Object&gt;, or null.
	 */
    @Override
	public Object executeAsyncJavascript(String javascript, Object... args) {
		logger.info("Executing explicit async javascript against " + toString());
		return ((JavascriptExecutor) wd).executeAsyncScript(javascript, we, args);
	}

	/**
	 * @return an int containing the element's width.
	 */
	@Override
	public int getWidth() {
		return we.getSize().getWidth();
	}

	/**
	 * @return an int containing the element's height.
	 */
	@Override
	public int getHeight() {
		return we.getSize().getHeight();
	}

	/**
	 * Returns the value of the specified CSS property, or null if it does not exist.
	 * <p/>
	 * Note that shorthand CSS properties (e.g. background, font, border, border-top, margin,
	 * margin-top, padding, padding-top, list-style, outline, pause, cue) are not returned, in
	 * accordance with the DOM CSS2 specification - you should directly access the longhand
	 * properties (e.g. background-color) to access the desired values.
	 *
	 * @return String    the value of the specified property, or null if it does not exist.
	 */
	@Override
	public String getCssValue(String propertyName) {
		return we.getCssValue(propertyName);
	}

	/**
	 * Hover over this element.
	 */
	public void hover() throws CandybeanException {
		logger.info("Hovering over element: " + this.toString());
		Actions action = new Actions(this.wd);
		action.moveToElement(this.we).perform();
	}

	/**
	 * Returns true if and only if the element is displayed {@link
	 * "http://selenium.googlecode.com/svn/trunk/docs/api/java/index.html" according
	 * to Selenium}
	 */
	public boolean isDisplayed() throws CandybeanException {
		logger.info("Determining if element is visible: " + this.toString());
		return we.isDisplayed();
	}

	/**
	 * Right-click this element.
	 */
	public void rightClick() throws CandybeanException {
		logger.info("Right-clicking element: " + this.toString());
		Actions action = new Actions(this.wd);
		action.contextClick(this.we).perform();
	}

	/**
	 * Scroll this element to the top of the window
	 */
	public void scroll() throws CandybeanException {
		scroll(Location.TOP);
	}
	

	/**
	 * Scroll the element to the specified location in the window.
	 */
	public void scroll(Location loc) throws CandybeanException {
		int y = this.we.getLocation().getY();
		int height = this.we.getSize().getHeight();
		switch (loc) {
		case TOP:
			logger.info("Scrolling element to the top of the viewport: " + this.toString());
			((JavascriptExecutor) this.wd).executeScript("window.scrollTo(0," + y + ");");
			break;
		case BOTTOM:
			logger.info("Scrolling element to the bottom of the viewport: " + this.toString());
			((JavascriptExecutor) this.wd).executeScript("window.scrollTo(0," + y + "- window.innerHeight + " + height + ");");
			break;
		case MIDDLE:
			logger.info("Scrolling element to the middle of the viewport: " + this.toString());
			((JavascriptExecutor) this.wd).executeScript("window.scrollTo(0," + y + "- (window.innerHeight/2) + " + height/2 + ");");
			break;
		default:
			break;
		}
	}

	/**
	 * Clears the element and sends a string to it.
	 * 
	 * @param input
	 *            string to send
	 */
	public void sendString(String input) throws CandybeanException {
		logger.info("Sending string: " + input + " to element: "
				+ this.toString());
		this.we.clear();

		// Re-find the element to avoid the stale element problem.
		// Re-finding this *still* causes problems in getControl.getControl
		// situations; a
		// universal solution should be found to resolve this inconsistency
		// since this
		// is the only method that does it and it violates the general
		// architecture
		this.we = this.wd.findElements(Hook.getBy(this.hook)).get(this.index);
		this.we.sendKeys(input);
	}

	/**
	 * Send a string to this element.
	 * 
	 * @param input
	 *            string to send
	 * @param append
	 *            if append is true, the element will be cleared first
	 * @throws CandybeanException
	 */
	public void sendString(String input, boolean append)
			throws CandybeanException {
		logger.info("Clear first?: " + append + "; sending string: " + input
				+ " to element: " + this.toString());
		if (!append)
			this.sendString(input);
		else {
			// Re-find the element to avoid the stale element problem.
			this.we = this.wd.findElements(Hook.getBy(this.hook)).get(this.index);
			this.we.sendKeys(input);
		}
	}

	/**
	 * Get the By using the hook in this WebDriverElement
	 * @return
	 * @throws CandybeanException
	 */
	public By getBy() throws CandybeanException {
		return Hook.getBy(this.hook);
	}
}
