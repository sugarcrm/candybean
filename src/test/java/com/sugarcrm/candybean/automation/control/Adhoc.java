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

import javax.swing.JOptionPane;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.automation.webdriver.WebDriverElement;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

//import com.sugarcrm.candybean.IAutomation.Strategy;
//import com.sugarcrm.candybean.automation.VHook;
//import com.sugarcrm.candybean.IAutomation;
//import com.sugarcrm.candybean.Candybean;
//import static org.junit.Assert.assertEquals;

public class Adhoc {
	
	protected static Candybean candybean;
	protected static WebDriverInterface iface;
		
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void first() throws Exception {
		String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY, Candybean.getDefaultConfigFile());
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		iface = candybean.getAIB(Adhoc.class).build();
		iface.start();
	}

	@Ignore
	@Test
	public void dragNDropTest() throws Exception {
		iface.go("http://honey-b/pro700/");
		iface.getWebDriverElement(Strategy.NAME, "username").sendString("admin");
		iface.getWebDriverElement(Strategy.NAME, "password").sendString("asdf");
		iface.getWebDriverElement(Strategy.NAME, "login_button").click();
		iface.go("http://honey-b/pro700/#bwc/index.php?module=ModuleBuilder&action=index&type=studio");
		iface.pause(8000);
		iface.focusFrame("bwc-frame");
		iface.getWebDriverElement(Strategy.PLINK, "Accounts").click();
		iface.pause(1000);
		iface.getWebDriverElement(Strategy.PLINK, "Layouts").click();
		iface.pause(1000);
		iface.getWebDriverElement(Strategy.PLINK, "Record View").click();
		iface.pause(1000);
		iface.interact("pause...");
		iface.getWebDriverElement(Strategy.ID, "le_label_1").dragNDrop(iface.getWebDriverElement(Strategy.ID, "21"));
		iface.interact("pause...");
		iface.focusDefault();
	}
	
	@Ignore
	@Test
	public void cookieTest() throws Exception {
		iface.go("http://orteil.dashnet.org/cookieclicker/");
		WebDriverElement bigCookie = iface.getWebDriverElement(Strategy.ID, "bigCookie");
		String s = Adhoc.getCookieClicks();
		while (Integer.parseInt(s) > 0) {
			for (int i = 0; i < Integer.parseInt(s); i++) {
				bigCookie.click();
			}
			s = Adhoc.getCookieClicks();
		}
	}
		
	private static String getCookieClicks() {
		String s = (String) JOptionPane.showInputDialog(null, 
				"Continue with x clicks...", 
				"Cookie Clicker", 
				JOptionPane.PLAIN_MESSAGE, null,
                null,
                "1000000");
		return s;
	}
	
	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}	
