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
package com.sugarcrm.voodoo.runner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sugarcrm.voodoo.runner.VoodooTag;
import com.sugarcrm.voodoo.runner.VoodooTagRunner;

@RunWith(VoodooTagRunner.class)
public class PlatformAnnotationTest {

	@Test
	@VoodooTag(OS="MAC")
	public void macSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Mac system only");
	}

	@Test
	@VoodooTag(OS="WINDOWS")
	public void windowsSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Windows system only");
	}

	@Test
	@VoodooTag(OS="UNIX")
	public void unixSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Unix system only");
	}

	@Test
	@VoodooTag(OS="SOLARIS")
	public void solarisSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Solaris system only");
	}

	@Test
	@VoodooTag(OS="MAC WINDOWS SOLARIS UNIX")
	public void AllPlatformTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Mac, Windows, Solaris, and Unix systems");
	}

	@Test
	@VoodooTag
	public void defaultPlatformTest() throws Exception {
		System.out.println("[Tag Test]: Printing as default setting (Any OS)");
	}

}
