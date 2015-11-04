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

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class WebDriverElementSystemTest {
	private WebDriverInterface iface;
	final String testPage = "file://"+ System.getProperty("user.dir")+"/resources/html/test/testPlayground.html";

	@Before
	public void setUp() throws Exception {
		Configuration config = TestConfiguration.getTestConfiguration("systemtest.webdriver.config");
		Candybean candybean = Candybean.getInstance(config);
		AutomationInterfaceBuilder builder = candybean.getAIB(WebDriverElementSystemTest.class);
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
	public void getAttributeTest() throws Exception {
		iface.go(testPage);
		WebDriverElement element = iface.getWebDriverElement(Strategy.ID, "toggleWriting");
		Assert.assertEquals("button", element.getAttribute("type"));
	}


	@Test
	public void getElementTest() throws Exception {
		iface.go(testPage);
		WebDriverElement div = iface.getWebDriverElement(Strategy.ID, "writingDiv");
		WebDriverElement element = ((WebDriverElement) div.getElement(new Hook(Strategy.TAG, "span"), 0));
		Assert.assertEquals("Click button to hide me", element.getText());
	}

	@Test
	public void getElementsTest() throws Exception {
		iface.go(testPage);
		List<WebDriverElement> elements = iface.getWebDriverElements(Strategy.NAME,"namedInput");
		Assert.assertEquals(4,elements.size());
	}

	@Test
	public void getTextTest() throws Exception {
		iface.go(testPage);

		WebDriverElement spanByID = iface.getWebDriverElement(Strategy.ID, "writing");
		Assert.assertEquals("Click button to hide me", spanByID.getText());

		WebDriverElement paragraphByCLASS = iface.getWebDriverElement(Strategy.CLASS, "normal");
		Assert.assertEquals("Click to make me blink", paragraphByCLASS.getText());

		WebDriverElement inputByNAME = iface.getWebDriverElement(Strategy.NAME, "namedInput");
		Assert.assertEquals("This is a named input", inputByNAME.getText());

		WebDriverElement spanByXPATH = iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"writing\"]");
		Assert.assertEquals("Click button to hide me", spanByXPATH.getText());
	}

	@Test
	public void getSelectTest() throws Exception {
		iface.go(testPage);
		WebDriverElement formElement = iface.getWebDriverElement(Strategy.ID, "form");
		WebDriverSelector actualSelectElement = (WebDriverSelector) formElement.getSelect(new Hook(Strategy.TAG, "select"), 0);
		Assert.assertEquals("Option 1", actualSelectElement.getFirstSelectedOption());
	}

	@Test
	public void containsTest() throws Exception {
		iface.go(testPage);
		Assert.assertTrue(iface.getWebDriverElement(Strategy.ID, "writingDiv").contains("Click button", true));
		Assert.assertTrue(iface.getWebDriverElement(Strategy.ID, "writingDiv").contains("cLiCk BuTtOn", false));
		Assert.assertTrue(iface.getWebDriverElement(Strategy.ID, "writingDiv").contains("Click button", false));
		Assert.assertFalse(iface.getWebDriverElement(Strategy.ID, "writingDiv").contains("cLiCk BuTtOn", true));

		Assert.assertFalse(iface.getWebDriverElement(Strategy.ID, "writingDiv").contains("Doesn't contain this", true));
		Assert.assertFalse(iface.getWebDriverElement(Strategy.ID, "writingDiv").contains("Doesn't contain this", true));
	}

	@Test
	public void doubleClickTest() throws Exception {
		iface.go(testPage);
		WebDriverElement para = iface.getWebDriverElement(new Hook(Strategy.ID, "doubleClickText"));
		para.doubleClick();
		Assert.assertFalse(para.isDisplayed());
	}

	@Ignore
	@Test
	public void dragNDropTest() throws Exception {
		String url = "http://demos111.mootools.net/DragDrop";
		iface.go(url);

		WebDriverElement dragElement = iface.getWebDriverElement(new Hook(Strategy.CSS, ".drag"));
		WebDriverElement dropElement = iface.getWebDriverElement(new Hook(Strategy.CSS, ".drop"));
		Assert.assertTrue("100px".equals(dropElement.getCssValue("height")));
		dragElement.dragNDrop(dropElement);
		iface.pause(1000);
		Assert.assertTrue("130px".equals(dropElement.getCssValue("height")));
	}

	@Test
	public void pauseUntilVisibleTest() throws Exception {
		long timeoutMilliSec = 4000;
		iface.go(testPage);

		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara"));
		text.click();
		Assert.assertTrue(iface.getPause().waitForVisible(new Hook(Strategy.ID, "intervalPara"), timeoutMilliSec) != null);
		Assert.assertTrue(text.isDisplayed());
		// Wait for the text to go invisible again
		iface.pause(1000);
		Assert.assertTrue(iface.getPause().waitForVisible(new Hook(Strategy.ID, "intervalPara")) != null);
		Assert.assertTrue(text.isDisplayed());

		// Look for a nonexistent element to get time out
		iface.wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		thrown.expect(CandybeanException.class);
		iface.getPause().waitForVisible(new Hook(Strategy.ID, "nonExistentElement"), 500);

	}

	@Test
	public void pauseUntilInvisibleTest() throws Exception {
		final long timeoutMilliSec = 2000;
		iface.go(testPage);

		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara"));
		text.click();
		iface.getPause().waitForInvisible(new Hook(Strategy.ID, "intervalPara"), timeoutMilliSec);
		Assert.assertTrue(!text.isDisplayed());

		// Wait until we know the element is visible and then force a timeout
		iface.getPause().waitForVisible(new Hook(Strategy.ID, "intervalPara"), timeoutMilliSec);

		thrown.expect(CandybeanException.class);
		iface.getPause().waitForInvisible(new Hook(Strategy.ID, "clickPara"), 100);
	}

	@Test
	public void pauseUntilClickableTest() throws Exception {
		iface.go(testPage);

		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara"));
		text.click();
		Assert.assertTrue(iface.getPause().waitUntil(WaitConditions.clickable(new Hook(Strategy.ID, "intervalPara"))) != null);

		// Look for a nonexistent element to get time out
		iface.wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		thrown.expect(CandybeanException.class);
		iface.getPause().waitUntil(WaitConditions.clickable(new Hook(Strategy.ID, "NonExistentElement")), 500);
	}

	@Test
	public void hoverTest() throws Exception {
		iface.go(testPage);
		WebDriverElement element = iface.getWebDriverElement(Strategy.ID, "hoverElement");
		element.hover();
		Assert.assertFalse(element.isDisplayed());
	}

	@Test
	public void isDisplayedTest() throws Exception {
		iface.go(testPage);
		WebDriverElement para = iface.getWebDriverElement(new Hook(Strategy.ID, "clickPara"));
		Assert.assertTrue(para.isDisplayed());
		para.click();
		Assert.assertFalse(para.isDisplayed());
	}

	@Test
	public void rightClickTest() throws Exception {
		iface.go(testPage);
		WebDriverElement para = iface.getWebDriverElement(new Hook(Strategy.ID, "rightClickText"));
		para.rightClick();
		// Second right click to cancel the menu
		para.rightClick();
		Assert.assertFalse(para.isDisplayed());
	}

	@Test
	public void sendStringTest() throws Exception {
		final String garbageString = "This string is garbage that needs to be cleared.";
		final String searchString = "sugarcrm";
		iface.go(testPage);

		WebDriverElement textField = iface.getWebDriverElement(Strategy.ID, "formInput");

		// clear and set scenario
		textField.sendString(garbageString);
		textField.sendString(searchString);
		Assert.assertEquals(searchString, textField.getText());

		// append scenario -- base test and append assert
		String searchString1 = "sugar";
		String searchString2 = "con";
		textField.sendString(searchString1);
		textField.sendString(searchString2, true);
		Assert.assertEquals(searchString1 + searchString2, textField.getText());

		// clear and set to empty string scenario
		String emptyString = "";
		textField.sendString(emptyString);
		Assert.assertEquals(emptyString, textField.getText());
	}

	@Test
	public void executeJavascriptTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "formInput"));
		String searchTerm = "This is a search term";
		searchBox.executeJavascript("arguments[0].value = '" + searchTerm + "';");
		Assert.assertTrue(searchBox.getAttribute("value").equals(searchTerm));
		String boxContents = (String)(searchBox.executeJavascript("return arguments[0].value;"));
		Assert.assertEquals("Javascript return value incorrect.  Expected: " + searchTerm +
				"   Found: " + boxContents, boxContents, searchTerm);
	}

	@Test
	public void getWidthTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement textField = iface.getWebDriverElement(Strategy.ID, "formInput");
		Assert.assertTrue(textField.getWidth() > 0);
	}

	@Test
	public void getHeightTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement textField = iface.getWebDriverElement(Strategy.ID, "formInput");
		Assert.assertTrue(textField.getHeight() > 0);
	}

	@Test
	public void getCssValueTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement textField = iface.getWebDriverElement(Strategy.ID, "formInput");
		Assert.assertEquals("inline-block", textField.getCssValue("display"));
	}

	@Test
	public void waitForAttribute() throws Exception {
		iface.go(testPage);
		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara"));
		text.click();
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "intervalPara"), "class", "hidden", true, 10) != null);
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "intervalPara"), "class", "hidden", false, 10) != null);
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "intervalPara"), "class", "normal", false, 10) != null);
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "intervalPara"), "class", "normal", true, 10) != null);
	}

	@Test
	public void waitForRegexAttribute() throws Exception {
		iface.go(testPage);
		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara"));
		text.click();
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "intervalPara"), "class", "h.*n", true, 10) != null);
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "intervalPara"), "class", ".idden", false, 10) != null);
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "intervalPara"), "class", "n.*l", false) != null);
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "intervalPara"), "class", "normal", false) != null);
	}

	@Test
	public void waitForOnScreen() throws Exception {
		iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/onOffScreen.html");
		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "p1"));
		text.click();
		iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "p1"), true);
		Assert.assertTrue(iface.getWebDriverElement(new Hook(Strategy.ID, "p1")).isOnScreen());
		iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "p1"), false);
		Assert.assertFalse(iface.getWebDriverElement(new Hook(Strategy.ID, "p1")).isOnScreen());
		iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "p1"), true);
		Assert.assertTrue(iface.getWebDriverElement(new Hook(Strategy.ID, "p1")).isOnScreen());

		iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "svg1"), true);
		iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "svg2"), false);
		iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "svg3"), true);
	}

	@Test
	public void toggleWaitForElement() throws Exception {
		iface.wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		iface.go(testPage);
		WebDriverElement button = iface.getWebDriverElement(new Hook(Strategy.ID, "toggleWriting"));
		iface.getPause().waitForElement(new Hook(Strategy.ID, "writing"));
		button.click();
		iface.getPause().waitForElementRemoved(new Hook(Strategy.ID, "writing"));
		button.click();
		iface.getPause().waitForElement(new Hook(Strategy.ID, "writing"));
		button.click();
		iface.getPause().waitForElementRemoved(new Hook(Strategy.ID, "writing"));
	}

	@Test
	public void getSourceTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "writingDiv"));
		final String found = searchBox.getSource();
		final String expected = "id=\"writing\"";
		Assert.assertTrue("Src did not contain " + expected + "\nFound source:\n" + found,
				found.contains(expected));
	}
}
