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
package com.sugarcrm.candybean.automation.control;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.utilities.exception.CandybeanException;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Represents an identifiable (via {@link By}) element or widget on a page that can be interacted with.
 * A {@link VControl} object should be used to automate tasks that require interaction with elements on a page.
 * @author cwarmbold
 */
public class VControl {
	
	
	protected final Candybean voodoo;
	protected final VInterface iface;
	protected final VHook hook;
	protected int index;
	public WebElement we;
	public Pause pause;
	
	public VControl(Candybean voodoo, VInterface iface, Strategy strategy, String hook) throws CandybeanException {
		this(voodoo, iface, new VHook(strategy, hook));
	}
	
	public VControl(Candybean voodoo, VInterface iface, Strategy strategy, String hook, int index) throws CandybeanException {
		this(voodoo, iface, new VHook(strategy, hook), index);
	}
	
	public VControl(Candybean voodoo, VInterface iface, VHook hook) throws CandybeanException {
		this(voodoo, iface, hook, 0);
	}
	
	public VControl(Candybean voodoo, VInterface iface, VHook hook, int index) throws CandybeanException {
			this.voodoo = voodoo;
			this.iface = iface;
			List<WebElement> wes = iface.wd.findElements(VControl.makeBy(hook));
			if (wes.size() == 0) {
				throw new CandybeanException("Control not found; zero web elements returned.");
			}
			this.we = wes.get(index);
			this.pause = new Pause(this);
			this.hook = hook;
			this.index = index;
	}
	
	public VControl(Candybean voodoo, VInterface iface, VHook hook, WebElement we) {
		this.voodoo = voodoo;
		this.iface = iface;
		this.hook = hook;
		this.we = we;
	}
	
	/**
	 * Get the value of an attribute of the control.
	 *
	 * @param attribute	name of the attribute to get
	 * @return the value of the specified attribute
	 * @throws CandybeanException if the attribute does not exist or the element
	 *							 cannot be found
	 */
	public String getAttribute(String attribute) throws CandybeanException {
		voodoo.log.info("Selenium: getting attribute: " + attribute	+ " for control: " + this.toString());
		String value = we.getAttribute(attribute);
		if (value == null) {
			throw new CandybeanException("Selenium: attribute " + value + "does not exist for element " + we.toString());
		}else { 
			return value;
		}
	}

	public String getSource() {
		voodoo.log.info("Selenium: getting source for control: " + this.toString());
		return (String)((JavascriptExecutor)iface.wd).executeScript("return arguments[0].innerHTML;", this.we);
	}

	/**
	 * Get the visible text of this control.  If the control is a button, the value is returned.
	 *
	 * @return the visible text of this element
	 */
	public String getText() {
		voodoo.log.info("Selenium: getting text for control: " + this.toString());
//		System.out.println("tagname: " + we.getTagName() + ", type attribute: " + we.getAttribute("type"));
		String type = we.getAttribute("type");
		if (type != null) {
			if (type.equalsIgnoreCase("button") || type.equalsIgnoreCase("input")) {
				return we.getAttribute("value");
			}
		}
		return we.getText();
	}

	/**
	 * Click the element.
	 */
	public void click() {
		voodoo.log.info("Selenium: clicking on control: " + this.toString());
		we.click();
	}
	
	/**
	 * Returns true if the control visibly contains the 
	 * given string in any non-visible=false element.
	 *
	 * @param s					The target string searched 
	 * for in the interface		
	 * @param caseSensitive		Whether or not the search
	 * is case sensitive		
	 * @return		Returns true if the interface visibly 
	 * contains the given string
	 */
	public boolean contains(String s, boolean caseSensitive) {
		voodoo.log.info("Searching if the control contains the following string: '" + s + "' with case sensitivity: " + caseSensitive);
		if (!caseSensitive) {
			s = s.toLowerCase();
		}
		List<WebElement> wes = this.we.findElements(By.xpath(".//*[not(@visible='false')]"));
		wes.add(this.we);
		for (WebElement we : wes) {
			String text = we.getText();
			if (!caseSensitive) {
				text = text.toLowerCase();
			}
//			System.out.println("text: " + text);
			if (text.contains(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Double-click the element.
	 */
	public void doubleClick() {
		voodoo.log.info("Selenium: double-clicking on control: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.doubleClick(we).perform();
	}

	/**
	 * Drag this control and drop onto another control.
	 *
	 * @param dropControl  target of the drag and drop
	 */
	public void dragNDrop(VControl dropControl)	{
		voodoo.log.info("Selenium: dragging control: " + this.toString() + " to control: " + dropControl.toString());
		Actions action = new Actions(this.iface.wd);
		action.dragAndDrop(this.we, dropControl.we).build().perform();
	}

	public VControl getControl(VHook hook) {
		return this.getControl(hook, 0);
	}

	public VControl getControl(VHook hook, int index) {
		voodoo.log.info("Selenium: getting control: " + hook.toString() + " from control: " + this.toString() + " with index: " + index);
		WebElement childWe = this.we.findElements(VControl.makeBy(hook)).get(index);
		return new VControl(this.voodoo, this.iface, hook, childWe);
	}

	public VControl getControl(Strategy strategy, String hookString) {
		return this.getControl(strategy, hookString, 0);
	}

	public VControl getControl(Strategy strategy, String hookString, int index) {
		return this.getControl(new VHook(strategy, hookString), index);
	}

//	@Deprecated
//	public void pause(String attribute, String value, int timeout) throws Exception {
//		voodoo.log.info("Selenium: waiting for " + timeout + "ms for control: " + this.toString()
//				+ " to have attribute: " + attribute + " to have value: " + value);
//		final WebElement we = this.getWebElement(this.getBy(this.hook), 0);
//		final String vAttribute = attribute;
//		final String vValue = value;
//		WebDriverWait wait = new WebDriverWait(this.iface.wd, timeout);
//		wait.until(new Function<WebDriver, Boolean>() {
//			public Boolean apply(WebDriver driver) {
//				return we.getAttribute(vAttribute).contains(vValue);
//			}
//		});
//	}
	
	/**
	 * Hover over this control.
	 *
	 */
	public void hover() {
		voodoo.log.info("Selenium: hovering over control: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.moveToElement(this.we).perform();
	}
	
	/**
	 * Returns true if and only if the control is displayed
	 * {@link http://selenium.googlecode.com/svn/trunk/docs/api/java/index.html according to Selenium}
	 *
	 */
	public boolean isDisplayed() {
		voodoo.log.info("Selenium: determining if control is visible: " + this.toString());
		return we.isDisplayed();
	}

	/**
	 * Right-click this control.
	 *
	 */
	public void rightClick() {
		voodoo.log.info("Selenium: right-clicking control: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.contextClick(this.we).perform();
	}

	/**
	 * Scroll the browser window to this control.
	 */
	public void scroll() {
		voodoo.log.info("Selenium: scrolling to control: " + this.toString());
		int y = this.we.getLocation().y;
		((JavascriptExecutor) this.iface.wd).executeScript("window.scrollBy(0," + y + ");");
	}

	/**
	 * Send a string to this control.
	 * @param input  string to send
	 */
	public void sendString(String input) {
		voodoo.log.info("Selenium: sending string: " + input + " to control: " + this.toString());
		this.we.clear();
		// Re-find the element to avoid the stale element problem.
		this.we = iface.wd.findElements(VControl.makeBy(hook)).get(index);
		this.we.sendKeys(input);
	}

	@Override
	public String toString() {
		return "VControl(" + this.hook.toString() + ")";
	}
	
	public By getBy() {
		return VControl.makeBy(this.hook);
	}
	
	private static By makeBy(VHook hook) {
		return VControl.makeBy(hook.hookStrategy, hook.hookString);
	}
	
	public static By makeBy(Strategy strategy, String hook) {
		switch (strategy) {
		case CSS:
			return By.cssSelector(hook);
		case XPATH:
			return By.xpath(hook);
		case ID:
			return By.id(hook);
		case NAME:
			return By.name(hook);
		case LINK:
			return By.linkText(hook);
		case PLINK:
			return By.partialLinkText(hook);
		case CLASS:
			return By.className(hook);
		case TAG:
			return By.tagName(hook);
		default:
			throw new SeleniumException("Selenium: strategy type not recognized.");
		}
	}
}