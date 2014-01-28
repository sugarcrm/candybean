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
package com.sugarcrm.candybean.examples.sugar;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.examples.sugar.SugarUser.SugarUserBuilder;
import com.sugarcrm.candybean.test.AbstractTest;
import com.sugarcrm.candybean.utilities.Utils;

public class SugarTest {
	
	private static Sugar sugar;
		
	@BeforeClass
	public static void first() throws Exception {
		Candybean candybean = getCandybean();
		Configuration sugarConfig = getSugarConfig();
		Properties sugarHooks = getSugarHooks();
		SugarUser adminUser = new SugarUserBuilder("admin", "Conrad", "cwarmbold@sugarcrm.com", "310.993.2449", "asdf").build();
		sugar = new Sugar(candybean, sugarConfig, sugarHooks, adminUser);
		sugar.start();
	}

//	@Ignore
	@Test
	public void sugarLoginLogoutTest() throws Exception {
		sugar.login();
		sugar.iface.interact("pause...");
		sugar.logout();
	}

	@AfterClass
	public static void last() throws Exception {
		sugar.stop();
	}
	
	private static Candybean getCandybean() throws Exception {
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) {
			candybeanConfigStr = AbstractTest.CONFIG_DIR.getCanonicalPath() + File.separator + "candybean.config";
		}
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		return Candybean.getInstance(candybeanConfig);
	}
	
	private static Configuration getSugarConfig() throws Exception {
		String sugarConfigStr = System.getProperty("sugar_config");
		if (sugarConfigStr == null) {
			sugarConfigStr = AbstractTest.CONFIG_DIR.getCanonicalPath() + File.separator + "sugar.config";
		}
		return new Configuration(new File(Utils.adjustPath(sugarConfigStr)));
	}
	
	private static Properties getSugarHooks() throws Exception {
		String sugarHooksStr = System.getProperty("sugar_hooks");
		if (sugarHooksStr == null) {
			sugarHooksStr = AbstractTest.CONFIG_DIR.getCanonicalPath() + File.separator + "sugar.hooks";
		}
		Properties sugarHooks = new Properties();
		sugarHooks.load(new FileInputStream(new File(Utils.adjustPath(sugarHooksStr))));
		return sugarHooks;
	}
}