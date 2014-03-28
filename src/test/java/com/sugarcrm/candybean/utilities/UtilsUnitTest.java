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

import java.io.File;
import org.junit.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;
import com.sugarcrm.candybean.utilities.Utils.Pair;
import com.sugarcrm.candybean.utilities.Utils.Triplet;

public class UtilsUnitTest {

	@Test
	public void testGetCascadingPropertyValue() {
		try {
			String propsFilePath = Candybean.ROOT_DIR + File.separator + "testutils.props";
			String propKey = "key";
			String propSysKey = "syskey";
			String propConfigVal = "configvalue";
			String propDefaultVal = "defaultvalue";
			String propSysVal = "systemvalue";

			// Resource setup
			File propsFile = new File(propsFilePath);
			propsFile.createNewFile();
			Configuration voodooConfig = new Configuration();
			voodooConfig.setValue(propKey, propConfigVal);
			voodooConfig.setValue(propSysKey, propConfigVal);
			System.setProperty(propSysKey, propSysVal);

			// Test
			String actualDefaultVal = voodooConfig.getValue("NULL", propDefaultVal);
			Assert.assertEquals("Expected default value.", propDefaultVal, actualDefaultVal);
			String actualConfigVal = voodooConfig.getValue(propKey, propDefaultVal);
			Assert.assertEquals("Expected configuration value.", propConfigVal, actualConfigVal);
			String actualSysVal = voodooConfig.getValue(propSysKey, propDefaultVal);
			Assert.assertEquals("Expected system value.", propSysVal, actualSysVal);

			// Resource cleanup
			System.clearProperty(propSysKey);
			propsFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}
	}

	@Test
	public void testPretruncate() {
		String original = "ffour";
		String expected = "four";
		String actual = Utils.pretruncate(original, 4);
		Assert.assertEquals("Actual pretruncated string length does not match expected.", expected, actual);
	}

	@Test
	public void testAdjustPath() {
		String c = File.separator;
		String path1 = "~/computer\\  science\\Hello\\";
		String expected1 = "~" + c + "computer\\  science" + c + "Hello" + c;
		String path2 = "c:\\computer\\\"science\"\\";
		String expected2 = "c:" + c + "computer" + c + "\"science\"" + c;
		String path3 = "\\\"computer science\"\\";
		String expected3 = c + "\"computer science\"" + c;
		String path4 = "cd /computer\\ science\\";
		String expected4 = "cd " + c + "computer\\ science" + c;
		String path5 = "computer\\ \\ \\ \\ \\ science/";
		String expected5 = "computer\\ \\ \\ \\ \\ science" + c;
		Assert.assertEquals(expected1, Utils.adjustPath(path1));
		Assert.assertEquals(expected2, Utils.adjustPath(path2));
		Assert.assertEquals(expected3, Utils.adjustPath(path3));
		Assert.assertEquals(expected4, Utils.adjustPath(path4));
		Assert.assertEquals(expected5, Utils.adjustPath(path5));
	}

	@Test
	public void testPair() {
		Object o1 = new Object();
		Object o2 = new Object();
		Pair<Object, Object> pair = new Pair<Object, Object>(o1, o2);
		Assert.assertEquals("Actual pair object x does not match expected, original object x.", pair.x, o1);
		Assert.assertEquals("Actual pair object y does not match expected, original object y.", pair.y, o2);
	}


	@Test
	public void testTriplet() {
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		Triplet<Object, Object, Object> triplet = new Triplet<Object, Object, Object>(o1, o2, o3);
		Assert.assertEquals("Actual pair object x does not match expected, original object x.", triplet.x, o1);
		Assert.assertEquals("Actual pair object y does not match expected, original object y.", triplet.y, o2);
		Assert.assertEquals("Actual pair object z does not match expected, original object z.", triplet.z, o3);
	}
}