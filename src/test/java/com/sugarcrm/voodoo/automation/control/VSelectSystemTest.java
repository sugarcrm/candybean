package com.sugarcrm.voodoo.automation.control;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import org.junit.Test;

import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VSelect;
import com.sugarcrm.voodoo.configuration.Configuration;

//import com.sugarcrm.voodoo.IAutomation.Strategy;
//import com.sugarcrm.voodoo.automation.VHook;
//import com.sugarcrm.voodoo.IAutomation;
//import com.sugarcrm.voodoo.Voodoo;

import static org.junit.Assert.assertEquals;

public class VSelectSystemTest {
	protected static Voodoo voodoo;
	protected static VInterface iface;
	
	@BeforeClass
	public static void first() throws Exception {
		String curWorkDir = System.getProperty("user.dir");
		String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
		String voodooPropsPath = relPropsPath + File.separator;
		String voodooPropsFilename = System.getProperty("voodoo_prop_filename");
		if (voodooPropsFilename == null) voodooPropsFilename = "voodoo-mac.properties";
		voodooPropsPath += voodooPropsFilename;
		
		Configuration voodooConfig = new Configuration();
		voodooConfig.load(new File(voodooPropsPath));
		voodoo = Voodoo.getInstance(voodooConfig);
		iface = voodoo.getInterface();
		iface.start();
	}

	@Test
	public void checkBoxSelectTest() throws Exception {
		
		// Checking checkbox select 
		String w3Url = "http://www.w3schools.com/html/html_forms.asp";
		iface.go(w3Url);
		VSelect select = iface.getSelect(new VHook(Strategy.XPATH, "//*[@id=\"main\"]/form[4]/input[1]"));
		Assert.assertEquals("Control should not be selected -- selected: " + select.isSelected(), select.isSelected(), false);
		select.select(true);
		Assert.assertEquals("Control should be selected -- selected: " + select.isSelected(), select.isSelected(), true);
		
        // Exception should throw for non-checkbox element
        //VHook nonCheckboxHook = new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[3]/input[1]"); // a radio box
		//voodoo.select(nonCheckboxHook, true);  // yes, verified exception was thrown
        
        // Checking getAttributeValue()
//		VControl control = iface.getControl(new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[1]/input[1]"));
//		String actText = control.getAttribute("type");
//        String expText = "text";
//        Assert.assertEquals("Expected value for the type attribute should match: " + expText, expText, actText);
//        
//		String actSize = control.getAttribute("size");
//        String expSize = "20";
//        Assert.assertEquals("Expected value for the size attribute should match: " + expSize, expSize, actSize);
//        
//		String actName = control.getAttribute("name");
//        String expName = "firstname";
//        Assert.assertEquals("Expected value for the name attribute should match: " + expName, expName, actName);
	}
	
	@Test
	public void selectTest() throws Exception {
		String option = "Sep";
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php?locale=en_US&loxv=v1_WITH_RULE";
		iface.go(facebookCreateAccountUrl);
		VSelect dropDownList = new VSelect(voodoo, iface, new VHook(Strategy.ID, "birthday_month"));
		// 2. Select the option 'Sep' from the 'birthday_month' drop-down menu
		dropDownList.select(option);
		// 3. Verify that 'Sep' was actually selected
		String actual = dropDownList.getSelected();
		String expected = option;
		assertEquals("Expected option value does not match actual value ~ expected: " + expected + ", actual: " + actual, expected, actual);
	}
	
	@Test
	public void getSelectedTest() throws Exception {
		String actual;
		String expected = "Month:"; // Assuming that we know that the current/default option is 'Month:'
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php?locale=en_US&loxv=v1_WITH_RULE";
		iface.go(facebookCreateAccountUrl);
		VSelect dropDownList = iface.getSelect(new VHook(Strategy.ID, "birthday_month"));
		// 2. Get the current option from the drop-down list
		actual = dropDownList.getSelected();
		// 3. Verify that actual value is the expected value
		assertEquals("Expected option value does not match actual value ~ expected: " + expected + ", actual: " + actual, expected, actual);
	}
	
	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}	
