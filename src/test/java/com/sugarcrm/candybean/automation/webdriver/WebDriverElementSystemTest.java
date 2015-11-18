/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebDriverElementSystemTest {
	private WebDriverInterface iface;
	final String testPage = "file://" + System.getProperty("user.dir") + "/resources/html/test/testPlayground.html";

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
		List<WebDriverElement> elements = iface.getWebDriverElements(Strategy.NAME, "namedInput");
		Assert.assertEquals(4, elements.size());
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
		final boolean CASE_SENSITIVE = true;
		final boolean CASE_INSENSITIVE = false;
		iface.go(testPage);
		final WebDriverElement writingDiv = iface.getWebDriverElement(Strategy.ID, "writingDiv");

		Assert.assertTrue(writingDiv.contains("Click button", CASE_SENSITIVE));
		Assert.assertTrue(writingDiv.contains("cLiCk BuTtOn", CASE_INSENSITIVE));
		Assert.assertTrue(writingDiv.contains("Click button", CASE_INSENSITIVE));
		Assert.assertFalse(writingDiv.contains("cLiCk BuTtOn", CASE_SENSITIVE));
		Assert.assertFalse(writingDiv.contains("Doesn't contain this", CASE_SENSITIVE));
		Assert.assertFalse(writingDiv.contains("Doesn't contain this", CASE_INSENSITIVE));
	}

	@Test
	public void doubleClickTest() throws Exception {
		iface.go(testPage);
		WebDriverElement doubleClickText = iface.getWebDriverElement(new Hook(Strategy.ID, "doubleClickText"));
		// Double clicking paragraph makes it disappear
		doubleClickText.doubleClick();
		Assert.assertFalse(doubleClickText.isDisplayed());
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
		iface.go(testPage);

		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara"));
		text.click();
		Assert.assertNotNull(iface.getPause().waitForVisible(new Hook(Strategy.ID, "intervalPara"), 2000));
		Assert.assertTrue(text.isDisplayed());
		// Wait for the text to go invisible again
		iface.pause(1000);
		Assert.assertNotNull(iface.getPause().waitForVisible(new Hook(Strategy.ID, "intervalPara"), 2000));
		Assert.assertTrue(text.isDisplayed());

		// Look for a nonexistent element to get time out, set the implicit wait to 0
		// so that searching for the element returns immediately when it doesn't find it
		iface.wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		thrown.expect(CandybeanException.class);
		iface.getPause().waitForVisible(new Hook(Strategy.ID, "nonExistentElement"), 500);

	}

	@Test
	public void pauseUntilInvisibleTest() throws Exception {
		iface.go(testPage);

		// Clicking intervalPara makes it toggle visibility every second
		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara"));
		text.click();
		iface.getPause().waitForInvisible(new Hook(Strategy.ID, "intervalPara"), 2000);
		Assert.assertTrue(!text.isDisplayed());

		// Force a timeout on a visible element
		thrown.expect(CandybeanException.class);
		iface.getPause().waitForInvisible(new Hook(Strategy.ID, "clickPara"), 100);
	}

	@Test
	public void pauseUntilClickableTest() throws Exception {
		iface.go(testPage);

		// Clicking intervalPara makes it toggle visibility every second
		iface.getWebDriverElement(new Hook(Strategy.ID, "intervalPara")).click();
		Assert.assertNotNull(iface.getPause().waitUntil(WaitConditions.clickable(new Hook(Strategy.ID, "intervalPara"))));

		// Look for a nonexistent element to get time out, set the implicit wait to 0
		// so that searching for the element returns immediately when it doesn't find it
		iface.wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		thrown.expect(CandybeanException.class);
		iface.getPause().waitUntil(WaitConditions.clickable(new Hook(Strategy.ID, "NonExistentElement")), 500);
	}

	@Test
	public void hoverTest() throws Exception {
		iface.go(testPage);
		WebDriverElement hoverElement = iface.getWebDriverElement(Strategy.ID, "hoverElement");
		// Hovering over hoverElement makes it invisible
		hoverElement.hover();
		Assert.assertFalse(hoverElement.isDisplayed());
	}

	@Test
	public void isDisplayedTest() throws Exception {
		iface.go(testPage);
		WebDriverElement clickPara = iface.getWebDriverElement(new Hook(Strategy.ID, "clickPara"));
		Assert.assertTrue(clickPara.isDisplayed());
		// Clicking clickPara toggles its visibility
		clickPara.click();
		Assert.assertFalse(clickPara.isDisplayed());
	}

	@Test
	public void rightClickTest() throws Exception {
		iface.go(testPage);
		WebDriverElement rightClickText = iface.getWebDriverElement(new Hook(Strategy.ID, "rightClickText"));
		// Right clicking rightClickText toggles its visibility
		rightClickText.rightClick();
		// Second right click to cancel the menu
		rightClickText.rightClick();
		Assert.assertFalse(rightClickText.isDisplayed());
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
		String boxContents = (String) (searchBox.executeJavascript("return arguments[0].value;"));
		Assert.assertEquals("Javascript return value incorrect.  Expected: " + searchTerm +
				"   Found: " + boxContents, boxContents, searchTerm);
	}

	@Test
	public void getWidthTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement form = iface.getWebDriverElement(Strategy.ID, "formInput");
		Assert.assertTrue(form.getWidth() > 1);
	}

	@Test
	public void getHeightTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement form = iface.getWebDriverElement(Strategy.ID, "formInput");
		Assert.assertTrue(form.getHeight() > 1);
	}

	@Test
	public void getCssValueTest() throws CandybeanException {
		iface.go(testPage);
		WebDriverElement textField = iface.getWebDriverElement(Strategy.ID, "formInput");
		Assert.assertEquals("inline-block", textField.getCssValue("display"));
	}

	@Test
	public void waitForAttribute() throws Exception {
		final Hook paragraph = new Hook(Strategy.ID, "intervalPara");
		final boolean MATCH = true;
		final boolean NO_MATCH = false;

		iface.go(testPage);
		// Clicking on paragraph toggles its class between hidden and normal every second
		iface.getWebDriverElement(paragraph).click();

		Assert.assertNotNull(iface.getPause().waitForAttribute(paragraph, "class", "hidden", MATCH, 10));
		Assert.assertNotNull(iface.getPause().waitForAttribute(paragraph, "class", "hidden", NO_MATCH, 10));
		Assert.assertNotNull(iface.getPause().waitForAttribute(paragraph, "class", "normal", NO_MATCH, 10));
		Assert.assertNotNull(iface.getPause().waitForAttribute(paragraph, "class", "normal", MATCH, 10));
	}

	@Test
	public void waitForRegexAttribute() throws Exception {
		final Hook paragraph = new Hook(Strategy.ID, "intervalPara");
		final boolean MATCH = true;
		final boolean NO_MATCH = false;

		iface.go(testPage);
		// Clicking on paragraph toggles its class between hidden and normal every second
		iface.getWebDriverElement(paragraph).click();

		Assert.assertNotNull(iface.getPause().waitForRegexAttribute(paragraph, "class", "h.*n", MATCH, 10));
		Assert.assertNotNull(iface.getPause().waitForRegexAttribute(paragraph, "class", ".idden", NO_MATCH, 10));
		Assert.assertNotNull(iface.getPause().waitForRegexAttribute(paragraph, "class", "n.*l", NO_MATCH));
		Assert.assertNotNull(iface.getPause().waitForRegexAttribute(paragraph, "class", "normal", NO_MATCH));
	}

	@Test
	public void waitForOnScreen() throws Exception {
		final boolean IS_ON_SCREEN = true;
		final boolean IS_OFF_SCREEN = false;
		final Hook paragraph = new Hook(Strategy.ID, "p1");

		iface.go("file://" + System.getProperty("user.dir") + "/resources/html/test/onOffScreen.html");

		/* Clicking on paragraph
		 *     Changes its coordinates to (-50,-50)
		 *     Wait 1 second
		 *     Changes its coordinates to (50,50)
		 *     Repeat
		 */
		iface.getWebDriverElement(paragraph).click();

		Assert.assertTrue(iface.getPause().waitForOnScreen(paragraph, IS_ON_SCREEN).isOnScreen());
		Assert.assertFalse(iface.getPause().waitForOnScreen(paragraph, IS_OFF_SCREEN).isOnScreen());
		Assert.assertTrue(iface.getPause().waitForOnScreen(paragraph, IS_ON_SCREEN).isOnScreen());

		// There are 3 static svgs on the page. svg1 is entirely on the screen, svg2 is entirely off, svg3 is half on
		Assert.assertTrue(iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "svg1"), IS_ON_SCREEN).isOnScreen());
		Assert.assertFalse(iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "svg2"), IS_OFF_SCREEN).isOnScreen());
		Assert.assertTrue(iface.getPause().waitForOnScreen(new Hook(Strategy.ID, "svg3"), IS_ON_SCREEN).isOnScreen());
	}

	@Test
	public void toggleWaitForElement() throws Exception {
		final Hook writingHook = new Hook(Strategy.ID, "writing");
		iface.go(testPage);

		// Clicking this button toggles the visibility of the element found by writing hook
		final WebDriverElement button = iface.getWebDriverElement(new Hook(Strategy.ID, "toggleWriting"));

		Assert.assertTrue(iface.getPause().waitForElement(writingHook).isDisplayed());
		button.click();

		// Look for a nonexistent element to get time out, set the implicit wait to 0
		// so that searching for the element returns immediately when it doesn't find it
		iface.wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		iface.getPause().waitForElementRemoved(writingHook);
		Assert.assertEquals(0, iface.getWebDriverElements(writingHook).size());
		button.click();

		Assert.assertTrue(iface.getPause().waitForElement(writingHook).isDisplayed());
		button.click();
		iface.getPause().waitForElementRemoved(writingHook);
	}

	@Test
	public void getSourceTest() throws CandybeanException {
		iface.go(testPage);
		final WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "writingDiv"));
		final String found = searchBox.getSource();
		final String expected = "id=\"writing\"";
		Assert.assertTrue("Src did not contain " + expected + "\nFound source:\n" + found,
				found.contains(expected));
	}
}
