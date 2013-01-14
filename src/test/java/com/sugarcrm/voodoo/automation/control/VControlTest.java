package com.sugarcrm.voodoo.automation.control;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import org.junit.Test;

import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VSelect;

//import com.sugarcrm.voodoo.IAutomation.Strategy;
//import com.sugarcrm.voodoo.automation.VHook;
//import com.sugarcrm.voodoo.IAutomation;
//import com.sugarcrm.voodoo.Voodoo;

import static org.junit.Assert.assertEquals;


public class VControlTest {
	protected static Voodoo voodoo;
	protected static VInterface iface;
	
	@BeforeClass
	public static void setupOnce() throws Exception {
		String curWorkDir = System.getProperty("user.dir");
		String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
		String voodooPropsPath = relPropsPath + File.separator + "voodoo.properties";
		
		Properties voodooProps = new Properties();
		voodooProps.load(new FileInputStream(new File(voodooPropsPath)));
		voodoo = Voodoo.getInstance(voodooProps);
		iface = voodoo.getInterface();
		iface.start();
	}

	@Test 
	// Can be verified by looking at the website checkbox (Double click is performed 3 times)
	public void testDoubleClickTest() throws Exception {
		String w3Url = "http://www.w3schools.com/html/html_forms.asp";
		iface.go(w3Url);
		//Checkbox control
		VControl checkboxControl = iface.getControl(new VHook(Strategy.XPATH, "/html/body/div[1]/div/div[4]/div[2]/form[4]/input[1]"));
		// DoubleClick on a Checkbox
		checkboxControl.scroll();
		iface.pause(2000);
		checkboxControl.doubleClick();
		iface.pause(2000); 
		checkboxControl.doubleClick();
		iface.pause(2000); 
		checkboxControl.doubleClick();
		iface.pause(2000); 
	}
	
	
	@Test
	public void dragNDropTest() throws Exception {
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
        Assert.assertEquals("Expected value for the itemid attribute should match: " + expItemid, expItemid, actItemid);
	}
	
	@Test
	public void dragNDropTest2() throws Exception {
//		voodoo.go("http://www.w3schools.com/html/html5_draganddrop.asp");
//		voodoo.halt(new VHook(Strategy.ID, "drag1"));
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
		
//		voodoo.halt(new VHook(Strategy.XPATH, "/html/body/div/div/div[4]/div[2]/hr[2]/div[2]/img"));
	}
	
	@AfterClass
	public static void cleanupOnce() throws Exception {
		iface.stop();
	}
}	
