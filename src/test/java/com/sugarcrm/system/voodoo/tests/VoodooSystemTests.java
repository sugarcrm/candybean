package com.sugarcrm.system.voodoo.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import org.junit.Test;

import com.sugarcrm.voodoo.IAutomation.Strategy;
import com.sugarcrm.voodoo.Voodoo;
import com.sugarcrm.voodoo.automation.VHook;

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
	
}	
