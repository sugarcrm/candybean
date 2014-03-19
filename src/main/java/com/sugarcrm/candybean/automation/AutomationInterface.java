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
package com.sugarcrm.candybean.automation;

import java.util.logging.Logger;

import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Encapsulates cross-platform automation by defining and implementing common
 * behavior where possible. Extended interface types should be evaluated for
 * where the fit in the existing automation hierarchy.
 * 
 */
public abstract class AutomationInterface {
	
	public enum Type { CHROME, FIREFOX, IE, SAFARI, ANDROID, IOS }
	
	/**
	 * A preconfigured logger instance for child interfaces to log messages.
	 */
	protected static Logger logger;
	
	/**
	 * Reference to global candybean instance.
	 */
	protected final Candybean candybean;
	
	/**
	 * Text-based type that allows parsing of type from configuration files/CLI.
	 */
	protected final Type iType;
	
	public AutomationInterface(Type iType) throws CandybeanException {
		this.candybean = Candybean.getInstance();
		logger = Logger.getLogger(Candybean.class.getSimpleName());
		this.iType = iType;
	}
	
	/**
	 * Returns the interface type configured for use by the user.
	 * 
	 * @param iType
	 * @return
	 * @throws Exception
	 */
	public static Type parseType(String iType) throws CandybeanException {
		Type parsedType = null;
		for (Type type : Type.values()) {
			if (type.name().equalsIgnoreCase(iType)) {
				parsedType = type;
				break;
			}
		}
		if (parsedType == Type.ANDROID) throw new CandybeanException("Android interface type not yet implemented.");
		if (parsedType == Type.IOS) throw new CandybeanException("iOS interface type not yet implemented.");
		return parsedType;
	}
	
	/**
	 * Pause the automation for the specified duration.
	 *
	 * @param ms	Duration of pause in milliseconds
	 * @throws Exception if the underlying {@link Thread#sleep} is interrupted
	 */
	public void pause(long ms) throws CandybeanException {
		try {
			logger.info("Pausing for " + ms + "ms via thread sleep.");
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			throw new CandybeanException(e.getMessage());
		}
	}
	
	/**
	 * Starts the interface in a type-specific way and prepares for automation.
	 */
	public abstract void start() throws CandybeanException;

	/**
	 * Closes the interface and performs any type-specific cleanup.
	 */
	public abstract void stop() throws CandybeanException;
}
