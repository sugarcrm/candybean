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
package com.sugarcrm.candybean.runner;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sugarcrm.candybean.runner.VTag;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class VTagUnitTest {

	@Test
	@VTag(tags={"mac"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void macTest() throws Exception {
		assertTrue(getOS().equalsIgnoreCase("mac"));
	}
	
	@Test
	@VTag(tags={"windows"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void windowsTest() throws Exception {
		assertTrue(getOS().equalsIgnoreCase("windows"));
	}
	
	@Test
	@VTag(tags={"linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void linuxTest() throws Exception {
		assertTrue(getOS().equalsIgnoreCase("linux"));
	}
	
	@Test
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void allTest() throws Exception {
		assertTrue(true);
	}
	
	@Test
	@VTag(tags={"xmac", "xwindows", "xlinux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void noneTest() throws Exception {
		fail();
	}
	
	@Test
	public void testTest() throws Exception {
		assertTrue(true);
	}
	
	@Test
	@VTag(tags={"mac", "windows", "linux"})
	public void noLogicTest() throws Exception {
		fail();
	}
	
	@Test
	@VTag(tags={"mac"}, tagLogicMethod="processTags")
	public void noLogicClassTest() throws Exception {
		fail();
	}
	
	@Test
	@VTag(tags={"mac"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest")
	public void noLogicMethodTest() throws Exception {
		fail();
	}
	
	public static boolean processTags(String tag) {
		String os = getOS();
		if (tag.equalsIgnoreCase(os)) return true;
		return false;
	}
	
	private static String getOS() {
		String osName = System.getProperty("os.name").toLowerCase().substring(0, 3);
		switch (osName) {
			case "win":
				return "windows";
			case "mac":
				return "mac";
			default:
				return "linux";
		}
	}
}