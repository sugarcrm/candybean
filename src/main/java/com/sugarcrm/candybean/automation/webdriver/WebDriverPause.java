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
	 * @return			Returning the object that is returned from ExpectedCondition when the condition is met
	 * @throws CandybeanException
	 */
	public Object waitUntil(ExpectedCondition condition, long timeoutMs) throws CandybeanException {
		// This is done by double-polling. WebDriverWait waits for wdPollingInterval amount of time.
		// This is done repetitively until the time reaches timeoutMs.
		long timeoutS = timeoutMs * 1000;
		long pollingIntervalS = timeoutS <= defaultPollingIntervalS ? timeoutS : defaultPollingIntervalS;
		final long startTime = currentTimeMillis();
		long currentTimeMs, currentTimeS = 0;
		String toThrow = null;
		Object toReturn = null;

		while((currentTimeMs = currentTimeMillis() - startTime) <= timeoutMs) {
			try {
				currentTimeS = currentTimeMs / 1000;
				logger.info(currentTimeS + " seconds have passed. Waiting until " + condition.toString() +
						" is satisfied.");
				toReturn = (new WebDriverWait(this.wd, pollingIntervalS)).until(condition);
				toThrow = null;
				break;
			} catch (WebDriverException wdException) {
				toThrow = wdException.toString();
			}
		}

		if(toThrow != null) {
			logger.severe("The timeout " + currentTimeS + " seconds have reached. Throwing Exception.");
			throw new CandybeanException(toThrow.replaceAll("(.*Timed out after )[0-9]+( seconds.*)",
					"$1" + currentTimeS + "$2"));
		}

		return toReturn;
	}

	public Object waitUntil(ExpectedCondition<?> condition) throws CandybeanException {
		return this.waitUntil(condition, defaultTimeoutMs);
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
}
