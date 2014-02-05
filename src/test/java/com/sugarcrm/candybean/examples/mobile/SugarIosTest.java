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
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteTouchScreen;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;
import com.sugarcrm.candybean.examples.ITest;

/**
 * Simple <a href="https://github.com/appium/appium">Appium</a> test which runs against an Appium server deployed
 * with the Sugar Mobile iOS app.
 *
 * @author Larry Cao
 */
public class SugarIosTest extends IOsTest implements ITest{

    @Before
    public void setUp() throws Exception {
        wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        wd.quit();
    }

    @Test
    public void testLogin() throws Exception {
        WebElement username = wd.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/textfield[1]"));
        assertTrue(username.isDisplayed());

        Set<String> handles = wd.getWindowHandles();

        for (String s : handles) {
            System.out.println(s);
        }

        if (!username.getText().equals("")) {
            username.clear();
        }
        username.sendKeys("admin");


        WebElement password = wd.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/secure[1]"));

        assertTrue(password.isDisplayed());

        password.click();

        password.sendKeys("asdf");

        WebElement login = wd.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/link[1]"));

        login.click();

        Thread.sleep(1000000);
    }

    public static class SwipeableWebDriver extends RemoteWebDriver implements HasTouchScreen {
        private RemoteTouchScreen touch;

        public SwipeableWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
            super(remoteAddress, desiredCapabilities);
            touch = new RemoteTouchScreen(getExecuteMethod());
        }

        public TouchScreen getTouch() {
            return touch;
        }
    }
}
