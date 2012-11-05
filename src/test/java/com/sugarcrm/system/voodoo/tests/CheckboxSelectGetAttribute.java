package com.sugarcrm.system.voodoo.tests;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.control.VHook;

public class CheckboxSelectGetAttribute {
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
		
		// Checking checkbox select 
		String w3Url = "http://www.w3schools.com/html/html_forms.asp";
		voodoo.go(w3Url);
		voodoo.pause(2000);
		
		VHook hook = new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[4]/input[1]");
		voodoo.select(hook, true);
        voodoo.pause(5000);  // pause for manual inspection
		voodoo.select(hook, false);
        voodoo.pause(5000); 
		voodoo.select(hook, true);
        voodoo.pause(2000);  
        
        // Exception should throw for non-checkbox element
        //VHook nonCheckboxHook = new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[3]/input[1]"); // a radio box
		//voodoo.select(nonCheckboxHook, true);  // yes, verified exception was thrown
        
        // Checking getAttributeValue()
		VHook attribHook = new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[1]/input[1]");
		String actText = voodoo.getAttributeValue(attribHook, "type");
        String expText = "text";
        Assert.assertEquals("Expected value for the type attribute should match: " + expText, expText, actText);
        
		String actSize = voodoo.getAttributeValue(attribHook, "size");
        String expSize = "20";
        Assert.assertEquals("Expected value for the size attribute should match: " + expSize, expSize, actSize);
        
		String actName = voodoo.getAttributeValue(attribHook, "name");
        String expName = "firstname";
        Assert.assertEquals("Expected value for the name attribute should match: " + expName, expName, actName);
	}
	
	@After
	public void cleanup() throws Exception {
		voodoo.stop();
	}
}	
