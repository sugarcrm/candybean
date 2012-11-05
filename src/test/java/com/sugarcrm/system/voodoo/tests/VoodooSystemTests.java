package com.sugarcrm.system.voodoo.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.control.VHook;

//import com.sugarcrm.voodoo.IAutomation.Strategy;
//import com.sugarcrm.voodoo.automation.VHook;
//import com.sugarcrm.voodoo.IAutomation;
//import com.sugarcrm.voodoo.Voodoo;

import static org.junit.Assert.assertEquals;


public class VoodooSystemTests {
	protected static Voodoo voodoo;
	
	@BeforeClass
	public static void setupOnce() throws Exception {
		String curWorkDir = System.getProperty("user.dir");
		String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
		String voodooPropsPath = relPropsPath + File.separator + "voodoo.properties";
		
		Properties voodooProps = new Properties();
		voodooProps.load(new FileInputStream(new File(voodooPropsPath)));
		voodoo = Voodoo.getInstance(voodooProps);
		voodoo.start();
	}

	@Test
	public void selectTest() throws Exception {
		VHook dropDownList = new VHook(Strategy.ID, "birthday_month");
		String option = "Sep";
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php?locale=en_US&loxv=v1_WITH_RULE";
		voodoo.go(facebookCreateAccountUrl);
		// 2. Select the option 'Sep' from the 'birthday_month' drop-down menu
		voodoo.select(dropDownList, option);
		// 3. Verify that 'Sep' was actually selected
		String actual = voodoo.getSelected(dropDownList);
		String expected = option;
		assertEquals("Expected option value does not match actual value ~ expected: " + expected + ", actual: " + actual, expected, actual);
	}
	
	@Test
	public void dragNDropTest() throws Exception {
//		voodoo.go("http://www.w3schools.com/html/html5_draganddrop.asp");
//		voodoo.waitFor(new VHook(Strategy.ID, "drag1"));
//		voodoo.dragNDrop(new VHook(Strategy.ID, "drag1"), new VHook(Strategy.ID, "div2"));
		
//		WebDriver driver = new ChromeDriver();
//		driver.get("http://www.w3schools.com/html/html5_draganddrop.asp");
//		Action dragAndDrop = (new Actions(driver)).clickAndHold(driver.findElement(By.id("drag1")))
//			       .moveToElement(driver.findElement(By.id("div2")))
//			       .release(driver.findElement(By.id("drag1")))
//			       .build();
//		voodoo.interact("wait for it...");
//		dragAndDrop.perform();
//		voodoo.interact("wait for it...");
		
//		voodoo.waitFor(new VHook(Strategy.XPATH, "/html/body/div/div/div[4]/div[2]/hr[2]/div[2]/img"));
	}
	
	@Test
	public void getSelectedTest() throws Exception {
		String actual;
		String expected = "Month:"; // Assuming that we know that the current/default option is 'Month:'
		VHook dropDownList = new VHook(Strategy.ID, "birthday_month");
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php?locale=en_US&loxv=v1_WITH_RULE";
		voodoo.go(facebookCreateAccountUrl);
		// 2. Get the current option from the drop-down list
		actual = voodoo.getSelected(dropDownList);
		// 3. Verify that actual value is the expected value
		assertEquals("Expected option value does not match actual value ~ expected: " + expected + ", actual: " + actual, expected, actual);
		
	}
	
	@AfterClass
	public static void cleanupOnce() throws Exception {
		voodoo.stop();
	}
}	
