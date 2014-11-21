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

import static org.junit.Assert.*;
import java.io.File;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.automation.webdriver.ChromeInterface;
import com.sugarcrm.candybean.automation.webdriver.FirefoxInterface;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class WebDriverSystemTest {

	private WebDriverInterface iface;
	
	@Before
	public void setUp() throws Exception {
		Configuration config = TestConfiguration.getTestConfiguration("systemtest.webdriver.config");
		Candybean candybean = Candybean.getInstance(config);
		AutomationInterfaceBuilder builder = candybean.getAIB(WebDriverSystemTest.class);
		builder.setType(Type.CHROME);
		iface = builder.build();
		iface.start();
	}

	@After
	public void tearDown() throws CandybeanException {
		iface.stop();
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
//	@Ignore
	@Test
	public void backwardForwardRefreshTest() throws Exception {
		String url1 = "https://www.google.com/";
		String url2 = "http://www.wikipedia.org/";
		String url3 = "http://www.reddit.com/";
		iface.go(url1);
		iface.go(url2);
		iface.go(url3);
		assertEquals(url3, iface.getURL());
		iface.backward();
		iface.pause(1000);
		assertEquals(url2, iface.getURL());
		iface.backward();
		iface.pause(1000);
		assertEquals(url1, iface.getURL());
		iface.forward();
		iface.pause(1000);
		assertEquals(url2, iface.getURL());
		iface.forward();
		iface.pause(1000);
		assertEquals(url3, iface.getURL());		
		iface.refresh(); // refreshing only at end; mid-refreshes crash in Chrome
		iface.pause(2000);
		assertEquals(url3, iface.getURL());		
	}

//	@Ignore
	@Test
	public void screenshotTest() throws Exception {
		File screenshotFile = new File(Candybean.ROOT_DIR + File.separator + "screenshot.png");
		String url = "https://www.google.com/";
		iface.go(url);
		iface.screenshot(screenshotFile);
		assertTrue(screenshotFile.exists());
		screenshotFile.delete();
	}

	@Ignore
	@Test
	public void startStopRestartTest() throws Exception {
		String expUrl = "https://www.google.com/";
		iface.go(expUrl);
		String actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		iface = new FirefoxInterface();
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.restart();
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		iface = new ChromeInterface();
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		try {
			thrown.expect(Exception.class);
			thrown.expectMessage("Automation interface not yet started; cannot restart.");
			iface.restart();
		} finally {
			iface.start();
		}
	}

	@Ignore
	@Test
	public void closeWindowTest() throws Exception {
//		this.iface.closeWindow();
	}

	@Ignore
	@Test
	public void goTest() throws Exception {
//		this.iface.go("");
	}

	@Ignore
	@Test
	public void presentAcceptDismissDialogTest() throws Exception {
		iface.go("http://www.mediacollege.com/internet/javascript/basic/alert.html");
		
		// dialog not yet visible
		assertFalse(iface.isDialogVisible());
		
		// clicking; alert should be visible and window inactive
		iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"content\"]/p[2]/input").click();
		assertTrue(iface.isDialogVisible());
		
		// accepting alert dialog; should be gone
		iface.acceptDialog();
		assertFalse(iface.isDialogVisible());
		
		// Dismiss not available in Chrome
		if (!(iface instanceof ChromeInterface)) {
			iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"content\"]/p[2]/input").click();
			assertTrue(iface.isDialogVisible());
			iface.dismissDialog();
			assertFalse(iface.isDialogVisible());
		}
	}

//	@Ignore
	@Test
	public void containsTest() throws Exception {
		iface.go("https://code.google.com/");
		boolean actCaseSensPos = iface.contains("Google Developers", true); //true
		boolean actCaseSensNeg = iface.contains("google developers", true); //false
		boolean actNeg = iface.contains("goggle devs", false); //false
		assertEquals(true, actCaseSensPos);
		assertEquals(false, actCaseSensNeg);
		assertEquals(false, actNeg);
	}
	
	@Ignore
	@Test
	public void focusDefaultTest() throws Exception {
//		this.iface.focusDefault();
	}

//	@Ignore
	@Test
	public void focusFrameTest() throws Exception {
		String expDefStr = "The magic of iframes";
		String expFrmStr = "http://www.littlewebhut.com/images/eightball.gif";
		iface.go("http://www.littlewebhut.com/articles/html_iframe_example/");
		String actDefStr = iface.getWebDriverElement(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
		
		// switch focus to frame by index
		iface.focusFrame(1);
//		System.out.println("SOURCE:\n" + iface.wd.getPageSource());
		String actFrmStr = iface.getWebDriverElement(Strategy.TAG, "img").getAttribute("src");
		assertEquals(expFrmStr, actFrmStr);
		
		// switch to default focus
		iface.focusDefault();
		actDefStr = iface.getWebDriverElement(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
		
		// switch focus to frame by name
		iface.focusFrame("imgbox");
		actFrmStr = iface.getWebDriverElement(Strategy.TAG, "img").getAttribute("src");
		assertEquals(expFrmStr, actFrmStr);
		
		// switch to default focus
		iface.focusDefault();
		actDefStr = iface.getWebDriverElement(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
		
		// switch to focus by control
		iface.focusFrame(new WebDriverElement(Strategy.ID, "imgbox", iface.wd));
		actFrmStr = iface.getWebDriverElement(Strategy.TAG, "img").getAttribute("src");
		assertEquals(expFrmStr, actFrmStr);
		
		// switch to default focus
		iface.focusDefault();
		actDefStr = iface.getWebDriverElement(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
	}

//	@Ignore
	@Test
	public void focusWindowTest() throws Exception {
		String expWindow0Title = "EchoEcho.Com Tools - Tools";
		String expWindow0URL = "http://www.echoecho.com/toolpopupgenerator.htm";
		String expWindow1Title = "Yahoo";
		String expWindow1URL = "https://www.yahoo.com/";
		String expWindow2Title = "Popup Windows : Example - JavaScript Tutorial - EchoEcho.Com - Beginners best choice!";
		String expWindow2URL = "http://www.echoecho.com/jswindows03.htm";
		String expWindow3Title = "Yahoo";
		String expWindow3URL = "https://www.yahoo.com/";
		
		iface.go(expWindow0URL);
	
		// Check assumptions
		assertEquals(expWindow0Title, iface.wd.getTitle());
		
		// Click pops-up window titled "Tryit Editor v1.9"
		iface.getWebDriverElement(Strategy.NAME, "B1").click();
		
		// Verify title without switching
		assertEquals(expWindow0Title, iface.wd.getTitle());
		
		// Verify title with switching
		iface.focusWindow(1);
		assertEquals(expWindow1Title, iface.wd.getTitle());
		
		// Verify title with switching
		iface.focusWindow(0);
		assertEquals(expWindow0Title, iface.wd.getTitle());
		
		// Navigate elsewhere and trigger popup window
		iface.go(expWindow2URL);
//		iface.interact("window focus after go: " + iface.wd.getWindowHandle());
		assertEquals(expWindow2Title, iface.wd.getTitle());
		((WebDriverElement)iface.getWebDriverElement(Strategy.CLASS, "main").getElement(new Hook(Strategy.TAG, "a"), 0)).click();
//		iface.interact(iface.getWindowsString());
				
		// Verify URL with switching to window by title
		iface.focusWindow(expWindow1Title);
		String actWindowURL = iface.getURL();
		assertEquals(expWindow1URL, actWindowURL);
//		iface.interact(iface.getWindowsString());
		
		// Verify URL with switching to window by URL
		iface.focusWindow(2);
		assertEquals(expWindow3Title, iface.wd.getTitle());
//		iface.interact(iface.getWindowsString());
		
		// Close window and revert to previous window (1 index); verify URL
		iface.closeWindow();
		actWindowURL = iface.getURL();
		assertEquals(expWindow1URL, actWindowURL);
//		iface.interact(iface.getWindowsString());
		
		// Close window and revert to previous window (0 index); verify URL
		iface.closeWindow();
		actWindowURL = iface.getURL();
		assertEquals(expWindow2URL, actWindowURL);
//		iface.interact(iface.getWindowsString());
		
		// Verify error by switching to erroneous window titles & indices
		thrown.expect(Exception.class);
		thrown.expectMessage("The given focus window string matched no title or URL: garbage");
		iface.focusWindow("garbage");
		thrown.expectMessage("Given focus window index is out of bounds: -1 current size: 1");
		iface.focusWindow(-1);
		thrown.expectMessage("Given focus window index is out of bounds: 1 current size: 1");
		iface.focusWindow(1);
//		iface.interact(iface.getWindowsString());
	}

	@Ignore
	@Test
	public void maximizeTest() {
//		this.iface.maximize();
	}

	@Ignore
	@Test
	public void getControlTest() throws Exception {
//		this.iface.getWebDriverElement(null);
	}

	@Ignore
	@Test
	public void getSelectTest() throws Exception {
//		this.iface.getSelect(null);
	}

}