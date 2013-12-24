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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.TimeoutException;

import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.control.VControl;
import com.sugarcrm.candybean.automation.control.VHook;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

//import com.sugarcrm.candybean.IAutomation.Strategy;
//import com.sugarcrm.candybean.automation.VHook;
//import com.sugarcrm.candybean.IAutomation;
//import com.sugarcrm.candybean.Candybean;
//import static org.junit.Assert.assertEquals;

public class Adhoc {
	
	protected static File relResourcesDir;
	protected static Candybean candybean;
	protected static VInterface iface;
		
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void first() throws Exception {
		relResourcesDir = new File(System.getProperty("user.dir") + File.separator + 
				"src" + File.separator +
				"test" + File.separator + 
				"resources" + File.separator);
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) candybeanConfigStr = relResourcesDir.getCanonicalPath() + File.separator + "candybean.config";
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		iface = candybean.getInterface();
		iface.start();
	}

	@Ignore
	@Test
	public void dragNDropTest() throws Exception {
		iface.go("http://honey-b/pro700/");
		iface.getControl(Strategy.NAME, "username").sendString("admin");
		iface.getControl(Strategy.NAME, "password").sendString("asdf");
		iface.getControl(Strategy.NAME, "login_button").click();
		iface.go("http://honey-b/pro700/#bwc/index.php?module=ModuleBuilder&action=index&type=studio");
		iface.pause(8000);
		iface.focusFrame("bwc-frame");
		iface.getControl(Strategy.PLINK, "Accounts").click();
		iface.pause(1000);
		iface.getControl(Strategy.PLINK, "Layouts").click();
		iface.pause(1000);
		iface.getControl(Strategy.PLINK, "Record View").click();
		iface.pause(1000);
		iface.interact("pause...");
		iface.getControl(Strategy.ID, "le_label_1").dragNDrop(iface.getControl(Strategy.ID, "21"));
		iface.interact("pause...");
		iface.focusDefault();
	}
	
	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}	
