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

import org.junit.Test;

import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.control.VControl;
import com.sugarcrm.candybean.automation.control.VHook;
import com.sugarcrm.candybean.automation.control.VSelect;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.configuration.Configuration;

//import com.sugarcrm.voodoo.IAutomation.Strategy;
//import com.sugarcrm.voodoo.automation.VHook;
//import com.sugarcrm.voodoo.IAutomation;
//import com.sugarcrm.voodoo.Voodoo;

import static org.junit.Assert.assertEquals;

public class VSelectSystemTest {
	protected static Candybean voodoo;
	protected static VInterface iface;
	
	@BeforeClass
	public static void first() throws Exception {
		String curWorkDir = System.getProperty("user.dir");
		String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
		String voodooPropsPath = relPropsPath + File.separator;
		String voodooPropsFilename = System.getProperty("voodoo_prop_filename");
		if (voodooPropsFilename == null) voodooPropsFilename = "candybean-mac.properties";
		voodooPropsPath += voodooPropsFilename;
		
		Configuration voodooConfig = new Configuration();
		voodooConfig.load(new File(voodooPropsPath));
		voodoo = Candybean.getInstance(voodooConfig);
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
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php";
		iface.go(facebookCreateAccountUrl);
		VSelect dropDownList = new VSelect(voodoo, iface, new VHook(Strategy.ID, "month"));
		// 2. Select the option 'Sep' from the 'birthday_month' drop-down menu
		dropDownList.select(option);
		// 3. Verify that 'Sep' was actually selected
		String actual = dropDownList.getSelected();
		String expected = option;
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getSelectedTest() throws Exception {
		String actual;
		String expected = "Month"; // Assuming that we know that the current/default option is 'Month:'
		// 1. navigate to Facebook create account page
		String facebookCreateAccountUrl = "https://www.facebook.com/r.php";
		iface.go(facebookCreateAccountUrl);
		VSelect dropDownList = iface.getSelect(new VHook(Strategy.ID, "month"));
		// 2. Get the current option from the drop-down list
		actual = dropDownList.getSelected();
		// 3. Verify that actual value is the expected value
		Assert.assertEquals(expected, actual);
	}
	
	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}	
