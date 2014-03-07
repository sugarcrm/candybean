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
import com.sugarcrm.candybean.automation.element.Pause;
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

	public Pause pause;

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
		List<WebElement> wes = this.wd.findElements(WebDriverElement.By(hook));
		if (wes.size() == 0) {
			throw new CandybeanException("Control not found; zero web elements returned.");
		}
		this.we = wes.get(index);
		this.pause = new WebDriverPause(this);
	}

	public WebDriverElement(Hook hook, int index, WebDriver wd, WebElement we) throws CandybeanException {
		super(hook, index);
		this.wd = wd;
		this.we = we;
		this.pause = new WebDriverPause(this);
	}

	/**
	 * Get the value of an attribute of the control.
	 * 
	 * @param attribute
	 *            name of the attribute to get
	 * @return the value of the specified attribute
	 * @throws CandybeanException
	 *             if the attribute does not exist or the element cannot be
	 *             found
	 */
	public String getAttribute(String attribute) throws CandybeanException {
		logger.info("Selenium: getting attribute: " + attribute
				+ " for control: " + this.toString());
		String value = we.getAttribute(attribute);
		if (value == null)
			throw new CandybeanException("Selenium: attribute does not exist.");
		else
			return value;
	}

	public String getSource() throws CandybeanException {
		logger.info("Selenium: getting source for control: " + this.toString());
		return (String) ((JavascriptExecutor) this.wd)
				.executeScript("return arguments[0].innerHTML;", this.we);
	}

	/**
	 * Get the visible text of this control. If the control is a button, the
	 * value is returned.
	 * 
	 * @return the visible text of this element
	 */
	public String getText() throws CandybeanException {
		logger.info("Selenium: getting text for control: " + this.toString());
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
		logger.info("Selenium: clicking on control: " + this.toString());
		we.click();
	}

	/**
	 * Returns true if the control visibly contains the given string in any
	 * non-visible=false element.
	 * 
	 * @param s
	 *            The target string searched for in the interface
	 * @param caseSensitive
	 *            Whether or not the search is case sensitive
	 * @return Returns true if the interface visibly contains the given string
	 */
	public boolean contains(String s, boolean caseSensitive) {
		logger.info("Searching if the control contains the following string: '"
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
		logger.info("Selenium: double-clicking on control: " + this.toString());
		Actions action = new Actions(this.wd);
		action.doubleClick(we).perform();
	}

	/**
	 * Drag this control and drop onto another control.
	 * 
	 * @param dropControl
	 *            target of the drag and drop
	 */
	public void dragNDrop(WebDriverElement dropControl)
			throws CandybeanException {
		logger.info("Selenium: dragging control: " + this.toString()
				+ " to control: " + dropControl.toString());
		Actions action = new Actions(this.wd);
		action.dragAndDrop(this.we, dropControl.we).build().perform();
	}

	@Override
	public Element getElement(Hook hook, int index) throws CandybeanException {
		logger.info("Selenium: getting control: " + hook.toString()
				+ " from control: " + this.toString() + " with index: " + index);
		WebElement childWe = this.we.findElements(WebDriverElement.By(hook))
				.get(index);
		return new WebDriverElement(hook, index, this.wd, childWe);
	}

	/**
	 * Hover over this control.
	 */
	public void hover() throws CandybeanException {
		logger.info("Selenium: hovering over control: " + this.toString());
		Actions action = new Actions(this.wd);
		action.moveToElement(this.we).perform();
	}

	/**
	 * Returns true if and only if the control is displayed {@link http
	 * ://selenium.googlecode.com/svn/trunk/docs/api/java/index.html according
	 * to Selenium}
	 */
	public boolean isDisplayed() throws CandybeanException {
		logger.info("Selenium: determining if control is visible: "
				+ this.toString());
		return we.isDisplayed();
	}

	/**
	 * Right-click this control.
	 */
	public void rightClick() throws CandybeanException {
		logger.info("Selenium: right-clicking control: " + this.toString());
		Actions action = new Actions(this.wd);
		action.contextClick(this.we).perform();
	}

	/**
	 * Scroll the browser window to this control.
	 */
	public void scroll() throws CandybeanException {
		logger.info("Selenium: scrolling to control: " + this.toString());
		int y = this.we.getLocation().y;
		((JavascriptExecutor) this.wd).executeScript("window.scrollBy(0," + y + ");");
	}

	/**
	 * Clears the control and sends a string to it.
	 * 
	 * @param input
	 *            string to send
	 */
	public void sendString(String input) throws CandybeanException {
		logger.info("Selenium: sending string: " + input + " to control: "
				+ this.toString());
		this.we.clear();

		// Re-find the element to avoid the stale element problem.
		// Re-finding this *still* causes problems in getControl.getControl
		// situations; a
		// universal solution should be found to resolve this inconsistency
		// since this
		// is the only method that does it and it violates the general
		// architecture
		this.we = this.wd.findElements(WebDriverElement.By(this.hook)).get(this.index);
		this.we.sendKeys(input);
	}

	/**
	 * Send a string to this control.
	 * 
	 * @param input
	 *            string to send
	 * @param append
	 *            if append is true, the control will be cleared first
	 * @throws CandybeanException
	 */
	public void sendString(String input, boolean append)
			throws CandybeanException {
		logger.info("Clear first?: " + append + "; sending string: " + input
				+ " to control: " + this.toString());
		if (!append)
			this.sendString(input);
		else {
			// Re-find the element to avoid the stale element problem.
			this.we = this.wd.findElements(WebDriverElement.By(this.hook)).get(this.index);
			this.we.sendKeys(input);
		}
	}

	public By getBy() throws CandybeanException {
		return WebDriverElement.By(this.hook);
	}

	public static By By(Hook hook) throws CandybeanException {
		return WebDriverElement.By(hook.getHookStrategy(),
				hook.getHookString());
	}

	public static By By(Strategy strategy, String hookString)
			throws CandybeanException {
		switch (strategy) {
		case CSS:
			return By.cssSelector(hookString);
		case XPATH:
			return By.xpath(hookString);
		case ID:
			return By.id(hookString);
		case NAME:
			return By.name(hookString);
		case LINK:
			return By.linkText(hookString);
		case PLINK:
			return By.partialLinkText(hookString);
		case CLASS:
			return By.className(hookString);
		case TAG:
			return By.tagName(hookString);
		default:
			throw new CandybeanException("Strategy type not recognized.");
		}
	}
}
