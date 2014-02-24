package com.sugarcrm.candybean.examples.mobile;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sugarcrm.candybean.examples.AndroidTest;
import com.sugarcrm.candybean.examples.ITest;

public class GeneralizedAndroidTest extends AndroidTest implements ITest{

	public GeneralizedAndroidTest() throws IOException, Exception {
		super();
	}

	@Before
	public void setUp() throws Exception {
		iface.pause(2000);
	}
	
	@After
	public void tearDown() throws Exception {
		iface.stop();
	}
	
    @Test
    public void testActive() throws Exception {
        WebElement text = iface.wd.findElement(By.xpath("//textfield[1]"));
        assertTrue(text.isDisplayed());

        WebElement button = iface.wd.findElement(By.xpath("//button[1]"));
        assertTrue(button.isDisplayed());
    }
    
    @Test
    public void testBasicAlert() throws Exception {
        iface.wd.findElement(By.xpath("//button[2]")).click();
        WebElement acceptButton = iface.wd.findElement(By.xpath("//button[1]"));
        acceptButton.click();
    }

}
