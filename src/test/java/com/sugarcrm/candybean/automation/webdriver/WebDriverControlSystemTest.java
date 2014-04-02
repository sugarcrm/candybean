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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.webdriver.WebDriverElement;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class WebDriverControlSystemTest {
	
	public static WebDriverInterface iface;
	
	@BeforeClass
	public static void beforeClass() throws CandybeanException{
		Candybean candybean = Candybean.getInstance();
		AutomationInterfaceBuilder builder = candybean.getAIB(WebDriverControlSystemTest.class);
		builder.setType(Type.CHROME);
		iface = builder.build();
	}
	
	@Before
	public void setUp() throws CandybeanException {
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
		String w3Url = "http://sfbay.craigslist.org/";
		iface.go(w3Url);
		String actAltValue = iface.getWebDriverElement(Strategy.ID, "container").getAttribute("summary");
		String expAltValue = "page";
		Assert.assertEquals(expAltValue, actAltValue);
	}
	
	
	@Test
	public void getElementTest() throws Exception {
		String craigsUrl = "http://sfbay.craigslist.org/";
		String expH2 = "san francisco";
		iface.go(craigsUrl);
		WebDriverElement header = iface.getWebDriverElement(Strategy.ID, "topban");
		((WebDriverElement) header.getElement(new Hook(Strategy.TAG, "a"), 1)).click();
		header = iface.getWebDriverElement(Strategy.ID, "topban");
		WebDriverElement h2Control = ((WebDriverElement) header.getElement(new Hook(Strategy.TAG, "h2"), 0));
		String actH2 = h2Control.getText().trim();
		Assert.assertEquals(expH2, actH2);
	}

	@Test
	public void getControlsTest() throws Exception{
		String craigsUrl = "http://sfbay.craigslist.org/";
		iface.go(craigsUrl);
		List<WebDriverElement> elements = iface.getWebDriverElements(Strategy.CLASS,"ban");
		Assert.assertEquals(elements.size(),15);
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
		//Fourth test
		url = "http://www.developphp.com/view_lesson.php?v=576";
		iface.go(url);
		actChapterText = iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"page_data\"]/div[4]/input").getText(); // button type button
		expChapterText = "Generic Button";
		Assert.assertEquals(expChapterText, actChapterText);
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

	@Ignore
	@Test
	// Can be verified by looking at the website checkbox (Double click is performed 3 times)
	public void doubleClickTest() throws Exception {
		String w3Url = "http://www.w3schools.com/html/html_forms.asp";
		iface.go(w3Url);
		//Checkbox element
		WebDriverElement checkboxControl = iface.getWebDriverElement(Strategy.NAME, "vehicle");
		// DoubleClick on a Checkbox
		checkboxControl.scroll();
		iface.pause(2000);
		checkboxControl.doubleClick();
		iface.pause(2000); 
		checkboxControl.doubleClick();
		iface.pause(2000); 
		checkboxControl.doubleClick();
		iface.pause(2000);
//		doubleClick(int index);
	}
	
	@Ignore
	@Test
	public void dragNDropTest() throws Exception {
		// http://jqueryui.com/resources/demos/droppable/default.html -- suggested from dev mailing list
		String w3Url = "http://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=6&ved=0CDoQFjAF&url=http%3A%2F%2Ftool-man.org%2Fexamples%2Fsorting.html&ei=nBGLUKi8CcGmigLah4CADg&usg=AFQjCNGL-HryUxMBRKn9gEM0F1xE_NNNyQ";
		iface.go(w3Url);
		iface.pause(2000);
		WebDriverElement imgControl = iface.getWebDriverElement(new Hook(Strategy.XPATH, "/html/body/ul[2]/li"));
		WebDriverElement targetControl = iface.getWebDriverElement(new Hook(Strategy.XPATH, "/html/body/ul[2]/li[2]"));
   		imgControl.dragNDrop(targetControl);
        iface.pause(3000);  // pause for manual inspection

   		// Verify draggable has been moved to new location
   		String actItemid = targetControl.getAttribute("itemid");
   		String expItemid = "1";
        Assert.assertEquals(expItemid, actItemid);
//		dragNDrop(VControl dropControl, int dragIndex, int dropIndex);
	}

	@Ignore
	@Test
	public void dragNDropTest2() throws Exception {
//		iface.go("http://www.w3schools.com/html/html5_draganddrop.asp");
//		candybean.interact(new VHook(Strategy.ID, "drag1"));
//		candybean.dragNDrop(new VHook(Strategy.ID, "drag1"), new VHook(Strategy.ID, "div2"));
//		WebDriver driver = new ChromeDriver();
//		driver.get("http://www.w3schools.com/html/html5_draganddrop.asp");
//		Action dragAndDrop = (new Actions(driver)).clickAndHold(driver.findElement(By.id("drag1")))
//			       .moveToElement(driver.findElement(By.id("div2")))
//			       .release(driver.findElement(By.id("drag1")))
//			       .build();
//		candybean.interact("wait for it...");
//		dragAndDrop.perform();
//		candybean.interact("wait for it...");
//		candybean.halt(new VHook(Strategy.XPATH, "/html/body/div/div/div[4]/div[2]/hr[2]/div[2]/img"));
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
		textControl.pause.untilVisible(timeout);
		endTime = System.currentTimeMillis();
		Assert.assertTrue((endTime - startTime) / Long.parseLong("1000") < (long)timeout);
	}

	@Test
	public void pauseUntilVisibleTest() throws Exception {
		long timeoutMs = 4000;
		iface.go("http://www.jquery4u.com/function-demos/delay/");
		WebDriverElement delayText = iface.getWebDriverElement(new Hook(Strategy.ID, "delay"));
		// checking assumptions -- text is not displayed
		Assert.assertFalse(delayText.isDisplayed());
		iface.getWebDriverElement(Strategy.ID, "delay-demobtn").click();
		delayText.pause.untilVisible((int)timeoutMs);
		Assert.assertTrue(delayText.isDisplayed());
		iface.pause(3000); // text is displayed for 1.5 seconds -- double it just in case
		Assert.assertFalse(delayText.isDisplayed());
		thrown.expect(TimeoutException.class);
		delayText.pause.untilVisible((int)timeoutMs); // this one should timeout
	}

	@Test
	public void hoverTest() throws Exception {
		iface.go("http://www.dynamicdrive.com/style/csslibrary/item/css-popup-image-viewer/");
		WebDriverElement image = iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"middlecolumn\"]/p[5]/a[2]/span/img");
		Assert.assertFalse(image.isDisplayed());
		iface.getWebDriverElement(Strategy.XPATH, "//*[@id=\"middlecolumn\"]/p[5]/a[2]/img").hover();
		Assert.assertTrue(image.isDisplayed());
	}
	
	@Ignore
	@Test
	public void isDisplayedTest() throws Exception {
		int timeout = 1000;
		iface.go("http://sfbay.craigslist.org/");
		iface.pause(timeout);
		WebDriverElement searchField;
//		if (iface.getType().equals(Type.IE)) 
//			searchField = iface.getWebDriverElement(Strategy.NAME, "q");
//		else
			searchField = iface.getWebDriverElement(Strategy.ID, "query");
		Assert.assertTrue(searchField.isDisplayed());
		WebDriverElement hiddenInput;
//		if (iface.getType().equals(Type.IE))
//			hiddenInput = iface.getWebDriverElement(Strategy.NAME, "site");
//		else
			hiddenInput = iface.getWebDriverElement(Strategy.NAME, "areaID");
		Assert.assertFalse(hiddenInput.isDisplayed());
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

}	