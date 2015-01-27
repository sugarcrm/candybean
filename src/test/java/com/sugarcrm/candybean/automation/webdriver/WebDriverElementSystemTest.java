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

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
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
		iface.go("http://sfbay.craigslist.org/");
		String actAltValue = iface.getWebDriverElement(Strategy.ID, "ppp").getAttribute("class");
		String expAltValue = "col";
		Assert.assertEquals(expAltValue, actAltValue);    
	}
	
	
	@Test
	public void getElementTest() throws Exception {
		String logoText = "craigslist";
		iface.go("http://sfbay.craigslist.org/");
		WebDriverElement div = iface.getWebDriverElement(Strategy.ID, "logo");
		WebDriverElement logoElement = ((WebDriverElement) div.getElement(new Hook(Strategy.TAG, "a"), 0));
		String actLogo = logoElement.getText().trim();
		Assert.assertEquals(logoText, actLogo);
	}

	@Test
	public void getElementsTest() throws Exception {
		iface.go("http://sfbay.craigslist.org/");
		List<WebDriverElement> elements = iface.getWebDriverElements(Strategy.CLASS,"ban");
		Assert.assertEquals(elements.size(),14);
	}

	@Test
	public void getTextTest() throws Exception {
		//First test
		String craigsUrl = "http://sfbay.craigslist.org/";
		iface.go(craigsUrl);
		String banner = "craigslist";
		WebDriverElement bannerDiv = iface.getWebDriverElement(Strategy.ID, "logo");
		WebDriverElement bannerLogo = ((WebDriverElement) bannerDiv.getElement(new Hook(Strategy.TAG, "a"), 0));
		String bannerText = bannerLogo.getText();
		Assert.assertEquals(bannerText,banner);
		//Second test
		String meanUrl = "http://www.mean.io/";
		iface.go(meanUrl);
		String banner1 = "The Friendly";
		WebDriverElement bannerElement = iface.getWebDriverElement(Strategy.CLASS, "banner-top-title");
		String bannerElementText = bannerElement.getText();
		Assert.assertTrue(bannerElementText.contains(banner1));
		//Third test
		String url = "http://www.echoecho.com/htmlforms12.htm";
		iface.go(url);
		String actChapterText = iface.getWebDriverElement(Strategy.NAME, "shorttext").getText(); // input type button
		String expChapterText = "Hit Me!";
		Assert.assertEquals(expChapterText, actChapterText);
		/* Webpage moved

		//Fourth test
		url = "http://www.developphp.com/view_lesson.php?v=576";
		iface.go(url);
		actChapterText = iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"page_data\"]/div[4]/input").getText(); // button type button
		expChapterText = "Generic Button";
		Assert.assertEquals(expChapterText, actChapterText);

		*/
	}

	@Test
	public void getSelectTest() throws Exception {
		iface.go("http://sfbay.craigslist.org/");
		WebDriverElement formElement = iface.getWebDriverElement(Strategy.ID, "search");
		WebDriverSelector actualSelectElement = (WebDriverSelector) formElement.getSelect(new Hook(Strategy.TAG, "select"), 0);
		Assert.assertEquals("for sale", actualSelectElement.getFirstSelectedOption());
	}

	@Test
	public void containsTest() throws Exception {
		iface.go("https://code.google.com/");
		boolean actCaseSensPos = iface.getWebDriverElement(Strategy.ID, "gc-footer").contains("Google Developers", true); // true
		boolean actCaseSensNeg = iface.getWebDriverElement(Strategy.ID, "gc-footer").contains("google developers", true); // false
		boolean actNeg = iface.getWebDriverElement(Strategy.ID, "gc-footer").contains("goggle devs", false); // false
		boolean negFalse = iface.getWebDriverElement(Strategy.CLASS, "ph-section").contains("Google Developers", false); // false
		boolean negTrue = iface.getWebDriverElement(Strategy.ID, "gc-footer").contains("Google Developers", false); // true
		Assert.assertEquals(true, actCaseSensPos);
		Assert.assertEquals(false, actCaseSensNeg);
		Assert.assertEquals(false, actNeg);
		Assert.assertEquals(false, negFalse);
		Assert.assertEquals(true, negTrue);
	}

	@Test
	public void doubleClickTest() throws Exception {
		iface.go("http://www.developerhelpway.com/jquery/events/dblClick-event.html");
		
		//Double-Clicking this button will trigger an alert through JavaScript event
		WebDriverElement dbClickButton = iface.getWebDriverElement(new Hook(Strategy.ID, "buttonId"));
		dbClickButton.doubleClick();
		Assert.assertTrue(iface.isDialogVisible());
		iface.acceptDialog();
	}

	@Test
	public void dragNDropTest() throws Exception {
		String url = "http://demos.telerik.com/kendo-ui/dragdrop/index";
		iface.go(url);
		
		WebDriverElement dragElement = iface.getWebDriverElement(new Hook(Strategy.ID, "draggable"));
		WebDriverElement dropElement = iface.getWebDriverElement(new Hook(Strategy.ID, "droptarget"));
		dragElement.dragNDrop(dropElement);
		Assert.assertEquals("You did great!", dropElement.getText());
	}

	@Test
	public void pauseUntilTextPresentTest() throws Exception {
		int timeout = 2000;
		long startTime = 0;
		long endTime = 0;
		iface.go("http://fvsch.com/code/transition-fade/test5.html");
		iface.pause(timeout);
		WebDriverElement textControl = iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"test\"]/div/div");
		Assert.assertFalse(textControl.isDisplayed());
		iface.pause(timeout);
		iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"test\"]/p[1]/button[1]").click();
		startTime = System.currentTimeMillis();
		iface.getPause().waitForVisible(textControl, 10000);
		endTime = System.currentTimeMillis();
		Assert.assertTrue((endTime - startTime) / Long.parseLong("1000") < (long)timeout);
	}

	@Test
	public void pauseUntilVisibleTest() throws Exception {
		long timeoutMilliSec = 4000;
		iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/blinking.html");

		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "p1"));
		text.click();
		Assert.assertTrue(iface.getPause().waitForVisible(new Hook(Strategy.ID, "p1"), timeoutMilliSec) != null);
		Assert.assertTrue(text.isDisplayed());
		// Wait for the text to go invisible again
		iface.pause(3000);
		Assert.assertTrue(iface.getPause().waitForVisible(new Hook(Strategy.ID, "p1")) != null);
		Assert.assertTrue(text.isDisplayed());

		// Look for a nonexistent element to get time out
		thrown.expect(CandybeanException.class);
		iface.getPause().waitForVisible(new Hook(Strategy.ID, "p2"), 500);

	}

	@Test
	public void pauseUntilInvisibleTest() throws Exception {
		long timeoutMilliSec = 3000;
		iface.go("http://www.w3schools.com/css/css_display_visibility.asp");

		WebDriverElement imgElement = iface.getWebDriverElement(new Hook(Strategy.ID, "imgbox2"));
		WebDriverElement hideButton = iface.getWebDriverElement(new Hook(Strategy.CSS, "#imgbox2 input"));

		// Test to ensure we don't wait for elements that are not there
		hideButton.click();
		iface.getPause().waitForInvisible(imgElement, timeoutMilliSec);
		Assert.assertFalse(imgElement.isDisplayed());
		iface.getPause().waitForInvisible(hideButton);


		WebDriverElement hideButton2 = iface.getWebDriverElement(new Hook(Strategy.CSS, "#imgbox1 input"));
		hideButton2.click();

		iface.getPause().waitForInvisible(new Hook(Strategy.CSS, "imgbox1"), 2000);

		// Check that waitForInvisible throws a Timeout exception if it waits too long
		try {
			iface.getWebDriverElement(new Hook(Strategy.CSS, ".box[value='Reset All']")).click();
			iface.getPause().waitForInvisible(new Hook(Strategy.ID, "imgbox2"), timeoutMilliSec);
		} catch (CandybeanException e) {
			Assert.assertTrue(iface.getWebDriverElement(new Hook(Strategy.ID, "imgbox2")).isDisplayed());
			return;
		}
		// Fail if the try doesn't return
		Assert.fail();
	}

	@Test
	public void pauseUntilClickableTest() throws Exception {
		String url = "http://sfbay.craigslist.org/";
		iface.go(url);

		String sfcLi = "#topban .sublinks li:first-child";
		Hook sfcLiButton = new Hook(Strategy.CSS, sfcLi + " a");
		iface.getPause().waitUntil(WaitConditions.clickable(sfcLiButton));
		iface.getWebDriverElement(sfcLiButton).click();
		Assert.assertEquals(iface.getURL(), url + "sfc/");
	}

	@Test
	public void hoverTest() throws Exception {
		iface.go("http://www.dynamicdrive.com/style/csslibrary/item/css-popup-image-viewer/");
		WebDriverElement image = iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"middlecolumn\"]/p[5]/a[2]/span/img");
		Assert.assertFalse(image.isDisplayed());
		iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"middlecolumn\"]/p[5]/a[2]/img").hover();
		Assert.assertTrue(image.isDisplayed());
	}
	
	@Test
	public void isDisplayedTest() throws Exception {
		iface.go("http://www.w3schools.com/css/css_display_visibility.asp");
		
		WebDriverElement imgElement = iface.getWebDriverElement(new Hook(Strategy.ID, "imgbox2"));
		Assert.assertTrue(imgElement.isDisplayed());
		
		//Click the hide button to hide imgElement (Setting visibility style to hidden)
		WebDriverElement hideButton = iface.getWebDriverElement(new Hook(Strategy.CSS, "#imgbox2 input"));
		hideButton.click();
		Assert.assertFalse(imgElement.isDisplayed());
	}

	@Ignore
	@Test
	public void rightClickTest() throws Exception {
//		rightClick();
//		rightClick(int index);
	}

	@Ignore
	@Test
	public void scrollTest() throws Exception {
//		scroll();
//		scroll(int index);
	}

	@Ignore
	@Test
    public void checkBoxSelectTest() throws Exception {

		// Checking checkbox select
		String w3Url = "http://www.w3schools.com/html/html_forms.asp";
		iface.go(w3Url);
		WebDriverSelector select = iface.getSelect(new Hook(Strategy.XPATH, "//*[@id=\"main\"]/form[4]"));
		Assert.assertEquals("Control should not be selected -- selected: " + select.isSelected(0), select.isSelected(0), false);
		select.select("I have a bike");
		Assert.assertEquals("Control should be selected -- selected: " + select.isSelected(0), select.isSelected(0), true);

        // Exception should throw for non-checkbox element
//        VHook nonCheckboxHook = new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[3]/input[1]"); // a radio box
//        candybean.select(nonCheckboxHook, true);  // yes, verified exception was thrown

		// Checking getAttributeValue()
		WebDriverElement element = iface.getWebDriverElement(new Hook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[1]/input[1]"));
		String actText = element.getAttribute("type");
		String expText = "text";
		Assert.assertEquals("Expected value for the type attribute should match: " + expText, expText, actText);

		String actSize = element.getAttribute("size");
		String expSize = "20";
		Assert.assertEquals("Expected value for the size attribute should match: " + expSize, expSize, actSize);

		String actName = element.getAttribute("name");
		String expName = "firstname";
		Assert.assertEquals("Expected value for the name attribute should match: " + expName, expName, actName);
	}

	@Test
	public void sendStringTest() throws Exception {
		String searchString = "sugarcrm";
		
		// clear and send scenario
		iface.go("http://www.duckduckgo.com/");
		iface.getWebDriverElement(Strategy.ID, "search_form_input_homepage").sendString(searchString);
		iface.getWebDriverElement(Strategy.ID, "search_button_homepage").click();
		Assert.assertTrue(iface.getWebDriverElement(Strategy.PLINK, "SugarCRM").isDisplayed());

		// append scenario -- base test and append assert
		iface.go("http://www.duckduckgo.com/");
		iface.getWebDriverElement(Strategy.ID, "search_form_input_homepage").sendString("crm", false);
		iface.getWebDriverElement(Strategy.ID, "search_button_homepage").click();
		iface.getWebDriverElement(Strategy.ID, "search_form_input").sendString("sugar", true);
		iface.getWebDriverElement(Strategy.ID, "search_button").click();
		Assert.assertTrue(iface.getWebDriverElement(Strategy.PLINK, "SugarCRM").isDisplayed());
	}

	@Test
	public void executeJavascriptTest() throws CandybeanException {
		iface.go("http://duckduckgo.com/");
		WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "search_form_input_homepage"));
		String searchTerm = "This is a search term";
		searchBox.executeJavascript("arguments[0].value = '" + searchTerm + "';");
		Assert.assertTrue(searchBox.getAttribute("value").equals(searchTerm));
		String boxContents = (String)(searchBox.executeJavascript("return arguments[0].value;"));
		Assert.assertEquals("Javascript return value incorrect.  Expected: " + searchTerm +
				"   Found: " + boxContents, boxContents, searchTerm);
	}

	@Test
	public void getWidthTest() throws CandybeanException {
		iface.go("https://www.google.com/");
		WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "gbqfq"));
		int searchBoxWidth = searchBox.getWidth();
		Assert.assertTrue(searchBoxWidth > 0);
	}

	@Test
	public void getHeightTest() throws CandybeanException {
		iface.go("https://www.google.com/");
		WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "gbqfq"));
		int searchBoxHeight = searchBox.getHeight();
		Assert.assertTrue(searchBoxHeight > 0);
	}

	@Test
	public void getCssValueTest() throws CandybeanException {
		iface.go("https://www.google.com/");
		WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "gbqfq"));
		Assert.assertEquals(searchBox.getCssValue("display"), "block");
	}

	@Test
	public void waitForAttribute() throws Exception {
		iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/blinking.html");
		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "p1"));
		text.click();
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "p1"), "class", "hidden", true, 10) != null);
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "p1"), "class", "hidden", false, 10) != null);
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "p1"), "class", "normal", false, 10) != null);
		Assert.assertTrue(iface.getPause().waitForAttribute(new Hook(Strategy.ID, "p1"), "class", "normal", false, 10) != null);
	}

	@Test
	public void waitForRegexAttribute() throws Exception {
		iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/blinking.html");
		WebDriverElement text = iface.getWebDriverElement(new Hook(Strategy.ID, "p1"));
		text.click();
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "p1"), "class", "h.*n", true, 10) != null);
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "p1"), "class", ".idden", false, 10) != null);
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "p1"), "class", "n.*l", false) != null);
		Assert.assertTrue(iface.getPause().waitForRegexAttribute(new Hook(Strategy.ID, "p1"), "class", "normal", false) != null);
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
		iface.go("file://"+ System.getProperty("user.dir")+"/resources/html/test/removeFromDOM.html");
		WebDriverElement button = iface.getWebDriverElement(new Hook(Strategy.ID, "b1"));
		iface.getPause().waitForElement(new Hook(Strategy.ID, "p1"));
		button.click();
		iface.getPause().waitForElementRemoved(new Hook(Strategy.ID, "p1"));
		button.click();
		iface.getPause().waitForElement(new Hook(Strategy.ID, "p1"));
		button.click();
		iface.getPause().waitForElementRemoved(new Hook(Strategy.ID, "p1"));
	}

	@Test
	public void getSourceTest() throws CandybeanException {
		iface.go("https://www.google.com/");
		WebDriverElement searchBox = iface.getWebDriverElement(new Hook(Strategy.ID, "lga"));
		String found = searchBox.getSource();
		String expected = "id=\"hplogo\"";
		Assert.assertTrue("Src did not contain " + expected + "\nFound source:\n" + found,
				found.contains("id=\"hplogo\""));
	}
}
