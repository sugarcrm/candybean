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
package com.sugarcrm.candybean.automation.webdriver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.automation.webdriver.WebDriverSelector;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

//import com.sugarcrm.candybean.IAutomation.Strategy;
//import com.sugarcrm.candybean.automation.VHook;
//import com.sugarcrm.candybean.IAutomation;
//import com.sugarcrm.candybean.Voodoo;

public class WebDriverSelectorSystemTest {
	
	protected static Candybean candybean;
	protected static WebDriverInterface iface;
	
	@BeforeClass
	public static void instantiateCb() throws Exception {
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) candybeanConfigStr = Candybean.CONFIG_DIR.getCanonicalPath() + File.separator + "candybean.config";
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
	}
	
	@Before
	public void first() throws Exception {
		iface = candybean.getWebDriverInterface();
	}
	
	@Test
	public void selectTest() throws Exception {
		String option = "Sep";
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php";
		iface.go(facebookCreateAccountUrl);
		WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.ID, "month"), iface.wd);
		// 2. Select the option 'Sep' from the 'birthday_month' drop-down menu
		select.select(option);
		// 3. Verify that 'Sep' was actually selected
		String actual = select.getAllSelectedOptions().get(0);
		String expected = option;
		Assert.assertEquals(expected, actual);
	}

    @Test
    public void selectMultipleTest() throws Exception {
        String multipleSelectURL = "http://odyniec.net/articles/multiple-select-fields/";
        iface.go(multipleSelectURL);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"), iface.wd);

        Assert.assertTrue(select.isMultiSelector());

        ArrayList<String> options = new ArrayList<>();
        options.add("Cheese");
        options.add("Olives");
        options.add("Green Pepper");

        Assert.assertFalse(select.isSelected());

        select.select(options);

        Assert.assertTrue(select.isSelected(options, true));
    }

    @Test
    public void selectAllTest() throws Exception {
        String multipleSelectURL = "http://odyniec.net/articles/multiple-select-fields/";
        iface.go(multipleSelectURL);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"), iface.wd);

        Assert.assertFalse(select.isSelected());

        select.selectAll();

        Assert.assertTrue(select.isSelected(select.getOptions(), true));
    }

    @Test
    public void deselectTest() throws Exception {
        String multipleSelectURL = "http://odyniec.net/articles/multiple-select-fields/";
        iface.go(multipleSelectURL);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"), iface.wd);

        select.select("Ham");

        Assert.assertTrue(select.isSelected("Ham"));

        select.deselect("Ham");

        Assert.assertFalse(select.isSelected("Ham"));
    }

    @Test
    public void deselectMultiple() throws Exception {
        String multipleSelectURL = "http://odyniec.net/articles/multiple-select-fields/";
        iface.go(multipleSelectURL);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"), iface.wd);

        select.selectAll();

        Assert.assertEquals(select.getAllSelectedOptions().size(), select.getOptions().size());

        List<String> selected = select.getOptions();

        selected.remove("Ham");
        selected.remove("Green Pepper");
        selected.remove("Mushrooms");

        select.deselect("Ham");
        select.deselect("Green Pepper");
        select.deselect("Mushrooms");

        Assert.assertTrue(select.isSelected(selected, true));
    }

    @Test
    public void deselectAllTest() throws Exception {
        String multipleSelectURL = "http://odyniec.net/articles/multiple-select-fields/";
        iface.go(multipleSelectURL);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"), iface.wd);

        Assert.assertFalse(select.isSelected());

        select.select(0);
        select.select(1);
        select.select(2);

        Assert.assertTrue(select.isSelected());
        Assert.assertEquals(select.getAllSelectedOptions().size(), 3);

        select.deselectAll();

        Assert.assertFalse(select.isSelected());
    }
	
	@Test
	public void getSelectedTest() throws Exception {
		String actual;
		String expected = "Month"; // Assuming that we know that the current/default option is 'Month:'
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php";
		iface.go(facebookCreateAccountUrl);
		WebDriverSelector select = iface.getSelect(new Hook(Strategy.ID, "month"));
		// 2. Get the current option from the drop-down list
		actual = select.getFirstSelectedOption();
		// 3. Verify that actual value is the expected value
		Assert.assertEquals(expected, actual);
	}
	
	@After
	public void last() throws Exception {
		iface.stop();
	}
}	