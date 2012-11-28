package com.sugarcrm.voodoo.automation;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VSelect;

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
		voodoo.auto.start();
	}

	@Test
	public void selectTest() throws Exception {
		
		// Checking checkbox select 
		String w3Url = "http://www.w3schools.com/html/html_forms.asp";
		voodoo.auto.go(w3Url);
		voodoo.pause(2000);
		
		VSelect select = new VSelect(new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[4]/input[1]"), voodoo.auto);
		select.select(true);
        voodoo.pause(5000);  // pause for manual inspection
		select.select(false);
        voodoo.pause(5000); 
		select.select(true);
        voodoo.pause(2000);  
        
        // Exception should throw for non-checkbox element
        //VHook nonCheckboxHook = new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[3]/input[1]"); // a radio box
		//voodoo.select(nonCheckboxHook, true);  // yes, verified exception was thrown
        
        // Checking getAttributeValue()
		VControl control = new VControl(new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[1]/input[1]"), voodoo.auto);
		String actText = control.getAttribute("type");
        String expText = "text";
        Assert.assertEquals("Expected value for the type attribute should match: " + expText, expText, actText);
        
		String actSize = control.getAttribute("size");
        String expSize = "20";
        Assert.assertEquals("Expected value for the size attribute should match: " + expSize, expSize, actSize);
        
		String actName = control.getAttribute("name");
        String expName = "firstname";
        Assert.assertEquals("Expected value for the name attribute should match: " + expName, expName, actName);
	}
	
	@After
	public void cleanup() throws Exception {
		voodoo.auto.stop();
	}
}	
