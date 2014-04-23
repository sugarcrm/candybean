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
package com.sugarcrm.candybean.automation.element;

import java.util.logging.Logger;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class Element {
	
	protected final Hook hook;
	protected int index;
	
	/**
	 * A preconfigured logger instance for child elements to log messages.
	 */
	public static Logger logger;
	
	public Element(Hook hook, int index) {
		this.hook = hook;
		this.index = index;
		logger = Logger.getLogger(Candybean.class.getSimpleName());
	}
	
	/**
	 * Returns the child element specified by the given {@link Hook} and index,
	 * that is, the element specified within the current, containing, parent
	 * element.
	 * 
	 * @param hook
	 * @param index
	 * @return
	 */
	public abstract Element getElement(Hook hook, int index) throws CandybeanException;

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(hook:"
				+ this.hook.toString() + ", index:" + this.index + ")";
	}
}
