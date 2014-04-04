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
package com.sugarcrm.candybean.automation.selenium;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sugarcrm.candybean.automation.element.Pause;
import com.sugarcrm.candybean.automation.selenium.SeleniumElement;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Utility class that provides several methods for an element to pause until an
 * action occurs.
 */
public class SeleniumPause extends Pause {

	private SeleniumElement wde;

	public SeleniumPause(SeleniumElement wde) {
		this.wde = wde;
	}

	@Override
	public SeleniumElement untilVisible(int timeoutMs) {
		(new WebDriverWait(this.wde.wd, timeoutMs / 1000)).until(ExpectedConditions
				.visibilityOf(this.wde.we));
		return this.wde;
	}
	
	public SeleniumElement untilTextPresent(String text, int timeout) 
			throws CandybeanException {
		(new WebDriverWait(this.wde.wd, timeout)).until(ExpectedConditions
				.textToBePresentInElement(this.wde.getBy(), text));
		return this.wde;
	}
}
