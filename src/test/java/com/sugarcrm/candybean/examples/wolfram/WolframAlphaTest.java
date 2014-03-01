package com.sugarcrm.candybean.examples.wolfram;


import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sugarcrm.candybean.examples.AbstractTest;

/**
 * Wolfram alpha test class defining any tests using the {@link Test } annotation.
 * @author Shehryar Farooq
 *
 */
public class WolframAlphaTest extends AbstractTest{

	/**
	 * Association class containing test operations for this test.
	 */
	private static WolframAlpha wolfram;
	
	
	/**
	 * Any processing to do before executing the tests.
	 * @throws Exception 
	 */
	@Before public void before() throws Exception{
		iface.start();
		wolfram = new WolframAlpha(iface);
	};
	
	/**
	 * Automated addition test that verifies Wolfram Alpha's addition capabilities
	 * @throws Exception 
	 */
	@Test
	public void variousInputsTest() throws Exception{
		for(WolframInputType testType : WolframInputType.values()){
			wolfram.goToLandingPage();
			wolfram.askWolfram(testType.getInput());
			wolfram.waitForResult(2000);
			assertEquals(wolfram.verifyAnswer(testType.getAnswer()),true);
		}
	}
	
	/**
	 * Any processing to do after executing the tests.
	 * @throws Exception 
	 */
	@AfterClass
	public static void last() throws Exception{
		iface.stop();
	};
}