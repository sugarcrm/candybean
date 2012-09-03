package com.sugarcrm.voodoo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

//@Inherited
//@RunWith(Parameterized.class)
public abstract class VoodooTest {
	
	@BeforeClass
	public static void setupOnce() throws Exception {}

	@Before
	public void setup() throws Exception {}

	@Test
	public void execute() throws Exception {}

	@After
	public void cleanup() throws Exception {}

	@AfterClass
	public static void cleanupOnce() {}
	
//	@Parameters
//    public static Collection<Object[]> browsers() throws Exception {
//    	Collection<Object[]> browsers = voodoo.getBrowsers();
//    	for (Object[] browserContainer : browsers) {
//    		WebDriver browser = (WebDriver)browserContainer[0];
//    		voodoo.initBrowser(browser);
//    	}
//		return browsers;
//    }
}
