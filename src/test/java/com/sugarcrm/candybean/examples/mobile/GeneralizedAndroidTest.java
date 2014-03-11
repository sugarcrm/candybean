package com.sugarcrm.candybean.examples.mobile;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.test.AndroidTest;

public class GeneralizedAndroidTest extends AndroidTest {


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
