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
import java.util.concurrent.TimeUnit;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.runner.VRunner;
import org.openqa.selenium.WebDriver;

@RunWith(VRunner.class)
public class WebDriverSystemTest {

	private WebDriverInterface iface;

	final String testPage1 = "file://"+ System.getProperty("user.dir")+"/resources/html/test/testPlayground.html";
	final String testPage2 = "file://"+ System.getProperty("user.dir")+"/resources/html/test/onOffScreen.html";

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
	
	@Test
	public void backwardForwardRefreshTest() throws Exception {
		iface.go(testPage1);
		iface.go(testPage2);
		iface.go(testPage1);
		assertEquals(testPage1, iface.getURL());
		iface.backward();
		iface.pause(1000);
		assertEquals(testPage2, iface.getURL());
		iface.backward();
		iface.pause(1000);
		assertEquals(testPage1, iface.getURL());
		iface.forward();
		iface.pause(1000);
		assertEquals(testPage2, iface.getURL());
		iface.forward();
		iface.pause(1000);
		assertEquals(testPage1, iface.getURL());
		iface.refresh(); // refreshing only at end; mid-refreshes crash in Chrome
		iface.pause(2000);
		assertEquals(testPage1, iface.getURL());
	}

	@Test
	public void screenshotTest() throws Exception {
		File screenshotFile = new File(Candybean.ROOT_DIR + File.separator + "screenshot.png");
		iface.go(testPage1);
		iface.screenshot(screenshotFile);
		assertTrue(screenshotFile.exists());
		screenshotFile.delete();
	}

	@Ignore
	@Test
	public void startStopRestartTest() throws Exception {
		iface.go(testPage1);
		String actUrl = iface.getURL();
		assertEquals(testPage1, actUrl);
		iface.stop();
		iface = new FirefoxInterface();
		iface.go(testPage1);
		actUrl = iface.getURL();
		assertEquals(testPage1, actUrl);
		iface.restart();
		iface.go(testPage1);
		actUrl = iface.getURL();
		assertEquals(testPage1, actUrl);
		iface.stop();
		iface = new ChromeInterface();
		iface.go(testPage1);
		actUrl = iface.getURL();
		assertEquals(testPage1, actUrl);
		iface.stop();
		try {
			thrown.expect(Exception.class);
			thrown.expectMessage("Automation interface not yet started; cannot restart.");
			iface.restart();
		} finally {
			iface.start();
		}
	}

	@Ignore("CB-259: Chromedriver")
	@Test
	public void openCloseWindowTest() throws Exception {
		iface.go(testPage1);
		assertEquals(testPage1, iface.wd.getCurrentUrl());
		iface.openWindow(testPage2);
		assertEquals(testPage2, iface.wd.getCurrentUrl());
		iface.focusWindow(0);
		assertEquals(testPage1, iface.wd.getCurrentUrl());
		iface.focusWindow(1);
		assertEquals(testPage2, iface.wd.getCurrentUrl());
		iface.closeWindow();
		assertEquals(testPage1, iface.wd.getCurrentUrl());
	}

	@Test
	public void presentAcceptDismissDialogTest() throws Exception {
		iface.go(testPage1);
		
		// dialog not yet visible
		assertFalse(iface.isDialogVisible());
		
		// clicking; alert should be visible and window inactive
		iface.getWebDriverElement(Strategy.ID, "newAlert").click();
		iface.pause(1000);
		assertTrue(iface.isDialogVisible());
		
		// accepting alert dialog; should be gone
		iface.acceptDialog();
		assertFalse(iface.isDialogVisible());
		
		// Dismiss not available in Chrome
		if (!(iface instanceof ChromeInterface)) {
			iface.getWebDriverElement(Strategy.ID, "newAlert").click();
			assertTrue(iface.isDialogVisible());
			iface.dismissDialog();
			assertFalse(iface.isDialogVisible());
		}
	}

	@Test
	public void containsTest() throws Exception {
		iface.go(testPage1);
		Assert.assertTrue(iface.contains("Click button", true));
		Assert.assertTrue(iface.contains("cLiCk BuTtOn", false));
		Assert.assertTrue(iface.contains("Click button", false));
		Assert.assertFalse(iface.contains("cLiCk BuTtOn", true));

		Assert.assertFalse(iface.contains("Doesn't contain this", true));
		Assert.assertFalse(iface.contains("Doesn't contain this", true));
	}

	@Test
	public void focusFrameTest() throws Exception {
		String mainText = "Click button to hide me";
		String iframeText = "This goes inside the iframe";
		iface.go(testPage1);
		WebDriverElement mainFramePara = iface.getWebDriverElement(Strategy.ID, "writing");
		assertEquals(mainText, mainFramePara.getText());

		// switch focus to frame by index
		iface.focusFrame(0);
		WebDriverElement iframePara = iface.getWebDriverElement(Strategy.ID, "iframePara");
		assertEquals(iframeText, iframePara.getText());
		
		// switch to default focus
		iface.focusDefault();
		mainFramePara = iface.getWebDriverElement(Strategy.ID, "writing");
		assertEquals(mainText, mainFramePara.getText());

		// switch focus to frame by name
		iface.focusFrame("Test_iframe");
		iframePara = iface.getWebDriverElement(Strategy.ID, "iframePara");
		assertEquals(iframeText, iframePara.getText());

		// switch to default focus
		iface.focusDefault();
		mainFramePara = iface.getWebDriverElement(Strategy.ID, "writing");
		assertEquals(mainText, mainFramePara.getText());

		// switch to focus by control
		iface.focusFrame(new WebDriverElement(Strategy.ID, "Test_iframe", iface.wd));
		iframePara = iface.getWebDriverElement(Strategy.ID, "iframePara");
		assertEquals(iframeText, iframePara.getText());

		// switch to default focus
		iface.focusDefault();
		mainFramePara = iface.getWebDriverElement(Strategy.ID, "writing");
		assertEquals(mainText, mainFramePara.getText());
	}

	@Ignore("CB-263: Candybean does not properly model windows")
	@Test
	public void focusWindowTest() throws Exception {
		String mainWindowTitle = "Candybean Test Page";
		String altWindowTitle = "Candybean Test Page 2";

		iface.go(testPage1);

		// Opens a window in a new tab
		iface.getWebDriverElement(Strategy.PLINK, "Open in new window").click();
		iface.getWebDriverElement(Strategy.PLINK, "Open in new window").click();

		// Verify title without switching
		assertEquals(mainWindowTitle, iface.wd.getTitle());

		// Verify title with switching
		iface.focusWindow(1);
		Thread.sleep(1000);
		assertEquals(altWindowTitle, iface.wd.getTitle());

		// Close window which should auto-focus to previous window; verify title
		iface.closeWindow();
		assertEquals(mainWindowTitle, iface.wd.getTitle());

		iface.getWebDriverElement(Strategy.PLINK, "Open in new window").click();
		iface.focusWindow(altWindowTitle);
		assertEquals(altWindowTitle, iface.wd.getTitle());
		assertEquals(testPage2, iface.getURL());

		// Verify URL with switching to window by URL
		iface.focusWindow(testPage1);
		assertEquals(mainWindowTitle, iface.wd.getTitle());

		// Close window and revert to previous window (1 index); verify URL
		iface.closeWindow();
		assertEquals(testPage2, iface.getURL());

		// Verify errors by switching to erroneous window titles & indices
		try {
			iface.focusWindow("garbage");
			assertTrue("Test did not fail when expected", false);
		} catch (CandybeanException e) {
			assertEquals("The given focus window string matched no title or URL: garbage",
					e.getMessage());
		}
		try {
			iface.focusWindow(-1);
			assertTrue("Test did not fail when expected", false);
		} catch (CandybeanException e) {
			assertEquals("Given focus window index is out of bounds: -1; current size: 1",
					e.getMessage());
		}
		try {
			iface.focusWindow(0);
			assertTrue("Test did not fail when expected", false);
		} catch (CandybeanException e) {
			assertEquals("Given focus window index is out of bounds: 1; current size: 1",
					e.getMessage());
		}
	}

	@Test
	public void executeJavaScriptTest() throws Exception {
		// 0 arguments
		String javascript = "alert('Alert!')";
		iface.executeJavascript(javascript);
		assertTrue(iface.wd.switchTo().alert().getText().contains("Alert!"));
		iface.acceptDialog();

		// 1 argument
		javascript = "alert(arguments[0])";
		String arg = "one";
		iface.executeJavascript(javascript, arg);
		assertTrue(iface.wd.switchTo().alert().getText().contains(arg));
		iface.acceptDialog();

		// multiple arguments
		javascript = "alert(arguments[0] + ' and ' + arguments[1])";
		String[] args = { "two", "three" };
		iface.executeJavascript(javascript, args);
		assertTrue(iface.wd.switchTo().alert().getText().contains(args[0]));
		assertTrue(iface.wd.switchTo().alert().getText().contains(args[1]));
		iface.acceptDialog();

		Long toReturn = 12l;
		javascript = "return " + toReturn + ";";
		Long returnValue = (Long)(iface.executeJavascript(javascript));
		assertEquals("Javascript return value incorrect.  Expected: " + toReturn +
				"   Found: " + returnValue, toReturn, returnValue);

	}

    @Test
	public void executeAsyncJavaScriptTest() throws Exception {
        iface.wd.manage().timeouts().setScriptTimeout(1500, TimeUnit.MILLISECONDS);

		// 0 arguments
        long start = System.currentTimeMillis();
		String javascript = "window.setTimeout(arguments[arguments.length - 1], 500);";
		iface.executeAsyncJavascript(javascript);

        // Assert that the test waited for at least 500ms
        assertTrue(System.currentTimeMillis() >= start + 500);

		// 1 argument
        start = System.currentTimeMillis();
		javascript = "window.setTimeout(arguments[arguments.length - 1], arguments[0]);";
		iface.executeAsyncJavascript(javascript, 1000);

        // Assert that the test waited for at least 1000ms
        assertTrue(System.currentTimeMillis() >= start + 1000);

        // multiple arguments
        start = System.currentTimeMillis();
        javascript = "window.setTimeout(arguments[arguments.length - 1], arguments[0]-arguments[1]);";

		iface.executeAsyncJavascript(javascript, 1000, 400);
        assertTrue(System.currentTimeMillis() >= start + 600);

		String toReturn = "Hello World!";
		javascript = "window.setTimeout(arguments[arguments.length-1] (\"Hello World!\"), 100);";
		String returnValue = (String)(iface.executeAsyncJavascript(javascript));
		assertEquals("Javascript return value incorrect.  Expected: " + toReturn +
				"   Found: " + returnValue, toReturn, returnValue);
	}
}