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
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import org.junit.Test;

import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.control.VHook;
import com.sugarcrm.candybean.automation.control.VSelect;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.test.AbstractTest;
import com.sugarcrm.candybean.utilities.Utils;

//import com.sugarcrm.candybean.IAutomation.Strategy;
//import com.sugarcrm.candybean.automation.VHook;
//import com.sugarcrm.candybean.IAutomation;
//import com.sugarcrm.candybean.Voodoo;

public class VSelectSystemTest {
	
	protected static Candybean candybean;
	protected static VInterface iface;
	
	@BeforeClass
	public static void first() throws Exception {
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) candybeanConfigStr = AbstractTest.CONFIG_DIR.getCanonicalPath() + File.separator + "candybean.config";
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		iface = candybean.getInterface();
		iface.start();
	}
	
	@Test
	public void selectTest() throws Exception {
		String option = "Sep";
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php";
		iface.go(facebookCreateAccountUrl);
		VSelect select = new VSelect(candybean, iface, new VHook(Strategy.ID, "month"));
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
        VSelect select = new VSelect(candybean, iface, new VHook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"));

        Assert.assertTrue(select.isMultiple());

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
        VSelect select = new VSelect(candybean, iface, new VHook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"));

        Assert.assertFalse(select.isSelected());

        select.selectAll();

        Assert.assertTrue(select.isSelected(select.getOptions(), true));
    }

    @Test
    public void deselectTest() throws Exception {
        String multipleSelectURL = "http://odyniec.net/articles/multiple-select-fields/";
        iface.go(multipleSelectURL);
        VSelect select = new VSelect(candybean, iface, new VHook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"));

        select.select("Ham");

        Assert.assertTrue(select.isSelected("Ham"));

        select.deselect("Ham");

        Assert.assertFalse(select.isSelected("Ham"));
    }

    @Test
    public void deselectMultiple() throws Exception {
        String multipleSelectURL = "http://odyniec.net/articles/multiple-select-fields/";
        iface.go(multipleSelectURL);
        VSelect select = new VSelect(candybean, iface, new VHook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"));

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
        VSelect select = new VSelect(candybean, iface, new VHook(Strategy.XPATH, "//*[@id=\"content\"]/div[4]/select"));

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
		VSelect select = iface.getSelect(new VHook(Strategy.ID, "month"));
		// 2. Get the current option from the drop-down list
		actual = select.getFirstSelectedOption();
		// 3. Verify that actual value is the expected value
		Assert.assertEquals(expected, actual);
	}
	
	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}	
