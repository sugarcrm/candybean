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
package com.sugarcrm.candybean.examples.yelp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.examples.yelp.YelpUser.YelpUserBuilder;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public class YelpTest {

	/**
	 * Contains methods for yelp test
	 */
	private static Yelp yelp;
	
	public static WebDriverInterface iface;

	@BeforeClass
	public static void beforeClass() throws CandybeanException{
		Candybean candybean = Candybean.getInstance();
		AutomationInterfaceBuilder builder = candybean.getAIB(YelpTest.class);
		builder.setType(Type.CHROME);
		iface = builder.build();
	}

	@Test
	public void yelpLoginLogoutTest() throws Exception {
		yelp.login();
		yelp.logout();
	}

	@Test
	public void yelpRandomTest() throws Exception {
		int timeout_in_minutes = 10;
		yelp.run(timeout_in_minutes);
	}

	@Before
	public void setUp() throws CandybeanException {
		String yelpHooksStr = System.getProperty("yelp.hooks", Candybean.ROOT_DIR + File.separator + "yelp.hooks");
		Properties yelpHooks = new Properties();
		try {
			yelpHooks.load(new FileInputStream(new File(yelpHooksStr)));
		} catch (Exception e) {
			throw new CandybeanException(e);
		}
		YelpUser user = new YelpUserBuilder("Sugar", "Stevens", "95014",
				"cwarmbold@sugarcrm.com", "Sugar123!").build();
		iface.start();
		yelp = new Yelp(iface, yelpHooks, user);
		yelp.start();
	}

	@After
	public void tearDown() throws CandybeanException {
		yelp.stop();
		iface.stop();
	}

}
