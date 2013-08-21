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
package com.sugarcrm.candybean.automation.mobile;

import org.junit.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.interactions.touch.TouchActions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.control.VHook;
import com.sugarcrm.candybean.automation.control.VSelect;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

/**
 * Simple <a href="https://github.com/appium/appium">Appium</a> test which runs against an Appium server deployed
 * with the Sugar Mobile android app.
 *
 * @author Larry Cao
 */
public class SugarAndroidTest {

    private WebDriver driver;

    private List<Integer> values;

    private static final int MINIMUM = 0;
    private static final int MAXIMUM = 10;

    @Before
    public void setUp() throws Exception {
        // set up appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");

        capabilities.setCapability(CapabilityType.VERSION, "4.2.2");

        capabilities.setCapability("device", "Android");
        capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
        capabilities.setCapability("app", "https://s3.amazonaws.com/voodoo2/SugarCRM.apk.zip");
        capabilities.setCapability("app-package", "com.example.TestApp");
        capabilities.setCapability("app-activity", "MyActivity");

        driver = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        values = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }


    @Test
    public void testLogin() throws Exception {
        WebElement username = driver.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/textfield[1]"));
        assertTrue(username.isDisplayed());

        Set<String> handles = driver.getWindowHandles();

        for (String s : handles) {
            System.out.println(s);
        }

        if (!username.getText().equals("")) {
            username.clear();
        }
        username.sendKeys("admin");


        WebElement password = driver.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/secure[1]"));

        assertTrue(password.isDisplayed());

        password.click();

        password.sendKeys("asdf");

        WebElement login = driver.findElement(By.xpath("//window[1]/scrollview[1]/webview[1]/link[1]"));

        login.click();

        Thread.sleep(1000000);
    }


    public class SwipeableWebDriver extends RemoteWebDriver implements HasTouchScreen {
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
