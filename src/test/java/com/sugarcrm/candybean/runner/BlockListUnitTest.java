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

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class BlockListUnitTest {
	
	// To verify VRunner @Ignore doesn't break
	@Ignore
	@Test
	public void ignoreFail() throws Exception {
		fail();
	}
	
	// This test will fail unless a blocklist is defined
	// which lists this qualified test name and blocks it 
	// from execution: "BlockListUnitTest.blockListFail"
	// The blocklist should be passed via system variable
	// with key "blocklist".  Marking as ignored after 
	// passing to prevent wasted effort.
	@Ignore
	@Test
	public void blockListFail() throws Exception {
		fail();
	}

	// To ensure VRunner @Test doesn't break
	@Ignore
	@Test
	public void pass() throws Exception {
		assertTrue(true);
	}
}
