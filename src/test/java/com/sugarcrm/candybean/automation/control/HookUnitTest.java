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
package com.sugarcrm.candybean.automation.control;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;


public class HookUnitTest {

	@Test
	public void testGetHooks() {
		try {
			File hooksFile = new File(Candybean.ROOT_DIR + File.separator + "testutils.hooks");
			hooksFile.createNewFile();
			Properties hooksProps = new Properties();
			String hook1Name = "hook1name";
			Strategy hook1Strategy = Strategy.ID;
			String hook1String = "hook1string";
			String hook2Name = "hook2name";
			Strategy hook2Strategy = Strategy.XPATH;
			String hook2String = "hook2string";
			hooksProps.setProperty(hook1Name, hook1Strategy + Hook.HOOK_DELIMITER + hook1String);
			hooksProps.setProperty(hook2Name, hook2Strategy + Hook.HOOK_DELIMITER + hook2String);
			hooksProps.store(new FileOutputStream(hooksFile), null);
//			JOptionPane.showInputDialog("pause");

			// Test
			Map<String, Hook> hooksMap = Hook.getHooks(hooksProps);
			Hook hook1 = hooksMap.get(hook1Name);
			Assert.assertEquals("Test hook1 strategy doesn't match expected: " + hook1Strategy, hook1Strategy, hook1.getHookStrategy());
			Assert.assertEquals("Test hook1 string doesn't match expected: " + hook1String, hook1String, hook1.getHookString());
			Hook hook2 = hooksMap.get(hook2Name);
			Assert.assertEquals("Test hook2 strategy doesn't match expected: " + hook2Strategy, hook2Strategy, hook2.getHookStrategy());
			Assert.assertEquals("Test hook2 string doesn't match expected: " + hook2String, hook2String, hook2.getHookString());

			// Resource cleanup
			hooksFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}

	}

	@Test
	public void testGetStrategy() {
		try {
			String[] strategyStrings = { "CSS", "ID", "NAME", "XPATH" };
			Strategy[] strategies = { Strategy.CSS, Strategy.ID, Strategy.NAME, Strategy.XPATH };
			for (int i = 0; i < strategyStrings.length; i++) {
				Strategy expectedStrategy = strategies[i];
				Strategy actualStrategy = Hook.getStrategy(strategyStrings[i]);
				Assert.assertEquals("Strategy doesn't match for given string: " + strategyStrings[i], expectedStrategy, actualStrategy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}
	}
}