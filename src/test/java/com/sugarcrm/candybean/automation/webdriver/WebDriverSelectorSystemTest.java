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

import java.util.ArrayList;
import java.util.List;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.webdriver.WebDriverSelector;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class WebDriverSelectorSystemTest {

	private WebDriverInterface iface;
	
	@Before
	public void setUp() throws Exception {
		Configuration config = TestConfiguration.getTestConfiguration("systemtest.webdriver.config");
		Candybean candybean = Candybean.getInstance(config);
		AutomationInterfaceBuilder builder = candybean.getAIB(WebDriverSelectorSystemTest.class);
		builder.setType(Type.CHROME);
		iface = builder.build();
		iface.start();
	}

	@After
	public void tearDown() throws CandybeanException {
		iface.stop();
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
        iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/multipleSelect.html");
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/select"), iface.wd);

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
        iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/multipleSelect.html");
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/select"), iface.wd);

        Assert.assertFalse(select.isSelected());

        select.selectAll();

        Assert.assertTrue(select.isSelected(select.getOptions(), true));
    }

    @Test
    public void deselectTest() throws Exception {
        iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/multipleSelect.html");
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/select"), iface.wd);

        select.select("Ham");

        Assert.assertTrue(select.isSelected("Ham"));

        select.deselect("Ham");

        Assert.assertFalse(select.isSelected("Ham"));
    }

    @Test
    public void deselectMultiple() throws Exception {
        iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/multipleSelect.html");
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/select"), iface.wd);

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
        iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/multipleSelect.html");
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/select"), iface.wd);

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

}	