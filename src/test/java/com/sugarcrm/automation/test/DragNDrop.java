package com.sugarcrm.automation.test;

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

// Drag and drop is not supported for HTML5. Same issue reported by other users as well.
// The following test ran fine using chrome 22.0.1229.94, selenium driver 2.19.
// However, it does not work on firefox. (Tried firefox 10, 11, 14, with selenium driver 2.19, 2.22, 2.25)
// For Sugar browser, drag and drop seemed to work fine.
//
public class DragNDrop {
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
		
		String w3Url = "http://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=6&ved=0CDoQFjAF&url=http%3A%2F%2Ftool-man.org%2Fexamples%2Fsorting.html&ei=nBGLUKi8CcGmigLah4CADg&usg=AFQjCNGL-HryUxMBRKn9gEM0F1xE_NNNyQ";
		voodoo.auto.go(w3Url);
		voodoo.pause(2000);
		VControl imgControl = new VControl(new VHook(Strategy.XPATH, "/html/body/ul[2]/li"), voodoo.auto);
		VControl targetControl = new VControl(new VHook(Strategy.XPATH, "/html/body/ul[2]/li[2]"), voodoo.auto);
   		imgControl.dragNDrop(targetControl);
        voodoo.pause(3000);  // pause for manual inspection
   		
   		// Verify draggable has been moved to new location
   		String actItemid = targetControl.getAttribute("itemid");
   		String expItemid = "1";
        Assert.assertEquals("Expected value for the itemid attribute should match: " + expItemid, expItemid, actItemid);
	}
	
	@After
	public void cleanup() throws Exception {
		voodoo.auto.stop();
	}
}	
