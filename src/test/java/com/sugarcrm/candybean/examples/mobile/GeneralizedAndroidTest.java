package com.sugarcrm.candybean.examples.mobile;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public class GeneralizedAndroidTest {

	public static WebDriverInterface iface;

	@BeforeClass
	public static void beforeClass() throws CandybeanException{
		Candybean candybean = Candybean.getInstance();
		AutomationInterfaceBuilder builder = candybean.getAIB(GeneralizedAndroidTest.class);
		builder.setType(Type.ANDROID);
		iface = builder.build();
	}
	
	@Before
	public void setUp() throws CandybeanException {
		iface.start();
		iface.pause(2000);
	}
	
	@After
	public void tearDown() throws CandybeanException {
		iface.stop();
	}
	
    @Test
    public void testActive() throws CandybeanException {
        WebElement text = iface.wd.findElement(By.xpath("//textfield[1]"));
        assertTrue(text.isDisplayed());

        WebElement button = iface.wd.findElement(By.xpath("//button[1]"));
        assertTrue(button.isDisplayed());
    }
    
    @Test
    public void testBasicAlert() throws CandybeanException {
        iface.wd.findElement(By.xpath("//button[2]")).click();
        WebElement acceptButton = iface.wd.findElement(By.xpath("//button[1]"));
        acceptButton.click();
    }

}
