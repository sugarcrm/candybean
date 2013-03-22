package com.sugarcrm.voodoo.automation.control;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author cwarmbold
 */
public class VControl {
	
	protected final Voodoo voodoo;
	protected final VInterface iface;
	protected final VHook hook;
	public final WebElement we;
	
	public VControl(Voodoo voodoo, VInterface iface, Strategy strategy, String hook) throws Exception {
		this(voodoo, iface, new VHook(strategy, hook));
	}
	
	public VControl(Voodoo voodoo, VInterface iface, Strategy strategy, String hook, int index) throws Exception {
		this(voodoo, iface, new VHook(strategy, hook), index);
	}
	
	public VControl(Voodoo voodoo, VInterface iface, VHook hook) throws Exception {
		this(voodoo, iface, hook, 0);
	}
	
	public VControl(Voodoo voodoo, VInterface iface, VHook hook, int index) throws Exception {
		this.voodoo = voodoo;
		this.iface = iface;
		this.we = iface.wd.findElements(this.getBy(hook)).get(index);
		this.hook = hook;
	}
	
	private VControl(Voodoo voodoo, VInterface iface, VHook hook, WebElement we) throws Exception {
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
		voodoo.log.info("Selenium: getting attribute: " + attribute	+ " for control: " + this.toString());
		String value = we.getAttribute(attribute);
		if (value == null) throw new Exception("Selenium: attribute does not exist.");
		else return value;
	}

	/**
	 * Get the visible text of this element.
	 *
	 * @return the visible text of this element
	 * @throws Exception	 if the element cannot be found
	 */
	public String getText() throws Exception {
		voodoo.log.info("Selenium: getting text for control: " + this.toString());
		return we.getText();
	}

	/**
	 * Click the element.
	 *
	 * @throws Exception	 if the element can not be found
	 */
	public void click() throws Exception {
		voodoo.log.info("Selenium: clicking on control: " + this.toString());
		we.click();
	}

	/**
	 * Double-click the element.
	 *
	 * @throws Exception	 if the element cannot be found
	 */
	public void doubleClick() throws Exception {
		voodoo.log.info("Selenium: double-clicking on control: " + this.toString());
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
		voodoo.log.info("Selenium: dragging control: " + this.toString() + " to control: " + dropControl.toString());
		Actions action = new Actions(this.iface.wd);
		action.dragAndDrop(this.we, dropControl.we).build().perform();
	}

	public VControl getControl(VHook hook) throws Exception {
		return this.getControl(hook, 0);
	}

	public VControl getControl(VHook hook, int index) throws Exception {
		voodoo.log.info("Selenium: getting control: " + hook.toString() + " from control: " + this.toString() + " with index: " + index);
		WebElement childWe = this.we.findElements(this.getBy(hook)).get(index);
		return new VControl(this.voodoo, this.iface, hook, childWe);
	}

	public VControl getControl(Strategy strategy, String hookString) throws Exception {
		return this.getControl(strategy, hookString, 0);
	}

	public VControl getControl(Strategy strategy, String hookString, int index) throws Exception {
		return this.getControl(new VHook(strategy, hookString), index);
	}

	/**
	 * Explicit wait until this control is displayed or the timeout (s) 
	 * is reached.
	 *	 *
	 * @throws Exception if the element cannot be found before the
	 *							timeout 
	 */
//	@Deprecated
//	public void halt(int timeout) throws Exception {
//		voodoo.log.info("Selenium: waiting for " + timeout + "ms on visibility of control: " + this.toString());
//		WebDriverWait wait = new WebDriverWait(this.iface.wd, timeout);
//		WebElement we = wait.until(ExpectedConditions.elementToBeClickable(this.getBy(this.hook)));
//		final WebElement we = this.getWebElement(this.getBy(this.hook));		
//		WebDriverWait wait = new WebDriverWait(this.iface.wd, explicitWait);
//		wait.until(new Function<WebDriver, Boolean>() {
//			public Boolean apply(WebDriver driver) {
//				return we.isDisplayed();
//			}
//		});
//	}

//	@Deprecated
//	public void halt(String attribute, String value, int timeout) throws Exception {
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
	 * @throws Exception	 if the element cannot be found
	 */
	public void hover() throws Exception {
		voodoo.log.info("Selenium: hovering over control: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.moveToElement(this.we).perform();
	}

	/**
	 * Right-click this control.
	 *
	 * @throws Exception	 if the element cannot be found
	 */
	public void rightClick() throws Exception {
		voodoo.log.info("Selenium: right-clicking control: " + this.toString());
		Actions action = new Actions(this.iface.wd);
		action.contextClick(this.we).perform();
	}

	/**
	 * Scroll the browser window to this control.
	 *
	 * @throws Exception	 if the element cannot be found or if the scroll fails
	 */
	public void scroll() throws Exception {
		voodoo.log.info("Selenium: scrolling to control: " + this.toString());
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
		voodoo.log.info("Selenium: sending string: " + input + " to control: " + this.toString());
		this.we.clear();
		this.we.sendKeys(input);
	}

	@Override
	public String toString() {
		return "VControl(" + this.hook.toString() + ")";
	}
	
	protected By getBy(VHook hook) throws Exception {
		return this.getBy(hook.hookStrategy, hook.hookString);
	}
	
	protected By getBy(Strategy strategy, String hook) throws Exception {
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
			throw new Exception("Selenium: strategy type not recognized.");
		}
	}
}
