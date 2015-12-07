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
import org.junit.*;
import org.junit.runner.RunWith;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class WebDriverSelectorSystemTest {

	private WebDriverInterface iface;
    String testPage = "file://"+ System.getProperty("user.dir")+"/resources/html/test/testPlayground.html";

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

    @Ignore("Requires a Chromedriver update, see CB-259")
	@Test
	public void selectTest() throws Exception {
        iface.go(testPage);
		WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.ID, "select"), iface.wd);
		select.select("Option 2");
		Assert.assertEquals("Option 2", select.getAllSelectedOptions().get(0));
	}

    @Ignore("Requires a Chromedriver update, see CB-259")
    @Test
    public void selectMultipleTest() throws Exception {
        iface.go(testPage);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/div/select"), iface.wd);

        Assert.assertTrue(select.isMultiSelector());

        ArrayList<String> options = new ArrayList<>();
        options.add("Cheese");
        options.add("Olives");
        options.add("Green Pepper");

        Assert.assertFalse(select.isSelected());

        select.select(options);

        Assert.assertTrue(select.isSelected(options, true));
    }

    @Ignore("Requires a Chromedriver update, see CB-259")
    @Test
    public void selectAllTest() throws Exception {
        iface.go(testPage);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/div/select"), iface.wd);

        Assert.assertFalse(select.isSelected());
        select.selectAll();
        Assert.assertTrue(select.isSelected(select.getOptions(), true));
    }

    @Ignore("Requires a Chromedriver update, see CB-259")
    @Test
    public void deselectTest() throws Exception {
        iface.go(testPage);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/div/select"), iface.wd);

        select.select("Ham");
        Assert.assertTrue(select.isSelected("Ham"));
        select.deselect("Ham");
        Assert.assertFalse(select.isSelected("Ham"));
    }

    @Ignore("Requires a Chromedriver update, see CB-259")
    @Test
    public void deselectMultiple() throws Exception {
        iface.go(testPage);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/div/select"), iface.wd);

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

    @Ignore("Requires a Chromedriver update, see CB-259")
    @Test
    public void deselectAllTest() throws Exception {
        iface.go(testPage);
        WebDriverSelector select = new WebDriverSelector(new Hook(Strategy.XPATH, "/html/body/div/select"), iface.wd);

        Assert.assertFalse(select.isSelected());

        select.select(0);
        select.select(1);
        select.select(2);

        Assert.assertTrue(select.isSelected());
        Assert.assertEquals(select.getAllSelectedOptions().size(), 3);

        select.deselectAll();

        Assert.assertFalse(select.isSelected());
    }
}