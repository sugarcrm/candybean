package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static com.sugarcrm.candybean.automation.element.Hook.getBy;

/**
 * This is a list of available wait conditions for WebDriverPause. It is a mix of default Selenium
 * conditions and custom conditions that implements the interface ExpectedCondition<RETURN_TYPE>.
 *
 * @author Eric Tam <etam@sugarcrm.com>
 */
public class WaitConditions {
	private WaitConditions() {
		// Utility class
	}

	/**
	 * A helper method to create a WebDriverElement given Hook and element. Depending on the tag, it will
	 * return either WebDriverSelector or WebDriverElement
	 *
	 * @param hook
	 * @param element
	 * @param driver
	 * @return
	 * @throws CandybeanException
	 */
	private static WebDriverElement createWebDriverElement(Hook hook, WebElement element, WebDriver driver)
			throws CandybeanException {
		if ("select".equals(element.getTagName())) {
			return new WebDriverSelector(hook, driver);
		}
		return new WebDriverElement(hook, 0, driver, element);
	}

	/**
	 * A helper method to find element on the page and handles exceptions
	 *
	 * @param hook
	 * @param driver
	 * @return
	 */
	private static WebElement findElement(Hook hook, WebDriver driver) throws CandybeanException {
		try {
			return driver.findElement(getBy(hook));
		} catch (WebDriverException e) {
			throw e;
		}
	}

	/**
	 * This returns the negated (logically opposite) condition. It waits until apply() returns null or false
	 * e.g. Conditions.not(Conditions.visible(...))
	 *
	 * @param    condition the condition you would like to negate
	 * @return The negated condition
	 */
	public static ExpectedCondition<Boolean> not(ExpectedCondition<?> condition) {
		return ExpectedConditions.not(condition);
	}

	/**
	 * Wait until the element is present on the DOM AND visible
	 *
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<WebDriverElement> visible(final Hook hook) {
		return new ExpectedCondition<WebDriverElement>() {
			@Override
			public WebDriverElement apply(WebDriver driver) {
				try {
					WebElement element = findElement(hook, driver);
					return element.isDisplayed() ? createWebDriverElement(hook, element, driver) : null;
				} catch (CandybeanException | StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "visibility of " + hook;
			}
		};
	}

	/**
	 * Wait until the element is present on the DOM AND visible
	 *
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<WebDriverElement> visible(final WebDriverElement wde) {
		return new ExpectedCondition<WebDriverElement>() {
			@Override
			public WebDriverElement apply(WebDriver driver) {
				try {
					return wde.isDisplayed() ? wde : null;
				} catch (CandybeanException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "visibility of " + wde;
			}
		};
	}

	/**
	 * Wait until the element is not present on the DOM OR invisible
	 *
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> invisible(Hook hook) throws CandybeanException {
		return ExpectedConditions.invisibilityOfElementLocated(getBy(hook));
	}

	/**
	 * Wait until the element is not present on the DOM OR invisible
	 *
	 * @param hook
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> invisibleWithText(Hook hook, String text) throws CandybeanException {
		return ExpectedConditions.invisibilityOfElementWithText(getBy(hook), text);
	}

	/**
	 * Wait until the element is present on the DOM
	 *
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<WebDriverElement> present(final Hook hook) {
		return new ExpectedCondition<WebDriverElement>() {
			@Override
			public WebDriverElement apply(WebDriver driver) {
				try {
					WebElement element = findElement(hook, driver);
					return createWebDriverElement(hook, element, driver);
				} catch (CandybeanException | StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "visibility of " + hook;
			}
		};
	}

	/**
	 * Wait until the element is clickable state (visible AND enabled)
	 *
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<WebElement> clickable(Hook hook) throws CandybeanException {
		return ExpectedConditions.elementToBeClickable(getBy(hook));
	}

	/**
	 * Wait until the element is clickable state (visible AND enabled)
	 *
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<WebElement> clickable(WebDriverElement wde) {
		return ExpectedConditions.elementToBeClickable(wde.we);
	}

	/**
	 * Wait until the element is selected
	 *
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> selected(Hook hook) throws CandybeanException {
		return ExpectedConditions.elementToBeSelected(getBy(hook));
	}

	/**
	 * Wait until the element is selected
	 *
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> selected(WebDriverElement wde) {
		return ExpectedConditions.elementToBeSelected(wde.we);
	}

	/**
	 * Wait until the element is unselected
	 *
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> unselected(Hook hook) throws CandybeanException {
		return ExpectedConditions.elementSelectionStateToBe(getBy(hook), false);
	}

	/**
	 * Wait until the element is unselected
	 *
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> unselected(WebDriverElement wde) {
		return ExpectedConditions.elementSelectionStateToBe(wde.we, false);
	}

	/**
	 * Wait until the frame is available to switch (Polling on switchTo().frame(...) until No NoSuchFrameException) and
	 * switch to this frame if no exception has occurred
	 *
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<WebDriver> frameToBeAvailableAndSwitchToIt(Hook hook) throws CandybeanException {
		return ExpectedConditions.frameToBeAvailableAndSwitchToIt(getBy(hook));
	}

	/**
	 * Wait until the frame is available to switch (Polling on switchTo().frame(...) until No NoSuchFrameException) and
	 * switch to this frame if no exception has occurred
	 *
	 * @param name
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<WebDriver> frameToBeAvailableAndSwitchToIt(String name) {
		return ExpectedConditions.frameToBeAvailableAndSwitchToIt(name);
	}

	/**
	 * Wait until the frame is available to switch (Polling on switchTo().frame(...) until No NoSuchFrameException) and
	 * switch to this frame if no exception has occurred
	 *
	 * @param frameIndex used to find the frame using the index
	 */
	public static ExpectedCondition<WebDriver> frameToBeAvailableAndSwitchToIt(final int frameIndex) {
		return new ExpectedCondition<WebDriver>() {
			@Override
			public WebDriver apply(WebDriver driver) {
				try {
					return driver.switchTo().frame(frameIndex);
				} catch (NoSuchFrameException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "frame to be available: " + frameIndex;
			}
		};
	}

	/**
	 * Wait until the frame is available to switch (Polling on switchTo().frame(...) until No NoSuchFrameException) and
	 * switch to this frame if no exception has occurred
	 *
	 * @param wde used to find the frame using WebDriverElement
	 */
	public static ExpectedCondition<WebDriver> frameToBeAvailableAndSwitchToIt(final WebDriverElement wde) {
		return new ExpectedCondition<WebDriver>() {
			@Override
			public WebDriver apply(WebDriver driver) {
				try {
					return driver.switchTo().frame(wde.we);
				} catch (NoSuchFrameException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "frame to be available: " + wde.toString();
			}
		};
	}

	/**
	 * Wait until the element is not present on the DOM
	 *
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> staleness(WebDriverElement wde) {
		return ExpectedConditions.stalenessOf(wde.we);
	}

	/**
	 * Wait until the expected text presents on the element
	 *
	 * @param hook
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> textIsPresent(Hook hook, String text) throws CandybeanException {
		return ExpectedConditions.textToBePresentInElementLocated(getBy(hook), text);
	}

	/**
	 * Wait until the expected text presents on the element
	 *
	 * @param wde
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> textIsPresent(WebDriverElement wde, String text) {
		return ExpectedConditions.textToBePresentInElement(wde.we, text);
	}

	/**
	 * Wait until the expected text presents on the element's value attribute
	 *
	 * @param hook
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public static ExpectedCondition<Boolean> textIsPresentInValueAttribute(Hook hook, String text) throws CandybeanException {
		return ExpectedConditions.textToBePresentInElementValue(getBy(hook), text);
	}

	/**
	 * Wait until the expected text presents on the element's value attribute
	 *
	 * @param wde
	 * @param text
	 * @return
	 */
	public static ExpectedCondition<Boolean> textIsPresentInValue(WebDriverElement wde, String text) {
		return ExpectedConditions.textToBePresentInElementValue(wde.we, text);
	}

	/**
	 * Wait until the window is available to switch (Polling on switchTo().window(...) until no
	 * NoSuchWindowException) and switch to this window if no exception has occurred
	 *
	 * @param	nameOrHandle	The name or handle of the window to switch to.
	 * @return	a WebDriver instance focused on the specified window
	 */
	public static ExpectedCondition<WebDriver> windowToBeAvailableAndSwitchToIt(final String nameOrHandle) {
		return new ExpectedCondition<WebDriver>() {
			@Override
			public WebDriver apply(WebDriver driver) {
				try {
					return driver.switchTo().window(nameOrHandle);
				} catch (NoSuchWindowException e) {
					System.out.println("No such window.");
					return null;
				}
			}

			@Override
			public String toString() {
				return "window to be available: " + nameOrHandle;
			}
		};
	}

	/**
	 * Wait for the specified number of windows to exist, e.g. when launching a new one to ensure
	 * it has been fully instantiated before interacting with it.
	 *
	 * @param	numberOfWindows	an int representing the desired number of windows to wait for.
	 * @return	boolean true if the number of windows is currently numberOfWindows, false if not
	 */
	public static ExpectedCondition<Boolean> numberOfWindowsToBe(final int numberOfWindows) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				driver.getWindowHandles();
				return driver.getWindowHandles().size() == numberOfWindows;
			}
		};
	}
}