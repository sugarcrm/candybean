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
/*
 * Basic.java --
 *
 *      The class in this file demonstrates the most basic of Voodoo
 *      functionality.
 */

package com.sugarcrm.candybean.examples;

import java.io.File;
import java.io.IOException;

import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

/**
 * Basic example class.
 *
 * <p>The Basic class demonstrates the most basic usage of VDD.  It
 * uses a Voodoo object to get the Selenium interface object.  With
 * that, it simply starts and then stops a WebDriver session.</p>
 *
 * @author Jon duSaint
 */

public class Basic {

	/**
	 * Log a message
	 *
	 * @param m  the message
	 */

	protected void log(String m) {
		System.out.println(m);
	}

	/**
	 * Log a Voodoo exception.
	 *
	 * <p>Every method in the Voodoo class and IInterface throws {@link
	 * Exception}.  Rather than boilerplate logging a message and the
	 * stack trace, this method combines those two functions, saving a
	 * small amount of code.</p>
	 *
	 * @param e  the exception from Voodoo
	 * @param m  a message to log with that exception
	 */

	protected void ve(Throwable e, String m) {
		System.err.println("Exception caught " + m + ":");
		e.printStackTrace(System.err);
	}

	/**
	 * Run the basic example code.
	 * @throws Exception 
	 */

	@Example
	public void runExample() throws Exception  {
		Configuration c;
		Candybean v = null;
		VInterface i = null;

		log("*** Example of basic VDD2 usage ***");

        /*
		 * This path should be replaced by the correct path to the
		 * properties file.
		 *
		 * XXX: There needs to be a Voodoo-provided runtime method of
		 * getting the path to this.  Hard-coded paths are fragile
		 * and almost certainly wrong.
		 */
		File relResourcesDir = new File(System.getProperty("user.dir") + File.separator + 
				"src" + File.separator +
				"test" + File.separator + 
				"resources" + File.separator);
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) candybeanConfigStr = relResourcesDir.getCanonicalPath() + "candybean.config";
		c = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		
		try {
			v = Candybean.getInstance(c);
		} catch (Exception e) {
			ve(e, "during Voodoo instantiation");
			return;
		}

		try {
			i = v.getInterface();
		} catch (Exception e) {
			ve(e, "Getting Voodoo interface");
			return;
		}

		log("Stopping WebDriver");
		try {
			i.stop();
		} catch (Exception e) {
			ve(e, "stopping WebDriver");
			return;
		}

		log("Done");
	}
}
