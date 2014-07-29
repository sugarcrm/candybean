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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;

/**
 * @author cwarmbold
 */
public class VControl {
	protected final Candybean voodoo;
	protected final VInterface iface;
	protected final VHook hook;
	protected int index;
	/**
	 * The Selenium WebElement representing this element.
	 */
	public WebElement we;
	/**
	 * An object allowing us to pause until a desired condition is met. 
	 */
	public Pause pause;
	
	/**
	 * @param voodoo
	 * @param iface
	 * @param strategy
	 * @param hook
	 * @throws Exception
	 */
	public VControl(Candybean voodoo, VInterface iface, Strategy strategy, String hook) throws Exception {
		this(voodoo, iface, new VHook(strategy, hook));
	}
	
	/**
	 * @param voodoo
	 * @param iface
	 * @param strategy
	 * @param hook
	 * @param index
	 * @throws Exception
	 */
	public VControl(Candybean voodoo, VInterface iface, Strategy strategy, String hook, int index) throws Exception {
		this(voodoo, iface, new VHook(strategy, hook), index);
	}
	
	/**
	 * @param voodoo
	 * @param iface
	 * @param hook
	 * @throws Exception
	 */
	public VControl(Candybean voodoo, VInterface iface, VHook hook) throws Exception {
		this(voodoo, iface, hook, 0);
	}
	
	/**
	 * @param voodoo
	 * @param iface
	 * @param hook
	 * @param index
	 * @throws Exception
	 */
	public VControl(Candybean voodoo, VInterface iface, VHook hook, int index) throws Exception {
			this.voodoo = voodoo;
			this.iface = iface;
			List<WebElement> wes = iface.wd.findElements(VControl.makeBy(hook));
			if (wes.size() == 0) throw new Exception("Control not found; zero web elements returned.");
			this.we = wes.get(index);
			this.pause = new Pause(this);
			this.hook = hook;
			this.index = index;
	}
	
	/**
	 * Default-access constructor for use by VSelect.
	 */
	VControl(Candybean voodoo, VInterface iface, VHook hook, WebElement we) throws Exception {
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
	 * @throws Exception	 if the attribute does not exist or the element
	 *							 cannot be found
	 */
	public String getAttribute(String attribute) throws Exception {
		voodoo.log.info("CandyBean: getting attribute: " + attribute	+ " for control: " + this.toString());
		String value = we.getAttribute(attribute);
		if (value == null) throw new Exception("CandyBean: attribute does not exist.");
		else return value;
	}

	/**
	 * @return the HTML source for this element
	 * @throws Exception
	 */
	public String getSource() throws Exception {
		voodoo.log.info("CandyBean: getting source for control: " + this.toString());
		return (String)((JavascriptExecutor)iface.wd).executeScript("return arguments[0].innerHTML;", this.we);
	}

	/**
	 * Get the visible text of this control.  If the control is a button, the value is returned.
	 *
	 * @return the visible text of this element
	 * @throws Exception	 if the element cannot be found
	 */
	public String getText() throws Exception {
		voodoo.log.info("CandyBean: getting text for control: " + this.toString());
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
	 *
	 * @throws Exception	 if the element can not be found
	 */
	public void click() throws Exception {
		voodoo.log.info("Candybean: clicking on control: " + this.toString());
		we.click();
	}
	
	/**
	 * Returns true if the control visibly contains the 
	 * given string in any non-visible=false element.
	 *
	 * @param  s   The target string searched for in the interface		
	 * @param  caseSensitive   Whether or not the search is case sensitive		
	 * @return true if this element visibly contains the given string
	 * @throws Exception
	 */
	public boolean contains(String s, boolean caseSensitive) throws Exception {
		voodoo.log.info("Searching if the control contains the following string: '" + s + "' with case sensitivity: " + caseSensitive);
		if (!caseSensitive) s = s.toLowerCase();
		List<WebElement> wes = this.we.findElements(By.xpath(".//*[not(@visible='false')]"));
		wes.add(this.we);
		for (WebElement we : wes) {
			String text = we.getText();
			if (!caseSensitive) text = text.toLowerCase();
//			System.out.println("text: " + text);
			if (text.contains(s)) return true;
		}
		return false;
	}

	/**
	 * Double-click the element.
	 *
	 * @throws Exception	 if the element cannot be found
	 */
	public void doubleClick() throws Exception {
		voodoo.log.info("CandyBean: double-clicking on control: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.doubleClick(we).perform();
	}

	/**
	 * Drag this control and drop onto another control.
	 *
	 * @param dropControl  target of the drag and drop
	 * @throws Exception	 if either element cannot be found
	 */
	public void dragNDrop(VControl dropControl)	throws Exception {
		voodoo.log.info("CandyBean: dragging control: " + this.toString() + " to control: " + dropControl.toString());
		Actions action = new Actions(this.iface.wd);
		action.dragAndDrop(this.we, dropControl.we).build().perform();
	}

	/**
	 * @param hook
	 * @return
	 * @throws Exception
	 */
	public VControl getControl(VHook hook) throws Exception {
		return this.getControl(hook, 0);
	}

    /**
     * @param hook
     * @param index
     * @return
     * @throws Exception
     */
	public VControl getControl(VHook hook, int index) throws Exception {
		voodoo.log.info("CandyBean: getting control: " + hook.toString() + " from control: " + this.toString() + " with index: " + index);
		WebElement childWe = this.we.findElements(VControl.makeBy(hook)).get(index);
		return new VControl(this.voodoo, this.iface, hook, childWe);
	}

    /**
     * @param hook
     * @param index
     * @return
     * @throws Exception
     */
    public VSelect getSelect(VHook hook) throws Exception {
        voodoo.log.info("CandyBean: getting select: " + hook.toString() + " from control: " + this.toString() + " with index: " + index);
        WebElement childWe = this.we.findElement(VControl.makeBy(hook));
        voodoo.log.info("Found select: " + childWe.toString());
        return new VSelect(this.voodoo, this.iface, hook);
    }

	/**
	 * @param strategy
	 * @param hookString
	 * @return
	 * @throws Exception
	 */
	public VControl getControl(Strategy strategy, String hookString) throws Exception {
		return this.getControl(strategy, hookString, 0);
	}

	/**
	 * @param strategy
	 * @param hookString
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public VControl getControl(Strategy strategy, String hookString, int index) throws Exception {
		return this.getControl(new VHook(strategy, hookString), index);
	}

	/**
	 * Hover over this control.
	 *
	 * @throws Exception	 if the element cannot be found
	 */
	public void hover() throws Exception {
		voodoo.log.info("CandyBean: hovering over control: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.moveToElement(this.we).perform();
	}
	
	/**
	 * Returns true if and only if the control is displayed
	 * {@link "http://selenium.googlecode.com/svn/trunk/docs/api/java/index.html"}
	 * @return true if this element is visibly displayed on the page.
	 *
	 * @throws Exception
	 */
	public boolean isDisplayed() throws Exception {
		voodoo.log.info("CandyBean: determining if element is visible: " + this.toString());
		return we.isDisplayed();
	}

	/**
	 * Right-click this control.
	 *
	 * @throws Exception   if the element cannot be found
	 */
	public void rightClick() throws Exception {
		voodoo.log.info("CandyBean: right-clicking element: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.contextClick(this.we).perform();
	}

	/**
	 * Scroll the browser window to this element.
	 *
	 * @throws Exception   if the element cannot be found or if the scroll fails
	 */
	public void scroll() throws Exception {
		voodoo.log.info("CandyBean: scrolling to element: " + this.toString());
		int y = this.we.getLocation().y;
		((JavascriptExecutor) this.iface.wd).executeScript("window.scrollBy(0," + y + ");");
	}

	/**
	 * Send a string to this control.
	 *
	 * @param input  string to send
	 * @throws Exception	 if the element cannot be found
	 */
	public void sendString(String input) throws Exception {
		voodoo.log.info("CandyBean: sending string: " + input + " to control: " + this.toString());
		this.we.clear();
		
		// Re-find the element to avoid the stale element problem.
		this.we = iface.wd.findElements(VControl.makeBy(hook)).get(index);
		this.we.sendKeys(input);
	}

	@Override
	public String toString() {
		return "VControl(" + this.hook.toString() + ")";
	}
	
	protected By getBy() throws Exception {
		return VControl.makeBy(this.hook);
	}
	
	protected static By makeBy(VHook hook) throws Exception {
		return VControl.makeBy(hook.hookStrategy, hook.hookString);
	}
	
	protected static By makeBy(Strategy strategy, String hook) throws Exception {
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
			throw new Exception("CandyBean: strategy type not recognized.");
		}
	}
}
