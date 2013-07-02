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
package com.sugarcrm.voodoo.utilities;

import java.util.logging.Logger;

/**
 * A Logger wrapper that can log INFO and SEVERE messages. Automatically checks
 * if Logger is null. If true, display message to console. Used to substitute a
 * Logger when a Logger's presence is optional.
 * 
 * @author ylin
 * 
 */
public class OptionalLogger {
	Logger log;

	public OptionalLogger() {
		this(null);
	}

	public OptionalLogger(Logger log) {
		this.log = log;
	}

	public void info(String msg, boolean writeBoth) {
		if (writeBoth) {
			if (log != null)
				log.info(msg);
			else {
				System.err.println("Logger is null!");
				System.out.print(msg);
			}
			System.out.print(msg);
		} else {
			if (log != null)
				log.info(msg);
			else
				System.out.print(msg);
		}
	}
	
	public void info(String msg) {
		info(msg, false);
	}

	public void severe(String msg, boolean writeBoth) {
		if (writeBoth) {
			if (log != null)
				log.severe(msg);
			else {
				System.err.println("Logger is null!");
				System.err.print(msg);
			}
		} else {
			if (log != null)
				log.severe(msg);
			else
				System.err.print(msg);
		}
	}
	
	public void severe(String msg) {
		severe(msg, false);
	}
}
