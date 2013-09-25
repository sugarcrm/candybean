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
package com.sugarcrm.candybean.automation.control;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.TimeoutException;

import com.sugarcrm.candybean.CB;
import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface.Type;
import com.sugarcrm.candybean.automation.control.VControl;
import com.sugarcrm.candybean.automation.control.VHook;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.Utils;

//import com.sugarcrm.candybean.IAutomation.Strategy;
//import com.sugarcrm.candybean.automation.VHook;
//import com.sugarcrm.candybean.IAutomation;
//import com.sugarcrm.candybean.Candybean;
//import static org.junit.Assert.assertEquals;

public class VControlSystemTest {
	
	protected static Candybean candybean;
	protected static VInterface iface;
		
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void first() throws Exception {
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) candybeanConfigStr = CB.CONFIG_DIR.getCanonicalPath() + File.separator + "candybean.config";
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		iface = candybean.getInterface();
		iface.start();
	}

//	@Ignore
	@Test
	public void getAttributeTest() throws Exception {
		String w3Url = "http://www.w3schools.com/html/default.asp";
		iface.go(w3Url);
		String actAltValue = iface.getControl(Strategy.XPATH, "//*[@id=\"topLogo\"]/a[2]/img").getAttribute("alt");
		String expAltValue = "W3Schools.com";
		Assert.assertEquals(expAltValue, actAltValue);
		String actHrefValue = iface.getControl(Strategy.ID, "top").getControl(Strategy.TAG, "a", 1).getAttribute("href");
		String expHrefValue = "http://www.w3schools.com/";
		Assert.assertEquals(expHrefValue, actHrefValue);
	}
	
//	@Ignore
	@Test
	public void getControlTest() throws Exception {
		String w3Url = "http://www.w3schools.com/";
		String expH2 = "HTML5 Introduction";
		iface.go(w3Url);
		iface.getControl(Strategy.ID, "leftcolumn").getControl(Strategy.TAG, "a", 1).click();
		VControl h1Control = iface.getControl(Strategy.TAG, "h1");
		String actH2 = h1Control.getText().trim();
		Assert.assertEquals(expH2, actH2);
//		String text2 = getText(int index);
	}
	
//	@Ignore
	@Test
	public void getTextTest() throws Exception {
		String w3Url = "http://www.w3schools.com/html/default.asp";
		iface.go(w3Url);
		String expChapterText = "W3Schools Home";
		String actChapterText = iface.getControl(Strategy.XPATH, "//*[@id=\"main\"]/div[1]/div[1]/a").getText().substring(2);
		Assert.assertEquals(expChapterText, actChapterText);
		w3Url = "http://www.echoecho.com/htmlforms12.htm";
		iface.go(w3Url);
		actChapterText = iface.getControl(Strategy.NAME, "shorttext").getText(); // input type button
		expChapterText = "Hit Me!";
		Assert.assertEquals(expChapterText, actChapterText);
		w3Url = "http://www.developphp.com/view_lesson.php?v=576";
		iface.go(w3Url);
		actChapterText = iface.getControl(Strategy.XPATH, "//*[@id=\"page_data\"]/div[4]/input").getText(); // button type button
		expChapterText = "Generic Button";
		Assert.assertEquals(expChapterText, actChapterText);
}

	@Ignore
	@Test
	public void clickTest() throws Exception {
//		click();
//		click(int index);
	}
	
//	@Ignore
	@Test
	public void containsTest() throws Exception {
		iface.go("https://code.google.com/");
		boolean actCaseSensPos = iface.getControl(Strategy.ID, "gc-footer").contains("Google Developers", true); //true
		boolean actCaseSensNeg = iface.getControl(Strategy.ID, "gc-footer").contains("google developers", true); //false
		boolean actNeg = iface.getControl(Strategy.ID, "gc-footer").contains("goggle devs", false); //false
		boolean negFalse = iface.getControl(Strategy.ID, "gc-topnav").contains("Google Developers", false); //false
		boolean negTrue = iface.getControl(Strategy.ID, "gc-footer").contains("Google Developers", false); //true
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
		//Checkbox control
		VControl checkboxControl = iface.getControl(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[4]/input[1]");
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
		VControl imgControl = iface.getControl(new VHook(Strategy.XPATH, "/html/body/ul[2]/li"));
		VControl targetControl = iface.getControl(new VHook(Strategy.XPATH, "/html/body/ul[2]/li[2]"));
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
//		candybean.go("http://www.w3schools.com/html/html5_draganddrop.asp");
//		candybean.halt(new VHook(Strategy.ID, "drag1"));
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
//		iface.pause(1000);
		VControl textControl = iface.getControl(Strategy.XPATH, "//*[@id=\"test\"]/div/div");
		Assert.assertFalse(textControl.isDisplayed());
		iface.pause(timeout);
		iface.getControl(Strategy.XPATH, "//*[@id=\"test\"]/p[1]/button[1]").click();
		startTime = System.currentTimeMillis();
		textControl.pause.untilVisible(timeout);
		endTime = System.currentTimeMillis();
		Assert.assertTrue((endTime - startTime) / Long.parseLong("1000") < (long)timeout);
	}
	
	@Test
	public void pauseUntilVisibleTest() throws Exception {
		long timeout = 10;
		long startTime = 0;
		long endTime = 0;
		iface.go("http://www.w3schools.com/css/css_display_visibility.asp");
		VControl hideControl = iface.getControl(Strategy.XPATH, "//*[@id=\"imgbox2\"]/input");
		startTime = System.currentTimeMillis();
		hideControl.pause.untilVisible((int)timeout);
		endTime = System.currentTimeMillis();
		Assert.assertTrue((endTime - startTime) / Long.parseLong("1000") < timeout);
		hideControl.click();
		startTime = System.currentTimeMillis();
		thrown.expect(TimeoutException.class);
		hideControl.pause.untilVisible((int)timeout);
		endTime = System.currentTimeMillis();
		Assert.assertTrue((endTime - startTime) / Long.parseLong("1000") >= timeout);
		
		// until text present
		iface.go("http://fvsch.com/code/transition-fade/test5.html");
		
	}
	
	@Ignore
	@Test
	public void hoverTest() throws Exception {
//		hover();
//		hover(int index);
	}
	
	@Test
	public void isDisplayedTest() throws Exception {
		iface.go("http://www.google.com");
		VControl searchField;
		if (iface.getType().equals(Type.IE)) 
			searchField = iface.getControl(Strategy.NAME, "q");
		else
			searchField = iface.getControl(Strategy.ID, "gbqfq");
		Assert.assertTrue(searchField.isDisplayed());
		VControl hiddenInput;
		if (iface.getType().equals(Type.IE))
			hiddenInput = iface.getControl(Strategy.NAME, "site");
		else
			hiddenInput = iface.getControl(Strategy.NAME, "output");
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

//    @Test
//    public void checkBoxSelectTest() throws Exception {
//
//        // Checking checkbox select
//        String w3Url = "http://www.w3schools.com/html/html_forms.asp";
//        iface.go(w3Url);
//        VSelect select = iface.getSelect(new VHook(Strategy.XPATH, "//*[@id=\"main\"]/form[4]"));
//        Assert.assertEquals("Control should not be selected -- selected: " + select.isSelected(0), select.isSelected(0), false);
//        select.select("I have a bike");
//        Assert.assertEquals("Control should be selected -- selected: " + select.isSelected(0), select.isSelected(0), true);
//
//        // Exception should throw for non-checkbox element
//        //VHook nonCheckboxHook = new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[3]/input[1]"); // a radio box
//        //candybean.select(nonCheckboxHook, true);  // yes, verified exception was thrown
//
//        // Checking getAttributeValue()
////		VControl control = iface.getControl(new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[1]/input[1]"));
////		String actText = control.getAttribute("type");
////        String expText = "text";
////        Assert.assertEquals("Expected value for the type attribute should match: " + expText, expText, actText);
////
////		String actSize = control.getAttribute("size");
////        String expSize = "20";
////        Assert.assertEquals("Expected value for the size attribute should match: " + expSize, expSize, actSize);
////
////		String actName = control.getAttribute("name");
////        String expName = "firstname";
////        Assert.assertEquals("Expected value for the name attribute should match: " + expName, expName, actName);
//    }

//	@Ignore
	@Test
	public void sendStringTest() throws Exception {
		String searchString = "sugarcrm";
		iface.go("http://www.duckduckgo.com/");
//		iface.pause(1000);
//		iface.interact("pause0");
		iface.getControl(Strategy.ID, "search_form_input_homepage").sendString(searchString);
//		iface.interact("pause1");
		iface.getControl(Strategy.ID, "search_button_homepage").click();
		Assert.assertTrue(iface.getControl(Strategy.PLINK, "SugarCRM").isDisplayed());
	}
	
	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}	
