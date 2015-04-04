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

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Hook;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sugarcrm.candybean.exceptions.CandybeanException;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import static java.lang.System.currentTimeMillis;

/**
 * Utility class that provides several methods for an element to pause until a
 * condition is satisfied
 *
 * @author Eric Tam
 * @author Jason Mittertreiner
 */
public class WebDriverPause {
	private WebDriver wd;
	private long defaultTimeoutMs;
	private long defaultPollingIntervalS;

	public Logger logger;

	public WebDriverPause(WebDriver wd, long defaultTimeoutMs, long defaultPollingIntervalS) {
		this.wd = wd;
		this.defaultTimeoutMs = defaultTimeoutMs;
		this.defaultPollingIntervalS = defaultPollingIntervalS;
		this.logger = Logger.getLogger(Candybean.class.getSimpleName());
	}

	/**
	 * Accepts any ExpectedCondition and poll under this condition is satisfied within timeout
	 * @param timeoutMs	Timeout in Milliseconds
	 * @param condition	The condition to poll
	 * @return Returning the object that is returned from ExpectedCondition when the condition is met
	 * @throws CandybeanException
	 */
	public Object waitUntil(ExpectedCondition condition, long timeoutMs) throws CandybeanException {
		// This is done by double-polling. WebDriverWait waits for wdPollingInterval amount of time.
		// This is done repetitively until the time reaches timeoutMs.
		final long pollingIntervalS = Math.min(defaultPollingIntervalS,  Math.max(timeoutMs/1000, 1));
		final long seleniumPollingIntervalMs = 250;
		final long startTime = currentTimeMillis();
		String toThrow = null;
		Object toReturn = null;

		logger.info("Waiting until " + condition.toString() + " is satisfied.");
		while(currentTimeMillis() - startTime <= timeoutMs) {
			try {
				toReturn = (new WebDriverWait(this.wd, pollingIntervalS, seleniumPollingIntervalMs)).until(condition);
				toThrow = null;
				break;
			} catch (WebDriverException wdException) {
				logger.info(currentTimeMillis() - startTime+ "ms have passed. Waiting until " + condition.toString() + " is satisfied.");
				toThrow = wdException.toString();
			}
		}

		if(toThrow != null) {
			logger.severe("The timeout of " + timeoutMs + "ms was reached. Throwing Exception.");
			throw new CandybeanException("Timed out after "+ timeoutMs + " seconds");
		}

		return toReturn;
	}

	public Object waitUntil(ExpectedCondition<?> condition) throws CandybeanException {
		return this.waitUntil(condition, defaultTimeoutMs);
	}

	/**
	 * Wait until an element is present on the DOM for up to timeoutMs seconds
	 *
	 * @param   hook Hook to find the element
	 * @param   timeoutMs Max wait time
	 * @return  The located element
	 * @throws  CandybeanException
	 */
	public WebDriverElement waitForElement(Hook hook, long timeoutMs) throws CandybeanException {
		return (WebDriverElement) this.waitUntil(WaitConditions.present(hook), timeoutMs);
	}

	/**
	 * Wait until an element is present on the DOM for up to 15 seconds
	 *
	 * @param   hook Hook to find the element
	 * @return  The located element
	 * @throws  CandybeanException
	 */
	public WebDriverElement waitForElement(Hook hook) throws CandybeanException {
		return (WebDriverElement) this.waitUntil(WaitConditions.present(hook), defaultTimeoutMs);
	}

	/**
	 * wait until an element is no present on the dom for up to timeoutMs seconds
	 *
	 * @param hook hook to find the element
	 * @param timeoutMs max wait time
	 * @throws CandybeanException
	 */
	public void waitForElementRemoved(Hook hook, long timeoutMs) throws CandybeanException {
		waitUntil(WaitConditions.not(WaitConditions.present(hook)), timeoutMs);
	}

	/**
	 * wait until an element is no present on the dom for up to 15 seconds
	 *
	 * @param   hook hook to find the element
	 * @throws  CandybeanException
	 */
	public void waitForElementRemoved(Hook hook) throws CandybeanException {
		waitUntil(WaitConditions.not(WaitConditions.present(hook)), defaultTimeoutMs);
	}

	/**
	 * Provides a simple method to wait for visible as it is often used
	 * @param hook
	 * @param timeoutMs
	 * @throws CandybeanException
	 */
	public WebDriverElement waitForVisible(Hook hook, long timeoutMs) throws CandybeanException {
		return (WebDriverElement) this.waitUntil(WaitConditions.visible(hook), timeoutMs);
	}

	/**
	 * Provides a simple method to wait for visible as it is often used
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement waitForVisible(Hook hook) throws CandybeanException {
		return this.waitForVisible(hook, defaultTimeoutMs);
	}

	/**
	 * Provides a simple method to wait for visible as it is often used
	 * @param wde
	 * @param timeoutMs
	 * @throws CandybeanException
	 */
	public WebDriverElement waitForVisible(WebDriverElement wde, long timeoutMs) throws CandybeanException {
		return (WebDriverElement) this.waitUntil(WaitConditions.visible(wde), timeoutMs);
	}

	/**
	 * Provides a simple method to wait for visible as it is often used
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement waitForVisible(WebDriverElement wde) throws CandybeanException {
		return this.waitForVisible(wde, defaultTimeoutMs);
	}

	/**
	 * Provides a simple method to wait for invisible
	 *
	 * @param   hook The hook used to find the element
	 * @param   timeoutMs The max wait time
	 * @throws CandybeanException If the element is visible after timeout
	 */
	public void waitForInvisible(Hook hook, long timeoutMs) throws CandybeanException {
		waitUntil(WaitConditions.invisible(hook), timeoutMs);
	}

	/**
	 * Provides a simple method to wait for invisible
	 *
	 * @param   hook The hook used to find the element
	 * @throws  CandybeanException If the element is visible after timeout
	 */
	public void waitForInvisible(Hook hook) throws CandybeanException {
		waitForInvisible(hook, defaultTimeoutMs);
	}

	/**
	 * Provides a simple method to wait for invisible
	 *
	 * @param wde The WebDriverElement to wait for
	 * @param timeoutMs The max wait time
	 * @throws CandybeanException
	 * 		If the element visible after timeout
	 */
	public void waitForInvisible(WebDriverElement wde, long timeoutMs) throws CandybeanException {
		waitUntil(WaitConditions.invisible(wde), timeoutMs);
	}

	/**
	 * Provides a simple method to wait for invisible
	 *
	 * @param   wde The WebDriverElement to wait for
	 * @throws  CandybeanException If the element visible after timeout
	 */
	public void waitForInvisible(WebDriverElement wde) throws CandybeanException {
		waitForInvisible(wde, defaultTimeoutMs);
	}

	/**
	 * Wait for an element to have the specified attribute with the specified value if expectValue is true, and the
	 * reverse if false, waiting for up to timeoutMS milliseconds.
	 *
	 * @param   hook Hook used to search for the element
	 * @param   attribute Attributed used to check for value
	 * @param   value Specified value of the attribute
	 * @param   expectValue If the value is expected or not
	 * @param   timeoutMS Max wait time before timeout
	 * @return  The found matching element
	 * @throws  CandybeanException If element not found or attribute has the wrong value
	 */
	public WebDriverElement waitForAttribute(Hook hook, String attribute, String value, boolean expectValue, long timeoutMS)
			throws CandybeanException {
		return (WebDriverElement) waitUntil(WaitConditions.hasAttribute(hook, attribute, value, expectValue),  timeoutMS);
	}

    /**
	 * Wait for an element to have the specified attribute with the specified value if expectValue is true, and the
	 * reverse if false, waiting for up to 15s
	 *
	 * @param   hook Hook used to search for the element
	 * @param   attribute Attributed used to check for value
	 * @param   value Specified value of the attribute
	 * @param   expectValue If the value is expected or not
	 * @return  The found matching element
	 * @throws  CandybeanException If element not found or attribute has the wrong value
	 */
	public WebDriverElement waitForAttribute(Hook hook, String attribute, String value, boolean expectValue)
			throws CandybeanException {
		return (WebDriverElement) waitUntil(WaitConditions.hasAttribute(hook, attribute, value, expectValue), defaultTimeoutMs);
	}

	/**
	 * Wait for an element to have the specified attribute with the specified value if expectValue is true, and the
	 * reverse if false, waiting for up to timeoutMS milliseconds.
	 *
	 * @param   hook Hook used to search for the element
	 * @param   attribute Attributed used to check for value
	 * @param   regex Speecified regex pattern of the attribute
	 * @param   expectValue If the value is expected or not
	 * @param   timeoutMS Max wait time before timeout
	 * @return  The found matching element
	 * @throws  CandybeanException If element not found or attribute has the wrong value
	 */
	public WebDriverElement waitForRegexAttribute(Hook hook, String attribute, String regex, boolean expectValue, long timeoutMS)
			throws CandybeanException {
		return (WebDriverElement) waitUntil(WaitConditions.hasRegexAttribute(hook, attribute, regex, expectValue),  timeoutMS);
	}

    /**
	 * Wait for an element to have the specified attribute with the specified value if expectValue is true, and the
	 * reverse if false, waiting for up to 15s
	 *
	 * @param   hook Hook used to search for the element
	 * @param   attribute Attributed used to check for value
	 * @param   regex Specified regex pattern of the attribute
	 * @param   expectValue If the value is expected or not
	 * @return  The found matching element
	 * @throws  CandybeanException If element not found or attribute has the wrong value
	 */
	public WebDriverElement waitForRegexAttribute(Hook hook, String attribute, String regex, boolean expectValue)
			throws CandybeanException {
		return (WebDriverElement) waitUntil(WaitConditions.hasRegexAttribute(hook, attribute, regex, expectValue), defaultTimeoutMs);
	}

	/**
	 * Waits until a specified element is on(off) screen for up to 15 seconds
	 *
	 * @param   hook Hook used to search for the element
	 * @param   isOnScreen Whether to check is the element is on or off screen
	 * @return  The element
	 * @throws  CandybeanException
	 */
	public WebDriverElement waitForOnScreen(Hook hook, boolean isOnScreen) throws CandybeanException {
		return  this.waitForOnScreen(hook, defaultTimeoutMs, isOnScreen);
	}

    /**
	 * Waits until a specified element is on(off) screen for up to timeoutMS seconds
	 *
	 * @param   hook Hook used to search for the element
	 * @param   timeoutMs Maximum time to wait for the element
	 * @param   isOnScreen Whether to check is the element is on or off screen
	 * @return  The element
	 * @throws  CandybeanException
	 */
	public WebDriverElement waitForOnScreen(Hook hook, long timeoutMs, boolean isOnScreen) throws CandybeanException {
		return (WebDriverElement) this.waitUntil(WaitConditions.onScreen(hook, isOnScreen), timeoutMs);
	}
}
