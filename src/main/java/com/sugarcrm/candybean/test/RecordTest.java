package com.sugarcrm.candybean.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sugarcrm.candybean.examples.AbstractTest;
import com.sugarcrm.candybean.runner.Duration;
import com.sugarcrm.candybean.runner.Record;
import com.sugarcrm.candybean.runner.VTag;
import com.sugarcrm.candybean.runner.VRunner;

@RunWith(VRunner.class)
public class RecordTest extends AbstractTest {
	
	@Test
	@Record(duration = Duration.FINAL)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void passedUrlTest() throws Exception {
		iface.start();
		String amazonUrl = "http://www.amazon.com/";
		iface.go(amazonUrl);
		assertEquals(amazonUrl, iface.getURL());		
	}
	
	@Test
	@Record(duration = Duration.FINAL)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void failedUrlTest() throws Exception {
		iface.start();
		String amazonUrl = "http://www.amazon.com/";
		String yahooUrl = "https://yahoo.com";
		iface.go(amazonUrl);
		iface.go(yahooUrl);
		assertEquals(iface.getURL(), amazonUrl);		
	}
	

}
