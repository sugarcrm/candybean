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
package com.sugarcrm.candybean.examples.mobile;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Simple <a href="https://github.com/appium/appium">Appium</a> test which runs against an Appium server deployed
 * with the Sugar Mobile iOS app.
 *
 * @author Larry Cao
 */
public class SugarIosTest {
	
	public static WebDriverInterface iface;

	@BeforeClass
	public static void beforeClass() throws CandybeanException{
		Candybean candybean = Candybean.getInstance();
		AutomationInterfaceBuilder builder = candybean.getAIB(SugarIosTest.class);
		builder.setType(Type.IOS);
		iface = builder.build();
	}
	
	@Before
	public void setUp() throws CandybeanException {
		iface.start();
        iface.wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@After
	public void tearDown() throws CandybeanException {
		iface.stop();
	}

    @Test
    public void testLogin() throws CandybeanException {
        WebElement username = iface.wd.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/textfield[1]"));
        assertTrue(username.isDisplayed());
        Set<String> handles = iface.wd.getWindowHandles();
        for (String s : handles) {
            System.out.println(s);
        }
        if (!username.getText().equals("")) {
            username.clear();
        }
        username.sendKeys("admin");
        WebElement password = iface.wd.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/secure[1]"));
        assertTrue(password.isDisplayed());
        password.click();
        password.sendKeys("asdf");
        WebElement login = iface.wd.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/link[1]"));
        login.click();
        iface.pause(1000000);
    }
}
