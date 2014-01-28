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
package com.sugarcrm.candybean.utilities;

/**
 * Verifies the running OS
 *
 */
public class OSValidator {
	private static String OperatingSystem = System.getProperty("os.name").toLowerCase();
 
	public static boolean isWindows() {
		return (OperatingSystem.indexOf("win") >= 0);
	}
 
	public static boolean isMac() {
		return (OperatingSystem.indexOf("mac") >= 0);
	}
 
	public static boolean isUnix() {
		return (OperatingSystem.indexOf("nix") >= 0 || OperatingSystem.indexOf("nux") >= 0 || OperatingSystem.indexOf("aix") > 0 );
	}
 
	public static boolean isSolaris() {
		return (OperatingSystem.indexOf("sunos") >= 0);
	}
}
