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
package com.sugarcrm.candybean.automation;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sugarcrm.candybean.automation.VInterface.Type;
import com.sugarcrm.candybean.automation.control.VControl;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.CB;
import com.sugarcrm.candybean.utilities.Utils;

public class VInterfaceSystemTest {

	protected static Candybean candybean;
	protected static VInterface iface;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void first() throws Exception {
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) candybeanConfigStr = CB.CONFIG_DIR.getCanonicalPath() + File.separator + "candybean.config";
		System.out.println("candybeanConfigPath: " + candybeanConfigStr);
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		iface = candybean.getInterface();
		iface.start();
	}
	
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
		Thread.sleep(1000);
		assertEquals(url2, iface.getURL());
		iface.backward();
		Thread.sleep(1000);
		assertEquals(url1, iface.getURL());
		iface.forward();
		Thread.sleep(1000);
		assertEquals(url2, iface.getURL());
		iface.forward();
		Thread.sleep(1000);
		assertEquals(url3, iface.getURL());		
		iface.refresh(); // refreshing only at end; mid-refreshes crash in Chrome
		Thread.sleep(1000);
		assertEquals(url3, iface.getURL());		
	}
	
//	@Ignore
	@Test
	public void screenshotTest() throws Exception {
		File screenshotFile = new File(CB.CONFIG_DIR.getCanonicalPath() + File.separator + "screenshot.png");
		String url = "https://www.google.com/";
		iface.go(url);
		iface.screenshot(screenshotFile);
		assertTrue(screenshotFile.exists());
		screenshotFile.delete();
	}

//	@Ignore
	@Test
	public void startStopRestartTest() throws Exception {
		String expUrl = "https://www.google.com/";
		iface.go(expUrl);
		String actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		iface.start(Type.FIREFOX);
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.restart();
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		iface.start(Type.CHROME);
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

	@Test
	public void presentAcceptDismissDialogTest() throws Exception {
		iface.go("http://www.mediacollege.com/internet/javascript/basic/alert.html");
		
		// dialog not yet visible
		assertFalse(iface.isDialogVisible());
		
		// clicking; alert should be visible and window inactive
		iface.getControl(Strategy.XPATH, "//*[@id=\"content\"]/p[2]/input").click();
		assertTrue(iface.isDialogVisible());
		
		// accepting alert dialog; should be gone
		iface.acceptDialog();
		assertFalse(iface.isDialogVisible());
		
		// Dismiss not available in Chrome
		if (!iface.getType().equals(Type.CHROME)) {
			iface.getControl(Strategy.XPATH, "//*[@id=\"content\"]/p[2]/input").click();
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
		String actDefStr = iface.getControl(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
		
		// switch focus to frame by index
		iface.focusFrame(1);
//		System.out.println("SOURCE:\n" + iface.wd.getPageSource());
		String actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		assertEquals(expFrmStr, actFrmStr);
		
		// switch to default focus
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
		
		// switch focus to frame by name
		iface.focusFrame("imgbox");
		actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		assertEquals(expFrmStr, actFrmStr);
		
		// switch to default focus
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
		
		// switch to focus by control
		iface.focusFrame(new VControl(candybean, iface, Strategy.ID, "imgbox"));
		actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		assertEquals(expFrmStr, actFrmStr);
		
		// switch to default focus
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "h2").getText();
		assertEquals(expDefStr, actDefStr);
	}

//	@Ignore
	@Test
	public void focusWindowTest() throws Exception {
		String expWindow0Title = "HTML Examples";
		String expWindow0URL = "http://www.w3schools.com/html/html_examples.asp";
		String expWindow1Title = "Tryit Editor v2.2";
		String expWindow1URL = "http://www.w3schools.com/html/tryit.asp?filename=tryhtml_basic_document";
		String expWindow2Title = "HTML Popup Windows - HTML Code Tutorial";
		String expWindow2URL = "http://www.htmlcodetutorial.com/linking/linking_famsupp_70.html";
		String expWindow3Title = "Popup Window - HTML Code Tutorial";
		String expWindow3URL = "http://www.htmlcodetutorial.com/linking/popup_test_a.html";
		
		iface.go(expWindow0URL);
	
		// Check assumptions
		assertEquals(expWindow0Title, iface.wd.getTitle());
		
		// Click pops-up window titled "Tryit Editor v1.8"
		iface.getControl(Strategy.PLINK, "HTML document").click();
		
		// Verify title without switching
		assertEquals(expWindow0Title, iface.wd.getTitle());
		
		// Verify title with switching
		iface.focusWindow(1);
		assertEquals(expWindow1Title, iface.wd.getTitle());
//		iface.interact(iface.getWindowsString());
		
		// Close window which should auto-focus to previous window; verify title
		iface.closeWindow();
		assertEquals(expWindow0Title, iface.wd.getTitle());
//		iface.interact(iface.getWindowsString());
		
		// Click pop-up window titled "Tryit Editor v1.8"
		iface.getControl(Strategy.PLINK, "HTML document").click();
		
		// Navigate elsewhere and trigger popup window
//		iface.interact("window focus before go: " + iface.wd.getWindowHandle());
		iface.go(expWindow2URL);
//		iface.interact("window focus after go: " + iface.wd.getWindowHandle());
		iface.getControl(Strategy.PLINK, "this link").click();
//		iface.interact(iface.getWindowsString());
		
		// Verify title with (not) switching to current window by index
		iface.focusWindow(0);
		assertEquals(expWindow2Title, iface.wd.getTitle());
//		iface.interact(iface.getWindowsString());
				
		// Verify URL with switching to window by title
		iface.focusWindow(expWindow1Title);
		String actWindowURL = iface.getURL();
		assertEquals(expWindow1URL, actWindowURL);
//		iface.interact(iface.getWindowsString());
		
		// Verify URL with switching to window by URL
		iface.focusWindow(expWindow3URL);
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
	}
	
	@Ignore
	@Test
	public void maximizeTest() {
//		this.iface.maximize();
	}

	@Ignore
	@Test
	public void getControlTest() throws Exception {
//		this.iface.getControl(null);
	}

	@Ignore
	@Test
	public void getSelectTest() throws Exception {
//		this.iface.getSelect(null);
	}

	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}
