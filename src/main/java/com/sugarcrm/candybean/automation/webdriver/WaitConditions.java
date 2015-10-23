package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.sugarcrm.candybean.automation.element.Hook.getBy;

/**
 * This is a list of available wait conditions for WebDriverPause. It is a mix of default Selenium
 * conditions and custom conditions that implements the interface ExpectedCondition
 *
 * @author Eric Tam etam@sugarcrm.com
 * @author Jason Mittertreiner
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
	 * A helper method to find the first matching element on the page
	 *
	 * @param	hook	The hook used to search for the element
	 * @param	driver	WebDriver to search with
	 * @return 	The element, if found
	 * @throws 	CandybeanException	If the element is not found
	 */
	private static WebElement findElement(Hook hook, WebDriver driver) throws CandybeanException {
		List<WebElement> elements = driver.findElements(getBy(hook));
		if (elements.isEmpty()) throw new CandybeanException("No such elements found");
		return elements.get(0);
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
	 * This is not possible with ExpectedConditions.not() because no only
	 * do we need to return when isDisplayed is false, we also need to
	 * return true with isDisplayed throws an exception, which
	 * ExpectedConditions.not does not do.
	 *
	 * @param	hook	The hook to search for the element
	 * @return	True, when the element is invisible or removed, otherwise false
	 */
	public static ExpectedCondition<Boolean> invisible(final Hook hook) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					WebElement element = findElement(hook, driver);
					return !(element.isDisplayed());
				} catch (CandybeanException | StaleElementReferenceException e) {
					return true;
				}
			}

			@Override
			public String toString() {
				return "invisibility of " + hook;
			}
		};
	}

	/**
	 * Wait until the element is not present on the DOM OR invisible
	 * This is not possible with ExpectedConditions.not because no only
	 * do we need to return when isDisplayed is false, we also need to
	 * return true with isDisplayed throws an exception, which
	 * ExpectedConditions.not does not do.
	 *
	 * @param	wde	The webdriver element to search for
	 * @return	True, when the element is invisible or removed, otherwise false
	 */
	public static ExpectedCondition<Boolean> invisible(final WebDriverElement wde) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return !wde.isDisplayed();
				} catch (CandybeanException | StaleElementReferenceException e) {
					return true;
				}
			}

			@Override
			public String toString() {
				return "invisibility of " + wde;
			}
		};
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
	 */
	public static ExpectedCondition<WebDriverElement> present(final Hook hook) {
		return new ExpectedCondition<WebDriverElement>() {
			@Override
			public WebDriverElement apply(WebDriver driver) {
				try {
					WebElement element = findElement(hook, driver);
					return createWebDriverElement(hook, element, driver);
				} catch (CandybeanException | StaleElementReferenceException e ) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "presence of " + hook;
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
	 */
	public static ExpectedCondition<WebElement> clickable(WebDriverElement wde) {
		return ExpectedConditions.elementToBeClickable(wde.we);
	}

	/**
	 * Test if the x and y coordinates of the element are with in the width and height of the screen
	 *
	 * @param	hook	The hook used to find the element
	 * @param	isOnScreen	If the element should be on screen or not
	 * @return	ExpectedCondition that tests to see if the element in on screen
	 * @throws	CandybeanException
	 */
	public static ExpectedCondition<WebDriverElement> onScreen(final Hook hook, final boolean isOnScreen) throws CandybeanException {
		return new ExpectedCondition<WebDriverElement>() {
			@Override
			public WebDriverElement apply(WebDriver driver) {
				try {
					WebDriverElement element = createWebDriverElement(hook,findElement(hook, driver),driver);
					return element.isOnScreen() == isOnScreen ? element : null;
				} catch (CandybeanException | StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() { return "if " + hook + (isOnScreen ? "is on screen" : "is off screen");
			}
		};
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
			public String toString(){
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

	/*
	 * Return the element found by hook that contains the specified attribute value if expected,
	 * or the reverse if not.
	 *
	 * @param	hook	The hook used to find the element
	 * @param	attribute	The attribute to check
	 * @param	value	The expected value of the attribute
	 * @param	expectValue	If the value is expected or not
	 * @return	The element if the specified value contains the specified attributed, null otherwise
	 */
	public static ExpectedCondition<WebDriverElement> hasAttribute(final Hook hook, final String attribute,
			final String value, final boolean expectValue) {
		return new ExpectedCondition<WebDriverElement>() {
			@Override
			public WebDriverElement apply(WebDriver driver) {
				try {
					WebElement element = findElement(hook, driver);
					/*
					Split the string so that we can match the attribute.
					If we are waiting for a value of "red", then "red left-aligned large"
					should match that, but "starred" should not.
					 */
					for (String currentValue : element.getAttribute(attribute).split("\\s")) {
						if (currentValue.equals(value)) {
							return expectValue ? createWebDriverElement(hook, element, driver) : null;
						}
					}
					return expectValue ? null : createWebDriverElement(hook, element, driver);
				} catch (CandybeanException | StaleElementReferenceException e) {
						return null;
				}
			}

			@Override
			public String toString() {
				return attribute + (expectValue? " is ": " isn't ") + value;
			}
		};
	}

	/*
	 * Return the element found by hook that contains the specified attribute value via regex if expecting match, or
	 * reverse if expectValue is false
	 *
	 * @param hook The hook used to find the element
	 * @param attribute The attribute to check
	 * @param regex String regex of the expected value of the attribute
	 * @param expectValue If the value is expected or not
	 * @return The element if the specified value contains the specified attributed, null otherwise
	 * @throws CandybeanException If the element is not found
	 */
	public static ExpectedCondition<WebDriverElement> hasRegexAttribute(final Hook hook, final String attribute,
			final String regex, final boolean expectValue) throws CandybeanException {
		return new ExpectedCondition<WebDriverElement>() {
			@Override
			public WebDriverElement apply(WebDriver driver) {
				try {
					WebElement element = findElement(hook, driver);

					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(element.getAttribute(attribute));
					return expectValue ? (m.matches() ? createWebDriverElement(hook, element, driver) : null)
						:(m.matches() ? null : createWebDriverElement(hook, element, driver));
				} catch (CandybeanException | StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return attribute + (expectValue? " matches ": " does not match ") + regex;
			}
	};
}
		}
