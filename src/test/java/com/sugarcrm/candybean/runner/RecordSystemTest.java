package com.sugarcrm.candybean.runner;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;

import com.sugarcrm.candybean.examples.AbstractTest;
import com.sugarcrm.candybean.runner.Duration;
import com.sugarcrm.candybean.runner.Record;
import com.sugarcrm.candybean.runner.VTag;
import com.sugarcrm.candybean.runner.VRunner;

/**
 * Show-cases the ability to record failing tests using {@link Record} annotation.
 * @author Shehryar Farooq
 *
 */
@RunWith(VRunner.class)
public class RecordSystemTest extends AbstractTest{
	
	@Before
	public void first() throws Exception {
		iface.start();
		((JavascriptExecutor)iface.wd).executeScript("self.focus()");
	}
	
	@Test
	@Record(duration = Duration.FINAL)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void passedUrlTest() throws Exception {
		String amazonUrl = "http://www.amazon.com/";
		iface.go(amazonUrl);
		assertEquals(amazonUrl, iface.getURL());		
	}
	
	@Test
	@Record(duration = Duration.FINAL)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void failedUrlTest() throws Exception {
		String amazonUrl = "http://www.amazon.com/";
		String yahooUrl = "https://yahoo.com";
		iface.go(amazonUrl);
		iface.go(yahooUrl);
		assertEquals(iface.getURL(), amazonUrl);		
	}
	

	@After
	public void last() throws Exception {
		iface.stop();
	}
	

}
